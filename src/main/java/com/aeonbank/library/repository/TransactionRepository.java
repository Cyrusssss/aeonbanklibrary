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
            <script>
            delete from `transaction`
             where id in
             <foreach item='item' collection='list' open='(' separator=',' close=')'>#{item}</foreach>
            </script>
            """)
    int deleteByIds(@Param("list") List<Long> ids);

    @Insert("""
            insert into `transaction`(book_id, borrower_id)
             values(#{bookId}, #{borrowerId})
            """)
    int insert(Transaction transaction);

    @Update("""
            update `transaction` set
             return_date = #{returnDate}
             where id = #{id}
            """)
    int updateReturnDate(Transaction transaction);

    @Select("""
            select *
             from `transaction`
             where book_id = #{bookId}
             and return_date is null
             limit 1
             for update
            """)
    Transaction getByBookIdNotReturnedForUpdate(Long bookId);

    @Select("""
            select count(*) CNT
             from `transaction`
             where borrower_id = #{borrowerId}
             and return_date is null
            """)
    @SelectKey(statement="", keyProperty="CNT", before=false, resultType=int.class)
    int getUnreturnedCountByBorrowerId(Long borrowerId);

}
