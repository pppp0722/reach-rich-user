package com.reachrich.reachrichuser.global.util;

import static com.reachrich.reachrichuser.global.exception.ErrorCode.EMAIL_SEND_FAILURE;

import com.reachrich.reachrichuser.global.exception.CustomException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(String email, String authCode) {
        try {
            String message = createVerificationEmailMessage(authCode);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(email);
            helper.setSubject("[Reach Rich] 회원가입 인증코드 발송");
            helper.setText(message, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("인증 이메일 전송 실패 : {}", e.getMessage());
            throw new CustomException(EMAIL_SEND_FAILURE);
        }
    }

    private String createVerificationEmailMessage(String authCode) {
        return "<div><h1>" + authCode + "</h1></div>";
    }
}
