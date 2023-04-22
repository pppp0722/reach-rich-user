package com.reachrich.reachrichuser.infrastructure.util;

import static com.reachrich.reachrichuser.infrastructure.util.Constants.ALPHABET_LEN;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.AUTH_CODE_LEN;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.DIGIT;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.LOWER_CASE;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.LOWER_CASE_START;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.UPPER_CASE;
import static com.reachrich.reachrichuser.infrastructure.util.Constants.UPPER_CASE_START;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomGenerator {

    public static String generateAuthCode() {
        return generateAuthCodeInternal(new Random().ints(AUTH_CODE_LEN, 0, 3).boxed());
    }

    private static String generateAuthCodeInternal(Stream<Integer> typeStream) {
        Random random = new Random();
        return typeStream.map(type -> {
            char c;
            switch (type) {
                case DIGIT:
                    c = Character.forDigit(random.nextInt(10), 10);
                    break;
                case LOWER_CASE:
                    c = (char) (LOWER_CASE_START + random.nextInt(ALPHABET_LEN));
                    break;
                case UPPER_CASE:
                    c = (char) (UPPER_CASE_START + random.nextInt(ALPHABET_LEN));
                    break;
                default:
                    c = '0';
            }
            return c;
        }).map(String::valueOf).collect(Collectors.joining());
    }
}
