package domain;

import java.util.Observable;
import java.util.Observer;

import view.GameScreen;

public class Player implements Observer {
	private String username;
	private GameScreen veld;

	public Player(String username) throws DomainException {
		this.setUsername(username);
		veld = new GameScreen();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	public void setUsername(String username) throws DomainException {
		if (username.equals(null) || username.trim().isEmpty()) {
			throw new DomainException("Username cannot be empty.");
		} else {
			this.username = username;
		}
	}

	public String getUsername() {
		return username;
	}

	public GameScreen getGameScreen() {
		return veld;
	}

}
