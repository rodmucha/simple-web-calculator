package com.checkmarx.calculator.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.checkmarx.calculator.domain.TokenOperator;
import com.checkmarx.calculator.domain.TokenValue;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Calculator
 */
public class Calculator {

    private static final String ERROR_MESSAGE = "Expression error.";
    private static final int MAX_CACHE_SIZE = 10000;
    private static final int CACHE_EXPIRATION = 10;
    
    private static final Cache<String,Double> cache = CacheBuilder
        .newBuilder()
        .maximumSize(MAX_CACHE_SIZE)
        .expireAfterWrite(CACHE_EXPIRATION, TimeUnit.MINUTES)
        .build();

    private final Stack<TokenValue> valueStack;
    private final Stack<TokenOperator> operatorStack;
    private final Notification notification;
    
    


    public Calculator() {
        this.operatorStack = new Stack<>();
        this.valueStack = new Stack<>();
        this.notification = new Notification();
    }

    public String getErrorMessage(){
        return notification.errorMessage();
    }

    public Optional<Double> calculate(String input){
        Double cacheResult = cache.getIfPresent(input); 

        if(cacheResult != null){
            Optional.of(cacheResult);
        }

        valueStack.clear();
        operatorStack.clear();
        notification.clear();
        
        parseInput(input);
        
        if(notification.hasErrors()){
            return Optional.empty();
        }
        
        final Optional<Double> result = processResult();

        if(notification.hasErrors()){
            return Optional.empty();
        }
        
        cache.put(input, result.get());
        
        return result;
    }

    private Optional<Double> processResult(){
        
        while (!operatorStack.isEmpty() && 
            operatorStack.peek().getPrecedence() > 0) {
            
            processOperator(operatorStack.pop());
        }
            
        if (!operatorStack.isEmpty() || valueStack.size() > 1) {
            notification.addError(ERROR_MESSAGE);
            return Optional.empty();
        }

        return Optional.of(valueStack.pop().getValue());
    }

    private void parseInput(String input){
        final String[] tokens = input.split("");
        String lastToken = "";

        for (String token : tokens) {
            
            if (TokenValue.isTokenValue(token)) {
                lastToken = lastToken.concat(token);
                continue;
            } else if (!lastToken.isEmpty()) {
                tokenize(lastToken);
                lastToken = "";
            }

            tokenize(token);
        }

        if (!lastToken.isEmpty()) {
            tokenize(lastToken);
        }
    }

    private void tokenize(String token) {
        
        if(TokenOperator.containsTokenOperator(token)){
            final TokenOperator tokenOperator = TokenOperator.getTokenOperator(token).get();

            if (operatorStack.empty() || 
                tokenOperator.getPrecedence() > operatorStack.peek().getPrecedence()) {
            
                operatorStack.push(tokenOperator);
            } else {
                while (!operatorStack.empty() 
                    && tokenOperator.getPrecedence() <= operatorStack.peek().getPrecedence()) {
                    
                    processOperator(operatorStack.pop());
                }
                
                operatorStack.push(tokenOperator);
            }
        }else if(TokenValue.isTokenValue(token)){

            valueStack.push(new TokenValue(token));
        }

    }
    
    private void processOperator(TokenOperator operator) {
        TokenValue a, b;
        
        if (valueStack.isEmpty()) {
            notification.addError(ERROR_MESSAGE);
            return;
        } else {
            b = valueStack.pop();
        }

        if (valueStack.isEmpty()) {
            notification.addError(ERROR_MESSAGE);
            return;
        } else {
            a = valueStack.pop();
        }
        
        final Double result = operator.operate(a.getValue(), b.getValue());
        valueStack.push(new TokenValue(result));
    }

    public class Notification {
        private List<String> errors = new ArrayList<>();

        public void addError(String message) {
            errors.add(message);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public void clear(){
            errors.clear();
        }

        public String errorMessage() {
            return errors.stream()
              .collect(Collectors.joining(", "));
          }
    }
}