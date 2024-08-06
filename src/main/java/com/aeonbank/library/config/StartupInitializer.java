package com.aeonbank.library.config;

import com.aeonbank.library.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class StartupInitializer {

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        initFirstDbRequest();
    }

    private void initFirstDbRequest(){
        try {
            bookRepository.get(1L);
            log.info("[initFirstDbRequest]Database connection initialized");
        } catch (Exception e) {
            log.error("[initFirstDbRequest]database connection initialization failed. error >>> ", e);
        }
    }

}
