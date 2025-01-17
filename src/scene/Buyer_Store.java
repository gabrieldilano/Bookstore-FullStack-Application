//Buyer_Store: Buyer view where buyers can view avaialable and purchase items 

package scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
	
import application.Item;
import application.SceneStack;
import application.UserSession;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sqlite.SQLite;

public class Buyer_Store extends Store {

	//instance variables
	private static String category = "Any";
	private static String condition = "Any";
	private static int page = 1;
    private static int numberOfPages = 0;
    private static Store instance;
    private Text previousBtnClicked = null;
    
    private Buyer_Store() {
    	
    }
    
	//getter for singleton instance
    public static Store getInstance() {
    	if (instance == null) {
    		instance = new Buyer_Store();
    	}
    	return instance;
    }
    
	//returns interface
	public Scene createScene(Stage primaryStage) {
		
		storeItems(category, condition);
		numberOfPages = Math.ceilDiv(Item.getList().size(), 5);
		UserSession.getInstance().setCurrentPage(1);

		
		//############ LAYOUT ############
		StackPane outerContainer = new StackPane();
			Button swapRoleBtn = new Button("Swap to seller");
			VBox container = new VBox();
			
				StackPane topSection = new StackPane();
					Rectangle rect = new Rectangle(400,100);
					Text title = new Text("Sun Devil Book");
			
				HBox middleSection = new HBox();
					VBox categorySection = new VBox();
						Text cateTitle = new Text("Categories");
						ComboBox<String> cb = new ComboBox<String>();
					VBox conditionSection = new VBox();
					Text condTitle = new Text("Conditions");
						ComboBox<String> cb2 = new ComboBox<String>();
				
				GridPane bottomSection = itemsSectionGenerate(page, 5);
				HBox pagesTransition = pagesTransitionCreate(container, numberOfPages);
		//************ LAYOUT ************
		
			
		//############ MARK-UP ############
		swapRoleBtn.setPrefWidth(100);
		swapRoleBtn.setPrefHeight(40);
//		swapRoleBtn.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		StackPane.setAlignment(swapRoleBtn, Pos.TOP_RIGHT);
		StackPane.setMargin(swapRoleBtn, new Insets(20));
			
		container.setPadding(new Insets(30,100,30,100));
		primaryStage.setMinWidth(1200);
		primaryStage.setMinHeight(1000);
		VBox.setVgrow(bottomSection, Priority.ALWAYS); //NEED TO COMMENT THIS OUT
		VBox.setMargin(bottomSection, new Insets(50, 0, 30, 0));
				rect.setFill(Color.rgb(242, 201, 71));
				rect.setArcWidth(20);
				rect.setArcHeight(20);
				title.setFill(Color.BLACK);  
			    title.setFont(Font.font("Open Sans", FontWeight.BOLD, 46));// Set text color
			    title.setStrokeWidth(40);
			    
			middleSection.setSpacing(20);
			
			    cateTitle.setFont(Font.font("Open Sans", FontWeight.BOLD, 18));
			    cb.getItems().addAll("Any", "Sci-Fi", "Math");
			    cb.setValue("Any");
			    cb.setPrefWidth(150);
			    
			    condTitle.setFont(Font.font("Open Sans", FontWeight.BOLD, 18));
			    cb2.getItems().addAll("Any", "Used Like New", "Moderately Used", "Heavily Used");
			    cb2.setValue("Any");
			    cb2.setPrefWidth(150);
			bottomSection.setMinHeight(100);
			HBox.setHgrow(bottomSection, Priority.ALWAYS);
		//************ MARK-UP ************
			
				
		//############ COMBINE COMPONENTS ############
		outerContainer.getChildren().addAll(container, swapRoleBtn);

		container.getChildren().addAll(topSection, middleSection, bottomSection, pagesTransition);
			topSection.getChildren().addAll(rect, title);
			middleSection.getChildren().addAll(categorySection, conditionSection);
				categorySection.getChildren().addAll(cateTitle, cb);
				conditionSection.getChildren().addAll(condTitle, cb2);
		//************ COMBINE COMPONENTS ************
				
		//############ FUNCTIONS ############
		cb.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				category = cb.getValue();
				storeItems(category, condition);
				numberOfPages = Math.ceilDiv(Item.getList().size(), 5);

				int bottomSectionIndex = container.getChildren().size()-2;
				container.getChildren().remove(bottomSectionIndex, bottomSectionIndex+2);
				GridPane bottomSection = itemsSectionGenerate(1, 5); //RESET to PAGE 1 with maximum of 5 items
				HBox.setHgrow(bottomSection, Priority.ALWAYS);
				VBox.setVgrow(bottomSection, Priority.ALWAYS); 
				HBox pagesTransition = pagesTransitionCreate(container, numberOfPages);
				container.getChildren().addAll(bottomSection, pagesTransition);
			}
		});
		cb2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				condition = cb2.getValue();
				storeItems(category, condition);
				numberOfPages = Math.ceilDiv(Item.getList().size(), 5);

				int bottomSectionIndex = container.getChildren().size()-2;
				container.getChildren().remove(bottomSectionIndex, bottomSectionIndex+2);

				GridPane bottomSection = itemsSectionGenerate(1, 5);
				HBox.setHgrow(bottomSection, Priority.ALWAYS);
				VBox.setVgrow(bottomSection, Priority.ALWAYS); 
				HBox pagesTransition = pagesTransitionCreate(container, numberOfPages);

				container.getChildren().addAll(bottomSection, pagesTransition);
						
			}
		});
		
		swapRoleBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				 SceneStack.getInstance().push(Seller_Store.getInstance().createScene(primaryStage));			        
			     primaryStage.setScene(SceneStack.getInstance().peek());
						
			}
		});
		return new Scene(outerContainer);
	}
	
	
	private GridPane itemsSectionGenerate(int page, int maxNumberOfItem) {
		ArrayList<Item> list = Item.getList();
		
		GridPane itemsLayout = new GridPane();
		
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setPercentWidth(55);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(25);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(20);
	    itemsLayout.getColumnConstraints().addAll(col0, col1,col2);
	    int numberOfPages = Math.ceilDiv(list.size(),maxNumberOfItem);
	    int lastPageItemsNumb = list.size() - 5*Math.floorDiv(list.size(),5);
	    int pageItemsNumb = page >= numberOfPages ? lastPageItemsNumb : 5;
	    
	    for (int i = 0; i < pageItemsNumb; ++i) {
	    	RowConstraints row = new RowConstraints();
		    row.setPercentHeight(20);
		    itemsLayout.getRowConstraints().add(row);
	    }
	    
		for (int i = 0; i < pageItemsNumb; ++i) {
			int calculatedIndex = i + (page-1)*maxNumberOfItem;
			Item currentItem = list.get(calculatedIndex);
			String title = currentItem.getName();
			String cat = currentItem.getCategory();
			String cond = currentItem.getCondition();
			double price = currentItem.getPrice();
			double discountedPrice = currentItem.getDiscountedPrice();
			
			//Set up row i in GridPane itemsLayout
			setUpRow(itemsLayout, i, title, cat, cond, price, discountedPrice);
		}
		
		return itemsLayout;
	}
	
	private void setUpRow(GridPane itemsLayout, int i, String title, String cat, String cond, double price, double discountedPrice) {
		int topBorderWidth = i == 0 ? 2 : 0;

		//LAYOUT
		VBox leftSection = new VBox();
			Text titleInp = new Text(title);
			HBox bookAbout = new HBox();
				HBox categorySection = new HBox();
					Text category = new Text("Category: ");
					Text categoryInp = new Text(cat);
				HBox conditionSection = new HBox();
					Text condition = new Text("Condition: ");
					Text conditionInp = new Text(cond);
		VBox middleSection = new VBox();
			HBox oldSection = new HBox();
				Text oldPriceText = new Text("Price: ");
				Text oldPriceTextInp = new Text("$" + price);
			HBox nowSection = new HBox();
				Text nowPriceText = new Text("Now: ");
				Text nowPriceTextInp = new Text("$" + discountedPrice);
				
		VBox rightSection = new VBox();
			StackPane purchaseBtn = new StackPane();
				Rectangle rect1 = new Rectangle(120, 35);
				
				Button btn1 = new Button("Purchase");
			StackPane addToCartBtn = new StackPane();
				Rectangle rect2 = new Rectangle(170, 35);
				Button btn2 = new Button("Add to cart");
		//**** MARK UP ****
		HBox.setHgrow(leftSection, Priority.SOMETIMES);
		HBox.setHgrow(middleSection, Priority.SOMETIMES);
		HBox.setHgrow(rightSection, Priority.SOMETIMES);
			leftSection.setAlignment(Pos.CENTER_LEFT);
//			leftSection.setMaxWidth(500);
//			leftSection.setMinWidth(500);
			leftSection.setSpacing(20);
			leftSection.setPadding(new Insets(0,0,0,50));
			leftSection.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(topBorderWidth, 0, 2, 2))
			));
				titleInp.setFont(Font.font("Open Sans", FontWeight.BOLD, 25));
			
//			leftSection.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
			oldPriceTextInp.setStrikethrough(true);
			middleSection.setAlignment(Pos.CENTER);
			middleSection.setSpacing(5);
			
			middleSection.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(topBorderWidth, 0, 2, 0))
			));
			
			
			rightSection.setAlignment(Pos.CENTER_RIGHT);
			rightSection.setSpacing(15);
			rightSection.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(topBorderWidth, 2, 2, 0))
			));
					rect1.setFill(Color.rgb(211, 230, 96));
					rect1.setArcWidth(12);
					rect1.setArcHeight(12);
					rect1.setStroke(Color.BLACK);
					btn1.setBackground(null);
					btn1.setFont(Font.font("Open Sans", 22));
					
					rect2.setFill(Color.rgb(211, 230, 96));
					rect2.setArcWidth(12);
					rect2.setArcHeight(12);
					rect2.setStroke(Color.BLACK);
					btn2.setBackground(null);
					btn2.setFont(Font.font("Open Sans", 22));
			
		//**** COMBINE TOGETHER ****
		
			itemsLayout.add(leftSection, 0, i);
				leftSection.getChildren().addAll(titleInp, bookAbout);
					bookAbout.getChildren().addAll(categorySection, conditionSection);
						categorySection.getChildren().addAll(category, categoryInp);
						conditionSection.getChildren().addAll(condition, conditionInp);
			itemsLayout.add(middleSection, 1, i);
				middleSection.getChildren().addAll(oldSection, nowSection);
					oldSection.getChildren().addAll(oldPriceText, oldPriceTextInp);
					nowSection.getChildren().addAll(nowPriceText, nowPriceTextInp);
			itemsLayout.add(rightSection, 2, i);
				rightSection.getChildren().addAll(purchaseBtn, addToCartBtn);
					purchaseBtn.getChildren().addAll(rect1, btn1);
					addToCartBtn.getChildren().addAll(rect2, btn2);	
	}
	
	
	//Get items from database and add it into list
	private void storeItems(String categ, String cond) {
		try {
			Connection con = SQLite.connect();
			String query = "";
			PreparedStatement pstm = null;
			if (categ.equals("Any") && cond.equals("Any")) {
				query = """
						SELECT * FROM store_items;
						""";
				pstm = con.prepareStatement(query);
			} else if(categ.equals("Any") && !cond.equals("Any")) {
				query = """
						SELECT * FROM store_items
						WHERE item_condition = ?;
						""";
				
				pstm = con.prepareStatement(query);
				pstm.setString(1, cond);
			} else if (!categ.equals("Any") && cond.equals("Any")) {
				query = """
						SELECT * FROM store_items
						WHERE item_category = ?;
						""";
				pstm = con.prepareStatement(query);
				pstm.setString(1, categ);

			}
			else {
				query = """
						SELECT * FROM store_items
						WHERE item_category = ? AND item_condition = ?;
						""";
				
				pstm = con.prepareStatement(query);
				pstm.setString(1, categ);
				pstm.setString(2, cond);
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
	private HBox pagesTransitionCreate(VBox container, int pageNum) {	
		HBox result = new HBox();
		for (int i = 0; i < pageNum; ++i) {
			
			Text currentNum = new Text((i+1) + "");	
			currentNum.setFont(Font.font("Open Sans", FontWeight.EXTRA_BOLD, 16));
			if (i == 0) {
				currentNum.setScaleX(1.5);
				currentNum.setScaleY(1.5);
				currentNum.setFill(Color.BLUE);
				System.out.println("Checkpoint 1");
				previousBtnClicked = currentNum;
			}
			
			currentNum.addEventHandler(MouseEvent.MOUSE_CLICKED, 
					new EventHandler<MouseEvent>() {
						public void handle(MouseEvent event) {
							//check for same button clicked 		
							if (previousBtnClicked.getText().equals(currentNum.getText())) {
								return;
							}
							previousBtnClicked.setScaleX(1);
							previousBtnClicked.setScaleY(1);
							previousBtnClicked.setFill(Color.BLACK);
							previousBtnClicked = currentNum;	
							int pageClicked = Integer.valueOf(((Text)event.getSource()).getText());
							System.out.println("CLicked: " + pageClicked);
							page = pageClicked;
							int bottomSectionIndex = container.getChildren().size()-2;
							container.getChildren().remove(bottomSectionIndex);
							GridPane bottomSection = itemsSectionGenerate(page, 5); //RESET to PAGE 1 with maximum of 5 items
							HBox.setHgrow(bottomSection, Priority.ALWAYS);
							VBox.setVgrow(bottomSection, Priority.ALWAYS); 
							container.getChildren().add(bottomSectionIndex, bottomSection);
							currentNum.setScaleX(1.5);
							currentNum.setScaleY(1.5);
							currentNum.setFill(Color.BLUE);
							UserSession.getInstance().setCurrentPage(pageClicked);
						
						}
					}
			);
			currentNum.addEventHandler(MouseEvent.MOUSE_ENTERED, 
					new EventHandler<MouseEvent>() {
						public void handle(MouseEvent event) {
							currentNum.setScaleX(1.5);
							currentNum.setScaleY(1.5);
							currentNum.setFill(Color.BLUE);
						}
					}
			);
			currentNum.addEventHandler(MouseEvent.MOUSE_EXITED, 
					new EventHandler<MouseEvent>() {
						public void handle(MouseEvent event) {
							System.out.printf("CurrentNum: %s, userNum: %s\n", currentNum.getText(), UserSession.getInstance().getCurrentPage()+"");
							if (currentNum.getText().equals(UserSession.getInstance().getCurrentPage()+"")) {
								
								return;
							}
							currentNum.setScaleX(1);
							currentNum.setScaleY(1);
							currentNum.setFill(Color.BLACK);
						}
					}
			);
			
			result.getChildren().add(currentNum);
			
			
		}
		result.setSpacing(16);
		result.setAlignment(Pos.CENTER);
		return result;
	}
	
}
