package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import view.GameScreen;

public class Game extends Observable {
	private List<Player> players = new ArrayList<>();
	private static Game uniqueInstance = new Game();
	private Player currentPlayer;

	private Game() {
	}

	public static Game getInstance() {
		return uniqueInstance;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void registerPlayer(Player player) throws DomainException {
		if (players.isEmpty()) {
			setCurrentPlayer(player);
		}
		for (Player existingPlayer : players) {
			if (existingPlayer.getUsername().equals(player.getUsername())) {
				throw new DomainException("Player already exists.");
			}
		}
		players.add(player);
	}

	public void registerPlayer(String username) throws DomainException {
		Player player = new Player(this, username);
		if (players.isEmpty()) {
			setCurrentPlayer(player);
		}
		for (Player existingPlayer : players) {
			if (existingPlayer.getUsername().equals(username)) {
				throw new DomainException("Player already exists.");
			}
		}
		players.add(player);
	}

	public Player getPlayer(String username) throws DomainException {
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		throw new DomainException("No player with this username.");
	}

	public List<GameScreen> getGameScreens() {
		List<GameScreen> gamescreens = new ArrayList<>();
		for (Player player : players) {
			gamescreens.add((player.getGameScreen()));
		}
		return gamescreens;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void setCurrentPlayer(Player currentPlayer) {
		if (currentPlayer.isGameOver()) {
			int i = 0;
			while (i < players.size() && currentPlayer.isGameOver()) {
				if (players.indexOf(currentPlayer) == players.size() - 1) {
					setCurrentPlayer(players.get(0));
				} else {
					setCurrentPlayer(players.get(players.indexOf(currentPlayer) + 1));
				}
				i++;
			}
			gameIsOver();
		} else {
			this.currentPlayer = currentPlayer;
		}
	}

	public void updateCurrentPlayer() {
		if (players.indexOf(currentPlayer) == players.size() - 1) {
			setCurrentPlayer(players.get(0));
		} else {
			setCurrentPlayer(players.get(players.indexOf(currentPlayer) + 1));
		}
		somethingChanged();
	}

	public void showDice() {
		somethingChanged();
	}

	public void startGame() {
		for (Player player : players) {
			player.getGameScreen().start();
		}
	}

	public List<Player> getWinningPlayers() {
		Map<Player, Integer> playerScores = new HashMap<Player, Integer>();
		for (Player player : getPlayers()) {
			playerScores.put(player, player.calculateTotalScore());
		}
		List<Player> winningPlayers = new ArrayList<Player>();
		int highestScore = (Collections.max(playerScores.values()));
		for (Entry<Player, Integer> playerScore : playerScores.entrySet()) {
			if (playerScore.getValue() == highestScore) {
				winningPlayers.add(playerScore.getKey());
			}
		}
		return winningPlayers;
	}

	public void gameIsOver() {
		for (Player player : players) {
			player.getGameScreen().endGame();
		}
		endGame();
	}

	public void somethingChanged() {
		setChanged();
		notifyObservers();
	}

	public void endGame() {
		Player surrenderedPlayer = null;
		for (Player player : players) {
			player.getGameScreen().getStage().close();
			if (player.surrendered()) {
				surrenderedPlayer = player;
			}
		}
		endAlert(surrenderedPlayer);
	}

	public void endAlert(Player player) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Yahtzee");
		String header = "";
		if (player != null) {
			header = player.getUsername() + " surrendered.\n";
		}
		if (getWinner().size() == 1) {
			header += getWinner().get(0).getUsername() + " won with " + getWinner().get(0).getGrandTotal() + " points!";
		} else if (getWinner().size() == 2) {
			header += getWinner().get(0).getUsername() + " and " + getWinner().get(1).getUsername() + " won with "
					+ getWinner().get(0).getGrandTotal() + " points!";
		}
		alert.setHeaderText(header);
		alert.setContentText("Want to play again?");

		ButtonType buttonTypeYes = new ButtonType("Yes");
		ButtonType buttonTypeNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeYes) {
		} else {
			alert.close();
		}
	}

	public ArrayList<Player> getWinner() {
		Player winner = null;
		Player secondWinner = null;
		int maxPoints = -1;
		for (Player player : players) {
			if (player.getGrandTotal() > maxPoints && !player.surrendered()) {
				maxPoints = player.getGrandTotal();
				winner = player;
			} else if (player.getGrandTotal() == maxPoints && !player.surrendered()) {
				secondWinner = player;
			}
		}
		ArrayList<Player> winners = new ArrayList<>();
		winners.add(winner);
		if (secondWinner != null) {
			if (secondWinner.getGrandTotal() == winner.getGrandTotal()) {
				winners.add(secondWinner);
			}
		}
		return winners;
	}
	


}
