package org.example.library_api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AdminAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean loggedIn = session!=null && session.getAttribute("loginUser")!=null;

        if(loggedIn){
            return true; // 已登入，放行
        }

        if(request.getRequestURI().equals("/admin.html")){
            log.info("【登入】未登入進入管理頁面，導向登入頁");
            response.sendRedirect("/login.html"); //頁面，導向登入頁
        } else {
            log.warn("【登入】未登入存取被拒: {} {}",request.getMethod(),request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // API:回傳 401
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("請先登入");
        }
        return false; // 擋下請求
    }
}
