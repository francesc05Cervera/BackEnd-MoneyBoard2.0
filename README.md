<div align="center">

# 💸 MoneyBoard 2.0 — Backend

**API REST per la gestione delle finanze personali**

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-✔-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

</div>

---

## 📌 Panoramica

**MoneyBoard 2.0** è il backend di un'applicazione web per la gestione delle finanze personali. Costruito con **Spring Boot 3**, espone una REST API sicura che permette agli utenti di registrare e monitorare entrate, uscite e categorie di spesa.

Questo progetto è la versione evoluta del precedente MoneyBoard, con un'architettura più robusta, Spring Security integrato e supporto Docker per il deploy.

---

## 🚀 Funzionalità principali

- 🔐 **Autenticazione e autorizzazione** tramite Spring Security
- 💰 **Gestione transazioni** — creazione, lettura, aggiornamento e cancellazione (CRUD)
- 🗂️ **Gestione categorie** di entrate e uscite
- 👤 **Gestione utenti** — registrazione e profilo
- 🗃️ **Persistenza dati** con JPA/Hibernate su PostgreSQL
- 🐳 **Deploy containerizzato** con Docker
- ⚙️ **Architettura a layer**: Controller → Service → Repository → Model

---

## 🏗️ Struttura del progetto

```text
src/main/java/com/MoneyBoard2/
├── config/ # Configurazioni Spring Security e CORS
├── Controller/ # REST Controllers (endpoint API)
├── Service/ # Business logic
├── Repository/ # Interfacce JPA (accesso al DB)
├── Model/ # Entità JPA (tabelle del database)
├── Request/ # DTO per le richieste HTTP
├── Exception/ # Gestione centralizzata degli errori
└── MoneyBoard2Application.java # Entry point
```


---

## 🛠️ Tech Stack

| Layer        | Tecnologia                          |
|--------------|-------------------------------------|
| Framework    | Spring Boot 3.5.11                  |
| Linguaggio   | Java 17                             |
| Database     | PostgreSQL (cloud via Neon)         |
| ORM          | Spring Data JPA / Hibernate         |
| Sicurezza    | Spring Security                     |
| Build tool   | Maven                               |
| Boilerplate  | Lombok                              |
| Container    | Docker                              |

---

## ⚡ Avvio in locale

### Prerequisiti

- Java 17+
- Maven 3.9+
- PostgreSQL (locale o istanza cloud [Neon](https://neon.tech))
- Docker *(opzionale)*

### Configurazione

Crea o modifica `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://<HOST>:<PORT>/<DATABASE>
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Avvio con Maven

```bash
# Clona il repository
git clone https://github.com/francesc05Cervera/BackEnd-MoneyBoard2.0.git
cd BackEnd-MoneyBoard2.0

# Build e avvio
./mvnw spring-boot:run
```

Il server sarà disponibile su `http://localhost:8080`.

### Avvio con Docker

```bash
# Build dell'immagine
docker build -t moneyboard2-backend .

# Avvio del container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<HOST>/<DB> \
  -e SPRING_DATASOURCE_USERNAME=<USER> \
  -e SPRING_DATASOURCE_PASSWORD=<PASSWORD> \
  moneyboard2-backend
```

---

## 📡 Endpoint principali

> Base URL: `http://localhost:8080/api`

| Metodo | Endpoint              | Descrizione                       | Auth |
|--------|-----------------------|-----------------------------------|------|
| POST   | `/auth/register`      | Registrazione nuovo utente        | ❌   |
| POST   | `/auth/login`         | Login e ottenimento token         | ❌   |
| GET    | `/transactions`       | Lista di tutte le transazioni     | ✅   |
| POST   | `/transactions`       | Crea una nuova transazione        | ✅   |
| PUT    | `/transactions/{id}`  | Modifica una transazione          | ✅   |
| DELETE | `/transactions/{id}`  | Elimina una transazione           | ✅   |
| GET    | `/categories`         | Lista categorie                   | ✅   |
| POST   | `/categories`         | Crea una nuova categoria          | ✅   |

---

## 📁 Requisiti di sistema

- **OS**: Windows / Linux / macOS
- **JDK**: 17 o superiore
- **RAM**: 512 MB minimo consigliato
- **DB**: PostgreSQL 14+

---

## 👨‍💻 Autore

Sviluppato da **Francesco Cervera**
🎓 Studente di Informatica — Università degli Studi di Napoli Federico II

[![GitHub](https://img.shields.io/badge/GitHub-francesc05Cervera-181717?style=flat-square&logo=github)](https://github.com/francesc05Cervera)

---

<div align="center">
<sub>MoneyBoard 2.0 Backend — Spring Boot · PostgreSQL · Docker</sub>
</div>
