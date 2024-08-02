package com.aeonbank.library.controller;

import com.aeonbank.library.common.constant.Enums;
import com.aeonbank.library.dto.borrower.BorrowerServiceRequest;
import com.aeonbank.library.dto.borrower.BorrowerServiceResponse;
import com.aeonbank.library.dto.common.ControllerResponse;
import com.aeonbank.library.dto.common.RequestResponse;
import com.aeonbank.library.service.borrower.BorrowerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class BorrowerController {

    @Autowired
    private BorrowerService<BorrowerServiceRequest, BorrowerServiceResponse> borrowerService;

    @GetMapping("/api/v1/borrower/list")
    public ResponseEntity<Object> list(@RequestBody BorrowerServiceRequest request) {
        log.info("[list]request received. request:{}", request);
        ControllerResponse response = new ControllerResponse();
        tryBlock: try {
            RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr = new RequestResponse<>();
            rr.setRequest(request);
            borrowerService.list(rr);
            if (rr.getStatus() == null) {
                log.error("[list]null status returned from service");
                break tryBlock;
            } else if (!rr.getStatus().getCode().equals(Enums.Status.SUCCESS.getCode())) {
                log.error("[list]not-success status returned from service");
                response.setCode(rr.getStatus().getCode());
                response.setMessage(rr.getStatus().getMessage());
                response.setDetail(rr.getStatus().getDetail());
                break tryBlock;
            } else if (rr.getResponse().getBorrowerList() == null) {
                log.error("[list]null borrowerList returned from service");
                break tryBlock;
            }
            response.setCode(Enums.Status.SUCCESS.getCode());
            response.setMessage(Enums.Status.SUCCESS.getMessage());
            response.setData(rr.getResponse().getBorrowerList());
            log.info("[list]operation success. borrowerListSize:{}", rr.getResponse().getBorrowerList().size());
        } catch (Exception e) {
            log.error("[list]something went wrong. error >>> ", e);
        }
        log.info("[list]end of request");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/v1/borrower/get")
    public ResponseEntity<Object> get(@RequestBody BorrowerServiceRequest request) {
        log.info("[get]request received. request:{}", request);
        ControllerResponse response = new ControllerResponse();
        tryBlock: try {
            RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr = new RequestResponse<>();
            rr.setRequest(request);
            borrowerService.get(rr);
            if (rr.getStatus() == null) {
                log.error("[get]null status returned from service");
                break tryBlock;
            } else if (!rr.getStatus().getCode().equals(Enums.Status.SUCCESS.getCode())) {
                log.error("[get]not-success status returned from service");
                response.setCode(rr.getStatus().getCode());
                response.setMessage(rr.getStatus().getMessage());
                response.setDetail(rr.getStatus().getDetail());
                break tryBlock;
            } else if (rr.getResponse().getBorrower() == null || rr.getResponse().getBorrower().getId() == null) {
                log.error("[get]null borrower returned from service");
                break tryBlock;
            }
            response.setCode(Enums.Status.SUCCESS.getCode());
            response.setMessage(Enums.Status.SUCCESS.getMessage());
            response.setData(rr.getResponse().getBorrower());
            log.info("[get]operation success. response >>> borrowerId:{}", rr.getResponse().getBorrower().getId());
        } catch (Exception e) {
            log.error("[get]something went wrong. error >>> ", e);
        }
        log.info("[get]end of request");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/v1/borrower/add")
    public ResponseEntity<Object> add(@RequestBody BorrowerServiceRequest request) {
        log.info("[add]request received. request:{}", request);
        ControllerResponse response = new ControllerResponse();
        tryBlock: try {
            RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr = new RequestResponse<>();
            rr.setRequest(request);
            borrowerService.add(rr);
            if (rr.getStatus() == null) {
                log.error("[add]null status returned from service");
                break tryBlock;
            } else if (!rr.getStatus().getCode().equals(Enums.Status.SUCCESS.getCode())) {
                log.error("[add]not-success status returned from service");
                response.setCode(rr.getStatus().getCode());
                response.setMessage(rr.getStatus().getMessage());
                response.setDetail(rr.getStatus().getDetail());
                break tryBlock;
            }
            response.setCode(Enums.Status.SUCCESS.getCode());
            response.setMessage(Enums.Status.SUCCESS.getMessage());
            log.info("[add]operation success");
        } catch (Exception e) {
            log.error("[add]something went wrong. error >>> ", e);
        }
        log.info("[add]end of request");
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/api/v1/borrower/update")
    public ResponseEntity<Object> update(@RequestBody BorrowerServiceRequest request) {
        log.info("[update]request received. request:{}", request);
        ControllerResponse response = new ControllerResponse();
        tryBlock: try {
            RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr = new RequestResponse<>();
            rr.setRequest(request);
            borrowerService.update(rr);
            if (rr.getStatus() == null) {
                log.error("[update]null status returned from service");
                break tryBlock;
            } else if (!rr.getStatus().getCode().equals(Enums.Status.SUCCESS.getCode())) {
                log.error("[update]not-success status returned from service");
                response.setCode(rr.getStatus().getCode());
                response.setMessage(rr.getStatus().getMessage());
                response.setDetail(rr.getStatus().getDetail());
                break tryBlock;
            }
            response.setCode(Enums.Status.SUCCESS.getCode());
            response.setMessage(Enums.Status.SUCCESS.getMessage());
            log.info("[update]operation success");
        } catch (Exception e) {
            log.error("[update]something went wrong. error >>> ", e);
        }
        log.info("[update]end of request");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/api/v1/borrower/update")
    public ResponseEntity<Object> delete(@RequestBody BorrowerServiceRequest request) {
        log.info("[delete]request received. request:{}", request);
        ControllerResponse response = new ControllerResponse();
        tryBlock: try {
            RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr = new RequestResponse<>();
            rr.setRequest(request);
            borrowerService.delete(rr);
            if (rr.getStatus() == null) {
                log.error("[delete]null status returned from service");
                break tryBlock;
            } else if (!rr.getStatus().getCode().equals(Enums.Status.SUCCESS.getCode())) {
                log.error("[delete]not-success status returned from service");
                response.setCode(rr.getStatus().getCode());
                response.setMessage(rr.getStatus().getMessage());
                response.setDetail(rr.getStatus().getDetail());
                break tryBlock;
            }
            response.setCode(Enums.Status.SUCCESS.getCode());
            response.setMessage(Enums.Status.SUCCESS.getMessage());
            log.info("[delete]operation success");
        } catch (Exception e) {
            log.error("[delete]something went wrong. error >>> ", e);
        }
        log.info("[delete]end of request");
        return ResponseEntity.ok().body(response);
    }

}
