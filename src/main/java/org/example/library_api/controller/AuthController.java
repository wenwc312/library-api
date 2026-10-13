package org.example.library_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.library_api.DTO.LoginRequest;
import org.example.library_api.service.LoginAttemptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private LoginAttemptService loginAttemptService;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    public AuthController(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session,
                                        HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();

        if(loginAttemptService.isLocked(ip)){
            log.warn("【登入】鎖定中仍嘗試登入: 帳號 {}，IP {}",request.username(),ip);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("登入失敗次數過多，請15分鐘後再試");
        }

        if(adminUsername.equals(request.username())&&adminPassword.equals(request.password())) {
            loginAttemptService.recordSuccess(ip);
            session.setAttribute("loginUser", request.username());
            log.info("【登入】登入成功: 帳號 {}",request.username());
            return ResponseEntity.ok("登入成功");
        }

        int remaining = loginAttemptService.recordFailure(ip);
        log.warn("【登入】登入失敗: 帳號 {}，IP {}，剩餘次數 {}",request.username(),ip,remaining);
        if(remaining == 0){
            return ResponseEntity.status((HttpStatus.TOO_MANY_REQUESTS))
                    .body("登入失敗次數過多，已鎖定15分鐘");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("帳號或密碼錯誤，剩餘嘗試次數 "+remaining+" 次");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        log.info("【登出】登出成功: 帳號 {}",session.getAttribute("loginUser"));
        session.invalidate();
        return ResponseEntity.ok("已登出");
    }
}
