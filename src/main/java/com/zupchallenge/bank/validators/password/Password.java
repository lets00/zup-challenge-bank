package com.zupchallenge.bank.validators.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Invalid Password. Password must have: minimum 8 chars, max 30; a upper char; a number";
    Class<?>[] groups() default {};
    Class< ? extends Payload>[] payload() default {};
}
