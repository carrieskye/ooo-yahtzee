package view;

import domain.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GameScreen extends BorderPane {
	private GameScreenStrategy strategy;
	private HBox hBoxGame, hBoxPlayer;
	private Label currentPlayerLabelBottom, gameLabel;

	public GameScreen(Player player, Player currentPlayer) {
		if (player.equals(currentPlayer)) {
			strategy = new PlayingStrategy(player);
		} else {
			strategy = new ObservingStrategy(player, currentPlayer);
		}
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
	
	public void start(){
		strategy.makeCenter();
	}
	
	public void update(Player currentPlayer){
		currentPlayerLabelBottom.setText(currentPlayer.getUsername() + " playing");
		strategy.updateField(currentPlayer);
	}

}
