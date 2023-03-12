package com.reachrich.reachrichuser.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reachrich.reachrichuser.global.exception.ErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter()
            .write(objectMapper.writeValueAsString(new ErrorResponse("로그인 인증 정보가 존재하지 않습니다.")));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
