package com.checkmarx.calculator.interfaces.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.checkmarx.calculator.interfaces.dto.validation.CalculatorExpression;

/**
 * CalculatorRequestDto
 */
public class CalculatorRequestDto {

    @NotBlank
    @Size(min = 3)
    @CalculatorExpression
    private String expression;

    public CalculatorRequestDto() {
        super();
    }

    public CalculatorRequestDto(final String expression) {
        setExpression(expression);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(final String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "CalculatorRequestDto [expression=" + expression + "]";
    }
}