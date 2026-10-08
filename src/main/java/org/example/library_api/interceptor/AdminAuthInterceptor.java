package org.example.library_api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean loggedIn = session!=null && session.getAttribute("loginUser")!=null;

        if(loggedIn){
            return true; // 已登入，放行
        }

        if(request.getRequestURI().equals("/admin.html")){
            response.sendRedirect("/login.html"); //頁面，導向登入頁
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // API:回傳 401
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("請先登入");
        }
        return false; // 擋下請求
    }
}
