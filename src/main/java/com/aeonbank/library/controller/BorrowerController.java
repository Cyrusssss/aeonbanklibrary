package com.aeonbank.library.controller;

import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BorrowerServiceRequest;
import com.aeonbank.library.service.BorrowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.aeonbank.library.common.Enums.DEFAULT_PAGE_SIZE;

@Slf4j
@RestController
public class BorrowerController {

    @Autowired
    private BorrowerService<BaseRequestResponse<BorrowerServiceRequest>> borrowerService;

    @GetMapping("/api/v1/borrower/list")
    public ResponseEntity<Object> list(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
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
        log.info("[list]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @GetMapping("/api/v1/borrower/get")
    public ResponseEntity<Object> get(@RequestParam Long id) {
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
        log.info("[get]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @PostMapping("/api/v1/borrower/add")
    public ResponseEntity<Object> add(@RequestBody BorrowerServiceRequest request) {
        log.info("[add]request received. request:{}", request);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            borrowerService.add(rr);
        } catch (Exception e) {
            log.error("[add]something went wrong. error >>> ", e);
        }
        log.info("[add]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @PutMapping("/api/v1/borrower/update")
    public ResponseEntity<Object> update(@RequestBody BorrowerServiceRequest request) {
        log.info("[update]request received. request:{}", request);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            borrowerService.update(rr);
        } catch (Exception e) {
            log.error("[update]something went wrong. error >>> ", e);
        }
        log.info("[update]end of request");
        return ResponseEntity.ok().body(rr);
    }

    @DeleteMapping("/api/v1/borrower/update")
    public ResponseEntity<Object> delete(@RequestBody BorrowerServiceRequest request) {
        log.info("[delete]request received. request:{}", request);
        BaseRequestResponse<BorrowerServiceRequest> rr = new BaseRequestResponse<>();
        try {
            rr.setRequest(request);
            borrowerService.delete(rr);
        } catch (Exception e) {
            log.error("[delete]something went wrong. error >>> ", e);
        }
        log.info("[delete]end of request");
        return ResponseEntity.ok().body(rr);
    }

}
