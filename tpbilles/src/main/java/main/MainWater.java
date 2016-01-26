package main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import core.Environnement;
import core.SMA;
import core.billes.Directions;
import core.wator.Nemo;
import core.wator.Shark;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import tools.Randomizer;

public class MainWater extends Application {

	private static int largeur = 200;
	private static int hauteur = 200;
	private static int nbShark = 1000;
	private static int nbNemo = 2500;
	private static int tempsAttente = 120;
	private static int tempsArret = 0;
	private static final int SIZE = 5;
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

		canvas = new Pane();
		final Scene scene = new Scene(canvas, largeur, hauteur);

		// for(int i = SIZE; i < hauteur; i += SIZE ){
		// canvas.getChildren().addAll(new Line(0, i, largeur, i));
		// }
		// for(int i = SIZE; i < largeur; i += SIZE ){
		// canvas.getChildren().addAll(new Line(i, 0, i, hauteur));
		// }
		primaryStage.setTitle("Chase me");
		primaryStage.setScene(scene);
		primaryStage.show();

		// Ajout des requins
		for (int i = 0; i < nbShark; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
					final int posY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
					final Directions direction = Directions
							.values()[Randomizer.randomGenerator.nextInt(Directions.values().length - 1) + 1];

					Shark shark = new Shark(env, sma, posX, posY, direction);
					sma.addAgent(shark);

					ok = true;

					circleObs.add(shark.getCircle());
				} catch (IllegalArgumentException ignore) {
				}
			}
		}

		// ajout de nemoes
		for (int i = 0; i < nbNemo; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
					final int posY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
					final Directions direction = Directions
							.values()[Randomizer.randomGenerator.nextInt(Directions.values().length - 1) + 1];

					Nemo nemo = new Nemo(env, sma, posX, posY, direction);
					sma.addAgent(nemo);
					ok = true;

					circleObs.add(nemo.getCircle());
				} catch (IllegalArgumentException ignore) {
				}
			}

		}

		canvas.getChildren().addAll(circleObs);

		final long start = Calendar.getInstance().getTimeInMillis();
		final long stop = tempsArret * 1000;
		int nbTours = 0;
		double tempsTotalRun = 0;

		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(33), new EventHandler<ActionEvent>() {

			public void handle(final ActionEvent t) {

				sma.run();

			}
		}));

		loop.setCycleCount(Timeline.INDEFINITE);

		PieChart.Data sharkData = new PieChart.Data("shark", 40);
		PieChart.Data nemoData = new PieChart.Data("nemo", 60);

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(sharkData, nemoData);
		final PieChart chart = new PieChart(pieChartData);

		chart.setTitle("Proportion");

		((Group) scene.getRoot()).getChildren().add(chart);

		primaryStage.setScene(scene);
		primaryStage.show();

		pieChartData.get(1).setPieValue(Math.random() * 100);
		loop.play();
	}

}
