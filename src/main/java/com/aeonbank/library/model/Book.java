package com.aeonbank.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends Audit {

    private String isbn;
    private String title;
    private String author;

}
