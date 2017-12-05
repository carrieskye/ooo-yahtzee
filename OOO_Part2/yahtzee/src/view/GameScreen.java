package view;

import domain.Category;
import domain.CategoryScore;
import domain.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
	private Button concedeButton;
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
		concedeButton = new Button("Concede");
		concedeButton.setOnAction(new concedeButtonHandler());
		// TODO
		// Make button show up on the screen. Button appears off-screen, if you increase
		// window size he becomes visible. I don't know enough about HBoxes and such to
		// get his working.
		hBoxPlayer.getChildren().addAll(currentPlayerLabelBottom, concedeButton);
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
		ObservableList<CategoryScore> emptyList = FXCollections.observableArrayList(
				CategoryScore.getEmptyCategoryScore(Category.ACES), CategoryScore.getEmptyCategoryScore(Category.TWOS),
				CategoryScore.getEmptyCategoryScore(Category.THREES),
				CategoryScore.getEmptyCategoryScore(Category.FOURS),
				CategoryScore.getEmptyCategoryScore(Category.FIVES),
				CategoryScore.getEmptyCategoryScore(Category.SIXES),
				CategoryScore.getEmptyCategoryScore(Category.THREE_OF_A_KIND),
				CategoryScore.getEmptyCategoryScore(Category.FOUR_OF_A_KIND),
				CategoryScore.getEmptyCategoryScore(Category.FULL_HOUSE),
				CategoryScore.getEmptyCategoryScore(Category.SMALL_STRAIGHT),
				CategoryScore.getEmptyCategoryScore(Category.LARGE_STRAIGHT),
				CategoryScore.getEmptyCategoryScore(Category.YAHTZEE),
				CategoryScore.getEmptyCategoryScore(Category.CHANCE));
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("points"));
		scoreTable.setItems(emptyList);
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
		for (CategoryScore currentCategoryScore : player.getCategoryScoreList()) {
			if (currentCategoryScore != null) {
				for (CategoryScore categoryScore : scoreTable.getItems()) {
					if (categoryScore.getCategory().equals(currentCategoryScore.getCategory())) {
						scoreTable.getItems().set(scoreTable.getItems().indexOf(categoryScore), currentCategoryScore);
					}
				}
			}
		}
	}

	public void update(Player currentPlayer) {
		currentPlayerLabelBottom.setText(currentPlayer.getUsername() + " playing");
		updateStrategy(currentPlayer);
		updateScoreTable();
		currentStrategy.setStrategyCenter();
		currentStrategy.updateField(currentPlayer);
	}

	class concedeButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			endGame();
		}
	}

	public void endGame() {
		// TODO
		// Use Game.getWinningPlayers() and Game.gameIsOver()
	}

	public HBox getPlayField() {
		return this.hBoxPlayField;
	}

}
