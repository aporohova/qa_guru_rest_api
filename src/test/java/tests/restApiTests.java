package tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.is;

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
        String requestBody = "{\n" +
                "    \"name\": \"John\",\n" +
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
                .statusCode(201)
                .body("name", is("John"));
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
        String requestBody = "{\n" +
                "    \"name\": \"John\",\n" +
                "    \"job\": \"Director\"\n" +
                "}";
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .patch("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("Director"));
    }
}
