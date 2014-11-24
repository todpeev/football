package soccersim.team.brute;

import java.util.ArrayList;
import java.util.HashMap;

import soccersim.base.DefendingSide;
import soccersim.team.Player;
import soccersim.team.Team;

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
			newPlayers.add(new BrutePlayer(this, playerIndex + 1,positions[playerIndex]));
			
			ArrayList<Integer> coord = new ArrayList<Integer>();
			coord.add(positionsCoordinatesY[playerIndex]);
			coord.add(positionsCoordinatesEastX[playerIndex]);
			coord.add(positionCoordinatesWestX[playerIndex]);
			positionsLocations.put(positions[playerIndex], coord);
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
    
    public HashMap<String, ArrayList<Integer>> getPositions(){
    	return positionsLocations;
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
