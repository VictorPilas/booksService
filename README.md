# Books Service 

Este es un servicio REST desarrollado en **Java 17** utilizando **Spring Boot**, que permite gestionar y consultar informaci��n sobre libros y autores. Los datos se encuentran en un archivo `books.json` incluido en los recursos.

## Caracter��sticas principales 

- Consultar libros por autor.
- Obtener informaci��n detallada de libros, como fechas formateadas y n��mero de p��ginas.
- Manejo centralizado de excepciones.
- Documentaci��n interactiva con Swagger (OpenAPI).
- Tests unitarios para asegurar la calidad del servicio.

## Requisitos previos 

- Java 17 o superior.
- Maven 3.6+.

## Instalaci��n y ejecuci��n 

Clona el repositorio y navega a la carpeta ra��z del proyecto:

```bash
git clone https://github.com/VictorPilas/booksService
cd booksService
```

Para compilar y ejecutar el proyecto:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

La aplicaci��n estar�� disponible en:

```
http://localhost:8080
```

## Documentaci��n API

Accede a la documentaci��n Swagger en:

```
http://localhost:8080/swagger-ui/index.html
```

## Estructura del proyecto 

- `controller/`: Controladores REST (BooksController).
- `service/`: L��gica de negocio (BooksService).
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


