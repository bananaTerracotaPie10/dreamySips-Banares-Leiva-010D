# ☕ DreamySips — Backend basado en Microservicios

## 📖 Descripción General

DreamySips es una plataforma de comercio electrónico desarrollada utilizando una arquitectura basada en microservicios con Spring Boot. El sistema permite administrar usuarios, clientes, productos, diseños personalizados, pedidos, pagos, despachos, notificaciones, reseñas y listas de deseos.

Cada microservicio es independiente y cuenta con su propia responsabilidad dentro del sistema, facilitando la escalabilidad, mantenibilidad y despliegue de la aplicación. La comunicación entre servicios se realiza mediante API REST y todas las solicitudes externas son centralizadas a través de un API Gateway.

---

# 🏗️ Arquitectura de Microservicios

| Microservicio   | Puerto | Función                                                       |
| --------------- | ------ | ------------------------------------------------------------- |
| API Gateway     | 8080   | Punto único de acceso a todos los microservicios              |
| Usuarios        | 8081   | Gestiona registro, autenticación y administración de usuarios |
| Clientes        | 8082   | Administra información personal y de contacto de los clientes |
| Catálogo        | 8083   | Gestiona productos, categorías, stock y precios               |
| Diseño          | 8084   | Administra diseños personalizados para productos              |
| Pedidos         | 8085   | Gestiona la creación y seguimiento de pedidos                 |
| Reseñas         | 8086   | Administra opiniones y valoraciones de productos              |
| Lista de Deseos | 8087   | Permite guardar productos para futuras compras                |
| Notificaciones  | 8088   | Envía avisos y mensajes relacionados con eventos del sistema  |
| Despacho        | 8089   | Gestiona la logística y seguimiento de envíos                 |
| Pago            | 8090   | Procesa y registra pagos asociados a pedidos                  |

---

# 📚 Bibliotecas Utilizadas

El proyecto utiliza las siguientes dependencias y bibliotecas:

* Spring Boot Starter Web
* Spring Boot Starter Data JPA
* Spring Boot Starter Validation
* Spring Security
* JWT (JSON Web Token)
* Spring Cloud Gateway
* Spring WebFlux
* SpringDoc OpenAPI (Swagger)
* Lombok
* MySQL Connector J
* Maven

---

# 🔧 Herramientas de Instalación

Para ejecutar el proyecto se requiere:

* Java JDK 17 o superior
* Apache Maven
* MySQL Server o XAMPP
* Visual Studio Code o IntelliJ IDEA
* Git
* GitHub
* Postman
* Navegador web compatible con Swagger UI

---

# 🗄️ Configuración de Base de Datos

Crear las bases de datos necesarias para cada microservicio:

```sql
CREATE DATABASE db_usuarios;
CREATE DATABASE db_clientes;
CREATE DATABASE db_catalogo;
CREATE DATABASE db_disenos;
CREATE DATABASE db_pedidos;
CREATE DATABASE db_resenas;
CREATE DATABASE db_listadeseos;
CREATE DATABASE db_notificaciones;
CREATE DATABASE db_despacho;
CREATE DATABASE db_pago;
```

---

# ⚙️ Configuración General

Cada microservicio debe contar con su propio archivo `application.yml` o `application.properties`.

Ejemplo de configuración:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_catalogo
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Las tablas son generadas automáticamente gracias a:

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

# ▶️ Ejecución del Proyecto

## Paso 1

Iniciar MySQL desde XAMPP o el servidor MySQL instalado.

## Paso 2

Levantar los microservicios en el siguiente orden:

1. Usuarios (8081)
2. Clientes (8082)
3. Catálogo (8083)
4. Diseño (8084)
5. Pedidos (8085)
6. Reseñas (8086)
7. Lista de Deseos (8087)
8. Notificaciones (8088)
9. Despacho (8089)
10. Pago (8090)
11. API Gateway (8080)

## Paso 3

Verificar que todos los servicios se encuentren ejecutándose correctamente.

---

# 🌐 Rutas del API Gateway

Todos los microservicios son accesibles mediante el API Gateway.

| Microservicio      | Ruta Base                   |
| ------------------ | --------------------------- |
| Usuarios           | `/api/v1/usuarios/**`       |
| Autenticación      | `/api/v1/auth/**`           |
| Clientes           | `/api/v1/clientes/**`       |
| Direcciones        | `/api/v1/direcciones/**`    |
| Catálogo           | `/api/v1/productos/**`      |
| Categorías         | `/api/v1/categoria/**`      |
| Diseño             | `/api/v1/disenos/**`        |
| Pedidos            | `/api/v1/pedidos/**`        |
| Detalles de Pedido | `/api/v1/detalles/**`       |
| Reseñas            | `/api/v1/resenas/**`        |
| Lista de Deseos    | `/api/v1/listadeseos/**`    |
| Notificaciones     | `/api/v1/notificaciones/**` |
| Despacho           | `/api/v1/despachos/**`      |
| Pago               | `/api/v1/pagos/**`          |
| Transacciones      | `/api/v1/transacciones/**`  |

---

# 📬 Ejemplos de Rutas para API REST

## Usuarios

```http
POST http://localhost:8080/api/v1/auth/login
```

```http
POST http://localhost:8080/api/v1/auth/register
```

```http
GET http://localhost:8080/api/v1/usuarios
```

---

## Clientes

```http
GET http://localhost:8080/api/v1/clientes
```

```http
GET http://localhost:8080/api/v1/clientes/1
```

```http
POST http://localhost:8080/api/v1/clientes
```

---

## Catálogo

```http
GET http://localhost:8080/api/v1/productos
```

```http
GET http://localhost:8080/api/v1/productos/1
```

```http
POST http://localhost:8080/api/v1/productos
```

---

## Categorías

```http
GET http://localhost:8080/api/v1/categoria
```

---

## Diseño

```http
GET http://localhost:8080/api/v1/disenos
```

```http
POST http://localhost:8080/api/v1/disenos
```

---

## Pedidos

```http
GET http://localhost:8080/api/v1/pedidos
```

```http
GET http://localhost:8080/api/v1/pedidos/1
```

```http
POST http://localhost:8080/api/v1/pedidos
```

---

## Detalles de Pedido

```http
GET http://localhost:8080/api/v1/detalles
```

---

## Reseñas

```http
GET http://localhost:8080/api/v1/resenas
```

```http
POST http://localhost:8080/api/v1/resenas
```

---

## Lista de Deseos

```http
GET http://localhost:8080/api/v1/listadeseos
```

```http
POST http://localhost:8080/api/v1/listadeseos
```

---

## Notificaciones

```http
GET http://localhost:8080/api/v1/notificaciones
```

```http
POST http://localhost:8080/api/v1/notificaciones
```

---

## Despacho

```http
GET http://localhost:8080/api/v1/despachos
```

```http
POST http://localhost:8080/api/v1/despachos
```

```http
PUT http://localhost:8080/api/v1/despachos/1
```

---

## Pago

```http
GET http://localhost:8080/api/v1/pagos
```

```http
POST http://localhost:8080/api/v1/pagos
```

```http
GET http://localhost:8080/api/v1/transacciones
```

---

# 📖 Rutas para Swagger

DreamySips utiliza SpringDoc OpenAPI para documentar todos los microservicios.

## Swagger Centralizado mediante API Gateway

```http
http://localhost:8080/swagger-ui.html
```

o

```http
http://localhost:8080/swagger-ui/index.html
```

Desde esta interfaz se puede acceder a la documentación de:

* Usuarios API
* Clientes API
* Catálogo API
* Diseños API
* Pedidos API
* Reseñas API
* Lista de Deseos API
* Notificaciones API
* Despacho API
* Pago API

---

## Endpoints de Documentación OpenAPI

### Usuarios

```http
http://localhost:8080/docs-usuarios/v3/api-docs
```

### Clientes

```http
http://localhost:8080/docs-clientes/v3/api-docs
```

### Catálogo

```http
http://localhost:8080/docs-catalogo/v3/api-docs
```

### Diseños

```http
http://localhost:8080/docs-disenos/v3/api-docs
```

### Pedidos

```http
http://localhost:8080/docs-pedidos/v3/api-docs
```

### Reseñas

```http
http://localhost:8080/docs-resenas/v3/api-docs
```

### Lista de Deseos

```http
http://localhost:8080/docs-listadeseos/v3/api-docs
```

### Notificaciones

```http
http://localhost:8080/docs-notificaciones/v3/api-docs
```

### Despacho

```http
http://localhost:8080/docs-despacho/v3/api-docs
```

### Pago

```http
http://localhost:8080/docs-pago/v3/api-docs
```

---

# 🔄 Comunicación entre Microservicios

La comunicación entre los microservicios se realiza mediante solicitudes HTTP REST.

Flujo general de compra:

1. El usuario inicia sesión mediante el microservicio de Usuarios.
2. El cliente consulta productos en el microservicio de Catálogo.
3. Puede guardar productos en Lista de Deseos.
4. Puede personalizar productos mediante el microservicio de Diseño.
5. Genera un pedido mediante el microservicio de Pedidos.
6. El microservicio de Pago registra la transacción.
7. El microservicio de Despacho crea el envío.
8. El microservicio de Notificaciones informa cambios de estado.
9. El cliente puede registrar una reseña del producto recibido.

---

# 🧪 Pruebas

Las APIs pueden probarse mediante:

* Swagger UI
* Postman
* Thunder Client
* Insomnia

Ejemplo:

```http
GET http://localhost:8080/api/v1/productos
```

```http
POST http://localhost:8080/api/v1/auth/login
```

```http
GET http://localhost:8080/api/v1/pedidos
```

---

# 👨‍💻 Proyecto Académico

Proyecto desarrollado utilizando arquitectura de microservicios con Spring Boot, API Gateway, JWT, Swagger OpenAPI y MySQL como sistema gestor de bases de datos.
