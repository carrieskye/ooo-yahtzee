package view;

import java.util.Observable;
import java.util.Observer;

import domain.Player;

public class GameScreen implements Observer {
	private Player player;

	public GameScreen(Player player) {
		setPlayer(player);
	}

	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	private void setPlayer(Player player) {
		this.player = player;
	}

}
