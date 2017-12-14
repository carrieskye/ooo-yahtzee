package application;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import controller.GameController;
import controller.PlayerController;
import domain.Game;
import domain.Player;
import javafx.stage.Stage;
import view.GameScreen;

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
					Player player = new Player(game, oldPlayer.getUsername());
					PlayerController controller = new PlayerController(game, player);
					GameScreen playerScreen = new GameScreen(controller, player.getUsername(), game.getCurrentPlayer().getUsername());
					controller.addScreen(playerScreen);
					game.registerPlayer(player);
					gameController.startPlayerScreen(player, playerScreen, "../application/application.css");
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	
}
