package soccersim.base;

/**
 * This enum indicates which side a team is defending. If a team is defending the
 * east side, they are trying to kick the ball to the west side and visa versa.
 * @author frank hadder, frank.hadder@gmail.com
 * @version 1.0
 */
public enum DefendingSide {
    /**
     * Indicates the team is defending the east side.
     */
    East,
    /**
     * Indicates the team is defending the west side.
     */
    West;

    /**
     * Gets the opposite side of the given direction. For example,
     * DefendingSide.East.getOppositeSide() returns DefendingSide.West.
     * @return the opposite defending side.
     */
    public DefendingSide getOppositeSide() {
		switch (this) {
		case East : return West;
		case West : return East;
		default : throw new RuntimeException("Unknown side " + this);
		}
	}
}
