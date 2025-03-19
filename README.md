# Books Service 

Este es un servicio REST desarrollado en **Java 17** utilizando **Spring Boot**, que permite gestionar y consultar informacion sobre libros y autores. Los datos se encuentran en un archivo `books.json` incluido en los recursos.

## Caracteristicas principales 

- Consultar libros por autor.
- Obtener informacion detallada de libros, como fechas formateadas y n¨²mero de p¨¢ginas.
- Manejo centralizado de excepciones.
- Documentacion interactiva con Swagger (OpenAPI).
- Tests unitarios para asegurar la calidad del servicio.

## Requisitos previos 

- Java 17 o superior.
- Maven 3.6+.

## Instalacion y ejecucion 

Clona el repositorio y navega a la carpeta raiz del proyecto:

```bash
git clone https://github.com/VictorPilas/booksService
cd booksService
```

Para compilar y ejecutar el proyecto (opcion 1):

```bash
./mvnw clean install
./mvnw spring-boot:run
```
## Docker (opcion 2)

```
docker-compose up -d
```

La aplicacion estara disponible en:

```
http://localhost:8080
```

## Documentacion API

Accede a la documentaci0n Swagger en:

```
http://localhost:8080/swagger-ui/index.html
```
## Docker

```
docker-compose up -d
```

## Estructura del proyecto 

- `controller/`: Controladores REST (BooksController).
- `service/`: L¨®gica de negocio (BooksService).
- `model/`: Clases de dominio (Book, Author, etc.).
- `exception/`: Manejo de excepciones personalizadas.
- `resources/books.json`: Fuente de datos de libros.
- `test/`: Pruebas unitarias.

## Ejecutar los tests 

Para ejecutar los tests unitarios:

```bash
./mvnw test
```

---


