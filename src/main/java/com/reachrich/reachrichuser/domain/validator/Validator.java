package com.reachrich.reachrichuser.domain.validator;

public interface Validator<T> {

    boolean validate(T objectToValidate);
}