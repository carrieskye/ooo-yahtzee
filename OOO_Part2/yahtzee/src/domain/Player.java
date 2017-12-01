package domain;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import view.GameScreen;

public class Player implements Observer {
	private Game game;
	private String username;
	private GameScreen screen;
	private ArrayList<ThrownDice> thrownDice;
	private ArrayList<ThrownDice> pickedDice;

	public Player(Game game, String username) throws DomainException {
		game.addObserver(this);
		setUsername(username);
		this.game = game;
		if (game.getCurrentPlayer() == null) {
			screen = new GameScreen(this, this);
		} else {
			screen = new GameScreen(this, game.getCurrentPlayer());
		}
		thrownDice = new ArrayList<>();
		pickedDice = new ArrayList<>();
	}

	public void playTurn() {
		screen.playTurn();
	}

	public void observeCurrentPlayer(Player player) {
		screen.observeCurrentPlayer(player);
	}

	public void throwDice() {
		if (thrownDice.isEmpty()) {
			for (int i = 0; i < 5; i++) {
				thrownDice.add(getRandomDice());
			}
		} else {
			for (ThrownDice dice : thrownDice) {
				if (!dice.isPicked()) {
					thrownDice.set(thrownDice.indexOf(dice), getRandomDice());
				}
			}
		}
		game.showDice();
	}

	public ThrownDice getRandomDice() {
		Random rand = new Random();
		int value = rand.nextInt((6 - 1) + 1) + 1;
		switch (value) {
		case 1:
			return new ThrownDice(Dice.ONE, false);
		case 2:
			return new ThrownDice(Dice.TWO, false);
		case 3:
			return new ThrownDice(Dice.THREE, false);
		case 4:
			return new ThrownDice(Dice.FOUR, false);
		case 5:
			return new ThrownDice(Dice.FIVE, false);
		case 6:
			return new ThrownDice(Dice.SIX, false);
		default:
			return null;
		}
	}

	public void pickDice(int index) {
		thrownDice.get(index).setPicked(true);
		pickedDice.add(thrownDice.get(index));
		game.showDice();
	}

	public void returnDice(int index) {
		for (ThrownDice thrownDice : thrownDice) {
			if (thrownDice.equals(pickedDice.get(index))) {
				thrownDice.setPicked(false);
			}
		}
		pickedDice.remove(index);
		game.showDice();
	}

	private void setUsername(String username) throws DomainException {
		if (username.isEmpty() || username == null) {
			throw new DomainException("Name cannot be empty.");
		}
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public GameScreen getGameScreen() {
		return screen;
	}

	public ArrayList<ThrownDice> getThrownDice() {
		return this.thrownDice;
	}

	public ArrayList<ThrownDice> getPickedDice() {
		return this.pickedDice;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.game = (Game) o;
		if (game.getCurrentPlayer().equals(this)) {
			playTurn();
		} else {
			observeCurrentPlayer(game.getCurrentPlayer());
		}
	}

}
