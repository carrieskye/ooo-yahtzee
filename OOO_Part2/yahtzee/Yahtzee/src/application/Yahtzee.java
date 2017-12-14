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
			GameController startController = new GameController(this, stage, game);
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
