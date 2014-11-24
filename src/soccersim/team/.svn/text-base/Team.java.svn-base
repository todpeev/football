package soccersim.team;

import java.util.List;

import soccersim.base.DefendingSide;

/**
 * The base class of a Team. This contains the base methods that all 
 * teams must have.
 * @author frank hadder, frank.hadder@gmail.com
 * @version 1.0
 */
public abstract class Team {

    /**
     * The number of players a team can have.
     */
    public static final int NUMBER_OF_PLAYERS = 4;
	
	/**
	 * Constructs a Team object that is defending one side.
	 * @param side the side that this team is currently defending.
	 */
	public Team(DefendingSide side) {
		this.side = side;
	}
	
	/**
	 * This method is called before the game begins.
	 */
	public abstract void initializeGame();
	
	/**
	 * This method is called after the game ends.
	 */
	public abstract void gameOver();
	
	/**
	 * This method is called after your team has scored a point.
	 */
	public abstract void afterPointWon();
	
	/**
	 * This method is called after the opposing team has scored a point.
	 */
	public abstract void afterPointLost();
	
	/**
	 * This method is called after a point is scored (regardless of side).
	 */
	public abstract void afterPoint();
	
	private DefendingSide side; 
	
	/**
	 * Get the current defending side.
	 * @return side
	 */
	public DefendingSide getDefendingSide() {
		return side;
	}
	
	/**
	 * The name of this team.
     * @return the name of this team.
     */
	public abstract String getName();

    /**
     * All the players on this team.
     */
    protected List<Player> players;

    /**
     * Gets all the players on this team.
     * @return the players for this team
     */
    public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets a unique name for this team. It simply returns the name of this team
	 * and the side on which the team is playing.
	 * @return the unique name of this team.
	 */
	public String getUniqueName() {
		return getName() + " {" + side + "}";
	}
	
	/**
	 * Returns the unique name of the team.
	 * @return the unique name of the team.
	 */
	public String toString() {
		return this.getUniqueName();
	}
	
	/**
	 * Returns the hash code of the unique name of this team.
	 * @return the hash code of the unique name of this team.
	 */
	public int hashCode() {
		return this.getUniqueName().hashCode();
	}
}
