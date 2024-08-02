package com.aeonbank.library.service.borrower;

import com.aeonbank.library.common.constant.Enums;
import com.aeonbank.library.dto.borrower.BorrowerServiceRequest;
import com.aeonbank.library.dto.borrower.BorrowerServiceResponse;
import com.aeonbank.library.dto.common.RequestResponse;
import com.aeonbank.library.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BorrowerServiceImpl extends BaseService implements BorrowerService<BorrowerServiceRequest, BorrowerServiceResponse> {

    @Override
    public void list(RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr) {
        log.info("[list]request received. request:{}", rr.getRequest());
        tryBlock: try {
            // validation - default status to validation failed status
//            log.info("[list]validation request");
//            rr.getStatus().setCode(Enums.Status.INVALID_INPUT.getCode());
//            rr.getStatus().setMessage(Enums.Status.INVALID_INPUT.getMessage());
//            if (rr.getRequest() == null) {
//                log.error("[list]request cannot be null");
//                rr.getStatus().setDetail("request cannot be null");
//                break tryBlock;
//            }


            rr.getStatus().setCode(Enums.Status.SUCCESS.getCode());
            rr.getStatus().setMessage(Enums.Status.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("[list]something went wrong. error >>> ", e);
        }
        log.info("[list]end of request");
    }

    @Override
    public void get(RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr) {
        log.info("[get]request received. request:{}", rr.getRequest());
        tryBlock: try {
            // validation - default status to validation failed status
//            log.info("[list]validation request");
//            rr.getStatus().setCode(Enums.Status.INVALID_INPUT.getCode());
//            rr.getStatus().setMessage(Enums.Status.INVALID_INPUT.getMessage());
//            if (rr.getRequest() == null) {
//                log.error("[list]request cannot be null");
//                rr.getStatus().setDetail("request cannot be null");
//                break tryBlock;
//            }


            rr.getStatus().setCode(Enums.Status.SUCCESS.getCode());
            rr.getStatus().setMessage(Enums.Status.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("[get]something went wrong. error >>> ", e);
        }
        log.info("[get]end of request");
    }

    @Override
    public void add(RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr) {
        log.info("[add]request received. request:{}", rr.getRequest());
        tryBlock: try {
            // validation - default status to validation failed status
//            log.info("[list]validation request");
//            rr.getStatus().setCode(Enums.Status.INVALID_INPUT.getCode());
//            rr.getStatus().setMessage(Enums.Status.INVALID_INPUT.getMessage());
//            if (rr.getRequest() == null) {
//                log.error("[list]request cannot be null");
//                rr.getStatus().setDetail("request cannot be null");
//                break tryBlock;
//            }


            rr.getStatus().setCode(Enums.Status.SUCCESS.getCode());
            rr.getStatus().setMessage(Enums.Status.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("[add]something went wrong. error >>> ", e);
        }
        log.info("[add]end of request");
    }

    @Override
    @Transactional
    public void update(RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr) {
        log.info("[update]request received. request:{}", rr.getRequest());
        tryBlock: try {
            // validation - default status to validation failed status
//            log.info("[list]validation request");
//            rr.getStatus().setCode(Enums.Status.INVALID_INPUT.getCode());
//            rr.getStatus().setMessage(Enums.Status.INVALID_INPUT.getMessage());
//            if (rr.getRequest() == null) {
//                log.error("[list]request cannot be null");
//                rr.getStatus().setDetail("request cannot be null");
//                break tryBlock;
//            }


            rr.getStatus().setCode(Enums.Status.SUCCESS.getCode());
            rr.getStatus().setMessage(Enums.Status.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("[update]something went wrong. error >>> ", e);
        }
        log.info("[update]end of request");
    }

    @Override
    @Transactional
    public void delete(RequestResponse<BorrowerServiceRequest, BorrowerServiceResponse> rr) {
        log.info("[delete]request received. request:{}", rr.getRequest());
        tryBlock: try {
            // validation - default status to validation failed status
//            log.info("[list]validation request");
//            rr.getStatus().setCode(Enums.Status.INVALID_INPUT.getCode());
//            rr.getStatus().setMessage(Enums.Status.INVALID_INPUT.getMessage());
//            if (rr.getRequest() == null) {
//                log.error("[list]request cannot be null");
//                rr.getStatus().setDetail("request cannot be null");
//                break tryBlock;
//            }


            rr.getStatus().setCode(Enums.Status.SUCCESS.getCode());
            rr.getStatus().setMessage(Enums.Status.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("[delete]something went wrong. error >>> ", e);
        }
        log.info("[delete]end of request");
    }

}
