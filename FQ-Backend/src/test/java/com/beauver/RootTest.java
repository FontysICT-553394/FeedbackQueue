package com.beauver;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class RootTest {

    @Test
    void testRootEndpoint() {
        given()
                .when().get("/")
                .then()
                .statusCode(200)
                .body(is("{\"status\":\"200\",\"message\":\"OK\"}"));
    }

}
