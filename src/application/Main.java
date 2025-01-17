//Main Class: Entry point for the application, handles initialization and scene management

package application;
import javafx.application.Application;
import javafx.stage.Stage;
import scene.Buyer_Store;
import scene.Login_Scene;
import scene.Seller_Store;


public class Main extends Application {

    //main method: starts the JavaFX application
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
    
    //launch method
    @Override
    public void start(Stage primaryStage) {

         //Seller_Store setup: adds a sample item to the store
    	Seller_Store s = new Seller_Store();
    	s.addItem("bookABC", "Used Like New", "Sci-Fi", 28.4, 13.6);
    	
        primaryStage.setTitle("Hello World!");
        //Scene loginScene = Login_Scene.createLoginScene(primaryStage);
        //primaryStage.setScene(loginScene);
        //Buyer_Store.createScene(primaryStage)
        SceneStack.getInstance().push(Buyer_Store.getInstance().createScene(primaryStage));
        //SceneStack.getInstance().push(Buyer_Store.createScene(primaryStage));
        
        // Display the primary stage in fullscreen
        primaryStage.setScene(SceneStack.getInstance().peek());
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}