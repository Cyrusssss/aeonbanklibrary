package com.aeonbank.library.repository;

import com.aeonbank.library.model.Book;
import com.aeonbank.library.model.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TransactionRepository {

    @Select("""
            select id
             from `transaction`
             where return_date is not null
             for update
            """)
    List<Long> getReturnedId();

    @Delete("""
            delete from `transaction`
             where id in
             <foreach item="id" collection="idList" open="(" close=")" separator=",">
            #{id}
            </foreach>
            """)
    int deleteByIds(List<Long> idList);

    @Insert("""
            insert into `transaction`(book_id, borrower_id)
             values(#{book_id}, #{borrower_id})
            """)
    int insert(Transaction transaction);

    @Update("""
            update `transaction` set
             return_date = #{return_date}
             where id = #{id}
            """)
    int updateReturnDate(Transaction transaction);

    @Select("""
            select *
             from `transaction`
             where book_id = #{book_id}
             and borrower_id = #{borrower_id}
             and return_date is null
             limit 1
             for update
            """)
    Transaction getByBookIdAndBorrowerId(Long bookId, Long borrowerId);

}
