package main;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import tools.Randomizer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainWater extends Application {

	private static int largeur = 100;
	private static int hauteur = 100;
	private static int nbShark = 80;
	private static int nbNemo = 600;
	private static int tempsAttente = 120;
	private static int tempsArret = 0;
	private static final int SIZE = 5;
	private static long seed = Calendar.getInstance().getTimeInMillis();

	public static List<Circle> Circle;

	public static int nemoCount = nbNemo;
	public static int sharckCount = nbShark;


	public static ObservableList<Circle> CircleObs;
	public static Pane canvas;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Circle = new ArrayList<Circle>();

		CircleObs = FXCollections.observableArrayList(Circle);

		Randomizer.setSeed(seed);
		final Environnement env = new Environnement(largeur, hauteur);
		final SMA sma = new SMA(env);
		boolean ok = false;

		Group root  = new Group();
		canvas = new Pane();
		HBox hbox = new HBox(canvas);
		root.getChildren().add(hbox);
		final Scene scene = new Scene(root, largeur * 5, hauteur * 5);

		primaryStage.setTitle("Chase me");

//		 Ajout des requins
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

					CircleObs.add(shark.getCircle());
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

					CircleObs.add(nemo.getCircle());
				} catch (IllegalArgumentException ignore) {
				}
			}

		}

		canvas.getChildren().addAll(CircleObs);

		final long start = Calendar.getInstance().getTimeInMillis();
		final long stop = tempsArret * 1000;
		int nbTours = 0;
		double tempsTotalRun = 0;





		PieChart.Data sharkData = new PieChart.Data("shark", sharckCount);
		PieChart.Data nemoData = new PieChart.Data("nemo", nemoCount);

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(sharkData, nemoData);
		final PieChart chart = new PieChart(pieChartData);

		chart.setTitle("Proportion");

		hbox.getChildren().addAll(chart);

		primaryStage.setScene(scene);
		primaryStage.show();


		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(60), new EventHandler<ActionEvent>() {

			public void handle(final ActionEvent t) {

				sma.run();
				pieChartData.get(0).setPieValue(sharckCount);
				pieChartData.get(1).setPieValue(nemoCount);

			}
		}));

		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();
	}

}
