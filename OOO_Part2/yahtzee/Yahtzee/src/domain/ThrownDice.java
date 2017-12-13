package domain;

public class ThrownDice {
	private Dice dice;
	private Boolean picked;

	public ThrownDice(Dice dice, Boolean picked) {
		setDice(dice);
		setPicked(picked);
	}

	public Dice getDice() {
		return dice;
	}

	public void setDice(Dice dice) {
		this.dice = dice;
	}

	public Boolean isPicked() {
		return picked;
	}

	public void setPicked(Boolean picked) {
		this.picked = picked;
	}
}
