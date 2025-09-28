package api.Day1;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	void createUserTest() throws IOException {
		Response response =given().baseUri(baseURI).body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Xtena\\eclipse-workspace\\RahulShetty\\users.json")))) //or payload.getPayload from payload class 
		.when().post("/users");
		userID = response.jsonPath().getString("id");
		Assert.assertEquals(response.getStatusCode(), 201);
		
	}
	
	@Test(priority=2)
	void updateUserTest() {
		Response response =given().baseUri(baseURI).body(payload.getUpdatedPayload()).pathParam("id", userID)
		.when().put("/users/{id}");
		String updatedName= response.jsonPath().getString("first_name");
		Assert.assertEquals(updatedName,"Jane Updated");
		
	}
	
	@Test(priority=3)
	void deleteTest() {
		given().baseUri(baseURI).pathParam("id", userID)
		.when().delete("/users/{id}")
		.then().log().all().statusCode(200);
	}

}
