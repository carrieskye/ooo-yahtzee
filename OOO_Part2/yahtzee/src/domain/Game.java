package domain;

import java.util.ArrayList;
import java.util.List;

import view.GameScreen;

public class Game {
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
		if (players.isEmpty()) {
			setCurrentPlayer(new Player(username));
		}
		for (Player existingPlayer : players) {
			if (existingPlayer.getUsername().equals(username)) {
				throw new DomainException("Player already exists.");
			}
		}
		players.add(new Player(username));
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
		this.currentPlayer = currentPlayer;
	}

	public void updateCurrentPlayer() {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).equals(getCurrentPlayer())) {
				try {
					setCurrentPlayer(players.get(i + 1));
				} catch (NullPointerException e) {
					setCurrentPlayer(players.get(0));
				}
			}
		}
	}
	
	public void startGame(){
		//TODO
	}

}
