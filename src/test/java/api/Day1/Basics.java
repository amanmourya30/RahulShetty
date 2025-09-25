package api.Day1;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.files.payload;
import io.restassured.response.Response;

public class Basics {
	
	static String baseURI ="http://localhost:3000";
	String userID;
	
	@Test(priority=1)
	void basicTest()
	{
		Response response =given().baseUri(baseURI).log().all()
		.when().get("/users");
		
		String contentTypeHd = response.getHeader("Content-Type");
		System.out.println("header found is: "+ contentTypeHd);
		Assert.assertEquals(contentTypeHd, "application/json");
		
		

	}
	
	@Test(priority=2)
	void createUserTest() {
		Response response =given().baseUri(baseURI).body(payload.getPayload())
		.when().post("/users");
		userID = response.jsonPath().getString("id");
		Assert.assertEquals(response.getStatusCode(), 201);
		
	}
	
	@Test(priority=3)
	void deleteTest() {
		given().baseUri(baseURI).pathParam("id", userID)
		.when().delete("/users/{id}")
		.then().log().all().statusCode(200);
	}

}
