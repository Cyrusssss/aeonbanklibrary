package com.aeonbank.library.controller;

import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BorrowerServiceRequest;
import com.aeonbank.library.service.BorrowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.aeonbank.library.common.Enums.DEFAULT_PAGE_SIZE;

@Slf4j
@RestController
public class BorrowerController {

    @Autowired
    private BorrowerService<BaseRequestResponse<BorrowerServiceRequest>> borrowerService;

    @GetMapping("/api/v1/borrower")
    public ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
        long timeNow = new Date().getTime();
        log.info("[list]request received. pageNo:{} pageSize:{}", pageNo, pageSize);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.getPagination().setPageNo(pageNo);
            rr.getPagination().setPageSize(pageSize);
            BorrowerServiceRequest request = new BorrowerServiceRequest();
            rr.setRequest(request);
            borrowerService.list(rr);
        } catch (Exception e) {
            log.error("[list]something went wrong. error >>> ", e);
        }
        log.info("[list]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @GetMapping("/api/v1/borrower/{id}")
    public ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> get(@PathVariable("id") Long id) {
        long timeNow = new Date().getTime();
        log.info("[get]request received. id:{}", id);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            BorrowerServiceRequest request = new BorrowerServiceRequest();
            request.setId(id);
            rr.setRequest(request);
            borrowerService.get(rr);
        } catch (Exception e) {
            log.error("[get]something went wrong. error >>> ", e);
        }
        log.info("[get]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @PostMapping("/api/v1/borrower")
    public ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> add(@RequestBody BorrowerServiceRequest request) {
        long timeNow = new Date().getTime();
        log.info("[add]request received. request:{}", request);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            borrowerService.add(rr);
        } catch (Exception e) {
            log.error("[add]something went wrong. error >>> ", e);
        }
        log.info("[add]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @PutMapping("/api/v1/borrower/{id}")
    public ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> update(@PathVariable("id") Long id,
                                         @RequestBody BorrowerServiceRequest request) {
        long timeNow = new Date().getTime();
        log.info("[update]request received. id:{} request:{}", id, request);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            request.setId(id);
            rr.setRequest(request);
            borrowerService.update(rr);
        } catch (Exception e) {
            log.error("[update]something went wrong. error >>> ", e);
        }
        log.info("[update]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

    @DeleteMapping("/api/v1/borrower/{id}")
    public ResponseEntity<BaseRequestResponse<BorrowerServiceRequest>> delete(@PathVariable("id") Long id) {
        long timeNow = new Date().getTime();
        log.info("[delete]request received. id:{}", id);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            BorrowerServiceRequest request = new BorrowerServiceRequest();
            request.setId(id);
            rr.setRequest(request);
            borrowerService.delete(rr);
        } catch (Exception e) {
            log.error("[delete]something went wrong. error >>> ", e);
        }
        log.info("[delete]end of request. timeConsumed:{}ms", new Date().getTime() - timeNow);
        return ResponseEntity.ok().body(rr);
    }

}
