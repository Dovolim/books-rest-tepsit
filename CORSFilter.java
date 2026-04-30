package it.edu.marconipontedera.tepsit.rest.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filtro JAX-RS per gestire le intestazioni CORS (Cross-Origin Resource Sharing).
 * Permette al client di comunicare con il server anche se gira su porte diverse.
 *
 * Basato sulla lezione 5 della guida html.it: "Filtri e listener".
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(CORSFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin",  "*");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        LOGGER.fine("CORS headers aggiunti per: " + requestContext.getUriInfo().getRequestUri());
    }
}
