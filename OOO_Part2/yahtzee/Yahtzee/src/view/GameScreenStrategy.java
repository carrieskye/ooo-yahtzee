package view;

import java.util.ArrayList;

import controller.PlayerController;
import domain.ThrownDice;

public interface GameScreenStrategy {
	void initialize();

	void setStrategyCenter();

	void makeCenter();

	void updateField(String currentPlayer, ArrayList<ThrownDice> thrownDice, ArrayList<ThrownDice> pickedDice);

	void addController(PlayerController playingController);

}
