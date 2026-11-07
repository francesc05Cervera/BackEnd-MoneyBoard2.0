<div align="center">
# 💸 MoneyBoard 2.0 — Backend
 
**API REST per la gestione delle finanze personali, protetta da JWT**
 
![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
 
</div>
---
 
## 📌 Panoramica
 
**MoneyBoard 2.0** è il backend di un'applicazione web per la gestione delle finanze personali. Ogni utente può creare **sezioni di risparmio** (ad esempio "Vacanze" o "Auto"), con un obiettivo da raggiungere, e registrare al loro interno **entrate e uscite**. Il saldo di ogni sezione si aggiorna automaticamente a ogni operazione.
 
L'autenticazione è basata su **JWT**: dopo il login il client riceve un token che identifica l'utente. Nessuna richiesta contiene l'id utente, che il backend ricava sempre dal token.
 
Il frontend che usa queste API è in un'altra repository: [FrontEnd-MoneyBoard2-SPA](https://github.com/francesc05Cervera/FrontEnd-MoneyBoard2-SPA) (Angular 17).
 
🌐 **Backend in produzione:** `https://backend-moneyboard2-0.onrender.com`
 
---
 
## 🚀 Funzionalità principali
 
- 🔐 **Registrazione e login** con password cifrate tramite BCrypt e token JWT firmato (HS256)
- 🗂️ **Sezioni di risparmio** — creazione, elenco ed eliminazione (solo se la sezione non contiene transazioni)
- 💰 **Transazioni** — creazione, elenco (tutte o per sezione) ed eliminazione
- 🧮 **Saldo automatico** — ogni entrata o uscita aggiorna il saldo della sezione; le uscite che renderebbero il saldo negativo vengono rifiutate, e la cancellazione di una transazione ripristina il saldo
- 👤 **Isolamento dei dati** — ogni utente vede e modifica solo le proprie sezioni e transazioni
- 🗃️ **Persistenza** con Spring Data JPA / Hibernate su PostgreSQL
- 🐳 **Deploy containerizzato** con Docker
---
 
## 🏗️ Struttura del progetto
 
```text
src/main/java/com/MoneyBoard2/
├── NEW_CONTROLLER_JWT/   # Controller REST (UsrCtrl, SezioneCtrl, TransazioneCtrl, Ping)
├── Service/              # Logica di business (ServiceUtente, ServiceSezione, ServiceTransazione)
├── Repository/           # Interfacce Spring Data JPA
├── Model/                # Entità JPA: Utente, SezioneRisparmio, Transazione
├── Request/              # DTO delle richieste HTTP
├── Response/             # DTO delle risposte (LoginResponse, RegisterResp)
├── Exception/            # Eccezioni applicative (CreationException, InfoNotFound, ...)
├── security/             # JwtService: generazione e validazione dei token
├── config/               # JwtConfig (bean JWT) e SecurityConfig (BCrypt, filter chain)
└── MoneyBoard2Application.java
```
 
Architettura a layer: **Controller → Service → Repository → Model**. La validazione del token avviene nei service tramite `JwtService`.
 
---
 
## 🛠️ Tech Stack
 
| Layer       | Tecnologia                                      |
|-------------|-------------------------------------------------|
| Framework   | Spring Boot 3.5.11                              |
| Linguaggio  | Java 17                                         |
| Database    | PostgreSQL (cloud via [Neon](https://neon.tech)) |
| ORM         | Spring Data JPA / Hibernate                     |
| Sicurezza   | JWT ([jjwt](https://github.com/jwtk/jjwt) 0.12.6) + BCrypt (Spring Security Crypto) |
| Build tool  | Maven (wrapper incluso)                         |
| Container   | Docker (build multi-stage)                      |
| Hosting     | Render                                          |
 
---
 
## ⚡ Avvio in locale
 
### Prerequisiti
 
- Java 17+
- Un database PostgreSQL (locale o su [Neon](https://neon.tech))
- Docker *(opzionale)*
Maven non va installato: basta il wrapper `mvnw` presente nella repository.
 
### Configurazione
 
Il file `src/main/resources/application.properties` è escluso da Git (`.gitignore`), perché contiene dati sensibili. La configurazione si può dare in due modi.
 
**Opzione 1 — variabili d'ambiente** (consigliata, è anche quella usata su Render e Docker):
 
| Variabile                        | Obbligatoria | Descrizione                                                                 |
|----------------------------------|:------------:|-----------------------------------------------------------------------------|
| `SPRING_DATASOURCE_URL`          | ✅ | URL JDBC, es. `jdbc:postgresql://<HOST>:5432/<DATABASE>`                    |
| `SPRING_DATASOURCE_USERNAME`     | ✅ | Utente del database                                                         |
| `SPRING_DATASOURCE_PASSWORD`     | ✅ | Password del database                                                       |
| `JWT_SECRET`                     | ✅ | Chiave di firma dei token, **codificata in Base64**, di almeno 32 byte      |
| `JWT_EXPIRATION_MS`              | ❌ | Durata del token in millisecondi (default `900000`, cioè 15 minuti)         |
| `SPRING_JPA_HIBERNATE_DDL_AUTO`  | ❌ | Impostala a `update` per far creare/aggiornare le tabelle a Hibernate       |
 
Per generare un `JWT_SECRET` valido:
 
```bash
openssl rand -base64 32
```
 
**Opzione 2 — file `application.properties`:**
 
```properties
spring.datasource.url=jdbc:postgresql://<HOST>:<PORT>/<DATABASE>
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
spring.jpa.hibernate.ddl-auto=update
 
jwt.secret=<CHIAVE_BASE64>
jwt.expiration-ms=900000
```
 
> ⚠️ Senza `jwt.secret` l'applicazione **non si avvia**.
 
### Avvio con Maven
 
```bash
git clone https://github.com/francesc05Cervera/BackEnd-MoneyBoard2.0.git
cd BackEnd-MoneyBoard2.0
 
# Linux / macOS
./mvnw spring-boot:run
 
# Windows
mvnw.cmd spring-boot:run
```
 
Il server è disponibile su `http://localhost:8080`. Per verificare che sia attivo:
 
```bash
curl http://localhost:8080/api2/ping
# pong
```
 
### Avvio con Docker
 
```bash
docker build -t moneyboard2-backend .
 
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<HOST>/<DB> \
  -e SPRING_DATASOURCE_USERNAME=<USER> \
  -e SPRING_DATASOURCE_PASSWORD=<PASSWORD> \
  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
  -e JWT_SECRET=<CHIAVE_BASE64> \
  moneyboard2-backend
```
 
### Test
 
```bash
./mvnw test
```
 
I test attuali coprono `JwtService` (generazione, validazione, firma alterata, token scaduto, estrazione dell'id utente).
 
---
 
## 📡 API
 
> Base URL: `http://localhost:8080/api2` (in produzione `https://backend-moneyboard2-0.onrender.com/api2`)
 
### Autenticazione
 
Gli endpoint protetti richiedono il token JWT, ottenuto dal login, nell'header `Authorization`.
 
> ⚠️ Il token va inviato **così com'è, senza il prefisso `Bearer `**:
>
> ```http
> Authorization: eyJhbGciOiJIUzI1NiJ9...
> ```
 
### Endpoint
 
| Metodo | Endpoint                         | Descrizione                                   | Auth |
|--------|----------------------------------|-----------------------------------------------|:----:|
| GET    | `/ping`                          | Controllo di stato, risponde `pong`           | ❌ |
| POST   | `/user/register`                 | Registrazione di un nuovo utente              | ❌ |
| POST   | `/user/login`                    | Login, restituisce il token JWT               | ❌ |
| GET    | `/sezione`                       | Sezioni dell'utente autenticato               | ✅ |
| POST   | `/sezione/create`                | Crea una sezione                              | ✅ |
| DELETE | `/sezione`                       | Elimina una sezione (body con il `nome`)      | ✅ |
| GET    | `/transazione`                   | Tutte le transazioni dell'utente              | ✅ |
| GET    | `/transazione/sezione/{id}`      | Transazioni di una sezione                    | ✅ |
| POST   | `/transazione/create`            | Crea una transazione                          | ✅ |
| DELETE | `/transazione/{id}`              | Elimina una transazione                       | ✅ |
 
### Esempi di richieste
 
**Registrazione** — `POST /api2/user/register`
 
```json
{ "username": "mario", "email": "mario@esempio.com", "password": "password123" }
```
 
Risposta `200`: `{ "message": "User registered successfully!" }`
 
**Login** — `POST /api2/user/login`
 
```json
{ "email": "mario@esempio.com", "password": "password123" }
```
 
Risposta `200`: `{ "token": "eyJhbGciOiJIUzI1NiJ9..." }`
 
**Crea sezione** — `POST /api2/sezione/create`
 
```json
{ "nome": "Vacanze", "obbiettivo": 1500.00 }
```
 
> Il campo si chiama `obbiettivo` (con due "b"), mentre nelle risposte compare come `obiettivo`.
 
**Elimina sezione** — `DELETE /api2/sezione`
 
```json
{ "nome": "Vacanze" }
```
 
**Crea transazione** — `POST /api2/transazione/create`
 
```json
{
  "titolo": "Stipendio",
  "descrizione": "Novembre",
  "importo": 1200.00,
  "entrata": true,
  "data": "2026-11-07",
  "sezione": { "nome": "Vacanze" }
}
```
 
> Nella richiesta il flag si chiama `entrata`. Nelle risposte la transazione espone invece `isEntrata`.
 
### Risposte e codici di stato
 
| Situazione                                               | Codice |
|----------------------------------------------------------|:------:|
| Operazione riuscita                                      | `200`  |
| Dati mancanti o non validi, sezione già esistente, saldo insufficiente, risorsa non trovata o non dell'utente | `400`  |
| Credenziali errate (solo login)                          | `401`  |
| Token non valido o scaduto, errore generico del server   | `500`  |
 
- Le risposte di `create` e `delete` sono in **testo semplice** (es. `Sezione creata con successo!`); gli errori contengono il messaggio.
- Login e registrazione restituiscono JSON in caso di successo e body vuoto in caso di errore.
---
 
## 🗄️ Modello dati
 
```text
Utente (1) ──< SezioneRisparmio (1) ──< Transazione
   └─────────────────────────────────────^
```
 
| Entità            | Campi principali                                                          |
|-------------------|---------------------------------------------------------------------------|
| `Utente`          | `id`, `username` (univoco), `email` (univoca), `password` (hash BCrypt)   |
| `SezioneRisparmio`| `id`, `nome`, `obiettivo`, `saldo`, `utente`                              |
| `Transazione`     | `id`, `titolo`, `descrizione`, `importo`, `isEntrata`, `data`, `sezione`, `utente` |
 
---
 
## ☁️ Deploy su Render
 
1. Crea un **Web Service** collegato alla repository, branch `main`, runtime **Docker** (usa il `Dockerfile` incluso).
2. Imposta in **Environment** le variabili elencate sopra (`SPRING_DATASOURCE_*`, `JWT_SECRET`, ...).
3. A ogni push su `main` Render ricostruisce l'immagine, se il deploy automatico è attivo. Altrimenti usa **Manual Deploy → Deploy latest commit**.
4. Controlla che `/api2/ping` risponda `pong`.
> Sul piano gratuito il servizio va in sospensione dopo un periodo di inattività, e la prima richiesta può richiedere circa un minuto. Per tenerlo attivo si può configurare un monitor (es. UptimeRobot) su `/api2/ping`.
 
---
 
## 🚧 Limiti noti
 
- Il nome della sezione è cercato **senza filtrare per utente**: due utenti non possono avere sezioni con lo stesso nome.
- Le entità `Transazione` e `SezioneRisparmio` sono restituite direttamente come JSON e includono l'oggetto `utente`: andrebbe escluso il campo `password` (hash) con `@JsonIgnore` o con dei DTO di risposta.
- Un token scaduto o non valido produce un `500` invece di un `401`.
- `SecurityConfig` lascia tutte le rotte aperte (`permitAll`): il controllo del token è fatto nei service, non da un filtro Spring Security.
- Non sono previsti endpoint di modifica (`PUT`) per sezioni e transazioni.
---
 
## 👨‍💻 Autore
 
Sviluppato da **Francesco Cervera**
🎓 Studente di Informatica — Università degli Studi di Napoli Federico II
 
[![GitHub](https://img.shields.io/badge/GitHub-francesc05Cervera-181717?style=flat-square&logo=github)](https://github.com/francesc05Cervera)
 
---
 
<div align="center">
<sub>MoneyBoard 2.0 Backend — Spring Boot · PostgreSQL · JWT · Docker</sub>
</div>
 
