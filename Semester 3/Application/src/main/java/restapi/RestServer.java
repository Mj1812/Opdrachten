package restapi;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RestServer {
    //region Constructor
    public static void main(String[] args) {
        ResourceConfig config = new ResourceConfig();
        config.packages("restapi.services");
        ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));

        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(8090);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/*");
        contextHandler.addServlet(servletHolder, "/*");

        try {
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.INFO, "Initializing Jetty RestServer...");
            server.start();
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.INFO, "Jetty server initialized");
        } catch (Exception e) {
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.SEVERE, e.toString());
        }
    }
    //endregion
}
