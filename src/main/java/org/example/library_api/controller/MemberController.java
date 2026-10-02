package org.example.library_api.controller;

import org.example.library_api.model.Member;
import org.example.library_api.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final LibraryService libraryService;

    public MemberController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public ResponseEntity<String> addMember(@RequestBody Member member) {
        if(isBlank(member.getName()) || isBlank(member.getUserId())){
            return ResponseEntity.badRequest().body("會員名稱及會員ID不可空白");
        }
        if(!libraryService.addNewMember(member)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("會員編號已存在");
        }
        return ResponseEntity.status(HttpStatus.OK).body("新增會員成功");
    }

    private boolean isBlank(String value){
        return value == null || value.isBlank();
    }
}
