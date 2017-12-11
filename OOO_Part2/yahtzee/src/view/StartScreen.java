package view;

import domain.DomainException;
import domain.Game;
import domain.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartScreen extends BorderPane {
	private Game game;
	private HBox hBoxTop, hBoxNewPlayers, hBoxButtons;
	private VBox vBoxNewPlayers;
	private Label gameLabel, playerLabel, currentPlayersLabel;
	private TextField playerField;
	private Stage primaryStage;
	private Button addPlayer, launchYahtzee;

	public StartScreen(Game game, Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.game = game;
		hBoxTop = new HBox(5);
		hBoxNewPlayers = new HBox(5);
		hBoxButtons = new HBox(5);
		vBoxNewPlayers = new VBox(5);
		makeGameLabel();
		makePlayersInput();
		makeButtons();
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
		addPlayer.setOnAction(new AddPlayerHandler());
		hBoxButtons.getChildren().add(addPlayer);
		launchYahtzee = new Button("Launch Yahtzee");
		launchYahtzee.setOnAction(new LaunchYahtzeeHandler());
		this.setBottom(hBoxButtons);
	}

	private void updateCurrentPlayers() {
		String playerNames = "Current players:\n";
		for (Player player : game.getPlayers()) {
			playerNames += player.getUsername() + "\n";
		}
		currentPlayersLabel.setText(playerNames);

		if (game.getPlayers().size() == 2) {
			hBoxButtons.getChildren().add(launchYahtzee);
		}
	}

	private void startPlayerScreen(Player player) {
		Stage stage = new Stage();
		Scene scene = new Scene(player.getGameScreen(), 1200, 800);
		scene.getStylesheets().addAll(primaryStage.getScene().getStylesheets());
		stage.setTitle("Screen of " + player.getUsername());
		stage.setScene(scene);
		stage.show();
		player.getGameScreen().setStage(stage);
	}

	class AddPlayerHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try {
				Player player = new Player(game, playerField.getText());
				game.registerPlayer(player);
				playerField.clear();
				playerField.setPromptText("");
				updateCurrentPlayers();
				primaryStage.setHeight(225 + 20 * game.getPlayers().size());
				startPlayerScreen(player);
			} catch (DomainException e) {
				playerField.clear();				
				playerField.setPromptText(e.getMessage());
			}	
		}
	}

	class LaunchYahtzeeHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			game.startGame();
			primaryStage.close();
		}
	}

}
