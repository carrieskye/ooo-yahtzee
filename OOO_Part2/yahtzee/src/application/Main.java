package application;

import javax.swing.JOptionPane;

import domain.DomainException;
import domain.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	Game game = Game.getInstance();

	@Override
	public void start(Stage primaryStage) {
		try {
			// BorderPane screen = new GameScreen(new Player("Jan"));
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 900, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			registerPlayers();
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

	private void registerPlayers() {
		boolean registeringPlayers = true;
		while (registeringPlayers == true) {
			try {
				game.registerPlayer(JOptionPane.showInputDialog("Register player with username:"));
			} catch (DomainException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch (NullPointerException e) {
				registeringPlayers = false;
			}
		}
	}

}
