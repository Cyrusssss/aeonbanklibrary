package com.aeonbank.library.config;

import com.aeonbank.library.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseConnectionInit {

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        try {
            bookRepository.get(1L);
            log.info("Database connection initialized");
        } catch (Exception e) {
            log.error("[init]database connection initialization failed. error >>> ", e);
        }
    }

}
