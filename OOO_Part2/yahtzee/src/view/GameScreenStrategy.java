package view;

import domain.Player;

public interface GameScreenStrategy {
	void initialize();

	void makeCenter();

	void updateField(Player currentPlayer);

}
