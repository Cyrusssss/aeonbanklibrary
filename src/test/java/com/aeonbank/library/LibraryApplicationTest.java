package com.aeonbank.library;

import com.aeonbank.library.common.Enums;
import com.aeonbank.library.common.ObjectMapperUtil;
import com.aeonbank.library.controller.BookController;
import com.aeonbank.library.controller.BorrowerController;
import com.aeonbank.library.controller.HealthCheckController;
import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BookServiceRequest;
import com.aeonbank.library.model.Book;
import com.aeonbank.library.model.Borrower;
import com.aeonbank.library.repository.BookRepository;
import com.aeonbank.library.repository.BorrowerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.ISBNValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Testing is done using in-memory H2 database
 * If there's any update on the sql script in the `db` folder in project root directory, please update it accordingly to the `test/resources/schema.sql`.
 * */

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class LibraryApplicationTest {

    @Autowired
    private BookController bookController;

    @Autowired
    private BorrowerController borrowerController;

    @Autowired
    private HealthCheckController healthCheckController;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    private ObjectMapper getObjectMapper(){
        return ObjectMapperUtil.getObjectMapper();
    }
    private ObjectMapper getObjectMapper(boolean excludeNull, boolean failUnknown){
        return ObjectMapperUtil.getObjectMapper(excludeNull, failUnknown);
    }

    private static final Faker faker = new Faker();
    private static final ISBNValidator validator = new ISBNValidator();

    @PostConstruct
    private void createDummyData(){
        // insert dummy Borrower
        for (int loop=1; loop<=5; loop++) {
            Borrower borrower = new Borrower();
            borrower.setName(String.join(" ", faker.name().firstName(), faker.name().lastName()));
            borrower.setEmail(faker.internet().emailAddress());
            borrower.setCreatedBy("system");
            borrower.setCreatedDate(new Date());
            int count = borrowerRepository.add(borrower);
            if (count != 1) {
                log.error("[createDummyData]failed to insert Borrower. count:{}", count);
                throw new RuntimeException("failed to insert Borrower");
            }
        }

        // insert dummy Book
        for (int loop=1; loop<=5; loop++) {
            Book book = new Book();
            book.setIsbn(genRandomIsbn());
            book.setTitle(faker.book().title());
            book.setAuthor(faker.book().author());
            book.setCreatedBy("system");
            book.setCreatedDate(new Date());
            int count = bookRepository.add(book);
            if (count != 1) {
                log.error("[createDummyData]failed to insert Book. count:{}", count);
                throw new RuntimeException("failed to insert Book");
            }
        }
    }

    private String genRandomIsbn() {
        if (new Random().nextBoolean()) {
            String isbn10;
            do {
                isbn10 = faker.code().isbn10();
            } while (!validator.isValidISBN10(isbn10));
            return isbn10;
        }
        else {
            String isbn13;
            do {
                isbn13 = faker.code().isbn13();
            } while (!validator.isValidISBN13(isbn13));
            return isbn13;
        }
    }

    @Test
    public void testBookList(){
        log.info("[testBookList]testList running");
        try {
            // call to controller
            ResponseEntity<BaseRequestResponse<BookServiceRequest>> responseEntity = bookController.list(null, null, null, null, null);

            // log response
            log.info("[testBookList]http status code: {}", responseEntity.getStatusCode().value());
            log.info("[testBookList]response body: {}", responseEntity.getBody());

            // check status code
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                log.error("[testBookList]return http status code is not 200");
                throw new RuntimeException("return http status code is not 200");
            }
            // check body
            if (responseEntity.getBody() == null) {
                log.error("[testBookList]return response body is null");
                throw new RuntimeException("return response body is null");
            }
            // check body content - status code
            if (responseEntity.getBody().getCode() == null || !responseEntity.getBody().getCode().equals(Enums.Status.SUCCESS.getCode())) {
                log.error("[testBookList]return http status code is not 200");
                throw new RuntimeException("return http status code is not 200");
            }
            // check body content - data
            if (responseEntity.getBody().getData() == null) {
                log.error("[testBookList]return content data is null");
                throw new RuntimeException("return content data is null");
            }
            List<Book> bookList = getObjectMapper().convertValue(responseEntity.getBody().getData(), new TypeReference<>() {});
            if (bookList == null || bookList.size() != 5) {
                log.error("[testBookList]bookList is null or size is not 5. size:{}", bookList == null ? "null" : bookList.size());
                throw new RuntimeException("return content data is null");
            }

            log.info("[testBookList]test completed successfully");
        } catch (Exception e) {
            log.error("[testBookList]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testList failed.");
        }
        log.info("[testBookList]end of testList");
    }

}
