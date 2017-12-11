package application;

import domain.Yahtzee;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameSuiteLauncher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		new Yahtzee(primaryStage);

	}

}
