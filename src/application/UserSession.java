//UserSession class: represents given session of application use

package application;

public class UserSession {

	//instance variables
	private static UserSession instance;
	private int userID;
	private String userRole;
	private int currentPage;
	
	//constructor
	public UserSession() {
		
	}
	
	//getter methods
	public static UserSession getInstance() {
		if (instance == null) {
			instance = new UserSession();
		}
		return instance;
	}
	
	//setter methods
	public void setUserID(int id) {
		this.userID = id;
	}
	public void setUserRole(String role) {
		this.userRole = role;
	}
	public void setCurrentPage(int n) {
		this.currentPage = n;
	}
	
	//getter methods
	public int getCurrentPage()  {
		return currentPage;
	}
	
	public int getUserID() {
		return userID;
	}
	public String getUserRole() {
		return userRole;
	}
	
}
