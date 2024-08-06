package com.aeonbank.library;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.ISBNValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
//@SpringBootTest
public class LibraryApplicationTest {

    @Test
    public void testGenerateIsbn() {
        Faker faker = new Faker();
        ISBNValidator validator = new ISBNValidator();

        List<String> isbn10List = new ArrayList<>();
        while (isbn10List.size() < 10) {
            String isbn10 = faker.code().isbn10();
            if (validator.isValidISBN10(isbn10)) {
                isbn10List.add(isbn10);
            }
        }

        List<String> isbn13List = new ArrayList<>();
        while (isbn13List.size() < 10) {
            String isbn13 = faker.code().isbn13();
            if (validator.isValidISBN13(isbn13)) {
                isbn13List.add(isbn13);
            }
        }

        log.info("[testGenerateIsbn]isbn10List >>> {}", isbn10List);
        log.info("[testGenerateIsbn]isbn13List >>> {}", isbn13List);
    }

}
