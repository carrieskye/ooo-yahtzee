package controller;

import domain.Game;
import domain.Player;
import javafx.scene.layout.VBox;
import view.GameScreen;
import view.GameScreenStrategy;

public class ObservingStrategyController extends PlayerController {

	public ObservingStrategyController(Game game, Player player, GameScreen screen, GameScreenStrategy strategy ) {
		super(game, player, screen);
		this.screen = screen;
	}

	public void setCenter(VBox vbox){
		screen.setCenter(vbox);
	}


}
