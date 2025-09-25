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

}
