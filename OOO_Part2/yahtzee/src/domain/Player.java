package domain;

import java.util.Observable;
import java.util.Observer;

import view.GameScreen;

public class Player implements Observer{
	private String username;
	private GameScreen veld;
	
	public Player(String username){
		veld = new GameScreen();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
