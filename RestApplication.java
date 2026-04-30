package it.edu.marconipontedera.tepsit.rest;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages("it.edu.marconipontedera.tepsit.rest");
    }
}
