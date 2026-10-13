package org.example.library_api.service;

import org.example.library_api.model.Book;
import org.example.library_api.model.Member;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {
    private List<Book> allBooks;
    private List<Member> allMembers;

    public LibraryService() {
        allBooks = new ArrayList<>();
        allMembers = new ArrayList<>();

        // 測試資料
        allBooks.add(new Book("野性的呼喚","john","s0001"));
        allBooks.add(new Book("侏儸紀公園","mary","s0002"));
        allBooks.add(new Book("哈利波特","JK","f0001"));
        allBooks.add(new Book("三國演義","羅貫中","h0001"));
        allMembers.add(new Member("王小明","U0001"));
        allMembers.add(new Member("陳大天","U0002"));
    }

    public void addBook(Book book) {
        allBooks.add(book);
    }

    public void addMember(Member member) {
        allMembers.add(member);
    }

    // 借書
    public BorrowResult borrowBook(String userId,String ISBN) {
        Member member = findMember(userId);
        if(member == null){
            return BorrowResult.MEMBER_NOT_FOUND;
        }

        Book book = findBook(allBooks,ISBN);
        if(book == null){
            return BorrowResult.BOOK_NOT_FOUND;
        }

        if (book.isBorrowed()) {
            return BorrowResult.ALREADY_BORROWED;
        }

        book.setBorrowed(true);
        member.borrowBook(book);
        return BorrowResult.SUCCESS;
    }

    // 還書
    public ReturnResult returnBook(String userId,String ISBN) {
        Member member = findMember(userId);
        if(member == null){
            return ReturnResult.MEMBER_NOT_FOUND;
        }

        Book book = findBook(member.getBorrowedBooks(),ISBN);
        if(book == null){
            return ReturnResult.NOT_BORROWED_BY_MEMBER;
        }
        book.setBorrowed(false);
        member.returnBook(book);
        return ReturnResult.SUCCESS;
    }

    // 可借閱的書籍清單
    public List<Book> availableBookList(){
        List<Book> borrowedList = new ArrayList<>();
        for (Book book:allBooks){
            if(!book.isBorrowed()){
                borrowedList.add(book);
            }
        }
        return borrowedList;
    }

    // 查詢書籍狀態
    public List<Book> searchBook(String keyword) {
        List<Book> bookList = new ArrayList<>();
        for (Book book:allBooks){
            if (book.getName().contains(keyword) || book.getAuthor().contains(keyword) || book.getISBN().contains(keyword)){
                bookList.add(book);
            }
        }
        return bookList;
    }

    // 查詢會員已借閱書籍清單
    public List<Book> getMemberBorrowedBookList(String memberId) {
        Member member = findMember(memberId);
        if(member == null) return null;
        return member.getBorrowedBooks();
    }

    // 新增書籍
    public boolean addNewBook(Book book) {
        for (Book b:allBooks){
            if(b.getISBN().equals(book.getISBN())){
                return false;
            }
        }
        book.setBorrowed(false);
        allBooks.add(book);
        return true;
    }

    // 新增會員
    public boolean addNewMember(Member member) {
        for(Member m: allMembers){
            if (m.getUserId().equals(member.getUserId())){
                return false;
            }
        }
        allMembers.add(member);
        return true;
    }

    // 抽出共用找會員方法
    private Member findMember(String userId) {
        for (Member member : allMembers){
            if (member.getUserId().equals(userId)){
                return member;
            }
        }
        return null;
    }

    // 抽出共用找書籍方法
    private Book findBook(List<Book> bookList,String isbn) {
        for (Book book:bookList){
            if (book.getISBN().equals(isbn)){
                return book;
            }
        }
        return null;
    }
}
