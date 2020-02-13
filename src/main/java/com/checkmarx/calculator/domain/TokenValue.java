package com.checkmarx.calculator.domain;


public class TokenValue {

    private final Double value;

    public TokenValue(Double value) {
        this.value = value;
    }

    public TokenValue(String input) {
        this.value = parseDouble(input);
    }

    public Double getValue() {
        return value;
    }

    public static boolean isTokenValue(String value) {
        return isNumeric(value);
    }

    private static boolean isNumeric(String value) {
        if (value == null) {
            return false;
        }

        try {
            Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    private static Double parseDouble(String value) {
        return Double.parseDouble(value);
    }
}