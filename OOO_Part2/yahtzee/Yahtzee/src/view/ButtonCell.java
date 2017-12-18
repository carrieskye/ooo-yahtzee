package view;

import controller.PlayingStrategyController;
import domain.Category;
import domain.Category.LowerSectionCategory;
import domain.Category.SpecialCategory;
import domain.Category.UpperSectionCategory;
import domain.CategoryScore;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

public class ButtonCell extends TableCell<CategoryScore, Integer> {
	private Category category;
	private Button pointsButton;
	private PlayingStrategyController controller;
	private boolean picked, yahtzee;

	public ButtonCell(PlayingStrategyController controller) {
		this.controller = controller;
		pointsButton = new Button();
		pointsButton.getStyleClass().add("points-button");
		this.controller.addCategoryHandler(pointsButton, this);
		textOnlyField("0");
		this.picked = false;
		this.yahtzee = false;
	}

	@Override
	protected void updateItem(Integer value, boolean empty) {
		super.updateItem(value, empty);
		if (!empty) {
			if (Integer.valueOf(value) < -1) {
				this.category = initializeCategory(value);
				if (category instanceof SpecialCategory) {
					textOnlyField("0");
				} else {
					textOnlyField("");
				}
			} else if (Integer.valueOf(value) == -1) {
				textOnlyField("");
			} else {
				if (category.equals(LowerSectionCategory.BONUS_YAHTZEE) && !yahtzee) {
					textOnlyField("");
				} else if (category instanceof SpecialCategory || picked) {
					textOnlyField(value.toString());
				} else {
					setGraphic(pointsButton);
					pointsButton.setText(value.toString());
					setText(null);
				}
			}
		}
	}

	public void textOnlyField(String text) {
		setText(text);
		setGraphic(null);
	}

	public Category initializeCategory(Integer value) {
		switch (value) {
		case -2:
			return UpperSectionCategory.ACES;
		case -3:
			return UpperSectionCategory.TWOS;
		case -4:
			return UpperSectionCategory.THREES;
		case -5:
			return UpperSectionCategory.FOURS;
		case -6:
			return UpperSectionCategory.FIVES;
		case -7:
			return UpperSectionCategory.SIXES;
		case -8:
			return SpecialCategory.UPPER_SECTION_SCORE;
		case -9:
			return SpecialCategory.UPPER_SECTION_BONUS;
		case -10:
			return SpecialCategory.UPPER_SECTION_TOTAL;
		case -11:
			return LowerSectionCategory.THREE_OF_A_KIND;
		case -12:
			return LowerSectionCategory.FOUR_OF_A_KIND;
		case -13:
			return LowerSectionCategory.FULL_HOUSE;
		case -14:
			return LowerSectionCategory.SMALL_STRAIGHT;
		case -15:
			return LowerSectionCategory.LARGE_STRAIGHT;
		case -16:
			return LowerSectionCategory.YAHTZEE;
		case -17:
			return LowerSectionCategory.BONUS_YAHTZEE;
		case -18:
			return LowerSectionCategory.CHANCE;
		case -19:
			return SpecialCategory.LOWER_SECTION_TOTAL;
		case -20:
			return SpecialCategory.GRAND_TOTAL;
		}
		return null;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setPicked(boolean picked) {
		this.picked = picked;
	}

	public void setYahtzee(boolean yahtzee) {
		this.yahtzee = yahtzee;
	}

}
