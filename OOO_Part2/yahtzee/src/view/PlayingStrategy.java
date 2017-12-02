package view;

import java.util.ArrayList;
import domain.Category;
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
	private ComboBox<String> categoryBox;
	private Label pointsLabel;
	private int rolls;

	public PlayingStrategy(Player player) {
		this.player = player;
		initialize();
		rolls = 0;
	}

	public void initialize() {
		vBoxDice = new VBox(5);
		hBoxThrownDice = new HBox(5);
		hBoxPickedDice = new HBox(5);
		hBoxCategory = new HBox(5);
		hBoxThrownDice.getStyleClass().add("dice-images");
		hBoxPickedDice.getStyleClass().add("dice-images");
		hBoxCategory.getStyleClass().add("dice-images");
		vBoxDice.setAlignment(Pos.TOP_CENTER);
		vBoxDice.setPadding(new Insets(15, 0, 0, 0));
		vBoxDice.getChildren().addAll(hBoxThrownDice, hBoxPickedDice, hBoxCategory);
	}

	@Override
	public void makeCenter() {
		rollDiceButton = new Button("Roll dice");
		rollDiceButton.setOnAction(new RollDiceHandler());
		rollDiceButton.setAlignment(Pos.CENTER);

		ObservableList<String> options = FXCollections.observableArrayList("ACES", "TWOS", "THREES", "FOURS", "FIVES",
				"SIXES", "THREE_OF_A_KIND", "FOUR_OF_A_KIND", "FULL_HOUSE", "SMALL_STRAIGHT", "LARGE_STRAIGHT",
				"YAHTZEE", "CHANCE");
		categoryBox = new ComboBox<String>(options);
		categoryBox.setOnAction(new CategoryHandler());
		categoryBox.setVisible(false);
		pointsLabel = new Label();
		hBoxCategory.getChildren().addAll(categoryBox, pointsLabel);
		
		submitButton = new Button("Ok");
		submitButton.setOnAction(new SubmitHandler());
		submitButton.setAlignment(Pos.CENTER);
		submitButton.setVisible(false);

		vBoxDice.getChildren().add(0, rollDiceButton);
		vBoxDice.getChildren().add(submitButton);
		player.getGameScreen().setCenter(vBoxDice);

		updateField(this.player);
	}

	@Override
	public void updateField(Player player) {
		this.player = player;
		if (rolls >= 3){
			rollDiceButton.setVisible(false);
		}
		if (!player.getThrownDice().isEmpty()) {
			categoryBox.setVisible(true);
			loadDice(player.getThrownDice(), hBoxThrownDice, false);
			if (!hBoxPickedDice.getChildren().isEmpty() || !player.getPickedDice().isEmpty()) {
				loadDice(player.getPickedDice(), hBoxPickedDice, true);
			}
			if (categoryBox.getPromptText() == "") {
				categoryBox.setPromptText("Category");
			} else if (player.getCategoryScore() != null) {
				submitButton.setVisible(true);
				pointsLabel.setText(player.getCategoryScore().getPoints() + " points");
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
			if (!picked) {
				button.setOnAction(new SetAsideHandler());
				if (dice.isPicked()) {
					button.setVisible(false);
				}
			} else {
				button.setOnAction(new ReturnHandler());
			}
			hbox.getChildren().add(button);
		}
	}

	class RollDiceHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			rolls += 1;
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
			player.calculateCategoryScore(Category.valueOf(categoryBox.getValue()));
		}
	}
	
	class SubmitHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			rolls = 0;
			player.endTurn();
		}
	}
	
	

}
