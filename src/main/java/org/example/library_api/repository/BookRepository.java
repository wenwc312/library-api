package org.example.library_api.repository;

import org.example.library_api.model.Book;
import org.example.library_api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,String> {
    // 可借閱的書:borrowed_By 是 Null
    List<Book> findByBorrowedByIsNull();

    // 某位會員借的書
    List<Book> findByBorrowedBy(Member member);

    // 依書名、作者、ISBN 搜尋
    @Query("SELECT b FROM Book b WHERE b.name LIKE CONCAT('%', :keyword, '%')" +
            "OR b.author LIKE CONCAT('%', :keyword, '%')" +
            "OR b.ISBN LIKE CONCAT('%', :keyword, '%')")
    List<Book> search(@Param("keyword") String keyword);
}
