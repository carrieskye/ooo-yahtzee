package view;

import java.util.ArrayList;

import controller.PlayerController;
import controller.PlayingStrategyController;
import domain.Category;
import domain.Category.LowerSectionCategory;
import domain.Category.UpperSectionCategory;
import domain.ThrownDice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayingStrategy implements GameScreenStrategy {
	private PlayingStrategyController controller;
	private VBox vBoxDice;
	private HBox hBoxThrownDice, hBoxPickedDice, hBoxCategory;
	private Button rollDiceButton, submitButton;
	private String player;
	private ComboBox<Category> categoryBox;
	private Label pointsLabel;
	private int action;
	private ObservableList<Category> options;
	private Category selectedCategory;

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
		controller.addCategoryHandler(categoryBox);
		categoryBox.setVisible(false);
		categoryBox.setPromptText("Category");
		pointsLabel = new Label();
		hBoxCategory.getChildren().addAll(categoryBox, pointsLabel);

		submitButton = new Button("Ok");
		controller.addSubmitHandler(submitButton);
		submitButton.setAlignment(Pos.CENTER);
		submitButton.setVisible(false);

		vBoxDice.getChildren().add(0, rollDiceButton);
		vBoxDice.getChildren().add(submitButton);

	}

	public void setStrategyCenter() {
		controller.setCenter(vBoxDice);
	}

	@Override
	public void updateField(String player, String category, int points, ArrayList<ThrownDice> thrownDice,
			ArrayList<ThrownDice> pickedDice) {
		this.player = player;
		switch (action) {
		case 0:
			rollDiceButton.setVisible(true);
			break;
		case 1:
		case 2:
		case 3:
			categoryBox.setVisible(true);
			loadDice(thrownDice, hBoxThrownDice, false);
			if (!hBoxPickedDice.getChildren().isEmpty() || !pickedDice.isEmpty()) {
				loadDice(pickedDice, hBoxPickedDice, true);
			}
			if (selectedCategory != null || categoryBox.getItems().size() == 1) {
				submitButton.setVisible(true);
				pointsLabel.setText(points + " points");
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
			if (selectedCategory != null && !selectedCategory.equals(LowerSectionCategory.BONUS_YAHTZEE)) {
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

	public Button getSubmitButton() {
		return submitButton;
	}

	public String getPlayer() {
		return player;
	}

	public ComboBox<Category> getCategoryBox() {
		return categoryBox;
	}

	public Label getPointsLabel() {
		return pointsLabel;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public ObservableList<Category> getOptions() {
		return options;
	}

	public Category getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(Category category) {
		this.selectedCategory = category;
	}

}
