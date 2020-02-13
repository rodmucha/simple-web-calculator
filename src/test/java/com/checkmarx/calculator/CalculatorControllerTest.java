package com.checkmarx.calculator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.MediaType;

import com.checkmarx.calculator.interfaces.dto.CalculatorRequestDto;
import com.checkmarx.calculator.interfaces.dto.CalculatorResponseDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;


@QuarkusTest
public class CalculatorControllerTest {

    @BeforeAll
    static void giveMeAMapper() {
        final Jsonb jsonb = JsonbBuilder.create();
        ObjectMapper mapper = new ObjectMapper() {
            @Override
            public Object deserialize(ObjectMapperDeserializationContext context) {
                return jsonb.fromJson(context.getDataToDeserialize().asString(), context.getType());
            }

            @Override
            public Object serialize(ObjectMapperSerializationContext context) {
                return jsonb.toJson(context.getObjectToSerialize());
            }
        };
        RestAssured.objectMapper(mapper);
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/api/hello")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

    @Test
    public void testCalculateEndpoint() {
        given()
          .when()
            .get("/api/calculate?expression=2+3*4/12")
          .then()
             .statusCode(200)
             .body(is("3.0"));
    }

    @Test
    public void testAjaxCalculateEndpoint() {
        final CalculatorResponseDto calculatorResponseDto = given()
                .body(new CalculatorRequestDto("2+3*4/12"))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                    .post("/api/ajax/calculate")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(CalculatorResponseDto.class);
        
        Assertions.assertEquals(
            new CalculatorResponseDto(3.0),
            calculatorResponseDto
        );
        
    }

}