package org.example.library_api.controller;

import org.example.library_api.model.Book;
import org.example.library_api.service.BorrowResult;
import org.example.library_api.service.LibraryService;
import org.example.library_api.service.ReturnResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class LibraryController {
    private static final Logger log = LoggerFactory.getLogger(LibraryController.class);
    private final LibraryService libraryService;

    // 建構子注入，Spring 會自動把 LibraryService 塞進來
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("available")
    public List<Book> getAvailableBooks() {
        return libraryService.availableBookList();
    }

    @GetMapping("/search")
    public List<Book> searchBook(@RequestParam String keyword){
        return libraryService.searchBook(keyword);
    }

    @GetMapping("/borrowedByMember")
    public ResponseEntity<List<Book>> getBorrowedByMember(@RequestParam String userId){
        List<Book> books = libraryService.getMemberBorrowedBookList(userId);
        if(books == null){
            return  ResponseEntity.notFound().build(); // 404
        }
        return ResponseEntity.ok(books); // 200 + 書籍清單
    }

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestParam String userId,@RequestParam String isbn){
        BorrowResult result = libraryService.borrowBook(userId,isbn);

        if(result == BorrowResult.SUCCESS){
            log.info("【借書】借書成功: 會員 {}，書籍 {}",userId,isbn);
        }else{
            log.warn("【借書】借書失敗: {}, 會員 {}，書籍 {}",result,userId,isbn);
        }
        return switch (result){
            case SUCCESS -> ResponseEntity.ok("【書籍借取完成】");
            case MEMBER_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("【借閱失敗:查無會員】");
            case BOOK_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("【借閱失敗:查無書籍】");
            case ALREADY_BORROWED -> ResponseEntity.status(HttpStatus.CONFLICT).body("【借閱失敗:書籍已被借閱】");
        };
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam String userId,@RequestParam String isbn){
        ReturnResult result = libraryService.returnBook(userId,isbn);
        if(result == ReturnResult.SUCCESS){
            log.info("【還書】還書成功: 會員 {}，書籍 {}",userId,isbn);
        }else{
            log.warn("【還書】還書失敗: {}, 會員 {}，書籍 {}",result,userId,isbn);
        }
        return switch (result){
            case SUCCESS -> ResponseEntity.ok("【書籍歸還成功】");
            case MEMBER_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("【歸還失敗:查無此會員】");
            case NOT_BORROWED_BY_MEMBER -> ResponseEntity.status(HttpStatus.CONFLICT).body("【歸還失敗:您並未借閱此書籍】");
        };
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book){
        if(isBlank(book.getName()) || isBlank(book.getAuthor()) ||  isBlank(book.getISBN())){
            log.warn("【新增書籍】新增失敗:欄位空白");
            return ResponseEntity.badRequest().body("書名、作者、ISBN皆不可空白");
        }
        if(!libraryService.addNewBook(book)){
            log.warn("【新增書籍】新增失敗: ISBN {} 已存在", book.getISBN());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ISBN 已存在");
        }
        log.info("【新增書籍】新增成功:{}，ISBN {}",book.getName(),book.getISBN());
        return ResponseEntity.status(HttpStatus.CREATED).body("新增書籍成功");
    }

    private boolean isBlank(String value){
        return value == null || value.isBlank();
    }
}
