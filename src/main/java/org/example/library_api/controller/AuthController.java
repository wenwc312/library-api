package org.example.library_api.controller;

import jakarta.servlet.http.HttpSession;
import org.example.library_api.DTO.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {
        if(adminUsername.equals(request.username())&&adminPassword.equals(request.password())) {
            session.setAttribute("loginUser", request.username());
            return ResponseEntity.ok("登入成功");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳號或密碼錯誤");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("已登出");
    }
}
