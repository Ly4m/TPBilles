package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import core.Environnement;
import core.SMA;
import core.billes.Bille;
import core.billes.Directions;
import core.wator.Mur;
import core.wator.Nemo;
import core.wator.Shark;
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

public class MainWater extends Application {

    private static int largeur = 8500;
    private static int hauteur = 4200;
    private static int nbShark = 10;
    private static int nbNemo = 50;
    private static int tempsAttente = 120;
    private static int tempsArret = 0;
    private static long seed = Calendar.getInstance().getTimeInMillis();

    public static List<Circle> circle;

    public static ObservableList<Circle> circleObs;
    public static Pane canvas;

    
	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {

        circle = new ArrayList<Circle>();

        circleObs = FXCollections.observableArrayList(circle);

        Randomizer.setSeed(seed);
        final Environnement env = new Environnement(largeur, hauteur);
        final SMA sma = new SMA(env);
        boolean ok = false;

//         Ajout des murs
        final int nbCasesY = hauteur / 5;
        final int nbCasesX = largeur / 5;
        for (int i = 0; i < nbCasesY; i++) {
            sma.addAgent(new Mur(env, sma, 0, i, Mur.TypeMur.VERTICAL));
            sma.addAgent(new Mur(env, sma, nbCasesX - 1, i, Mur.TypeMur.VERTICAL));
        }
        for (int i = 1; i < nbCasesX - 1; i++) {
            sma.addAgent(new Mur(env, sma, i, 0, Mur.TypeMur.HORIZONTAL));
            sma.addAgent(new Mur(env, sma, i, nbCasesY - 1, Mur.TypeMur.HORIZONTAL));
        }


        canvas = new Pane();
        final Scene scene = new Scene(canvas, largeur / 5, hauteur / 5);


        primaryStage.setTitle("Chase me");
        primaryStage.setScene(scene);
        primaryStage.show();


//         Ajout des requins
        for (int i = 0; i < nbShark; i++) {
            ok = false;
            while (!ok) {
                try {
                    final int posX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
                    final int posY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
                    final Directions direction = Directions.values()[Randomizer.randomGenerator
                            .nextInt(Directions.values().length - 1) + 1];

                    Shark shark = new Shark(env, sma, posX, posY, direction);
                    sma.addAgent(shark);

                    ok = true;


                    circleObs.add(shark.getCircle());
                } catch (IllegalArgumentException ignore) {
                }
            }


        }
        
        for (int i = 0; i < nbNemo; i++) {
            ok = false;
            while (!ok) {
                try {
                    final int posX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
                    final int posY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
                    final Directions direction = Directions.values()[Randomizer.randomGenerator
                            .nextInt(Directions.values().length - 1) + 1];

                    Nemo nemo = new Nemo(env, sma, posX, posY, direction);
                    sma.addAgent(nemo);

                    ok = true;


                    circleObs.add(nemo.getCircle());
                } catch (IllegalArgumentException ignore) {
                }
            }


        }


//        circle.forEach(b -> canvas.getChildren().addAll(b));

    canvas.getChildren().addAll(circleObs);

        final long start = Calendar.getInstance().getTimeInMillis();
        final long stop = tempsArret * 1000;
        int nbTours = 0;
        double tempsTotalRun = 0;

        System.out.println(env.getLocations().length);

        final Timeline loop = new Timeline(new KeyFrame(Duration.millis(5), new EventHandler<ActionEvent>() {


            public void handle(final ActionEvent t) {


                sma.run();

            }
        }));


        loop.setCycleCount(Timeline.INDEFINITE);
        loop.play();
	}

}
