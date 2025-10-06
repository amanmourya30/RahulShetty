package pojo;

public class LoginResponse {

	public static String token;
	public static String userId;
	public static String message;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		LoginResponse.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		LoginResponse.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		LoginResponse.message = message;
	}

}
