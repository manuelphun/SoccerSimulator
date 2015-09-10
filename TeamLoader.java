import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grant Cooksey on 9/6/15.
 * Purpose: The TeamLoader loads compiled teams into the program so that they can be used to compete.
 * Teams must extend the Team interface and the compiled .class file must be located in the parent directory.
 */
public class TeamLoader {
    public static final String TEAM_PACKAGE = "";

    private ArrayList<Class<Team>> teamClass;
    private String[] teamNames;

    /**
     * Loads teams.
     */
    public TeamLoader() {
        teamNames = findTeams();
        loadTeams(teamNames);
    }

    /**
     * Gets the number of teams loaded and returns the value
     *
     * @return number of teams loaded.
     */
    public int numTeams() {
        return teamClass.size();
    }

    /**
     * Gets the loaded teams.
     *
     * @return Array of loaded teams
     */
    public ArrayList<Class<Team>> getTeamClass() {
        return teamClass;
    }

    /**
     * Gets the team names of the loaded teams.
     *
     * @return string array of the teams loaded.
     */
    public String[] getTeamNames() {
        String[] temp = new String[teamNames.length];
        for (int i = 0; i < teamNames.length; ++i) {
            temp[i] = teamNames[i];
        }

        return temp;
    }

    /**
     * Finds the .class team files
     *
     * @return string array of file names
     */
    private String[] findTeams() {
        String path = ".";
        String fileName;
        List<String> teamFilesList = new ArrayList<String>();
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; ++i) {
            if (files[i].isFile()) {
                fileName = files[i].getName();
                if (isTeamFile(fileName)) {
                    teamFilesList.add(fileName);
                }
            }
        }

        String[] teamFiles = new String[teamFilesList.size()];
        teamFiles = teamFilesList.toArray(teamFiles);

        for (int i = 0; i < teamFiles.length; ++i) {
            teamFiles[i] = teamFiles[i].substring(0, teamFiles[i].length() - 6);
        }

        return teamFiles;
    }

    /**
     * Finds all the team .class files
     *
     * @param fileName to check
     *
     * @return is a team .class file
     */
    private boolean isTeamFile(String fileName) {
	if (fileName.endsWith(".class")) {
	    if (!fileName.equals("Main.class") && !fileName.equals("SimulationPane.class") && !fileName.equals("SoccerBatch.class") && !fileName.equals("SoccerGUI.class") && !fileName.equals("SoccerGame.class") && !fileName.equals("Team.class") && !fileName.equals("TeamLoader.class") && !fileName.startsWith("SimulationPane$")) {
		return true;
	    }
	}

	return false;
    }
	    

    /**
     * Dynamically loads Team classes into the program. Classes are
     * added to teamClass.  It is assumed that the classes loaded implement Team.
     * Chooses one of the teams as the default team.  If no teams are able to load,
     * the program will exit.
     *
     * @param teamName String array of .class file names
     */
    private void loadTeams(String[] teamName) {
        ClassLoader classLoader = TeamLoader.class.getClassLoader();
        teamClass = new ArrayList<Class<Team>>();
        String name, packageName;

        for (int i = 0; i < teamName.length; ++i) {
            name = TEAM_PACKAGE + teamName[i];
            try {
                Class aClass = classLoader.loadClass(name);
                teamClass.add(aClass);
                //System.out.println(teamClass.get(i).getName());
            }
            catch (ClassNotFoundException e) {
                System.out.println("Class " + name +" failed to load.");
            }
        }

        if (teamClass.size() > 0) {
            try {
                Main.eastTeam = teamClass.get(0).newInstance();
                //eastName.setText(Main.eastTeam.teamName());
                Main.westTeam = teamClass.get(0).newInstance();
                //westName.setText(Main.westTeam.teamName());
            } catch (IllegalAccessException e1) {
                System.out.println("Something went wrong.");
            } catch (InstantiationException e2) {
                System.out.println("Something went wrong.");
            }
        }
        else {
            System.out.println("No teams were found.");
            System.exit(0);
        }
    }
}
