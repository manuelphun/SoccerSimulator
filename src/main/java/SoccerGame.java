package soccer;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by grantcooksey on 8/25/15.
 * Purpose: The game simulation.
 *
 * TODO Bug where player does nothing but blocks opposite teams kicking location
 */
public class SoccerGame extends AnimationTimer {

    private final int FIELD_X_BEGIN = 15;
    private final int FIELD_X_END = 575;
    private final int FIELD_Y_BEGIN = 5;
    private final int FIELD_Y_END = 285;
    private final int MAX_X = 58;
    private final int MAX_Y = 30;
    private final int START_DIST_FROM_MIDDLE = 13;
    private final int SOCCER_BALL = 3;
    private final int GRID_DIST = 10;
    private final int PAUSE_TIMER = 100;

    protected static final int TIMER_INSTANT = 0;
    protected static final int TIMER_FAST = 2;
    protected static final int TIMER_NORMAL = 15;
    protected static final int TIMER_SLOW = 25;
    protected static int timeSetting;
    protected static final int PLAYER_EAST = 1;
    protected static final int PLAYER_WEST = 2;

    private ArrayList<Rectangle> east;
    private ArrayList<Rectangle> west;
    private Circle ball;
    private int timer;
    private int resetGameTimer;
    private static int[][] grid;
    private static int[][] eastTeamLoc;
    private static boolean[] eastTeamStuck;
    private static boolean[] westTeamStuck;
    private static int[][] westTeamLoc;
    private int[] localArea;
    private int ballDirection;
    private int ballX;
    private int ballY;
    private int playerMove;

    /* Constructor */
    public SoccerGame() {
        timer = 0;
        timeSetting = TIMER_NORMAL;
        grid = new int[MAX_X][MAX_Y];
        eastTeamLoc = new int[4][2];
        eastTeamStuck = new boolean[4];
        westTeamLoc = new int[4][2];
        westTeamStuck = new boolean[4];
        localArea = new int[9];

        east = new ArrayList<Rectangle>();
        west = new ArrayList<Rectangle>();
        for (int i = 0; i < 4; i++) {
            east.add(new Rectangle(10, 10, Color.BLUE));
            east.get(i).setStroke(Color.BLACK);
            east.get(i).setStrokeWidth(1);
            west.add(new Rectangle(10, 10, Color.RED));
            west.get(i).setStroke(Color.BLACK);
            west.get(i).setStrokeWidth(1);
        }
        ball = new Circle(5, Color.PURPLE);
        Main.game.getChildren().addAll(ball);
        for (int i  = 0; i < 4; ++i) {
            Main.game.getChildren().addAll(east.get(i), west.get(i));
        }

        setGame();
    }

    /**
     * Sets up the game from the start.  Players and ball are aligned
     * in their starting positions and the game is initialized.  This
     * should be called at the start of the game and between points.
     * If it is between points, the game will be started automatically.
     */
    private void setGame() {
        /* clears grid */
        for (int i = 0; i < MAX_X; ++i) {
            Arrays.fill(grid[i], 0);
        }

        /* sets all players to not be stuck */
        Arrays.fill(eastTeamStuck, false);
        Arrays.fill(westTeamStuck, false);

        /* places players on the field and updates grid */
        int distY, eastDistX, westDistX;
        for (int i = 0; i < 4; ++i) {
            distY = (i * 6) + 5;
            eastDistX = MAX_X / 2 + START_DIST_FROM_MIDDLE;
            westDistX = (MAX_X / 2 - START_DIST_FROM_MIDDLE) - 1;
            east.get(i).relocate(FIELD_X_BEGIN + (eastDistX * GRID_DIST),
                    (distY * GRID_DIST) + FIELD_Y_BEGIN);
            west.get(i).relocate(FIELD_X_BEGIN + (westDistX * GRID_DIST),
                    (distY * GRID_DIST) + FIELD_Y_BEGIN);
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
        // uses - 1 to center ball in the middle of the field
        ball.relocate(FIELD_X_BEGIN + (GRID_DIST * ballX), FIELD_Y_BEGIN + (GRID_DIST * ballY));
        grid[ballX][ballY] = SOCCER_BALL;

        if (SimulationPane.eastScore > 0 || SimulationPane.westScore > 0) {
            Main.eastTeam.initializePoint();
            Main.westTeam.initializePoint();

            timer = 1;
            resetGameTimer = PAUSE_TIMER;

            /* starts game */
            this.start();
        }
        else {
            /* initialize game  called before game starts */
            Main.eastTeam.initializeGame();
            Main.westTeam.initializeGame();
        }
    }

    /**
     * Check if all players are stuck.
     *
     * @return if all players are stuck true, otherwise false
     */
    private boolean isStuck() {
        boolean stuck = true;
        for (int i = 0; i < 4; ++i) {
            if (!eastTeamStuck[i] || !westTeamStuck[i]) {
                stuck = false;
            }
        }
        return stuck;
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
                localArea[i] = Team.EMPTY;
            else if (r < 0 || r > MAX_X || s < 0 || s > MAX_Y)
                localArea[i] = Team.BOUNDARY;
            else if (grid[r][s] == 0)
                localArea[i] = Team.EMPTY;
            else if (grid[r][s] == SOCCER_BALL)
                localArea[i] = Team.BALL;
            else if (grid[r][s] == team)
                localArea[i] = Team.TEAMMATE;
            else
                localArea[i] = Team.OPPONENT;
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
     * Determines direction of ball in relation to player.
     *
     * @param x coordinate of player
     * @param y coordinate of player
     * @param team of player
     *
     * @return direction of ball
     */
    private int ballDirectionCalc(int x, int y, int team) {
        int dir = 0;

        int r, s;
        if (team == PLAYER_WEST) {
            if (ballX - 1 > x)
                r = 1;
            else if (ballX - 1 == x)
                r = 0;
            else
                r = -1;
        }
        else {
            if (ballX + 1 < x)
                r = 1;
            else if (ballX + 1 == x)
                r = 0;
            else
                r = -1;
        }

        if (ballY < y)
            s = 1;
        else if (ballY == y)
            s = 0;
        else
            s = -1;

        if (r == -1 && s == 1)
            dir = Team.NW;
        else if (r == 0 && s == 1)
            dir = Team.N;
        else if (r == 1 && s == 1)
            dir = Team.NE;
        else if (r == -1 && s == 0)
            dir = Team.E;
        else if (r == 0 && s == 0)
            dir = Team.PLAYER;
        else if (r == 1 && s == 0)
            dir = Team.W;
        else if (r == -1 && s == -1)
            dir = Team.SW;
        else if (r == 0 && s == -1)
            dir = Team.S;
        else if (r == 1 && s == -1)
            dir = Team.SE;

        return dir;
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
            case Team.NW:
                return (Team.NE);
            case Team.N:
                return (Team.N);
            case Team.NE:
                return (Team.NW);
            case Team.E:
                return (Team.W);
            case Team.SE:
                return (Team.SW);
            case Team.S:
                return (Team.S);
            case Team.SW:
                return (Team.SE);
            case Team.W:
                return (Team.E);
            case Team.KICK:
                return (Team.KICK);
            case Team.PLAYER:
                return (Team.PLAYER);
            default:
                return (Team.PLAYER);
        }
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

    @Override
    public void handle(long now) {
        if (timer == 0) {
            if (resetGameTimer == 0) {
                Random random = new Random();

                /* update players */
                for (int i = 0; i < 4; ++i) {
                    /* randomizes player movements */
                    if (random.nextBoolean()) {
                        if (updateEast(i)) {
                            break;
                        }
                        if (updateWest(i)) {
                            break;
                        }
                    } else {
                        if (updateWest(i)) {
                            break;
                        }
                        if (updateEast(i)) {
                            break;
                        }
                    }
                }
            }
            
            if (isStuck()) {
                stop();
                setGame();
                resetGameTimer = PAUSE_TIMER;
                if (SimulationPane.eastScore == 0 && SimulationPane.westScore == 0) {
                    this.start();
                }
            }

            timer = timeSetting;
        }
        else {
            if (resetGameTimer > 0) {
                resetGameTimer--;
            }
            else {
                timer--;
            }
        }
    }

    /**
     * Updates and moves west player
     *
     * @param i index of player int west team
     *
     * @return true if point was scored, otherwise false
     */
    private boolean updateWest(int i) {
        populateLocalArea(westTeamLoc[i][0], westTeamLoc[i][1], PLAYER_WEST);
        ballDirection = ballDirectionCalc(westTeamLoc[i][0], westTeamLoc[i][1], PLAYER_WEST);
        switch (i) {
            case 0:
                playerMove = Main.westTeam.player1(localArea, ballDirection,
                        westTeamLoc[i][0], westTeamLoc[i][1]);
                break;
            case 1:
                playerMove = Main.westTeam.player2(localArea, ballDirection,
                        westTeamLoc[i][0], westTeamLoc[i][1]);
                break;
            case 2:
                playerMove = Main.westTeam.player3(localArea, ballDirection,
                        westTeamLoc[i][0], westTeamLoc[i][1]);
                break;
            case 3:
                playerMove = Main.westTeam.player4(localArea, ballDirection,
                        westTeamLoc[i][0], westTeamLoc[i][1]);
                break;
        }
        return updateBoard(west.get(i), PLAYER_WEST, i, westTeamLoc[i][0], westTeamLoc[i][1], playerMove);
    }

    /**
     * Updates and moves east player
     *
     * @param i index of player int east team
     *
     * @return true if point was scored, otherwise false
     */
    private boolean updateEast(int i) {
        populateLocalArea(eastTeamLoc[i][0], eastTeamLoc[i][1], PLAYER_EAST);
        ballDirection = ballDirectionCalc(eastTeamLoc[i][0], eastTeamLoc[i][1], PLAYER_EAST);
        switch (i) {
            case 0:
                playerMove = Main.eastTeam.player1(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1]);
                break;
            case 1:
                playerMove = Main.eastTeam.player2(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1]);
                break;
            case 2:
                playerMove = Main.eastTeam.player3(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1]);
                break;
            case 3:
                playerMove = Main.eastTeam.player4(localArea, ballDirection,
                        eastTeamLoc[i][0], eastTeamLoc[i][1]);
                break;
        }
        return updateBoard(east.get(i), PLAYER_EAST, i, eastTeamLoc[i][0], eastTeamLoc[i][1], playerMove);
    }

    /**
     * Updates positions of players on the board.  Does not move player if move is invalid.
     * Should be used with the return value of the individual player functions.
     *
     * @param player Shape used to represent the player in the GUI
     * @param team team number in the grid
     * @param playerNumInLoc player number and index in player location array
     * @param beginX starting x of player
     * @param beginY starting y of player
     * @param dir direction to move player or kick the ball.
     *
     * @return true if point was scored, otherwise false
     */
    private boolean updateBoard(Shape player, int team, int playerNumInLoc, int beginX, int beginY, int dir) {
        int x = 0, y = 0;

        if (team == PLAYER_EAST) {
            dir = swapDir(dir);
        }

        /* Determines gird space to move player to */
        switch (dir) {
            case Team.NW : x = beginX - 1;
                y =  beginY - 1;
                break;
            case Team.N : x = beginX;
                y =  beginY - 1;
                break;
            case Team.NE : x = beginX + 1;
                y =  beginY - 1;
                break;
            case Team.E : x = beginX - 1;
                y =  beginY;
                break;
            case Team.PLAYER : x = beginX;
                y =  beginY;
                break;
            case Team.W : x = beginX + 1;
                y =  beginY;
                break;
            case Team.SW : x = beginX - 1;
                y =  beginY + 1;
                break;
            case Team.S : x = beginX;
                y =  beginY + 1;
                break;
            case Team.SE : x = beginX + 1;
                y =  beginY + 1;
                break;
            default:
                x = beginX;
                y = beginY;
                break;
        }

        /* If the player tries to kick the ball and is in a valid spot */
        if (dir == Team.KICK && ((team == PLAYER_EAST && x == ballX + 1 && y == ballY) ||
                (team == PLAYER_WEST && x == ballX - 1 && y == ballY))) {
            if (kickBall(team)) {
                return true;
            }
        }

        if (x >= 0 && y >= 0 && x < MAX_X - 1 && y < MAX_Y - 1 &&
                (grid[x][y] == Team.EMPTY || (x == beginX && y == beginY))) {
            player.relocate(FIELD_X_BEGIN + (x * GRID_DIST), FIELD_Y_BEGIN + (y * GRID_DIST));

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
            if (team == PLAYER_EAST) {
                eastTeamStuck[playerNumInLoc] = true;
            }
            else {
                westTeamStuck[playerNumInLoc] = true;
            }
        }

        return false;
    }

    /**
     * Kicks the ball randomly in the direction of a particular team.
     *
     * @param team who is kicking the ball
     *
     * @return returns true if point was scored, otherwise, false
     */
    private boolean kickBall(int team) {
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

        /* Update GUI */
        ball.relocate(FIELD_X_BEGIN + (GRID_DIST * newBallX), FIELD_Y_BEGIN + (GRID_DIST * newBallY));

        /* Update grid */
        grid[ballX][ballY] = 0;
        grid[newBallX][newBallY] = SOCCER_BALL;
        ballX = newBallX;
        ballY = newBallY;

        /* Checks if point is scored and handles appropriately */
        if (ballX == 0) {
            if (SimulationPane.eastScore == 6) {
                gameWon(PLAYER_EAST);
            }
            Main.eastTeam.wonPoint();
            Main.westTeam.lostPoint();
            scoredPoint(PLAYER_EAST);
            won = true;
        }
        else if (ballX == MAX_X - 2) {
            if (SimulationPane.westScore == 6) {
                gameWon(PLAYER_WEST);
            }
            Main.eastTeam.lostPoint();
            Main.westTeam.wonPoint();
            scoredPoint(PLAYER_WEST);
            won = true;
        }

        return won;
    }

    /**
     * Resets board and updates score.
     *
     * @param team which scored
     */
    private void scoredPoint(int team) {
        stop();

        /* increments the score */
        if (team == PLAYER_EAST) {
            SimulationPane.eastScore++;
        }
        else {
            SimulationPane.westScore++;
        }

        SimulationPane.updateScore();

        /* Pauses the game between points */
        resetGameTimer = PAUSE_TIMER;
        timer = timeSetting;
        setGame();
    }

    /**
     * Prints the winning message and ends the game.
     */
    private void gameWon(int team) {
        /* prints the winning message */
        if (team == PLAYER_EAST) {
            System.out.println("East Team: " + Main.eastTeam.teamName() + " won the game.");
        }
        else {
            System.out.println("West Team: " + Main.westTeam.teamName() + " won the game.");
        }

        /* ends the game */
        System.exit(0);
    }

    /**
     * Restarts the game
     */
    public void restart() {
        stop();

        /* New team is loaded so scores are reset */
        SimulationPane.eastScore = 0;
        SimulationPane.westScore = 0;

        setGame();
    }

    public void run() {
        this.start();
    }
}
