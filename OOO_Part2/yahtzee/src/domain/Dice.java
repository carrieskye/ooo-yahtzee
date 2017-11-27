package domain;

import java.util.Random;

public class Dice {
	private int value;
	
	public Dice(){
		value = 0;
	}
	
	public void throwDice(){
		Random rand = new Random();
		this.value = rand.nextInt((6 - 1) + 1) + 1;
		System.out.println(this.value);
	}

}
