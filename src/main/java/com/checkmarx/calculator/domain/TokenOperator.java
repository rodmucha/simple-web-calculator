package com.checkmarx.calculator.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.DoubleStream;

public enum TokenOperator {
    PLUS_OPERATOR("+", 1, (a,b) -> a+b),
    MINUS_OPERATOR("-", 1, (a,b) -> a-b),
    MULTIPLICATION_OPERATOR("*", 2, (a,b) -> a*b),
    DIVISION_OPERATOR("/", 2, (a,b) -> a/b);

    private static final Map<String, TokenOperator> TOKEN_OPERATOR_MAP 
        = generateTokenOperatorMap();

    private final int precedence;
    private final String token;
    private final DoubleBinaryOperator binaryOperator;

    private TokenOperator(final String token, final int precedence, 
        final DoubleBinaryOperator binaryOperator) {
        
        this.precedence = precedence;
        this.token = token;
        this.binaryOperator = binaryOperator;
    }

    public final int getPrecedence() {
        return precedence;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return this.getToken();
    }

    public static Optional<TokenOperator> getTokenOperator(final String value) {
        if (TOKEN_OPERATOR_MAP.containsKey(value)) {
            return Optional.of(TOKEN_OPERATOR_MAP.get(value));
        }

        return Optional.empty();
    }

    public static boolean containsTokenOperator(final String value) {
        return TOKEN_OPERATOR_MAP.containsKey(value);
    }

    public Double operate(final Double a, final Double b) { 
        
        return DoubleStream.of(a, b)
                .reduce(getBinaryOperator())
                .getAsDouble();
    }

    protected DoubleBinaryOperator getBinaryOperator(){
        return binaryOperator;
    }

    private static final Map<String, TokenOperator> generateTokenOperatorMap() {
        final Map<String, TokenOperator> map = new HashMap<>();

        for (final TokenOperator tokenOperatorType : TokenOperator.values()) {
            map.put(tokenOperatorType.getToken(), tokenOperatorType);
        }

        return map;
    }

    
}