package com.aeonbank.library.service;

public interface BookService<REQUEST> extends BaseCrudService<REQUEST> {

    void borrowBook(REQUEST request);
    void returnBook(REQUEST request);

}
