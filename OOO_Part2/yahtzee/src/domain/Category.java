package domain;

public enum Category {
	ACES("Aces"), TWOS("Twos"), THREES("Threes"), FOURS("Fours"), FIVES("Fives"), SIXES("Sixes"), THREE_OF_A_KIND(
			"Three of a Kind"), FOUR_OF_A_KIND("Four of a Kind"), FULL_HOUSE("Full House"), SMALL_STRAIGHT(
					"Small Straight"), LARGE_STRAIGHT("Large Straight"), YAHTZEE("Yahtzee"), CHANCE("Chance");

	private Category(String category) {

	}

}
