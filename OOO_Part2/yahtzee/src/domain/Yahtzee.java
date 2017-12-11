package domain;

import javax.swing.JOptionPane;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.StartScreen;

public class Yahtzee {
	private Game game;
	private BorderPane root;
	private Scene scene;

	public Yahtzee(Stage stage) {
		try {
			game = Game.getInstance();
			game.reset();
			root = new StartScreen(game, stage);
			scene = new Scene(root, 380, 200);
			scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
			stage.setTitle("GameSuite");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

}
