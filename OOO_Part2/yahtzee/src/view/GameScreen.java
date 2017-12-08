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
	private Button surrenderButton;
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
		hBoxGame.getStyleClass().add("top-bottom-hbox");
		gameLabel = new Label("Yahtzee");
		gameLabel.getStyleClass().add("game-label");
		hBoxGame.getChildren().add(gameLabel);
		this.setTop(hBoxGame);
	}

	public void makeCenter() {
		hBoxPlayField = new HBox(5);
		hBoxPlayField.getStyleClass().add("playfield");
		vBoxPoints = new VBox(5);
		vBoxPoints.setAlignment(Pos.TOP_CENTER);
		vBoxPoints.setPadding(new Insets(15, 0, 0, 0));
		this.setCenter(hBoxPlayField);
	}

	public void makeBottom(Player currentPlayer) {
		hBoxPlayer = new HBox(20);
		hBoxPlayer.getStyleClass().add("top-bottom-hbox");
		currentPlayerLabelBottom = new Label(currentPlayer.getUsername() + " playing");
		currentPlayerLabelBottom.getStyleClass().add("player-label");
		surrenderButton = new Button("Surrender");
		surrenderButton.setOnAction(new SurrenderButtonHandler());
		hBoxPlayer.getChildren().addAll(currentPlayerLabelBottom, surrenderButton);
		this.setBottom(hBoxPlayer);
	}

	public void makeRight() {
		scoreTable = new TableView<CategoryScore>();
		scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		categoryCol = new TableColumn<CategoryScore, String>("Category");
		scoreCol = new TableColumn<CategoryScore, Integer>("Score");
		categoryCol.setMinWidth(125);
		scoreCol.setMinWidth(25);
		scoreTable.getColumns().add(categoryCol);
		scoreTable.getColumns().add(scoreCol);
		ObservableList<CategoryScore> emptyCategoryScores = FXCollections.observableArrayList();
		for (Category category : Category.values()) {
			emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(category));
		}
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("points"));
		scoreTable.setItems(emptyCategoryScores);
		vBoxPoints.getChildren().add(scoreTable);
	}

	public void start() {
		playingStrategy.makeCenter();
		observingStrategy.makeCenter();
		currentStrategy.setStrategyCenter();
		hBoxPlayField.getChildren().addAll(vBoxPoints);
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



	public void endGame() {
		// TODO
		// Use Game.getWinningPlayers() and Game.gameIsOver()
	}

	public HBox getPlayField() {
		return this.hBoxPlayField;
	}
	
	class SurrenderButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			endGame();
		}
	}

}
