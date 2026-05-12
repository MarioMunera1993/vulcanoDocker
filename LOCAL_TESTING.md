# 🐳 Pruebas locales con Docker Compose

Sigue esta guía para probar todo tu stack (frontend + backend + PostgreSQL) en tu máquina local antes de desplegar a Railway.

---

## ✅ Requisitos

- Docker Desktop instalado: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
- Git (para clonar/actualizar repos)
- 2-3 minutos de paciencia en el primer build

---

## 🚀 Inicio rápido

### Opción A: Desde la raíz del proyecto (RECOMENDADO)

```bash
# 1. Navega a la carpeta raíz donde están ambos proyectos
cd E:\Cesde\TercerMomento\ProyectoIntegrador\UnionProyecto\ApiConDocker

# 2. Construye e inicia todos los contenedores
docker-compose up --build

# 3. Espera a que veas:
# - "app started on port 8080" → Backend listo
# - "nginx: signal process started" → Frontend listo
```

### Opción B: Desde la carpeta del backend (ALTERNATIVA)

```bash
cd cursos-vulcano-grupo-1

docker-compose up --build

# Nota: Esto levanta backend + PostgreSQL, pero NO el frontend.
# Solo úsalo si necesitas probar backend sin frontend.
```

---

## 🌐 Acceso a los servicios

Una vez que todo esté corriendo, abre en tu navegador:

| Servicio | URL | Propósito |
|----------|-----|----------|
| Frontend | `http://localhost:3000` | Tu aplicación React |
| Backend | `http://localhost:8080` | API REST |
| Swagger | `http://localhost:8080/swagger-ui.html` | Documentación API |
| PostgreSQL | `localhost:5432` | Base de datos (solo si conectas con cliente) |

---

## 🔧 Comandos útiles

### Ver logs en tiempo real

```bash
# Todos los servicios
docker-compose logs -f

# Solo backend
docker-compose logs -f backend

# Solo frontend
docker-compose logs -f frontend

# Solo PostgreSQL
docker-compose logs -f postgres
```

### Detener todos los servicios

```bash
docker-compose down

# También elimina datos persistentes (base de datos)
docker-compose down -v
```

### Reconstruir solo un servicio

```bash
# Backend
docker-compose up --build backend

# Frontend
docker-compose up --build frontend

# PostgreSQL (generalmente no es necesario)
docker-compose up --build postgres
```

### Ejecutar comandos dentro de un contenedor

```bash
# Ver las carpetas del backend
docker-compose exec backend ls -la

# Conectar a PostgreSQL desde la CLI
docker-compose exec postgres psql -U postgres -d cursosvulcano
```

---

## 🐛 Solución de problemas locales

### Error: "Port 3000 is already in use"

```bash
# Cambia el puerto en docker-compose.yml:
# Línea: ports:
#   - "3001:3000"  ← Cambia 3000 a otro puerto (ej: 3001)

docker-compose up --build
```

Luego accede a: `http://localhost:3001`

### Error: "Connection refused" en frontend hacia backend

El frontend no puede alcanzar `http://app:8080` (usa el nombre del servicio docker).

**Solución**: En el `nginx.conf` del frontend, debe estar:
```nginx
location /api {
    proxy_pass http://app:8080;  ← Nombre del servicio backend
    ...
}
```

Si está mal, corrígelo y ejecuta:
```bash
docker-compose up --build frontend
```

### Error: PostgreSQL no inicia

```bash
# Revisa los logs
docker-compose logs postgres

# Si está corrupto, elimina el volumen y reinicia
docker-compose down -v
docker-compose up --build
```

### Frontend tarda mucho en compilar la primera vez

Es normal. Vite necesita descargar e instalar todas las dependencias (~2-3 minutos).

Paciencia. La próxima vez será más rápido.

---

## 📁 Estructura de carpetas esperada

```
E:\Cesde\TercerMomento\ProyectoIntegrador\UnionProyecto\ApiConDocker\
├── docker-compose.yml           ← Archivo principal (úsalo desde aquí)
├── RAILWAY_DEPLOYMENT.md        ← Guía de Railway
├── LOCAL_TESTING.md             ← Este archivo
│
├── cursos-vulcano-grupo-1/      ← Backend
│   ├── Dockerfile
│   ├── docker-compose.yml       ← (Alternativo, no uses si usas el raíz)
│   ├── pom.xml
│   ├── src/
│   └── uploads/                 ← Archivos subidos (temporal)
│
└── vulcano-app-v2/             ← Frontend
    ├── Dockerfile
    ├── nginx.conf
    ├── package.json
    ├── vite.config.js
    └── src/
```

---

## ✨ Casos de uso

### 1. Desarrollo con hot-reload (sin Docker)

Si prefieres usar npm/npm dev del frontend:

```bash
# Terminal 1: Backend + PostgreSQL (Docker)
cd cursos-vulcano-grupo-1
docker-compose up --build

# Terminal 2: Frontend (npm)
cd vulcano-app-v2
npm install
npm run dev
```

Luego abre:
- Frontend: `http://localhost:5173` (puerto default de Vite)
- Backend: `http://localhost:8080`

### 2. Solo testing de API (sin frontend)

```bash
cd cursos-vulcano-grupo-1
docker-compose up --build

# Abre en Postman o curl:
curl http://localhost:8080/api/courses
```

### 3. Recrear todo desde cero (base de datos limpia)

```bash
docker-compose down -v
docker-compose up --build
```

---

## 🔐 Variables de entorno locales

El `docker-compose.yml` raíz usa:
- `postgres_secure_change_me` como contraseña de PostgreSQL
- `uploads` como carpeta de subidas

Para cambiarlas, edita el archivo `docker-compose.yml` en la raíz.

---

## ✅ Checklist de verificación local

Antes de desplegar a Railway, verifica:

- [ ] Frontend carga en `http://localhost:3000`
- [ ] Backend Swagger disponible en `http://localhost:8080/swagger-ui.html`
- [ ] Puedes crear un usuario en la API
- [ ] Puedes hacer login desde el frontend
- [ ] Los datos se guardan en PostgreSQL (lista usuarios desde Swagger)
- [ ] Las imágenes se suben correctamente a `/uploads`
- [ ] Los logs no muestran errores de conexión

Si todos los items están OK, tu stack está listo para Railway. 🎉

---

**Última actualización**: Mayo 2026
