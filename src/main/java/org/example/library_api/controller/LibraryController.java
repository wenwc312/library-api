package org.example.library_api.controller;

import org.example.library_api.model.Book;
import org.example.library_api.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class LibraryController {
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
    public String borrowBook(@RequestParam String userId,@RequestParam String isbn){
        return libraryService.borrowBook(userId,isbn);
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam String userId,@RequestParam String isbn){
        return libraryService.returnBook(userId,isbn);
    }


}
