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

    @Value("${BRANCH_NAME}")
    private String branchName;

    @Value("${COMMIT_ID}")
    private String commitId;

    @Autowired
    private BookRepository bookRepository;

    private static final String REPLACE_BRANCH_NAME = "REPLACE_BRANCH_NAME";
    private static final String REPLACE_COMMIT_ID = "REPLACE_COMMIT_ID";

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
        log.info("[displayAppInfo]app is running on branch:{} with commitId:{}",
                branchName.equals(REPLACE_BRANCH_NAME) ? "#" : branchName,
                commitId.equals(REPLACE_COMMIT_ID) ? "#" : commitId
        );
    }

}
