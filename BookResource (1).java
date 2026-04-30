package it.edu.marconipontedera.tepsit.rest;

import it.edu.marconipontedera.tepsit.rest.model.Book;
import it.edu.marconipontedera.tepsit.rest.service.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Risorsa REST per la gestione dei Libri.
 *
 * Endpoints implementati (come descritto nella prima pagina della guida html.it):
 *
 *   GET    /books           → lista tutti i libri
 *   GET    /books/{id}      → dettaglio singolo libro
 *   POST   /books           → crea un nuovo libro
 *   PUT    /books/{id}      → modifica un libro esistente
 *   DELETE /books/{id}      → elimina un libro
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    /**
     * GET /books
     * Restituisce la lista completa di tutti i libri.
     */
    @GET
    public Response getAllBooks() {
        List<Book> books = BookService.getAllBooks();
        return Response.ok(books).build();
    }

    /**
     * GET /books/{id}
     * Restituisce il dettaglio di un singolo libro tramite il suo ID.
     * Risponde 404 se il libro non esiste.
     */
    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = BookService.getBookById(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"errore\":\"Libro con id=" + id + " non trovato\"}")
                    .build();
        }
        return Response.ok(book).build();
    }

    /**
     * POST /books
     * Crea un nuovo libro. Il corpo della richiesta deve essere un JSON
     * con i campi: title, author, year, isbn.
     * L'ID viene assegnato automaticamente dal server.
     */
    @POST
    public Response createBook(Book book) {
        if (book == null || book.getTitle() == null || book.getTitle().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"errore\":\"Il campo title è obbligatorio\"}")
                    .build();
        }
        Book created = BookService.addBook(book);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    /**
     * PUT /books/{id}
     * Modifica un libro esistente identificato dall'ID nel path.
     * Il corpo deve contenere i nuovi dati del libro.
     * Risponde 404 se il libro non esiste.
     */
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        Book updated = BookService.updateBook(id, book);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"errore\":\"Libro con id=" + id + " non trovato\"}")
                    .build();
        }
        return Response.ok(updated).build();
    }

    /**
     * DELETE /books/{id}
     * Elimina il libro con l'ID specificato.
     * Risponde 404 se il libro non esiste.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        boolean deleted = BookService.deleteBook(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"errore\":\"Libro con id=" + id + " non trovato\"}")
                    .build();
        }
        return Response.noContent().build();
    }
}
