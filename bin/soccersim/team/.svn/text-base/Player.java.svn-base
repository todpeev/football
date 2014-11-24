package soccersim.team;

import java.util.Hashtable;

import soccersim.base.DefendingSide;
import soccersim.base.MoveDirection;

/**
 * A player on the field. A player implements a MoveStrategy which actually dicates
 * the strategy the player plays with. By default, the player assumes it's the east
 * team and it's kicking a ball towards the west. The field will adjust this move
 * to the opposite direction if the player is actually on the west. This can be
 * overriden.
 *
 * @author frank hadder
 * @version 1.0
 */
public abstract class Player implements MoveStrategy {
	
	/**
	 * Constructs a new Player on a given team with the given player number.
     * @param team the team for which the player is playing.
     * @param playerNumber the number of the player.
     */
	public Player(Team team, int playerNumber) {
		this.team = team;
		this.playerNumber = playerNumber;
		defendingSide = team.getDefendingSide();
		initializeLocalArea();
	}
	
	private void initializeLocalArea() {
		localArea = new Hashtable<MoveDirection, PlayerNeighborState>();
		for (MoveDirection direction : MoveDirection.values()) {
			localArea.put(direction, PlayerNeighborState.Empty);
		}
	}
	
	private int playerNumber = 0;
	
	/**
	 * Gets the playerNumber for the player. This is used to differentiate players.
	 * @return the player number
	 */
	public int getPlayerNumber() {
		return playerNumber;
	}
	
	protected Hashtable<MoveDirection, PlayerNeighborState> localArea; 
	
	/**
	 * Gets the PlayerNeighborState at the given direction.
	 * @param direction the MoveDirection to get
	 * @return the PlayerNeighborState at the given direction
	 * @throws RuntimeException if the direction is unknown.
	 */
	public PlayerNeighborState getLocalAreaAt(MoveDirection direction) {
		if (localArea.containsKey(direction)) {
			return localArea.get(direction);
		}
		throw new RuntimeException("Unknown direction " + direction);
	}
	
	/**
	 * Gets a hashtable of the PlayerNeighborStates at the various 
	 * MoveDirection.
	 * @return a hashtable of the PlayerNeighborStates at the various 
	 * MoveDirections.
	 */
	public Hashtable<MoveDirection, PlayerNeighborState> getLocalArea() {
		return localArea;
	}
	
	/**
	 * Sets the PlayerNeighborState at the given direction.
	 * @param direction the MoveDirection to set
	 * @param state the state at the given MoveDirection
	 * @throws RuntimeException if the direction is unknown.
	 */
	public void setLocalAreaAt(MoveDirection direction, PlayerNeighborState state) {
		if (localArea.containsKey(direction)) {
			localArea.put(direction, state);
		}
		throw new RuntimeException("Unknown direction " + direction);
	}
	
	/**
	 * Gets a hashtable of the PlayerNeighborStates at the various 
	 * MoveDirection.
	 * @param localArea a hashtable of the PlayerNeighborStates at the various 
	 * MoveDirections.
	 */
	public void setLocalArea(Hashtable<MoveDirection, PlayerNeighborState> localArea) {
		this.localArea = localArea;
	}
	
	private int xCoordinate = 0;
	
	/**
	 * Gets the x coordinate for the player.
	 * @return the x coordinate value
	 */
	public int getXCoordinate() {
		return xCoordinate;
	} 
	
	/**
	 * Sets the x coordinate for the player.
	 * @param value the new x coordinate value
	 */
	public void setXCoordinate(int value) {
		xCoordinate = value;
	}
	
	private int yCoordinate = 0;
	
	/**
	 * Gets the y coordinate for the player.
	 * @return the y coordinate value
	 */
	public int getYCoordinate() {
		return yCoordinate;
	}
	
	/**
	 * Sets the y coordinate for the player.
	 * @param value the new y coordinate value
	 */
	public void setYCoordinate(int value) {
		yCoordinate = value;
	}
	
	private MoveDirection ballDirection;
	
	/**
	 * Gets the direction of the ball for the player.
	 * @return the ball direction
	 */
	public MoveDirection getBallDirection() {
		return ballDirection;
	}
	
	/**
	 * Sets the direction of the ball for the player
	 * @param value the new ball direction value
	 */
	public void setBallDirection(MoveDirection value) {
		ballDirection = value;
	}
	
	private Team team;
	
	/**
	 * Gets the team for which the player is playing.
	 * @return the team for which the player is playing
	 */
	public Team getTeam() {
		return team;
	}
	
	private DefendingSide defendingSide;
	
	/**
	 * Gets the side the player is defending.
	 * @return the side the player is defending
	 */
	public DefendingSide getDefendingSide() {
		return defendingSide;
	}

    private boolean assumeEast = true;

    /**
     * By default, the player assumes he is defending the east side.
     * This can be overriden if an individual strategy dictates it.
     * @return whether or not this player assumes it's on the east side.
     */
    public boolean getAssumeEast() {
        return assumeEast;
    }

    @Override
	public String toString() {
		StringBuilder result = new StringBuilder();

        result.append(team.getUniqueName()).append(" ");
		result.append(String.valueOf(playerNumber));
		result.append("\nLocal Area: ");
		
		for (MoveDirection direction : localArea.keySet()) {
			if (localArea.get(direction) != PlayerNeighborState.Empty) {
                result.append(direction).append(":").append(localArea.get(direction)).append(" ");
			}
		}
        result.append("\nLocation: (").append(xCoordinate).append(",").append(yCoordinate).append(")");

        result.append("\nBallDirection: ").append(ballDirection).append("\n");
		
		return result.toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object.equals(this)) {
			return true;
		}
		if (!(object instanceof Player)) {
			return false;
		}
		Player testPlayer = (Player)object;
		return (this.team.getUniqueName() + " " + this.playerNumber)
                .equals(testPlayer.team.getUniqueName() + " " + testPlayer.playerNumber);
	}
}
