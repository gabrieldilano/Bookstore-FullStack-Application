//SQLite Class: provides utility methods for interacting with an SQLite database

package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite {
	public static Connection connect() {
		String url = "jdbc:sqlite:database.db";
    	try {
			Connection con = DriverManager.getConnection(url);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ResultSet sendQuery(Connection con, String query) {
		try {
			Statement stm = con.createStatement();
			ResultSet result = stm.executeQuery(query);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void sendQueryUpdate(Connection con, String query) {
		try {
			Statement stm = con.createStatement();
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void clean(Connection con) {
		
		sendQueryUpdate(con, "VACUUM");
	}
	public static void print(ResultSet s) {
		try {
			ResultSetMetaData md = s.getMetaData();
			int passwordIndex = -1;
			int columnCount = md.getColumnCount();
			for (int i = 1; i <= columnCount; ++i) {
				if (md.getColumnName(i).equals("password")) {
					System.out.printf("%-70s", md.getColumnName(i));
					passwordIndex = i;
					continue;
				}
				System.out.printf("%-20s", md.getColumnName(i));
			}
			System.out.println();
			while (s.next()) {
				for (int i = 1; i <= columnCount; ++i) {
					if (passwordIndex == i) {
						System.out.printf("%-70s", s.getString(i));
						passwordIndex = i;
						continue;
					}
					System.out.printf("%-20s", s.getString(i));
				}
				System.out.println();
			}
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
