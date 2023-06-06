package tests;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;






public class restApiTests {
    /*
    1. Request https://reqres.in/
     */
    @Test
    void checkSingleUser () {
      get("https://reqres.in/")
                .then()
                .body("total", is(20));
    }
}
