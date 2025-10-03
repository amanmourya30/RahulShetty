package dataDrivenExcelAndJSON;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import pojo.Address;
import pojo.User;
import utilities.ExcelUtils;

public class DDTexcel {
	private static List<String> createdIds= new ArrayList<String>();
	
	@BeforeClass
	void setup() {
	    RestAssured.baseURI = "http://localhost:3000";
	}
	
	@Test(dataProvider="ExcelUserData" ,priority = 1)
    void testCreateUserFromExcel(String id, String email, String first_name, String last_name,
            String is_active, String street, String city, String zipcode,
            String skills) {
		// --- Build POJO ---
	    User user = new User();
	    user.setId(id);
	    user.setEmail(email);
	    user.setFirst_name(first_name);
	    user.setLast_name(last_name);
	    user.setIs_active(Boolean.parseBoolean(is_active));

	    Address addr = new Address();
	    addr.setStreet(street);
	    addr.setCity(city);
	    addr.setZipcode(zipcode);
	    user.setAddress(addr);

	    user.setSkills(Arrays.asList(skills.split(",")));

        given()
            .contentType(ContentType.JSON)
            .body(user) // Serialization happens here
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .log().ifValidationFails();
        createdIds.add(id);
    }
	
	@Test(priority = 2,dependsOnMethods = "testCreateUserFromExcel" )
	void printNamesById() {
		SoftAssert softAssert = new SoftAssert();
		for(String id: createdIds) {
			User user = given()
			            .contentType(ContentType.JSON)
			        .when()
			            .get("/users/"+id)
			        .then()
			            .statusCode(200)
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
				        .when()
				            .delete("/users/"+id)
				        .then()
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
	        .when()
	            .get("/users/"+id)
	        .then()
	            .statusCode(404)
	            .log().ifValidationFails()
	            .extract().asString();		
			
			softAssert.assertEquals(responseBody.trim(), "Not Found","User ID: " + id + " was not deleted properly");
		}
		softAssert.assertAll();
	}
	
	
	@DataProvider(name = "ExcelUserData")
	public Object[][] getUserDataFromExcel() {
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testData/userData.xlsx";
	    return ExcelUtils.getExcelData(filePath, "Sheet1");
	}
}
