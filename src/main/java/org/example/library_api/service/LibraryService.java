package org.example.library_api.service;


import org.example.library_api.model.Book;
import org.example.library_api.model.Member;
import org.example.library_api.repository.BookRepository;
import org.example.library_api.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LibraryService(BookRepository bookRepository, MemberRepository memberRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    // 借書
    @Transactional
    public BorrowResult borrowBook(String userId,String ISBN) {
        Member member = memberRepository.findById(userId).orElse(null);
        if(member == null){
            return BorrowResult.MEMBER_NOT_FOUND;
        }

        Book book = bookRepository.findById(ISBN).orElse(null);
        if(book == null){
            return BorrowResult.BOOK_NOT_FOUND;
        }

        if (book.isBorrowed()) {
            return BorrowResult.ALREADY_BORROWED;
        }

        book.setBorrowedBy(member);
        bookRepository.save(book);
        return BorrowResult.SUCCESS;
    }

    // 還書
    @Transactional
    public ReturnResult returnBook(String userId,String ISBN) {
        if(!memberRepository.existsById(userId)){
            return ReturnResult.MEMBER_NOT_FOUND;
        }

        Book book = bookRepository.findById(ISBN).orElse(null);
        if(book == null || book.getBorrowedBy() == null
                || !book.getBorrowedBy().getUserId().equals(userId)){
            return ReturnResult.NOT_BORROWED_BY_MEMBER;
        }
        book.setBorrowedBy(null);
        bookRepository.save(book);
        return ReturnResult.SUCCESS;
    }

    // 可借閱的書籍清單
    public List<Book> availableBookList(){
        return bookRepository.findByBorrowedByIsNull();
    }

    // 查詢書籍狀態
    public List<Book> searchBook(String keyword) {
        return bookRepository.search(keyword);
    }

    // 查詢會員已借閱書籍清單
    public List<Book> getMemberBorrowedBookList(String memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null) return null;
        return bookRepository.findByBorrowedBy(member);
    }

    // 新增書籍
    @Transactional
    public boolean addNewBook(Book book) {
        if(bookRepository.existsById(book.getISBN())){
            return false;
        }
        bookRepository.save(book);
        return true;
    }

    // 新增會員
    @Transactional
    public boolean addNewMember(Member member) {
        if(memberRepository.existsById(member.getUserId())){
            return false;
        }
        memberRepository.save(member);
        return true;
    }


}
