# 🌋 Vulcano API — Backend (Grupo 1)

**Plataforma de gamificación educativa para fortalecer la lógica de programación mediante desafíos interactivos.**

---

## 📌 Descripción

VULCANO API es el backend de la plataforma Vulcano, una aplicación educativa gamificada que permite a los estudiantes de desarrollo de software consolidar conocimientos teóricos mediante desafíos interactivos, gestión de cursos modulares y clases privadas con expertos.

**Arquitectura:** API REST desarrollada con Java Spring Boot que sirve datos al frontend React (vulcano-app-v2).

---

## 🛠️ Tech Stack

| Tecnología | Versión | Descripción |
| :--- | :--- | :--- |
| **Java** | 21 | Lenguaje principal |
| **Spring Boot** | 3.4.2 | Framework backend |
| **Spring Data JPA** | 3.4.2 | ORM con Hibernate |
| **Maven** | 3.9+ | Gestor de dependencias |
| **Lombok** | managed | Reducción de boilerplate |
| **PostgreSQL** | runtime | BD de producción |
| **H2** | runtime | BD en memoria para desarrollo |

---

## 📋 Requisitos Previos

- **Java JDK 21** o superior
- **Maven 3.9+** (o usar el wrapper `./mvnw` incluido)
- **PostgreSQL** (solo para producción)

---

## 🚀 Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/MarioMunera1993/vulcano-api-grupo-1.git
cd vulcano-api-grupo-1
```

### 2. Ejecutar en modo desarrollo (H2 en memoria)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Ejecutar en modo producción (PostgreSQL)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

> El servidor arrancará en `http://localhost:8080`

---

## 🔑 Variables de Entorno

| Variable | Descripción | Perfil |
| :--- | :--- | :--- |
| `DB_USERNAME` | Usuario de PostgreSQL | prod |
| `DB_PASSWORD` | Contraseña de PostgreSQL | prod |
| `SPRING_PROFILES_ACTIVE` | Perfil activo (`dev` / `prod`) | ambos |

---

## 📡 Endpoints Principales

### Autenticación (`/api/auth`)

| Método | Ruta | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Iniciar sesión | Público |

### Usuarios (`/api/users`)

| Método | Ruta | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/users` | Crear usuario + perfil | Público |
| `GET` | `/api/users` | Listar todos los usuarios | Admin |
| `GET` | `/api/users/{id}` | Obtener usuario por ID | Autenticado |
| `PUT` | `/api/users/{id}` | Actualizar perfil | Autenticado |
| `DELETE` | `/api/users/{id}` | Eliminar usuario | Admin |
| `PATCH` | `/api/users/{id}/role` | Cambiar rol del usuario | Admin |
| `POST` | `/api/users/{userId}/courses/{courseId}` | Inscribirse en un curso | Autenticado |

### Cursos (`/api/courses`)

| Método | Ruta | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/courses` | Listar todos los cursos | Público |
| `GET` | `/api/courses/{id}` | Obtener curso por ID | Público |
| `POST` | `/api/courses` | Crear curso | Admin |
| `PUT` | `/api/courses/{id}` | Actualizar curso | Admin |
| `DELETE` | `/api/courses/{id}` | Eliminar curso | Admin |

### Módulos (`/api/modules`)

| Método | Ruta | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/modules` | Listar todos los módulos | Público |
| `GET` | `/api/modules/{id}` | Obtener módulo por ID | Público |
| `GET` | `/api/modules/course/{courseId}` | Módulos de un curso | Público |
| `POST` | `/api/modules/course/{courseId}` | Crear módulo | Admin (header `X-User-Id`) |
| `PUT` | `/api/modules/{id}` | Actualizar módulo | Admin (header `X-User-Id`) |
| `DELETE` | `/api/modules/{id}` | Eliminar módulo | Admin (header `X-User-Id`) |

### Reseñas (`/api/reviews`)

| Método | Ruta | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/reviews` | Listar todas las reseñas | Público |
| `GET` | `/api/reviews/{id}` | Obtener reseña por ID | Público |
| `POST` | `/api/reviews` | Crear reseña | Autenticado |
| `PUT` | `/api/reviews/{id}` | Actualizar reseña | Autenticado |
| `DELETE` | `/api/reviews/{id}` | Eliminar reseña | Autenticado |

### Horarios / Clases Privadas (`/api/schedules`)

| Método | Ruta | Descripción | Acceso |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/schedules/available/{expertId}` | Horarios disponibles de un experto | Público |
| `GET` | `/api/schedules/student/{studentId}` | Clases del estudiante | Autenticado |
| `POST` | `/api/schedules/student/{studentId}` | Agendar clase | Autenticado |
| `PUT` | `/api/schedules/{scheduleId}/student/{studentId}` | Modificar clase | Autenticado |
| `DELETE` | `/api/schedules/{scheduleId}/student/{studentId}` | Cancelar clase | Autenticado |

---

## 📊 Modelo de Datos

```
User ──(1:1)──> UserProfile
User ──(M:N)──> Course        (tabla: user_courses)
Course ──(1:N)──> Module
Course ──(1:N)──> Review
```

---

## 🧪 Reglas de Negocio

- **Username único:** No se permite crear dos usuarios con el mismo `username`.
- **Publicación de cursos:** Un curso no puede publicarse (`isPublished=true`) si no tiene al menos un módulo.
- **Control de roles:** Solo usuarios con rol `ADMIN` pueden crear, editar y eliminar cursos y módulos.
- **Inscripción:** Un usuario no puede inscribirse dos veces al mismo curso.

---

## 👥 Integrantes del Equipo

| Nombre | Rol principal | Usuario GitHub |
| :--- | :--- | :--- |
| Mario Múnera | Líder / Backend | [@MarioMunera1993](https://github.com/MarioMunera1993) |
| Albany Luciani | Frontend Lead | [@albanyluciani1](https://github.com/albanyluciani1) |
| Roque Aldana | Backend / DB Specialist | [@Julio28012020](https://github.com/Julio28012020) |
| Julio Correa | QA / Tester | [@Jcorrea24](https://github.com/Jcorrea24) |
| Sergio Montoya | UI/UX Designer | [@SerelbochaD3773](https://github.com/SerelbochaD3773) |

---

## 📄 Licencia

MIT License *(Recomendada para proyectos académicos).*