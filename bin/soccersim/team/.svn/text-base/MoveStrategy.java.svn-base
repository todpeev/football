package soccersim.team;

import soccersim.base.MoveDirection;

/**
 * Contains a strategy for moving.
 * 
 * @author frank hadder, frank.hadder@gmail.com
 * @version 1.0
 */
public interface MoveStrategy {

	/** 
	 * Decides the best move to make and returns that move. 
	 * 
	 * To kick the ball, return the direction of the ball. The ball must 
	 * be within one square of the player for the kick to succeed. 
	 * 
	 * If the player tries to move into an occupied square (one occupied 
	 * by a teammate, opponent, goal state, or field boundry) then the 
	 * move will not succeed. No error will be thrown, however.
	 *   
	 * @return the best move to make.
	 */
	MoveDirection chooseBestMove();
	
}
