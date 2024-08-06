package com.aeonbank.library.controller;

import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BookServiceRequest;
import com.aeonbank.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.aeonbank.library.common.Enums.DEFAULT_PAGE_SIZE;

@Slf4j
@RestController
public class BookController {

    @Autowired
    private BookService<BaseRequestResponse<BookServiceRequest>> bookService;

    @GetMapping("/api/v1/book")
    public ResponseEntity<BaseRequestResponse<BookServiceRequest>> list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author
    ) {
        long timeNow = new Date().getTime();
        log.info("[list]request received. pageNo:{} pageSize:{}", pageNo, pageSize);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.getPagination().setPageNo(pageNo);
            rr.getPagination().setPageSize(pageSize);
            BookServiceRequest request = new BookServiceRequest();
            request.setIsbn(isbn);
            request.setTitle(title);
            request.setAuthor(author);
            rr.setRequest(request);
            bookService.list(rr);
        } catch (Exception e) {
            log.error("[list]something went wrong. error >>> ", e);
        }
        log.info("[list]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<BaseRequestResponse<BookServiceRequest>> get(@PathVariable("id") Long id) {
        long timeNow = new Date().getTime();
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
        log.info("[get]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @PostMapping("/api/v1/book")
    public ResponseEntity<BaseRequestResponse<BookServiceRequest>> add(@RequestBody BookServiceRequest request) {
        long timeNow = new Date().getTime();
        log.info("[add]request received. request:{}", request);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            bookService.add(rr);
        } catch (Exception e) {
            log.error("[add]something went wrong. error >>> ", e);
        }
        log.info("[add]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<BaseRequestResponse<BookServiceRequest>> update(@PathVariable("id") Long id,
                                         @RequestBody BookServiceRequest request) {
        long timeNow = new Date().getTime();
        log.info("[update]request received. id:{} request:{}", id, request);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            request.setId(id);
            rr.setRequest(request);
            bookService.update(rr);
        } catch (Exception e) {
            log.error("[update]something went wrong. error >>> ", e);
        }
        log.info("[update]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<BaseRequestResponse<BookServiceRequest>> delete(@PathVariable("id") Long id) {
        long timeNow = new Date().getTime();
        log.info("[delete]request received. id:{}", id);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            BookServiceRequest request = new BookServiceRequest();
            request.setId(id);
            rr.setRequest(request);
            bookService.delete(rr);
        } catch (Exception e) {
            log.error("[delete]something went wrong. error >>> ", e);
        }
        log.info("[delete]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @PostMapping("/api/v1/book/{id}/borrow-book")
    public ResponseEntity<BaseRequestResponse<BookServiceRequest>> borrowBook(@PathVariable("id") Long id,
                                             @RequestBody BookServiceRequest request) {
        long timeNow = new Date().getTime();
        log.info("[borrowBook]request received. id:{} request:{}", id, request);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            request.setBookId(id);
            rr.setRequest(request);
            bookService.borrowBook(rr);
        } catch (Exception e) {
            log.error("[borrowBook]something went wrong. error >>> ", e);
        }
        log.info("[borrowBook]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @PostMapping("/api/v1/book/{id}/return-book")
    public ResponseEntity<BaseRequestResponse<BookServiceRequest>> returnBook(@PathVariable("id") Long id) {
        long timeNow = new Date().getTime();
        log.info("[returnBook]request received. id:{}", id);
        BaseRequestResponse<BookServiceRequest> rr = new BaseRequestResponse<>();
        try {
            BookServiceRequest request = new BookServiceRequest();
            request.setId(id);
            rr.setRequest(request);
            bookService.returnBook(rr);
        } catch (Exception e) {
            log.error("[returnBook]something went wrong. error >>> ", e);
        }
        log.info("[returnBook]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

}
