package dataDrivenExcelAndJSON;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.http.ContentType;
import pojo.Address;
import pojo.User;
import utilities.ExcelUtils;

public class DDTexcel {
	private static List<String> createdIds= new ArrayList<String>();
	private static RequestSpecification requestSpec;
	private static ResponseSpecification responseSpec200;
	private static ResponseSpecification responseSpec201;
	private static ResponseSpecification responseSpec404;
	private static ResponseSpecification responseSpec200or204;
	
	@BeforeClass
	void setup() {
	    
	    requestSpec = new RequestSpecBuilder()
	    			.setBaseUri("http://localhost:3000")
	    			.setContentType(ContentType.JSON)
	    			.build();
	    responseSpec201 = new ResponseSpecBuilder()
					.expectStatusCode(201)
					.build();
	    responseSpec200 = new ResponseSpecBuilder()
					.expectStatusCode(200)
					.build();
	    responseSpec404 = new ResponseSpecBuilder()
					.expectStatusCode(404)
					.build();
	    responseSpec200or204 = new ResponseSpecBuilder()
	            .expectStatusCode(anyOf(equalTo(200), equalTo(204)))
	            .build();

	}
	
	@Test(dataProvider="ExcelUserData" ,priority = 1)
    void testCreateUserFromExcel(String id, String email, String first_name, String last_name,
            String is_active, String street, String city, String zipcode,
            String skills) {
		// --- Build POJO ---
		User user = buildUser(id, email, first_name, last_name, is_active, street, city, zipcode, skills);
        given()
            .spec(requestSpec)
            .body(user) // Serialization happens here
        .when()
            .post("/users")
        .then()
        	.spec(responseSpec201)
            .log().ifValidationFails();
        createdIds.add(id);
    }
	
	@Test(priority = 2,dependsOnMethods = "testCreateUserFromExcel" )
	void printNamesById() {
		SoftAssert softAssert = new SoftAssert();
		for(String id: createdIds) {
			User user = given()
					.spec(requestSpec)
					.when()
			            .get("/users/"+id)
			        .then()
			            .spec(responseSpec200)
			            .log().ifValidationFails()
			            .extract().as(User.class);
			
			System.out.println("For id: "+id+"--->"+ user.getFirst_name()+" "+user.getLast_name());	
			softAssert.assertNotNull(user.getFirst_name(), "First name should not be null");

		}
		softAssert.assertAll();
	}
	
	@Test(priority = 3,dependsOnMethods = "testCreateUserFromExcel" )
	void deleteUserById() {
		SoftAssert softAssert = new SoftAssert();
		for(String id: createdIds) {
			int statusCode =given()
						.spec(requestSpec)
				        .when()
				            .delete("/users/"+id)
				        .then()
				        	.spec(responseSpec200or204)
				            .log().ifValidationFails()
				            .extract().statusCode();
			softAssert.assertTrue(statusCode==200||statusCode==204, "Delete failed for ID: " + id);
		}
		softAssert.assertAll();
	}
	
	@Test(priority = 4,dependsOnMethods = "deleteUserById" )
	void verifyDeletionByIds() {
		SoftAssert softAssert = new SoftAssert();
		for(String id: createdIds) {
			String responseBody = given()
					.spec(requestSpec)
	        .when()
	            .get("/users/"+id)
	        .then()
	            .spec(responseSpec404)
	            .log().ifValidationFails()
	            .extract().asString();		
			
			softAssert.assertEquals(responseBody.trim(), "Not Found","User ID: " + id + " was not deleted properly");
		}
		createdIds.clear();
		softAssert.assertAll();
	}
	
	
	@DataProvider(name = "ExcelUserData")
	public Object[][] getUserDataFromExcel() {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testData/userData.xlsx";
	    return ExcelUtils.getExcelData(filePath, "Sheet1");
	}
	
	private User buildUser(String id, String email, String first, String last,
            String isActive, String street, String city, String zipcode,
            String skills) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setFirst_name(first);
		user.setLast_name(last);
		user.setIs_active(Boolean.parseBoolean(isActive));
		
		Address addr = new Address();
		addr.setStreet(street);
		addr.setCity(city);
		addr.setZipcode(zipcode);
		user.setAddress(addr);
		
		user.setSkills(Arrays.asList(skills.split(",")));
		return user;
		}

}
