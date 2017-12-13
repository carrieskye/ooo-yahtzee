package controller;

import domain.Game;
import domain.Player;
import domain.Yahtzee;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.StartScreen;

public class StartController {
	private BorderPane root;
	private Scene scene;

	public StartController(Yahtzee yahtzee, Stage stage, Game game) throws Exception {
		root = new StartScreen(yahtzee, game, stage);
		scene = new Scene(root, 380, 225 + 20 * game.getPlayers().size());
		scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
		stage.setTitle("GameSuite");
		stage.setScene(scene);
		stage.show();
	}
	
	public void startPlayerScreen(Player player, String resource) {
		Stage stage = new Stage();
		Scene scene = new Scene(player.getGameScreen(), 1200, 800);
		scene.getStylesheets().add(getClass().getResource(resource).toExternalForm());
		stage.setTitle("Screen of " + player.getUsername());
		stage.setScene(scene);
		stage.show();
		player.getGameScreen().setStage(stage);
	}
}
