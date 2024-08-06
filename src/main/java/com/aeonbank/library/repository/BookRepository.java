package com.aeonbank.library.repository;

import com.aeonbank.library.model.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookRepository {

    @Select("""
            select *
             from book
            """)
    List<Book> list();

    @Select("""
            select *
             from book
             where id = #{id}
            """)
    Book get(Long id);

    @Select("""
            select *
             from book
             where isbn = #{isbn}
            """)
    Book getByIsbn(String isbn);

    @Insert("""
            insert into book(isbn, title, author)
             values(#{isbn}, #{title}, #{author})
            """)
    @Options(useGeneratedKeys = true, keyProperty="id", keyColumn="id")
    int add(Book book);

    @Update("""
            update book set
             isbn = #{isbn},
             title = #{title},
             author = #{author},
             borrower_id = #{borrowerId}
             where id = #{id}
            """)
    int update(Book book);

    @Delete("""
            delete from book
             where id = #{id}
            """)
    int delete(Long id);

    @Select("""
            select *
             from book
             where id = #{id}
             for update
            """)
    Book getForUpdate(Long id);

}
