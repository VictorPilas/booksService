package com.example.booksService.controller;

import com.example.booksService.exception.ResourceNotFoundException;
import com.example.booksService.model.Book;
import com.example.booksService.model.BookByAuthorResponse;
import com.example.booksService.model.BookFormatedDateResponse;
import com.example.booksService.model.BookPagesMaxMinResponse;
import com.example.booksService.model.BookTitleCountResponse;
import com.example.booksService.model.BookWithoutDateResponse;
import com.example.booksService.service.BooksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private static final Logger logger = LoggerFactory.getLogger(BooksController.class);

    @Autowired
    private BooksService booksService;

    // 1. Filtrar libros por páginas mínimas y palabra clave
    @GetMapping("/filter")
    public List<Book> filterBooks(@RequestParam int minPages, @RequestParam String keyword) {
        logger.info("Request to filter books with minPages={} and keyword={}", minPages, keyword);
        List<Book> result = booksService.filterBooks(minPages, keyword);
        if (result.isEmpty()) {
            logger.warn("No books found matching filter criteria: minPages={}, keyword={}", minPages, keyword);
            throw new ResourceNotFoundException("No books found matching the filter criteria.");
        }
        return result;
    }

    // 2. Obtener libros por autor (nombre + apellido)
    @GetMapping("/author")
    public List<Book> getBooksByAuthor(@RequestParam String authorName) {
        logger.info("Request to get books by author: {}", authorName);
        List<Book> result = booksService.getBooksByAuthor(authorName);
        if (result.isEmpty()) {
            logger.warn("No books found for author: {}", authorName);
            throw new ResourceNotFoundException("No books found for author: " + authorName);
        }
        return result;
    }

    // 3. Títulos alfabéticamente y libros por autor.
    @GetMapping("/sorted-count")
    public BookTitleCountResponse getSortedCount() {
        logger.info("Request to get sorted titles and books count by author");
        BookTitleCountResponse response = booksService.getBookTitleCountResponse();
        if (response == null || response.getTitles().isEmpty()) {
            logger.warn("No books available for sorting/count");
            throw new ResourceNotFoundException("No books available.");
        }
        return response;
    }

    // 4. Obtener libros con fecha formateada
    @GetMapping("/formatted-dates")
    public List<BookFormatedDateResponse> getFormattedDates() {
        logger.info("Request to get books with formatted dates");
        List<BookFormatedDateResponse> result = booksService.getFormattedDate();
        if (result.isEmpty()) {
            logger.warn("No books with formatted dates found.");
            throw new ResourceNotFoundException("No books with formatted dates found.");
        }
        return result;
    }

    // 5. Promedio, libro con más y menos páginas.
    @GetMapping("/average-max-min")
    public BookPagesMaxMinResponse getBookPagesMaxMinResponse() {
        logger.info("Request to get average, max and min pages of books");
        return booksService.getBookPagesMaxMinResponse();
    }

    // 6. Obtener libros por autor con word count
    @GetMapping("/author-wordcount")
    public List<BookByAuthorResponse> getBookByAuthorResponse() {
        logger.info("Request to get books by author with word count");
        List<BookByAuthorResponse> result = booksService.getBookByAuthorResponse();
        if (result.isEmpty()) {
            logger.warn("No books found.");
            throw new ResourceNotFoundException("No books found.");
        }
        return result;
    }

    // 7. Obtener libros sin fecha + autores duplicados
    @GetMapping("/without-date")
    public BookWithoutDateResponse getBooksWithoutDate() {
        logger.info("Request to get books without publication date and check duplicated authors");
        return booksService.getBookWithoutDateResponse();
    }

    // 8. Obtener top 3 libros más recientes
    @GetMapping("/most-recent")
    public List<Book> getTopMostRecentBooks() {
        logger.info("Request to get top 3 most recent books");
        List<Book> result = booksService.getTopMostRecentBooks();
        if (result.isEmpty()) {
            logger.warn("No recent books found.");
            throw new ResourceNotFoundException("No recent books found.");
        }
        return result;
    }
}
