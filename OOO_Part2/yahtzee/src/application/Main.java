package application;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import domain.DomainException;
import domain.Game;
import domain.Player;
import javafx.application.Application;
import javafx.stage.Stage;

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

public class Main extends Application {
	Game game;
	BorderPane root;
	Scene scene;
	HBox hboxHigh = new HBox(5), hboxLow = new HBox(5);
	VBox vbox = new VBox(5);
	Label gameLabel, playerLabel, currentPlayersLabel;
	TextField playerField;
	Stage primaryStage;
	Button addPlayer, launchYahtzee;
	ArrayList<Stage> playerStages = new ArrayList<>();

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			game = Game.getInstance();
			root = new BorderPane();
			scene = new Scene(root, 380, 200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			hboxHigh.setAlignment(Pos.CENTER);
			hboxHigh.setMinHeight(50);
			hboxLow.setAlignment(Pos.CENTER);
			hboxLow.setMinHeight(50);
			vbox.setAlignment(Pos.TOP_CENTER);

			gameLabel = new Label("Yahtzee");
			setLabelLayout(gameLabel);

			playerLabel = new Label("Enter name of player:");
			playerField = new TextField();
			currentPlayersLabel = new Label();

			hboxHigh.getChildren().addAll(playerLabel, playerField);
			vbox.getChildren().addAll(hboxHigh, currentPlayersLabel);

			addPlayer = new Button("Add Player");
			addPlayer.setOnAction(new AddPlayerHandler());
			hboxLow.getChildren().add(addPlayer);
			launchYahtzee = new Button("Launch Yahtzee");
			launchYahtzee.setOnAction(new LaunchYahtzeeHandler());

			root.setTop(gameLabel);
			root.setCenter(vbox);
			root.setBottom(hboxLow);

			primaryStage.setTitle("GameSuite");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	public Label setLabelLayout(Label label) {
		label.setStyle(
				"-fx-background-color: #FFFFFF; -fx-alignment: center; -fx-font: 25px Tahoma; -fx-font-weight: bold");
		label.setMinHeight(50);
		label.setMinWidth(400);
		return label;
	}

	public void updateCurrentPlayers() {
		String playerNames = "Current players:\n";
		for (Player player : game.getPlayers()) {
			playerNames += player.getUsername() + "\n";
		}
		currentPlayersLabel.setText(playerNames);

		if (game.getPlayers().size() == 2) {
			hboxLow.getChildren().add(launchYahtzee);
		}
	}

	public void startScreen(Player player) {
		Stage stage = new Stage();
		Scene scene = new Scene(player.getGameScreen(), 900, 600);
		stage.setTitle("Screen of " + player.getUsername());
		stage.setScene(scene);
		stage.show();
		playerStages.add(stage);
	}

	class AddPlayerHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Player player = new Player(playerField.getText());
			try {
				game.registerPlayer(player);
			} catch (DomainException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
			playerField.clear();
			updateCurrentPlayers();
			primaryStage.setHeight(200 + 18 * game.getPlayers().size());
			startScreen(player);
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
