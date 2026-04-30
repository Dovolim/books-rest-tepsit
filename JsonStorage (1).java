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

/**
 * Classe responsabile della persistenza dei dati su file JSON usando Gson.
 *
 * RISPOSTA ALLA DOMANDA 1:
 * Il server NON effettua MAI un reset automatico dei dati in questa implementazione.
 * Nella versione base della guida html.it, i dati venivano tenuti solo in memoria
 * (in una List statica), quindi si "resettavano" ogni volta che il server veniva
 * riavviato (perché la JVM ricreava la classe da zero perdendo lo stato in memoria).
 * Con questa implementazione la lista viene salvata su file JSON (books.json) e
 * ricaricata all'avvio, rendendo i dati persistenti tra un riavvio e l'altro.
 */
public class JsonStorage {

    private static final Logger LOGGER = Logger.getLogger(JsonStorage.class.getName());
    private static final String FILE_NAME = "books.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonStorage() {}

    /**
     * Restituisce il percorso del file JSON nella directory corrente di esecuzione.
     */
    private static File getFile() {
        return new File(System.getProperty("user.dir"), FILE_NAME);
    }

    /**
     * Carica la lista di libri dal file JSON.
     * Se il file non esiste restituisce una lista vuota (primo avvio).
     */
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
     * Salva la lista di libri sul file JSON.
     * Viene chiamato dopo ogni modifica (POST, PUT, DELETE).
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
