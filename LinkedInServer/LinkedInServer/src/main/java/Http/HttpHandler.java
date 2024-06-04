package Http;

import Controller.RequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class HttpHandler {
    public static HttpServer makeConnectionPoint(String ip, int port) throws IOException {
        return HttpServer.create(new InetSocketAddress(ip, port), 0);
    }

    public static void createContext (HttpServer httpServer) throws IOException {

        HttpServer server = httpServer;
        server.createContext("/user/register", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                RequestHandler.registerHandler(exchange);
            }
        });

        server.createContext("/login", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.loginHandler(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Start the server
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }
}
