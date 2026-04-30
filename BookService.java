package it.edu.marconipontedera.tepsit.rest.service;

import it.edu.marconipontedera.tepsit.rest.model.Book;
import it.edu.marconipontedera.tepsit.rest.storage.JsonStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Servizio singleton che gestisce la collezione di libri in memoria,
 * con persistenza automatica su file JSON tramite JsonStorage.
 *
 * NOTA sulla domanda 1 (reset):
 * Nella versione originale della guida html.it la lista dei libri era una
 * variabile statica Java inizializzata con dati di esempio al momento del
 * caricamento della classe. Questo significa che i dati venivano resettati
 * (tornando ai valori iniziali hard-coded) ogni volta che il server veniva
 * riavviato, perché la JVM ricreava la classe statica da zero.
 * In questa versione migliorata i dati vengono invece letti da books.json
 * all'avvio e scritti su disco dopo ogni modifica, garantendo la persistenza.
 */
public class BookService {

    // Lista in memoria, caricata dal file JSON all'avvio
    private static List<Book> books;

    // Contatore ID auto-incrementale
    private static AtomicInteger idCounter;

    // Inizializzazione statica: carica dati dal JSON (o dati demo se primo avvio)
    static {
        books = JsonStorage.load();
        if (books.isEmpty()) {
            // Dati di esempio solo al primissimo avvio (quando il file non esiste)
            books = new ArrayList<>();
            books.add(new Book(1, "I Promessi Sposi",     "Alessandro Manzoni", 1827, "978-88-04-00001-1"));
            books.add(new Book(2, "Il Nome della Rosa",   "Umberto Eco",        1980, "978-88-04-00002-2"));
            books.add(new Book(3, "Pinocchio",            "Carlo Collodi",      1883, "978-88-04-00003-3"));
            JsonStorage.save(books);
        }
        int maxId = books.stream().mapToInt(Book::getId).max().orElse(0);
        idCounter = new AtomicInteger(maxId);
    }

    private BookService() {}

    /** Restituisce tutti i libri */
    public static List<Book> getAllBooks() {
        return books;
    }

    /** Cerca un libro per ID, restituisce null se non trovato */
    public static Book getBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    /**
     * Aggiunge un nuovo libro assegnando un ID auto-incrementale.
     * Salva automaticamente su JSON.
     */
    public static Book addBook(Book book) {
        book.setId(idCounter.incrementAndGet());
        books.add(book);
        JsonStorage.save(books);
        return book;
    }

    /**
     * Aggiorna un libro esistente tramite PUT.
     * Salva automaticamente su JSON.
     * @return il libro aggiornato, o null se l'ID non esiste.
     */
    public static Book updateBook(int id, Book updated) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == id) {
                updated.setId(id);
                books.set(i, updated);
                JsonStorage.save(books);
                return updated;
            }
        }
        return null;
    }

    /**
     * Elimina un libro per ID.
     * Salva automaticamente su JSON.
     * @return true se il libro è stato eliminato, false se non trovato.
     */
    public static boolean deleteBook(int id) {
        boolean removed = books.removeIf(b -> b.getId() == id);
        if (removed) JsonStorage.save(books);
        return removed;
    }
}
