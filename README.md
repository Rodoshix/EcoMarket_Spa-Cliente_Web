# EcoMarket SPA - Microservicio Cliente Web

Este microservicio forma parte del sistema distribuido **EcoMarket SPA** para proyecto semestral FullStack_1, y se encarga de gestionar funcionalidades del cliente final como:

- Carrito de compras temporal
- Lista de productos favoritos
- Historial de navegación

Además, **consume microservicios externos** para validar usuarios y productos, utilizando autenticación JWT.

---

## Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security (sólo en usuarios-auth)
- RestTemplate
- Lombok
- MySQL (XAMPP)
- Postman (para pruebas)
- JWT (para validación de seguridad con usuarios-auth-service)
---

## Configuración del entorno

### Base de datos

- Motor: MySQL (MariaDB compatible)
- Nombre: `clienteweb_db`
- Usuario: `root`
- Contraseña: *(vacía por defecto en XAMPP)*

### Archivo `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clienteweb_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8083
```

---

## Funcionalidades

- Gestionar historial de navegación de productos por usuario.
- Añadir o eliminar productos favoritos.
- Agregar, consultar y vaciar el carrito de compras.
- Validación de usuario con token JWT.
- Consulta de productos desde `inventario-service`.
- Preparación de la venta: este microservicio puede estructurar los productos del carrito para enviar a `venta-service`.

---

## Arquitectura

Este microservicio consume:

- `usuarios-auth-service` (`http://localhost:8081`) → Validación JWT y datos del usuario
- `inventario-service` (`http://localhost:8082`) → Información de productos por ID

---

## Endpoints disponibles

### Carrito

| Método | Ruta                                         | Descripción                         |
|--------|----------------------------------------------|-------------------------------------|
| GET    | `/api/carrito/{email}`                       | Obtener el carrito de un usuario    |
| POST   | `/api/carrito`                               | Agregar producto al carrito         |
| DELETE | `/api/carrito/{email}/producto/{idProducto}` | Eliminar un producto del carrito    |
| DELETE | `/api/carrito/{email}`                       | Vaciar todo el carrito              |

### Favoritos

| Método | Ruta                                         | Descripción                            |
|--------|----------------------------------------------|----------------------------------------|
| GET    | `/api/favoritos/{email}`                    | Obtener lista de favoritos             |
| POST   | `/api/favoritos`                            | Agregar producto a favoritos           |
| DELETE | `/api/favoritos/{email}/producto/{idProducto}` | Quitar producto de favoritos       |

### Historial

| Método | Ruta                         | Descripción                       |
|--------|------------------------------|-----------------------------------|
| GET    | `/api/historial/{email}`     | Ver historial de navegación       |
| POST   | `/api/historial`             | Registrar una navegación de producto |

---

## Integración con Venta-Service

Aunque este microservicio no registra ventas, permite:

- Armar la estructura de datos del carrito del usuario.
- Consultar productos con stock desde `inventario-service`.
- Validar el usuario que está comprando con `usuarios-auth-service`.

El frontend puede utilizar estos datos para enviarlos a `venta-service`.

---

## Cómo probar con Postman
Importa la colección incluida:  
**`EcoMarket - Cliente_Web-service.postman_collection.json`**

1. **Login para obtener token JWT:**

   `POST http://localhost:8081/auth/login`

   Body:
   ```json
   {
     "email": "cliente@example.com",
     "password": "cliente123"
   }
   ```

2. **Usa el token para los demás endpoints:**

   En Headers:
   ```
   Authorization: Bearer TU_TOKEN
   ```

3. **Ejemplo para agregar producto al carrito:**

   `POST http://localhost:8083/api/carrito`

   Body:
   ```json
   {
     "emailUsuario": "cliente@example.com",
     "idProducto": 1,
     "nombreProducto": "",
     "cantidad": 1,
     "precioUnitario": 0
   }
   ```

---

## Integración con microservicios

- Usa `RestTemplate` para hacer llamadas HTTP seguras.
- Reenvía el token JWT como `Authorization: Bearer ...` al microservicio `usuarios-auth`.
- Consulta productos a través de `GET /api/productos/{id}` al microservicio `inventario`.

---
