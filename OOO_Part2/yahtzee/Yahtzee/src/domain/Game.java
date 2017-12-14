package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import controller.GameController;

public class Game extends Observable {
	private GameController controller;
	private List<Player> players;
	private static Game uniqueInstance = new Game();
	private Player currentPlayer;

	private Game() {
		players = new ArrayList<>();
	}

	public static Game getInstance() {
		return uniqueInstance;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void reset() {
		players.clear();
		this.currentPlayer = null;
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
			endGame();
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
		this.controller.startGame();
	}

	public void endGame() {
		this.controller.endGame();
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

	public void somethingChanged() {
		setChanged();
		notifyObservers();
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
