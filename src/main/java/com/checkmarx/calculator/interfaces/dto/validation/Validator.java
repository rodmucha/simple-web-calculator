package com.checkmarx.calculator.interfaces.dto.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Validator implements ConstraintValidator<CalculatorExpression, String> {
    
    @Override
    public boolean isValid(final String expression, 
        final ConstraintValidatorContext constraintValidatorContext) {
        
        final String calculatorExpressionRegex = "(\\d)+(( *[\\+\\-\\*\\/] *)(\\d)+)+";
        
        return Pattern.matches(calculatorExpressionRegex, expression);
    }
}