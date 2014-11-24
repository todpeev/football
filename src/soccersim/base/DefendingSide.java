package soccersim.base;

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
