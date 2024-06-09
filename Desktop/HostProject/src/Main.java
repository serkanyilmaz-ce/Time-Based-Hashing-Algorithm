import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
	Double averagePingTime = PingOperations.averagePingTime("localhost");
	averagePingTime = averagePingTime - (averagePingTime % 10);

	JSONObject userLoginObject = new JSONObject();

	Scanner scannerUsername = new Scanner(System.in);
	System.out.println("Username: ");
	String username = scannerUsername.nextLine();

	userLoginObject.put("username", username);

	Scanner scannerPassword = new Scanner(System.in);
	System.out.println("Password: ");
	String password = scannerPassword.nextLine();

	userLoginObject.put("password", password);

	String hashedPassword = DataHash.passwordHash(password, averagePingTime);

	userLoginObject.put("hashedPassword", hashedPassword);

	String responseLogin = PostOperations.postTransaction(userLoginObject.toString(),
		"http://localhost:8082/ClientServerWeb/login");

	responseLogin = responseLogin.substring(0, responseLogin.length() - 1);

	if (StringUtils.equals(responseLogin, "001")) {
	    System.out.println("Login Successful");
	} else if (StringUtils.equals(responseLogin, "002")) {
	    Scanner scannerOTP = new Scanner(System.in);
	    System.out.println("Your network speed is not regular. Please enter OTP code: ");
	    String otpCode = scannerOTP.nextLine();
	    userLoginObject.put("otpCode", otpCode);
	    String responseLoginAgain = PostOperations.postTransaction(userLoginObject.toString(),
		    "http://localhost:8082/ClientServerWeb/login");

	    responseLoginAgain = responseLoginAgain.substring(0, responseLoginAgain.length() - 1);
	    if (StringUtils.equals(responseLoginAgain, "001")) {
		System.out.println("Login Successful");
	    } else {
		System.out.println("System not allow to login now, try again later");
	    }
	} else if (StringUtils.equals(responseLogin, "003")) {
	    System.out.println("Incorrect Username or Password");
	} else if (StringUtils.equals(responseLogin, "004")) {
	    Scanner scannerOTP = new Scanner(System.in);
	    System.out.println("Enter OTP Code");
	    String otpCode = scannerOTP.nextLine();
	    userLoginObject.put("otpCode", otpCode);
	    String responseOTP = PostOperations.postTransaction(userLoginObject.toString(),
		    "http://localhost:8082/ClientServerWeb/register");
	    responseOTP = responseOTP.substring(0, responseOTP.length() - 1);
	    if (StringUtils.equals(responseOTP, "101")) {
		String responseLoginAgain = PostOperations.postTransaction(userLoginObject.toString(),
			"http://localhost:8082/ClientServerWeb/login");

		responseLoginAgain = responseLoginAgain.substring(0, responseLoginAgain.length() - 1);
		if (StringUtils.equals(responseLoginAgain, "001")) {
		    System.out.println("Login Successful");
		} else {
		    System.out.println("System not allow to login now, try again later");
		}
	    }

	}
    }

}
