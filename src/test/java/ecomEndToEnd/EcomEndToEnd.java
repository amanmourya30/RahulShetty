package ecomEndToEnd;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.LoginPojo;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;
import utilities.ExcelUtils;

public class EcomEndToEnd {
	private static String token;
	private static String userId;
	private static String productId;
	private static String orderId;
	private static RequestSpecification requestSpec;
	private static ResponseSpecification responseSpec200;
	private static ResponseSpecification responseSpec201;
	private static ResponseSpecification responseSpec404;
	private static ResponseSpecification responseSpec200or204;
	
	@BeforeClass
	void setup() {
	    
	    requestSpec = new RequestSpecBuilder()
	    			.setBaseUri("https://rahulshettyacademy.com")
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
	
	@Test(priority = 1)
    void loginTest() {
		// --- Build POJO ---
		LoginPojo cred = new LoginPojo();
		cred.setUserEmail("restassured99@gmail.com");
		cred.setUserPassword("Rest@123");
		LoginResponse loginResponse=given()
					            .spec(requestSpec)
					            .body(cred) // Serialization happens here
					        .when()
					            .post("/api/ecom/auth/login")
					        .then()
					        	.spec(responseSpec200)
					            .log().all().extract().response().as(LoginResponse.class);
		token=loginResponse.getToken();
		userId=loginResponse.getUserId();
    }
	
	@Test(priority = 2,dependsOnMethods = "loginTest" )
	void createProduct() {
				
		String addProdResponse=given()
					            .baseUri("https://rahulshettyacademy.com")
					            .header("Authorization", token)
					            .multiPart("productAddedBy", userId)
					            .multiPart("productImage", new File(System.getProperty("user.dir")+"/src/test/resources/testData/sam.png"))           // attach image file
					            .multiPart("productName", "Samsung TV")
					            .multiPart("productCategory", "Electronics")
					            .multiPart("productSubCategory", "Television")
					            .multiPart("productPrice", "800")
					            .multiPart("productDescription", "Samsung Television Description")
					            .multiPart("productFor", "men")
					        .when()
					            .post("/api/ecom/product/add-product")
					        .then()
					        	.spec(responseSpec201)
					            .log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(addProdResponse);
		productId=js.get("productId");
		System.out.println("Product created and its id is: "+productId);
	}
	
	@Test(priority = 3,dependsOnMethods = "createProduct" )
	void placeOrderTest() {
		
		OrderDetails orderDetail =new OrderDetails();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		
		List<OrderDetails> orderDetailList =new ArrayList<OrderDetails>();
		orderDetailList.add(orderDetail);
		Orders order = new Orders();
		order.setOrders(orderDetailList);
		
		
				Response responseAddOrder=given()
							.spec(requestSpec)
							.header("Authorization", token)
							.body(order)
				        .when()
				            .post("/api/ecom/order/create-order")
				        .then()
				        	.spec(responseSpec201)
				            .log().all()
				            .extract().response();
				
				orderId = responseAddOrder.jsonPath().getString("orders[0]");
				System.out.println("Order palced and order id is: "+orderId);
	}
	
	@Test(priority = 4,dependsOnMethods = "placeOrderTest" )
	void viewOrderDetails() {

			String resOrderDetailsBody = given()
					.baseUri("https://rahulshettyacademy.com")
		            .header("Authorization", token)
					.queryParam("id", orderId)
	        .when()
	            .get("/api/ecom/order/get-orders-details")
	        .then()
	            .spec(responseSpec200)
	            .log().ifValidationFails()
	            .extract().asString();
			
			JsonPath js =new JsonPath(resOrderDetailsBody);
			String orderIdFromOrderDetails =js.getString("data._id");
			System.out.println("viewing order detaisl and order id from Response is: "+ orderIdFromOrderDetails);
			Assert.assertEquals(orderIdFromOrderDetails,orderId);
	}


}
