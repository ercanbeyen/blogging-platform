package com.ercanbeyen.bloggingplatform.validatior;

import com.ercanbeyen.bloggingplatform.annotation.PasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordRequest, String> {

    @Override
    public void initialize(PasswordRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        String regex =  "^" +                   // start-of-string
                        "(?=.*[0-9])" +         // a digit must occur at least once
                        "(?=.*[a-z])" +         // a lower case letter must occur at least once
                        "(?=.*[A-Z])" +         // an upper case letter must occur at least once
                        "(?=.*[@#$%^&+=])" +    // a special character must occur at least once
                        "(?=\\S+$)" +           // no whitespace allowed in the entire string
                        ".{8,}" +               // anything, at least eight places though
                        "$";                    // end-of-string

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }

}
