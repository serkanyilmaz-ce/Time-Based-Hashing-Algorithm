package net.javaguides.login.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import net.javaguides.login.bean.UserLogin;

public class LoginDao {

	public String validate(UserLogin user) throws ClassNotFoundException {

		boolean statusUsernameIpAddressCheck = false;
		boolean statusUsernamePasswordCheck = false;
		boolean statusIpAddressCheck = false;
		boolean statusPasswordContainCheck = false;

		String responseCode = "000";

		Class.forName("com.mysql.jdbc.Driver");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project1?useSSL=false",
				"root", "123456")) {

			if (user.getOtpCode() != null) {
				if (StringUtils.equals(user.getOtpCode(), "1234")) {
					responseCode = "001";
					return responseCode;
				} else {
					responseCode = "005";
					return responseCode;
				}
			}

			if (statusUsernameIpAddressCheck(user, connection)) {

				if (statusUsernamePasswordCheck(user, connection)) {
					responseCode = "001";
				} else if (statusPasswordContainsCheck(user, connection)) {
					responseCode = "002";
				} else {
					responseCode = "003";
				}

			} else if (statusPasswordContainsCheck(user, connection)) {
				// need register for new ip address
				responseCode = "004";
			} else {
				responseCode = "003";
			}

		} catch (SQLException e) {
			printSQLException(e);
		}

		return responseCode;
	}

	private boolean statusUsernameIpAddressCheck(UserLogin user, Connection connection) throws SQLException {

		boolean statusUsernameIpAddressCheckValue = false;
		PreparedStatement preparedStatementforIpAddress = connection
				.prepareStatement("select * from user_register where username = ? and ip_Address = ? ");
		preparedStatementforIpAddress.setString(1, user.getUsername());
		preparedStatementforIpAddress.setString(2, user.getIpAddress());
		ResultSet rs1 = preparedStatementforIpAddress.executeQuery();
		statusUsernameIpAddressCheckValue = rs1.next();

		return statusUsernameIpAddressCheckValue;
	}

	private boolean statusUsernamePasswordCheck(UserLogin user, Connection connection) throws SQLException {

		boolean statusUsernamePasswordCheck = false;
		PreparedStatement preparedStatementforLogin = connection
				.prepareStatement("select * from user_login where username = ? and password = ? ");
		preparedStatementforLogin.setString(1, user.getUsername());
		preparedStatementforLogin.setString(2, user.getHashedPassword());

		System.out.println(preparedStatementforLogin);
		ResultSet rs3 = preparedStatementforLogin.executeQuery();
		statusUsernamePasswordCheck = rs3.next();

		return statusUsernamePasswordCheck;
	}

	private boolean statusPasswordContainsCheck(UserLogin user, Connection connection) throws SQLException {

		boolean statusPasswordContainCheck = false;
		PreparedStatement preparedStatementforPasswordContainCheck = connection
				.prepareStatement("select * from user where username = ? and password = ? ");
		preparedStatementforPasswordContainCheck.setString(1, user.getUsername());
		preparedStatementforPasswordContainCheck.setString(2, user.getPassword());

		System.out.println(preparedStatementforPasswordContainCheck);
		ResultSet rs5 = preparedStatementforPasswordContainCheck.executeQuery();
		statusPasswordContainCheck = rs5.next();

		return statusPasswordContainCheck;
	}

	private boolean statusIpAddressCheck(UserLogin user, Connection connection) throws SQLException {

		return false;
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}

}
