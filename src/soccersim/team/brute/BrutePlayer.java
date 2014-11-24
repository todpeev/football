package soccersim.team.brute;

import soccersim.team.Team;
import soccersim.base.DefendingSide;
import soccersim.base.Field;
import soccersim.base.MoveDirection;
import soccersim.team.Player;

import java.util.Random;

public class BrutePlayer extends Player {

    /**
     * Constructs a RandomPlayer
     * @param team the team the RandomPlayer plays with.
     * @param playerNumber the Number of the player.
     */
    public BrutePlayer(Team team, int playerNumber, String position) {
		super(team, playerNumber, position);
	}

    /**
     * This strategy simply tries to move towards the ball and kick it to the east.
     * @return the best move to make.
     */
    public MoveDirection chooseBestMove(Field field) {
		MoveDirection bestMove;
		MoveDirection coordinates;
		if(this.getHasBall() == true){
			bestMove = MoveDirection.West;
		} else {
			if(PlayerShouldMoveTowardsBall(field.getBallXCoordinate(), field.getFieldWidth())){
				coordinates = this.getBallDirection();
			} else {
				coordinates = this.getPositionDirection();
			}
			// assume we're kicking the ball to the west and defending the east, for the West player the directions are changed in Field.movePlayer()
			switch(coordinates) {
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
		}
	
	

		return bestMove;
	}

    
    public boolean PlayerShouldMoveTowardsBall(int ballXCoordinate, int fieldWidth){
    	boolean shouldMove = true;
    	if((getPlayerPosition().equals("left back") || getPlayerPosition().equals("right back")
    			|| getPlayerPosition().equals("sweeper") || getPlayerPosition().equals("stopper")) 
    			&& ballXCoordinate <= ((getDefendingSide()== DefendingSide.West)?0.30:1.0) * fieldWidth
    			&& ballXCoordinate >= ((getDefendingSide()== DefendingSide.West)?0.0:0.70) * fieldWidth ){
    		
    		return shouldMove;
    		
    	} else if ((getPlayerPosition().equals("right midfielder") || getPlayerPosition().equals("left midfielder")
    			) 
    			&& ballXCoordinate < 0.70 * fieldWidth
    			&& ballXCoordinate > 0.30 * fieldWidth ){
    				
    		return shouldMove;
    				
    	} else if((getPlayerPosition().equals("defensive midfielder_right") || getPlayerPosition().equals("defensive midfielder_left"))
    			&& ballXCoordinate < ((getDefendingSide()== DefendingSide.West)?0.40 * fieldWidth:0.7* fieldWidth)
    			&& ballXCoordinate > ((getDefendingSide()== DefendingSide.West)?0.30 * fieldWidth:0.6* fieldWidth)){
    			 return shouldMove;
    		
    	} else if ((getPlayerPosition().equals("striker") || getPlayerPosition().equals("forward"))&&
    			ballXCoordinate >= ((getDefendingSide()== DefendingSide.West)?0.0:0.0) * fieldWidth
    			&& ballXCoordinate <= ((getDefendingSide()== DefendingSide.West)?1.0:1.0) * fieldWidth){
    			
    		return shouldMove;
    		
    	} else {
    		shouldMove = false;
    		return shouldMove;
    	}
    	
    	
    }
    private int randomInt(int n) {
        Random generator = new Random();
        return generator.nextInt(n);
    }

    
}
