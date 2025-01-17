//Account_Management Class: handles account creation and login functionality

package account;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

import sqlite.SQLite;
import application.UserSession;



public class Account_Management {	

	//creates a new user account with a username and hashed password
	public static void createAccount(String username, String password) throws Exception {
		if (checkValidUsername(username) == false) {
			//System.out.println("Duplicated user");
			throw new Exception("Duplicated user"); // Throw exception if duplicate
		}

		// Hash the password using SHA-256
		byte[] passwordSha = getSHA(password);
		String passwordHex = bytesToHex(passwordSha);

		 // SQL query to insert the new account into the users table
		String query = "INSERT INTO users(username, password, role) VALUES (?, ?, ?);";
		Connection con = SQLite.connect();
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, username);
		ps.setString(2, passwordHex);
		ps.setString(3, "buyer");
		ps.executeUpdate();
		con.close();
	}
	
	//authenticates a user based on their username and password
	public static boolean login(String username, String password) {
		try {
			// Hash the input password using SHA-256
			byte[] passwordSha = getSHA(password);
		
			String passwordHex = bytesToHex(passwordSha);
			
			// SQL query to verify username and password
			Connection con = SQLite.connect();
			String query = """
						SELECT user_ID, role FROM users WHERE username = ? AND password = ?;
					""";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, passwordHex);
			ResultSet result = ps.executeQuery();
			
			if (result.next()) { // Store user data in the UserSession singleton if user exists
				UserSession.getInstance().setUserID(result.getInt("user_ID"));
				UserSession.getInstance().setUserRole(result.getString("role"));
				return true;
			}
			con.close(); // Close the database connection
			return false;
		} catch (NoSuchAlgorithmException| SQLException e) { //error handling
			e.printStackTrace();
			return false; // Return false if an error occurs
		}
	}


	//verifies if the given username is available
	private static boolean checkValidUsername(String username) {
        try {
			 // SQL query to check for username existence
        	Connection con = SQLite.connect();
        	String query = """
        			SELECT 1 FROM users WHERE username = ? LIMIT 1;
        			""";
        	ResultSet result = SQLite.sendQuery(con, query);
        	con.close(); // Close the database connection
        	
        	if (result.next()) { // If a result is found, username is taken
        		return false;
        	}
        	return true; // Username is available
        	
        } catch (SQLException e) { //error handling
        	e.printStackTrace();
        	return false; // Return false if an error occurs
        }
        
	}
	
	
	//generates a SHA-256 hash of the given input string
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
		MessageDigest digest =  MessageDigest.getInstance("SHA-256"); //gets SHA-256 algorithm
		return digest.digest(input.getBytes(StandardCharsets.UTF_8)); //Hash the input
	}
	
	//converts a byte array into a hexadecimal string
	public static String bytesToHex(byte[] sha) {
		String hex = "";
		for (int i = 0; i < sha.length; ++i) { // Convert each byte to a hex value
			hex += String.format("%02X", sha[i]);
		}
		return hex;
	}
}
