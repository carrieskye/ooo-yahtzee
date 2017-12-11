package domain;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.StartScreen;

public class Yahtzee {
	private Game game;
	private BorderPane root;
	private Scene scene;

	public Yahtzee(Stage stage, ArrayList<Player> players) {
		try {
			game = Game.getInstance();
			game.reset();
			if (players != null) {
				for (Player oldPlayer : players) {
					Player player = new Player(game, oldPlayer.getUsername());
					game.registerPlayer(player);
					startPlayerScreen(player, "../application/application.css");
				}
			}
			root = new StartScreen(this, game, stage);
			scene = new Scene(root, 380, 225 + 20 * game.getPlayers().size());
			scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
			stage.setTitle("GameSuite");
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
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
