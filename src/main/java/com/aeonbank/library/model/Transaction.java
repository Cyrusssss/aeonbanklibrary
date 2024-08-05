package com.aeonbank.library.model;

import lombok.Data;

import java.util.Date;

@Data
public class Transaction {

    private Long bookId;
    private Long borrowerId;
    private Date returnDate;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

}
