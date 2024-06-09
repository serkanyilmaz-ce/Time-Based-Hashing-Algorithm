package net.javaguides.registration.dao;

import net.javaguides.registration.model.Users;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;

public class UsersDao {

	public int registerUsers(Users user) throws ClassNotFoundException {

		String INSERT_USER_LOGIN_SQL = "INSERT INTO user_login" + "  ( username, password, ip_address) VALUES "
				+ " ( ?, ?, ?);";

		String INSERT_USER_REGISTER_SQL = "INSERT INTO user_register" + "  ( username, ip_address) VALUES "
				+ " (?, ?);";

		int result = 0;

		Class.forName("com.mysql.jdbc.Driver");

		if (StringUtils.equals(user.getOtpCode(), "1234")) {

			try (Connection connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/project1?useSSL=false", "root", "123456")) {

				PreparedStatement preparedStatementRegisterTable = connection
						.prepareStatement(INSERT_USER_REGISTER_SQL);
//				preparedStatementRegisterTable.setInt(1, user.getId());
				preparedStatementRegisterTable.setString(1, user.getUsername());
				preparedStatementRegisterTable.setString(2, user.getIpAddress());

				System.out.println(preparedStatementRegisterTable);

				result = preparedStatementRegisterTable.executeUpdate();

				PreparedStatement preparedStatementLoginTable = connection.prepareStatement(INSERT_USER_LOGIN_SQL);
//				preparedStatementLoginTable.setInt(1, user.getId());
				preparedStatementLoginTable.setString(1, user.getUsername());
				preparedStatementLoginTable.setString(2, user.getHashedPassword());
				preparedStatementLoginTable.setString(3, user.getIpAddress());

				System.out.println(preparedStatementLoginTable);

				result = preparedStatementLoginTable.executeUpdate();

			} catch (SQLException e) {
				printSQLException(e);

			}

		} else {
			return result;
		}

		return result;
	}

	public int loginUsers(Users user) {

		return 1;
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
