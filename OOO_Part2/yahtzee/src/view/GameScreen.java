package view;

import java.util.Observable;
import java.util.Observer;
import domain.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameScreen extends BorderPane implements Observer {
	private Player player;

	public GameScreen(Player player) {
		setPlayer(player);
		Label gameLabel = new Label("Yahtzee");
		Label playerLabel = new Label(player.getUsername() + " playing");
		this.setTop(setLabelLayout(gameLabel));
		this.setBottom(setLabelLayout(playerLabel));
	}

	public Label setLabelLayout(Label label) {
		label.setStyle(
				"-fx-background-color: #FFFFFF; -fx-alignment: center; -fx-font: 25px Tahoma; -fx-font-weight: bold");
		label.setMinHeight(50);
		label.setMinWidth(900);
		return label;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	private void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

}
