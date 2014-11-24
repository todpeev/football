package soccersim.base;

import soccersim.team.Team;
import soccersim.team.Player;
import java.util.ArrayList;

/**
 * Handles the playing of a game.  
 * @author frank hadder, frank.hadder@gmail.com
 * @version 1.0
 */
public class GameHandler {

	/**
	 * Creates a game with the two given teams. The teams themselves will dictate
     * which side they are defending.
	 * @param teamOne the first team
	 * @param teamTwo the second team
	 */
	public GameHandler(Team teamOne, Team teamTwo) {
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
     */
    public void execute() {
		while (gameInProgress()) {
			executeStep();
		}
	}

    /**
     * Let all the players move one move.
     */
    public void executeStep() {
		// move the sideToGoFirst
		executeSide(sideToGoFirst);

		// switch sides
		sideToGoFirst = sideToGoFirst.getOppositeSide();
		
		// move the other side
		executeSide(sideToGoFirst);
    }

    /**
     * Move all the players of a given side.
     * @param side the side to move the players
     */
    private void executeSide(DefendingSide side) {
		for (Player player : field.getPlayers()) {
			if (player.getDefendingSide() == side) {
				MoveDirection bestMove = player.chooseBestMove();
				field.movePlayer(bestMove, player);
			}
            
            if (field.playerHasScored()) {
                // change the score
                score.incrementScore(field.getSideThatScored());

                // call team methods
                eastTeam.afterPoint();
                westTeam.afterPoint();
                if (field.getSideThatScored() == DefendingSide.East) {
                    eastTeam.afterPointWon();
                    westTeam.afterPointLost();
                } else {
                    eastTeam.afterPointLost();
                    westTeam.afterPointWon();
                }

                // if the game is over, call team methods
                if (!gameInProgress()) {
                    eastTeam.gameOver();
                    westTeam.gameOver();

                }

                // reset the field
                field.resetFieldOfPlay();
                break; // field is reset, this side is done moving.
			}
		}
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
