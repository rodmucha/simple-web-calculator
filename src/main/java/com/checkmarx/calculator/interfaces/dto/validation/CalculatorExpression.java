package com.checkmarx.calculator.interfaces.dto.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { Validator.class })
public @interface CalculatorExpression {

    String message()

    default "Calculator expression is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}