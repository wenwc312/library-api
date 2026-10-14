package org.example.library_api.service;

import org.example.library_api.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LibraryServiceTest {

    @Autowired
    private LibraryService service;

    @Test
    void 借書成功後書籍不在可借閱清單(){
        assertEquals(BorrowResult.SUCCESS,service.borrowBook("U0001","s0001"));

        boolean stillAvailable = service.availableBookList().stream()
                .anyMatch(book -> book.getISBN().equals("s0001"));
        assertFalse(stillAvailable);
    }

    @Test
    void 已被借走的書不能再借(){
        service.borrowBook("U0001","s0001");
        assertEquals(BorrowResult.ALREADY_BORROWED,service.borrowBook("U0002","s0001"));
    }

    @Test
    void 不能借不存在的書(){
        service.borrowBook("U0001","9999");
        assertEquals(BorrowResult.BOOK_NOT_FOUND,service.borrowBook("U0002","9999"));
    }

    @Test
    void 不能歸還別人借的書(){
        // Issue #7 的回歸測試
        service.borrowBook("U0001","s0001");
        service.borrowBook("U0002","s0002");

        assertEquals(ReturnResult.NOT_BORROWED_BY_MEMBER,service.returnBook("U0002","s0001"));
        assertEquals(1,service.getMemberBorrowedBookList("U0001").size());
        assertEquals(ReturnResult.SUCCESS,service.returnBook("U0001","s0001"));
    }

    @Test
    void 查無會員時借閱清單為null(){
        assertNull(service.getMemberBorrowedBookList("U9999"));
    }

    @Test
    void 新增重複ISBN的書籍回失敗(){
        assertTrue(service.addNewBook(new Book("西遊記","吳承恩","h0002")));
        assertFalse(service.addNewBook(new Book("另一本書","某人","h0002")));
    }
}
