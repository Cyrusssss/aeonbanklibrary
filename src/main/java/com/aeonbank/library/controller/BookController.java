package com.aeonbank.library.controller;

import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BookServiceRequest;
import com.aeonbank.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.aeonbank.library.common.Enums.DEFAULT_PAGE_SIZE;

@Slf4j
@RestController
public class BookController {

    @Autowired
    private BookService<BaseRequestResponse<BookServiceRequest>> bookService;

    @GetMapping("/api/v1/book/list")
    public ResponseEntity<Object> list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
        log.info("[list]request received. pageNo:{} pageSize:{}", pageNo, pageSize);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.getPagination().setPageNo(pageNo);
            rr.getPagination().setPageSize(pageSize);
            BookServiceRequest request = new BookServiceRequest();
            rr.setRequest(request);
            bookService.list(rr);
        } catch (Exception e) {
            log.error("[list]something went wrong. error >>> ", e);
        }
        log.info("[list]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @GetMapping("/api/v1/book/get")
    public ResponseEntity<Object> get(@RequestParam Long id) {
        log.info("[get]request received. id:{}", id);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            BookServiceRequest request = new BookServiceRequest();
            request.setId(id);
            rr.setRequest(request);
            bookService.get(rr);
        } catch (Exception e) {
            log.error("[get]something went wrong. error >>> ", e);
        }
        log.info("[get]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @PostMapping("/api/v1/book/add")
    public ResponseEntity<Object> add(@RequestBody BookServiceRequest request) {
        log.info("[add]request received. request:{}", request);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            bookService.add(rr);
        } catch (Exception e) {
            log.error("[add]something went wrong. error >>> ", e);
        }
        log.info("[add]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @PutMapping("/api/v1/book/update")
    public ResponseEntity<Object> update(@RequestBody BookServiceRequest request) {
        log.info("[update]request received. request:{}", request);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            bookService.update(rr);
        } catch (Exception e) {
            log.error("[update]something went wrong. error >>> ", e);
        }
        log.info("[update]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @DeleteMapping("/api/v1/book/update")
    public ResponseEntity<Object> delete(@RequestBody BookServiceRequest request) {
        log.info("[delete]request received. request:{}", request);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            bookService.delete(rr);
        } catch (Exception e) {
            log.error("[delete]something went wrong. error >>> ", e);
        }
        log.info("[delete]end of request");
        return ResponseEntity.ok().body(rr);
    }

}
