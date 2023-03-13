package com.reachrich.reachrichuser.global.util;

import static com.reachrich.reachrichuser.global.util.Const.ALPHABET_LEN;
import static com.reachrich.reachrichuser.global.util.Const.AUTH_CODE_LEN;
import static com.reachrich.reachrichuser.global.util.Const.DIGIT;
import static com.reachrich.reachrichuser.global.util.Const.LOWER_CASE;
import static com.reachrich.reachrichuser.global.util.Const.LOWER_CASE_START;
import static com.reachrich.reachrichuser.global.util.Const.UPPER_CASE;
import static com.reachrich.reachrichuser.global.util.Const.UPPER_CASE_START;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
