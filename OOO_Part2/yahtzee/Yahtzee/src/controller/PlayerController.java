package controller;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import domain.Game;
import domain.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import view.GameScreen;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class PlayerController implements Observer {
	private Game game;
	private Player player;
	private GameScreen screen;
	private PlayingStrategyController playingController;
	private ObservingStrategyController observingController;

	public PlayerController(Game game, Player player) {
		this.game = game;
		game.addObserver(this);
		this.player = player;

		player.addController(this);
	}
	
	public void addScreen(GameScreen screen){
		this.screen = screen;
		screen.addController(this, playingController, observingController);
		playingController = new PlayingStrategyController(game, player, screen,  screen.getPlayingStrategy());
		observingController = new ObservingStrategyController(game, player, screen, screen.getObservingStrategy());
	}

	public void startGame() {
		screen.start();
	}

	public void endGame() {
		screen.endGame();
	}

	public boolean newGame(Player player) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
		dialogPane.getStyleClass().add("alert");
		alert.setTitle("Yahtzee");
		String header = "";
		if (player != null) {
			header = player.getUsername() + " surrendered: ";
		}
		if (game.getWinner().size() == 1) {
			header += game.getWinner().get(0).getUsername() + " won with " + game.getWinner().get(0).getGrandTotal()
					+ " points!";
		} else if (game.getWinner().size() == 2) {
			header += game.getWinner().get(0).getUsername() + " and " + game.getWinner().get(1).getUsername()
					+ " won with " + game.getWinner().get(0).getGrandTotal() + " points!";
		}
		alert.setHeaderText(header);
		alert.setContentText("Want to play again?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	public void addSurrenderButtonHandler(Button button) {
		button.setOnAction(new SurrenderButtonHandler());

	}

	@Override
	public void update(Observable o, Object arg) {
		this.game = (Game) o;
		screen.getCurrentPlayerLabelBottom().setText(game.getCurrentPlayer().getUsername() + " playing");
		updateStrategy();
		screen.updateScoreTable(player.getCategoryScoreList(), player.getTotalScoresList());
		screen.getCurrentStrategy().setStrategyCenter();
		screen.getCurrentStrategy().updateField(game.getCurrentPlayer().getUsername(),
				game.getCurrentPlayer().getCategoryScore().getCategory().toString(),
				game.getCurrentPlayer().getCategoryScore().getPoints(), game.getCurrentPlayer().getThrownDice(),
				game.getCurrentPlayer().getPickedDice());
	}

	public void updateStrategy() {
		if (player.equals(game.getCurrentPlayer())) {
			screen.setCurrentStrategy(screen.getPlayingStrategy());
		} else {
			screen.setCurrentStrategy(screen.getObservingStrategy());
		}
	}

	class SurrenderButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			player.surrender();
			screen.endGame();
		}
	}
}
