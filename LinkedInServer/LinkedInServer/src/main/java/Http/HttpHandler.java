package Http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpHandler {
    public static HttpServer makeConnectionPoint(String ip, int port) throws IOException {
        return HttpServer.create(new InetSocketAddress(ip, port), 0);
    }
}
