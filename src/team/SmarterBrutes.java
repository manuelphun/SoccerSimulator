package team;

/**
 * Created by grantcooksey on 9/3/15.
 * Purpose: TODO
 */
public class SmarterBrutes implements Team {
    @Override
    public int player1(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        int move = Team.PLAYER;
        if (ballDirection == Team.PLAYER) {
            move = Team.KICK;
        }
        else if (localArea[ballDirection] == Team.EMPTY) {
            move = ballDirection;
        }
        else {
            switch (ballDirection) {
                case Team.NW:
                    if (localArea[Team.W] == Team.EMPTY) {
                        move = Team.W;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.N:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
                case Team.NE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.E:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.SE;
                    }
                    break;
                case Team.SE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.S;
                    }
                    break;
                case Team.S:
                    if (localArea[Team.SE] == Team.EMPTY) {
                        move = Team.SE;
                    }
                    else {
                        move = Team.SW;
                    }
                    break;
                case Team.SW:
                    if (localArea[Team.S] == Team.EMPTY) {
                        move = Team.S;
                    }
                    else {
                        move = Team.W;
                    }
                    break;
                case Team.W:
                    if (localArea[Team.SW] == Team.EMPTY) {
                        move = Team.SW;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
            }
        }

        return move;
    }

    @Override
    public int player2(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        int move = Team.PLAYER;
        if (ballDirection == Team.PLAYER) {
            move = Team.KICK;
        }
        else if (localArea[ballDirection] == Team.EMPTY) {
            move = ballDirection;
        }
        else {
            switch (ballDirection) {
                case Team.NW:
                    if (localArea[Team.W] == Team.EMPTY) {
                        move = Team.W;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.N:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
                case Team.NE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.E:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.SE;
                    }
                    break;
                case Team.SE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.S;
                    }
                    break;
                case Team.S:
                    if (localArea[Team.SE] == Team.EMPTY) {
                        move = Team.SE;
                    }
                    else {
                        move = Team.SW;
                    }
                    break;
                case Team.SW:
                    if (localArea[Team.S] == Team.EMPTY) {
                        move = Team.S;
                    }
                    else {
                        move = Team.W;
                    }
                    break;
                case Team.W:
                    if (localArea[Team.SW] == Team.EMPTY) {
                        move = Team.SW;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
            }
        }

        return move;
    }

    @Override
    public int player3(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        int move = Team.PLAYER;
        if (ballDirection == Team.PLAYER) {
            move = Team.KICK;
        }
        else if (localArea[ballDirection] == Team.EMPTY) {
            move = ballDirection;
        }
        else {
            switch (ballDirection) {
                case Team.NW:
                    if (localArea[Team.W] == Team.EMPTY) {
                        move = Team.W;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.N:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
                case Team.NE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.E:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.SE;
                    }
                    break;
                case Team.SE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.S;
                    }
                    break;
                case Team.S:
                    if (localArea[Team.SE] == Team.EMPTY) {
                        move = Team.SE;
                    }
                    else {
                        move = Team.SW;
                    }
                    break;
                case Team.SW:
                    if (localArea[Team.S] == Team.EMPTY) {
                        move = Team.S;
                    }
                    else {
                        move = Team.W;
                    }
                    break;
                case Team.W:
                    if (localArea[Team.SW] == Team.EMPTY) {
                        move = Team.SW;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
            }
        }

        return move;
    }

    @Override
    public int player4(int[] localArea, int ballDirection, int x, int y, int ballX, int ballY) {
        int move = Team.PLAYER;
        if (ballDirection == Team.PLAYER) {
            move = Team.KICK;
        }
        else if (localArea[ballDirection] == Team.EMPTY) {
            move = ballDirection;
        }
        else {
            switch (ballDirection) {
                case Team.NW:
                    if (localArea[Team.W] == Team.EMPTY) {
                        move = Team.W;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.N:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
                case Team.NE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.N;
                    }
                    break;
                case Team.E:
                    if (localArea[Team.NE] == Team.EMPTY) {
                        move = Team.NE;
                    }
                    else {
                        move = Team.SE;
                    }
                    break;
                case Team.SE:
                    if (localArea[Team.E] == Team.EMPTY) {
                        move = Team.E;
                    }
                    else {
                        move = Team.S;
                    }
                    break;
                case Team.S:
                    if (localArea[Team.SE] == Team.EMPTY) {
                        move = Team.SE;
                    }
                    else {
                        move = Team.SW;
                    }
                    break;
                case Team.SW:
                    if (localArea[Team.S] == Team.EMPTY) {
                        move = Team.S;
                    }
                    else {
                        move = Team.W;
                    }
                    break;
                case Team.W:
                    if (localArea[Team.SW] == Team.EMPTY) {
                        move = Team.SW;
                    }
                    else {
                        move = Team.NW;
                    }
                    break;
            }
        }

        return move;
    }

    @Override
    public String teamName() {
        return "Smarter Brutes";
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
