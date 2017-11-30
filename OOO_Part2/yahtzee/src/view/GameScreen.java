package view;

import domain.Player;
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
	private HBox hBoxGame, hBoxPlayer, hBoxDiceImages;
	private VBox vBoxDice;
	private Button rollDiceButton;
	private ImageView[] diceImages;
	private Label currentPlayerLabel;

	public GameScreen(Player player) {
		diceImages = new ImageView[5];
		setPlayer(player);
		makeTop();
		makeBottom();
	}

	public void makeTop() {
		hBoxGame = new HBox(5);
		Label gameLabel = setLabelLayout(new Label("Yahtzee"), 25, 900, 50, true, true);
		hBoxGame.getChildren().add(gameLabel);
		this.setTop(hBoxGame);
	}

	public void makeBottom() {
		hBoxPlayer = new HBox(5);
		Label playerLabel = setLabelLayout(new Label(player.getUsername() + " playing"), 20, 900, 50, true, false);
		hBoxPlayer.getChildren().add(playerLabel);
		this.setBottom(hBoxPlayer);
	}

	public Label setLabelLayout(Label label, int fontsize, int width, int height, boolean background, boolean bold) {
		String style = "";
		if (background) {
			style += "-fx-background-color: #FFFFFF;";
		}
		style += "-fx-font: " + fontsize + "px Tahoma;";
		if (bold) {
			style += "-fx-font-weight: bold;";
		}
		label.setStyle(style + "-fx-alignment: center");
		label.setMinHeight(height);
		label.setMinWidth(width);
		return label;
	}

	public void playTurn() {
		rollDiceButton = new Button("Roll dice");
		rollDiceButton.setOnAction(new RollDiceHandler());
		rollDiceButton.setAlignment(Pos.CENTER);
		makeDiceField(rollDiceButton, player);
	}

	public void observeCurrentPlayer(Player player) {
		currentPlayerLabel = setLabelLayout(new Label(player.getUsername() + " is currently playing."), 15, 900, 50,
				false, false);
		makeDiceField(currentPlayerLabel, player);
		if (!player.getThrownDice().isEmpty()) {
			currentPlayerLabel.setText(player.getUsername() + " threw:");
		}

	}

	public void makeDiceField(Node node, Player player) {
		vBoxDice = new VBox(5);
		vBoxDice.setAlignment(Pos.TOP_CENTER);
		vBoxDice.setPadding(new Insets(15, 12, 15, 12));
		hBoxDiceImages = new HBox(5);
		hBoxDiceImages.setAlignment(Pos.CENTER);
		hBoxDiceImages.setPadding(new Insets(15, 12, 15, 12));
		for (int i = 0; i < 5; i++) {
			diceImages[i] = new ImageView();
			diceImages[i].setFitWidth(40);
			diceImages[i].setFitHeight(40);
			hBoxDiceImages.getChildren().add(diceImages[i]);
		}
		vBoxDice.getChildren().addAll(node, hBoxDiceImages);
		this.setCenter(vBoxDice);
		if (!player.getThrownDice().isEmpty()) {
			throwDice(player);
		}
	}

	public void throwDice(Player player) {
		hBoxDiceImages.getChildren().clear();
		for (int i = 0; i < diceImages.length; i++) {
			diceImages[i].setImage(player.getThrownDice().get(i).getImage());
			hBoxDiceImages.getChildren().add(diceImages[i]);
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

}
