# 🚀 Guía de despliegue en Railway.app — Backend + Frontend + PostgreSQL

---

## 📋 Tabla de contenidos

1. [Requisitos previos](#requisitos-previos)
2. [Crear proyecto en Railway](#crear-proyecto-en-railway)
3. [Configurar PostgreSQL](#configurar-postgresql)
4. [Desplegar Backend](#desplegar-backend)
5. [Desplegar Frontend](#desplegar-frontend)
6. [Configurar variables de entorno](#configurar-variables-de-entorno)
7. [Verificar despliegue](#verificar-despliegue)
8. [Dominios y networking](#dominios-y-networking)

---

## ✅ Requisitos previos

- Cuenta en [Railway.app](https://railway.app)
- Repositorio en GitHub con ambos proyectos (o subidos a Railway)
- Docker Desktop instalado (opcional, para testing local)

---

## 🏗️ Crear proyecto en Railway

### Paso 1: Inicia sesión en Railway

1. Ve a [https://railway.app](https://railway.app)
2. Haz clic en **Sign In** → **GitHub**
3. Autoriza Railway para acceder a tus repositorios

### Paso 2: Crea un nuevo proyecto

1. Haz clic en **+ New Project**
2. Elige **Deploy from GitHub repo** (o **Empty Project** si quieres subirlo después)
3. Conecta tu repositorio (asegúrate que contiene ambas carpetas: `cursos-vulcano-grupo-1` y `vulcano-app-v2`)

---

## 🗄️ Configurar PostgreSQL

### Paso 3: Añade PostgreSQL a tu proyecto

1. En Railway, ve a tu proyecto recién creado
2. Haz clic en **+ Add** (parte superior derecha)
3. Busca **PostgreSQL** en la lista de integraciones
4. Haz clic en **PostgreSQL** → **Add**
5. Railway creará automáticamente:
   - Un contenedor de PostgreSQL
   - Variables de entorno: `DATABASE_URL`, `PGHOST`, `PGPORT`, `PGUSER`, `PGPASSWORD`

### Paso 4: Obtén las credenciales de PostgreSQL

1. Ve a la sección **PostgreSQL** en tu proyecto
2. Haz clic en la pestaña **Connect**
3. Verás algo como:
   ```
   DATABASE_URL=postgresql://postgres:password@host:5432/railway
   PGHOST=host
   PGPORT=5432
   PGUSER=postgres
   PGPASSWORD=password
   ```
4. **Anota estos valores** — los necesitarás para el backend

---

## 🔧 Desplegar Backend

### Paso 5: Crea un servicio para el Backend

1. En tu proyecto de Railway, haz clic en **+ Add**
2. Elige **GitHub Repo** → Selecciona tu repositorio
3. Railway detectará automáticamente el `Dockerfile` en `cursos-vulcano-grupo-1`
4. Configura:
   - **Root Directory**: `cursos-vulcano-grupo-1` (si no lo detecta)
   - **Dockerfile**: `Dockerfile` (default)

### Paso 6: Configura variables de entorno del Backend

1. Una vez creado el servicio backend, haz clic en **Variables**
2. Añade estas variables:

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `PORT` | `8080` | Puerto de escucha (Railway lo usa) |
| `DB_URL` | `jdbc:postgresql://${{ Postgres.PGHOST }}:${{ Postgres.PGPORT }}/railway` | URL de conexión |
| `DB_USERNAME` | `${{ Postgres.PGUSER }}` | Usuario PostgreSQL |
| `DB_PASSWORD` | `${{ Postgres.PGPASSWORD }}` | Contraseña PostgreSQL |
| `UPLOAD_DIR` | `/app/uploads` | Directorio de subidas (temporal) |
| `JAVA_OPTS` | `-Xms256m -Xmx512m` | Opciones JVM |

**Importante**: Railway permite usar referencias a otras variables con `${{ ServiceName.VAR }}`

### Paso 7: Desplega el Backend

1. Haz clic en **Deploy** (esquina superior derecha del servicio)
2. Railway compilará y desplegará automáticamente
3. Espera a que aparezca **✓ Your Railway deployment is live**
4. Copia la URL asignada automáticamente, ej: `https://backend-prod.railway.app`

---

## 🎨 Desplegar Frontend

### Paso 8: Crea un servicio para el Frontend

1. En tu proyecto, haz clic en **+ Add**
2. Elige **GitHub Repo** → Selecciona tu repositorio
3. Railway detectará el `Dockerfile` en `vulcano-app-v2`
4. Configura:
   - **Root Directory**: `vulcano-app-v2`
   - **Dockerfile**: `Dockerfile`

### Paso 9: Configura variables de entorno del Frontend

1. En la sección **Variables** del frontend, añade:

| Variable | Valor | Descripción |
|----------|-------|-------------|
| `VITE_API_URL` | `https://backend-prod.railway.app` | URL del backend en Railway |

(Usa la URL real que obtengas del backend en paso 7)

### Paso 10: Desplega el Frontend

1. Haz clic en **Deploy**
2. Espera a que se complete
3. Copia la URL asignada, ej: `https://frontend-prod.railway.app`

---

## 🔐 Configurar variables de entorno

### Paso 11: Asegurar credenciales sensibles (Opcional)

Si no quieres que PostgreSQL cree un usuario `postgres` con contraseña débil:

1. Ve a PostgreSQL → **Variables**
2. Cambia `PGPASSWORD` a una contraseña más segura
3. Presiona **Enter** para guardar
4. Railway reiniciará PostgreSQL automáticamente

### Paso 12: Verificar todas las variables

1. Ve a **Variables** de cada servicio:
   - **Backend**: debe tener `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
   - **Frontend**: debe tener `VITE_API_URL`
   - **PostgreSQL**: auto-generadas

---

## ✅ Verificar despliegue

### Paso 13: Probar el Backend

Abre en tu navegador:
```
https://tu-backend-url/swagger-ui.html
```

Deberías ver la documentación de Swagger. Si ves un error 502 o 500, revisa los logs:
1. Ve al servicio backend
2. Haz clic en **Logs**
3. Busca errores de conexión a PostgreSQL

### Paso 14: Probar el Frontend

Abre en tu navegador:
```
https://tu-frontend-url
```

Deberías ver tu aplicación React. Si ves errores en la consola del navegador (DevTools), probablemente es un problema de CORS o la URL del backend.

### Paso 15: Probar comunicación Frontend → Backend

1. Abre tu aplicación frontend
2. Intenta hacer login o crear un usuario
3. En DevTools → Network, verifica que las peticiones a `/api/*` lleguen correctamente
4. Si ves errores CORS, revisa el `CorsConfig.java` del backend

---

## 🌐 Dominios y Networking

### Opción A: Usar subdominio gratuito de Railway (recomendado para inicio)

Railway asigna automáticamente URLs como:
- Backend: `https://cursosvulcano-production.railway.app`
- Frontend: `https://vulcano-app-production.railway.app`

**Ventaja**: Gratuito, automático
**Desventaja**: URLs largas, no personalizadas

### Opción B: Dominio personalizado (requiere dominio propio)

1. Compra un dominio (ej: `midominio.com`)
2. En Railway, ve a tu proyecto → **Settings**
3. Busca **Custom Domain**
4. Añade:
   - `api.midominio.com` → Backend
   - `app.midominio.com` → Frontend
5. Configura los DNS según las instrucciones de Railway

---

## 🐛 Solución de problemas

### Problema: Backend no conecta a PostgreSQL

**Error típico**: `Connection refused` o `FATAL: password authentication failed`

**Solución**:
1. Ve a PostgreSQL → Logs
2. Verifica que esté corriendo
3. Ve a Backend → Variables
4. Revisa que `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` sean correctos
5. Usa las variables auto-generadas de PostgreSQL con `${{ Postgres.* }}`

### Problema: Frontend no puede alcanzar el Backend (CORS)

**Error típico**: `CORS error` o `No 'Access-Control-Allow-Origin' header`

**Solución**:
1. En Backend, revisa [CorsConfig.java](../cursos-vulcano-grupo-1/src/main/java/com/grupo1/cursosvulcano/config/CorsConfig.java)
2. Añade el dominio del frontend:
   ```java
   .allowedOrigins("https://tu-frontend-url")
   ```
3. Redeploy el backend
4. También, en Frontend, actualiza `VITE_API_URL`:
   ```
   VITE_API_URL=https://tu-backend-url
   ```
5. Redeploy el frontend

### Problema: Archivos subidos no persisten (uploads)

Esto es **esperado** en Railway. Los contenedores son efímeros.

**Solución temporal**: Los archivos desaparecen al redeploy. Esto es normal.

**Solución permanente** (futura): Migra a S3/R2/Spaces para almacenar imágenes.

---

## 📊 Resumen de arquitectura final

```
┌─────────────────────────────────────────┐
│         Railway.app (Proyecto)          │
├─────────────────────────────────────────┤
│                                         │
│  ┌──────────────┐  ┌──────────────┐   │
│  │  Frontend    │  │   Backend    │   │
│  │ (React/Vite)│  │(Spring Boot) │   │
│  │ :3000       │  │ :8080        │   │
│  └──────┬───────┘  └──────┬───────┘   │
│         │                 │            │
│         └────────┬────────┘            │
│                  │                     │
│          ┌───────▼────────┐            │
│          │   PostgreSQL   │            │
│          │ (Managed DB)   │            │
│          └────────────────┘            │
│                                         │
└─────────────────────────────────────────┘

Frontend: https://vulcano-app.railway.app
Backend:  https://cursosvulcano.railway.app
DB:       Manejada por Railway
```

---

## 🎯 Próximos pasos (Futuro)

1. **Dominio personalizado**: Compra `miapp.com` y configura DNS
2. **Almacenamiento persistente**: Migra `uploads/` a Cloudflare R2 o S3
3. **Seguridad**: Implementa JWT + Spring Security
4. **CI/CD**: Configura GitHub Actions para tests automáticos
5. **Monitoring**: Configura logs y alertas en Railway

---

## 📞 Soporte

- Documentación de Railway: [https://docs.railway.app](https://docs.railway.app)
- Comunidad Discord: [https://discord.gg/railway](https://discord.gg/railway)
- Soporte oficial: [support@railway.app](mailto:support@railway.app)

---

**Última actualización**: Mayo 2026
**Autor**: DevOps Migration Guide
