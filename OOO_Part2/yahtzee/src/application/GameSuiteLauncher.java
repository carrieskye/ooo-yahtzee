package application;

import javax.swing.JOptionPane;
import domain.Game;
import javafx.application.Application;
import javafx.stage.Stage;
import view.StartScreen;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class GameSuiteLauncher extends Application {
	Game game;
	BorderPane root;
	Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			game = Game.getInstance();
			root = new StartScreen(game, primaryStage);
			scene = new Scene(root, 380, 200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("GameSuite");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

}
