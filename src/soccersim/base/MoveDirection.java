package soccersim.base;

/**
 * Eight cardinal directions. 
 * @author frank hadder, mofications by Todor Peev
 * @version 1.0
 */
public enum MoveDirection {
    /**
     * Indicates the North direction.
     */
    East (67.5, 112.5),//East
    /**
     * Indicates the NorthEast direction.
     */
    NorthEast (22.5, 67.5),
    /**
     * Indicates the East direction.
     */
    North (337.5, 22.5),//North
    /**
     * Indicates the SouthEast direction.
     */
    NorthWest (292.5, 337.5),//NorthWest
    /**
     * Indicates the South direction.
     */
    West (247.5, 292.5),//West
    /**
     * Indicates the SouthWest direction.
     */
    SouthWest (202.5, 247.5),//SouthWest
    /**
     * Indicates the West direction.
     */
    South (157.5, 202.5),//South
    /**
     * Indicates the NorthWest direction.
     */
    SouthEast (112.5, 157.5);//SouthEast
	
	MoveDirection(double startDegrees, double endDegrees) {
		this.startDegrees = startDegrees;
		this.endDegrees = endDegrees;
	}
	
	/**
	 * Gets a move direction from an integer value. Expects an integer between 0-7. This is
     * inteded to help get a random MoveDirection.
	 * @param value an integer between 0-7.
	 * @return the MoveDirection that corresponds to the given integer value
	 */
	public static MoveDirection getDirection(int value) {
		switch (value % 8) {
		case 0 : return North;
		case 1 : return NorthEast;
		case 2 : return East;
		case 3 : return SouthEast;
		case 4 : return South;
		case 5 : return SouthWest;
		case 6 : return West;
		case 7 : return NorthWest;
		default : throw new RuntimeException("Invalid integer value. Expects a number between 1-8."); 
		}
	}
	
	/**
	 * Gets the cardinal direction at the specified degrees. For example, 90 degrees will return
     * MoveDirection.North.
	 * @param degrees the amount of degrees to translate into the cardinal direction.
	 * @return the cardinal direction at the specified degrees.
	 */
	public static MoveDirection getDirectionFromDegrees(double degrees) {
		while (degrees < 0) {
			degrees += 360;
		}
		while (degrees > 360) {
			degrees -= 360;
		}
		for (MoveDirection direction : MoveDirection.values()) {
			// check for special case of east (because it goes from 360 to 0)
			if (direction == North) {
				if ((degrees >= direction.startDegrees && degrees <= 360) 
						|| degrees >= 0 && degrees < direction.endDegrees) {
					return direction;
				}
			} else if (degrees >= direction.startDegrees && degrees < direction.endDegrees) {
				return direction;
			}
		}
		throw new RuntimeException("There is no MoveDirection at degrees " + degrees);
	}
	
	/**
	 * Gets the cardinal direction at the specified radians. For example, 3.14 radians will return
     * MoveDirection.West.
	 * @param radians the amount of radians to translate into the cardinal direction.
	 * @return the cardinal direction at the specified radian.
	 */
	public static MoveDirection getDirectionFromRadians(double radians) {
		return getDirectionFromDegrees(radians * (360.0 / Math.PI));
	}
	
	/**
	 * This will return the direction flipped horizontally. For example,
	 * NorthEast will be fliped to NorthWest.
	 * @return the horizontally flipped direction.
	 */
	public MoveDirection flipDirectionHorizontal() {
		switch (this) {
		case North : return North;
		case NorthEast : return NorthWest;
		case East : return West;
		case SouthEast : return SouthWest;
		case South : return South;
		case SouthWest : return SouthEast;
		case West : return East;
		case NorthWest : return NorthEast;
		default : throw new RuntimeException("Cannot horizontally flip the direction " + this);
		}
	}
	
	/**
	 * This will return the direction flipped Vertically. For example,
	 * NorthEast will be fliped to SouthEast.
	 * @return the vertically flipped direction.
	 */
	public MoveDirection flipDirectionVertical() {
		switch (this) {
		case North : return South;
		case NorthEast : return SouthEast;
		case East : return East;
		case SouthEast : return NorthEast;
		case South : return North;
		case SouthWest : return NorthWest;
		case West : return West;
		case NorthWest : return SouthWest;
		default : throw new RuntimeException("Cannot vertically flip the direction " + this);
		}
	}
	
	private double startDegrees;

    /**
     * This will return the starting degrees of this MoveDirection. For example,
     * MoveDirection.North.getStartDegrees() will return 67.5 degrees.
     * @return the degrees that start the MoveDirection.
     */
    public double getStartDegrees() {
		return startDegrees;
	}
	
	private double endDegrees;

    /**
     * This will return the ending degrees of this MoveDirection. For example,
     * MoveDirection.North.getEndDegrees() will return 112.5 degrees. 
     * @return the degrees that end the MoveDirection.
     */
    public double getEndDegrees() {
		return endDegrees;
	}
}
