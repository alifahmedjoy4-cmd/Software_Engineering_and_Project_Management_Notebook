<div align="center">

# 🚀 CampusBoot

### A tiny Spring-Boot-style framework built with **Custom Annotations** and **Java Reflection**

![Java](https://img.shields.io/badge/Java-11%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Reflection](https://img.shields.io/badge/Java-Reflection-blue?style=for-the-badge)
![Libraries](https://img.shields.io/badge/External%20Libraries-None-success?style=for-the-badge)
![Score](https://img.shields.io/badge/Expected%20Score-10%2F10-brightgreen?style=for-the-badge)

**Student:** Alif Ahmed Joy &nbsp;|&nbsp; **ID:** IT24015

**Supervised By**

**Dr. Ziaur Rahman**

Professor

Department of ICT, MBSTU.

</div>

---

## 📑 Contents

1. 📘 [The Assignment (Sir's Brief)](#1--the-assignment-sirs-brief)
2. 👤 [My Bio Data](#2--my-bio-data)
3. 🧩 [My Work, Topic by Topic (with marks)](#3--my-work-topic-by-topic)
4. 🗺️ [Annotation Usage Map](#4--annotation-usage-map)
5. 🖥️ [Program Output](#5--program-output)

---

# 1. 📘 The Assignment (Sir's Brief)

### 1.1 Title
**Build "CampusBoot" with Custom Annotations** — Java Reflection and Custom Annotations: how Spring Boot works under the hood.

- ⏱ Time: 3 hours
- 🎯 Total score: 10 points
- ☕ Language: Java 11+ (no external libraries)

### 1.2 The Story
Every project in the Campus IT Lab repeats the same boilerplate: creating objects by hand, reading config values, validating data and wiring URLs to methods. Spring Boot solves this with annotations, but the lab cannot use Spring, so the task is to build a tiny framework called **CampusBoot**.

> The goal is to prove that `@Component`, `@Value`, `@GetMapping` and friends are not magic. They are just annotations plus reflection.

### 1.3 Learning Outcomes
- Declare custom annotations using `@Target`, `@Retention` and element defaults.
- Use Reflection (`Class`, `Field`, `Method`) to read annotations at runtime.
- Build a mini IoC container, a value injector, a validator and a router.
- Explain how Spring Boot applies the same ideas.

### 1.4 The Four Annotations Required
- **`@Component`** (on a class): `String value() default ""`. Marks a class to be created and managed by the container.
- **`@Value`** (on a field): `String value()`. Injects a config value into the field.
- **`@Range`** (on a field): `int min()`, `int max()`, `String message() default "Value out of range"`. Validates an int field.
- **`@Route`** (on a method): `String path()`. Maps a URL path to a method.

### 1.5 What Had to Be Built
- **Container:** `scan(...)`, `injectValues(...)`, `getBean(...)`
- **Validator:** `static List<String> validate(Object obj)`
- **Router:** `String dispatch(Container c, String path)`
- **Reflection Questions:** 3 short answers saved in `answers.txt`
- **Starter code (not modified):** `GradeController`, `ReportPrinter`, `Student`, `Main`

### 1.6 Marking Scheme (10 points)
- **A. Annotations (2):** correct `@Target` and `@Retention` (1); correct elements and defaults (1)
- **B. Container (3):** scan and bean naming (1); `@Value` injection with reflection (1); `int` type conversion and skip handling (1)
- **C. Validator (2):** reads `@Range` correctly (1); correct message format and default message (1)
- **D. Router (2):** finds and invokes `@Route` method (1); handles 404 case (1)
- **E. Reflection Questions (1):** three reflection answers, clear and correct

### 1.7 Submission Rules
- One `.zip` named `StudentID_CampusBoot.zip` containing all `.java` files and `answers.txt`.
- The program must compile and match the Sample Output.
- Code that does not compile gets a maximum of 3 points.

---

# 2. 👤 Bio Data

- **Name:** Alif Ahmed Joy
- **Student ID:** IT24015
- **Supervised By:** Dr. Ziaur Rahman, Professor, Department of ICT, MBSTU.
- **Project:** CampusBoot
- **Submission file:** `IT24015_CampusBoot.zip`
- **Files I wrote:** `Component.java`, `Value.java`, `Range.java`, `Route.java`, `Container.java`, `Validator.java`, `Router.java`, `answers.txt`
- **Files given by sir (unchanged):** `GradeController.java`, `ReportPrinter.java`, `Student.java`, `Main.java`

---

# 3. 🧩 My Work, Topic by Topic

Each topic below shows **how many marks it is worth**, **which file and lines** contain the work, and the **actual code** that proves it.

---

## 🏷️ 3.1 Annotations &nbsp; `2 marks`

Marks split as in the rubric: correct `@Target` and `@Retention` (1) and correct elements and defaults (1).

### 3.1.1 ⚙️ Correct `@Target` and `@Retention` &nbsp; `1 mark`

**What I did:** Every annotation has `@Retention(RetentionPolicy.RUNTIME)` so reflection can see it while the program runs. Each one also has the correct `@Target`: `TYPE` for `@Component`, `FIELD` for `@Value` and `@Range`, `METHOD` for `@Route`.

📍 **`Component.java`, lines 3–4** — @Component on classes
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
```

📍 **`Value.java`, lines 3–4** — @Value on fields
```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
```

📍 **`Range.java`, lines 3–4** — @Range on fields
```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
```

📍 **`Route.java`, lines 3–4** — @Route on methods
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
```

---

### 3.1.2 🧱 Correct Elements and Defaults &nbsp; `1 mark`

**What I did:** Each annotation has exactly the elements from the specification. `@Component` has `value()` defaulting to `""`, and `@Range` has `min()`, `max()` and `message()` defaulting to `"Value out of range"`.

**Example of use** (`GradeController.java` uses `@Component` with a name, `Student.java` uses `@Range` with and without a message):
```java
@Component("gradeController")
```
```java
    @Range(min = 0, max = 100, message = "Score must be between 0 and 100")
    private int score;

    @Range(min = 1, max = 12)
```

📍 **`Component.java`, lines 5–7**
```java
public @interface Component {
    String value() default "";
}
```

📍 **`Range.java`, lines 5–9** — default message lives here
```java
public @interface Range {
    int min();
    int max();
    String message() default "Value out of range";
}
```

> 📝 **Note on `@Route`:** the spec says `path()`, but the starter code writes `@Route("/hello")`. Java only allows that shorthand for an element named `value()`. So `Route.java` has **both** `path()` and `value()`, and the Router accepts either.

📍 **`Route.java`, lines 5–9**
```java
public @interface Route {
    // Spec asks for path(); starter code uses the shorthand @Route("/hello"),
    // which Java only allows through an element named value(). Both are supported.
    String path() default "";
    String value() default "";
```

---

---

## 📦 3.2 Container (IoC + Value Injection) &nbsp; `3 marks`

Marks split as in the rubric: scan and bean naming (1), `@Value` injection with reflection (1), and `int` type conversion with skip handling (1). All of this is in `Container.java`.

### 3.2.1 📦 Scan and Bean Naming &nbsp; `1 mark`

**What I did:** `scan()` checks each class with `isAnnotationPresent(Component.class)`. Classes without it are skipped and a message is printed. For the rest, the bean name is `value()`, or if it is empty, the simple class name with a lowercase first letter. Then the object is created through the no-arg constructor and stored in a `Map<String,Object>`.

📍 **`Container.java`, lines 8–28** — scan() method
```java
    public void scan(Class<?>... classes) {
        for (Class<?> c : classes) {
            if (!c.isAnnotationPresent(Component.class)) {
                System.out.println("[Container] Skipped: " + c.getSimpleName() + " (no @Component)");
                continue;
            }
            try {
                Component comp = c.getAnnotation(Component.class);
                String name = comp.value();
                if (name.isEmpty()) {
                    String s = c.getSimpleName();
                    name = Character.toLowerCase(s.charAt(0)) + s.substring(1);
                }
                Object instance = c.getDeclaredConstructor().newInstance();
                beans.put(name, instance);
                System.out.println("[Container] Registered: " + name + " (" + c.getSimpleName() + ")");
            } catch (Exception e) {
                System.out.println("[Container] Failed to create: " + c.getSimpleName() + " - " + e);
            }
        }
    }
```

**Example:** `@Component("gradeController")` gives the bean name `gradeController`. If it had no name, the bean would be called `gradeController` anyway (class name with lowercase first letter). `ReportPrinter` has no annotation.
```
[Container] Registered: gradeController (GradeController)
[Container] Skipped: ReportPrinter (no @Component)
```

---

### 3.2.2 💉 `@Value` Injection with Reflection &nbsp; `1 mark`

**What I did:** `injectValues()` loops over every bean and over its declared fields. When a field has `@Value`, it reads the key from the annotation, looks it up in the config map, calls `setAccessible(true)` so private fields can be written, and sets the value with `f.set(...)`.

📍 **`Container.java`, lines 30–36** — finding @Value fields and the config value
```java
    public void injectValues(Map<String, String> config) {
        for (Object bean : beans.values()) {
            for (Field f : bean.getClass().getDeclaredFields()) {
                Value v = f.getAnnotation(Value.class);
                if (v == null) continue;
                String raw = config.get(v.value());
                if (raw == null) continue;
```

📍 **`Container.java`, lines 37–45** — setAccessible(true) and f.set(...)
```java
                try {
                    f.setAccessible(true);
                    if (f.getType() == int.class || f.getType() == Integer.class) {
                        f.set(bean, Integer.parseInt(raw.trim()));
                    } else {
                        f.set(bean, raw);
                    }
                    System.out.println("[Container] Injected: " + v.value() + " -> "
                            + bean.getClass().getSimpleName() + "." + f.getName());
```

**Example:** the field is private and has no setter, yet it gets its value.
```java
public class GradeController {
    @Value("app.name")     private String appName;
```
```
[Container] Injected: app.name -> GradeController.appName
[Container] Injected: app.passMark -> GradeController.passMark
```

---

### 3.2.3 🔢 `int` Type Conversion and Skip Handling &nbsp; `1 mark`

**What I did:** Config values are always Strings. If the field type is `int`, the value is converted with `Integer.parseInt`, otherwise it is set as a String. If a key is missing from the config, that field is skipped instead of crashing. A `try/catch` protects against bad values.

📍 **`Container.java`, lines 35–36** — skip when the key is missing
```java
                String raw = config.get(v.value());
                if (raw == null) continue;
```

📍 **`Container.java`, lines 39–43** — int conversion
```java
                    if (f.getType() == int.class || f.getType() == Integer.class) {
                        f.set(bean, Integer.parseInt(raw.trim()));
                    } else {
                        f.set(bean, raw);
                    }
```

📍 **`Container.java`, lines 46–48** — safe error handling
```java
                } catch (Exception e) {
                    System.out.println("[Container] Injection failed: " + f.getName() + " - " + e);
                }
```

**Example:** config has `"app.passMark" = "40"` (a String), but the field is `int passMark`, so it becomes the number `40`. The router later returns `Pass mark is 40`. If the config had no `app.passMark` key, the field would simply be skipped.

Also `getBean(String name)` is implemented here: 📍 **`Container.java`, lines 53–55**

---

---

## ✅ 3.3 Validator &nbsp; `2 marks`

Marks split as in the rubric: reads `@Range` correctly (1) and correct message format with the default message (1). All of this is in `Validator.java`.

### 3.3.1 ✅ Reads `@Range` Correctly &nbsp; `1 mark`

**What I did:** `Validator.validate()` loops over all fields of the object, reads `@Range` from each, makes the private field readable with `setAccessible(true)`, reads its `int` value with `getInt`, and compares it with `min()` and `max()`.

📍 **`Validator.java`, lines 8–16** — validate() method
```java
        for (Field f : obj.getClass().getDeclaredFields()) {
            Range r = f.getAnnotation(Range.class);
            if (r == null) continue;
            try {
                f.setAccessible(true);
                int value = f.getInt(obj);
                if (value < r.min() || value > r.max()) {
                    violations.add(f.getName() + " - " + r.message() + " (actual: " + value + ")");
                }
```

The annotation itself is used on the `Student` class (given by sir):
```java
public class Student {
    @Range(min = 0, max = 100, message = "Score must be between 0 and 100")
    private int score;

    @Range(min = 1, max = 12)
    private int semester;
```

---

### 3.3.2 📝 Message Format and Default Message &nbsp; `1 mark`

**What I did:** The message follows the exact format `fieldName - message (actual: value)`. It uses `r.message()`, so `score` shows its custom message and `semester` automatically shows the default `"Value out of range"` from `Range.java`. When all fields are valid, an empty list is returned.

📍 **`Validator.java`, lines 15–15** — message format
```java
                    violations.add(f.getName() + " - " + r.message() + " (actual: " + value + ")");
```

📍 **`Validator.java`, lines 21–21** — empty list when everything is valid
```java
        return violations;
```

**Example:** `new Student(120, 5)` has `score = 120`, which is above `max = 100`. `new Student(85, 5)` is valid, so `Validator` returns an empty list and `Main` prints `OK`. `new Student(70, 15)` has `semester = 15`, above `max = 12`, and uses the default message.
```
VIOLATION: score - Score must be between 0 and 100 (actual: 120)
VIOLATION: semester - Value out of range (actual: 15)
```

---

---

## 🧭 3.4 Router &nbsp; `2 marks`

Marks split as in the rubric: finds and invokes the `@Route` method (1) and handles the 404 case (1). All of this is in `Router.java`.

### 3.4.1 🧭 Finds and Invokes the `@Route` Method &nbsp; `1 mark`

**What I did:** `Router.dispatch()` loops over every bean in the container and every method of each bean. For each method it reads `@Route` and compares the path with the request path. When they match, it calls `m.invoke(bean)` and returns the `String` result.

📍 **`Router.java`, lines 8–19** — dispatch() method
```java
    public static String dispatch(Container c, String path) {
        for (Object bean : c.getBeans()) {
            for (Method m : bean.getClass().getDeclaredMethods()) {
                Route r = m.getAnnotation(Route.class);
                if (r != null && routePath(r).equals(path)) {
                    try {
                        m.setAccessible(true);
                        return (String) m.invoke(bean);
                    } catch (Exception e) {
                        return "500 Error: " + e;
                    }
                }
```

**Example:** the request `/hello` matches `@Route("/hello")` on `hello()`, which returns text using the injected `appName`.
```java

    @Route("/hello")
    public String hello() { return "Welcome to " + appName; }

    @Route("/status")
```
```
[Router] /hello -> Welcome to CampusBoot
[Router] /status -> Pass mark is 40
```

---

### 3.4.2 🚫 Handles the 404 Case &nbsp; `1 mark`

**What I did:** If no bean has a method with the requested path, the loops finish and the method returns `"404 Not Found: " + path`.

📍 **`Router.java`, lines 22–22**
```java
        return "404 Not Found: " + path;
```

Result: `[Router] /unknown -> 404 Not Found: /unknown`

---

## 💬 3.5 Reflection Questions &nbsp; `1 mark`

Three clear and correct reflection answers, saved in `answers.txt` (0.33 marks each).

The answers are copied here from `answers.txt`.

**Q1. What would happen if we forgot @Retention(RetentionPolicy.RUNTIME)?**

> Without it, the annotation gets the default retention, CLASS, so it is kept in the .class file but is not available to the JVM at runtime. Methods like isAnnotationPresent() and getAnnotation() would return false/null, so the Container would skip every class, no values would be injected, the Validator would find no @Range fields and the Router would always return 404. The framework would compile fine but do nothing.

**Q2. Mapping to Spring Boot and one missing feature.**

> @Component maps to Spring's @Component (or @Service/@Controller), @Value maps to Spring's @Value("${key}"), @Range maps to Bean Validation annotations such as @Min/@Max (jakarta.validation), and @Route maps to @GetMapping/@RequestMapping. Real Spring also does dependency injection between beans (@Autowired, constructor injection), which our mini-framework does not; it also has singleton/prototype scopes, proxies/AOP and a real web server, none of which we have.

**Q3. Why is setAccessible(true) both powerful and risky?**

> It is powerful because it lets reflection read and write private fields and call private methods, which is how frameworks inject values without needing setters. It is risky because it breaks encapsulation: code can change internal state and bypass validation or security checks, which makes programs harder to maintain and can be abused. In newer Java versions with modules it can also fail with InaccessibleObjectException.

---

# 4. 🗺️ Annotation Usage Map

Where each annotation is **defined**, **used** and **read by reflection**.

### `@Component`
- 🔧 Defined in: `Component.java`
- 📌 Used in: `GradeController.java` (class level)
- 🔍 Read in: `Container.java`, `scan()`

```java
@Component("gradeController")
```

### `@Value`
- 🔧 Defined in: `Value.java`
- 📌 Used in: `GradeController.java` on `appName` and `passMark`
- 🔍 Read in: `Container.java`, `injectValues()`

```java
public class GradeController {
    @Value("app.name")     private String appName;
```

### `@Range`
- 🔧 Defined in: `Range.java`
- 📌 Used in: `Student.java` on `score` (with custom message) and `semester` (default message)
- 🔍 Read in: `Validator.java`, `validate()`

```java
    @Range(min = 0, max = 100, message = "Score must be between 0 and 100")
    private int score;

    @Range(min = 1, max = 12)
    private int semester;
```

### `@Route`
- 🔧 Defined in: `Route.java`
- 📌 Used in: `GradeController.java` on `hello()` and `status()`
- 🔍 Read in: `Router.java`, `dispatch()`

```java

    @Route("/hello")
    public String hello() { return "Welcome to " + appName; }

    @Route("/status")
```

### No annotation on purpose
`ReportPrinter.java` has no annotation, so the container prints `Skipped`.

---

# 5. 🖥️ Program Output

Compile and run:
```
javac *.java
java Main
```

<div align="center">

<img src="Output.png" alt="CampusBoot program output" width="760">

</div>

The output matches the Sample Output given by sir line by line.

---
