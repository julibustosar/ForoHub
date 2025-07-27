# 💬 ForoHub

**ForoHub** es una API REST construida con **Java** y **Gradle** que permite a los usuarios registrarse, autenticarse mediante **JWT**, y realizar publicaciones (posts). Cada acción sensible está protegida con mecanismos de autorización para garantizar que solo el usuario autenticado pueda interactuar con el contenido.

---

## 🛠️ Tecnologías utilizadas

- **Java 17+**
- **Gradle**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **Base de datos relacional (PostgreSQL o similar)**

---

## 🔐 Autenticación

- Los usuarios deben **registrarse** y luego **iniciar sesión**.
- Al iniciar sesión, se genera un **token JWT** que se debe incluir en las peticiones siguientes (en el encabezado `Authorization`).
- El token asegura que **solo el usuario autenticado pueda crear, editar o eliminar sus propios posts**.

---

## 🧩 Funcionalidades principales

### ✅ Registro y autenticación
- `POST /auth/register`: crea un nuevo usuario.
- `POST /auth/login`: autentica al usuario y devuelve el JWT.

### 📝 Gestión de publicaciones (posts)
- `GET /posts`: lista todos los posts.
- `POST /posts`: crea un nuevo post (requiere token).
- `PUT /posts/{id}`: actualiza un post (solo si es del usuario autenticado).
- `DELETE /posts/{id}`: elimina un post (solo si es del usuario autenticado).

