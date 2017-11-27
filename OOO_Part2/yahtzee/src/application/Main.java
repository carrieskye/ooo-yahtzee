package application;
	
import domain.Player;
import javafx.application.Application;
import javafx.stage.Stage;
import view.GameScreen;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		BorderPane screen = new GameScreen(new Player("Jan"));
		Scene scene = new Scene(screen, 900, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
