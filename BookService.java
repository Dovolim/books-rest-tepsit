package it.edu.marconipontedera.tepsit.rest.service;
import it.edu.marconipontedera.tepsit.rest.model.Book;
import it.edu.marconipontedera.tepsit.rest.storage.JsonStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BookService {

    // Lista in memoria, caricata dal file JSON all'avvio
    private static List<Book> books;
    private static AtomicInteger idCounter;

    // carica dati dal JSON
    static {
        books = JsonStorage.load();
        if (books.isEmpty()) {
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

    public static Book getBookById(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public static Book addBook(Book book) {
        book.setId(idCounter.incrementAndGet());
        books.add(book);
        JsonStorage.save(books);
        return book;
    }

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

    public static boolean deleteBook(int id) {
        boolean removed = books.removeIf(b -> b.getId() == id);
        if (removed) JsonStorage.save(books);
        return removed;
    }
}
