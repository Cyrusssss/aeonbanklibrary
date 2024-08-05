package com.aeonbank.library;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.ISBNValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class LibraryApplicationTest {

    @Test
    public void test() {
        log.debug("test");
    }

    @Test
    public void testGenerateIsbn() {
        Faker faker = new Faker();
        ISBNValidator validator = new ISBNValidator();

        String isbn10 = faker.code().isbn10();
        String isbn13 = faker.code().isbn13();

        log.info("Generated ISBN-10: {}", isbn10);
        log.info("Is ISBN-10 valid: " + validator.isValidISBN10(isbn10));

        log.info("Generated ISBN-13: {}", isbn13);
        log.info("Is ISBN-13 valid: {}", validator.isValidISBN13(isbn13));
    }

}
