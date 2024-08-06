package com.aeonbank.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Transaction extends Audit {

    private Long bookId;
    private Long borrowerId;
    private Date returnDate;

}
