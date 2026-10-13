package org.example.library_api.controller;

import org.example.library_api.model.Member;
import org.example.library_api.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final LibraryService libraryService;

    public MemberController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public ResponseEntity<String> addMember(@RequestBody Member member) {
        if(isBlank(member.getName()) || isBlank(member.getUserId())){
            log.warn("【新增會員】新增失敗:欄位空白");
            return ResponseEntity.badRequest().body("會員名稱及會員ID不可空白");
        }
        if(!libraryService.addNewMember(member)){
            log.warn("【新增會員】新增失敗: 會員編號 {} 已存在",member.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("會員編號已存在");
        }
        log.info("【新增會員】新增成功: {}，會員編號 {}",member.getName(),member.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body("新增會員成功");
    }

    private boolean isBlank(String value){
        return value == null || value.isBlank();
    }
}
