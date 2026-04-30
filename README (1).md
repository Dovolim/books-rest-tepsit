# 📚 Books REST Server/Client — JAX-RS con Jersey

**Progetto TEPSIT · ITI "G. Marconi" Pontedera**

Implementazione di un sistema client/server RESTful in Java usando il framework **Jersey** (JAX-RS),
con persistenza dei dati in **JSON tramite Gson**.

---

## 📋 Descrizione

Il progetto è diviso in **due moduli Maven distinti**:

| Modulo | Tipo | Descrizione |
|--------|------|-------------|
| `books-rest-server` | WAR / Jetty | Server JAX-RS che espone le API REST per i libri |
| `books-rest-client` | JAR | Client Java che interroga il server con Jersey Client |

Il progetto segue la guida di Francesco Cioffi su html.it:
> https://www.html.it/guide/restful-web-services-in-java-con-jersey/

e il modello `pom.xml` del prof. Lenzi:
> https://github.com/kismet/teaching/blob/main/tepsit/java-web/hello-rest/pom.xml

---

## 🛠 Tecnologie utilizzate

| Tecnologia | Versione | Ruolo |
|------------|----------|-------|
| Java       | 8        | Linguaggio |
| Maven      | 3.x      | Build e dipendenze |
| Jersey     | 2.23.2   | Framework JAX-RS (server + client) |
| Jetty      | 9.3.11   | Servlet engine embedded |
| Gson       | 2.10.1   | Serializzazione/persistenza JSON |
| MOXy       | 2.23.2   | Provider JSON per Jersey |

---

## 🚀 Avvio del Server

```bash
cd books-rest-server
mvn jetty:run
```

Il server si avvia sulla porta **8080**.
URL base delle API: `http://localhost:8080/api/books`

---

## 📡 Endpoint REST disponibili

| Metodo | URL | Descrizione |
|--------|-----|-------------|
| `GET`    | `/api/books`      | Lista tutti i libri |
| `GET`    | `/api/books/{id}` | Dettaglio libro per ID |
| `POST`   | `/api/books`      | Crea un nuovo libro |
| `PUT`    | `/api/books/{id}` | Modifica un libro esistente |
| `DELETE` | `/api/books/{id}` | Elimina un libro |

### Esempi con curl

```bash
# Lista tutti i libri
curl -X GET http://localhost:8080/api/books

# Dettaglio libro id=1
curl -X GET http://localhost:8080/api/books/1

# Crea un nuovo libro
curl -X POST http://localhost:8080/api/books \
     -H "Content-Type: application/json" \
     -d '{"title":"La Divina Commedia","author":"Dante Alighieri","year":1320,"isbn":"978-00-000-0001-1"}'

# Aggiorna il libro con id=1
curl -X PUT http://localhost:8080/api/books/1 \
     -H "Content-Type: application/json" \
     -d '{"title":"I Promessi Sposi (Rivisto)","author":"Alessandro Manzoni","year":1840,"isbn":"978-88-04-00001-1"}'

# Elimina il libro con id=2
curl -X DELETE http://localhost:8080/api/books/2
```

---

## 💾 Persistenza JSON con Gson

I dati vengono salvati automaticamente nel file **`books.json`** nella directory di esecuzione del server.

- Il file viene **creato** al primo avvio (con 3 libri di esempio).
- Viene **aggiornato** dopo ogni POST, PUT o DELETE.
- Viene **riletto** ad ogni riavvio del server, garantendo la **persistenza tra i riavvii**.

```json
[
  {
    "id": 1,
    "title": "I Promessi Sposi",
    "author": "Alessandro Manzoni",
    "year": 1827,
    "isbn": "978-88-04-00001-1"
  }
]
```

---

## ❓ Risposta alla Domanda 1

**Il server effettua mai un Reset dei dati? Quando avviene questo Reset e perché?**

> Nella versione base della guida html.it, i dati venivano tenuti **solo in memoria** (variabile statica Java).
> Questo causava un **reset involontario** ad ogni riavvio del server, perché la JVM ricreava la classe da zero,
> perdendo tutte le modifiche fatte in runtime e ripristinando solo i dati hard-coded nel codice sorgente.
>
> In questa versione migliorata **non avviene nessun reset**: i dati vengono letti da `books.json` all'avvio
> e scritti su disco dopo ogni modifica, garantendo la **persistenza permanente**.

---

## ▶️ Avvio del Client

Con il server già in esecuzione:

```bash
cd books-rest-client
mvn package
java -jar target/books-rest-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Il client esegue in sequenza tutte le operazioni CRUD e mostra i risultati su console.

---

## 📁 Struttura del progetto

```
books-rest-server/
├── pom.xml
└── src/main/java/it/edu/marconipontedera/tepsit/rest/
    ├── RestApplication.java          ← configurazione @ApplicationPath
    ├── BookResource.java             ← endpoint REST /api/books
    ├── model/
    │   └── Book.java                 ← bean entità libro
    ├── service/
    │   └── BookService.java          ← logica di business + gestione lista
    ├── storage/
    │   └── JsonStorage.java          ← persistenza Gson su books.json
    └── filter/
        └── CORSFilter.java           ← filtro JAX-RS per CORS

books-rest-client/
├── pom.xml
└── src/main/java/it/edu/marconipontedera/tepsit/client/
    └── BooksClient.java              ← client Jersey che testa tutti gli endpoint
```

---

## 📖 Riferimenti

- [Guida RESTful Web Services in Java con Jersey — html.it](https://www.html.it/guide/restful-web-services-in-java-con-jersey/)
- [Modello pom.xml del prof. Lenzi — GitHub](https://github.com/kismet/teaching/blob/main/tepsit/java-web/hello-rest/pom.xml)
- [Documentazione Jersey 2.x](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/index.html)
- [Gson — Google](https://github.com/google/gson)
