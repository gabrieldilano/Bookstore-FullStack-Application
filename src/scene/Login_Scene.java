//Login_Scene: User interface for login screen, serves as entry to application flow
package scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import account.Account_Management;
import application.SceneStack;

public class Login_Scene {

	//font styling
	static Font FONT_1 = Font.font("Open Sans", FontWeight.EXTRA_BOLD, 32);
	static Font LABEL_FONT = Font.font("Open Sans", FontWeight.BOLD, 20);
	static Font LABEL_FONT_HOLD = Font.font("Open Sans", FontWeight.BOLD, 23);
	private static Login_Scene instance;
	
	private Login_Scene() {}

	//returns singleton instance
	public static Login_Scene getInstance() {
		if (instance == null) {
			instance = new Login_Scene();
		}
		return instance;
	}
	
	//returns interface
	public static Scene createScene(Stage primaryStage) {
		
		
		//### OVERALL LAYER ####
		VBox loginScene = new VBox();
			StackPane titleLayout = new StackPane();
				Rectangle titleRect = new Rectangle(300, 70);
				Text titleText = new Text("Sun Devil Book");
			GridPane middleLayout = new GridPane();
				VBox accountForm = new VBox();
					Label userLabel = new Label("Username");
					TextField userTf = new TextField();
					Label passwordLabel = new Label("Password");
					PasswordField passwordTf = new PasswordField();
				StackPane loginBtn = new StackPane();
					Rectangle loginBtnShape = new Rectangle(100, 37);
					Button btn1 = new Button("Sign in");
				VBox signUpRight = new VBox();
					StackPane signupBtn = new StackPane();
						Rectangle signupBtnShape = new Rectangle(100, 37);
						Button btn2 = new Button("Sign up");
				Text notifyText = new Text();
		//****** OVERALL LAYER ******
						
						
		// ############ MARK UP  ############
		loginScene.setAlignment(Pos.CENTER);
		loginScene.setPadding(new Insets(-30, 0,0,0));
			//titleLayout
				titleRect.setFill(Color.rgb(242, 201, 71));  // Set the fill color
				titleRect.setStroke(Color.LIGHTYELLOW);
				titleRect.setArcWidth(20);
				titleRect.setArcHeight(20);
				titleText.setFill(Color.BLACK);  
				titleText.setFont(FONT_1);// Set text color
				titleText.setStrokeWidth(40);
			middleLayout.setMaxWidth(500);
			middleLayout.setPadding(new Insets(35,0,0,0));
			for (int i = 0; i < 2; ++i) {
				ColumnConstraints c = new ColumnConstraints();
				c.setPercentWidth(50);
				middleLayout.getColumnConstraints().add(c);
			}
			RowConstraints r1 = new RowConstraints();
			r1.setPercentHeight(70);
			RowConstraints r2 = new RowConstraints();
			r2.setPercentHeight(30);
			middleLayout.getRowConstraints().addAll(r1,r2);
				//accountForm
					userLabel.setFont(LABEL_FONT);
					userTf.setFont(LABEL_FONT);
					passwordLabel.setFont(LABEL_FONT);
					passwordTf.setFont(LABEL_FONT);
					passwordLabel.setPadding(new Insets(20, 0, 0, 0));
				loginBtn.setAlignment(Pos.CENTER);
				loginBtn.setPadding(new Insets(30,0,0,0));
				loginBtn.setMaxWidth(100);
				GridPane.setHalignment(loginBtn, HPos.LEFT);
				GridPane.setValignment(loginBtn, VPos.TOP);
					loginBtnShape.setArcWidth(15);
					loginBtnShape.setArcHeight(15);
					loginBtnShape.setFill(Color.rgb(80,150,233));
					loginBtnShape.setStroke(Color.BLACK);
					btn1.setFont(LABEL_FONT);
					btn1.setBackground(Background.fill(Color.TRANSPARENT));
					btn1.setTextFill(Color.WHITE);
				GridPane.setHalignment(signUpRight, HPos.RIGHT);
				signUpRight.setAlignment(Pos.CENTER);
					signupBtn.setPadding(new Insets(20, 0, 0, 0));
						signupBtnShape.setArcWidth(15);
						signupBtnShape.setArcHeight(15);
						signupBtnShape.setFill(Color.rgb(211,230,96));
						signupBtnShape.setStroke(Color.BLACK);
						btn2.setFont(LABEL_FONT);
						btn2.setBackground(Background.fill(Color.TRANSPARENT));
		//************* MARK UP  *************
						

		//####### FUNCTIONS / EVENT HANDLER #############
		// ******* Handling LOG IN *******
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String username = userTf.getText();
				String password = passwordTf.getText();
				if (Account_Management.login(username, password)) {
					//primaryStage.setScene(Store.createStore(primaryStage));
					SceneStack.getInstance().push(Buyer_Store.getInstance().createScene(primaryStage));
					primaryStage.setScene(SceneStack.getInstance().peek());
					primaryStage.setFullScreen(true);
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setHeaderText(null);
					alert.setContentText("Invalid username or password");
					alert.showAndWait();
				}	
			}
		});	
		// ******* Handling SIGN UP *******
		btn2.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		        String username = userTf.getText();
		        String password = passwordTf.getText();
		        try {
					Account_Management.createAccount(username, password);
					userTf.setText("");
					passwordTf.setText("");
					notifyText.setText("Successfully signed up!");
				} catch (NoSuchAlgorithmException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e2) {
					Alert alert = new Alert(AlertType.INFORMATION);
			        alert.setTitle("Warning");
			        alert.setHeaderText(null);
			        alert.setContentText(e2.getMessage());
			        alert.showAndWait(); 
				}   
		    }
		});
		// ##### SIGN UP HOVER #####
		//Mouse on sign up button
		signupBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, 
			    new EventHandler<MouseEvent>() {
			        @Override public void handle(MouseEvent e) {
			        	signupBtn.setEffect(new DropShadow());
			        	signupBtn.setScaleX(1.1);
			        	signupBtn.setScaleY(1.1);
			        }
			});
		
		//Removing the shadow when the mouse cursor is off
		signupBtn.addEventHandler(MouseEvent.MOUSE_EXITED, 
			    new EventHandler<MouseEvent>() {
			        @Override 
			        public void handle(MouseEvent e) {
			        	signupBtn.setEffect(null);
			        	signupBtn.setScaleX(1);
			        	signupBtn.setScaleY(1);
			        }
			});
		// ##### SIGN IN HOVER #####
		// Mouse on sign in
		loginBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, 
			    new EventHandler<MouseEvent>() {
			        @Override 
			        public void handle(MouseEvent e) {
			        	loginBtn.setEffect(new DropShadow());
			        	loginBtn.setScaleX(1.1);
			        	loginBtn.setScaleY(1.1);
			        }
			});
		//Removing the shadow when the mouse cursor is off
		loginBtn.addEventHandler(MouseEvent.MOUSE_EXITED, 
			    new EventHandler<MouseEvent>() {
			        @Override public void handle(MouseEvent e) {
			        	loginBtn.setEffect(null);
			        	loginBtn.setScaleX(1);
			        	loginBtn.setScaleY(1);
			        }
				}
		);
		//************ FUNCTIONS / EVENT HANDLER ************
		
		
		//######## COMBINE TOGETHER ########
		loginScene.getChildren().addAll(titleLayout, middleLayout);
			titleLayout.getChildren().addAll(titleRect, titleText);
			middleLayout.add(accountForm, 0, 0);
			middleLayout.add(signUpRight, 1, 0);
			middleLayout.add(loginBtn, 0, 1);
				accountForm.getChildren().addAll(userLabel,userTf, passwordLabel, passwordTf);
				loginBtn.getChildren().addAll(loginBtnShape,btn1);
				signUpRight.getChildren().addAll(signupBtn);
					signupBtn.getChildren().addAll(signupBtnShape,btn2);
			middleLayout.add(notifyText, 1, 1);
		//*********** COMBINE TOGETHER ***********
				

		return new Scene(loginScene, 700, 500);
	}
}
