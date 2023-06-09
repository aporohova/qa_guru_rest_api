package org.example.tests;
import io.qameta.allure.restassured.AllureRestAssured;
import org.example.models.CreateUser;
import org.example.models.CreateUserResponse;
import org.example.models.UpdateUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.is;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class restApiTests {
    @BeforeEach
    public void beforeEach(){
        baseURI ="https://reqres.in/api";
    }
    @Test
    void checkSingleUser () {
        Integer expectedId = 2;
        Integer actualId = given()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path("data.id");
    assertEquals(expectedId,actualId);
    }
    @Test
    void checkUserNotFound () {
                get("/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }
    @Test
    void createUser () {
        CreateUser requestBody = new CreateUser();
        requestBody.setName("John");
        requestBody.setJob("Teacher");

        CreateUserResponse createUserResponse = given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(CreateUserResponse.class);
    }
    @Test
    void createUserNegative () {
        String requestBody = "{\n" +
                "    \"name\": \n" +
                "    \"job\": \"Teacher\"\n" +
                "}";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(400);
    }
    @Test
    void updateUser () {
        CreateUser requestBody = new CreateUser();
        requestBody.setName("John");
        requestBody.setJob("Director");

        UpdateUserResponse updateUserResponse = given()
                .log().uri()
                .log().body()
                .filter(new AllureRestAssured())
                .contentType(JSON)
                .body(requestBody)
                .when()
                .patch("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UpdateUserResponse.class);
    }
    @Test
    void checkListResource () {

        given()
                .log().uri()
                .log().body()
                .when()
                .get("/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/resource-list.json"))
                .body("total", is(12));
    }
}
