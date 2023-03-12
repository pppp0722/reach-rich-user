package com.reachrich.reachrichuser.global.filter;

import static com.reachrich.reachrichuser.global.util.Const.LOGIN_USER;
import static java.util.Objects.isNull;

import com.reachrich.reachrichuser.user.domain.User;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(LOGIN_USER);
        if (!isNull(user)) {
            SecurityContextHolder.getContext().setAuthentication(user.makeAuthentication());
        }
        filterChain.doFilter(request, response);
    }
}
