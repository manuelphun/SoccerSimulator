package team;

/**
 * Created by grantcooksey on 8/29/15.
 * Purpose: The most straight forward Brute force approach.  Does not handle players getting in the way.
 */
public class BrutalBeaters implements Team {
    @Override
    public int player1(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        if (ballDirection == PLAYER)
            return KICK;
        else
            return ballDirection;
    }

    @Override
    public int player2(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        if (ballDirection == PLAYER)
            return KICK;
        else
            return ballDirection;
    }

    @Override
    public int player3(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        if (ballDirection == PLAYER)
            return KICK;
        else
            return ballDirection;
    }

    @Override
    public int player4(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        if (ballDirection == PLAYER)
            return KICK;
        else
            return ballDirection;
    }

    @Override
    public String teamName() {
        return "Brutal Beaters";
    }

    @Override
    public void initializePoint() {

    }

    @Override
    public void wonPoint() {

    }

    @Override
    public void lostPoint() {

    }

    @Override
    public void initializeGame() {

    }

    @Override
    public void gameOver() {

    }
}
