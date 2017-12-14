package view;

import java.util.ArrayList;

import controller.ObservingStrategyController;
import controller.PlayerController;
import domain.ThrownDice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ObservingStrategy implements GameScreenStrategy {
	private ObservingStrategyController controller;
	private VBox vBoxDice;
	private HBox hBoxThrownDice, hBoxPickedDice, hBoxScore;
	private Label currentPlayerLabelCenter, categoryLabel;
	private String currentPlayer;

	public ObservingStrategy(String player, String initialCurrentPlayer) {
		this.currentPlayer = initialCurrentPlayer;
		initialize();
	}

	public void initialize() {
		vBoxDice = new VBox(5);
		hBoxThrownDice = new HBox(5);
		hBoxPickedDice = new HBox(5);
		hBoxScore = new HBox(5);
		hBoxThrownDice.getStyleClass().add("dice-images");
		hBoxPickedDice.getStyleClass().add("dice-images");
		hBoxScore.getStyleClass().add("dice-images");
		currentPlayerLabelCenter = new Label();
		categoryLabel = new Label();
		vBoxDice.setAlignment(Pos.TOP_CENTER);
		vBoxDice.setPadding(new Insets(15, 0, 0, 0));
		vBoxDice.getChildren().addAll(currentPlayerLabelCenter, hBoxThrownDice, hBoxPickedDice, categoryLabel,
				hBoxScore);
	}

	public void addController(PlayerController controller) {
		this.controller = (ObservingStrategyController) controller;
	}

	@Override
	public void makeCenter() {
		currentPlayerLabelCenter.setText("Waiting for " + currentPlayer + " to throw.");
	}

	public void setStrategyCenter() {
		controller.setCenter(vBoxDice);
	}

	@Override
	public void updateField(String currentPlayer, String category, int points, ArrayList<ThrownDice> thrownDice,
			ArrayList<ThrownDice> pickedDice) {
		this.currentPlayer = currentPlayer;
		if (thrownDice.isEmpty()) {
			currentPlayerLabelCenter.setText("Waiting for " + currentPlayer + " to throw.");
			hBoxThrownDice.getChildren().clear();
			hBoxPickedDice.getChildren().clear();
			categoryLabel.setText("");
		} else {
			currentPlayerLabelCenter.setText(currentPlayer + " threw:");
			loadDice(thrownDice, hBoxThrownDice, false);
			if (!hBoxPickedDice.getChildren().isEmpty() || !pickedDice.isEmpty()) {
				loadDice(pickedDice, hBoxPickedDice, true);
			}
			try {
				if (category != null) {
					categoryLabel.setText(category + ": " + points + " points");
				}
			} catch (Exception e) {
			}
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
			if (!picked && dice.isPicked()) {
				button.setVisible(false);
			}
			hbox.getChildren().add(button);
		}
	}

}
