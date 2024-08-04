package com.aeonbank.library.common;

import lombok.Getter;

public class Enums {

    public static final String DEFAULT_PAGE_SIZE = "25";

    @Getter
    public enum Status {

        SOMETHING_WENT_WRONG("999999", "Something went wrong"),
        INVALID_INPUT("999998", "Invalid input"),
        DATA_NOT_FOUND("999997", "Data not found"),
        SUCCESS("000000", "Operation success");

        private final String code;
        private final String message;

        Status(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }

}
