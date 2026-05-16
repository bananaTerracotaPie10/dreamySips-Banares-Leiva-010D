# ☕ DreamySips — Backend Microservicios

Sistema de backend basado en microservicios para la gestión de un catálogo de productos, clientes, pedidos, diseños y reseñas.

---

## 🧩 Microservicios

| Servicio | Puerto | Base de datos | Descripción |
|---|---|---|---|
| `service_cliente` | 8082 | `db_clientes` | Gestión de clientes y direcciones de envío |
| `service_catalogo` | 8083 | `db_catalogo` | Gestión de productos y categorías |
| `service_pedidos` | 8084 | `db_pedidos` | Gestión de pedidos y detalles de pedido |
| `service_diseno` | 8085 | `db_disenos` | Gestión de imágenes y diseños |
| `service_resenas` | 8086 | `db_resenas` | Gestión de reseñas de productos |

---

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring WebFlux** (WebClient para comunicación entre servicios)
- **Spring Cloud Gateway** (API Gateway)
- **MySQL** (XAMPP)
- **Lombok**
- **Maven**

---

## ▶️ Cómo correr el proyecto

### 1. Requisitos previos

- Tener instalado **Java 17+**
- Tener instalado **XAMPP** con MySQL activo
- Tener instalado **VSCode** con las extensiones de Spring Boot

### 2. Configurar la base de datos

Crear las siguientes bases de datos en MySQL (phpMyAdmin o consola):

```sql
CREATE DATABASE db_clientes;
CREATE DATABASE db_catalogo;
CREATE DATABASE db_pedidos;
CREATE DATABASE db_disenos;
CREATE DATABASE db_resenas;
```

### 3. Levantar los servicios

Abrir los proyectos en VSCode y desde el **Spring Boot Dashboard** iniciar cada uno en este orden:

```
1. XAMPP (MySQL)
2. service_cliente
3. service_catalogo
4. service_diseno
5. service_pedidos
6. service_resenas
7. API Gateway (último)
```

### 4. Las tablas se crean automáticamente

Cada servicio tiene configurado en su `application.properties`:

```properties
spring.jpa.hibernate.ddl-auto=update
```

No es necesario crear las tablas manualmente.

---

## ⚙️ Configuración `application.properties`

Cada servicio necesita su propio archivo con la conexión a MySQL y las URLs de los otros servicios. Ejemplo para `service_pedidos`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_pedidos
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

services.cliente.url=http://localhost:8082
services.catalogo.url=http://localhost:8083
```

---

## 🌐 API Gateway

Todos los servicios son accesibles a través del Gateway. Rutas configuradas:

| Servicio | Ruta |
|---|---|
| `service_cliente` | `/api/v1/clientes/**` |
| `service_catalogo` | `/api/v1/productos/**` |
| `service_pedidos` | `/api/v1/pedidos/**` |
| `service_diseno` | `/api/v1/disenos/**` |
| `service_resenas` | `/api/v1/resenas/**` |

---

## 📬 Probar con Postman

Una vez levantados todos los servicios, se pueden probar los endpoints desde **Postman**. Ejemplo:

```
GET  http://localhost:8084/api/v1/pedidos/1/completo
POST http://localhost:8083/api/v1/productos
GET  http://localhost:8086/api/v1/resenas/1/completo
GET  http://localhost:8085/api/v1/disenos
```
