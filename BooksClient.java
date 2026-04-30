package it.edu.marconipontedera.tepsit.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

public class BooksClient {

    private static final String BASE_URL = "http://localhost:8080/api/books";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {

        String url = args.length > 0 ? args[0] : BASE_URL;

        System.out.println("=================================================");
        System.out.println("  Books REST Client - TEPSIT Marconi Pontedera");
        System.out.println("  Server: " + url);
        System.out.println("=================================================\n");

        // Creazione del client 
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);

        System.out.println("--- [1] GET " + url + " ---");
        Response resp = target.request(MediaType.APPLICATION_JSON).get();
        System.out.println("Status: " + resp.getStatus());
        String json = resp.readEntity(String.class);
        System.out.println(json);
        resp.close();

        System.out.println("\n--- [2] GET " + url + "/1 ---");
        resp = target.path("/1").request(MediaType.APPLICATION_JSON).get();
        System.out.println("Status: " + resp.getStatus());
        System.out.println(resp.readEntity(String.class));
        resp.close();

        System.out.println("\n--- [3] POST " + url + " (crea nuovo libro) ---");
        String nuovoLibro = GSON.toJson(new BookPayload(
                "La Divina Commedia", "Dante Alighieri", 1320, "978-88-04-00099-9"
        ));
        System.out.println("Body inviato: " + nuovoLibro);
        resp = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(nuovoLibro, MediaType.APPLICATION_JSON));
        System.out.println("Status: " + resp.getStatus());
        String createdJson = resp.readEntity(String.class);
        System.out.println(createdJson);
        resp.close();

        // Estrae l'ID del libro appena creato
        BookPayload created = GSON.fromJson(createdJson, BookPayload.class);
        int newId = created.id;
        System.out.println("→ Libro creato con ID=" + newId);

        System.out.println("\n--- [4] PUT " + url + "/" + newId + " (aggiorna) ---");
        String libroAggiornato = GSON.toJson(new BookPayload(
                "La Divina Commedia (Ed. Critica)", "Dante Alighieri", 1321, "978-88-04-00099-9"
        ));
        System.out.println("Body inviato: " + libroAggiornato);
        resp = target.path("/" + newId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(libroAggiornato, MediaType.APPLICATION_JSON));
        System.out.println("Status: " + resp.getStatus());
        System.out.println(resp.readEntity(String.class));
        resp.close();

        // 5. DELETE /api/books/{newId}  → Elimina il libro
        System.out.println("\n--- [5] DELETE " + url + "/" + newId + " ---");
        resp = target.path("/" + newId)
                .request(MediaType.APPLICATION_JSON)
                .delete();
        System.out.println("Status: " + resp.getStatus() + " (204 = eliminato con successo)");
        resp.close();
        // 6. GET /api/books  → Lista finale (verifica che il libro sia sparito)

        System.out.println("\n--- [6] GET " + url + " (lista finale) ---");
        resp = target.request(MediaType.APPLICATION_JSON).get();
        System.out.println("Status: " + resp.getStatus());
        System.out.println(resp.readEntity(String.class));
        resp.close();

        // 7. GET /api/books/999  → Libro inesistente (test 404)

        System.out.println("\n--- [7] GET " + url + "/999 (test 404) ---");
        resp = target.path("/999").request(MediaType.APPLICATION_JSON).get();
        System.out.println("Status: " + resp.getStatus() + " (404 = non trovato, come atteso)");
        System.out.println(resp.readEntity(String.class));
        resp.close();

        client.close();
        System.out.println("\n=== Test completati ===");
    }

    static class BookPayload {
        int id;
        String title;
        String author;
        int year;
        String isbn;

        BookPayload(String title, String author, int year, String isbn) {
            this.title  = title;
            this.author = author;
            this.year   = year;
            this.isbn   = isbn;
        }
    }
}
