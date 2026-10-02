package org.example.library_api.service;

import org.example.library_api.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryServiceTest {
    private LibraryService service;

    @BeforeEach
    void setUp() {
        // 每個測試開始前都建立新的 LibraryService，資料都是初始測試資料
        service = new LibraryService();
    }

    @Test
    void 借書成功後書籍不在可借閱清單(){
        assertEquals("【書籍借取完成】",service.borrowBook("U0001","s0001"));

        boolean stillAvailable = service.availableBookList().stream()
                .anyMatch(book -> book.getISBN().equals("s0001"));
        assertFalse(stillAvailable);
    }

    @Test
    void 已被借走的書不能再借(){
        service.borrowBook("U0001","s0001");
        assertEquals("【查無書籍資料或書籍已被借閱】",service.borrowBook("U0002","s0001"));
    }

    @Test
    void 不能歸還別人借的書(){
        // Issue #7 的回歸測試
        service.borrowBook("U0001","s0001");
        service.borrowBook("U0002","s0002");

        assertEquals("查無書籍資料或會員未借閱此書籍",service.returnBook("U0002","s0001"));
        assertEquals(1,service.getMemberBorrowedBookList("U0001").size());
        assertEquals("歸還書籍成功",service.returnBook("U0001","s0001"));
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
