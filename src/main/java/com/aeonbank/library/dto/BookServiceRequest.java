package com.aeonbank.library.dto;

import com.aeonbank.library.model.Book;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookServiceRequest extends Book {
}
