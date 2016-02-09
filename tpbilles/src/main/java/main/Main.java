package main;

import core.AgentPhysique;
import core.Environnement;
import core.SMA;
import core.billes.Bille;
import core.Directions;
import core.billes.Mur;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import tools.Randomizer;

import java.util.*;

public class Main extends Application {

    private static int largeur = 150;
    private static int hauteur = 250;
    private static int nbAgents = 1000;
    private static int tempsAttente = 120;
    private static int tempsArret = 0;
    private boolean torique = false;
    private static long seed = Calendar.getInstance().getTimeInMillis();

    public static List<Circle> Circle;

    public static ObservableList<Circle> CircleObs;
    public static Pane canvas;


    @Override
    public void start(Stage primaryStage) throws Exception {

        Circle = new ArrayList<>();

        CircleObs = FXCollections.observableArrayList(Circle);

        Randomizer.setSeed(seed);
        final Environnement env = new Environnement(largeur, hauteur);
        final SMA sma = new SMA(env);
        boolean ok;

//         Ajout des murs
        final int nbCasesY = hauteur;
        final int nbCasesX = largeur;
        if(!torique) {
        for (int i = 0; i < nbCasesY; i++) {
            sma.addAgent(new Mur(env, sma, 0, i, Mur.TypeMur.VERTICAL));
            sma.addAgent(new Mur(env, sma, nbCasesX - 1, i, Mur.TypeMur.VERTICAL));
        }
        for (int i = 1; i < nbCasesX - 1; i++) {
            sma.addAgent(new Mur(env, sma, i, 0, Mur.TypeMur.HORIZONTAL));
            sma.addAgent(new Mur(env, sma, i, nbCasesY - 1, Mur.TypeMur.HORIZONTAL));
        }
        }



        canvas = new Pane();
        final Scene scene = new Scene(canvas, largeur * 5, hauteur * 5);


        primaryStage.setTitle("Bouncy Bounce");
        primaryStage.setScene(scene);
        primaryStage.show();


//         Ajout des agents
        for (int i = 0; i < nbAgents; i++) {
            ok = false;
            while (!ok) {
                try {
                    final int posX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
                    final int posY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
                    final Directions direction = Directions.values()[Randomizer.randomGenerator
                            .nextInt(Directions.values().length - 1) + 1];

                    Bille bille = new Bille(env, sma, posX, posY, direction);
                    sma.addAgent(bille);

                    ok = true;

                    CircleObs.add(bille.getCircle());
                } catch (IllegalArgumentException ignore) {
                }
            }


        }


//        Circle.forEach(b -> canvas.getChildren().addAll(b));

    canvas.getChildren().addAll(CircleObs);

        final long start = Calendar.getInstance().getTimeInMillis();
        final long stop = tempsArret * 1000;
        int nbTours = 0;
        double tempsTotalRun = 0;

        System.out.println(env.getLocations().length);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(10), t -> {

            sma.run();

        }));


        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
