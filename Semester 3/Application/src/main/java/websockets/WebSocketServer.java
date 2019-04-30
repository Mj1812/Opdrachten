package websockets;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import restapi.RestAuthenticationClient;
import websockets.sockets.ServerSocket;

import javax.websocket.server.ServerContainer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebSocketServer {
    public static void main(String[] args) {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8095);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        try {
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
            wscontainer.addEndpoint(ServerSocket.class);
            server.start();
            server.join();
            server.wait(10000);
            server.stop();
        } catch (Exception e) {
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
