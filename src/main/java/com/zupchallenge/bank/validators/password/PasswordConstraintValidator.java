package com.zupchallenge.bank.validators.password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // password must have:
        // minimum 8 chars, max 30
        // must have a upper char
        // must have a number

        return value != null &&
                value.length() >= 8 &&
                value.length() <= 30 &&
                value.matches("[A-Z]+") &&
                value.matches("[0-9]+");
    }
}
