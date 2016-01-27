package main;

import core.Directions;
import core.Environnement;
import core.SMA;
import core.billes.Bille;
import core.escape.MurAgent;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import tools.Randomizer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by leemans on 27/01/16.
 */
public class MainEscape extends Application {

    // Environment fields

    private static int largeur = 175;
    private static int hauteur = 100;
    private static int nbAgents = 500;
    private static int tempsAttente = 120;
    private static int tempsArret = 0;
    private static int pourcentageMur = 20;
    private static long seed = Calendar.getInstance().getTimeInMillis();

    // Escape fields
    public static Directions direction = Directions.IMMOBILE;

    public static ArrayList<Rectangle> murs;
    public static ObservableList<Rectangle> mursObs;
    public static ObservableList<Circle> units;

    public static Pane canvas;


    @Override
    public void start(Stage primaryStage) throws Exception {

//        units = new FXCollections.observableArrayList(units);
        murs = new ArrayList<Rectangle>();
        mursObs = FXCollections.observableArrayList(murs);

        canvas = new Pane();


        Randomizer.setSeed(seed);
        final Environnement environnement = new Environnement(largeur, hauteur);
        final SMA sma = new SMA(environnement);

        int wallToAdd = hauteur*largeur*20/100;

        //Ajout des murs
        final int nbCasesY = hauteur;
        final int nbCasesX = largeur;
        for (int i = 0; i < nbCasesY; i++) {
            System.out.println("i : "+i);
            sma.addAgent((new MurAgent(environnement, sma, 0, i)));
            sma.addAgent((new MurAgent(environnement, sma, nbCasesX - 1, i)));
        }
        for (int i = 1; i < nbCasesX - 1; i++) {
            sma.addAgent(new MurAgent(environnement, sma, i, 0));
            sma.addAgent(new MurAgent(environnement, sma, i, nbCasesY - 1));
        }

        boolean ok = false;
        for(int i =0; i<wallToAdd; i++){
            ok = false;
            Color couleur = Color.CORNFLOWERBLUE;
            while (!ok) {
                try {
                    final int posX = Randomizer.randomGenerator.nextInt(environnement.getLocations()[0].length);
                    final int posY = Randomizer.randomGenerator.nextInt(environnement.getLocations().length);
                    final Directions direction = Directions.values()[Randomizer.randomGenerator
                            .nextInt(Directions.values().length - 1) + 1];

//                    Bille bille = new Bille(environnement, sma, posX, posY, direction);
//                    sma.addAgent(bille);

                    ok = true;


//                    mursObs.add(bille.getCircle());
                } catch (IllegalArgumentException ignore) {
                }
            }

        }

        final Scene scene = new Scene(canvas, largeur * 5, hauteur * 5);
        canvas.getChildren().addAll(mursObs);

        primaryStage.setTitle("Catch me bitch(es) !");
        primaryStage.setScene(scene);
        primaryStage.show();


        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(5), new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent t) {
                sma.run();
            }
        }));

        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
    }

    public static void main(String[] args) {

        launch(args);


    }

}
