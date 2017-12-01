package view;

import java.util.ArrayList;

import domain.Dice;
import domain.Player;
import domain.ThrownDice;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameScreen extends BorderPane {
	private Player player;
	private HBox hBoxGame, hBoxPlayer, hBoxThrownDice, hBoxPickedDice;
	private VBox vBoxDice;
	private Button rollDiceButton;
	private Label currentPlayerLabel;

	public GameScreen(Player player) {
		setPlayer(player);
		makeTop();
		makeBottom();
	}

	public void makeTop() {
		hBoxGame = new HBox(5);
		Label gameLabel = new Label("Yahtzee");
		gameLabel.getStyleClass().add("game-label");
		hBoxGame.getChildren().add(gameLabel);
		this.setTop(hBoxGame);
	}

	public void makeBottom() {
		hBoxPlayer = new HBox(5);
		Label playerLabel = new Label(player.getUsername() + " playing");
		playerLabel.getStyleClass().add("player-label");
		hBoxPlayer.getChildren().add(playerLabel);
		this.setBottom(hBoxPlayer);
	}

	public void playTurn() {
		rollDiceButton = new Button("Roll dice");
		rollDiceButton.setOnAction(new RollDiceHandler());
		rollDiceButton.setAlignment(Pos.CENTER);
		makeDiceField(rollDiceButton, player);
	}

	public void observeCurrentPlayer(Player player) {
		currentPlayerLabel = new Label(player.getUsername() + " is currently playing.");
		makeDiceField(currentPlayerLabel, player);
		if (!player.getThrownDice().isEmpty()) {
			currentPlayerLabel.setText(player.getUsername() + " threw:");
		}

	}

	public void makeDiceField(Node node, Player player) {
		vBoxDice = new VBox(5);
		vBoxDice.setAlignment(Pos.TOP_CENTER);
		vBoxDice.setPadding(new Insets(15, 0, 0, 0));
		hBoxThrownDice = new HBox(5);
		hBoxThrownDice.getStyleClass().add("dice-images");
		hBoxPickedDice = new HBox(5);
		hBoxPickedDice.getStyleClass().add("dice-images");
		vBoxDice.getChildren().addAll(node, hBoxThrownDice, hBoxPickedDice);
		this.setCenter(vBoxDice);
		if (!player.getThrownDice().isEmpty()) {
			loadDice(player.getThrownDice(), hBoxThrownDice, false);
			if (!player.getPickedDice().isEmpty()) {
				loadDice(player.getPickedDice(), hBoxPickedDice, true);
			}
		}
	}

	public void loadDice(ArrayList<ThrownDice> diceList, HBox hbox, Boolean picked) {
		hbox.getChildren().clear();
		ImageView imageView;
		for (ThrownDice dice : diceList) {
			imageView = new ImageView();
			imageView.setImage(dice.getDice().getImage());
			imageView.setFitWidth(40);
			imageView.setFitHeight(40);
			Button button = new Button(null, imageView);
			button.getStyleClass().add("dice-button");
			if (!picked) {
				button.setOnAction(new SetAsideHandler());
				if (dice.isPicked()) {
					button.setVisible(false);
				}
			} else {
				button.setOnAction(new ReturnHandler());
			}
			hbox.getChildren().add(button);
		}
	}

	private void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

	class RollDiceHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			player.throwDice();
		}
	}

	class SetAsideHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			for (Node node : hBoxThrownDice.getChildren()) {
				Button button = (Button) node;
				if (button.equals(event.getSource())) {
					player.pickDice(hBoxThrownDice.getChildren().indexOf(button));
				}
			}
		}
	}

	class ReturnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			for (Node node : hBoxPickedDice.getChildren()) {
				Button button = (Button) node;
				if (button.equals(event.getSource())) {
					player.returnDice(hBoxPickedDice.getChildren().indexOf(button));
				}
			}
		}
	}

}
