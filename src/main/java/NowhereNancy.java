import java.util.Random;

/**
 * Created by grantcooksey on 8/23/15.
 * Purpose: Default team.  Just randomly moves around.  They will not win.
 */
public class NowhereNancy implements Team {


    public NowhereNancy() {
    }

    @Override
    public int player1(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        Random random = new Random();
        return random.nextInt(8);
    }

    @Override
    public int player2(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        Random random = new Random();
        return random.nextInt(8);
    }

    @Override
    public int player3(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        Random random = new Random();
        return random.nextInt(8);
    }

    @Override
    public int player4(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        Random random = new Random();
        return random.nextInt(8);
    }

    @Override
    public String teamName() {
        return "NowhereNancy";
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
