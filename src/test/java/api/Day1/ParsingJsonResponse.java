package api.Day1;

import org.testng.Assert;

import api.files.payload;
import io.restassured.path.json.JsonPath;

public class ParsingJsonResponse {

	public static void main(String[] args) {
		JsonPath js = new JsonPath(payload.coursesMockResponse());
		
//		1. Print No of courses returned by API
		
		int count = js.get("courses.size()");
		System.out.println(count);
		
//		2.Print Purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);

//		3. Print Title of the first course
		String firstTitle = js.getString("courses[0].title");
		System.out.println("First course title is: "+firstTitle);
		
//		4. Print All course titles and their respective Prices
		
		for(int i=0; i<count;i++) {
			String courseTitle = js.get("courses["+i+"].title");
			System.out.println(courseTitle);
			String coursePrice = js.get("courses["+i+"].price").toString();
			System.out.println(coursePrice);
		}

//		5. Print no of copies sold by RPA Course
		
		for(int i=0; i<count;i++) {
			String courseTitle = js.get("courses["+i+"].title");
			if(courseTitle.equalsIgnoreCase("RPA")) {
				System.out.println("RPA course copies sold: "+js.get("courses["+i+"].copies").toString());
				break;
			}
		}
		
		

//		6. Verify if Sum of all Course prices matches with Purchase Amount
		int resTotal=0;
		for(int i=0; i<count;i++) {
			int price = js.get("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			resTotal = resTotal+(price*copies);
		}
		System.out.println("response amount found is: "+resTotal);
		if(resTotal==js.getInt("dashboard.purchaseAmount")) {
			System.out.println("Both amount matches");
		}
		Assert.assertEquals(resTotal, 910);
		
	}

}
