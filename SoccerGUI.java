import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Grant Cooksey on 9/6/15.
 * Purpose: Maintains the soccer simulation gui.
 */
public class SoccerGUI extends AnimationTimer {

    private final int FIELD_X_BEGIN = 15;
    private final int FIELD_X_END = 575;
    private final int FIELD_Y_BEGIN = 5;
    private final int FIELD_Y_END = 285;
    private final int GRID_DIST = 10;
    private final int PAUSE_TIMER = 100;

    protected static final int TIMER_FAST = 2;
    protected static final int TIMER_NORMAL = 15;
    protected static final int TIMER_SLOW = 25;
    protected static int timeSetting;

    private ArrayList<Rectangle> east;
    private ArrayList<Rectangle> west;
    private Circle ball;
    private int timer;
    private int resetGameTimer;

    private SoccerGame soccerGame;

    public SoccerGUI() {
        timer = 0;
        timeSetting = TIMER_NORMAL;

        soccerGame = new SoccerGame();

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
     * Sets the game up from the starting position. Called at the beginning of the game and between points.
     */
    public void setGame() {
        soccerGame.setGame();

        int distY, eastDistX, westDistX;
        for (int i = 0; i < 4; ++i) {
            distY = (i * 6) + 5;
            eastDistX = SoccerGame.MAX_X / 2 + SoccerGame.START_DIST_FROM_MIDDLE;
            westDistX = (SoccerGame.MAX_X / 2 - SoccerGame.START_DIST_FROM_MIDDLE) - 1;
            east.get(i).relocate(FIELD_X_BEGIN + (eastDistX * GRID_DIST),
                    (distY * GRID_DIST) + FIELD_Y_BEGIN);
            west.get(i).relocate(FIELD_X_BEGIN + (westDistX * GRID_DIST),
                    (distY * GRID_DIST) + FIELD_Y_BEGIN);
        }
        // uses - 1 to center ball in the middle of the field
        int ballX = (SoccerGame.MAX_X / 2) - 1;
        int ballY = ((SoccerGame.MAX_Y / 2) - 1);
        ball.relocate(FIELD_X_BEGIN + (GRID_DIST * ballX), FIELD_Y_BEGIN + (GRID_DIST * ballY));

        timer = 1;
        resetGameTimer = PAUSE_TIMER;

        if (SimulationPane.eastScore > 0 || SimulationPane.westScore > 0) {
            /* starts game */
            this.start();
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
                        /* Updates east -> west */
                        if (soccerGame.updateEast(i)) {
                            eastScored();
                            break;
                        }
                        updatePlayer(SoccerGame.PLAYER_EAST, soccerGame.getPlayerIndex(),
                                soccerGame.getMoveX(), soccerGame.getMoveY());
                        /* updates ball */
                        if (soccerGame.getBallMoved()) {
                            updateBall(soccerGame.getBallX(), soccerGame.getBallY());
                        }

                        if (soccerGame.updateWest(i)) {
                            westScored();
                            break;
                        }
                        updatePlayer(SoccerGame.PLAYER_WEST, soccerGame.getPlayerIndex(),
                                soccerGame.getMoveX(), soccerGame.getMoveY());
                        /* updates ball */
                        if (soccerGame.getBallMoved()) {
                            updateBall(soccerGame.getBallX(), soccerGame.getBallY());
                        }
                    } else {
                        /* updates west -> east */
                        if (soccerGame.updateWest(i)) {
                            westScored();
                            break;
                        }
                        updatePlayer(SoccerGame.PLAYER_WEST, soccerGame.getPlayerIndex(),
                                soccerGame.getMoveX(), soccerGame.getMoveY());
                         /* updates ball */
                        if (soccerGame.getBallMoved()) {
                            updateBall(soccerGame.getBallX(), soccerGame.getBallY());
                        }

                        if (soccerGame.updateEast(i)) {
                            eastScored();
                            break;
                        }
                        updatePlayer(SoccerGame.PLAYER_EAST, soccerGame.getPlayerIndex(),
                                soccerGame.getMoveX(), soccerGame.getMoveY());
                         /* updates ball */
                        if (soccerGame.getBallMoved()) {
                            updateBall(soccerGame.getBallX(), soccerGame.getBallY());
                        }
                    }
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
     * Updates east score and check for winning conditions.
     */
    private void eastScored() {
        if (SimulationPane.eastScore == 6) {
            gameWon(SoccerGame.PLAYER_EAST);
        }
        scoredPoint(SoccerGame.PLAYER_EAST);
    }

    /**
     * Updates west score and check for winning conditions.
     */
    private void westScored() {
        if (SimulationPane.westScore == 6) {
            gameWon(SoccerGame.PLAYER_WEST);
        }
        scoredPoint(SoccerGame.PLAYER_WEST);
    }

    /**
     * Updates the ball in the GUI.
     *
     * @param x coordinate
     * @param y coordinate
     */
    private void updateBall(int x, int y) {
        soccerGame.movedBall();
        ball.relocate(FIELD_X_BEGIN + (GRID_DIST * x), FIELD_Y_BEGIN + (GRID_DIST * y));
    }

    /**
     * Updates a player in the GUI.
     *
     * @param team of the player
     * @param i index in player array
     * @param x coordinate
     * @param y coordinate
     */
    private void updatePlayer(int team, int i, int x, int y) {
        if (team == SoccerGame.PLAYER_EAST) {
            east.get(i).relocate(FIELD_X_BEGIN + (x * GRID_DIST), FIELD_Y_BEGIN + (y * GRID_DIST));
        }
        else {
            west.get(i).relocate(FIELD_X_BEGIN + (x * GRID_DIST), FIELD_Y_BEGIN + (y * GRID_DIST));
        }
    }

    /**
     * Resets board and updates score.
     *
     * @param team which scored
     */
    private void scoredPoint(int team) {
        stop();

        /* increments the score */
        if (team == SoccerGame.PLAYER_EAST) {
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
        if (team == SoccerGame.PLAYER_EAST) {
            System.out.println("East Team: " + Main.eastTeam.teamName() + " won the game.");
        }
        else {
            System.out.println("West Team: " + Main.westTeam.teamName() + " won the game.");
        }

        soccerGame.gameOver();

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

    /**
     * starts the game.
     */
    public void run() {
        this.start();
    }

    /**
     * Resets the current game without changing score.
     */
    public void reset() {
        stop();
        setGame();
        
        resetGameTimer = PAUSE_TIMER;
        timer = timeSetting;
        if (SimulationPane.eastScore == 0 && SimulationPane.westScore == 0) {
            run();
        }
    }
}
