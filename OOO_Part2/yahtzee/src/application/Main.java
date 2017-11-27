package application;

import domain.Player;
import javafx.application.Application;
import javafx.stage.Stage;
import view.GameScreen;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane screen = new GameScreen(new Player("Jan"));
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 900, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Button launchYahtzee = new Button("launch Yahtzee");
			root.setCenter(launchYahtzee);
			primaryStage.setTitle("GameSuite");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	class launchYahtzeeHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
		}
	}

}
