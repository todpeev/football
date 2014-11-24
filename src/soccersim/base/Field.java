package soccersim.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import soccersim.team.Player;
import soccersim.team.PlayerNeighborState;

/**
 * This represents the field that the soccer game is played on. It incorporates the field
 * information (like where the the goals and boundaries are) as well as the objects on the
 * field (the players and the ball). Through this class all the movement is achieved (the
 * ball being kicked, and the players moving around). The ball will not always move as
 * expected (direction and distance are "fuzzy" meaning they won't move the same every time).
  *
 * @author frank hadder, mofications by Todor Peev
 * @version 1.0
 */
public class Field {

    /**
     * Constructs a field with the given dimensions and players.
     * @param width the width of the field
     * @param height the height of the field
     * @param players the players playing on the field.
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public Field(int width, int height, List<Player> players) throws ClassNotFoundException, SQLException {
    	
		fieldHeight = height;
		fieldWidth = width;
		this.players = players;
		field = new FieldPositionState[height][width];
		// initialize everything.
		initDBConnection();
		initializeField();
		resetPlayers();
		resetBall();
		updatePlayerInformation();
	}
	
	/** Initialization Code **/

    /**
     * Setup the field to indicate where the goal states and boundaries are.
     */
    private void initializeField() {
		// setup each square on the field
		for (int rowIndex = 0; rowIndex < fieldHeight; rowIndex++) {
			for (int colIndex = 0; colIndex < fieldWidth; colIndex++) {
				if (isBoundaryState(rowIndex, colIndex)) {
					field[rowIndex][colIndex] = FieldPositionState.Boundary;
				} else if (isGoalState(rowIndex, colIndex)) {
					field[rowIndex][colIndex] = FieldPositionState.Goal; 
				} else {
					field[rowIndex][colIndex] = FieldPositionState.Empty;
				}
			}
		}
	}

    /**
     * Returns whether the specified position is a goal state.
     * @param rowIndex the rowIndex (y coordinate)
     * @param colIndex the colIndex (x coordinate)
     * @return whether or not this specified position is a goal state.
     */
    private boolean isGoalState(int rowIndex, int colIndex) {
		return colIndex == 0 || colIndex == fieldWidth - 1;
	}

    /**
     * Returns whether the specified position is a boundary.
     * @param rowIndex the rowIndex (y coordinate)
     * @param colIndex the colIndex (x coordinate)
     * @return whether or not this specified position is a boundary.
     */
    private boolean isBoundaryState(int rowIndex, int colIndex) {
		return rowIndex == 0 || rowIndex == fieldHeight - 1;
	}
	
	/** Reset Code **/

    /**
     * This method will reset the ball and players to their initial starting position.
     */
    public void resetFieldOfPlay() {
		resetPlayers();
		resetBall();
		playerHasScored = false;
	}

    /**
     * Resets the players to their starting positions.
     */
    private void resetPlayers() {
	
		int verticalPosition=0;
		int horizontalPositon=0;
		
		for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
			
			Player player = players.get(playerIndex);
			ArrayList<Integer> coordinates = new ArrayList<Integer>();
			HashMap<String, ArrayList<Integer>> position = player.getTeam().getPositions();
			coordinates = position.get(player.getPlayerPosition());
			
			verticalPosition = coordinates.get(0);
			if (player.getDefendingSide() == DefendingSide.East) {
				horizontalPositon = coordinates.get(1);
			} else {
				horizontalPositon = coordinates.get(2);
			}

			player.setXCoordinate(horizontalPositon);
			player.setYCoordinate(verticalPosition);
			players.set(playerIndex, player);
		
	}
}

    /**
     * Resets the ball to the center of the field.
     */
    private void resetBall() {
		ballXCoordinate = fieldWidth / 2;
		ballYCoordinate = fieldHeight / 2;
	}

	/** Update Players **/
	
	/**
	 * This method updates the local area and ball position of 
	 * all the players.
	 */
	public void updatePlayerInformation() {
		for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
			Player player = players.get(playerIndex);
			
			player.setBallDirection(getPlayerBallPosition(player));
			player.setPositionDirection(getPlayerOriginalPosition(player));
			player.setLocalArea(getPlayerLocalAreas(player));
			
			players.set(playerIndex, player);
		}
	}

    /**
     * Gets the direction of the ball relative to the specified player.
     * @param player the player to calculate relative ball position.
     * @return the current position of the player relative to the specified player post (defender, attacker,etc).
     */
	public MoveDirection getPlayerOriginalPosition(Player player){
    	double angle;
    	int origPositionY = player.getTeam().getPositions().get(player.getPlayerPosition()).get(0);
    	int origPositionX;
    	
    	if(player.getDefendingSide()==DefendingSide.East){
    		origPositionX = player.getTeam().getPositions().get(player.getPlayerPosition()).get(1);
    	} else {
    		origPositionX = player.getTeam().getPositions().get(player.getPlayerPosition()).get(2);
    	}
    	
    	angle = Math.atan2(
		player.getYCoordinate()- origPositionY,
		player.getXCoordinate()-origPositionX); 
    			
    	// get the polar coordinates in radians:
		if(angle>=0){
			angle = angle * (360.0 / (2.0 * Math.PI));
		} else {
			angle = (2*Math.PI+angle)* (360.0 / (2.0 * Math.PI));
		}
		
		// convert radians to degrees:
		if(angle<=90){
			angle = 90- angle;
		} else {
			angle = 360-(angle-90);
		}
		// convert polar coordinates to cardinal coordinates:
		MoveDirection result = MoveDirection.getDirectionFromDegrees(angle);

		
		if ( player.getDefendingSide() == DefendingSide.East) {
			result = result.flipDirectionHorizontal();
		}

		return result;
	}
	
    /**
     * Gets the direction of the ball relative to the specified player.
     * @param player the player to calculate relative ball position.
     * @return the position of the ball relative to the specified player.
     */
    private MoveDirection getPlayerBallPosition(Player player) {
    	double angle;
    	angle = Math.atan2(
		player.getYCoordinate()-ballYCoordinate,
		player.getXCoordinate()-ballXCoordinate); 
    			
    	// get the polar coordinates in radians:
		if(angle>=0){
			angle = angle * (360.0 / (2.0 * Math.PI));
		} else {
			angle = (2*Math.PI+angle)* (360.0 / (2.0 * Math.PI));
		}
		
		// convert radians to degrees:
		if(angle<=90){
			angle = 90- angle;
		} else {
			angle = 360-(angle-90);
		}
		// convert polar coordinates to cardinal coordinates:
		MoveDirection result = MoveDirection.getDirectionFromDegrees(angle);

		
		if ( player.getDefendingSide() == DefendingSide.East) {
			result = result.flipDirectionHorizontal();
		}

		return result;
	}

    /**
     * Gets the local area relative to the specified player.
     * @param player the player to get the local position around.
     * @return a hashtable of the specified players local area.
     */
    private Hashtable<MoveDirection, PlayerNeighborState> getPlayerLocalAreas(Player player) {
		Hashtable<MoveDirection, PlayerNeighborState> localArea = new Hashtable<MoveDirection, PlayerNeighborState>();
		
		for (MoveDirection direction : MoveDirection.values()) {
			int directionX = getXFrom(direction, player.getXCoordinate());
			int directionY = getYFrom(direction, player.getYCoordinate());
			
			PlayerNeighborState state = getPlayerNeighborStateAt(player.getDefendingSide(), directionX, directionY);
			localArea.put(direction, state);
		}
		
		// if needed, flip the local area
		//if (player.getAssumeEast() && player.getDefendingSide() == DefendingSide.West) {
			//Hashtable<MoveDirection, PlayerNeighborState> flippedLocalArea = new Hashtable<MoveDirection, PlayerNeighborState>();
			
			//for (MoveDirection direction : localArea.keySet()) {
				//flippedLocalArea.put(direction.flipDirectionHorizontal(), localArea.get(direction));
			//}
			
			//localArea = flippedLocalArea;
		//}
		
		return localArea;
	}

    /**
     * Gets the new x position given the current x coord and the direction
     * @param direction the direction to move on the field.
     * @param currentXCoordinate the current x coordinate
     * @return the new x coordinate
     */
    private int getXFrom(MoveDirection direction, int currentXCoordinate) {
		switch (direction) {
		case North : return currentXCoordinate;
		case NorthEast : return currentXCoordinate + 1;
		case East : return currentXCoordinate + 1;
		case SouthEast : return currentXCoordinate + 1;
		case South : return currentXCoordinate;
		case SouthWest : return currentXCoordinate - 1;
		case West : return currentXCoordinate - 1;
		case NorthWest : return currentXCoordinate - 1;
		default : throw new RuntimeException("Cannot calculate the x coordinate from " + direction);
		}
	}

    /**
     * Gets the new y position given the current y coord and the direction
     * @param direction the direction to move on the field.
     * @param currentYCoordinate the current y coordinate
     * @return the new y coordinate
     */
    private int getYFrom(MoveDirection direction, int currentYCoordinate) {
		switch (direction) {
		case North : return currentYCoordinate - 1;
		case NorthEast : return currentYCoordinate - 1;
		case East : return currentYCoordinate;
		case SouthEast : return currentYCoordinate + 1;
		case South : return currentYCoordinate + 1;
		case SouthWest : return currentYCoordinate + 1;
		case West : return currentYCoordinate;
		case NorthWest : return currentYCoordinate - 1;
		default : throw new RuntimeException("Cannot calculate the y coordinate from " + direction);
		}
	}

    /**
     * The PlayerNeighborState at the given field position.
     * @param playerSide the side the player is defending (used to indicate if the state contains
     * a teammate, or opponent player).
     * @param colIndex the colIndex (x coordinate)
     * @param rowIndex the rowIndex (y coordinate)
     * @return the PlayerNeighborState at the given field position.
     */
    private PlayerNeighborState getPlayerNeighborStateAt(DefendingSide playerSide, int colIndex, int rowIndex) {
		PlayerNeighborState result;
		
		if (isGoalState(rowIndex, colIndex)) {
			return PlayerNeighborState.Goal;
		} else if (isBoundaryState(rowIndex, colIndex)) {
			return PlayerNeighborState.Boundary;
		}
		
		if (ballXCoordinate == colIndex && ballYCoordinate == rowIndex) {
			result = PlayerNeighborState.Ball;
		} else if (rowIndex > 22 || rowIndex < 0 || colIndex > 79 || colIndex < 1){
			result = PlayerNeighborState.Boundary;
		}	else {
			switch (field[rowIndex][colIndex]) {
			case Empty : result = PlayerNeighborState.Empty; break;
			case Boundary: result = PlayerNeighborState.Boundary; break;
			case Goal : result = PlayerNeighborState.Goal; break;
			default : throw new RuntimeException("The field at (" + colIndex + "," + rowIndex + ") isn't instantiated creately.");
			}
		}
		
		for(Player player : players) {
			if (player.getXCoordinate() == colIndex && player.getYCoordinate() == rowIndex) {
				if (player.getDefendingSide() == playerSide) {
					result = PlayerNeighborState.Teammate;
				} else {
					result = PlayerNeighborState.Opponent;
				}
			}
		}
		
		return result;
	}
    
    Connection conn;
    
    public void initDBConnection() throws ClassNotFoundException, SQLException{
    	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    	conn = DriverManager.getConnection("jdbc:sqlserver://DCSRV1.nwtraders.msft;user=application_login;password=1234567;database=AdventureWorks2008");
    }
    
    public int getSkill(String skill, DefendingSide defendingSide, String position) throws SQLException, ClassNotFoundException{
    	
    	
    	String side;
    	if(defendingSide == DefendingSide.West){
			side = "west";
		} else {
			side = "east";
		}
    	
		Statement sta = conn.createStatement();
		String Sql = "select " + skill + " from [SoccerSimDb].[dbo].[player_skills] where defending_side = '" + side + "' AND position = '"+ position +"'";
		ResultSet rs = sta.executeQuery(Sql);
		int result = 0;
		if(rs.next()) {
			result = rs.getInt(skill);
		} 
		
		return result;

    }
    
    private boolean hasShot;
    
    public void setHasShot(boolean shot){
    	hasShot = shot; 
    }
    
    public boolean getHasShot(){
    	return hasShot;
    }
    
    Player shooter;
    public void setShooter(Player player){
    	shooter = player;
    }
    
    public Player getShooter(){
    	return shooter;
    }
	/** Move Player **/

    /**
     * Moves the player in the given direction. If the state the player is moving in to
     * contains the ball, the ball is kicked in that direction, if the state contains another
     * player (regardless of the players defending side) the move is ignored. All the players
     * get an updated state after the move is executed.
     * @param move the direction to move
     * @param player the player to move
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public void movePlayer(MoveDirection move, Player player) throws ClassNotFoundException, SQLException {


    	MoveDirection bestMove = move;
		// if the player is defending the west side, flip their move, since it was assumed in calculating the best move the player is kicking to the west
		if ( player.getDefendingSide() == DefendingSide.West) {
			bestMove = bestMove.flipDirectionHorizontal();
		}
		
		int newXCoordinate = getXFrom(bestMove, player.getXCoordinate());
		int newYCoordinate = getYFrom(bestMove, player.getYCoordinate());
		
		PlayerNeighborState state = getPlayerNeighborStateAt(DefendingSide.West, newXCoordinate, newYCoordinate);
		if (ballXCoordinate == newXCoordinate && ballYCoordinate == newYCoordinate && player.getHasBall() == false) {
			
			
			if(!(player.getPlayerPosition().equals("forward")) && !(player.getPlayerPosition().equals("striker"))){
				
				int chanceToKick;
				
				
				
				if(ballOwnedBy == null){
					chanceToKick = 100;
				} else {
					for(Player pl:players){
						if(pl.getHasBall()==true){
							if(pl.getDefendingSide()==player.getDefendingSide()){
								// do nothing
								return;
							}
						}
					}
					
					Random randomGenerator = new Random();
					chanceToKick = 100 - getSkill("dribble_skill", player.getDefendingSide(), player.getPlayerPosition()) + randomGenerator.nextInt(15);
				}
			
			
				if(chanceToKick >=50){
					if(kickBall(bestMove)) {
						
						for(Player singlePlayer: players){
							singlePlayer.setHasBall(false);
						}
						

							movePlayerTo(player, newXCoordinate, newYCoordinate);
						
						// someone just kicked the ball so we know there isn't any deadlocks
						playerUnableToMove = 0;
					}
				}
			} else {
				Random random = new Random();
				int chanceToOwn = getSkill("own_skill", player.getDefendingSide(), player.getPlayerPosition()) + random.nextInt(15);
				if(chanceToOwn>50){
					for(Player pl:players){
						pl.setHasBall(false);
						
					}
					player.setHasBall(true);
					setBallOwnedBy(player);
				} 
				
			}
			
		} else if (player.getHasBall() == true){
			
			if((player.getXCoordinate()>=50&&player.getDefendingSide()==DefendingSide.West)||(player.getXCoordinate()<=30&&player.getDefendingSide()==DefendingSide.East)){
				setHasShot(true);
				setShooter(player);
				Random random = new Random();
				int chanceToScore = getSkill("shoot_skill", player.getDefendingSide(), player.getPlayerPosition()) + random.nextInt(15);
				if(chanceToScore>=50){
					if(player.getDefendingSide()==DefendingSide.West){
						ballXCoordinate = 79;
						ballYCoordinate = 12;
						playerHasScored = true;
			    		sideThatScored = DefendingSide.West;
						
						
					} else {
						ballXCoordinate = 0;
						ballYCoordinate = 12;
						playerHasScored = true;
			    		sideThatScored = DefendingSide.East;
					}
				} else {
					if(player.getDefendingSide()==DefendingSide.West){
						ballXCoordinate = 78;
						ballYCoordinate = random.nextInt(12);
						
					} else {
						ballXCoordinate = 1;
						ballYCoordinate = random.nextInt(12);
						
					}
				}
				
				player.setHasBall(false);
				ballOwnedBy = null;
			} else {
			
				PlayerNeighborState ballNeighborState;
				if(player.getDefendingSide()==DefendingSide.West){
					ballNeighborState = getPlayerNeighborStateAt(DefendingSide.West, newXCoordinate+1, newYCoordinate);
					state = getPlayerNeighborStateAt(DefendingSide.West, newXCoordinate, newYCoordinate);
				
				} else {
				
					state = getPlayerNeighborStateAt(DefendingSide.East, newXCoordinate, newYCoordinate);
					ballNeighborState = getPlayerNeighborStateAt(DefendingSide.East, newXCoordinate-1, newYCoordinate);
				}
			
			
				if((state == PlayerNeighborState.Empty || state == PlayerNeighborState.Ball) && (ballNeighborState == PlayerNeighborState.Empty)){
				
					if(player.getDefendingSide()==DefendingSide.West){
						ballXCoordinate=newXCoordinate+1;
						ballYCoordinate=newYCoordinate;
						//System.out.println("west");
					} else {
						//System.out.println("east");
						ballXCoordinate=newXCoordinate-1;
						ballYCoordinate=newYCoordinate;
					}
					movePlayerTo(player, newXCoordinate, newYCoordinate);
				
				} else if (getPlayerNeighborStateAt(DefendingSide.East, player.getXCoordinate(), player.getYCoordinate()+1) == PlayerNeighborState.Empty
					&& getPlayerNeighborStateAt(DefendingSide.East, player.getXCoordinate()-1, player.getYCoordinate()+1) == PlayerNeighborState.Empty
					&& player.getDefendingSide()==DefendingSide.East){
				
					newXCoordinate = player.getXCoordinate();
					newYCoordinate = player.getYCoordinate()+1;
					ballXCoordinate=newXCoordinate-1;
					ballYCoordinate=newYCoordinate+1;
					movePlayerTo(player, newXCoordinate, newYCoordinate);
				} else if (getPlayerNeighborStateAt(DefendingSide.West, player.getXCoordinate(), player.getYCoordinate()-1) == PlayerNeighborState.Empty
					&& getPlayerNeighborStateAt(DefendingSide.West, player.getXCoordinate()-1, player.getYCoordinate()-1) == PlayerNeighborState.Empty
					&& player.getDefendingSide()==DefendingSide.East){
				
					newXCoordinate = player.getXCoordinate();
					newYCoordinate = player.getYCoordinate()-1;
					ballXCoordinate=newXCoordinate-1;
					ballYCoordinate=newYCoordinate-1;
					movePlayerTo(player, newXCoordinate, newYCoordinate);
				
				} else if(getPlayerNeighborStateAt(DefendingSide.West, player.getXCoordinate(), player.getYCoordinate()+1) == PlayerNeighborState.Empty
						&& getPlayerNeighborStateAt(DefendingSide.West, player.getXCoordinate()+1, player.getYCoordinate()+1) == PlayerNeighborState.Empty
						&& player.getDefendingSide()==DefendingSide.West){
				
					newXCoordinate = player.getXCoordinate();
					newYCoordinate = player.getYCoordinate()+1;
					ballXCoordinate=newXCoordinate+1;
					ballYCoordinate=newYCoordinate+1;
					movePlayerTo(player, newXCoordinate, newYCoordinate);
				} else if(getPlayerNeighborStateAt(DefendingSide.West, player.getXCoordinate(), player.getYCoordinate()-1) == PlayerNeighborState.Empty
						&& getPlayerNeighborStateAt(DefendingSide.West, player.getXCoordinate()+1, player.getYCoordinate()-1) == PlayerNeighborState.Empty
						&& player.getDefendingSide()==DefendingSide.West){
				
					newXCoordinate = player.getXCoordinate();
					newYCoordinate = player.getYCoordinate()-1;
					ballXCoordinate=newXCoordinate+1;
					ballYCoordinate=newYCoordinate-1;
					movePlayerTo(player, newXCoordinate, newYCoordinate);
				} else {
					unableToMove();
			}
			
			}
			
		} else  if (state == PlayerNeighborState.Empty) {
			
	    	int origPositionY = player.getTeam().getPositions().get(player.getPlayerPosition()).get(0);
	    	int origPositionX;
	    	
	    	if(player.getDefendingSide()==DefendingSide.East){
	    		origPositionX = player.getTeam().getPositions().get(player.getPlayerPosition()).get(1);
	    	} else {
	    		origPositionX = player.getTeam().getPositions().get(player.getPlayerPosition()).get(2);
	    	}
			
			if((newYCoordinate == origPositionY) && (newXCoordinate == origPositionX) && player.PlayerShouldMoveTowardsBall(getBallXCoordinate(), getFieldWidth())==false){
				// do nothing
			} else {
				movePlayerTo(player, newXCoordinate, newYCoordinate);
			}
			
			
		} else {
			unableToMove();
		}
		updatePlayerInformation();
	}
    
 
    public void unableToMove(){
		// do nothing if state = Opponent, Teammate, Boundary or Goal
		// check for deadlocks
		if (playerUnableToMove >= PLAYER_DEADLOCK) {
			try{
				nudgeBallToAvoidDeadlock();
			} catch (Exception StackOverflowError){
				resetFieldOfPlay();
				return;
			}
			
			
			playerUnableToMove = 0;
		} else {
			playerUnableToMove++;
		}
    }
    
    public boolean PlayerShouldMove(Player player){
    	boolean shouldMove = true;
    	if((player.getPlayerPosition().equals("left back") || player.getPlayerPosition().equals("right back")
    			|| player.getPlayerPosition().equals("sweeper") || player.getPlayerPosition().equals("stopper")) 
    			&& ballXCoordinate <= ((player.getDefendingSide()== DefendingSide.West)?0.30:1.0) * fieldWidth
    			&& ballXCoordinate >= ((player.getDefendingSide()== DefendingSide.West)?0.0:0.70) * fieldWidth ){
    		
    		return shouldMove;
    		
    	} else if ((player.getPlayerPosition().equals("right midfielder") || player.getPlayerPosition().equals("left midfielder")
    			|| player.getPlayerPosition().equals("defensive midfielder_right") || player.getPlayerPosition().equals("defensive midfielder_left")) 
    			&& ballXCoordinate <= 0.70 * fieldWidth
    			&& ballXCoordinate >= 0.30*fieldWidth ){
    				
    		return shouldMove;
    				
    	} else if ((player.getPlayerPosition().equals("striker") || player.getPlayerPosition().equals("forward"))){
    			
    		return shouldMove;
    		
    	} else {
    		shouldMove = false;
    		return shouldMove;
    	}
    	
    	
    }
    /**
     * Moves the player to the given coordinates
     * @param player the player to move
     * @param xCoordinate the x coordinate to which to move
     * @param yCoordinate the y coordinate to which to move
     */
    private void movePlayerTo(Player player, int xCoordinate, int yCoordinate) {
		// look for the appropriate player
		for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
			Player thisPlayer = players.get(playerIndex);
			if (thisPlayer == player) {
				thisPlayer.setXCoordinate(xCoordinate);
				thisPlayer.setYCoordinate(yCoordinate);
				players.set(playerIndex, thisPlayer);
				break; // we've found our player, we can stop looking
			}
		}
	}
	
	/**
	 * This method tries to kick the ball up to the max distance. It will stop
	 * when it hits a boundry. If the ball cannot be kicked, it will return 
	 * false (true otherwise). 
	 * @param direction the direction to kick the ball
	 * @return whether the ball has successfully been kicked.
	 */
	private boolean kickBall(MoveDirection direction) {
		boolean ballHasBeenKicked = false;

        int kickingDistance = GameHandler.BASE_KICK_DISTANCE + getRandomInt(6);

        for (int ballHasMoved = 0; ballHasMoved < kickingDistance; ballHasMoved++) {
            MoveDirection actualDirection = getNoisyMoveDirection(direction);
            int newXCoordinate = getXFrom(actualDirection, ballXCoordinate);
			int newYCoordinate = getYFrom(actualDirection, ballYCoordinate);
			
			PlayerNeighborState state = getPlayerNeighborStateAt(DefendingSide.East, newXCoordinate, newYCoordinate);
			
			if (state == PlayerNeighborState.Empty) {
				moveBallTo(newXCoordinate, newYCoordinate);
				ballHasBeenKicked = true;
			} else if (state == PlayerNeighborState.Goal) {
                moveBallTo(newXCoordinate, newYCoordinate);
                //playerHasScored = true;
                ballHasBeenKicked = true;
                break;
            } else {
				break; // the ball hit something... it isn't moving now.
			}
		}
		return ballHasBeenKicked;
	}

    /**
     * Gets a random integer between 0 and the given integer, n.
     * @param n the upperbound of the random number
     * @return a random integer between 0 and n.
     */
    private int getRandomInt(int n) {
        Random generator = new Random();
        return generator.nextInt(n);
    }

    /**
     * Gets the move direction adjusted to a "noise" factor. Most of the time the ball
     * will move in the given direction. Sometimes, however, the ball will move either
     * one 'tick' to the left or right of the given direction. For example, if the ball
     * is supposed to move North, it might also move NorthWest or NorthEast.
     * @param direction the direction the ball is supposed to move
     * @return the actual direction the ball will move
     */
    private MoveDirection getNoisyMoveDirection(MoveDirection direction) {
        int randomInt = getRandomInt(10);
        if (randomInt == 0) {
            return MoveDirection.getDirectionFromDegrees(direction.getStartDegrees()+45 );
        } else if (randomInt == 1) {
            return MoveDirection.getDirectionFromDegrees(direction.getStartDegrees()-45 );
        } else {
            return direction;
        }
    }

    /**
     * Moves the ball to the given coordinates.
     * @param xCoordinate the x coordinate
     * @param yCoordinate the y coordinate
     */
    private void moveBallTo(int xCoordinate, int yCoordinate) {
		ballXCoordinate = xCoordinate;
		ballYCoordinate = yCoordinate;
	}

    /**
     * If a deadlock is detected (the ball cannot move because it is trapped) then
     * this will "nudge" the ball in a random direction to free it.
     */
    
    int threshold = 0;
    private void nudgeBallToAvoidDeadlock() {
		// avoid stackoverflow error
    	if (threshold > 10){
			threshold =0;
			resetFieldOfPlay();
		}
    	MoveDirection nudgeDirection = randomMoveDirection();
		int newX = getXFrom(nudgeDirection, ballXCoordinate);
		int newY = getYFrom(nudgeDirection, ballYCoordinate);
		PlayerNeighborState state = getPlayerNeighborStateAt(DefendingSide.East, newX, newY);
		// keep going until we find a free state in the random direction
		while (state != PlayerNeighborState.Empty) {
			// if we run into a wall trying to find an empty state,
			//  just call this function again, and a new random direction 
			//  will be choosen.
			if (state == PlayerNeighborState.Boundary || state == PlayerNeighborState.Goal) {
				threshold++;
				nudgeBallToAvoidDeadlock();
				break;
			}
			newX = getXFrom(nudgeDirection, newX);
			newY = getYFrom(nudgeDirection, newY);
			state = getPlayerNeighborStateAt(DefendingSide.East, newX, newY);
		}
		if (state == PlayerNeighborState.Empty) {
			moveBallTo(newX, newY);
        }
	}

    /**
     * Returns a random MoveDirection.
     * @return a random MoveDirection.
     */
    private MoveDirection randomMoveDirection() {
		Random generator = new Random();
		int randomNumber = generator.nextInt(8);
		
		return MoveDirection.getDirection(randomNumber);
	}
	
	/** Properties & Fields **/
	
	private List<Player> players;

    /**
     * Gets a list of all the players (of both teams) on the field.
     * @return a list of all the players on the field (both teams).
     */
    public List<Player> getPlayers() {
		return players;
	}
	
	private FieldPositionState[][] field;
	
	// this will help us avoid a deadlock
	private int playerUnableToMove = 0;
	private final int PLAYER_DEADLOCK = 16;
	
	private int ballXCoordinate;

    /**
     * Gets the x coordinate of the ball.
     * @return the x coordinate of the ball.
     */
    public int getBallXCoordinate() {
        return ballXCoordinate;
    }

    private int ballYCoordinate;

    /**
     * Gets the y coordinate of the ball.
     * @return the y coordinate of the ball.
     */
    public int getBallYCoordinate() {
        return ballYCoordinate;
    }
	
    private int fieldWidth;
    
    public int getFieldWidth(){
    	return fieldWidth;
    }
	
	private int fieldHeight;
	
	private boolean playerHasScored = false;

    /**
     * Gets whether a player has scored or not.
     * @return whether or not a player has scored
     */
    public boolean playerHasScored() {
		return playerHasScored;
	}
	
	private DefendingSide sideThatScored;

    /**
     * Gets the defending side that scored the point.
     * @return the defending side that scored the point.
     */
    public DefendingSide getSideThatScored() {
		return sideThatScored;
	}
	
	/** Overrides **/
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		for (int rowIndex = 0; rowIndex < fieldHeight; rowIndex++) {
			for (int colIndex = 0; colIndex < fieldWidth; colIndex++) {
				result.append(getCharAt(rowIndex, colIndex));
			}
			result.append("\n");
		}
		
		return result.toString();
	}

    /**
     * Gets a character representation of the given coordinates.
     * @param rowIndex the rowIndex (y coordinate)
     * @param colIndex the colIndex (x coordinate)
     * @return a character representation of the given coordinates.
     */
	private char getCharAt(int rowIndex, int colIndex) {
		char result;
		
		if (ballXCoordinate == colIndex && ballYCoordinate == rowIndex) {
			result = '0';
		} else {
			switch (field[rowIndex][colIndex]) {
			case Empty : result = '-'; break;
			case Boundary: result = '#'; break;
			case Goal : result = '.'; break;
			default : throw new RuntimeException("The field at (" + colIndex + "," + rowIndex + ") isn't instantiated creately.");
			}
		}
		
		for(Player player : players) {
			if (player.getXCoordinate() == colIndex && player.getYCoordinate() == rowIndex) {
				switch (player.getDefendingSide()) {
				case East : result = '<'; break;
				case West : result = '>'; break;
				default : throw new RuntimeException("Player at (" + colIndex + "," + rowIndex + ") isn't instantiated creately.");
				}
			}
		}
		
		return result;
	}
	
	private Player ballOwnedBy = null;
	
	public Player getBallOwnedBy(){
		return ballOwnedBy;
	}
	
	public void setBallOwnedBy(Player player){
		ballOwnedBy = player;
	}
}
