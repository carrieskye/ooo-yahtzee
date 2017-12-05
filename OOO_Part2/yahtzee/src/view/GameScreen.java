package view;

import domain.CategoryScore;
import domain.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameScreen extends BorderPane {
	private Player player;
	private GameScreenStrategy currentStrategy, playingStrategy, observingStrategy;
	private HBox hBoxGame, hBoxPlayField, hBoxPlayer;
	private VBox vBoxPoints;
	private Label currentPlayerLabelBottom, gameLabel;
	TableColumn<CategoryScore, String> categoryCol;
	TableColumn<CategoryScore, Integer> scoreCol;
	private TableView<CategoryScore> scoreTable;

	public GameScreen(Player player, Player currentPlayer) {
		this.player = player;
		playingStrategy = new PlayingStrategy(player);
		observingStrategy = new ObservingStrategy(player, currentPlayer);
		updateStrategy(currentPlayer);
		makeTop();
		makeCenter();
		makeBottom(currentPlayer);
		makeRight();
	}

	public void makeTop() {
		hBoxGame = new HBox(5);
		gameLabel = new Label("Yahtzee");
		gameLabel.getStyleClass().add("game-label");
		hBoxGame.getChildren().add(gameLabel);
		this.setTop(hBoxGame);
	}

	public void makeCenter() {
		hBoxPlayField = new HBox(5);
		hBoxPlayField.getStyleClass().add("center");
		vBoxPoints = new VBox(5);
		vBoxPoints.setAlignment(Pos.TOP_CENTER);
		vBoxPoints.setPadding(new Insets(15, 0, 0, 0));
		this.setCenter(hBoxPlayField);
	}

	public void makeBottom(Player currentPlayer) {
		hBoxPlayer = new HBox(5);
		currentPlayerLabelBottom = new Label(currentPlayer.getUsername() + " playing");
		currentPlayerLabelBottom.getStyleClass().add("player-label");
		hBoxPlayer.getChildren().add(currentPlayerLabelBottom);
		this.setBottom(hBoxPlayer);
	}

	public void makeRight() {
		scoreTable = new TableView<CategoryScore>();
		scoreTable.setEditable(true);
		scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		categoryCol = new TableColumn<CategoryScore, String>("Category");
		scoreCol = new TableColumn<CategoryScore, Integer>("Score");
		scoreTable.getColumns().add(categoryCol);
		scoreTable.getColumns().add(scoreCol);
		this.setRight(scoreTable);
	}

	public void start() {
		playingStrategy.makeCenter();
		observingStrategy.makeCenter();
		currentStrategy.setStrategyCenter();
		hBoxPlayField.getChildren().add(vBoxPoints);
	}

	public void updateStrategy(Player currentPlayer) {
		if (player.equals(currentPlayer)) {
			currentStrategy = playingStrategy;
		} else {
			currentStrategy = observingStrategy;
		}
	}

	public void updateScoreTable() {
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("points"));
		ObservableList<CategoryScore> categoryScoreList = FXCollections
				.observableArrayList(player.getCategoryScoreList());
		scoreTable.setItems(categoryScoreList);
	}

	public void update(Player currentPlayer) {
		currentPlayerLabelBottom.setText(currentPlayer.getUsername() + " playing");
		updateStrategy(currentPlayer);
		updateScoreTable();
		currentStrategy.setStrategyCenter();
		currentStrategy.updateField(currentPlayer);
	}

	public void endGame() {
		// TODO
		// Use Game.getWinningPlayers() and Game.gameIsOver()
	}

	public HBox getPlayField() {
		return this.hBoxPlayField;
	}

}
