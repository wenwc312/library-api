package org.example.library_api.config;

import org.example.library_api.model.Book;
import org.example.library_api.model.Member;
import org.example.library_api.repository.BookRepository;
import org.example.library_api.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public DataInitializer(BookRepository bookRepository, MemberRepository memberRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(bookRepository.count()==0){
            bookRepository.saveAll(List.of(
                    new Book("野性的呼喚","傑克·倫敦","s0001"),
                    new Book("侏儸紀公園","麥可·克萊頓","s0002"),
                    new Book("哈利波特:神秘的魔法石","J·K·羅琳","f0001"),
                    new Book("三國演義","羅貫中","h0001"),
                    new Book("盜墓筆記:七星魯王宮","南派三叔","c0001")
            ));
        }
        if(memberRepository.count()==0){
            memberRepository.saveAll(List.of(
                    new Member("王小明","U0001"),
                    new Member("王小美","U0002")
            ));
        }
    }
}
