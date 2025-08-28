# Bejeweled (Spring Boot + React + PostgreSQL)

A small **Bejeweled** game with a **Spring Boot** backend, **React** frontend (built and embedded into the backend), and **PostgreSQL** database.  
The project is packaged with **Docker Compose**, so you can spin everything up with a single command.

## Badges

[![Java](https://img.shields.io/badge/Java-11-blue?logo=java)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.4.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61dafb?logo=react)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)](https://www.docker.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9.8-C71A36?logo=apachemaven)](https://maven.apache.org/)

## Contents
- [Technologies](#technologies)
- [Quick Start (Docker)](#quick-start-docker)
- [Share via Internet (ngrok)](#share-via-internet-ngrok)
- [Development Mode (without Docker)](#development-mode-without-docker)
- [Frontend Environment Variables](#frontend-environment-variables)
- [Building Frontend into Backend JAR](#building-frontend-into-backend-jar)
- [Useful Docker Commands](#useful-docker-commands)
- [Endpoints](#endpoints)
- [Common Issues & Solutions](#common-issues--solutions)
- [Repository Structure](#repository-structure)

---

## Technologies
- **Java / Spring Boot 2.4.x**
- **React (Vite)** — static build embedded into Spring Boot
- **WebSocket (SockJS + STOMP)** — `/ws`
- **PostgreSQL**
- **Docker / Docker Compose**
- **Maven**

---

## Quick Start (Docker)

> Requires **Docker** and **Docker Compose** installed.

```bash
git clone https://github.com/absorfy/Bejeweled
cd Bejeweled

# Start everything (backend + DB)
docker compose up
# or in background:
# docker compose up -d

# Open in browser:
# http://localhost:8080
```

> On the first run, a new PostgreSQL volume is created. Hibernate will auto-generate the tables.

---

## Share via Internet (ngrok)

Want to show the game to a friend while your laptop is running?  
The easiest way is a **ngrok tunnel**:

```bash
# in the first terminal
docker compose up -d

# in the second terminal (install ngrok and set your authtoken)
ngrok http --region=eu 8080
```

Ngrok will give you an **HTTPS link** like `https://xxxxx.ngrok-free.app` — share it.  
(Backend already supports proxy headers: `server.forward-headers-strategy=framework`.)

---

## Development Mode (without Docker)

If you want quick frontend hot-reload development:

1) **Database** (via Docker or locally):
```bash
docker compose up db
```

2) **Backend**:
```bash
# in repo root
mvn spring-boot:run
# runs at http://localhost:8080
```

3) **Frontend**:
```bash
cd frontend
cp .env.development.example .env.development    # if available
npm install
npm run dev
# runs at http://localhost:5173
```

In dev mode, frontend points to backend at `http://localhost:8080` (see below).

---

## Frontend Environment Variables

We use `VITE_BASE_URL` **only in dev**, while in prod we use **relative paths** (same origin).

- `frontend/.env.development`:
  ```env
  VITE_BASE_URL=http://localhost:8080
  ```
- `frontend/.env.production` — **without** `VITE_BASE_URL` (or empty file)

Frontend axios config:
```js
import axios from "axios";

const rawBase = import.meta.env?.VITE_BASE_URL?.trim();
const baseURL = (rawBase ? rawBase.replace(/\/+/g, "") : "") + "/api";

const api = axios.create({
  baseURL,                        // dev: http://localhost:8080/api, prod: /api
  withCredentials: true,
  headers: { "ngrok-skip-browser-warning": "true" }
});

export default api;
```

WebSocket (SockJS) relative path in prod:
```js
// prod: '/ws' -> wss://<your-domain>/ws
// dev: http://localhost:8080/ws
const resolveSocketUrl = () => {
  const base = import.meta.env?.VITE_BASE_URL?.trim();
  return base ? `${base.replace(/\/+/g, "")}/ws` : "/ws";
};
```

---

## Building Frontend into Backend JAR

If you changed the frontend — you need to **rebuild frontend and backend**:

```bash
# 1) build frontend
cd frontend
npm ci
npm run build

# 2) copy dist/ content into backend (usually src/main/resources/static/)
#    or use a script to automate it

# 3) rebuild backend image and restart
cd ..
docker compose build backend
docker compose up -d
```

---

## Useful Docker Commands

```bash
# Run in background
docker compose up -d

# View logs
docker compose logs -f backend
docker compose logs -f db

# Stop
docker compose down

# Stop and remove volumes (DB reset!)
docker compose down -v

# Clean rebuild of backend
docker compose build --no-cache backend
```

---

## Endpoints

- **HTTP UI (React build served by Spring Boot)**: `http://localhost:8080`
- **REST API**: `/api/...` (already configured in axios)
- **WebSocket (SockJS/STOMP)**: `/ws`
  - prefixes: `/app` (inbound), `/topic` (outbound)

---

## Common Issues & Solutions

**1) CSS/JS not loading, MIME errors, 403**  
Cause: security config blocks static files or returns 403.  
Solution:
- allow static in `SecurityConfig`:
  ```java
  .authorizeRequests()
    .antMatchers("/", "/index.html",
                 "/assets/**", "/static/**", "/css/**", "/js/**",
                 "/images/**", "/favicon.ico", "/manifest.json", "/webjars/**")
    .permitAll()
    .anyRequest().permitAll();
  ```
- ensure frontend is **built** and files placed into `src/main/resources/static/`.

---

**2) “An insecure SockJS connection may not be initiated from a page loaded over HTTPS”**  
Cause: page is over **HTTPS**, socket opens via **ws/http**.  
Solution: in prod use **relative** `"/ws"` (browser will switch to `wss://`).

---

**3) Requests go to `.../undefined/api/...`**  
Cause: `VITE_BASE_URL` missing in prod, code concatenates `undefined + '/api'`.  
Solution: build base URL conditionally (see `client.js` above).

---

**4) Pages/redirects fail behind ngrok**  
Add to `application.properties`:
```properties
server.forward-headers-strategy=framework
```

---

## Repository Structure

```
Bejeweled/
├─ docker-compose.yml
├─ Dockerfile
├─ pom.xml
├─ src/
│  └─ main/
│     ├─ java/...                # backend (Spring Boot)
│     └─ resources/
│        ├─ application.properties
│        └─ static/              # React build (dist) goes here
└─ frontend/
   ├─ package.json
   ├─ vite.config.*.ts/js
   ├─ src/                       # React source code
   ├─ .env.development           # VITE_BASE_URL=http://localhost:8080
```
