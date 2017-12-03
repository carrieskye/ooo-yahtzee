package view;

import domain.Player;

public interface GameScreenStrategy {
	void initialize();

	void setStrategyCenter();

	void makeCenter();

	void updateField(Player currentPlayer);

}
