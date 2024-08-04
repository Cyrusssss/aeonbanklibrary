package com.aeonbank.library.service;

import com.aeonbank.library.common.Enums;
import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BookServiceRequest;
import com.aeonbank.library.model.Book;
import com.aeonbank.library.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Slf4j
@Service
public class BookServiceImpl extends BaseService implements BookService<BaseRequestResponse<BookServiceRequest>> {

    @Autowired
    private BookRepository bookRepository;

    private ISBNValidator isbnValidator;

    @PostConstruct
    private void init(){
        isbnValidator = new ISBNValidator();
    }

    @Override
    public void list(BaseRequestResponse<BookServiceRequest> rr) {
        log.info("[list]request received");
        try {
            // get data from db, set into return response
            rr.setData(bookRepository.list());

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[list]something went wrong. error >>> ", e);
        }
        log.info("[list]end of request");
    }

    @Override
    public void get(BaseRequestResponse<BookServiceRequest> rr) {
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
            rr.setData(bookRepository.get(rr.getRequest().getId()));
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
    public void add(BaseRequestResponse<BookServiceRequest> rr) {
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
            if (StringUtils.isBlank(rr.getRequest().getIsbn())) {
                log.error("[add]input ISBN cannot be null or blank");
                rr.setDetail("input ISBN cannot be null or blank");
                break tryBlock;
            }
            else if (isbnValidator.isValid(rr.getRequest().getIsbn())) {
                log.error("[add]input ISBN is not a valid ISBN");
                rr.setDetail("input ISBN is not a valid ISBN");
                break tryBlock;
            }
            else if (StringUtils.isBlank(rr.getRequest().getTitle())) {
                log.error("[add]input Title cannot be null or blank");
                rr.setDetail("input Title cannot be null or blank");
                break tryBlock;
            }
            else if (StringUtils.isBlank(rr.getRequest().getAuthor())) {
                log.error("[add]input Author cannot be null or blank");
                rr.setDetail("input Author cannot be null or blank");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // prepare to insert
            Book book = new Book();
            book.setIsbn(rr.getRequest().getIsbn());
            book.setTitle(rr.getRequest().getTitle());
            book.setAuthor(rr.getRequest().getAuthor());

            // insert
            int insertCount = bookRepository.add(book);
            if (insertCount != 1) {
                log.error("[add]failed to insert book, insertCount is not 1. insertCount:{}", insertCount);
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
    public void update(BaseRequestResponse<BookServiceRequest> rr) {
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
            else if (StringUtils.isBlank(rr.getRequest().getIsbn())) {
                log.error("[update]input ISBN cannot be null or blank");
                rr.setDetail("input ISBN cannot be null or blank");
                break tryBlock;
            }
            else if (isbnValidator.isValid(rr.getRequest().getIsbn())) {
                log.error("[update]input ISBN is not a valid ISBN");
                rr.setDetail("input ISBN is not a valid ISBN");
                break tryBlock;
            }
            else if (StringUtils.isBlank(rr.getRequest().getTitle())) {
                log.error("[update]input Title cannot be null or blank");
                rr.setDetail("input Title cannot be null or blank");
                break tryBlock;
            }
            else if (StringUtils.isBlank(rr.getRequest().getAuthor())) {
                log.error("[update]input Author cannot be null or blank");
                rr.setDetail("input Author cannot be null or blank");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // get record for update
            Book book = bookRepository.getForUpdate(rr.getRequest().getId());
            if (book == null) {
                log.error("[update]failed to get book or lock the record");
                rr.setDetail("failed to get book or lock the record");
                break tryBlock;
            }

            // set fields
            book.setIsbn(rr.getRequest().getIsbn());
            book.setAuthor(rr.getRequest().getAuthor());
            book.setTitle(rr.getRequest().getTitle());

            // update
            int rowsAffected = bookRepository.update(book);
            if (rowsAffected != 1) {
                log.error("[add]failed to update book, rowsAffected is not 1. rowsAffected:{}", rowsAffected);
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
    public void delete(BaseRequestResponse<BookServiceRequest> rr) {
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
            int rowsAffected = bookRepository.delete(rr.getRequest().getId());
            if (rowsAffected != 1) {
                log.error("[delete]failed to delete book, rowsAffected is not 1. rowsAffected:{}", rowsAffected);
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
