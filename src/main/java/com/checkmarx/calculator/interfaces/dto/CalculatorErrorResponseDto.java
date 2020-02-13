package com.checkmarx.calculator.interfaces.dto;

/**
 * CalculatorErrorResponseDto
 */
public class CalculatorErrorResponseDto {

    private String error;

    public CalculatorErrorResponseDto(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
    
}