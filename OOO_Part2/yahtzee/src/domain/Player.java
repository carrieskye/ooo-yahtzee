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
	private ArrayList<Dice> thrownDice;

	public Player(Game game, String username) {
		game.addObserver(this);
		setUsername(username);
		this.game = game;
		screen = new GameScreen(this);
		thrownDice = new ArrayList<>();
	}

	public void playTurn() {
		screen.playTurn();
	}
	
	public void observeCurrentPlayer(Player player){
		screen.observeCurrentPlayer(player);
	}

	public void throwDice() {
		thrownDice.clear();
		Random rand = new Random();
		for (int i = 0; i < 5; i++) {
			int value = rand.nextInt((6 - 1) + 1) + 1;
			switch (value) {
			case 1:
				thrownDice.add(Dice.ONE);
				break;
			case 2:
				thrownDice.add(Dice.TWO);
				break;
			case 3:
				thrownDice.add(Dice.THREE);
				break;
			case 4:
				thrownDice.add(Dice.FOUR);
				break;
			case 5:
				thrownDice.add(Dice.FIVE);
				break;
			case 6:
				thrownDice.add(Dice.SIX);
				break;
			}
		}
		
		game.showDice();
	}

	private void setUsername(String username) {
		if (username.isEmpty() || username == null) {
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public GameScreen getGameScreen() {
		return screen;
	}

	public ArrayList<Dice> getThrownDice() {
		return this.thrownDice;
	}

	@Override
	public void update(Observable o, Object arg) {
		Game game = (Game) o;
		if (game.getCurrentPlayer().equals(this)) {
			playTurn();
		}
		else{
			observeCurrentPlayer(game.getCurrentPlayer());
		}
	}

}
