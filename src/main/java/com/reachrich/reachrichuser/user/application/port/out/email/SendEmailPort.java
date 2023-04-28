package com.reachrich.reachrichuser.user.application.port.out.email;

public interface SendEmailPort {

    void sendVerificationEmail(String email, String authCode);
}
