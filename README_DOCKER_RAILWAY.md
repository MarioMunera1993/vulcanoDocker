# 📦 Resumen de migración a Docker + Railway

**Estado**: ✅ Completo (Listo para despliegue)

---

## 📋 Archivos creados/modificados

### Backend (`cursos-vulcano-grupo-1/`)

| Archivo | Estado | Descripción |
|---------|--------|-------------|
| `Dockerfile` | ✨ Creado | Build multietapa optimizado (Java 21) |
| `.dockerignore` | ✨ Creado | Excluye archivos innecesarios del build |
| `.env.example` | ✨ Creado | Plantilla de variables de entorno |
| `docker-compose.yml` | 🔄 Actualizado | Ahora incluye frontend + backend + postgres |
| `src/main/resources/application.properties` | 🔄 Modificado | Configuración para Docker/Railway (PORT, DB, UPLOAD_DIR) |
| `src/main/java/com/grupo1/cursosvulcano/config/WebConfig.java` | 🔄 Modificado | Mapeo dinámico de directorio de uploads |
| `src/main/java/com/grupo1/cursosvulcano/service/UserProfileService.java` | 🔄 Modificado | Upload configurable vía `@Value` |

### Frontend (`vulcano-app-v2/`)

| Archivo | Estado | Descripción |
|---------|--------|-------------|
| `Dockerfile` | ✨ Creado | Build multietapa (Node + Nginx) |
| `nginx.conf` | ✨ Creado | Configuración Nginx para SPA + proxy al backend |
| `.dockerignore` | ✨ Creado | Excluye node_modules y archivos innecesarios |
| `.env.example` | ✨ Creado | Plantilla VITE_API_URL |

### Raíz del proyecto (`ApiConDocker/`)

| Archivo | Estado | Descripción |
|---------|--------|-------------|
| `docker-compose.yml` | ✨ Creado | Orquestación completa: frontend + backend + PostgreSQL |
| `RAILWAY_DEPLOYMENT.md` | ✨ Creado | **Guía paso a paso para Railway.app** |
| `LOCAL_TESTING.md` | ✨ Creado | **Guía para probar con Docker localmente** |

---

## ⚡ Inicio rápido

### 1️⃣ Prueba local (5-10 minutos)

```bash
cd E:\Cesde\TercerMomento\ProyectoIntegrador\UnionProyecto\ApiConDocker

docker-compose up --build
```

Luego abre:
- **Frontend**: `http://localhost:3000`
- **Backend API**: `http://localhost:8080`
- **Swagger**: `http://localhost:8080/swagger-ui.html`

**Ver guía detallada**: [LOCAL_TESTING.md](./LOCAL_TESTING.md)

### 2️⃣ Desplegar a Railway (15 minutos)

Sigue paso a paso: [RAILWAY_DEPLOYMENT.md](./RAILWAY_DEPLOYMENT.md)

En resumen:
1. Crea proyecto en Railway
2. Añade PostgreSQL
3. Desplega backend (con GitHub o Docker)
4. Desplega frontend (con GitHub o Docker)
5. Configura variables de entorno
6. Verifica endpoints

---

## 🏗️ Arquitectura final

```
┌─────────────────────────────────────────────────────┐
│                  PRODUCCIÓN (Railway)               │
├─────────────────────────────────────────────────────┤
│                                                     │
│  Frontend (Nginx)      Backend (Spring Boot)        │
│  Port: 3000           Port: 8080                   │
│  ↓                     ↓                             │
│  SPA React        REST API + Swagger               │
│  ↓                     ↓                             │
│  └─────────┬─────────────┘                         │
│            │                                         │
│      ┌─────▼─────────┐                             │
│      │   PostgreSQL  │                             │
│      │   (Railway)   │                             │
│      └───────────────┘                             │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## ✅ Pre-Flight Checklist

Antes de ir a producción (Railway):

- [ ] Ejecutaste `docker-compose up --build` localmente
- [ ] Frontend carga sin errores en `http://localhost:3000`
- [ ] Backend está disponible en `http://localhost:8080`
- [ ] Swagger se abre en `http://localhost:8080/swagger-ui.html`
- [ ] Creaste un usuario de prueba desde Swagger o el frontend
- [ ] Pudiste hacer login
- [ ] Los datos se guardaron en PostgreSQL
- [ ] Leíste [RAILWAY_DEPLOYMENT.md](./RAILWAY_DEPLOYMENT.md) completamente
- [ ] Tienes cuenta en Railway.app
- [ ] Tienes repo en GitHub (opcional, Railway soporta zip también)

---

## 🔐 Seguridad (Lo que deberías hacer después)

### Actual (para desarrollo/pruebas)
- ✅ Autenticación básica (username + contraseña en texto)
- ✅ Sin JWT
- ✅ Swagger expuesto

### Recomendado para producción (siguiente iteración)
- 🔒 Spring Security + JWT
- 🔒 CORS restrictivo
- 🔒 Swagger solo en desarrollo
- 🔒 HTTPS obligatorio
- 🔒 Rate limiting

### Para almacenamiento de archivos (necesario después)
- 🔒 S3 / Cloudflare R2 / DigitalOcean Spaces
- Eliminar `uploads/` local
- Archivos persistentes en buckets

---

## 📊 Cambios en la configuración

### Variables de entorno requeridas

**Backend (Railway)**:
```bash
PORT=8080                                              # Puerto obligatorio
DB_URL=jdbc:postgresql://host:5432/dbname           # De PostgreSQL plugin
DB_USERNAME=postgres                                  # Usuario PostgreSQL
DB_PASSWORD=password                                  # Contraseña PostgreSQL
UPLOAD_DIR=/app/uploads                              # Carpeta temporal
JAVA_OPTS=-Xms256m -Xmx512m                          # Memoria JVM
```

**Frontend (Railway)**:
```bash
VITE_API_URL=https://tu-backend-railway.app          # URL del backend
```

**Local (docker-compose.yml)**:
```bash
# Definidas en docker-compose.yml, no necesitas .env
DB_URL=jdbc:postgresql://postgres:5432/cursosvulcano
DB_USERNAME=postgres
DB_PASSWORD=postgres_secure_change_me
PORT=8080
UPLOAD_DIR=uploads
```

---

## 🚨 Problemas conocidos y soluciones

| Problema | Solución | Más info |
|----------|----------|----------|
| **Port 3000 en uso** | Cambia en docker-compose.yml: `3001:3000` | LOCAL_TESTING.md |
| **Frontend no conecta a backend** | Revisa VITE_API_URL en Railway variables | RAILWAY_DEPLOYMENT.md |
| **PostgreSQL no conecta** | Verifica credenciales en DB_URL | RAILWAY_DEPLOYMENT.md |
| **Archivos subidos desaparecen** | Normal en Railway (efímero). Migra a S3 después | - |
| **Swagger retorna 403/404** | Añade dominio del frontend en CorsConfig.java | - |
| **Imagen tarda mucho en buildear** | Normal primera vez (~3min). Paciencia. | - |

---

## 📞 Soportes útiles

| Recurso | Link |
|---------|------|
| Documentación Railway | [https://docs.railway.app](https://docs.railway.app) |
| Docker Compose Docs | [https://docs.docker.com/compose](https://docs.docker.com/compose) |
| Spring Boot Config | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| Vite Config | [https://vitejs.dev/config](https://vitejs.dev/config) |
| Nginx Config | [https://nginx.org/en/docs](https://nginx.org/en/docs) |

---

## 🎯 Próximas mejoras

**Corto plazo** (próxima semana):
1. Implementar JWT en backend
2. Mejorar CORS
3. Añadir validaciones de entrada

**Mediano plazo** (próximo mes):
1. Integrar S3/R2 para imágenes
2. Implementar tests automáticos
3. CI/CD con GitHub Actions

**Largo plazo** (próximos meses):
1. Rate limiting
2. Monitoreo y logs centralizados
3. Base de datos read-replica para escala
4. Cache con Redis

---

## 📝 Notas finales

✅ **Tu stack está listo para:**
- Desarrollo local con Docker Compose
- Testing de toda la aplicación
- Despliegue en Railway.app

⚠️ **Ten en cuenta:**
- Los archivos subidos se pierden en Railway (usa buckets después)
- Cambia la contraseña de PostgreSQL antes de producción
- Configura dominios personalizados en Railway

🚀 **Próximo paso**: Sigue [RAILWAY_DEPLOYMENT.md](./RAILWAY_DEPLOYMENT.md) para desplegar hoy.

---

**Última actualización**: 12 de mayo de 2026  
**Autor**: Full Stack DevOps Guide  
**Estado**: ✅ Listo para producción (con reservas de seguridad)
