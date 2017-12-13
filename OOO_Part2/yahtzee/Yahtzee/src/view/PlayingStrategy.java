package view;

import java.util.ArrayList;
import domain.Category;
import domain.Category.LowerSectionCategory;
import domain.Category.UpperSectionCategory;
import domain.Player;
import domain.ThrownDice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayingStrategy implements GameScreenStrategy {
	private VBox vBoxDice;
	private HBox hBoxThrownDice, hBoxPickedDice, hBoxCategory;
	private Button rollDiceButton, submitButton;
	private Player player;
	private ComboBox<Category> categoryBox;
	private Label pointsLabel;
	private int action;
	private ObservableList<Category> options;
	private Category selectedCategory;

	public PlayingStrategy(Player player) {
		this.player = player;
		initialize();
		action = 0;
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
		rollDiceButton.setOnAction(new RollDiceHandler());
		rollDiceButton.setAlignment(Pos.CENTER);

		categoryBox = new ComboBox<Category>();
		options = FXCollections.observableArrayList();
		for (UpperSectionCategory category : UpperSectionCategory.values()) {
			options.add(category);
		}
		for (LowerSectionCategory category : LowerSectionCategory.values()) {
			if (!category.equals(LowerSectionCategory.BONUS_YAHTZEE)) {
				options.add(category);
			}
		}
		categoryBox.getItems().addAll(options);
		categoryBox.setOnAction(new CategoryHandler());
		categoryBox.setVisible(false);
		categoryBox.setPromptText("Category");
		pointsLabel = new Label();
		hBoxCategory.getChildren().addAll(categoryBox, pointsLabel);

		submitButton = new Button("Ok");
		submitButton.setOnAction(new SubmitHandler());
		submitButton.setAlignment(Pos.CENTER);
		submitButton.setVisible(false);

		vBoxDice.getChildren().add(0, rollDiceButton);
		vBoxDice.getChildren().add(submitButton);

	}

	public void setStrategyCenter() {
		player.getGameScreen().setCenter(vBoxDice);
	}

	@Override
	public void updateField(Player player) {
		this.player = player;
		switch (action) {
		case 0:
			rollDiceButton.setVisible(true);
			break;
		case 1:
		case 2:
		case 3:
			categoryBox.setVisible(true);
			loadDice(player.getThrownDice(), hBoxThrownDice, false);
			if (!hBoxPickedDice.getChildren().isEmpty() || !player.getPickedDice().isEmpty()) {
				loadDice(player.getPickedDice(), hBoxPickedDice, true);
			}
			if (selectedCategory != null) {
				submitButton.setVisible(true);
				pointsLabel.setText(player.getCategoryScore().getPoints() + " points");
			} else {
				categoryBox.setPromptText("Category");
			}
			if (action == 3)
				rollDiceButton.setVisible(false);
			break;
		case 4:
			action = 0;
			hBoxPickedDice.getChildren().clear();
			hBoxThrownDice.getChildren().clear();
			submitButton.setVisible(false);
			pointsLabel.setText("");
			categoryBox.setVisible(false);
			categoryBox.setPromptText("Category");
			if (!selectedCategory.equals(LowerSectionCategory.BONUS_YAHTZEE)){
				categoryBox.getItems().remove(categoryBox.getItems().indexOf(selectedCategory));
			}
			selectedCategory = null;
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
					button.setOnAction(new SetAsideHandler());
				}
				if (dice.isPicked()) {
					button.setVisible(false);
				}
			} else if (action < 3) {
				button.setOnAction(new ReturnHandler());
			}
			hbox.getChildren().add(button);
		}
	}

	public String toEnumString(String original) {
		return original.toUpperCase().replaceAll(" ", "_");
	}

	class RollDiceHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			action += 1;
			player.throwDice();
		}
	}

	class SetAsideHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			for (Node node : hBoxThrownDice.getChildren()) {
				Button button = (Button) node;
				if (button.equals(event.getSource())) {
					player.pickDice(hBoxThrownDice.getChildren().indexOf(button));
					break;
				}
			}
		}
	}

	class ReturnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			for (Node node : hBoxPickedDice.getChildren()) {
				Button button = (Button) node;
				if (button.equals(event.getSource())) {
					player.returnDice(hBoxPickedDice.getChildren().indexOf(button));
					break;
				}
			}
		}
	}

	class CategoryHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			selectedCategory = categoryBox.getValue();
			try {
				player.calculateCategoryScore(categoryBox.getValue());
			} catch (IllegalArgumentException e) {
				player.calculateCategoryScore(categoryBox.getValue());
			} catch (NullPointerException e) {
				categoryBox.getSelectionModel().selectFirst();
				try {
					player.calculateCategoryScore(categoryBox.getValue());
				} catch (IllegalArgumentException e2) {
					player.calculateCategoryScore(categoryBox.getValue());
				}
			}
		}

	}

	class SubmitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			try {
				if (categoryBox.getValue().equals(LowerSectionCategory.YAHTZEE)) {
					categoryBox.getItems().add(LowerSectionCategory.BONUS_YAHTZEE);
				}
			} catch (IllegalArgumentException e) {
			}
			action = 4;
			// categoryBox.getSelectionModel().clearSelection();
			// categoryBox.setValue(null);
			// categoryBox.setVisible(false);
			// categoryBox.getSelectionModel().selectFirst();
			player.endTurn();
		}
	}

}
