package com.aeonbank.library.model;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Audit {

    private Long id;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

}
