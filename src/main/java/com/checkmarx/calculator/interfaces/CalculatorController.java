package com.checkmarx.calculator.interfaces;

import java.util.Optional;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.checkmarx.calculator.application.Calculator;
import com.checkmarx.calculator.interfaces.dto.CalculatorErrorResponseDto;
import com.checkmarx.calculator.interfaces.dto.CalculatorRequestDto;
import com.checkmarx.calculator.interfaces.dto.CalculatorResponseDto;

@Path("/api")
public class CalculatorController {

    @GET
    @Path("/calculate")
    @Produces(MediaType.TEXT_PLAIN)
    public Response calculate(@QueryParam("expression") @Valid CalculatorRequestDto expression) {
        final Calculator calculator = new Calculator();
        final Optional<Double> result = calculator.calculate(expression.getExpression());

        if(!result.isPresent()){
            
            return Response.status(Status.BAD_REQUEST)
                    .entity(calculator.getErrorMessage())
                    .build();
        }

        return Response.ok(result.get()).build();
    }

    @POST
    @Path("/ajax/calculate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateThroughAjax(@Valid CalculatorRequestDto expression) {
        final Calculator calculator = new Calculator();
        final Optional<Double> result = calculator.calculate(expression.getExpression());

        if(!result.isPresent()){
            final CalculatorErrorResponseDto calculatorErrorResponseDto 
                = new CalculatorErrorResponseDto(calculator.getErrorMessage()); 
            
            return Response.status(Status.BAD_REQUEST)
                    .entity(calculatorErrorResponseDto)
                    .build();
        }

        final CalculatorResponseDto calculatorResponseDto 
            = new CalculatorResponseDto(result.get());
        
            return Response.ok(calculatorResponseDto).build();
    }


    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public String hello(){
        return "hello";
    }
}