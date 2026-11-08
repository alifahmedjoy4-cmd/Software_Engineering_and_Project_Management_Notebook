package bd.mbstu.planner.util;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/** Small helper to write JSON responses from servlets. */
public final class Json {
    private static final Gson GSON = new Gson();

    private Json() { }

    public static void write(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(GSON.toJson(body));
    }

    public static void error(HttpServletResponse resp, int status, String message) throws IOException {
        write(resp, status, Map.of("error", message));
    }
}
