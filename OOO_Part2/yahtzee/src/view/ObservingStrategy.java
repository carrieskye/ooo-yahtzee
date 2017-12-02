package view;

import java.util.ArrayList;
import domain.Player;
import domain.ThrownDice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ObservingStrategy implements GameScreenStrategy {
	private VBox vBoxDice;
	private HBox hBoxThrownDice, hBoxPickedDice;
	private Label currentPlayerLabelCenter, categoryLabel;
	private Player currentPlayer;
	private Player player;

	public ObservingStrategy(Player player, Player currentPlayer) {
		this.player = player;
		this.currentPlayer = currentPlayer;
		initialize();
	}

	public void initialize() {
		vBoxDice = new VBox(5);
		hBoxThrownDice = new HBox(5);
		hBoxPickedDice = new HBox(5);
		hBoxThrownDice.getStyleClass().add("dice-images");
		hBoxPickedDice.getStyleClass().add("dice-images");
		currentPlayerLabelCenter = new Label();
		categoryLabel = new Label();
		vBoxDice.setAlignment(Pos.TOP_CENTER);
		vBoxDice.setPadding(new Insets(15, 0, 0, 0));
		vBoxDice.getChildren().addAll(hBoxThrownDice, hBoxPickedDice, categoryLabel);
	}

	@Override
	public void makeCenter() {
		currentPlayerLabelCenter.setText("Waiting for " + currentPlayer.getUsername() + " to throw.");
		player.getGameScreen().setCenter(vBoxDice);
	}

	public void observeCurrentPlayer(Player currentPlayer) {
		if (!currentPlayer.getThrownDice().isEmpty()) {
			currentPlayerLabelCenter.setText(currentPlayer.getUsername() + " threw:");
		}
	}

	@Override
	public void updateField(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		if (!currentPlayer.getThrownDice().isEmpty()) {
			loadDice(currentPlayer.getThrownDice(), hBoxThrownDice, false);
			if (!hBoxPickedDice.getChildren().isEmpty() || !currentPlayer.getPickedDice().isEmpty()) {
				loadDice(currentPlayer.getPickedDice(), hBoxPickedDice, true);
			}
			if (currentPlayer.getCategoryScore() != null) {
				categoryLabel.setText(currentPlayer.getCategoryScore().getCategory().toString() + ": "
						+ currentPlayer.getCategoryScore().getPoints() + " points");
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
