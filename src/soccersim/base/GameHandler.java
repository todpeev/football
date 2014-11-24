package soccersim.base;

import soccersim.team.Team;
import soccersim.team.Player;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles the playing of a game.  
 * @author frank hadder, mofications by Todor Peev
 * @version 1.0
 */
public class GameHandler {

	/**
	 * Creates a game with the two given teams. The teams themselves will dictate
     * which side they are defending.
	 * @param teamOne the first team
	 * @param teamTwo the second team
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public GameHandler(Team teamOne, Team teamTwo) throws ClassNotFoundException, SQLException {
		score = new Score();

        if (teamOne.getDefendingSide() == DefendingSide.East) {
            eastTeam = teamOne;
            westTeam = teamTwo;
        } else {
            eastTeam = teamTwo;
            westTeam = teamOne;
        }
        eastTeam.initializeGame();
        westTeam.initializeGame();

        ArrayList<Player> players = new ArrayList<Player>();
		players.addAll(teamOne.getPlayers());
		players.addAll(teamTwo.getPlayers());
        
        field = new Field(FIELD_WIDTH, FIELD_HEIGHT, players);
		
		initializePlayers();
	}
	
	private void initializePlayers() {
		field.updatePlayerInformation();
	}

    /**
     * Let the teams move until the game is over.
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public void execute() throws ClassNotFoundException, SQLException {
		while (gameInProgress()) {
			executeStep();
		}
	}

    /**
     * Let all the players move one move.
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public void executeStep() throws ClassNotFoundException, SQLException {
		// move the sideToGoFirst
		executeSide(sideToGoFirst);

		// switch sides
		sideToGoFirst = sideToGoFirst.getOppositeSide();
	   //	if(field.playerHasScored()){
    		//return;
    	//}
		// move the other side
		executeSide(sideToGoFirst);
    }

    /**
     * Move all the players of a given side.
     * @param side the side to move the players
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    private void executeSide(DefendingSide side) throws ClassNotFoundException, SQLException {
 
    	for (Player player : field.getPlayers()) {
			if (player.getDefendingSide() == side) {
				MoveDirection bestMove = player.chooseBestMove(field);
				field.movePlayer(bestMove, player);
			}
            

		}
	}
	
    public boolean hasScored(){

    	return field.playerHasScored();
    }
    
    public void fieldReset(){
    	score.incrementScore(field.getSideThatScored());
    	field.resetFieldOfPlay();
    }


    /** Properties & Fields **/

    private Team eastTeam;
    private Team westTeam;

    private Score score;

    /**
     * Gets the score for the current game.
     * @return the score for the current game.
     */
    public Score getScore() {
		return score;
	}

    /**
     * Whether or not the game is in progress.
     * @return whether or not the game is in progress.
     */
    public boolean gameInProgress() {
		return score.getHighestScore() < POINTS_FOR_WIN;
	}
	
	private Field field;

    /**
     * Gets the field for the current game.
     * @return the Field for the current game.
     */
    public Field getField() {
		return field;
	}
	
	private DefendingSide sideToGoFirst = DefendingSide.East;
	
	/** Constants **/

    /**
     * The width of the field.
     */
    public static final int FIELD_WIDTH = 80;
    /**
     * The height of the field.
     */
    public static final int FIELD_HEIGHT = 23;
    /**
     * The amount of points needed for a win.
     */
    public static final int POINTS_FOR_WIN = 7;
    /**
     * The base line distance a ball can be kicked.
     */
    public static final int BASE_KICK_DISTANCE = 10;
}
