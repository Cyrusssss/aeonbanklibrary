package com.aeonbank.library.dto.book;

import com.aeonbank.library.model.Book;
import lombok.Data;

import java.util.List;

@Data
public class BookServiceResponse {

    private Book book;
    private List<Book> bookList;

}
