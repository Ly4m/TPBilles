package main;

import java.util.ArrayList;
import java.util.Calendar;

import core.Directions;
import core.Environnement;
import core.SMA;
import core.escape.MurAgent;
import core.escape.PlayerAgent;
import core.escape.ZombieAgent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import tools.Randomizer;

/**
 * Created by leemans on 27/01/16.
 */
public class MainEscape extends Application {

	// Environment fields

	private static int largeur = 175;
	private static int hauteur = 100;
	private static int nbHunter = 4;
	private static int tempsAttente = 120;
	private static int tempsArret = 0;
	private static int pourcentageMur = 0;
	private static long seed = Calendar.getInstance().getTimeInMillis();

	// Escape fields
	public static Directions direction = Directions.IMMOBILE;

	public static ArrayList<Rectangle> murs;
	public static ObservableList<Rectangle> mursObs;
	public static ObservableList<Circle> units;

	public static PlayerAgent playerAgent;

	public static Pane canvas;
	public static int[][] dijkstra;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// units = new FXCollections.observableArrayList(units);
		murs = new ArrayList<Rectangle>();
		mursObs = FXCollections.observableArrayList(murs);

		canvas = new Pane();

		Randomizer.setSeed(seed);
		final Environnement environnement = new Environnement(largeur, hauteur);
		final SMA sma = new SMA(environnement);

		int wallToAdd = hauteur * largeur * pourcentageMur / 100;
		dijkstra = new int[largeur][hauteur];

		// Ajout des murs extérieurs
		final int nbCasesY = hauteur;
		final int nbCasesX = largeur;
		for (int i = 0; i < nbCasesY; i++) {
			System.out.println("i : " + i);
			sma.addAgent((new MurAgent(environnement, sma, 0, i)));
			sma.addAgent((new MurAgent(environnement, sma, nbCasesX - 1, i)));
		}
		for (int i = 1; i < nbCasesX - 1; i++) {
			sma.addAgent(new MurAgent(environnement, sma, i, 0));
			sma.addAgent(new MurAgent(environnement, sma, i, nbCasesY - 1));
		}

		// Ajout des murs intérieurs
		boolean ok = false;
		for (int i = 0; i < wallToAdd; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt(environnement.getLocations()[0].length);
					final int posY = Randomizer.randomGenerator.nextInt(environnement.getLocations().length);

					MurAgent mur = new MurAgent(environnement, sma, posX, posY);
					sma.addAgent(mur);

					ok = true;

				} catch (IllegalArgumentException ignore) {
				}
			}

		}

		// Ajout des chasseurs
		for (int i = 0; i < nbHunter; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt(environnement.getLocations()[0].length);
					final int posY = Randomizer.randomGenerator.nextInt(environnement.getLocations().length);

					ZombieAgent hunter = new ZombieAgent(environnement, sma, posX, posY);
					sma.addAgent(hunter);

					ok = true;

				} catch (IllegalArgumentException ignore) {
				}
			}

		}

		// Ajout du joueur
		ok = false;
		while (!ok) {
			try {
				final int posX = Randomizer.randomGenerator.nextInt(environnement.getLocations()[0].length);
				final int posY = Randomizer.randomGenerator.nextInt(environnement.getLocations().length);

				playerAgent = new PlayerAgent(environnement, sma, posX, posY);
				sma.addAgent(playerAgent);

				ok = true;

			} catch (IllegalArgumentException ignore) {
			}
		}

		final Scene scene = new Scene(canvas, largeur * 5, hauteur * 5);
		canvas.getChildren().addAll(mursObs);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP:
					playerAgent.setDirection(Directions.HAUT);
					break;
				case DOWN:
					playerAgent.setDirection(Directions.BAS);
					break;
				case LEFT:
					playerAgent.setDirection(Directions.GAUCHE);
					break;
				case RIGHT:
					playerAgent.setDirection(Directions.DROITE);
					break;
				default:
					break;
				}
			}

		});
		
		dijkstraInit();
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

	private void dijkstraInit() {
		//init du tableau de valeur
		for(int i=0; i<largeur; i++){
			for(int j=0; j<hauteur; j++){
				dijkstra[i][j] = -1	;
			}
		}		
	}

	public static void main(String[] args) {

		launch(args);

	}

}
