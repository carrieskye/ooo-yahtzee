package domain;

import view.GameScreen;

public class Player {
	private String username;
	private GameScreen screen;

	public Player(String username) {
		setUsername(username);
		screen = new GameScreen(this);
	}

	private void setUsername(String username) {
		if (username.isEmpty() || username == null){
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

}
