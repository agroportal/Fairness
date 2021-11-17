package fr.lirmm.fairness.assessment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class ResponseController {


    private final HttpServletResponse servletResponse;
    private JsonObject response;
    public ResponseController(HttpServletResponse resp) {
        this.servletResponse = resp;
        this.response= new JsonObject();
    }

    private JsonObject getStatus(boolean success, String request, long time, String message) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new GsonBuilder().create();
        jsonObject.add("request", gson.toJsonTree(request));
        jsonObject.add("success", gson.toJsonTree(success));
        if (message != null && !message.isEmpty()) {
            jsonObject.add("message", gson.toJsonTree(message));
        }
        jsonObject.add("executionTime", gson.toJsonTree(time));
        return jsonObject;
    }

    public void respond(boolean success , String requestURI , long startTime, String message){

        PrintWriter out;

        this.response.add("status", getStatus(success, requestURI, System.currentTimeMillis() - startTime, message));

        try {
            out = servletResponse.getWriter();
            servletResponse.setContentType("application/json");
            servletResponse.setCharacterEncoding("UTF-8");
            out.print(this.response.toString());
            out.flush();
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe("JSON RESPONSE ERROR : " + e.getMessage());
        }

    }

    public JsonObject getResponse() {
        return response;
    }
}
