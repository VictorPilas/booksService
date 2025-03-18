package com.example.booksService.service;

import com.example.booksService.model.Book;
import com.example.booksService.model.BookByAuthorResponse;
import com.example.booksService.model.BookFormatedDateResponse;
import com.example.booksService.model.BookPagesMaxMinResponse;
import com.example.booksService.model.BookTitleCountResponse;
import com.example.booksService.model.BookWithoutDateResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BooksService {
    private static final Logger logger = LoggerFactory.getLogger(BooksService.class);
    private static final long wordCount = 250;
    private static final int top = 3;
    private List<Book> books;

    @PostConstruct
    public void loadBooks() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            books = objectMapper.readValue(new URL("file:src/main/resources/books.json"), new TypeReference<List<Book>>() {});
            logger.info("Books loaded successfully, total books: {}", books.size());
        } catch (Exception e) {
            logger.error("Error loading books: {}", e.getMessage(), e);
            books = new ArrayList<>();
        }
    }

    public List<Book> filterBooks(int minPages, String keyword) {
        logger.info("Filtering books with minPages={} and keyword={}", minPages, keyword);
        List<Book> filteredBooks = books.stream()
                .filter(book -> book.getPages() > minPages && book.getTitle().contains(keyword))
                .collect(Collectors.toList());
        logger.info("Filtered books count: {}", filteredBooks.size());
        return filteredBooks;
    }

    public List<Book> getBooksByAuthor(String authorName) {
        logger.info("Getting books by author: {}", authorName);
        List<Book> result = books.stream()
                .filter(book -> {
                    String authorFullName = book.getAuthor().getName()
                            + (book.getAuthor().getFirstSurname() != null ? " " + book.getAuthor().getFirstSurname() : "");
                    return authorFullName.equalsIgnoreCase(authorName);
                })
                .collect(Collectors.toList());
        logger.info("Books found for author {}: {}", authorName, result.size());
        return result;
    }

    public List<String> getSortedTitles() {
        logger.info("Sorting book titles alphabetically");
        List<String> sortedTitles = books.stream()
                .map(Book::getTitle)
                .sorted()
                .collect(Collectors.toList());
        logger.info("Sorted titles count: {}", sortedTitles.size());
        return sortedTitles;
    }

    public Map<String, Long> countBooksByAuthor() {
        logger.info("Counting books by author");
        Map<String, Long> countMap = books.stream()
                .collect(Collectors.groupingBy(book -> book.getAuthor().getName()
                        + (book.getAuthor().getFirstSurname() != null ? " " + book.getAuthor().getFirstSurname() : ""), Collectors.counting()));
        logger.info("Counted books by {} authors", countMap.size());
        return countMap;
    }

    public BookTitleCountResponse getBookTitleCountResponse() {
        logger.info("Generating BookTitleCountResponse");
        return new BookTitleCountResponse(getSortedTitles(), countBooksByAuthor());
    }

    public List<BookFormatedDateResponse> getFormattedDate() {
        logger.info("Formatting book publication dates");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<BookFormatedDateResponse> formattedList = books.stream()
                .map(book -> {
                    String formattedDate = null;
                    if (book.getPublicationTimestamp() != null) {
                        formattedDate = formatter.format(book.getPublicationTimestamp());
                    }
                    return new BookFormatedDateResponse(book, formattedDate);
                })
                .collect(Collectors.toList());
        logger.info("Formatted dates for {} books", formattedList.size());
        return formattedList;
    }

    public double getAveragePages() {
        logger.info("Calculating average number of pages");
        double avg = books.stream().mapToInt(Book::getPages).average().orElse(0);
        logger.info("Average pages: {}", avg);
        return avg;
    }

    public Book getBookWithMostPages() {
        logger.info("Finding book with most pages");
        Book maxBook = books.stream().max(Comparator.comparingInt(Book::getPages)).orElse(null);
        logger.info("Book with most pages: {}", maxBook != null ? maxBook.getTitle() : "None");
        return maxBook;
    }

    public Book getBookWithLeastPages() {
        logger.info("Finding book with least pages");
        Book minBook = books.stream().min(Comparator.comparingInt(Book::getPages)).orElse(null);
        logger.info("Book with least pages: {}", minBook != null ? minBook.getTitle() : "None");
        return minBook;
    }

    public BookPagesMaxMinResponse getBookPagesMaxMinResponse() {
        logger.info("Generating BookPagesMaxMinResponse");
        return new BookPagesMaxMinResponse(getAveragePages(), getBookWithMostPages(), getBookWithLeastPages());
    }

    public List<BookByAuthorResponse> getBookByAuthorResponse() {
        logger.info("Generating BookByAuthorResponse with word count");
        List<BookByAuthorResponse> response = countBooksByAuthor().keySet().stream().map(authorName -> {
            List<Book> booksByAuthor = getBooksByAuthor(authorName);
            Long wordAmount = booksByAuthor.stream().mapToLong(Book::getPages).sum() * wordCount;
            return new BookByAuthorResponse(authorName, booksByAuthor, wordAmount);
        }).collect(Collectors.toList());
        logger.info("Generated response for {} authors", response.size());
        return response;
    }

    public BookWithoutDateResponse getBookWithoutDateResponse() {
        logger.info("Checking for duplicated authors and books without publication date");
        boolean duplicated = countBooksByAuthor().values().stream().anyMatch(count -> count > 1);
        List<Book> booksWithoutDate = books.stream().filter(book -> book.getPublicationTimestamp() == null).collect(Collectors.toList());
        logger.info("Found {} books without date and duplicated authors: {}", booksWithoutDate.size(), duplicated);
        return new BookWithoutDateResponse(duplicated, booksWithoutDate);
    }

    public List<Book> getTopMostRecentBooks() {
        logger.info("Fetching top {} most recent books", top);
        List<Book> recentBooks = books.stream()
                .filter(book -> book.getPublicationTimestamp() != null)
                .sorted(Comparator.comparingLong(Book::getPublicationTimestamp).reversed())
                .limit(top)
                .collect(Collectors.toList());
        logger.info("Found {} recent books", recentBooks.size());
        return recentBooks;
    }
}
