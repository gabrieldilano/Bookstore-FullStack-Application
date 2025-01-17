//Item Class: represents individual item with given information

package application;

import java.util.ArrayList;

public class Item {

	//instance variables
	private static ArrayList<Item> list = new ArrayList<Item>();
	private int itemID;
	private String itemName;
	private String itemCondition;
	private String itemCategory;
	private double itemPrice;
	private double itemDiscountedPrice;

	//constructor
	public Item(int id, String name, String condition, String category, double price, double discountedPrice) {
		this.itemID = id;
		this.itemName = name;
		this.itemCondition = condition;
		this.itemCategory = category;
		this.itemPrice = price;
		this.itemDiscountedPrice = discountedPrice;
	}

	//getter methods
	public int getID() {
		return itemID;
	}
	public String getName() {
		return itemName;
	}
	public String getCondition() {
		return itemCondition;
	}
	public String getCategory() {
		return itemCategory;
	}
	public double getPrice() {
		return itemPrice;
	}
	public double getDiscountedPrice() {
		return itemDiscountedPrice;
	}
	
	public static ArrayList<Item> getList() {
		return list;
	}

	//destructor for list
	public static void clear() {
		list.clear();
	}
}

