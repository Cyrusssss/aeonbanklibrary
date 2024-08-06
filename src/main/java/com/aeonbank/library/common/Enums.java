package com.aeonbank.library.common;

import lombok.Getter;

public class Enums {

    public static final String DEFAULT_PAGE_SIZE = "25";

    @Getter
    public enum Status {

        SOMETHING_WENT_WRONG("999999", "Something went wrong"),
        INVALID_INPUT("999998", "Invalid input"),
        DATA_NOT_FOUND("999997", "Data not found"),
        BOOK_IS_NOT_AVAILABLE("999996", "Book is not available"),
        TRANSACTION_NOT_FOUND("999995", "Transaction not found"),
        DATA_NOT_TALLY("999994", "Data not tally"),
        BOOK_NOT_RETURNED("999993", "Book not returned"),
        SUCCESS("000000", "Operation success");

        private final String code;
        private final String message;

        Status(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }

}
