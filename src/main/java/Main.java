import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.canvas.*;

/**
 * Created by Grant Cooksey on 8/23/15.
 * Purpose: Builds the field drawing and is the driver for the program.
 */

public class Main extends Application {

    public static final int GRASS_WIDTH = 600;
    public static final int GRASS_HEIGHT = 300;

    protected static Team eastTeam, westTeam;
    protected static Pane game;

    /**
     * Builds a graphical drawing of a soccer field/
     *
     * @param length of field
     *
     * @return Canvas of the field
     */
    private static Canvas field(int length) {
        Canvas field = new Canvas(length, length / 2);
        GraphicsContext gc = field.getGraphicsContext2D();

        /* Sets the green field stripes */
        for (int i = 0; i < 600; i += 40) {
            if (i % 80 == 0) {
                gc.setFill(Color.rgb(75, 141, 14));
                gc.fillRect(i, 0, 40, length / 2);
            }
            else {
                gc.setFill(Color.rgb(72, 169, 0));
                gc.fillRect(i, 0, 40, length / 2);
            }
        }

        /* Sets the field markings */
        gc.setLineWidth(2.0);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        //bounderies
        gc.strokeLine(15, 5, 15, 295);
        gc.strokeLine(15, 295, 585, 295);
        gc.strokeLine(585, 295, 585, 5);
        gc.strokeLine(15, 5, 585, 5);
        //middle line
        gc.strokeLine(300, 5, 300, 295);
        gc.fillOval(293, 143, 14, 14);
        gc.strokeOval(246, 96, 108, 108);
        //goal box
        gc.strokeLine(15, 57, 96, 57);
        gc. strokeLine(96, 57, 96, 243);
        gc.strokeLine(15, 243, 96, 243);
        gc.strokeLine(585, 57, 504, 57);
        gc.strokeLine(504, 57, 504, 243);
        gc.strokeLine(585, 243, 504, 243);
        //6 yard box
        gc.strokeLine(15, 108, 40, 108);
        gc.strokeLine(40, 108, 40, 192);
        gc.strokeLine(15, 192, 40, 192);
        gc.strokeLine(585, 108, 560, 108);
        gc.strokeLine(560, 108, 560, 192);
        gc.strokeLine(585, 192, 560, 192);

        return field;
    }

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        VBox vbox = new VBox(10);

        /* Sets default teams */
        //eastTeam = new NowhereNancy();
        //westTeam = new NowhereNancy();

        /* Graphical representation of the field */
        Canvas canvas = field(GRASS_WIDTH);

        /* Pane in which the players and the ball are drawn */
        game = new Pane();
        game.setPrefSize(GRASS_WIDTH, GRASS_HEIGHT);

        Pane scoreBoard = new SimulationPane();

        StackPane stackPane = new StackPane();

        stackPane.getChildren().addAll(canvas, game);
        vbox.getChildren().addAll(stackPane, scoreBoard);
        root.getChildren().add(vbox);

        Scene scene = new Scene(root);
        scene.setFill(Color.LIGHTGREY);
        primaryStage.setTitle("Soccer Server");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //SoccerBatch soccerBatch = new SoccerBatch();

        if (args.length == 1 && args[0].equals("batch")) {
            SoccerBatch soccerBatch = new SoccerBatch();
        }
        else {
            launch(args);
        }

    }
}