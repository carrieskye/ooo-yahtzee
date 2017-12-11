package view;

import java.util.Observable;
import java.util.Observer;
import domain.Category;
import domain.Category.LowerSectionCategory;
import domain.Category.SpecialCategory;
import domain.Category.UpperSectionCategory;
import domain.CategoryScore;
import domain.Game;
import domain.Player;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameScreen extends BorderPane implements Observer {
	private Game game;
	private Stage stage;
	private Player player;
	private GameScreenStrategy currentStrategy, playingStrategy, observingStrategy;
	private HBox hBoxGame, hBoxPlayer;
	private VBox vBoxPoints;
	private Label currentPlayerLabelBottom, gameLabel;
	private Button surrenderButton;
	TableColumn<CategoryScore, String> categoryCol;
	TableColumn<CategoryScore, Integer> scoreCol;
	private TableView<CategoryScore> scoreTable;

	public GameScreen(Game game, Player player, Player currentPlayer) {
		this.player = player;
		game.addObserver(this);
		playingStrategy = new PlayingStrategy(player);
		observingStrategy = new ObservingStrategy(player, currentPlayer);
		updateStrategy(currentPlayer);
		makeTop();
		makeCenter();
		makeBottom(currentPlayer);
		makeRight();
		setStyles();
	}

	public void makeTop() {
		hBoxGame = new HBox(5);
		gameLabel = new Label("Yahtzee");
		hBoxGame.getChildren().add(gameLabel);
		this.setTop(hBoxGame);
	}

	public void makeCenter() {
		vBoxPoints = new VBox(5);
	}

	public void makeBottom(Player currentPlayer) {
		hBoxPlayer = new HBox(20);
		currentPlayerLabelBottom = new Label(currentPlayer.getUsername() + " playing");
		surrenderButton = new Button("Surrender");
		surrenderButton.setOnAction(new SurrenderButtonHandler());
		hBoxPlayer.getChildren().add(currentPlayerLabelBottom);
		this.setBottom(hBoxPlayer);
	}

	public void makeRight() {
		scoreTable = new TableView<CategoryScore>();
		scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		categoryCol = new TableColumn<CategoryScore, String>("Category");
		categoryCol.setMinWidth(100);
		scoreCol = new TableColumn<CategoryScore, Integer>("Score");
		scoreCol.getStyleClass().add("score-column");
		scoreTable.getColumns().add(categoryCol);
		scoreTable.getColumns().add(scoreCol);
		ObservableList<CategoryScore> emptyCategoryScores = makeCategories();
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("points"));
		scoreTable.setItems(emptyCategoryScores);
		scoreTable.setFixedCellSize(25);
		scoreTable.prefHeightProperty()
				.bind(Bindings.size(scoreTable.getItems()).multiply(scoreTable.getFixedCellSize()).add(30));
		vBoxPoints.getChildren().add(scoreTable);
	}

	public ObservableList<CategoryScore> makeCategories() {
		ObservableList<CategoryScore> emptyCategoryScores = FXCollections.observableArrayList();
		for (Category category : UpperSectionCategory.values()) {
			emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(category));
		}
		emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(SpecialCategory.UPPER_SECTION_SCORE));
		emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(SpecialCategory.UPPER_SECTION_BONUS));
		emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(SpecialCategory.UPPER_SECTION_TOTAL));
		for (Category category : LowerSectionCategory.values()) {
			emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(category));
		}
		emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(SpecialCategory.LOWER_SECTION_TOTAL));
		emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(SpecialCategory.UPPER_SECTION_TOTAL));
		emptyCategoryScores.add(CategoryScore.getEmptyCategoryScore(SpecialCategory.GRAND_TOTAL));
		return emptyCategoryScores;
	}

	public void setStyles() {
		hBoxGame.getStyleClass().add("top-bottom-hbox");
		gameLabel.getStyleClass().add("game-label");
		vBoxPoints.getStyleClass().add("points-vbox");
		hBoxPlayer.getStyleClass().add("top-bottom-hbox");
		currentPlayerLabelBottom.getStyleClass().add("player-label");
	}

	public void start() {
		hBoxPlayer.getChildren().add(surrenderButton);
		playingStrategy.makeCenter();
		observingStrategy.makeCenter();
		currentStrategy.setStrategyCenter();
		this.setRight(vBoxPoints);
		player.getGameScreen().setMargin(vBoxPoints, new Insets(20));
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

		for (CategoryScore totalScore : player.getTotalScoresList()) {
			if (totalScore != null) {
				for (CategoryScore categoryScore : scoreTable.getItems()) {
					if (categoryScore.getCategory().equals(totalScore.getCategory())) {
						scoreTable.getItems().set(scoreTable.getItems().indexOf(categoryScore), totalScore);
					}
				}
			}

		}
	}

	public void endGame() {
		stage.close();
	}

	public Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.game = (Game) o;
		currentPlayerLabelBottom.setText(game.getCurrentPlayer().getUsername() + " playing");
		updateStrategy(game.getCurrentPlayer());
		updateScoreTable();
		currentStrategy.setStrategyCenter();
		currentStrategy.updateField(game.getCurrentPlayer());
	}

	class SurrenderButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			player.surrender();
			endGame();
		}
	}

}
