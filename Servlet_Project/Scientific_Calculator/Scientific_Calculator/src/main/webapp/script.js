const display = document.getElementById("display");
const message = document.getElementById("message");

// Number input
function number(value) {

    if (value === ".") {

        const currentNumber = display.value.split(/[+\-*/^%]/).pop();

        if (currentNumber.includes(".")) {
            return;
        }

        if (currentNumber === "") {
            display.value += "0";
        }
    }

    display.value += value;

    if (message) {
        message.textContent = "";
    }
}

// Clear display
function clearDisplay() {

    display.value = "";

    if (message) {
        message.textContent = "";
    }
}

// Delete last character
function deleteLast() {

    display.value = display.value.slice(0, -1);
}

// Send calculation to Java Servlet
async function sendCalculation(operation, a, b = "") {

    if (message) {
        message.textContent = "Calculating...";
    }

    const data = new URLSearchParams();

    data.append("operation", operation);
    data.append("a", a);

    if (b !== "") {
        data.append("b", b);
    }

    try {

        const response = await fetch("calculator", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: data.toString()
        });

        const result = await response.json();

        if (!response.ok || !result.success) {
            throw new Error(result.error || "Calculation failed");
        }

        display.value = result.result;

        if (message) {
            message.textContent = "Calculation successful!";
        }

    } catch (error) {

        display.value = "";

        if (message) {
            message.textContent = error.message;
        }

        console.error("Calculator error:", error);
    }
}

// Scientific calculator functions
function scientific(operation) {

    const value = display.value.trim();

    if (value === "" || !Number.isFinite(Number(value))) {

        if (message) {
            message.textContent = "Enter a valid number first";
        }

        return;
    }

    sendCalculation(operation, value);
}

// Main calculation function
function calculate() {

    const expression = display.value.trim();

    if (!expression) {

        if (message) {
            message.textContent = "Please enter a calculation";
        }

        return;
    }

    // Percentage
    if (/^-?\d+(?:\.\d+)?%$/.test(expression)) {

        sendCalculation("percent", expression.slice(0, -1));
        return;
    }

    // Decimal number calculation
    const match = expression.match(
        /^(-?\d+(?:\.\d+)?)\s*([+\-*/^])\s*(-?\d+(?:\.\d+)?)$/
    );

    if (!match) {

        if (message) {
            message.textContent =
                "Invalid expression. Example: 10.5+5.2";
        }

        return;
    }

    const a = match[1];
    const operator = match[2];
    const b = match[3];

    const operations = {
        "+": "add",
        "-": "subtract",
        "*": "multiply",
        "/": "divide",
        "^": "power"
    };

    sendCalculation(operations[operator], a, b);
}
