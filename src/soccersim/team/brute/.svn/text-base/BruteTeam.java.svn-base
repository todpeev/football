package soccersim.team.brute;

import java.util.ArrayList;

import soccersim.base.DefendingSide;
import soccersim.team.Player;
import soccersim.team.Team;

/**
 * Brute force soccer. All players push the ball to the goal.
 * @author Tucker Balch	tucker@cc.gatech.edu, java port by frank hadder, frank.hadder@gmail.com
 * @version 1.0
 */
public class BruteTeam extends Team {

    /**
     * Creates a BruteTeam defending the given side.
     * @param side the side this team is defending
     */
    public BruteTeam(DefendingSide side) {
		super(side);
		this.players = initializePlayers();
	}
	
	private ArrayList<Player> initializePlayers() {
		ArrayList<Player> newPlayers = new ArrayList<Player>(NUMBER_OF_PLAYERS);
		
		for (int playerIndex = 0; playerIndex < NUMBER_OF_PLAYERS; playerIndex++) {
			newPlayers.add(new BrutePlayer(this, playerIndex + 1));
		}
		
		return newPlayers;
	}

    /**
     * Returns the team name.
     * @return the team name.
     */
    public String getName() {
		return "The Brutal Brutes";
	}

	@Override
	public void gameOver() {}

	@Override
	public void initializeGame() {}

	@Override
	public void afterPoint() {}

	@Override
	public void afterPointLost() {}

	@Override
	public void afterPointWon() {}

}
