package domain;

import java.util.ArrayList;
import java.util.Random;

import controller.PlayerController;
import domain.Category.LowerSectionCategory;
import domain.Category.SpecialCategory;
import domain.Category.UpperSectionCategory;

public class Player {
	private PlayerController controller;
	private Game game;
	private String username;
	private ArrayList<ThrownDice> thrownDice;
	private ArrayList<ThrownDice> pickedDice;
	private ArrayList<CategoryScore> categoryScores;
	private CategoryScore currentCategory, upperSectionScore, upperSectionBonus, upperSectionTotal, lowerSectionTotal,
			grandTotal;
	private int yahtzeeBonus, turn;
	private static final int MAX_TURN = 63;
	private boolean gameOver, surrendered;

	public Player(Game game, String username) throws DomainException {
		setUsername(username);
		this.game = game;
		this.yahtzeeBonus = 0;
		this.turn = 0;
		this.gameOver = false;
		this.surrendered = false;

		thrownDice = new ArrayList<>();
		pickedDice = new ArrayList<>();
		categoryScores = new ArrayList<>();

		upperSectionScore = new CategoryScore(SpecialCategory.UPPER_SECTION_SCORE);
		upperSectionBonus = new CategoryScore(SpecialCategory.UPPER_SECTION_BONUS);
		upperSectionTotal = new CategoryScore(SpecialCategory.UPPER_SECTION_TOTAL);
		lowerSectionTotal = new CategoryScore(SpecialCategory.LOWER_SECTION_TOTAL);
		grandTotal = new CategoryScore(SpecialCategory.GRAND_TOTAL);
	}

	public void addController(PlayerController controller) {
		this.controller = controller;
	}

	public void startGame() {
		controller.startGame();
	}

	public void endGame() {
		controller.endGame();
	}

	public boolean newGame(Player surrenderedPlayer) {
		return controller.newGame(surrenderedPlayer);
	}

	public void throwDice() {
		if (thrownDice.isEmpty()) {
			for (int i = 0; i < 5; i++) {
				thrownDice.add(getRandomDice());
			}
		} else {
			for (ThrownDice dice : thrownDice) {
				if (!dice.isPicked()) {
					thrownDice.set(thrownDice.indexOf(dice), getRandomDice());
					if (currentCategory != null && !currentCategory.equals(null)) {
						calculateCategoryScore(currentCategory.getCategory());
					}
				}
			}
		}
		game.showDice();
	}

	public ThrownDice getRandomDice() {
		Random rand = new Random();
		int value = rand.nextInt((6 - 1) + 1) + 1;
		switch (value) {
		case 1:
			return new ThrownDice(Dice.ONE, false);
		case 2:
			return new ThrownDice(Dice.TWO, false);
		case 3:
			return new ThrownDice(Dice.THREE, false);
		case 4:
			return new ThrownDice(Dice.FOUR, false);
		case 5:
			return new ThrownDice(Dice.FIVE, false);
		case 6:
			return new ThrownDice(Dice.SIX, false);
		default:
			return null;
		}
	}

	public void pickDice(int index) {
		thrownDice.get(index).setPicked(true);
		pickedDice.add(thrownDice.get(index));
		game.showDice();
	}

	public void returnDice(int index) {
		for (ThrownDice thrownDice : thrownDice) {
			if (thrownDice.equals(pickedDice.get(index))) {
				thrownDice.setPicked(false);
			}
		}
		pickedDice.remove(index);
		game.showDice();
	}

	public void endTurn() {
		if (currentCategory.getCategory().equals(LowerSectionCategory.BONUS_YAHTZEE)) {
			if (yahtzeeBonus >= 1) {
				for (CategoryScore categoryScore : categoryScores) {
					if (categoryScore.getCategory().equals(LowerSectionCategory.BONUS_YAHTZEE)) {
						categoryScore.setYahtzeeBonus(yahtzeeBonus, currentCategory.getDice());
					}
				}
			} else {
				categoryScores.add(currentCategory);
			}
			yahtzeeBonus += 1;
		} else {
			if (!currentCategory.getCategory().equals(LowerSectionCategory.YAHTZEE)) {
				turn += 1;
			}
			categoryScores.add(currentCategory);
		}
		updateTotals();
		thrownDice.clear();
		pickedDice.clear();
		currentCategory = null;
		game.showDice();
		game.updateCurrentPlayer();
		checkGameOver();
	}

	public void calculateCategoryScore(Category category) {
		ArrayList<Dice> categoryDice = new ArrayList<>();
		for (ThrownDice dice : pickedDice) {
			categoryDice.add(dice.getDice());
		}
		for (ThrownDice dice : thrownDice) {
			if (!dice.isPicked()) {
				categoryDice.add(dice.getDice());
			}
		}

		this.currentCategory = new CategoryScore(category, categoryDice);
		game.showDice();
	}

	public void updateTotals() {
		if (currentCategory.getCategory() instanceof UpperSectionCategory) {
			currentCategory.updateTotals(upperSectionScore);
			currentCategory.updateTotals(upperSectionTotal);
			if (upperSectionScore.getPoints() >= 13 && upperSectionBonus.getPoints() == 0) {
				currentCategory.addBonus(upperSectionBonus);
				currentCategory.addBonus(upperSectionTotal);
			}
		} else if (currentCategory.getCategory() instanceof LowerSectionCategory) {
			currentCategory.updateTotals(lowerSectionTotal);
		}
		currentCategory.updateTotals(grandTotal);
	}

	public void checkGameOver() {
		if (this.turn == MAX_TURN) {
			this.gameOver = true;
		}
	}

	public int calculateTotalScore() {
		int totalScore = 0;
		for (CategoryScore categoryScore : getCategoryScoreList()) {
			totalScore += categoryScore.getPoints();
		}
		return totalScore;
	}

	public void surrender() {
		this.surrendered = true;
		game.endGame();
	}

	private void setUsername(String username) throws DomainException {
		if (username.isEmpty() || username == null) {
			throw new DomainException("Name cannot be empty.");
		}
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public ArrayList<ThrownDice> getThrownDice() {
		return this.thrownDice;
	}

	public ArrayList<ThrownDice> getPickedDice() {
		return this.pickedDice;
	}

	public CategoryScore getCategoryScore() {
		return this.currentCategory;
	}

	public ArrayList<CategoryScore> getCategoryScoreList() {
		return this.categoryScores;
	}

	public ArrayList<CategoryScore> getTotalScoresList() {
		ArrayList<CategoryScore> totalScores = new ArrayList<>();
		totalScores.add(upperSectionScore);
		totalScores.add(upperSectionBonus);
		totalScores.add(upperSectionTotal);
		totalScores.add(lowerSectionTotal);
		totalScores.add(grandTotal);
		return totalScores;
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	public boolean surrendered() {
		return this.surrendered;
	}

	public int getGrandTotal() {
		return this.grandTotal.getPoints();
	}

}
