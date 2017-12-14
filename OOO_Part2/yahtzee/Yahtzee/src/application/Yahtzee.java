package application;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import controller.GameController;
import domain.Game;
import domain.Player;
import javafx.stage.Stage;

public class Yahtzee {
	private Game game;

	public Yahtzee(Stage stage, ArrayList<Player> players) {
		try {
			game = Game.getInstance();
			game.reset();
			GameController gameController = new GameController(this, stage, game);
			game.setController(gameController);
			if (players != null) {
				for (Player oldPlayer : players) {
					gameController.initializePlayer(oldPlayer.getUsername());
				}
			}
			gameController.updateCurrentPlayers();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

}
