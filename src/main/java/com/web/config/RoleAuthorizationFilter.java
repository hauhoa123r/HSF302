package com.web.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleAuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        HttpSession session = req.getSession(false);
        if (uri.startsWith("/login") ||
                uri.startsWith("/register") ||
                uri.startsWith("/logout") ||
                uri.startsWith("/forgot") ||
                uri.startsWith("/reset") ||
                uri.startsWith("/api/user/") ||
                uri.startsWith("/static") ||
                uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg")
        ) {
            chain.doFilter(request, response);
            return;
        }
        if (session == null || session.getAttribute("role") == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("401 - Chưa đăng nhập");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (uri.startsWith("/api/admin") || uri.startsWith("/admin")) {
            if (!"ADMIN".equals(role)) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("403 - Chỉ ADMIN được phép truy cập");
                return;
            }
        } else if (uri.startsWith("/api/trainer")) {
            if (!"TRAINER".equals(role) && !"ADMIN".equals(role)) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("403 - Chỉ TRAINER hoặc ADMIN được phép truy cập");
                return;
            }
        } else if (uri.startsWith("/api/user")) {
            if (!"MEMBER".equals(role) && !"TRAINER".equals(role) && !"ADMIN".equals(role)) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().write("403 - Không đủ quyền truy cập khu vực USER/MEMBER");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
