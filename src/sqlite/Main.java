//Main Class: used for testing SQLite database operations such as table creation and querying

package sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		try {
			Connection con = SQLite.connect();
			String query1 =	"""
					CREATE TABLE users (
					            user_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
					            username VARCHAR(30),
					            password VARCHAR(64),
	                role VARCHAR(15)
	            );
	            """;
			String query2 = """
					SELECT * from users;
				""";
			String query3 = """
							DELETE FROM users;
					""";
			String query4 = """
					CREATE TABLE store_items (
						item_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
						item_name VARCHAR(150),
						item_condition VARCHAR(50),
						item_category VARCHAR(30),
						price DECIMAL(6, 2),
						discounted_price DECIMAL(6,2)
					);
					""";
			String query5 = """
					INSERT INTO store_items(item_name, item_condition, item_category, price, discounted_price) VALUES(
						'BookC', 'Heavily Used', 'Math', '78.99', '23.33'
					);
					""";
			String query6 = """
					CREATE TABLE store (
						seller_ID INTEGER NOT NULL,
						item_ID  INTEGER NOT NULL
					);
					""";
			//SQLite.sendQueryUpdate(con, query6);
			//ResultSet result = SQLite.sendQuery(con, query2);
			//SQLite.print(result);
			//SQLite.clean(con);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
