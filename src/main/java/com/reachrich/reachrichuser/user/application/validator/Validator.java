package com.reachrich.reachrichuser.user.application.validator;

public interface Validator<T> {

    boolean validate(T objectToValidate);
}