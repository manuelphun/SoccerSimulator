package team;

/**
 * Created by Grant Cooksey on 8/23/15.
 * Purpose: An interface representing a team.
 *
 * When making your team, write it as if you are the team starting on the east side of the field.
 * The simulator will automatically switch directions for you.  Each player can be programmed differently
 * according to whatever strategy you wish to use.  To score a point to must push the ball across your
 * opponent's touchline.  Each player can only "see" the immediate 9 cells around it including the own
 * player's cell(localArea) and each player knows the direction the ball is in relation to it.  Assuming
 * the ball is not kicked, the direction will always lead to the cell directly to the side of it.  To kick
 * the ball, a player must be standing directly to the side of the ball in the direction of their opponent's
 * goal.  If a player is in the correct spot to kick the ball, ballDirection will be PLAYER.  If two players
 * from opposite teams are in a valid position to kick the ball and kick at the same time, the kick will be
 * random. When moving players, they cannot move through the ball, out of bounds, or through another player.
 * If the player gets stuck behind the ball or a teammate, the player functions must account for this and
 * and adjust directions.  If the ball gets suck the game will be called a draw.
 */
public interface Team {

    public static final int EMPTY = 0;
    public static final int GOAL = 1;
    public static final int BALL = 2;
    public static final int BOUNDARY = 3;
    public static final int TEAMMATE = 6;
    public static final int OPPONENT = 7;
    public static final int KICK = 9;
    public static final int DO_NOTHING = 10;

    public static final int NW = 0;
    public static final int N = 1;
    public static final int NE = 2;
    public static final int W = 3;
    public static final int PLAYER = 4;
    public static final int E = 5;
    public static final int SW = 6;
    public static final int S = 7;
    public static final int SE = 8;

    /**
     * Represents the strategy for player 1.
     *
     * @param localArea an array indexed from NW, N, NE, W, PLAYER, E,... etc
     *                   that describes what is the cell in that direction
     *                   of the player.  This will either be EMPTY, BALL, TEAMMATE,
     *                   OPPONENT, or BOUNDARY.  PLAYER will be EMPTY since a player
     *                   could do nothing and stay in the same spot.
     * @param ballDirection Direction of ball in relation to player.
     *                       Represented by a Cardinal direction.  Will be PLAYER
     *                      if in a valid kicking position.
     * @param x location of player on the x axis.
     * @param y location of player on the y axis.
     * @param ballX location of ball on the x axis.
     * @param ballY location of the ball on the y axis.
     *
     * @return an int representing direction to move or kick ball
     */
    int player1(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY);

    /**
     * Represents the strategy for player 2.
     *
     * @param localArea an array indexed from NW, N, NE, W, PLAYER, E,... etc
     *                   that describes what is the cell in that direction
     *                   of the player.  This will either be EMPTY, BALL, TEAMMATE,
     *                   OPPONENT, or BOUNDARY.  PLAYER will be EMPTY since a player
     *                   could do nothing and stay in the same spot.
     * @param ballDirection Direction of ball in relation to player.
     *                       Represented by a Cardinal direction. Will be PLAYER
     *                      if in a valid kicking position.
     * @param x location of player on the x axis.
     * @param y location of player on the y axis.
     * @param ballX location of ball on the x axis.
     * @param ballY location of the ball on the y axis.
     *
     * @return an int N, NE, E,.. etc. representing direction to move, KICK to kick the ball,
     *         or DO_NOTHING to do nothing
     */
    int player2(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY);

    /**
     * Represents the strategy for player 3.
     *
     * @param localArea an array indexed from NW, N, NE, W, PLAYER, E,... etc
     *                   that describes what is the cell in that direction
     *                   of the player.  This will either be EMPTY, BALL, TEAMMATE,
     *                   OPPONENT, or BOUNDARY.  PLAYER will be EMPTY since a player
     *                   could do nothing and stay in the same spot.
     * @param ballDirection Direction of ball in relation to player.
     *                       Represented by a Cardinal direction. Will be PLAYER
     *                      if in a valid kicking position.
     * @param x location of player on the x axis.
     * @param y location of player on the y axis.
     * @param ballX location of ball on the x axis.
     * @param ballY location of the ball on the y axis.
     *
     * @return an int representing direction to move or kick ball
     */
    int player3(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY);

    /**
     * Represents the strategy for player 4.
     *
     * @param localArea an array indexed from NW, N, NE, W, PLAYER, E,... etc
     *                   that describes what is the cell in that direction
     *                   of the player.  This will either be EMPTY, BALL, TEAMMATE,
     *                   OPPONENT, or BOUNDARY.  PLAYER will be EMPTY since a player
     *                   could do nothing and stay in the same spot.
     * @param ballDirection Direction of ball in relation to player.
     *                       Represented by a Cardinal direction. Will be PLAYER
     *                      if in a valid kicking position.
     * @param x location of player on the x axis.
     * @param y location of player on the y axis.
     * @param ballX location of ball on the x axis.
     * @param ballY location of the ball on the y axis.
     *
     * @return an int representing direction to move or kick ball
     */
    int player4(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY);

    /**
     * The team name.
     *
     * @return String representing the team name
     */
    String teamName();

    /**
     * This function is called per point before play begins.
     */
    void initializePoint();

    /**
     * This function is called after your team wins a point.
     */
    void wonPoint();

    /**
     * This is called when your team loses a point.
     */
    void lostPoint();

    /**
     * This function is called before the start of the game.
     */
    void initializeGame();

    /**
     * This function is called at the end of the game.
     */
    void gameOver();
}
