package domain;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.StartController;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.StartScreen;

public class Yahtzee {
	private Game game;


	public Yahtzee(Stage stage, ArrayList<Player> players) {
		try {
			game = Game.getInstance();
			game.reset();
			StartController startController = new StartController(this, stage, game);
			if (players != null) {
				for (Player oldPlayer : players) {
					Player player = new Player(game, oldPlayer.getUsername());
					game.registerPlayer(player);
					startController.startPlayerScreen(player, "../application/application.css");
				}
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}




	
}
