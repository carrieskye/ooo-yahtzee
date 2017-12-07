package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import view.GameScreen;

public class Game extends Observable {
	private List<Player> players = new ArrayList<>();
	private static Game uniqueInstance = new Game();
	private Player currentPlayer;
	private static final int MAX_TURN = 13;

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
		if (currentPlayer.getTurn() >= MAX_TURN) {
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
		// TODO
		// Implement GameScreen.endGame()
		somethingChanged();
	}

	public void somethingChanged() {
		setChanged();
		notifyObservers();
	}

}
