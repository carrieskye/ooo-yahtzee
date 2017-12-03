package view;

import domain.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GameScreen extends BorderPane {
	private Player player;
	private GameScreenStrategy currentStrategy, playingStrategy, observingStrategy;
	private HBox hBoxGame, hBoxPlayer;
	private Label currentPlayerLabelBottom, gameLabel;

	public GameScreen(Player player, Player currentPlayer) {
		this.player = player;
		playingStrategy = new PlayingStrategy(player);
		observingStrategy = new ObservingStrategy(player, currentPlayer);
		updateStrategy(currentPlayer);
		makeTop();
		makeBottom(currentPlayer);
	}

	public void makeTop() {
		hBoxGame = new HBox(5);
		gameLabel = new Label("Yahtzee");
		gameLabel.getStyleClass().add("game-label");
		hBoxGame.getChildren().add(gameLabel);
		this.setTop(hBoxGame);
	}

	public void makeBottom(Player currentPlayer) {
		hBoxPlayer = new HBox(5);
		currentPlayerLabelBottom = new Label(currentPlayer.getUsername() + " playing");
		currentPlayerLabelBottom.getStyleClass().add("player-label");
		hBoxPlayer.getChildren().add(currentPlayerLabelBottom);
		this.setBottom(hBoxPlayer);
	}

	public void start() {
		playingStrategy.makeCenter();
		observingStrategy.makeCenter();
		currentStrategy.setStrategyCenter();	
		}

	public void updateStrategy(Player currentPlayer) {
		if (player.equals(currentPlayer)) {
			currentStrategy = playingStrategy;
		} else {
			currentStrategy = observingStrategy;
		}
	}

	public void update(Player currentPlayer) {
		currentPlayerLabelBottom.setText(currentPlayer.getUsername() + " playing");
		updateStrategy(currentPlayer);
		currentStrategy.setStrategyCenter();
		currentStrategy.updateField(currentPlayer);
	}

}
