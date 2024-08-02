package com.aeonbank.library.common.constant;

import lombok.Getter;

public class Enums {

    @Getter
    public enum Status {

        SOMETHING_WENT_WRONG("999999", "Something went wrong"),
        INVALID_INPUT("999998", "Invalid input"),
        SUCCESS("000000", "Operation success");

        private final String code;
        private final String message;

        Status(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }

}
