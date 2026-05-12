# 🏗️ Diagrama de arquitectura — Vulcano Full Stack

## Arquitectura Local (Development)

```
┌────────────────────────────────────────────────────────────────────┐
│                    Docker Compose (Local)                          │
│                    http://localhost:*                              │
├────────────────────────────────────────────────────────────────────┤
│                                                                    │
│  ┌──────────────────┐         ┌──────────────────────────────┐   │
│  │                  │         │                              │   │
│  │   🎨 Frontend    │         │     🔌 Backend               │   │
│  │                  │         │                              │   │
│  │  React 19        │         │  Spring Boot 3.4.5           │   │
│  │  Vite 7.3        │         │  Java 21                     │   │
│  │  Nginx           │         │  Port: 8080                  │   │
│  │  Port: 3000      │         │                              │   │
│  │                  │         │  ▪ REST API                  │   │
│  │  ▪ SPA estático  │────────▶│  ▪ Swagger UI                │   │
│  │  ▪ Hot-reload    │  /api   │  ▪ User CRUD                │   │
│  │    (dev)         │         │  ▪ Course CRUD              │   │
│  │  ▪ Proxy         │         │  ▪ Modules CRUD             │   │
│  │    /api          │         │  ▪ File Upload              │   │
│  │    /uploads      │         │                              │   │
│  │                  │         │                              │   │
│  └──────────────────┘         └────────────┬─────────────────┘   │
│                                             │                     │
│                                             │ jdbc:postgresql     │
│                                             │                     │
│                                    ┌────────▼─────────┐          │
│                                    │                  │          │
│                                    │  🗄️ PostgreSQL  │          │
│                                    │                  │          │
│                                    │  Port: 5432      │          │
│                                    │  User: postgres  │          │
│                                    │  DB: cursosvulcano          │
│                                    │                  │          │
│                                    │  ▪ Users         │          │
│                                    │  ▪ Courses       │          │
│                                    │  ▪ Modules       │          │
│                                    │  ▪ Reviews       │          │
│                                    │  ▪ Schedules     │          │
│                                    │                  │          │
│                                    └──────────────────┘          │
│                                                                   │
│  Volúmenes:                                                       │
│  ▪ postgres-data    (Persistente)                                │
│  ▪ uploads/         (Temporal)                                   │
│                                                                   │
└────────────────────────────────────────────────────────────────────┘

Comando: docker-compose up --build
```

---

## Arquitectura Producción (Railway.app)

```
┌──────────────────────────────────────────────────────────────────────┐
│                     RAILWAY.APP (Production)                         │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  https://vulcano-app-prod.railway.app                               │
│         │                                                            │
│         └─────────────────────────────────────────┐                 │
│                                                   │                 │
│  ┌──────────────────────────────────────┐        │                 │
│  │                                      │        │                 │
│  │  🎨 FRONTEND SERVICE                │        │                 │
│  │                                      │        │                 │
│  │  Image: vulcano-app-v2:latest       │        │                 │
│  │  Port: 3000 (interno)               │        │                 │
│  │  Dominio: app.midominio.com        │        │                 │
│  │  (o subdomain de Railway)           │        │                 │
│  │                                      │        │                 │
│  │  ▪ React build estático             │        │                 │
│  │  ▪ Nginx reverse proxy               │        │                 │
│  │  ▪ Proxy /api → backend:8080        │        │                 │
│  │  ▪ Proxy /uploads → backend:8080    │        │                 │
│  │                                      │        │                 │
│  │  Variables:                          │        │                 │
│  │  - VITE_API_URL=                    │        │                 │
│  │    https://api.midominio.com        │        │                 │
│  │                                      │        │                 │
│  └──────────────┬───────────────────────┘        │                 │
│                 │                                │                 │
│                 │ /api                           │                 │
│                 │ /uploads                       │                 │
│                 ▼                                │                 │
│  ┌──────────────────────────────────────┐        │                 │
│  │                                      │        │                 │
│  │  🔌 BACKEND SERVICE                 │        │                 │
│  │                                      │        │                 │
│  │  Image: cursos-vulcano:latest       │        │                 │
│  │  Port: 8080 (interno)               │        │                 │
│  │  Dominio: api.midominio.com        │        │                 │
│  │  (o subdomain de Railway)           │        │                 │
│  │                                      │        │                 │
│  │  ▪ Spring Boot app.jar              │        │                 │
│  │  ▪ REST API endpoints                │        │                 │
│  │  ▪ Swagger UI                        │        │                 │
│  │  ▪ File upload handler               │        │                 │
│  │                                      │        │                 │
│  │  Variables:                          │        │                 │
│  │  - PORT=8080                        │        │                 │
│  │  - DB_URL=postgresql://...          │        │                 │
│  │  - DB_USERNAME=${{Postgres.PGUSER}} │        │                 │
│  │  - DB_PASSWORD=${{Postgres.PGPASS}} │        │                 │
│  │  - UPLOAD_DIR=/app/uploads          │        │                 │
│  │  - JAVA_OPTS=-Xms256m -Xmx512m      │        │                 │
│  │                                      │        │                 │
│  └──────────────┬───────────────────────┘        │                 │
│                 │                                │                 │
│                 │ jdbc:postgresql:...            │                 │
│                 │                                │                 │
│  ┌──────────────▼────────────────────────┐       │                 │
│  │                                       │       │                 │
│  │  🗄️ POSTGRESQL (Managed Service)     │       │                 │
│  │                                       │       │                 │
│  │  Version: 15 (Alpine)                │       │                 │
│  │  Port: 5432 (interno)                │       │                 │
│  │  Host: ${PGHOST}                     │       │                 │
│  │  User: ${PGUSER}                     │       │                 │
│  │  Password: ${PGPASSWORD}             │       │                 │
│  │  Database: railway (default)         │       │                 │
│  │                                       │       │                 │
│  │  ▪ Automáticamente creada            │       │                 │
│  │  ▪ Backups automáticos               │       │                 │
│  │  ▪ Persistencia garantizada          │       │                 │
│  │  ▪ No necesita configuración         │       │                 │
│  │                                       │       │                 │
│  └───────────────────────────────────────┘       │                 │
│                                                   │                 │
│  Almacenamiento:                                  │                 │
│  ▪ uploads/       → Temporal (efímero)           │                 │
│                     (Se pierde en redeploy)      │                 │
│  ▪ PostgreSQL    → Persistente                   │                 │
│                                                   │                 │
│  CI/CD:                                           │                 │
│  ▪ GitHub Integration (auto-deploy)              │                 │
│  ▪ Detects Dockerfile automáticamente             │                 │
│  ▪ Build → Deploy → Live                         │                 │
│                                                   │                 │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Flujo de datos (Ejemplo: crear usuario)

### Local Development
```
1. Usuario en http://localhost:3000 (Frontend React)
                 ↓
2. Click en "Crear usuario"
                 ↓
3. POST /api/users (Frontend → Nginx proxy)
                 ↓
4. http://localhost:8080/api/users (Backend Spring Boot)
                 ↓
5. UserController.createUser() procesa
                 ↓
6. INSERT en PostgreSQL (localhost:5432)
                 ↓
7. Respuesta: User object + 201 Created
                 ↓
8. Frontend recibe y actualiza UI
```

### Production (Railway)
```
1. Usuario en https://app.railway.app (Frontend Nginx)
                 ↓
2. Click en "Crear usuario"
                 ↓
3. POST /api (Nginx proxy local)
                 ↓
4. https://api.railway.app/api/users (Backend Spring Boot)
                 ↓
5. UserController.createUser() procesa
                 ↓
6. INSERT en PostgreSQL (Railway Managed DB)
                 ↓
7. Respuesta: User object + 201 Created
                 ↓
8. Frontend recibe y actualiza UI
```

---

## Capas de la aplicación

```
┌────────────────────────────────────────────┐
│     Presentation Layer (Frontend)          │
│                                            │
│  • React Components                        │
│  • Vite bundler                            │
│  • Tailwind CSS                            │
│  • React Router (SPA)                      │
│  • Nginx (static files + proxy)            │
└────────────────┬───────────────────────────┘
                 │ HTTP/REST
                 │
┌────────────────▼───────────────────────────┐
│     API Layer (Backend)                    │
│                                            │
│  • Spring Boot Controllers                 │
│  • REST endpoints (@RestController)        │
│  • Swagger UI documentation                │
│  • Request/Response DTOs                   │
│  • CORS configuration                      │
└────────────────┬───────────────────────────┘
                 │ JDBC
                 │
┌────────────────▼───────────────────────────┐
│     Service Layer (Business Logic)         │
│                                            │
│  • UserService                             │
│  • CourseService                           │
│  • ModuleService                           │
│  • ReviewService                           │
│  • ClassScheduleService                    │
│  • UserProfileService                      │
└────────────────┬───────────────────────────┘
                 │ JPA/Hibernate
                 │
┌────────────────▼───────────────────────────┐
│     Repository Layer (Data Access)         │
│                                            │
│  • UserRepository (JpaRepository)          │
│  • CourseRepository                        │
│  • ModuleRepository                        │
│  • ReviewRepository                        │
│  • ClassScheduleRepository                 │
│  • UserProfileRepository                   │
└────────────────┬───────────────────────────┘
                 │ SQL
                 │
┌────────────────▼───────────────────────────┐
│     Database Layer                         │
│                                            │
│  • PostgreSQL (Relational DB)              │
│  • Tables: users, courses, modules, etc.   │
│  • Schemas & Constraints                   │
│  • Persistence storage                     │
└────────────────────────────────────────────┘
```

---

## Flujos de despliegue

### Flujo Local (Development)

```
Código fuente
    ↓
git push (opcional)
    ↓
docker-compose up --build
    ↓
Docker daemon
    ├─ Build imagen backend (Dockerfile)
    ├─ Build imagen frontend (Dockerfile)
    └─ Pull imagen PostgreSQL
    ↓
docker-compose crea red vulcano-network
    ↓
Inicia 3 contenedores:
    ├─ frontend:3000 (Nginx)
    ├─ backend:8080 (Spring Boot)
    └─ postgres:5432 (PostgreSQL)
    ↓
Disponible en http://localhost:3000
```

### Flujo Railway (Production)

```
GitHub Push (o ZIP upload)
    ↓
Railway webhook recibe notificación
    ↓
Clona repo / Extrae ZIP
    ↓
Detecta Dockerfile en:
    ├─ cursos-vulcano-grupo-1/Dockerfile
    └─ vulcano-app-v2/Dockerfile
    ↓
Railway Build Service
    ├─ Compila backend (Maven)
    ├─ Construye imagen backend
    ├─ Compila frontend (npm)
    └─ Construye imagen frontend
    ↓
Railway Deployment Service
    ├─ Inicia contenedor backend
    ├─ Inicia contenedor frontend
    └─ (PostgreSQL ya está)
    ↓
Asigna URLs:
    ├─ https://vulcano-app-prod.railway.app
    └─ https://cursosvulcano-prod.railway.app
    ↓
Disponible en https://vulcano-app-prod.railway.app
```

---

## Matriz de tecnologías

| Capa | Tecnología | Versión | Propósito |
|------|-----------|---------|----------|
| **Frontend** | React | 19.2.0 | UI/UX |
| | Vite | 7.3.1 | Bundler |
| | Tailwind CSS | 4.2.1 | Estilos |
| | React Router | 7.14.0 | Routing SPA |
| | Axios/Fetch | builtin | HTTP client |
| **Backend** | Java | 21 | Lenguaje |
| | Spring Boot | 3.4.5 | Framework |
| | Spring Data JPA | 3.4.5 | ORM |
| | Hibernate | managed | ORM provider |
| | PostgreSQL Driver | managed | JDBC |
| **Database** | PostgreSQL | 15 | RDBMS |
| **DevOps** | Docker | latest | Containerización |
| | Docker Compose | latest | Orquestación |
| | Nginx | latest | Reverse proxy |
| **Hosting** | Railway.app | prod | Cloud platform |

---

## Métricas de despliegue

```
┌──────────────────────────────────┐
│    Tamaño de imágenes Docker     │
├──────────────────────────────────┤
│ Backend                          │
│  - Build stage:    ~500 MB       │
│  - Final image:    ~250 MB       │
│                                  │
│ Frontend                         │
│  - Build stage:    ~500 MB       │
│  - Final image:    ~50 MB        │
│                                  │
│ PostgreSQL                       │
│  - Imagen:         ~100 MB       │
│                                  │
│ Total en Railway: ~400 MB        │
└──────────────────────────────────┘

Tiempo de deployment:
  - Backend build:      ~2-3 min
  - Frontend build:     ~2-3 min
  - PostgreSQL init:    ~30 seg
  - Total:             ~5-7 min (primera vez)
  - Updates:           ~2-3 min

Recursos estimados:
  - Memory:            ~512 MB (backend)
                       ~256 MB (frontend)
                       ~256 MB (PostgreSQL)
                       = 1 GB total
  - CPU:               0.5-1 CPU shared
  - Storage:           5-10 GB (depende de datos)
```

---

## Monitoreo de salud

```
┌──────────────────────────────────┐
│   Health Checks (Railway)        │
├──────────────────────────────────┤
│                                  │
│ Backend:                         │
│  GET /actuator/health           │
│  Response: { "status": "UP" }   │
│                                  │
│ Frontend:                        │
│  HTTP 200 en /                  │
│  (Nginx serves index.html)       │
│                                  │
│ PostgreSQL:                      │
│  TCP port 5432 accessible       │
│  Database conexión OK           │
│                                  │
└──────────────────────────────────┘
```

---

**Última actualización**: 12 de mayo de 2026
