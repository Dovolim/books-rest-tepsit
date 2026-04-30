package it.edu.marconipontedera.tepsit.rest;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Classe di configurazione dell'applicazione JAX-RS.
 * Definisce il path base "/api" per tutti gli endpoint REST.
 *
 * Tutti i servizi saranno accessibili sotto:
 *   http://localhost:8080/api/books
 */
@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {

    public RestApplication() {
        // Scansione automatica del package per trovare le risorse JAX-RS
        packages("it.edu.marconipontedera.tepsit.rest");
    }
}
