package soccersim.team;

public enum PlayerNeighborState {
    /**
     * An empty state.
     */
    Empty,
    /**
     * A boundary (wall) state.
     */
    Boundary,
    /**
     * A goal state.
     */
    Goal,
    /**
     * The ball
     */
    Ball,
    /**
     * A member of the opposite team.
     */
    Opponent,
    /**
     * A member of the same team.
     */
    Teammate;
}
