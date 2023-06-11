package org.example.tests;
import org.example.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.specs.Specs.*;


public class restApiTests {
    String userName = "John";
    String userJob = "Teacher";
    String updatedJob = "Director";
    @BeforeEach
    public void beforeEach(){
        baseURI ="https://reqres.in/";
        basePath = "/api";
    }
    @Test
    @DisplayName("User is found:check ID")
    void checkSingleUser () {
        Integer expectedUserId = 2;
        SingleUserResponse response = step("Make a request", () -> given(requestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(response200Spec)
                .extract().as(SingleUserResponse.class));
        step("Check response", () ->
        assertThat(response.getData().getId().equals(expectedUserId)));
    }
    @Test
    @DisplayName("User not found")
    void checkUserNotFound () {
        BadRequest response = step ("Make a request", () -> given(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .spec(response404Spec))
                .extract().as(BadRequest.class);
        step("Check response", () ->
                assertThat(response.getError()).isNull());
    }
    @Test
    @DisplayName("Create User")
    void createUser () {
        CreateUser requestBody = new CreateUser();
        requestBody.setName(userName);
        requestBody.setJob(userJob);

        CreateUserResponse response = step("Make a request", () -> given(requestSpec)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .spec(response201Spec)
                .extract().as(CreateUserResponse.class));
        step("Check Name in response", () ->
            assertThat(response.getName().equals(userName)));

        step("Check Job in response", () ->
            assertThat(response.getJob().equals(userJob)));

    }
    @Test
    @DisplayName("Delete User")
    void deleteUser () {
        step("Make a request",() -> given(deleteUserRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(response204Spec));
    }
    @Test
    @DisplayName("Update User")
    void updateUser () {
        CreateUser requestBody = new CreateUser();
        requestBody.setName(userName);
        requestBody.setJob(updatedJob);

        UpdateUserResponse updateUserResponse = step("Make a request", ()-> given(requestSpec)
                .body(requestBody)
                .when()
                .patch("/users/2")
                .then()
                .spec(response200Spec)
                .extract().as(UpdateUserResponse.class));
        step("Check response", () ->
                assertThat(updateUserResponse.getJob()).isEqualTo(updatedJob));
    }
}
