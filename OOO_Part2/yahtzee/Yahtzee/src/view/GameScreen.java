package view;

import java.util.ArrayList;
import controller.ObservingStrategyController;
import controller.PlayerController;
import controller.PlayingStrategyController;
import domain.Category;
import domain.Category.LowerSectionCategory;
import domain.Category.SpecialCategory;
import domain.Category.UpperSectionCategory;
import domain.CategoryScore;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class GameScreen extends BorderPane {
	private String currentPlayer;
	private PlayerController controller;
	private Stage stage;
	private GameScreenStrategy currentStrategy, playingStrategy, observingStrategy;
	private HBox hBoxGame, hBoxPlayer;
	private VBox vBoxPoints;
	private Label currentPlayerLabelBottom, gameLabel;
	private Button surrenderButton;
	TableColumn<CategoryScore, String> categoryCol;
	TableColumn<CategoryScore, Integer> scoreCol;
	private TableView<CategoryScore> scoreTable;

	public GameScreen(String player, String currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.playingStrategy = new PlayingStrategy(player);
		this.observingStrategy = new ObservingStrategy(player, currentPlayer);
	}

	public void initialize(PlayerController controller) {
		this.controller = controller;
		controller.updateStrategy();
		controller.addControllersToScreen();

		makeTop();
		makeCenter();
		makeBottom(currentPlayer);
		makeRight();
		setStyles();
	}

	public void addController(PlayerController controller, PlayingStrategyController playingController,
			ObservingStrategyController observingController) {
		this.controller = controller;
		playingStrategy.addController(playingController);
		observingStrategy.addController(observingController);
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

	public void makeBottom(String currentPlayer) {
		hBoxPlayer = new HBox(20);
		currentPlayerLabelBottom = new Label(currentPlayer + " playing");
		surrenderButton = new Button("Surrender");
		controller.addSurrenderButtonHandler(surrenderButton);
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
		categoryCol.setSortable(false);
		scoreCol.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<CategoryScore, Integer>, ObservableValue<Integer>>() {

					@Override
					public ObservableValue<Integer> call(TableColumn.CellDataFeatures<CategoryScore, Integer> p) {
						return new SimpleIntegerProperty(p.getValue().getPoints()).asObject();
					}
				});

		scoreCol.setCellFactory(new Callback<TableColumn<CategoryScore, Integer>, TableCell<CategoryScore, Integer>>() {

			@Override
			public TableCell<CategoryScore, Integer> call(TableColumn<CategoryScore, Integer> p) {
				return new ButtonCell();

			}

		});

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

	@SuppressWarnings("static-access")
	public void start() {
		hBoxPlayer.getChildren().add(surrenderButton);
		playingStrategy.makeCenter();
		observingStrategy.makeCenter();
		currentStrategy.setStrategyCenter();
		this.setRight(vBoxPoints);
		this.setMargin(vBoxPoints, new Insets(20));
	}

	public void updateScoreTable(ArrayList<CategoryScore> categories, ArrayList<CategoryScore> totals) {
		for (CategoryScore currentCategoryScore : categories) {
			if (currentCategoryScore != null) {
				for (CategoryScore categoryScore : scoreTable.getItems()) {
					if (categoryScore.getCategory().equals(currentCategoryScore.getCategory())) {
						scoreTable.getItems().set(scoreTable.getItems().indexOf(categoryScore), currentCategoryScore);
					}
				}
			}
		}

		for (CategoryScore totalScore : totals) {
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

	public void setCurrentStrategy(GameScreenStrategy currentStrategy) {
		this.currentStrategy = currentStrategy;
	}

	public GameScreenStrategy getPlayingStrategy() {
		return this.playingStrategy;
	}

	public void setPlayingStrategy() {
		this.currentStrategy = playingStrategy;
	}

	public void setObservingStrategy() {
		this.currentStrategy = observingStrategy;
	}

	public GameScreenStrategy getCurrentStrategy() {
		return this.currentStrategy;
	}

	public GameScreenStrategy getObservingStrategy() {
		return this.observingStrategy;
	}

	public Label getCurrentPlayerLabelBottom() {
		return this.currentPlayerLabelBottom;
	}

	private class ButtonCell extends TableCell<CategoryScore, Integer> {
		final Button chooseButton = new Button("OK");

		ButtonCell() {
			this.chooseButton.getStyleClass().add("ok-button");
			 chooseButton.setVisible(false);
			chooseButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {
					System.out.println("test");
					// ...
				}
			});
		}

		@Override
		protected void updateItem(Integer i, boolean empty) {
			super.updateItem(i, empty);
			if (!empty) {
				setGraphic(chooseButton);
				setText(i.toString());
			}
		}
	}

}
