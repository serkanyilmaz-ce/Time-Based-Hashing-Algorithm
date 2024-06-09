package net.javaguides.login.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.buf.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import net.javaguides.login.bean.UserLogin;
import net.javaguides.login.database.LoginDao;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginDao loginDao;

	public void init() {
		loginDao = new LoginDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonObject;
		String requestValues = getRequestValues(request);
		UserLogin user = new UserLogin();
		response.setContentType("text/plain");

		try {
			jsonObject = new JSONObject(requestValues);

			String username = jsonObject.getString("username");
			String hashedPassword = jsonObject.getString("hashedPassword");
			String password = jsonObject.getString("password");
			if (jsonObject.has("otpCode")) {
				user.setOtpCode(jsonObject.getString("otpCode"));
			}
			String ipAddress = request.getRemoteAddr();
			user.setUsername(username);
			user.setPassword(password);
			user.setHashedPassword(hashedPassword);
			user.setIpAddress(ipAddress);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			String validateResponse = loginDao.validate(user);
			if (validateResponse == "001") {
				System.out.println("Login Successful");
				response.getWriter().write("001");
			} else if (validateResponse == "002") {
				System.out.println("Network speed is not regular. Please enter OTP code: ");
				response.getWriter().write("002");
			} else if (validateResponse == "003") {
				System.out.println("Incorrect Username or Password");
				response.getWriter().write("003");
			} else if (validateResponse == "004") {
				System.out.println("New IpAddress, Need Register with OTP code.");
				response.getWriter().write("004");
			} else if (validateResponse == "005") {
				System.out.println("Incorrect OTP code, Login Failed");
				response.getWriter().write("005");
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/loginsuccess.jsp");
//		dispatcher.forward(request, response);
	}

	private String getRequestValues(HttpServletRequest request) {
		String req = null;
		try {
			byte[] data = new byte[request.getContentLength()];
			InputStream sis = request.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(sis);
			bis.read(data, 0, data.length);

			if (request.getCharacterEncoding() != null) {
				req = new String(data, request.getCharacterEncoding());
			} else {
				req = new String(data);
			}
		} catch (Exception e) {
			System.out.printf("" + "An error occured while having request values", e);
		}
		return req;
	}
}