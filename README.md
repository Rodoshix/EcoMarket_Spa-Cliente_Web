# EcoMarket SPA - Microservicio Cliente Web

Este microservicio forma parte del sistema distribuido **EcoMarket SPA**, y se encarga de gestionar funcionalidades del cliente final como:

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

---

## Arquitectura

Este microservicio consume:

- `usuarios-auth-service` (`http://localhost:8081`) → Validación JWT y datos del usuario
- `inventario-service` (`http://localhost:8082`) → Información de productos por ID

---

## Endpoints expuestos por cliente-web

### Requieren token JWT

| Método | Ruta                                | Descripción                           |
|--------|-------------------------------------|---------------------------------------|
| POST   | `/api/carrito`                      | Agrega un producto al carrito         |
| DELETE | `/api/carrito/{email}`              | Vacía el carrito del usuario          |
| DELETE | `/api/carrito/{email}/producto/{id}`| Elimina un producto del carrito       |
| POST   | `/api/favoritos`                    | Agrega un producto a favoritos        |
| DELETE | `/api/favoritos/{email}/producto/{id}` | Elimina un producto de favoritos   |
| POST   | `/api/historial`                    | Registra una navegación de producto   |
| GET    | `/api/carrito/{email}`              | Obtiene el carrito del usuario        |
| GET    | `/api/favoritos/{email}`            | Obtiene la lista de favoritos         |
| GET    | `/api/historial/{email}`            | Obtiene el historial de navegación    |

---

## Cómo probar con Postman
Importa la colección incluida:  
**`EcoMarket-ClienteWeb-JWT-Rutas-Corregidas-Postman`**

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
