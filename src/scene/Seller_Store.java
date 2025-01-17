//Seller_Store: Seller View for store where sellers can manage inventory

package scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Item;
import application.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sqlite.SQLite;

public class Seller_Store extends Store {
	private static Store instance;
	public Seller_Store() {
		
	}
	
	//returns singleton instance
	public static Store getInstance() {
		if (instance == null) {
			instance = new Seller_Store();
		}
		return instance;
	}
	
	//returns interface
	public Scene createScene(Stage primaryStage) {
		HBox h = new HBox();
		Text t = new Text("Congratulations!");
		
		h.setAlignment(Pos.CENTER);
		h.setPrefWidth(1200);
		h.setPrefHeight(1100);
		h.getChildren().add(t);
	//HBox.setHgrow(h, Priority.ALWAYS);
		
		return new Scene(h);
		
	}
	
	//adds item to database
	public void addItem(String item_name, String item_condition, String item_category, double price, double discounted_price) {
		try {
			Connection con = SQLite.connect();
			String query = "INSERT INTO store_items(item_name, item_condition, item_category, price, discounted_price) VALUES(?, ?, ?, ?, ?);";
			PreparedStatement pstm = con.prepareStatement(query);
			pstm.setString(1, item_name);
			pstm.setString(2, item_condition);
			pstm.setString(3, item_category);
			pstm.setDouble(4, price);
			pstm.setDouble(5, discounted_price);
			pstm.executeUpdate();
			ResultSet result = pstm.getGeneratedKeys();
			if (result.next()) {
				int item_ID = result.getInt(1);
				query = "INSERT INTO store(seller_ID, item_ID) VALUES(?, ?);";
				pstm = con.prepareStatement(query);
				pstm.setInt(1, UserSession.getInstance().getUserID());
				pstm.setInt(2, item_ID);
				pstm.executeUpdate();			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//refreshes display of items
	public static void updateListItems(String categ, String cond) {
		try {
			Connection con = SQLite.connect();
			String query = "";
			PreparedStatement pstm = null;
			if (categ.equals("Any") && cond.equals("Any")) {
				query = """
						SELECT * FROM store_items WHERE user_ID = ?;
						""";
				pstm = con.prepareStatement(query);
				pstm.setInt(1, UserSession.getInstance().getUserID());
			} else if(categ.equals("Any") && !cond.equals("Any")) {
				query = """
						SELECT * FROM store_items
						WHERE item_condition = ? AND user_ID = ?;
						""";
				
				pstm = con.prepareStatement(query);
				pstm.setString(1, cond);
				pstm.setInt(2, UserSession.getInstance().getUserID());

			} else if (!categ.equals("Any") && cond.equals("Any")) {
				query = """
						SELECT * FROM store_items
						WHERE item_category = ? AND user_ID = ?;
						""";
				pstm = con.prepareStatement(query);
				pstm.setString(1, categ);
				pstm.setInt(2, UserSession.getInstance().getUserID());
			}
			else {
				query = """
						SELECT * FROM store_items
						WHERE item_category = ? AND item_condition = ? AND user_ID = ?;
						""";
				
				pstm = con.prepareStatement(query);
				pstm.setString(1, categ);
				pstm.setString(2, cond);
				pstm.setInt(3, UserSession.getInstance().getUserID());

			}
			
			ResultSet result = pstm.executeQuery();
			Item.getList().clear();
			while (result.next()) {
				int itemID = result.getInt("item_ID");
				String itemName = result.getString("item_name");
				
				String itemCondition = result.getString("item_condition");
				String itemCategory = result.getString("item_category");
				double itemPrice = result.getDouble("price");
				double itemDiscountedPrice = result.getDouble("discounted_price");
				Item item = new Item(itemID, itemName, itemCondition, itemCategory, itemPrice, itemDiscountedPrice);
				Item.getList().add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}		
