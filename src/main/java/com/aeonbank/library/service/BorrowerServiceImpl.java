package com.aeonbank.library.service;

import com.aeonbank.library.common.Enums;
import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BorrowerServiceRequest;
import com.aeonbank.library.model.Borrower;
import com.aeonbank.library.repository.BorrowerRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class BorrowerServiceImpl extends BaseService implements BorrowerService<BaseRequestResponse<BorrowerServiceRequest>> {

    @Autowired
    private BorrowerRepository borrowerRepository;

    private EmailValidator emailValidator;

    @PostConstruct
    private void init(){
        emailValidator = EmailValidator.getInstance();
    }

    @Override
    public void list(BaseRequestResponse<BorrowerServiceRequest> rr) {
        log.info("[list]request received");
        try {
            // get data from db, set into return response
            rr.setData(borrowerRepository.list());

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[list]something went wrong. error >>> ", e);
        }
        log.info("[list]end of request");
    }

    @Override
    public void get(BaseRequestResponse<BorrowerServiceRequest> rr) {
        log.info("[get]request received");
        tryBlock: try {
            // validation
            if (rr == null) {
                log.error("[get]input object cannot be null");
                rr = new BaseRequestResponse<>();
                rr.setDetail("[backend error]input object cannot be null");
                break tryBlock;
            }
            if (rr.getRequest() == null) {
                log.error("[get]request cannot be null");
                rr.setDetail("[backend error]request cannot be null");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.INVALID_INPUT);
            if (rr.getRequest().getId() == null || rr.getRequest().getId() < 1) {
                log.error("[get]input Id cannot be null or less than 1");
                rr.setStatus(Enums.Status.INVALID_INPUT);
                rr.setDetail("input Id cannot be null or less than 1");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // get data from db, set into return response
            rr.setData(borrowerRepository.get(rr.getRequest().getId()));
            if (rr.getData() == null) {
                log.info("[get]data not found");
                rr.setStatus(Enums.Status.DATA_NOT_FOUND);
                break tryBlock;
            }

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[get]something went wrong. error >>> ", e);
        }
        log.info("[get]end of request");
    }

    @Override
    @Transactional
    public void add(BaseRequestResponse<BorrowerServiceRequest> rr) {
        log.info("[add]request received");
        tryBlock: try {
            // validation
            if (rr == null) {
                log.error("[add]input object cannot be null");
                rr = new BaseRequestResponse<>();
                rr.setDetail("[backend error]input object cannot be null");
                break tryBlock;
            }
            else if (rr.getRequest() == null) {
                log.error("[add]request cannot be null");
                rr.setDetail("[backend error]request cannot be null");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.INVALID_INPUT);
            if (StringUtils.isBlank(rr.getRequest().getName())) {
                log.error("[add]input Name cannot be null or blank");
                rr.setDetail("input Name cannot be null or blank");
                break tryBlock;
            }
            else if (StringUtils.isBlank(rr.getRequest().getEmail())) {
                log.error("[add]input Email cannot be null or blank");
                rr.setDetail("input Email cannot be null or blank");
                break tryBlock;
            }
            else if (emailValidator.isValid(rr.getRequest().getEmail())) {
                log.error("[add]input Email is not a valid email");
                rr.setDetail("input Email is not a valid email");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // prepare to insert
            Borrower borrower = new Borrower();
            borrower.setName(rr.getRequest().getName());
            borrower.setEmail(rr.getRequest().getEmail());

            // insert
            int insertCount = borrowerRepository.add(borrower);
            if (insertCount != 1) {
                log.error("[add]failed to insert borrower, insertCount is not 1. insertCount:{}", insertCount);
                break tryBlock;
            }

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[add]something went wrong. error >>> ", e);
        }
        // set a new rr if rr is null
        if (rr == null) {
            rr = new BaseRequestResponse<>();
        }
        // rollback transaction if status is not success
        if (rr.getCode() == null || !rr.getCode().equals(Enums.Status.SUCCESS.getCode())) {
            log.error("[add]rolling back transaction");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        log.info("[add]end of request");
    }

    @Override
    @Transactional
    public void update(BaseRequestResponse<BorrowerServiceRequest> rr) {
        log.info("[update]request received");
        tryBlock: try {
            // validation
            if (rr == null) {
                log.error("[update]input object cannot be null");
                rr = new BaseRequestResponse<>();
                rr.setDetail("[backend error]input object cannot be null");
                break tryBlock;
            }
            else if (rr.getRequest() == null) {
                log.error("[update]request cannot be null");
                rr.setDetail("[backend error]request cannot be null");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.INVALID_INPUT);
            if (rr.getRequest().getId() == null || rr.getRequest().getId() < 1) {
                log.error("[update]input Id cannot be null less than 1");
                rr.setDetail("input Id cannot be null less than 1");
                break tryBlock;
            }
            if (StringUtils.isBlank(rr.getRequest().getName())) {
                log.error("[update]input Name cannot be null or blank");
                rr.setDetail("input Name cannot be null or blank");
                break tryBlock;
            }
            else if (StringUtils.isBlank(rr.getRequest().getEmail())) {
                log.error("[update]input Email cannot be null or blank");
                rr.setDetail("input Email cannot be null or blank");
                break tryBlock;
            }
            else if (emailValidator.isValid(rr.getRequest().getEmail())) {
                log.error("[update]input Email is not a valid email");
                rr.setDetail("input Email is not a valid email");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // get record for update
            Borrower borrower = borrowerRepository.getForUpdate(rr.getRequest().getId());
            if (borrower == null) {
                log.error("[update]failed to get borrower or lock the record");
                rr.setDetail("failed to get borrower or lock the record");
                break tryBlock;
            }

            // set fields
            borrower.setName(rr.getRequest().getName());
            borrower.setEmail(rr.getRequest().getEmail());

            // update
            int rowsAffected = borrowerRepository.update(borrower);
            if (rowsAffected != 1) {
                log.error("[add]failed to update borrower, rowsAffected is not 1. rowsAffected:{}", rowsAffected);
                break tryBlock;
            }

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[update]something went wrong. error >>> ", e);
        }
        // set a new rr if rr is null
        if (rr == null) {
            rr = new BaseRequestResponse<>();
        }
        // rollback transaction if status is not success
        if (rr.getCode() == null || !rr.getCode().equals(Enums.Status.SUCCESS.getCode())) {
            log.error("[update]rolling back transaction");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        log.info("[update]end of request");
    }

    @Override
    @Transactional
    public void delete(BaseRequestResponse<BorrowerServiceRequest> rr) {
        log.info("[delete]request received");
        tryBlock: try {
            // validation
            if (rr == null) {
                log.error("[delete]input object cannot be null");
                rr = new BaseRequestResponse<>();
                rr.setDetail("[backend error]input object cannot be null");
                break tryBlock;
            }
            else if (rr.getRequest() == null) {
                log.error("[delete]request cannot be null");
                rr.setDetail("[backend error]request cannot be null");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.INVALID_INPUT);
            if (rr.getRequest().getId() == null || rr.getRequest().getId() < 1) {
                log.error("[delete]input Id cannot be null less than 1");
                rr.setDetail("input Id cannot be null less than 1");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // update
            int rowsAffected = borrowerRepository.delete(rr.getRequest().getId());
            if (rowsAffected != 1) {
                log.error("[delete]failed to delete borrower, rowsAffected is not 1. rowsAffected:{}", rowsAffected);
                break tryBlock;
            }

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[delete]something went wrong. error >>> ", e);
        }
        // set a new rr if rr is null
        if (rr == null) {
            rr = new BaseRequestResponse<>();
        }
        // rollback transaction if status is not success
        if (rr.getCode() == null || !rr.getCode().equals(Enums.Status.SUCCESS.getCode())) {
            log.error("[delete]rolling back transaction");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        log.info("[delete]end of request");
    }

}
