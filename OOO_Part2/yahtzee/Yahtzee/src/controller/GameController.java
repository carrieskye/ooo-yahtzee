package controller;

import application.Yahtzee;
import domain.DomainException;
import domain.Game;
import domain.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.StartScreen;

public class GameController {
	private StartScreen screen;
	private Scene scene;
	private Stage stage;
	private Game game;

	public GameController(Yahtzee yahtzee, Stage stage, Game game) throws Exception {
		this.game = game;
		this.stage = stage;
		screen = new StartScreen(this);
		scene = new Scene(screen, 380, 225 + 20 * game.getPlayers().size());
		scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
		stage.setTitle("GameSuite");
		stage.setScene(scene);
		stage.show();
	}
	
	public void startPlayerScreen(Player player, String resource) {
		Stage stage = new Stage();
		Scene scene = new Scene(player.getGameScreen(), 1200, 800);
		scene.getStylesheets().add(getClass().getResource(resource).toExternalForm());
		stage.setTitle("Screen of " + player.getUsername());
		stage.setScene(scene);
		stage.show();
		player.getGameScreen().setStage(stage);
	}
	

	public void updateCurrentPlayers() {
		if (game.getPlayers().size() > 0) {
			String playerNames = "Current players:\n";
			for (Player player : game.getPlayers()) {
				playerNames += player.getUsername() + "\n";
			}
			screen.getCurrentPlayersLabel().setText(playerNames);
		}
		if (game.getPlayers().size() >= 2 && !screen.gethBoxButtons().getChildren().contains(screen.getLaunchYahtzee())) {
			screen.gethBoxButtons().getChildren().add(screen.getLaunchYahtzee());
		}
	}
	
	public void addLaunchYahtzeeHandler(Button button){
		button.setOnAction(new LaunchYahtzeeHandler());
	}
	
	public void addAddPlayerHandler(Button button){
		button.setOnAction(new AddPlayerHandler());
	}
	

	class AddPlayerHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try {
				Player player = new Player(game, screen.getPlayerField().getText());
				game.registerPlayer(player);
				screen.getPlayerField().clear();
				screen.getPlayerField().setPromptText("");
				updateCurrentPlayers();
				stage.setHeight(225 + 20 * game.getPlayers().size());
				startPlayerScreen(player, "../application/application.css");
			} catch (DomainException e) {
				screen.getPlayerField().clear();
				screen.getPlayerField().setPromptText(e.getMessage());
			}
		}
	}
	
	class LaunchYahtzeeHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			game.startGame();
			stage.close();
		}
	}
}
