package api.files;

public class payload {
	
	public static String getPayload() {
		return "{\r\n"
				+ "  \"id\": \"2\",\r\n"
				+ "  \"email\": \"jane.doe@test.io\",\r\n"
				+ "  \"first_name\": \"Jane\",\r\n"
				+ "  \"last_name\": \"Doe\",\r\n"
				+ "  \"is_active\": true,\r\n"
				+ "  \"address\": {\r\n"
				+ "    \"street\": \"101 Test Avenue\",\r\n"
				+ "    \"city\": \"QA City\",\r\n"
				+ "    \"zipcode\": \"33445\"\r\n"
				+ "  },\r\n"
				+ "  \"skills\": [\"Cypress\", \"JavaScript\", \"API Testing\"]\r\n"
				+ "}";
	}
	
	public static String getUpdatedPayload() {
		return "{\r\n"
				+ "  \"id\": 2,\r\n"
				+ "  \"email\": \"jane.doe@test.io\",\r\n"
				+ "  \"first_name\": \"Jane Updated\",\r\n"
				+ "  \"last_name\": \"Doe\",\r\n"
				+ "  \"is_active\": false,\r\n"
				+ "  \"address\": {\r\n"
				+ "    \"street\": \"202 QA Boulevard\",\r\n"
				+ "    \"city\": \"Testville\",\r\n"
				+ "    \"zipcode\": \"33445\"\r\n"
				+ "  },\r\n"
				+ "  \"skills\": [\"Cypress\", \"JavaScript\", \"API Testing\", \"Postman\"]\r\n"
				+ "}";
	}
	
public static String coursesMockResponse() {
		return "{\r\n"
				+ "  \"dashboard\": {\r\n"
				+ "    \"purchaseAmount\": 910,\r\n"
				+ "    \"website\": \"rahulshettyacademy.com\"\r\n"
				+ "  },\r\n"
				+ "  \"courses\": [\r\n"
				+ "    {\r\n"
				+ "      \"title\": \"Selenium Python\",\r\n"
				+ "      \"price\": 50,\r\n"
				+ "      \"copies\": 6\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"title\": \"Cypress\",\r\n"
				+ "      \"price\": 40,\r\n"
				+ "      \"copies\": 4\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "      \"title\": \"RPA\",\r\n"
				+ "      \"price\": 45,\r\n"
				+ "      \"copies\": 10\r\n"
				+ "    }\r\n"
				+ "  ]\r\n"
				+ "}";
	}

}
