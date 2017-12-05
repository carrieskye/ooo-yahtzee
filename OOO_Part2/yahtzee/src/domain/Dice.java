package domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;

public enum Dice {

	ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);
	private Image img;
	private int number;

	private Dice(int number) {
		this.number = number;
		FileInputStream inputstream;
		try {
			inputstream = new FileInputStream("resources/dice/dice-" + number + ".png");
			this.img = new Image(inputstream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public Image getImage() {
		return this.img;
	}
	
	public int getNumber(){
		return this.number;
	}


}