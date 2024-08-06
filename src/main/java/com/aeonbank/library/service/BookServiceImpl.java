package com.aeonbank.library.service;

import com.aeonbank.library.common.Enums;
import com.aeonbank.library.dto.BaseRequestResponse;
import com.aeonbank.library.dto.BookServiceRequest;
import com.aeonbank.library.model.Book;
import com.aeonbank.library.model.Borrower;
import com.aeonbank.library.model.Transaction;
import com.aeonbank.library.repository.BookRepository;
import com.aeonbank.library.repository.BorrowerRepository;
import com.aeonbank.library.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImpl extends BaseService implements BookService<BaseRequestResponse<BookServiceRequest>> {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private ISBNValidator isbnValidator;

    private static final String ACTION_BORROW_BOOK = "borrow book";
    private static final String ACTION_RETURN_BOOK = "return book";
    private static final String ACTION_CHANGE_BORROWER = "change borrower";

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
            else if (!isbnValidator.isValid(rr.getRequest().getIsbn())) {
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

            // check if book with same isbn exist
            Book book = bookRepository.getByIsbn(rr.getRequest().getIsbn());
            if (book != null) {
                if (!book.getTitle().equals(rr.getRequest().getTitle())) {
                    log.error("[add]book with same isbn exist but input Title not tally. inputTitle:{} existingBookTitle:{}", rr.getRequest().getTitle(), book.getTitle());
                    rr.setDetail("book with same isbn exist but input Title not tally with book Title in database");
                    rr.setStatus(Enums.Status.DATA_NOT_TALLY);
                    break tryBlock;
                }
                else if (!book.getAuthor().equals(rr.getRequest().getAuthor())) {
                    log.error("[add]book with same isbn exist but input Author not tally. inputAuthor:{} existingBookAuthor:{}", rr.getRequest().getAuthor(), book.getAuthor());
                    rr.setDetail("book with same isbn exist but input Author not tally with book Author in database");
                    rr.setStatus(Enums.Status.DATA_NOT_TALLY);
                    break tryBlock;
                }
            } else {
                book = new Book();
                book.setIsbn(rr.getRequest().getIsbn());
                book.setTitle(rr.getRequest().getTitle());
                book.setAuthor(rr.getRequest().getAuthor());
            }

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
            else if (!isbnValidator.isValid(rr.getRequest().getIsbn())) {
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
            else if (rr.getRequest().getBorrowerId() != null && rr.getRequest().getBorrowerId() < 1) {
                log.error("[update]input borrowerId cannot less than 1");
                rr.setDetail("input borrowerId cannot less than 1");
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

            // find out what needed to do
            String action = null;
            if (book.getBorrowerId() == null) {
                if (rr.getRequest().getBorrowerId() != null) {
                    action = ACTION_BORROW_BOOK;
                }
            } else {
                if (rr.getRequest().getBorrowerId() == null) {
                    action = ACTION_RETURN_BOOK;
                } else {
                    if (!book.getBorrowerId().equals(rr.getRequest().getBorrowerId())) {
                        action = ACTION_CHANGE_BORROWER;
                    }
                }
            }

            // perform transaction update/create
            if (action != null) {
                // return book, close transaction
                if (action.equals(ACTION_RETURN_BOOK) || action.equals(ACTION_CHANGE_BORROWER)) {
                    // get transaction for update
                    Transaction transaction = transactionRepository.getByBookIdNotReturnedForUpdate(book.getId());
                    if (transaction == null) {
                        log.info("[update]transaction not found, update return status is not possible");
                        rr.setDetail("transaction not found, update return status is not possible");
                        rr.setStatus(Enums.Status.TRANSACTION_NOT_FOUND);
                        break tryBlock;
                    }
                    transaction.setReturnDate(new Date());

                    // update transaction
                    int rowsAffected = transactionRepository.updateReturnDate(transaction);
                    if (rowsAffected != 1) {
                        log.info("[update]update transaction failed. rowsAffected:{}", rowsAffected);
                        rr.setDetail("transaction update failed");
                        break tryBlock;
                    }
                }
                // borrow book, create transaction
                if (action.equals(ACTION_BORROW_BOOK) || action.equals(ACTION_CHANGE_BORROWER)) {
                    // check if borrower exist
                    Borrower borrower = borrowerRepository.get(rr.getRequest().getBorrowerId());
                    if (borrower == null) {
                        log.info("[update]borrower not found");
                        rr.setDetail("borrower not found");
                        rr.setStatus(Enums.Status.DATA_NOT_FOUND);
                        break tryBlock;
                    }

                    // insert transaction
                    Transaction transaction = new Transaction();
                    transaction.setBookId(book.getId());
                    transaction.setBorrowerId(rr.getRequest().getBorrowerId());
                    int insertCount = transactionRepository.insert(transaction);
                    if (insertCount != 1) {
                        log.info("[update]fail to create a new transaction to borrow the book");
                        rr.setDetail("fail to create borrow transaction");
                        break tryBlock;
                    }
                }
            }

            // update book
            book.setIsbn(rr.getRequest().getIsbn());
            book.setAuthor(rr.getRequest().getAuthor());
            book.setTitle(rr.getRequest().getTitle());
            book.setBorrowerId(rr.getRequest().getBorrowerId());
            int rowsAffected = bookRepository.update(book);
            if (rowsAffected != 1) {
                log.error("[update]failed to update book. rowsAffected:{}", rowsAffected);
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

            // get book
            Book book = bookRepository.getForUpdate(rr.getRequest().getId());
            if (book.getBorrowerId() != null) {
                log.info("[borrowBook]book is borrowed, delete is not possible");
                rr.setDetail("book is borrowed, delete is not possible");
                rr.setStatus(Enums.Status.BOOK_IS_NOT_AVAILABLE);
                break tryBlock;
            }

            // delete
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

    @Override
    @Transactional
    public void borrowBook(BaseRequestResponse<BookServiceRequest> rr) {
        log.info("[borrowBook]request received");
        tryBlock: try {
            // validation
            if (rr == null) {
                log.error("[borrowBook]input object cannot be null");
                rr = new BaseRequestResponse<>();
                rr.setDetail("[backend error]input object cannot be null");
                break tryBlock;
            }
            else if (rr.getRequest() == null) {
                log.error("[borrowBook]request cannot be null");
                rr.setDetail("[backend error]request cannot be null");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.INVALID_INPUT);
            if (rr.getRequest().getBookId() == null || rr.getRequest().getBookId() < 1) {
                log.error("[borrowBook]input bookId cannot be null less than 1");
                rr.setDetail("input bookId cannot be null less than 1");
                break tryBlock;
            }
            else if (rr.getRequest().getBorrowerId() == null || rr.getRequest().getBorrowerId() < 1) {
                log.error("[borrowBook]borrowerId cannot be null less than 1");
                rr.setDetail("input borrowerId cannot be null less than 1");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // update book
            Book book = bookRepository.getForUpdate(rr.getRequest().getBookId());
            if (book == null) {
                log.info("[borrowBook]book not found");
                rr.setDetail("book not found");
                rr.setStatus(Enums.Status.DATA_NOT_FOUND);
                break tryBlock;
            }
            else if (book.getBorrowerId() != null) {
                log.info("[borrowBook]book is not available to be borrowed");
                rr.setDetail("book is not available to be borrowed");
                rr.setStatus(Enums.Status.BOOK_IS_NOT_AVAILABLE);
                break tryBlock;
            }
            book.setBorrowerId(rr.getRequest().getBorrowerId());
            bookRepository.update(book);

            // check if borrower exist
            Borrower borrower = borrowerRepository.get(rr.getRequest().getBorrowerId());
            if (borrower == null) {
                log.info("[borrowBook]borrower not found");
                rr.setDetail("borrower not found");
                rr.setStatus(Enums.Status.DATA_NOT_FOUND);
                break tryBlock;
            }

            // insert transaction
            Transaction transaction = new Transaction();
            transaction.setBookId(book.getId());
            transaction.setBorrowerId(borrower.getId());
            int insertCount = transactionRepository.insert(transaction);
            if (insertCount != 1) {
                log.info("[borrowBook]fail to create a new transaction to borrow the book");
                rr.setDetail("fail to create transaction");
                break tryBlock;
            }

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[borrowBook]something went wrong. error >>> ", e);
        }
        // set a new rr if rr is null
        if (rr == null) {
            rr = new BaseRequestResponse<>();
        }
        // rollback transaction if status is not success
        if (rr.getCode() == null || !rr.getCode().equals(Enums.Status.SUCCESS.getCode())) {
            log.error("[borrowBook]rolling back transaction");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        log.info("[borrowBook]end of request");
    }

    @Override
    @Transactional
    public void returnBook(BaseRequestResponse<BookServiceRequest> rr) {
        log.info("[returnBook]request received");
        tryBlock: try {
            // validation
            if (rr == null) {
                log.error("[returnBook]input object cannot be null");
                rr = new BaseRequestResponse<>();
                rr.setDetail("[backend error]input object cannot be null");
                break tryBlock;
            }
            else if (rr.getRequest() == null) {
                log.error("[returnBook]request cannot be null");
                rr.setDetail("[backend error]request cannot be null");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.INVALID_INPUT);
            if (rr.getRequest().getId() == null || rr.getRequest().getId() < 1) {
                log.error("[returnBook]input Id cannot be null less than 1");
                rr.setDetail("input Id cannot be null less than 1");
                break tryBlock;
            }
            rr.setStatus(Enums.Status.SOMETHING_WENT_WRONG);

            // return book
            Book book = bookRepository.getForUpdate(rr.getRequest().getId());
            if (book.getBorrowerId() == null) {
                log.info("[borrowBook]book is not borrowed, return is not possible");
                rr.setDetail("book is not borrowed, return is not possible");
                rr.setStatus(Enums.Status.BOOK_IS_NOT_AVAILABLE);
                break tryBlock;
            }
            book.setBorrowerId(null);
            bookRepository.update(book);

            // get transaction for update
            Transaction transaction = transactionRepository.getByBookIdNotReturnedForUpdate(rr.getRequest().getId());
            if (transaction == null) {
                log.info("[borrowBook]transaction not found, return is not possible");
                rr.setDetail("transaction not found, return is not possible");
                rr.setStatus(Enums.Status.TRANSACTION_NOT_FOUND);
                break tryBlock;
            }
            transaction.setReturnDate(new Date());

            // update transaction
            int updateCount = transactionRepository.updateReturnDate(transaction);
            if (updateCount != 1) {
                log.info("[borrowBook]update failed. updateCount:{}", updateCount);
                rr.setDetail("transaction update failed");
                break tryBlock;
            }

            // set status to success
            rr.setStatus(Enums.Status.SUCCESS);
        } catch (Exception e) {
            log.error("[returnBook]something went wrong. error >>> ", e);
        }
        // set a new rr if rr is null
        if (rr == null) {
            rr = new BaseRequestResponse<>();
        }
        // rollback transaction if status is not success
        if (rr.getCode() == null || !rr.getCode().equals(Enums.Status.SUCCESS.getCode())) {
            log.error("[returnBook]rolling back transaction");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        log.info("[returnBook]end of request");
    }

}
