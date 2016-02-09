package main;

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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.cli.*;
import tools.DijkstraCalculator;
import tools.Randomizer;

import java.util.ArrayList;
import java.util.Calendar;


public class MainEscape extends Application {
	/*
	 * figer le jeu quand on est attrapp�
	 * 
	 * vitesses variables
	 * 
	 * rapport :
	 * 
	 * Un UML g�n�ral sur notre projet global
	 * 
	 * 3 partie => une par TP avec pour chaque le graph UML correspondant +
	 * expliqation sur lancement et organisation
	 * 
	 * 
	 */

	// Environment fields

	static Options options = new Options();

	private static int largeur = 130;
	private static int hauteur = 130;
	private static int nbHunter = 5;
	private static int tempsAttente = 120;
	private static int tempsArret = 0;
	private static int pourcentageMur = 5;
	private static int vitesseAvatar = 1;
	private static long seed = Calendar.getInstance().getTimeInMillis();

	// Escape fields
	public static Directions direction = Directions.IMMOBILE;

	public static ArrayList<Rectangle> murs;
	public static ObservableList<Rectangle> mursObs;
	public static ObservableList<Circle> units;

	public static PlayerAgent playerAgent;

	public static Pane canvas;
	public static DijkstraCalculator dijkstra;

	// Param fields
	public static String LARGEUR = "largeur";
	public static String HAUTEUR = "hauteur";
	public static String NB_HUNTER = "nbHunter";
	public static String POURCENTAGE_MUR = "pourcentageMur";
	public static String VITESSE_AVATAR = "vitesseAvatar";

	public static int roundCount = 0;

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
					final int posX = Randomizer.randomGenerator.nextInt(environnement.getLocations()[0].length - 1);
					final int posY = Randomizer.randomGenerator.nextInt(environnement.getLocations().length - 1);

					ZombieAgent hunter = new ZombieAgent(environnement, sma, posX, posY);

					if (environnement.getLocations()[posY][posX] == null) {
						sma.addAgent(hunter);
						ok = true;
					} else {
						canvas.getChildren().removeAll(hunter.getRectangle());

					}
				} catch (IllegalArgumentException ignore) {
					System.out.println("Error");
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
				canvas.getChildren().removeAll(playerAgent.getRectangle());
			}
		}

		dijkstra = new DijkstraCalculator(environnement);

		final Scene scene = new Scene(canvas, largeur * 5, hauteur * 5);
		canvas.getChildren().addAll(mursObs);

		scene.setOnKeyPressed(event -> {
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
		});

		primaryStage.setTitle("Run forest run !");
		primaryStage.setScene(scene);
		primaryStage.show();

		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(73), new EventHandler<ActionEvent>() {
			public void handle(final ActionEvent t) {
				sma.run();
			}
		}));

		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();
	}

	public static void main(String[] args) {

		// g�re les arguments
		options.addOption(LARGEUR, false, "largeur de la grille");
		options.addOption(HAUTEUR, false, "hauteur de la grille");
		options.addOption(NB_HUNTER, false, "nombre de chasseurs");
		options.addOption(POURCENTAGE_MUR, false, "pourcentage de mur pr�sent dans la grille");
		options.addOption(VITESSE_AVATAR, false, "vitesse du joueur par rapport aux chasseurs");

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Hunter", options);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (cmd.hasOption(LARGEUR)) {
			largeur = Integer.parseInt(cmd.getOptionValue(LARGEUR));
		}

		if (cmd.hasOption(HAUTEUR)) {
			hauteur = Integer.parseInt(cmd.getOptionValue(HAUTEUR));
		}

		if (cmd.hasOption(NB_HUNTER)) {
			nbHunter = Integer.parseInt(cmd.getOptionValue(NB_HUNTER));
		}
		if (cmd.hasOption(POURCENTAGE_MUR)) {
			pourcentageMur = Integer.parseInt(cmd.getOptionValue(POURCENTAGE_MUR));
		}
		if (cmd.hasOption(VITESSE_AVATAR)) {
			vitesseAvatar = Integer.parseInt(cmd.getOptionValue(VITESSE_AVATAR));
		}
		launch(args);

	}

}
