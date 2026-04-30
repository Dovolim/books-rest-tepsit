package it.edu.marconipontedera.tepsit.rest.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.edu.marconipontedera.tepsit.rest.model.Book;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JsonStorage {

    private static final Logger LOGGER = Logger.getLogger(JsonStorage.class.getName());
    private static final String FILE_NAME = "books.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonStorage() {}

    private static File getFile() {
        return new File(System.getProperty("user.dir"), FILE_NAME);
    }

    public static List<Book> load() {
        File f = getFile();
        if (!f.exists()) {
            LOGGER.info("File " + FILE_NAME + " non trovato, si parte con lista vuota.");
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(f)) {
            Type listType = new TypeToken<List<Book>>() {}.getType();
            List<Book> books = GSON.fromJson(reader, listType);
            if (books == null) return new ArrayList<>();
            LOGGER.info("Caricati " + books.size() + " libri da " + FILE_NAME);
            return books;
        } catch (IOException e) {
            LOGGER.severe("Errore lettura " + FILE_NAME + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Salva la lista di libri sul file
     * Viene chiamato dopo ogni modifica
     */
    public static void save(List<Book> books) {
        File f = getFile();
        try (Writer writer = new FileWriter(f)) {
            GSON.toJson(books, writer);
            LOGGER.info("Salvati " + books.size() + " libri in " + f.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.severe("Errore scrittura " + FILE_NAME + ": " + e.getMessage());
        }
    }
}
