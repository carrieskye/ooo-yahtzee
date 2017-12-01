package view;

import java.util.ArrayList;
import domain.DomainException;
import domain.Game;
import domain.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
	private HBox hBoxNewPlayers, hBoxButtons;
	private VBox vBoxNewPlayers;
	private Label gameLabel, playerLabel, currentPlayersLabel;
	private TextField playerField;
	private Stage primaryStage;
	private Button addPlayer, launchYahtzee;
	private ArrayList<Stage> playerStages = new ArrayList<>();

	public StartScreen(Game game, Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.game = game;
		hBoxNewPlayers = new HBox(5);
		hBoxButtons = new HBox(5);
		vBoxNewPlayers = new VBox(5);
		makeGameLabel();
		makePlayersInput();
		makeButtons();
	}

	private void makeGameLabel() {
		gameLabel = new Label("Yahtzee");
		setLabelLayout(gameLabel);
		this.setTop(gameLabel);
	}

	private void makePlayersInput() {
		vBoxNewPlayers.setAlignment(Pos.TOP_CENTER);
		hBoxNewPlayers.setAlignment(Pos.CENTER);
		hBoxNewPlayers.setMinHeight(50);
		playerLabel = new Label("Enter name of player:");
		playerField = new TextField();
		currentPlayersLabel = new Label();
		hBoxNewPlayers.getChildren().addAll(playerLabel, playerField);
		vBoxNewPlayers.getChildren().addAll(hBoxNewPlayers, currentPlayersLabel);
		this.setCenter(vBoxNewPlayers);
	}

	private void makeButtons() {
		hBoxButtons.setAlignment(Pos.CENTER);
		hBoxButtons.setMinHeight(50);
		addPlayer = new Button("Add Player");
		addPlayer.setOnAction(new AddPlayerHandler());
		hBoxButtons.getChildren().add(addPlayer);
		launchYahtzee = new Button("Launch Yahtzee");
		launchYahtzee.setOnAction(new LaunchYahtzeeHandler());
		this.setBottom(hBoxButtons);
	}

	private Label setLabelLayout(Label label) {
		label.setStyle(
				"-fx-background-color: #FFFFFF; -fx-alignment: center; -fx-font: 25px Tahoma; -fx-font-weight: bold");
		label.setMinHeight(50);
		label.setMinWidth(400);
		return label;
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
		Scene scene = new Scene(player.getGameScreen(), 900, 600);
		scene.getStylesheets().addAll(primaryStage.getScene().getStylesheets());
		stage.setTitle("Screen of " + player.getUsername());
		stage.setScene(scene);
		stage.show();
		playerStages.add(stage);
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
				primaryStage.setHeight(200 + 18 * game.getPlayers().size());
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
