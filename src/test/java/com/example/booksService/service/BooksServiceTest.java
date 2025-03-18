package com.example.booksService.service;

import com.example.booksService.model.Author;
import com.example.booksService.model.Book;
import com.example.booksService.model.BookByAuthorResponse;
import com.example.booksService.model.BookFormatedDateResponse;
import com.example.booksService.model.BookPagesMaxMinResponse;
import com.example.booksService.model.BookTitleCountResponse;
import com.example.booksService.model.BookWithoutDateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BooksServiceTest {

    private BooksService booksService;
    private List<Book> mockBooks;

    @BeforeEach
    public void setUp() {
        booksService = new BooksService();

        mockBooks = new ArrayList<>();
        mockBooks.add(new Book(1, "Book One", 200, "", new Author("John", "Doe", ""), 1609459200000L)); // 2021-01-01
        mockBooks.add(new Book(2, "Book Two", 150, "", new Author("Jane", "Smith", ""), null));
        mockBooks.add(new Book(3, "Book Three", 300, "", new Author("John", "Doe", ""), 1672531200000L)); // 2023-01-01

        ReflectionTestUtils.setField(booksService, "books", mockBooks);
    }

    @Test
    public void testFilterBooks() {
        List<Book> result = booksService.filterBooks(180, "Two");
        assertEquals(3, result.size()); // Book One (pages > 180) and Book Two (title contains "Two")
    }

    @Test
    public void testGetBooksByAuthor() {
        List<Book> result = booksService.getBooksByAuthor("John Doe");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(b -> b.getAuthor().getName().equals("John")));
    }

    @Test
    public void testGetSortedTitles() {
        List<String> titles = booksService.getSortedTitles();
        assertEquals(Arrays.asList("Book One", "Book Three", "Book Two"), titles);
    }

    @Test
    public void testCountBooksByAuthor() {
        Map<String, Long> countMap = booksService.countBooksByAuthor();
        assertEquals(2, countMap.get("John Doe"));
        assertEquals(1, countMap.get("Jane Smith"));
    }

    @Test
    public void testGetBookTitleCountResponse() {
        BookTitleCountResponse response = booksService.getBookTitleCountResponse();
        assertEquals(3, response.getTitles().size());
        assertEquals(2, response.getAuthorCount().get("John Doe"));
    }

    @Test
    public void testGetFormattedDate() {
        List<BookFormatedDateResponse> responses = booksService.getFormattedDate();
        assertEquals("2021-01-01", responses.get(0).getFormatedDate());
        assertNull(responses.get(1).getFormatedDate());
        assertEquals("2023-01-01", responses.get(2).getFormatedDate());
    }

    @Test
    public void testGetAveragePages() {
        double avg = booksService.getAveragePages();
        assertEquals(216.66, avg, 0.1);
    }

    @Test
    public void testGetBookWithMostPages() {
        Book maxBook = booksService.getBookWithMostPages();
        assertEquals("Book Three", maxBook.getTitle());
    }

    @Test
    public void testGetBookWithLeastPages() {
        Book minBook = booksService.getBookWithLeastPages();
        assertEquals("Book Two", minBook.getTitle());
    }

    @Test
    public void testGetBookPagesMaxMinResponse() {
        BookPagesMaxMinResponse response = booksService.getBookPagesMaxMinResponse();
        assertEquals(216.66, response.getAverage(), 0.1);
        assertEquals("Book Three", response.getMostPages().getTitle());
        assertEquals("Book Two", response.getMinPages().getTitle());
    }

    @Test
    public void testGetBookByAuthorResponse() {
        List<BookByAuthorResponse> responses = booksService.getBookByAuthorResponse();
        assertEquals(2, responses.size());
        for (BookByAuthorResponse res : responses) {
            if (res.getAuthorName().equals("John Doe")) {
                assertEquals(2, res.getBooks().size());
                assertEquals(125000L, res.getWordCount()); // (200+300)*250
            }
        }
    }

    @Test
    public void testGetBookWithoutDateResponse() {
        BookWithoutDateResponse response = booksService.getBookWithoutDateResponse();
        assertTrue(response.isDuplicated());
        assertEquals(1, response.getBooks().size());
        assertEquals("Book Two", response.getBooks().get(0).getTitle());
    }

    @Test
    public void testGetTopMostRecentBooks() {
        List<Book> recentBooks = booksService.getTopMostRecentBooks();
        assertEquals(2, recentBooks.size());
        assertEquals("Book Three", recentBooks.get(0).getTitle());
    }
}
