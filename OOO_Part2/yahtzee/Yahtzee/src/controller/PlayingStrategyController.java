package controller;

import domain.Category.LowerSectionCategory;
import domain.Game;
import domain.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import view.ButtonCell;
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
	
	public void addCategoryHandler(Button button, ButtonCell buttoncell) {
		button.setOnAction(new CategoryHandler(buttoncell));
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
		private ButtonCell buttonCell;

		public CategoryHandler(ButtonCell buttonCell) {
			this.buttonCell = buttonCell;
		}

		@Override
		public void handle(ActionEvent event) {
			this.buttonCell.getCategory();
			buttonCell.setPicked(true);
			if (buttonCell.getCategory().equals(LowerSectionCategory.YAHTZEE)) {
				buttonCell.setYahtzee(true);
			}
			strategy.setAction(4);
			player.endTurn(buttonCell.getCategory());

		}
	}

}
