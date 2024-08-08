package com.aeonbank.library;

import com.aeonbank.library.common.Enums;
import com.aeonbank.library.common.ObjectMapperUtil;
import com.aeonbank.library.controller.BookController;
import com.aeonbank.library.controller.BorrowerController;
import com.aeonbank.library.controller.HealthCheckController;
import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BookServiceRequest;
import com.aeonbank.library.dto.BorrowerServiceRequest;
import com.aeonbank.library.model.Book;
import com.aeonbank.library.model.Borrower;
import com.aeonbank.library.repository.BookRepository;
import com.aeonbank.library.repository.BorrowerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Testing is done using in-memory H2 database
 * If there's any update on the sql script in the `db` folder in project root directory, please update it accordingly to the `test/resources/schema.sql`.
 * Mysql script is not fully compatible to H2, please convert it before update to schema.sql
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

    @Test // need to run following sequentially, without this junit will just run randomly, else will get record locking error.
    public void runTest(){
        // test book
        testBookList();
        testBookGet();
        testBookAdd();
        testBookUpd();
        testBookDel();

        // test borrower
        testBorrowerList();
        testBorrowerGet();
        testBorrowerAdd();
        testBorrowerUpd();
        testBorrowerDel();
    }

    //@Test
    public void testBookList(){
        log.info("[testBookList]testList running");
        try {
            // call to controller
            ResponseEntity<BaseRequestResponse<BookServiceRequest>> responseEntity = bookController.list(null, null, null, null, null);

            // validation
            Object data;
            try {
                data = getBookResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBookList]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            if (data == null) {
                log.error("[testBookList]return content data is null");
                throw new Exception("return content data is null");
            }

            List<Book> bookList = getObjectMapper().convertValue(data, new TypeReference<>() {});
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

    //@Test
    public void testBookGet(){
        log.info("[testBookGet]testBookGet running");
        try {
            // call to controller
            ResponseEntity<BaseRequestResponse<BookServiceRequest>> responseEntity = bookController.get(1L);

            // validation
            Object data;
            try {
                data = getBookResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBookGet]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            if (data == null) {
                log.error("[testBookGet]return content data is null");
                throw new Exception("return content data is null");
            }

            Book book = getObjectMapper().convertValue(data, new TypeReference<>() {});
            if (book == null) {
                log.error("[testBookGet]book is null");
                throw new RuntimeException("book is null");
            }
            if (book.getId() == null || StringUtils.isBlank(book.getIsbn()) || StringUtils.isBlank(book.getTitle()) || StringUtils.isBlank(book.getAuthor())) {
                log.error("[testBookGet]book data is invalid. book:{}", book);
                throw new RuntimeException("book data is invalid");
            }

            log.info("[testBookGet]test completed successfully");
        } catch (Exception e) {
            log.error("[testBookGet]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBookGet failed.");
        }
        log.info("[testBookGet]end of testBookGet");
    }

    //@Test
    public void testBookAdd(){
        log.info("[testBookAdd]testBookAdd running");
        try {
            // create request
            BookServiceRequest request = new BookServiceRequest();
            request.setIsbn(genRandomIsbn());
            request.setTitle(faker.book().title());
            request.setAuthor(faker.book().author());

            // call to controller
            ResponseEntity<BaseRequestResponse<BookServiceRequest>> responseEntity = bookController.add(request);

            // validation
            Object data;
            try {
                data = getBookResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBookAdd]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            log.info("[testBookAdd]test completed successfully");
        } catch (Exception e) {
            log.error("[testBookAdd]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBookAdd failed.");
        }
        log.info("[testBookAdd]end of testBookAdd");
    }

    //@Test
    public void testBookUpd(){
        log.info("[testBookUpd]testBookUpd running");
        try {
            // create request
            BookServiceRequest request = new BookServiceRequest();
            request.setIsbn(genRandomIsbn());
            request.setTitle(faker.book().title());
            request.setAuthor(faker.book().author());

            // call to controller
            ResponseEntity<BaseRequestResponse<BookServiceRequest>> responseEntity = bookController.update(6L, request);

            // validation
            Object data;
            try {
                data = getBookResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBookUpd]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            log.info("[testBookUpd]test completed successfully");
        } catch (Exception e) {
            log.error("[testBookUpd]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBookUpd failed.");
        }
        log.info("[testBookUpd]end of testBookUpd");
    }

    //@Test
    public void testBookDel(){
        log.info("[testBookDel]testBookDel running");
        try {
            // call to controller
            ResponseEntity<BaseRequestResponse<BookServiceRequest>> responseEntity = bookController.delete(6L);

            // validation
            Object data;
            try {
                data = getBookResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBookDel]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            log.info("[testBookDel]test completed successfully");
        } catch (Exception e) {
            log.error("[testBookDel]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBookDel failed.");
        }
        log.info("[testBookDel]end of testBookDel");
    }

    //@Test
    public void testBorrowerList(){
        log.info("[testBorrowerList]testBorrowerList running");
        try {
            // call to controller
            ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> responseEntity = borrowerController.list(null, null);

            // validation
            Object data;
            try {
                data = getBorrowerResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBorrowerList]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            if (data == null) {
                log.error("[testBorrowerList]return content data is null");
                throw new Exception("return content data is null");
            }

            List<Borrower> borrowerList = getObjectMapper().convertValue(data, new TypeReference<>() {});
            if (borrowerList == null || borrowerList.size() != 5) {
                log.error("[testBorrowerList]borrowerList is null or size is not 5. size:{}", borrowerList == null ? "null" : borrowerList.size());
                throw new RuntimeException("return content data is null");
            }

            log.info("[testBorrowerList]test completed successfully");
        } catch (Exception e) {
            log.error("[testBorrowerList]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBorrowerList failed.");
        }
        log.info("[testBorrowerList]end of testBorrowerList");
    }

    //@Test
    public void testBorrowerGet(){
        log.info("[testBorrowerGet]testBorrowerGet running");
        try {
            // call to controller
            ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> responseEntity = borrowerController.get(1L);

            // validation
            Object data;
            try {
                data = getBorrowerResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBorrowerGet]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            if (data == null) {
                log.error("[testBorrowerGet]return content data is null");
                throw new Exception("return content data is null");
            }

            Borrower borrower = getObjectMapper().convertValue(data, new TypeReference<>() {});
            if (borrower == null) {
                log.error("[testBorrowerGet]borrower is null");
                throw new RuntimeException("borrower is null");
            }
            if (borrower.getId() == null || StringUtils.isBlank(borrower.getName()) || StringUtils.isBlank(borrower.getEmail())) {
                log.error("[testBorrowerGet]borrower data is invalid. borrower:{}", borrower);
                throw new RuntimeException("borrower data is invalid");
            }

            log.info("[testBorrowerGet]test completed successfully");
        } catch (Exception e) {
            log.error("[testBorrowerGet]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBorrowerGet failed.");
        }
        log.info("[testBorrowerGet]end of testBorrowerGet");
    }

    //@Test
    public void testBorrowerAdd(){
        log.info("[testBorrowerAdd]testBorrowerAdd running");
        try {
            // create request
            BorrowerServiceRequest request = new BorrowerServiceRequest();
            request.setName(String.join(" ", faker.name().firstName(), faker.name().lastName()));
            request.setEmail(faker.internet().emailAddress());

            // call to controller
            ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> responseEntity = borrowerController.add(request);

            // validation
            Object data;
            try {
                data = getBorrowerResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBorrowerAdd]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            log.info("[testBorrowerAdd]test completed successfully");
        } catch (Exception e) {
            log.error("[testBorrowerAdd]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBorrowerAdd failed.");
        }
        log.info("[testBorrowerAdd]end of testBorrowerAdd");
    }

    //@Test
    public void testBorrowerUpd(){
        log.info("[testBorrowerUpd]testBorrowerUpd running");
        try {
            // create request
            BorrowerServiceRequest request = new BorrowerServiceRequest();
            request.setName(String.join(" ", faker.name().firstName(), faker.name().lastName()));
            request.setEmail(faker.internet().emailAddress());

            // call to controller
            ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> responseEntity = borrowerController.update(6L, request);

            // validation
            Object data;
            try {
                data = getBorrowerResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBorrowerUpd]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            log.info("[testBorrowerUpd]test completed successfully");
        } catch (Exception e) {
            log.error("[testBorrowerUpd]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBorrowerUpd failed.");
        }
        log.info("[testBorrowerUpd]end of testBorrowerUpd");
    }

    //@Test
    public void testBorrowerDel(){
        log.info("[testBorrowerDel]testBorrowerDel running");
        try {
            // call to controller
            ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> responseEntity = borrowerController.delete(6L);

            // validation
            Object data;
            try {
                data = getBorrowerResponseData(responseEntity);
            } catch (Exception e) {
                log.error("[testBorrowerDel]get response data failed. error >>> {}", e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

            log.info("[testBorrowerDel]test completed successfully");
        } catch (Exception e) {
            log.error("[testBorrowerDel]something went wrong. error >>> ", e);
            throw new RuntimeException("something went wrong. testBorrowerDel failed.");
        }
        log.info("[testBorrowerDel]end of testBorrowerDel");
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

    private Object getBookResponseData(ResponseEntity<BaseRequestResponse<BookServiceRequest>> responseEntity) throws Exception {
        return getResponseData(responseEntity.getStatusCode(), responseEntity.getBody());
    }

    private Object getBorrowerResponseData(ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> responseEntity) throws Exception {
        return getResponseData(responseEntity.getStatusCode(), responseEntity.getBody());
    }

    private Object getResponseData(HttpStatusCode httpStatusCode, BaseRequestResponse body) throws Exception {
        // log response
        log.info("[getResponseData]http status code: {}", httpStatusCode.value());
        //log.info("[getBookResponseData]response body: {}", responseEntity.getBody());

        // check status code
        if (httpStatusCode != HttpStatus.OK) {
            throw new Exception("return http status code is not 200");
        }
        // check body
        if (body == null) {
            throw new Exception("return response body is null");
        }
        // check body content - status code
        if (body.getCode() == null || !body.getCode().equals(Enums.Status.SUCCESS.getCode())) {
            throw new Exception("return http status code is not 200");
        }
        return body.getData();
    }

}
