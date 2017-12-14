package view;

import controller.GameController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartScreen extends BorderPane {
	private GameController controller;
	private HBox hBoxTop, hBoxNewPlayers, hBoxButtons;
	private VBox vBoxNewPlayers;
	private Label gameLabel, playerLabel, currentPlayersLabel;
	private TextField playerField;
	private Button addPlayer, launchYahtzee;

	public StartScreen(GameController controller) throws Exception {
		this.controller = controller;
		hBoxTop = new HBox(5);
		hBoxNewPlayers = new HBox(5);
		hBoxButtons = new HBox(5);
		vBoxNewPlayers = new VBox(5);
		makeGameLabel();
		makePlayersInput();
		makeButtons();
		controller.updateCurrentPlayers();
	}

	private void makeGameLabel() {
		hBoxTop.getStyleClass().add("top-bottom-hbox");
		gameLabel = new Label("Yahtzee");
		gameLabel.getStyleClass().add("game-label");
		hBoxTop.getChildren().add(gameLabel);
		this.setTop(hBoxTop);
	}

	private void makePlayersInput() {
		vBoxNewPlayers.getStyleClass().add("start-screen-vbox");
		hBoxNewPlayers.getStyleClass().add("start-screen-hbox");
		playerLabel = new Label("Enter name of player:");
		playerField = new TextField();
		currentPlayersLabel = new Label();
		hBoxNewPlayers.getChildren().addAll(playerLabel, playerField);
		vBoxNewPlayers.getChildren().addAll(hBoxNewPlayers, currentPlayersLabel);
		this.setCenter(vBoxNewPlayers);
	}

	private void makeButtons() {
		hBoxButtons.getStyleClass().add("start-screen-hbox");
		addPlayer = new Button("Add Player");
		controller.addAddPlayerHandler(addPlayer);
		hBoxButtons.getChildren().add(addPlayer);

		launchYahtzee = new Button("Launch Yahtzee");
		controller.addLaunchYahtzeeHandler(launchYahtzee);
		this.setBottom(hBoxButtons);
	}

	public HBox gethBoxButtons() {
		return this.hBoxButtons;
	}

	public TextField getPlayerField() {
		return this.playerField;
	}

	public Label getCurrentPlayersLabel() {
		return this.currentPlayersLabel;
	}

	public Button getLaunchYahtzee() {
		return this.launchYahtzee;
	}

}
