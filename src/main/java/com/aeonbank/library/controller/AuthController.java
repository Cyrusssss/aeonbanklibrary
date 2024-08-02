package com.aeonbank.library.controller;

import com.aeonbank.library.dto.auth.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    @PostMapping("/api/v1/authenticate/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        log.info("[login]request received. request:{}", request);
        try {

        } catch (Exception e) {
            log.error("[login]something went wrong. error >>> ", e);
        }
        log.info("[login]end of request");
        return ResponseEntity.ok().body(null);
    }

}
