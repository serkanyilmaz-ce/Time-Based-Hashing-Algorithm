package net.javaguides.registration.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import net.javaguides.registration.dao.UsersDao;
import net.javaguides.registration.model.Users;

/**
 * Servlet implementation class UsersServlet
 */
@WebServlet("/register")
public class UsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UsersDao usersDao = new UsersDao();

	public void init() {
		usersDao = new UsersDao();
	}

	public UsersServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/userregister.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestValues = getRequestValues(request);
		System.out.printf("request values: ", requestValues);
		Users users = new Users();
		response.setContentType("text/plain");
		String ipAddress;
		

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(requestValues);
			String otpCode = jsonObject.getString("otpCode");
			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");
			String hashedPassword = jsonObject.getString("hashedPassword");
			ipAddress = request.getRemoteAddr();
			users.setOtpCode(otpCode);
			users.setUsername(username);
			users.setPassword(password);
			users.setIpAddress(ipAddress);
			users.setHashedPassword(hashedPassword);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			int isOk = usersDao.registerUsers(users);
			if (isOk == 1) {
				response.getWriter().write("101");
			} else {
				response.getWriter().write("102");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
