import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grant Cooksey on 8/25/15.
 * Purpose: Creates the Pane to display scores and select teams.
 */
public class SimulationPane extends VBox {

    public static final String SCORE_TEXT_SPACE = "       ";

    protected static int eastScore, westScore;
    protected static Text scores;
    private TeamLoader teamLoader;
    private SoccerGUI simulation;
    private Text eastName, westName;

    public SimulationPane() {
        super(10);

        teamLoader = new TeamLoader();

        simulation = new SoccerGUI();

        BorderPane scorePane = initializeScorePane();
        BorderPane optionPane = initializeOptionPane();

        this.getChildren().addAll(scorePane, optionPane);
    }

    /**
     * Initializes the pane containing the team options, speed and play buttons.
     *
     * @return Pane containing team options
     */
    private BorderPane initializeOptionPane() {
        BorderPane borderPane = new BorderPane();

        /* sets combo boxes for selecting teamNames */
        ComboBox<String> selectEastTeam, selectWestTeam;
        selectEastTeam = new ComboBox<String>();
        selectWestTeam = new ComboBox<String>();

        String[] teamNames = teamLoader.getTeamNames();

        for (int i = 0; i < teamNames.length; ++i) {
            selectEastTeam.getItems().add(teamNames[i]);
            selectWestTeam.getItems().add(teamNames[i]);
        }
        setTeam(selectEastTeam, SoccerGame.PLAYER_EAST);
        setTeam(selectWestTeam, SoccerGame.PLAYER_WEST);
        selectWestTeam.setPromptText("Select Team");
        selectEastTeam.setPromptText("Select Team");

        /* sets button to start play */
        VBox vBox = new VBox(4);
        HBox hBox = new HBox(2);
        Button playButton = new Button("Play");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulation.run();
            }
        });
        //playButton.setOnAction(e -> simulation.run());
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                simulation.stop();
            }
        });
        //stopButton.setOnAction(e -> simulation.stop());
	Button resetButton = new Button("Reset");
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent t) {
		    simulation.reset();
		}
	    });
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(playButton, stopButton, resetButton);

        /* combobox to select speed of simulation */
        ComboBox<String> selectSpeed = new ComboBox<String>();
        selectSpeed.getItems().addAll(
                "Slow",
                "Normal",
                "Fast" //,
                //"Instant"
        );
        selectSpeed.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                switch (selectSpeed.getValue()) {
                    case "Slow":
                        SoccerGUI.timeSetting = SoccerGUI.TIMER_SLOW;
                        break;
                    case "Normal":
                        SoccerGUI.timeSetting = SoccerGUI.TIMER_NORMAL;
                        break;
                    case "Fast":
                        SoccerGUI.timeSetting = SoccerGUI.TIMER_FAST;
                        break;
                }
            }
        });
        /*
        selectSpeed.setOnAction(event -> {
            switch (selectSpeed.getValue()) {
                case "Slow":
                    SoccerGame.timeSetting = SoccerGame.TIMER_SLOW;
                    break;
                case "Normal":
                    SoccerGame.timeSetting = SoccerGame.TIMER_NORMAL;
                    break;
                case "Fast":
                    SoccerGame.timeSetting = SoccerGame.TIMER_FAST;
                    break;

                case "Instant":
                    SoccerGame.timeSetting = SoccerGame.TIMER_INSTANT;
                    break;

            }
        });
        */
        selectSpeed.setPromptText("Set Speed");

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hBox, selectSpeed);


        /* Adds panes and combo boxes to option pane */
        borderPane.setLeft(selectEastTeam);
        BorderPane.setMargin(selectEastTeam, new Insets(0, 0, 10, 10));
        BorderPane.setMargin(vBox, new Insets(0, 0, 10, 0));
        BorderPane.setAlignment(vBox, Pos.CENTER);
        borderPane.setCenter(vBox);
        borderPane.setRight(selectWestTeam);
        BorderPane.setMargin(selectWestTeam, new Insets(0, 10, 10, 0));

        return borderPane;
    }

    /**
     * Sets the action listener for the combobox to use a dynamically loaded class
     *
     * @param comboBox that needs action listener set
     * @param team direction
     */
    private void setTeam(ComboBox<String> comboBox, final int team) {
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                /* restarts simulation and updates score */
                eastScore = 0;
                westScore = 0;
                updateScore();
                simulation.restart();

                String name = comboBox.getValue();

                ArrayList<Class<Team>> teamClass = teamLoader.getTeamClass();

                /* finds the index of the selected value in teamClass */
                int teamIndex = 0;
                for (int i = 0; i < teamClass.size(); ++i) {
                    if (teamClass.get(i).getName().equals(TeamLoader.TEAM_PACKAGE + name)) {
                        teamIndex = i;
                    }
                }

                /* Loads Class */
                if (team == SoccerGame.PLAYER_WEST) {
                    try {
                        Main.eastTeam = teamClass.get(teamIndex).newInstance();
                        eastName.setText(Main.eastTeam.teamName());
                    }
                    catch (IllegalAccessException e1) {
                        System.out.println("Something went wrong.");
                    }
                    catch (InstantiationException e2) {
                        System.out.println("Something went wrong.");
                    }
                }
                else {
                    try {
                        Main.westTeam = teamClass.get(teamIndex).newInstance();
                        westName.setText(Main.westTeam.teamName());
                    }
                    catch (InstantiationException e1) {
                        System.out.println("Something went wrong.");
                    }
                    catch (IllegalAccessException e2) {
                        System.out.println("Something went wrong.");
                    }
                }
            }
        });
        /*
        comboBox.setOnAction(e -> {

        });
        */
    }

    /**
     * Creates the pane that is used for the scores.
     *
     * @return panes of the scores
     */
    private BorderPane initializeScorePane() {
        BorderPane borderPane = new BorderPane();
        /* Team Scores listed here */
        eastScore = 0;
        westScore = 0;

        /* sets team name text */
        eastName = new Text(Main.eastTeam.teamName());
        westName = new Text(Main.westTeam.teamName());
        eastName.setFont(new Font(20));
        eastName.setFill(Color.BLUE);
        westName.setFont(new Font(20));
        westName.setFill(Color.RED);

        /* adds score to a plane so that they wont resize when the text is changed */
        StackPane eastPane = new StackPane();
        StackPane westPane = new StackPane();
        eastPane.setPrefWidth(200);
        westPane.setPrefWidth(200);
        eastPane.setAlignment(Pos.CENTER_RIGHT);
        westPane.setAlignment(Pos.CENTER_LEFT);
        eastPane.getChildren().add(eastName);
        westPane.getChildren().add(westName);

        /* sets score */
        scores = new Text(westScore + SCORE_TEXT_SPACE + eastScore);
        scores.setFill(Color.BLACK);
        scores.setFont(new Font(20));

        /* Adds scores and names to score Pane */
        BorderPane.setMargin(westPane, new Insets(0, 0, 10, 10));
        borderPane.setLeft(westPane);
        BorderPane.setAlignment(scores, Pos.CENTER);
        BorderPane.setMargin(scores, new Insets(0, 0, 10, 0));
        borderPane.setCenter(scores);
        BorderPane.setMargin(eastPane, new Insets(0, 10, 10, 0));
        borderPane.setRight(eastPane);

        return borderPane;
    }

    /**
     * Updates the score so that changes to eastScore and wesScore
     * appear in the GUI.
     */
    public static void updateScore() {
        scores.setText(westScore + SCORE_TEXT_SPACE + eastScore);
    }
}
