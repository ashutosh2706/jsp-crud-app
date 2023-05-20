package app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.model.User;

public class UserDAO {
	
	private final String url = "jdbc:mysql://localhost:3306/crudapp?useSSL=false&allowPublicKeyRetrieval=true";
	private final String username = "root";
	private final String password = "root";
	
	private static final String INSERT_VALUE = "insert into users" + " (name, country, email) values" + " (?, ?, ?)";
	private static final String SELECT_USER_BY_ID = "select * from users where id = ?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USER = "delete from users where id = ?";
	private static final String UPDATE_USER = "update users set name = ?, country = ?, email = ? where id = ?";
	
	
	private Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	public void insertUser(User user) {
		try {
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_VALUE);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getCountry());
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateUser(User user) throws SQLException {
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
		preparedStatement.setString(1, user.getName());
		preparedStatement.setString(2, user.getCountry());
		preparedStatement.setString(3, user.getEmail());
		preparedStatement.setInt(4, user.getId());
		
		return preparedStatement.executeUpdate() > 0;		// executeUpdate() returns the count of updated rows
	}
	
	public User selectUser(int id) {
		User user = null;
		
		try {
			
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				user = new User(id, resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("country"));
			} else throw new RuntimeException("Result Set Null [UserDAO.java : 77]");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public List<User> selectAllUsers() {
		List<User> users = new ArrayList<>();
		
		try {
			
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String country = resultSet.getString("country");
				String email = resultSet.getString("email");
				users.add(new User(id, name, email, country));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public boolean deleteUser(int id) throws SQLException {
		
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER);
		preparedStatement.setInt(1, id);
		return preparedStatement.executeUpdate() > 0;
		
	}
	
}
