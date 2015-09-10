import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grant Cooksey on 9/6/15.
 * Purpose: Provides support for batch processing of soccer simulations.  Each team plays against each other.
 */
public class SoccerBatch {

    private TeamLoader teamLoader;

    public SoccerBatch() {
        teamLoader = new TeamLoader();

        System.out.println("Batch processing of all teams enabled.");
	System.out.println("All teams will play each other twice per round, switching field sides every game.");
        System.out.print("Please enter how many rounds you wish to play as an integer: ");

        /* determines how many times each team will play each other. */
        int playIterations;
        Scanner scan = new Scanner(System.in);
        try {
            playIterations = scan.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Input not recognized. Each team will play each other once.");
            playIterations = 1;
        }
        scan.close();

        System.out.println();

        int[] winCount = simulateGames(playIterations);
        printWins(winCount);

	System.exit(0);
    }

    /**
     * Performs a batch simulation of all the teams playing each other.
     *
     * @param plays number of teams each team play each other
     *
     * @return int array of win count
     */
    private int[] simulateGames(int plays) {
        int[] wins =  new int[teamLoader.numTeams()];

        SoccerGame soccerGame = new SoccerGame();

        ArrayList<Class<Team>> teamClass = teamLoader.getTeamClass();

        for (int i = 0; i < teamLoader.numTeams(); ++i) {
            for (int j = 0; j < teamLoader.numTeams(); ++j) {
                if (i != j) {

                    /* load west team */
                    try {
                        Main.westTeam = teamClass.get(j).newInstance();
                    }
                    catch (IllegalAccessException e1) {
                        System.out.println("Something went wrong.");
                    }
                    catch (InstantiationException e2) {
                        System.out.println("Something went wrong.");
                    }

                    for (int k = 0; k < plays; ++k) {
                        switch (game(soccerGame)) {
                            case SoccerGame.PLAYER_EAST: wins[i]++;
                                break;
                            case SoccerGame.PLAYER_WEST: wins[j]++;
                                break;
                        }
                    }
                }
            }

            /* load east team */
            try {
                Main.eastTeam = teamClass.get(i).newInstance();
            }
            catch (IllegalAccessException e1) {
                System.out.println("Something went wrong.");
            }
            catch (InstantiationException e2) {
                System.out.println("Something went wrong.");
            }
        }

        return wins;
    }

    /**
     * Main simulation loop for each game.
     * TODO FIX
     *
     * @param soccerGame SoccerGame object
     *
     * @return winning team
     */
    private int game(SoccerGame soccerGame) {
        soccerGame.setGame();

        Random random = new Random();
        int teamScored = 0;
        int eastScore = 0;
        int westScore = 0;

        while (eastScore < 7 && westScore < 7) {
            while (teamScored == 0) {
                    /* update players */
                for (int i = 0; i < 4; ++i) {
                    /* randomizes player movements */
                    if (random.nextBoolean()) {
                        /* Updates east -> west */
                        if (soccerGame.updateEast(i)) {
                            teamScored = SoccerGame.PLAYER_EAST;
                            break;
                        }
                        if (soccerGame.updateWest(i)) {
                            teamScored = SoccerGame.PLAYER_WEST;
                            break;
                        }
                    } else {
                        /* Updates west -> east */
                        if (soccerGame.updateWest(i)) {
                            teamScored = SoccerGame.PLAYER_WEST;
                            break;
                        }
                        if (soccerGame.updateEast(i)) {
                            teamScored = SoccerGame.PLAYER_EAST;
                            break;
                        }
                    }
                }
            }

            if (teamScored == SoccerGame.PLAYER_EAST) {
                eastScore++;
            }
            else {
                westScore++;
            }
        }

        if (eastScore == 7) {
            return SoccerGame.PLAYER_EAST;
        }
        else {
            return SoccerGame.PLAYER_WEST;
        }
    }
    
    public class TeamScore implements Comparable<TeamScore> {
	int wins;
	String name;
	public TeamScore(String n, int w) {
	    wins = w;
	    name = n;
	}
	
	public String getName() {
	    return name;
	}
	
	public Integer getWins() {
	    return new Integer(wins);
	}
	
	public int compareTo(TeamScore t) {
	    return this.getWins().compareTo(t.getWins());
	}
	
	public String toString() {
	    return name + "\t\t" + wins;
	}
    }
    
    /**
     * Prints the wins from the simulator.
     *
     * @param winCount array of the wins.
     */
    private void printWins(int[] winCount) {
        String[] teamNames = teamLoader.getTeamNames();
        System.out.println("Team Names\t\tWins");
	
	List<TeamScore> ts = new ArrayList<TeamScore>();
        for (int i = 0; i < winCount.length; ++i) {
            ts.add(new TeamScore(teamNames[i], winCount[i]));
	}

	Collections.sort(ts, Collections.reverseOrder());
	for (int i = 0; i < ts.size(); ++i) {
	    System.out.println(ts.get(i));
	}
    }
}
