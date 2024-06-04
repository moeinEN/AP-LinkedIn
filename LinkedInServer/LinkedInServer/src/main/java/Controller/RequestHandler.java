package Controller;

import Database.DatabaseQueryController;
import Model.Messages;
import Model.RegisterCredentials;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class RequestHandler {
    public static void registerHandler(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {

            RegisterCredentials registerCredentials = null;
            try (InputStream requestBody = exchange.getRequestBody();
                 InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                Gson gson = new Gson();
                registerCredentials = gson.fromJson(reader, RegisterCredentials.class);

            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, -1); // 400 Bad Request
                return;
            }

            Messages signUpMessage = Controllers.SignUpController.validateUserData(registerCredentials.getEmail(), registerCredentials.getPassword(), registerCredentials.getConfirmationPassword(), registerCredentials.getUsername());
            if(signUpMessage == Messages.SUCCESS) {
                signUpMessage = DatabaseQueryController.addUser(registerCredentials.getUsername(), registerCredentials.getPassword(), registerCredentials.getEmail());
            }

            byte[] responseBytes = signUpMessage.toByte("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length); // use the actual length of the response body

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            byte[] response = Messages.METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(405, response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
}
