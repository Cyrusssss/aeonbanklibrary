package com.aeonbank.library.config;

import com.aeonbank.library.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupInitializer {

    @Value("${my.branch-name}")
    private String branchName;

    @Value("${my.commit-id}")
    private String commitId;

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        displayAppInfo();
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

    private void displayAppInfo(){
        log.info("[displayAppInfo]app is running on branch:{} with commitId:{}", branchName, commitId);
    }

}
