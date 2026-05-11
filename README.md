# Microservicio de Productos — FULLSTACK3-PRODUCTOS

Microservicio REST desarrollado con **Spring Boot 3.5** que gestiona el catálogo de productos del sistema ComputerStore. Forma parte del proyecto Full Stack III (DSY2205).

---

## Tecnologías

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation, Actuator)
- Oracle Cloud (Wallet BBDDFS3)
- Lombok
- Docker
- JUnit 5 + Mockito

---

## Puerto

| Entorno | Puerto |
|---------|--------|
| Local   | `8080` |
| Docker  | `8080` |

---

## Endpoints disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/productos` | Lista todos los productos |
| `GET` | `/api/productos/{id}` | Obtiene un producto por ID |
| `POST` | `/api/productos` | Crea un nuevo producto (solo ADMIN) |
| `PUT` | `/api/productos/{id}` | Actualiza un producto existente (solo ADMIN) |
| `DELETE` | `/api/productos/{id}` | Elimina un producto (solo ADMIN) |

---

## Estructura del proyecto

```
src/
├── main/java/fullstack_productos/productos/
│   ├── controller/     ProductoController.java
│   ├── service/        ProductoService.java, ProductoServiceImpl.java
│   ├── repository/     ProductoRepository.java
│   ├── model/          Producto.java
│   └── exception/      ResourceNotFoundException.java, GlobalExceptionHandler.java
└── test/java/fullstack_productos/productos/
    ├── ProductoServiceImplTest.java
    └── ProductoControllerTest.java
```

---

## Modelo de datos

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador único |
| `nombre` | String | Nombre del producto (3–100 caracteres) |
| `descripcion` | String | Descripción del producto (máx. 200 caracteres) |
| `precio` | Double | Precio unitario (mayor a 0) |
| `stock` | Integer | Unidades disponibles (mínimo 0) |

---

## Levantar en local

### 1. Requisitos previos

- Java 17+
- Maven 3.8+
- Carpeta `Wallet_BBDDFS3/` presente en la raíz del proyecto

### 2. Compilar

```bash
mvn clean package -DskipTests
```

### 3. Ejecutar

```bash
mvn spring-boot:run
```

La API queda disponible en: `http://localhost:8080/api/productos`

---

## Levantar con Docker

```bash
# 1. Compilar primero
mvn clean package -DskipTests

# 2. Levantar contenedor
docker compose up --build
```

---

## Ejecutar pruebas unitarias

```bash
mvn test
```

Los tests cubren los métodos `listar`, `buscarPorId`, `guardar`, `actualizar` y `eliminar`, tanto a nivel de servicio (Mockito) como de controlador (MockMvc).

---

## Variables de entorno (Docker)

| Variable | Valor |
|----------|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:oracle:thin:@bbddfs3_tp?TNS_ADMIN=/app/wallet` |
| `SPRING_DATASOURCE_USERNAME` | `ADMIN` |
| `SPRING_DATASOURCE_PASSWORD` | `BBDD_fullstack2026` |
