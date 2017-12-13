package domain;

public interface Category {

	public enum UpperSectionCategory implements Category {
		ACES("Aces"), TWOS("Twos"), THREES("Threes"), FOURS("Fours"), FIVES("Fives"), SIXES("Sixes");

		private String text;

		private UpperSectionCategory(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}
	}

	public enum LowerSectionCategory implements Category {
		THREE_OF_A_KIND("Three of a kind"), FOUR_OF_A_KIND("Four of a kind"), FULL_HOUSE("Full house"), SMALL_STRAIGHT(
				"Small straight"), LARGE_STRAIGHT(
						"Large straight"), YAHTZEE("Yahtzee"), BONUS_YAHTZEE("Bonus Yahtzee"), CHANCE("Chance");
		
		private String text;

		private LowerSectionCategory(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}

	}

	public enum SpecialCategory implements Category {
		UPPER_SECTION_SCORE("Total score"), UPPER_SECTION_BONUS("Bonus"), UPPER_SECTION_TOTAL(
				"Upper section total"), LOWER_SECTION_TOTAL("Lower section total"), GRAND_TOTAL("Grand total");

		private String text;

		private SpecialCategory(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return this.text;
		}
	}

}
