# ⚡ Referencia rápida - Comandos esenciales

Cheatsheet para desarrollo y despliegue.

---

## 🐳 Docker Compose (Local)

```bash
# Levantar todo (primer build tarda ~3 min)
docker-compose up --build

# Levantar sin rebuild
docker-compose up

# Levantar en background
docker-compose up -d

# Detener todo
docker-compose down

# Detener + borrar volúmenes (BD limpia)
docker-compose down -v

# Ver logs de todo
docker-compose logs -f

# Ver logs de un servicio
docker-compose logs -f backend       # o frontend, postgres

# Reconstruir un servicio
docker-compose up --build backend    # o frontend

# Ejecutar comando en contenedor
docker-compose exec backend bash     # Entra en shell del backend
docker-compose exec postgres psql -U postgres -d cursosvulcano

# Listar contenedores activos
docker-compose ps
```

---

## 🏗️ Build local (sin Docker)

```bash
# Backend (Maven)
cd cursos-vulcano-grupo-1
./mvnw clean package          # Compilar
./mvnw spring-boot:run        # Ejecutar

# Frontend (Node)
cd vulcano-app-v2
npm install                   # Instalar dependencias
npm run dev                   # Desarrollo (hot-reload)
npm run build                 # Build para producción
npm run preview              # Vista previa del build
```

---

## 🌐 Acceso a servicios

| Servicio | URL | Notas |
|----------|-----|-------|
| Frontend | `http://localhost:3000` | React |
| Backend | `http://localhost:8080` | Spring Boot |
| Swagger | `http://localhost:8080/swagger-ui.html` | API docs |
| PostgreSQL | `localhost:5432` | Cliente necesario |

---

## 📝 Endpoints principales (Swagger)

**URL**: `http://localhost:8080/swagger-ui.html`

```
POST   /api/auth/login                 → Iniciar sesión
POST   /api/users                      → Crear usuario
GET    /api/users/{id}                 → Obtener usuario
GET    /api/courses                    → Listar cursos
GET    /api/modules/course/{courseId}  → Módulos de un curso
POST   /api/userProfiles/{id}/upload-image → Subir imagen
```

---

## 🔒 Variables de entorno

### Backend (application.properties o Railway)

```properties
PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/cursosvulcano
DB_USERNAME=postgres
DB_PASSWORD=postgres_secure_change_me
UPLOAD_DIR=uploads
JAVA_OPTS=-Xms256m -Xmx512m
```

### Frontend (.env o Railway)

```
VITE_API_URL=http://localhost:8080
```

---

## 🚀 Railway Deployment

### 1. Crear proyecto

```bash
# Via GitHub (recomendado)
1. En railway.app → + New Project → GitHub Repo
2. Selecciona tu repositorio
3. Railway auto-detecta los Dockerfiles
```

### 2. Añadir PostgreSQL

```bash
# En Railway
1. + Add
2. Busca PostgreSQL
3. Haz clic en "Add"
4. Railway crea usuario y contraseña automáticamente
```

### 3. Configurar variables de entorno

**Backend**:
```
DB_URL=jdbc:postgresql://${{ Postgres.PGHOST }}:${{ Postgres.PGPORT }}/railway
DB_USERNAME=${{ Postgres.PGUSER }}
DB_PASSWORD=${{ Postgres.PGPASSWORD }}
PORT=8080
UPLOAD_DIR=/app/uploads
```

**Frontend**:
```
VITE_API_URL=https://tu-backend-railway.app
```

### 4. Desplegar

```bash
# En Railway
1. Cada servicio → Deploy (botón arriba a la derecha)
2. Espera a que diga "✓ Your Railway deployment is live"
3. Copia la URL asignada
```

---

## 🐛 Debugging

### Ver logs del backend
```bash
docker-compose logs -f backend

# O en Railway
Backend Service → Logs
```

### Ver logs del frontend
```bash
docker-compose logs -f frontend

# O en Railway
Frontend Service → Logs
```

### Conectar a PostgreSQL

```bash
# Local con Docker
docker-compose exec postgres psql -U postgres -d cursosvulcano

# Comandos útiles en psql
\dt                          # Listar tablas
SELECT * FROM "user";        # Ver usuarios
\q                           # Salir
```

### Test de API (curl)

```bash
# Listar cursos
curl http://localhost:8080/api/courses

# Crear usuario
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "email": "test@test.com",
    "password": "pass123"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "password": "pass123"
  }'
```

---

## ⚠️ Problemas frecuentes

| Problema | Solución |
|----------|----------|
| **Puerto en uso** | Cambia en docker-compose.yml el puerto externo |
| **Build lento** | Normal primera vez, Docker cachea después |
| **Frontend no alcanza backend** | Verifica VITE_API_URL |
| **PostgreSQL no conecta** | Revisa credenciales en DB_URL |
| **Archivos subidos desaparecen (Railway)** | Es efímero, migra a S3 después |

---

## 📚 Documentación

- **Railway**: https://docs.railway.app
- **Docker Compose**: https://docs.docker.com/compose
- **Spring Boot**: https://spring.io/projects/spring-boot
- **Vite**: https://vitejs.dev
- **Nginx**: https://nginx.org/en/docs

---

## 📋 Checklist pre-despliegue

- [ ] `docker-compose up --build` corre sin errores
- [ ] Frontend accesible en `http://localhost:3000`
- [ ] Backend API en `http://localhost:8080`
- [ ] Swagger UI abre
- [ ] Puedo crear un usuario
- [ ] Puedo hacer login
- [ ] Base de datos tiene datos
- [ ] Archivos se suben correctamente

✅ Si todos los items están OK → **Listo para Railway**

---

**Última actualización**: Mayo 2026
