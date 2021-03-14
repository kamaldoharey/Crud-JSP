package com.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.usermanagement.bean.User;

public class UserDao {
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/userdb";
	private String userName = "root";
	private String password = "";
	
	private static final String Insert_Query = "insert into users(name,email,country) values(?,?,?);";
	private static final String Select_By_Id_Query = "select id,name,email,country from users where id =?";
	private static final String Select_All_Query = "select * from users";
	private static final String Delete_Query = "delete from users where id =?;";
	private static final String Update_Query = "update users  set name = ?, email = ? ,country = ? where id =?;";
	
	public UserDao() {
		
	}
	
	protected Connection getConnection() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userName, password);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//Insert Operation
	public void insertQuery(User user) throws SQLException {
		System.out.println(Insert_Query);
		try(Connection con = getConnection();
			 PreparedStatement ps = con.prepareCall(Insert_Query)){
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getCountry());
			System.out.println(ps);
			ps.executeUpdate();
		}
		catch(SQLException e){
			printSQLException(e);
		}
	}
		
	//Select By Id
	public User selectByIdQuery(int id) {
		User user = null;
		try(Connection con = getConnection();
				 PreparedStatement ps = con.prepareCall(Select_By_Id_Query);){
			ps.setInt(1, id);
			System.out.println(ps);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id,name,email,country);
			}
		}
		catch(SQLException e){
			printSQLException(e);
		}
		return user;
	}
	
	//Select All
	public List<User> selectAllQuery(){
		List<User> users = new ArrayList<>();
		try(Connection con = getConnection();
				 PreparedStatement ps = con.prepareCall(Select_All_Query);){
			System.out.println(ps);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id,name,email,country));
			}
		}	
		catch(SQLException e){
			printSQLException(e);
		}
		return users;
	}
		
	//Update Operation
	public boolean updateQuery(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection con = getConnection();
				 PreparedStatement ps = con.prepareCall(Update_Query);){
			System.out.print("Updated User : " + ps);
			ps.setString(1,user.getName());
			ps.setString(2,user.getEmail());
			ps.setString(3,user.getCountry());
			ps.setInt(4,user.getId());
			
			rowUpdated = ps.executeUpdate() > 0;
		}
		return rowUpdated;
	}
	
	//Delete Operation
	public boolean deleteQuery(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection con = getConnection();
				 PreparedStatement ps = con.prepareCall(Delete_Query);){		
			ps.setInt(1,id);			
			rowDeleted = ps.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	private void printSQLException(SQLException ex){
		for(Throwable e : ex) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQL State : " + ((SQLException) e).getSQLState());
				System.err.println("Error Code : " + ((SQLException) e).getErrorCode());
				System.err.println("Message : " + e.getMessage());
				Throwable t = ex.getCause();
				while(t != null) {
					System.out.println("Cause : "+ t);
					t = t.getCause();
				}
			}
		}
	}
}
