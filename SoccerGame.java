import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Grant Cooksey on 8/25/15.
 * Purpose: Maintains the Soccer Simulation.  Provides methods to update the game based on the player algorithms.
 */
public class SoccerGame {

    protected static final int BATCH = 1;
    protected static final int GUI = 2;

    protected static final int EMPTY = 0;
    protected static final int GOAL = 1;
    protected static final int BALL = 2;
    protected static final int BOUNDARY = 3;
    protected static final int TEAMMATE = 6;
    protected static final int OPPONENT = 7;
    protected static final int KICK_E = 9;
    protected static final int KICK_N = 10;
    protected static final int KICK_W = 11;
    protected static final int KICK_S = 12;
    protected static final int KICK_NE = 13;
    protected static final int KICK_SE = 14;
    protected static final int KICK_SW = 15;
    protected static final int KICK_NW = 16;

    protected static final int NW = 0;
    protected static final int N = 1;
    protected static final int NE = 2;
    protected static final int W = 3;
    protected static final int PLAYER = 4;
    protected static final int E = 5;
    protected static final int SW = 6;
    protected static final int S = 7;
    protected static final int SE = 8;
    protected static final int PLAYER_EAST = 1;
    protected static final int PLAYER_WEST = 2;

    protected static final int MAX_X = 58;
    protected static final int MAX_Y = 30;
    protected static final int START_DIST_FROM_MIDDLE = 13;
    protected final int SOCCER_BALL = 3;

    private static int[][] grid;
    private static int[][] eastTeamLoc;
    private static int[][] westTeamLoc;
    private int[] localArea;
    private int ballDirection;
    private int ballX;
    private int ballY;
    private int moveX;
    private int moveY;
    private int playerIndex;
    private boolean ballMoved;

    /* Constructor */
    public SoccerGame() {
        grid = new int[MAX_X][MAX_Y];
        eastTeamLoc = new int[4][2];
        westTeamLoc = new int[4][2];
        localArea = new int[9];
    }

    /**
     * Sets up the game from the start.  Players and ball are aligned
     * in their starting positions and the game is initialized.  This
     * should be called at the start of the game and between points.
     * If it is between points, the game will be started automatically.
     */
    public void setGame() {
        /* clears grid */
        for (int i = 0; i < MAX_X; ++i) {
            Arrays.fill(grid[i], 0);
        }

        /* places players on the field and updates grid */
        int distY, eastDistX, westDistX;
        for (int i = 0; i < 4; ++i) {
            distY = (i * 6) + 5;
            eastDistX = MAX_X / 2 + START_DIST_FROM_MIDDLE;
            westDistX = (MAX_X / 2 - START_DIST_FROM_MIDDLE) - 1;
            grid[westDistX][distY] = PLAYER_WEST;
            grid[eastDistX][distY] = PLAYER_EAST;
            westTeamLoc[i][0] = westDistX;
            westTeamLoc[i][1] = distY;
            eastTeamLoc[i][0] = eastDistX;
            eastTeamLoc[i][1] = distY;
        }
        /* places ball on the field and updates grid */
        ballX = (MAX_X / 2) - 1;
        ballY = (MAX_Y / 2) - 1;
        grid[ballX][ballY] = SOCCER_BALL;

        /* initialize game  called before game starts */
        if (SimulationPane.eastScore == 0 && SimulationPane.westScore == 0) {
            Main.eastTeam.initializeGame();
            Main.westTeam.initializeGame();
        }

        Main.eastTeam.initializePoint();
        Main.westTeam.initializePoint();
    }

    /**
     * Populates the local area array for the player functions.  Creates a grid that
     * records what object or within one grid space away from the player
     *
     * @param x x coordinate of player
     * @param y y coordinate of player
     * @param team team number of player
     */
    private void populateLocalArea(int x, int y, int team) {
        int r = x - 1;
        int s = y - 2;
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0) {
                r = x - 1;
                s++;
            }
            else {
                r++;
            }

            if (r == x && s == y)
                localArea[i] = EMPTY;
            else if (r < 0 || r > MAX_X - 2 || s < 0 || s > MAX_Y - 2)
                localArea[i] = BOUNDARY;
            else if (grid[r][s] == 0)
                localArea[i] = EMPTY;
            else if (grid[r][s] == SOCCER_BALL)
                localArea[i] = BALL;
            else if (grid[r][s] == team)
                localArea[i] = TEAMMATE;
            else
                localArea[i] = OPPONENT;
        }

        if (team == PLAYER_WEST) {
            swapLocalArea();
        }
    }

    /**
     * Swaps the local area of the local area method.
     */
    private void swapLocalArea() {
        int temp = 0;
        for (int i = 0; i < 9; i += 3) {
            temp = localArea[i];
            localArea[i] = localArea[i + 2];
            localArea[i + 2] = temp;
        }
    }

    /**
     * Determines direction of ball in relation to player.
     *
     * @param x coordinate of player
     * @param y coordinate of player
     * @param team of player
     *
     * @return direction of ball
     */
    private int ballDirectionCalc(int x, int y, int team) {
        int newX, newY;

        newX = findRealDirX(x, team);
        newY = findRealDirY(y);

        int dir = findDir(newX, newY);

        if (localArea[dir] != EMPTY && dir != PLAYER) {
            dir = findClosestDir(dir);
        }

        return dir;
    }

    /**
     * Finds an open space closest to a ball direction.  Uses a stack to determine space.
     *
     * @param dir direction to ball
     *
     * @return new direction to move
     */
    private int findClosestDir(int dir) {
        boolean[] local = convertToLocal();
        int ballClosest = dirToStackDir(dir);

        boolean blocked = true;
        for (int i = 0; i < 8; ++i) {
            if (local[i]) {
                blocked = false;
            }
        }

        if (blocked) {
            return PLAYER;
        }

        Stack<Integer> stack = new Stack<Integer>();

        int i = 1;
        int index;
        int newDir = ballClosest;
        while (!local[newDir]) {
            index = (ballClosest + i) % 8;
            stack.add(index);

            index = (ballClosest - i) % 8;
            if (index < 0) {
                index *= -1;
            }
            stack.add(index);

            newDir = stack.pop();
            i++;
        }

        newDir = stackDirToDir(newDir);

        return newDir;
    }

    private int stackDirToDir(int stackDir) {
        int newDir;
        switch (stackDir) {
            case 0 : newDir = NW;
                break;
            case 1 : newDir = N;
                break;
            case 2 : newDir = NE;
                break;
            case 7 : newDir = E;
                break;
            case 3 : newDir = W;
                break;
            case 4 : newDir = SW;
                break;
            case 5 : newDir = S;
                break;
            case 6 : newDir = SE;
                break;
            default: newDir = stackDir;
                break;
        }

        return newDir;
    }

    /**
     * Converts a dir into a dir compatible with the new direction algorithm
     *
     * @param dir old direction
     *
     * @return new direction
     */
    private int dirToStackDir(int dir) {
        int newDir;
        switch (dir) {
            case NW : newDir = 0;
                break;
            case N : newDir = 1;
                break;
            case NE : newDir = 2;
                break;
            case E : newDir = 7;
                break;
            case W : newDir = 3;
                break;
            case SW : newDir = 4;
                break;
            case S : newDir = 5;
                break;
            case SE : newDir = 6;
                break;
            default: newDir = dir;
                break;
        }

        return newDir;
    }

    /**
     * Converts the localArea array into an array of booleans to used with the stack to determine new direction
     *
     * @return boolean array describing blocked local area
     */
    private boolean[] convertToLocal() {
        boolean[] local = new boolean[8];

        if (localArea[NW] == EMPTY) {
            local[0] = true;
        }
        else {
            local[0] = false;
        }
        if (localArea[N] == EMPTY) {
            local[1] = true;
        }
        else {
            local[1] = false;
        }
        if (localArea[NE] == EMPTY) {
            local[2] = true;
        }
        else {
            local[2] = false;
        }
        if (localArea[E] == EMPTY) {
            local[3] = true;
        }
        else {
            local[3] = false;
        }
        if (localArea[SE] == EMPTY) {
            local[4] = true;
        }
        else {
            local[4] = false;
        }
        if (localArea[S] == EMPTY) {
            local[5] = true;
        }
        else {
            local[5] = false;
        }
        if (localArea[SW] == EMPTY) {
            local[6] = true;
        }
        else {
            local[6] = false;
        }
        if (localArea[W] == EMPTY) {
            local[7] = true;
        }
        else {
            local[7] = false;
        }

        return local;
    }

    /**
     * Finds the real x direction of the ball regardless of anything objects.  Basically a direct line to the team's
     * kicking location.
     *
     * @param x coordinate of player
     * @param team of player
     *
     * @return -1 if ball's x coordinate is less, 1 if greater, or 0 if level
     */
    private int findRealDirX(int x, int team) {
        int r;
        if (team == PLAYER_WEST) {
            if (ballX - 1 > x)
                r = -1;
            else if (ballX + 1 < x)
                r = 1;
            else
                r = 0;
        }
        else {
            if (ballX + 1 < x)
                r = -1;
            else if (ballX - 1 > x)
                r = 1;
            else
                r = 0;
        }

        return r;
    }

    /**
     * Finds the real y direction of the ball regardless of anything objects.  Basically a direct line to the team's
     * kicking location.
     *
     * @param y coordinate of player
     *
     * @return -1 if ball's y coordinate is less, 1 if greater, or 0 if level
     */
    private int findRealDirY(int y) {
        int s;
        if (ballY + 1 < y)
            s = 1;
        else if (ballY - 1 > y)
            s = -1;
        else
            s = 0;

        return s;
    }

    /**
     * Finds the direction to move a player based the new x and y coordinate changes.
     * Assume the the player is located at (0, 0).
     *
     * @param x coordinate change
     * @param y coordinate change
     *
     * @return direction to move
     */
    private int findDir(int x, int y) {
        int dir;

        if (x == -1 && y == 1)
            dir = NW;
        else if (x == 0 && y == 1)
            dir = N;
        else if (x == 1 && y == 1)
            dir = NE;
        else if (x == -1 && y == 0)
            dir = E;
        else if (x == 0 && y == 0)
            dir = PLAYER;
        else if (x == 1 && y == 0)
            dir = W;
        else if (x == -1 && y == -1)
            dir = SW;
        else if (x == 0 && y == -1)
            dir = S;
        else //(r == 1 && s == -1)
            dir = SE;

        return dir;
    }

    /**
     * flips the x coodinate so that the west player's team functions will think
     * it is on the east side.
     *
     * @param x coordinate to be flipped
     *
     * @return new x coordinate
     */
    private int mirrorWestX(int x) {
	int newX = (MAX_X - 2) - x;

	return newX;
    }

    /**
     * Updates and moves west player
     *
     * @param i index of player int west team
     *
     * @return true if point was scored, otherwise false
     */
    public boolean updateWest(int i) {
        int playerMove = 0;
        populateLocalArea(westTeamLoc[i][0], westTeamLoc[i][1], PLAYER_WEST);
        ballDirection = ballDirectionCalc(westTeamLoc[i][0], westTeamLoc[i][1], PLAYER_WEST);
        switch (i) {
            case 0:
                playerMove = Main.westTeam.player1(localArea, ballDirection,
						   mirrorWestX(westTeamLoc[i][0]), 
						   westTeamLoc[i][1], 
						   mirrorWestX(ballX), ballY);
                break;
            case 1:
                playerMove = Main.westTeam.player2(localArea, ballDirection,
						   mirrorWestX(westTeamLoc[i][0]), 
						   westTeamLoc[i][1], 
						   mirrorWestX(ballX), ballY);
                break;
            case 2:
                playerMove = Main.westTeam.player3(localArea, ballDirection,
						   mirrorWestX(westTeamLoc[i][0]), 
						   westTeamLoc[i][1], 
						   mirrorWestX(ballX), ballY);
                break;
            case 3:
                playerMove = Main.westTeam.player4(localArea, ballDirection,
						   mirrorWestX(westTeamLoc[i][0]), 
						   westTeamLoc[i][1], 
						   mirrorWestX(ballX), ballY);
                break;
        }
        return update(PLAYER_WEST, i, westTeamLoc[i][0], westTeamLoc[i][1], playerMove);
    }

    /**
     * Updates and moves east player
     *
     * @param i index of player int east team
     *
     * @return true if point was scored, otherwise false
     */
    public boolean updateEast(int i) {
        int playerMove = 0;
        populateLocalArea(eastTeamLoc[i][0], eastTeamLoc[i][1], PLAYER_EAST);
        ballDirection = ballDirectionCalc(eastTeamLoc[i][0], eastTeamLoc[i][1], PLAYER_EAST);
        switch (i) {
            case 0:
                playerMove = Main.eastTeam.player1(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1], ballX, ballY);
                break;
            case 1:
                playerMove = Main.eastTeam.player2(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1], ballX, ballY);
                break;
            case 2:
                playerMove = Main.eastTeam.player3(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1], ballX, ballY);
                break;
            case 3:
                playerMove = Main.eastTeam.player4(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1], ballX, ballY);
                break;
        }

        return update(PLAYER_EAST, i, eastTeamLoc[i][0], eastTeamLoc[i][1], playerMove);
    }

    /**
     * Updates positions of players on the board.  Does not move player if move is invalid.
     * Should be used with the return value of the individual player functions.
     *
     * @param team team number in the grid
     * @param playerNumInLoc player number and index in player location array
     * @param beginX starting x of player
     * @param beginY starting y of player
     * @param dir direction to move player or kick the ball.
     *
     * @return true if point was scored, otherwise false
     */
    private boolean update(int team, int playerNumInLoc, int beginX, int beginY, int dir) {
        int x = 0, y = 0;

        if (team == PLAYER_WEST) {
            dir = swapDir(dir);
        }

        /* Determines gird space to move player to */
        switch (dir) {
            case NW : x = beginX - 1;
                y =  beginY - 1;
                break;
            case N : x = beginX;
                y =  beginY - 1;
                break;
            case NE : x = beginX + 1;
                y =  beginY - 1;
                break;
            case E : x = beginX - 1;
                y =  beginY;
                break;
            case PLAYER : x = beginX;
                y =  beginY;
                break;
            case W : x = beginX + 1;
                y =  beginY;
                break;
            case SW : x = beginX - 1;
                y =  beginY + 1;
                break;
            case S : x = beginX;
                y =  beginY + 1;
                break;
            case SE : x = beginX + 1;
                y =  beginY + 1;
                break;
            default:
                x = beginX;
                y = beginY;
                break;
        }

        /* Updates variable for gui */
        playerIndex = playerNumInLoc;
        moveX = beginX;
        moveY = beginY;


        /* If the player tries to kick the ball and is in a valid spot */
        if (validKick(dir, x, y)) {
            if (kickBallRandom(team, dir)) {
                return true;
            }
        }

        if (x >= 0 && y >= 0 && x < MAX_X - 1 && y < MAX_Y - 1 &&
                (grid[x][y] == EMPTY || (x == beginX && y == beginY))) {
            /* Updates variables for GUI */
            moveX = x;
            moveY = y;

            /* update player grid */
            grid[beginX][beginY] = 0;
            grid[x][y] = team;
            if (team == PLAYER_EAST) {
                eastTeamLoc[playerNumInLoc][0] = x;
                eastTeamLoc[playerNumInLoc][1] = y;
            } else {
                westTeamLoc[playerNumInLoc][0] = x;
                westTeamLoc[playerNumInLoc][1] = y;
            }
        }
        else {
            /* No move is made. Should not be called */
            //System.out.println("Error false move- " + team + ": player- " + playerNumInLoc);
        }

        return false;
    }

    /**
     * Checks if a player is a valid kicking location.  Returns true, otherwise, false.
     *
     * @param dir of player move
     * @param coordinate of player on x axis
     * @param coordinate of player on y axis
     *
     * @return true is in kicking position, otherwise false
     */
    private boolean validKick(int dir, int x, int y) {
	if (dir >= KICK_E && dir <= KICK_S && x >= ballX - 1 && x <= ballX + 1 &&
	    y >= ballY - 1 && y <= ballY + 1) {
	    return true;
	}
	
	return false;
    }

    /**
     * Kicks the ball randomly in the direction of a particular team.
     *
     * @param team who is kicking the ball
     * @param direction ball is kicked
     *
     * @return returns true if point was scored, otherwise, false
     */
    private boolean kickBall(int team, int kickDir) {
        int newBallX = 0, newBallY = 0;
        Random random = new Random();
        boolean won = false;

	/* Finds new random ball location in direction of kicking team */
	do {
	    newBallX = 3 + random.nextInt(5);
	    newBallY = random.nextInt(4);
	    
	    if (team == PLAYER_EAST) {
		newBallX *= -1;
	    }
	    
	    if (random.nextBoolean()) {
		newBallY *= -1;
	    }
	    
	    newBallX += ballX;
	    newBallY += ballY;
	    
	    if (newBallX < 0) {
		newBallX = 0;
	    }
	    else if (newBallX >= MAX_X - 2) {
		newBallX = MAX_X - 2;
	    }
	    
	    if (newBallY < 0) {
		newBallY = 0;
	    }
	    else if (newBallY >= MAX_Y - 2) {
		newBallY = MAX_Y - 2;
	    }
        } while (grid[newBallX][newBallY] != 0);
       
        /* Updates variables for GUI */
	ballMoved = true;

        /* Update grid */
        /* Randomly choose whether or not to warp or stop ball. 60% chance of stopping ball */
	double warpsVal = Math.random();
        if(warpsVal < 0.6) 
            moveBallWithStops(ballX, ballY, newBallX, newBallY);
        else {
            grid[ballX][ballY] = EMPTY;
            grid[newBallX][newBallY] = SOCCER_BALL;
            ballX = newBallX;
            ballY = newBallY;
        }
	
        /* Checks if point is scored and handles appropriately */
        if (ballX == 0) {
            Main.eastTeam.wonPoint();
            Main.westTeam.lostPoint();
            won = true;
        }
        else if (ballX == MAX_X - 2) {
            Main.eastTeam.lostPoint();
            Main.westTeam.wonPoint();
            won = true;
        }
	
        return won;
    } 

    private boolean kickBallRandom(int team, int dir) {
        int newBallX = 0, newBallY = 0;
        Random random = new Random();
        boolean won = false;

        /* Finds new random ball location in direction of kicking team */
	do {
	    newBallX = 3 + random.nextInt(5);
	    newBallY = random.nextInt(4);

	    if (team == PLAYER_EAST) {
		newBallX *= -1;
	    }

	    if (random.nextBoolean()) {
		newBallY *= -1;
	    }

	    newBallX += ballX;
	    newBallY += ballY;

	    if (newBallX < 0) {
		newBallX = 0;
	    }
	    else if (newBallX >= MAX_X - 2) {
		newBallX = MAX_X - 2;
	    }

	    if (newBallY < 0) {
		newBallY = 0;
	    }
	    else if (newBallY >= MAX_Y - 2) {
		newBallY = MAX_Y - 2;
	    }
        } while (grid[newBallX][newBallY] != 0);

        /* Updates variables for GUI */
        ballMoved = true;

        /* Update grid */
        grid[ballX][ballY] = 0;
        grid[newBallX][newBallY] = SOCCER_BALL;
        ballX = newBallX;
        ballY = newBallY;

        /* Checks if point is scored and handles appropriately */
        if (ballX == 0) {
            Main.eastTeam.wonPoint();
            Main.westTeam.lostPoint();
            won = true;
        }
        else if (ballX == MAX_X - 2) {
            Main.eastTeam.lostPoint();
            Main.westTeam.wonPoint();
            won = true;
        }

        return won;
    }
    
    /**
     * Recursively moves the ball in the given direction. If it hits something or hits an invalid space it stops.
     *
     * @param original x position of the ball
     * @param original y position of the ball
     * @param goal x position of the ball
     * @param goal y position of the ball
     */
    private void moveBallWithStops(int origX, int origY, int goalX, int goalY) {
        int newX = origX;
        int newY = origY;
        double slope = (goalY - origY) / (goalX - goalY);

        if(slope > 1) // Go vertically one space towards goal location
            newY = origY + getSign(goalY - origY);
        else // Go horizontally one space towards goal location
            newX = origX + getSign(goalX - origX);

        if(grid[newX][newY] == 0) { // Check to make sure the space is empty, if so move the ball to the space
            grid[origX][origY] = EMPTY;
            grid[newX][newY] = SOCCER_BALL;
            moveBallWithStops(newX, newY, goalX, goalY);
        } else {
            // If the space is blocked or invalid, do nothing
        }
    }
    
    /* @param number to get sign returned */
    private int getSign(int num) {
        if(num > 0)
            return 1;
        if(num < 0)
            return -1;
        else 
            return 0;
    }

    /**
     * Swaps directions for the east team.  Teams as if they are facing west and
     * so the east side needs to be converted to go the other direction.
     *
     * @param direction to be switched
     * @return new direction
     */
    public static int swapDir(int direction) {
        switch(direction) {
            case NW:
                return (NE);
            case N:
                return (N);
            case NE:
                return (NW);
            case E:
                return (W);
            case SE:
                return (SW);
            case S:
                return (S);
            case SW:
                return (SE);
            case W:
                return (E);
            case KICK_E:
                return (KICK_E);
            case PLAYER:
                return (PLAYER);
            default:
                return (PLAYER);
        }
    }

    /**
     * Called when the game is over.
     */
    public void gameOver() {
        Main.eastTeam.gameOver();
        Main.westTeam.gameOver();
    }

    /**
     * Prints the locations of the players.  Used for testing.
     */
    private void printTeams() {
        System.out.println("East Team");
        for (int i = 0; i < 4; ++i) {
            System.out.print(eastTeamLoc[i][0] + " " + eastTeamLoc[i][1] + "\n");
        }
        System.out.println("West Team");
        for (int i = 0; i < 4; ++i) {
            System.out.print(westTeamLoc[i][0] + " " + westTeamLoc[i][1] + "\n");
        }
    }

    /**
     * Prints the game grid.  Used for testing.
     */
    private void printGrid() {
        for (int i = 0; i < MAX_Y; ++i) {
            for (int j = 0; j < MAX_X; ++j) {
                System.out.print(grid[j][i] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints localArea array. Used for testing
     */
    private void printPop() {
        for (int i = 0; i < 9; ++i) {
            System.out.print(localArea[i] + " ");
        }
        System.out.println();
    }

    /**
     * Getter for playerIndex.  Used for the GUI.
     *
     * @return playerIndex
     */
    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     * Getter for moveY.  Used for the GUI.
     *
     * @return moveY
     */
    public int getMoveY() {
        return moveY;
    }

    /**
     * Getter for moveX.  Used for the GUI.
     *
     * @return moveX
     */
    public int getMoveX() {
        return moveX;
    }

    /**
     * Getter for ballMoved.  Used for the GUI.
     *
     * @return ballMoved
     */
    public boolean getBallMoved() {
        return ballMoved;
    }

    /**
     * Updates the move ball variable after the GUI updates.
     */
    public void movedBall() {
        ballMoved = false;
    }

    /**
     * Getter for ballX.  Used for the GUI.
     *
     * @return ballX
     */
    public int getBallX() {
        return ballX;
    }

    /**
     * Getter for ballY.  Used for the GUI.
     *
     * @return ballY
     */
    public int getBallY() {
        return ballY;
    }
}
