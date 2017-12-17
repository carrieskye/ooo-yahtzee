package view;

import java.util.ArrayList;
import controller.PlayerController;
import controller.PlayingStrategyController;
import domain.ThrownDice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayingStrategy implements GameScreenStrategy {
	private PlayingStrategyController controller;
	private VBox vBoxDice;
	private HBox hBoxThrownDice, hBoxPickedDice, hBoxCategory;
	private Button rollDiceButton;
	private String player;
	private int action;

	public PlayingStrategy(String player) {
		this.player = player;
		initialize();
		action = 0;
	}

	public void addController(PlayerController controller) {
		this.controller = (PlayingStrategyController) controller;
	}

	public void initialize() {
		vBoxDice = new VBox(5);
		vBoxDice.getStyleClass().add("dice-field");
		vBoxDice.setAlignment(Pos.TOP_CENTER);
		vBoxDice.setPadding(new Insets(15, 0, 0, 0));
		hBoxThrownDice = new HBox(5);
		hBoxPickedDice = new HBox(5);
		hBoxCategory = new HBox(5);
		hBoxThrownDice.getStyleClass().add("dice-images");
		hBoxPickedDice.getStyleClass().add("dice-images");
		hBoxCategory.getStyleClass().add("dice-images");
		vBoxDice.getChildren().addAll(hBoxThrownDice, hBoxPickedDice, hBoxCategory);
	}

	@Override
	public void makeCenter() {
		rollDiceButton = new Button("Roll dice");
		controller.addRollDiceHandler(rollDiceButton);
		rollDiceButton.setAlignment(Pos.CENTER);
		vBoxDice.getChildren().add(0, rollDiceButton);
	}

	public void setStrategyCenter() {
		controller.setCenter(vBoxDice);
	}

	@Override
	public void updateField(String player, ArrayList<ThrownDice> thrownDice,
			ArrayList<ThrownDice> pickedDice) {
		this.player = player;
		switch (action) {
		case 0:
			rollDiceButton.setVisible(true);
			break;
		case 1:
		case 2:
		case 3:
			loadDice(thrownDice, hBoxThrownDice, false);
			if (!hBoxPickedDice.getChildren().isEmpty() || !pickedDice.isEmpty()) {
				loadDice(pickedDice, hBoxPickedDice, true);
			}
			if (action == 3)
				rollDiceButton.setVisible(false);
			break;
		case 4:
			action = 0;
			hBoxPickedDice.getChildren().clear();
			hBoxThrownDice.getChildren().clear();
			break;
		}
	}

	public void loadDice(ArrayList<ThrownDice> diceList, HBox hbox, Boolean picked) {
		hbox.getChildren().clear();
		ImageView imageView;
		for (ThrownDice dice : diceList) {
			imageView = new ImageView();
			imageView.setImage(dice.getDice().getImage());
			imageView.setFitWidth(40);
			imageView.setFitHeight(40);
			Button button = new Button(null, imageView);
			button.getStyleClass().add("dice-button");
			if (!picked) {
				if (action < 3) {
					controller.addSetAsideHandler(button);
				}
				if (dice.isPicked()) {
					button.setVisible(false);
				}
			} else if (action < 3) {
				controller.addReturnHandler(button);
			}
			hbox.getChildren().add(button);
		}
	}

	public String toEnumString(String original) {
		return original.toUpperCase().replaceAll(" ", "_");
	}

	public PlayingStrategyController getController() {
		return controller;
	}

	public VBox getvBoxDice() {
		return vBoxDice;
	}

	public HBox gethBoxThrownDice() {
		return hBoxThrownDice;
	}

	public HBox gethBoxPickedDice() {
		return hBoxPickedDice;
	}

	public HBox gethBoxCategory() {
		return hBoxCategory;
	}

	public Button getRollDiceButton() {
		return rollDiceButton;
	}

	public String getPlayer() {
		return player;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
