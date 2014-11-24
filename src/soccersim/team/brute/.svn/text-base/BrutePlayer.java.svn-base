package soccersim.team.brute;

import soccersim.team.Team;
import soccersim.base.MoveDirection;
import soccersim.team.Player;

import java.util.Random;

/**
 * Just goes towards the ball.
 * @author Tucker Balch	tucker@cc.gatech.edu, java port by frank hadder, frank.hadder@gmail.com
 * @version 1.0
 */
public class BrutePlayer extends Player {

    /**
     * Constructs a RandomPlayer
     * @param team the team the RandomPlayer plays with.
     * @param playerNumber the Number of the player.
     */
    public BrutePlayer(Team team, int playerNumber) {
		super(team, playerNumber);
	}

    /**
     * This strategy simply tries to move towards the ball and kick it to the east.
     * @return the best move to make.
     */
    public MoveDirection chooseBestMove() {
		MoveDirection bestMove;
		
		// assume we're kicking the ball to the west and defending the east
		switch(this.getBallDirection()) {
		case North : bestMove = MoveDirection.NorthEast; break;
		case NorthEast : bestMove = MoveDirection.NorthEast; break;
		case NorthWest : bestMove = MoveDirection.North; break;
		case West : bestMove = MoveDirection.West; break;
		case East :
            // just don't kick it east
            if (randomInt(2) == 0) {
                bestMove = MoveDirection.NorthEast;
            } else {
                bestMove = MoveDirection.SouthEast;
            }
            break;
        case South : bestMove = MoveDirection.SouthEast; break;
		case SouthEast : bestMove = MoveDirection.SouthEast; break;
		case SouthWest : bestMove = MoveDirection.South; break;
		default : bestMove = MoveDirection.North; break;
		}

		return bestMove;
	}

    private int randomInt(int n) {
        Random generator = new Random();
        return generator.nextInt(n);
    }

}
