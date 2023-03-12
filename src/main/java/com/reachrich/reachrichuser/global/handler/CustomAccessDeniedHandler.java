package com.reachrich.reachrichuser.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reachrich.reachrichuser.global.exception.ErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter()
            .write(objectMapper.writeValueAsString(new ErrorResponse("API 호출에 필요한 권한이 없습니다.")));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
