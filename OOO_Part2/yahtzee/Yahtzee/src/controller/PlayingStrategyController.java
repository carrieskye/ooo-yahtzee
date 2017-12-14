package controller;

import domain.Game;
import domain.Player;
import domain.Category;
import domain.Category.LowerSectionCategory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import view.GameScreen;
import view.GameScreenStrategy;
import view.PlayingStrategy;

public class PlayingStrategyController extends PlayerController {
	private PlayingStrategy strategy;

	public PlayingStrategyController(Game game, Player player, GameScreen screen, GameScreenStrategy strategy) {
		super(game, player, screen);
		this.strategy = (PlayingStrategy) strategy;
		this.screen = screen;
	}

	public void addRollDiceHandler(Button button) {
		button.setOnAction(new RollDiceHandler());
	}

	public void addSetAsideHandler(Button button) {
		button.setOnAction(new SetAsideHandler());
	}

	public void addReturnHandler(Button button) {
		button.setOnAction(new ReturnHandler());
	}

	public void addCategoryHandler(ComboBox<Category> categoryBox) {
		categoryBox.setOnAction(new CategoryHandler());
	}

	public void addSubmitHandler(Button button) {
		button.setOnAction(new SubmitHandler());
	}

	public void setCenter(VBox vbox) {
		screen.setCenter(vbox);
	}

	class RollDiceHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			strategy.setAction(strategy.getAction() + 1);
			player.throwDice();
		}
	}

	class SetAsideHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			for (Node node : strategy.gethBoxThrownDice().getChildren()) {
				Button button = (Button) node;
				if (button.equals(event.getSource())) {
					player.pickDice(strategy.gethBoxThrownDice().getChildren().indexOf(button));
					break;
				}
			}
		}
	}

	class ReturnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			for (Node node : strategy.gethBoxPickedDice().getChildren()) {
				Button button = (Button) node;
				if (button.equals(event.getSource())) {
					player.returnDice(strategy.gethBoxPickedDice().getChildren().indexOf(button));
					break;
				}
			}
		}
	}

	class CategoryHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			strategy.setSelectedCategory(strategy.getCategoryBox().getValue());
			try {
				player.calculateCategoryScore(strategy.getCategoryBox().getValue());
			} catch (IllegalArgumentException e) {
				player.calculateCategoryScore(strategy.getCategoryBox().getValue());
			} catch (NullPointerException e) {
				strategy.getCategoryBox().getSelectionModel().selectFirst();
				try {
					player.calculateCategoryScore(strategy.getCategoryBox().getValue());
				} catch (IllegalArgumentException e2) {
					player.calculateCategoryScore(strategy.getCategoryBox().getValue());
				}
			}
		}

	}

	class SubmitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try {
				if (strategy.getCategoryBox().getValue().equals(LowerSectionCategory.YAHTZEE)
						&& player.getCategoryScore().getPoints() != 0) {
					strategy.getCategoryBox().getItems().add(LowerSectionCategory.BONUS_YAHTZEE);
				}
			} catch (IllegalArgumentException e) {
			}
			strategy.setAction(4);
			player.endTurn();
		}
	}

}
