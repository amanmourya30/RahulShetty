package serialDeserial;

import org.testng.annotations.Test;
import io.restassured.http.ContentType;

import pojo.Address;
import pojo.User;

import static io.restassured.RestAssured.*;

import java.util.Arrays;

public class DemoSerializationAndDeserialization {

    private static final String BASE_URI = "http://localhost:3000";

    @Test(priority = 1)
    void getUsersData() {
        given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .log().all()
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .log().all();
    }

    @Test(priority = 2)
    void createUser() {
        // Create Address
        Address address = new Address("101 Test Avenue", "QA City", "33445");

        // Create User
        User user = new User(
                "3",
                "jane.doe@test.io",
                "Jane",
                "Doe",
                true,
                address,
                Arrays.asList("Cypress", "JavaScript", "API Testing")
        );

        given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .body(user) // Serialization happens here
            .log().all()
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .log().all();
    }
    
    @Test(priority = 3)
    void getUsersByID() {
        User user =given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .log().all()
        .when()
            .get("/users/3")
        .then()
            .statusCode(200)
            .log().all().extract().as(User.class);
        
        System.out.println(user.getAddress().getCity());
        System.out.println(user.getFirst_name());
//        System.out.println(user.getSkills());
        
        for(String skill:user.getSkills()) {
        	System.out.println(skill);
        }
    }
    
}























