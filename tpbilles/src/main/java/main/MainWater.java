package main;

import core.Directions;
import core.Environnement;
import core.SMA;
import core.wator.Nemo;
import core.wator.Shark;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.cli.*;
import tools.Randomizer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWater extends Application {

	static Options options = new Options();

	private static final int MAX_DATA_POINTS = 200;
	private XYChart.Series sharkSeries;
	private XYChart.Series nemoSeries;
	private int xSeriesData = 0;
	private ConcurrentLinkedQueue<Number> sharkDataQ = new ConcurrentLinkedQueue<Number>();
	private ConcurrentLinkedQueue<Number> nemoDataQ = new ConcurrentLinkedQueue<Number>();
	private ExecutorService executor;
	private AddToQueue addToQueue;
	private Timeline timeline2;
	private NumberAxis xAxis;

	// LEGIT
	private static int largeur = 150;
	private static int hauteur = 175;
	private static int nbShark = 120;
	private static int nbNemo = 600;
	private static int survieShark = 3;
	private static int reproductionShark = 9;
	private static int reproductionNemo = 2;
	private static int tempsAttente = 120;
	private static int tempsArret = 0;
	private static final int SIZE = 5;
	private static long seed = Calendar.getInstance().getTimeInMillis();

	public static List<Circle> Circle;

	public static int nemoCount = nbNemo;
	public static int sharkCount = nbShark;

	public static ObservableList<Circle> CircleObs;
	public static Pane canvas;

	public static String NB_SHARK = "nbShark";
	public static String NB_NEMO = "nbNemo";
	public static String SURVIE_SHARK = "survieShark";
	public static String REPRODUCTION_SHARK = "reproductionShark";
	public static String REPRODUCTION_NEMO = "reproductionNemo";
	public static String LARGEUR = "largeur";
	public static String HAUTEUR = "hauteur";

	public static void main(String[] args) {

		// g�re les arguments
		options.addOption(NB_SHARK, true, "nombre de requin au d�part");
		options.addOption(NB_NEMO, true, "nombre de poissons au d�part");
		options.addOption(SURVIE_SHARK, true, "nombre de tours durant lesquels un requin survis sans manger");
		options.addOption(REPRODUCTION_SHARK, true, "nombre de tours avant la reproduction des requins");
		options.addOption(REPRODUCTION_NEMO, true, "nombre de tours avant la reproduction des poissons");
		options.addOption(HAUTEUR, true, "hauteur de la grille");
		options.addOption(LARGEUR, true, "largeur de la grille");

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Wator", options);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (cmd.hasOption(NB_SHARK)) {
			System.out.println(cmd.getOptionValue(NB_SHARK));
			nbShark = Integer.parseInt(cmd.getOptionValue(NB_SHARK));
		}

		if (cmd.hasOption(NB_NEMO)) {
			nbNemo = Integer.parseInt(cmd.getOptionValue(NB_NEMO));
		}

		if (cmd.hasOption(SURVIE_SHARK)) {
			survieShark = Integer.parseInt(cmd.getOptionValue(SURVIE_SHARK));
		}

		if (cmd.hasOption(REPRODUCTION_NEMO)) {
			reproductionNemo = Integer.parseInt(cmd.getOptionValue(REPRODUCTION_NEMO));
		}

		if (cmd.hasOption(REPRODUCTION_SHARK)) {
			reproductionShark = Integer.parseInt(cmd.getOptionValue(REPRODUCTION_SHARK));
		}
		if (cmd.hasOption(LARGEUR)) {
			largeur = Integer.parseInt(cmd.getOptionValue(LARGEUR));
		}
		if (cmd.hasOption(HAUTEUR)) {
			hauteur = Integer.parseInt(cmd.getOptionValue(HAUTEUR));
		}

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

		Group root = new Group();
		canvas = new Pane();
		HBox hbox = new HBox();
		hbox.getChildren().add(canvas);
		root.getChildren().add(hbox);
		final Scene scene = new Scene(root, largeur * 5, hauteur * 5);

		primaryStage.setTitle("Chase me");

		// Ajout des requins
		for (int i = 0; i < nbShark; i++) {
			ok = false;
			while (!ok) {
				try {
					final int posX = Randomizer.randomGenerator.nextInt(env.getLocations()[0].length);
					final int posY = Randomizer.randomGenerator.nextInt(env.getLocations().length);
					final Directions direction = Directions
							.values()[Randomizer.randomGenerator.nextInt(Directions.values().length - 1) + 1];

					Shark shark = new Shark(env, sma, posX, posY, direction, survieShark, reproductionShark);
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

					Nemo nemo = new Nemo(env, sma, posX, posY, direction, reproductionNemo);
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

		PieChart.Data sharkData = new PieChart.Data("shark", sharkCount);
		PieChart.Data nemoData = new PieChart.Data("nemo", nemoCount);

		final ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(sharkData, nemoData);
		final PieChart chart = new PieChart(pieChartData);

		chart.setTitle("Proportion");
		VBox vbox = new VBox();
		vbox.getChildren().add(chart);

		hbox.getChildren().addAll(vbox);

		// KENJI
		xAxis = new NumberAxis(0, MAX_DATA_POINTS, MAX_DATA_POINTS / 10);
		xAxis.setForceZeroInRange(false);
		xAxis.setAutoRanging(false);

		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(true);

		// -- Chart
		final LineChart<Number, Number> sc = new LineChart<Number, Number>(xAxis, yAxis) {
			// Override to remove symbols on each data point
			@Override
			protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {
			}
		};
		sc.setAnimated(false);
		sc.setId("PopulationChart");
		sc.setTitle("Popululation chart");

		// -- Chart Series
		sharkSeries = new AreaChart.Series<Number, Number>();
		sharkSeries.setName("Shark");
		sc.getData().add(sharkSeries);

		nemoSeries = new AreaChart.Series<Number, Number>();
		nemoSeries.setName("Nemo");
		sc.getData().add(nemoSeries);

		hbox.getChildren().addAll(sc);

		primaryStage.setScene(scene);
		primaryStage.show();

		// -- Prepare Executor Services
		executor = Executors.newCachedThreadPool();
		addToQueue = new AddToQueue();
		executor.execute(addToQueue);
		// -- Prepare Timeline
		prepareTimeline();

		final Timeline loop = new Timeline(new KeyFrame(Duration.millis(60), new EventHandler<ActionEvent>() {

			public void handle(final ActionEvent t) {

				sma.run();
				pieChartData.get(0).setPieValue(sharkCount);
				pieChartData.get(1).setPieValue(nemoCount);

			}
		}));

		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();
	}

	private class AddToQueue implements Runnable {
		public void run() {
			try {
				// add a item of random data to queue
				sharkDataQ.add(sharkCount);
				nemoDataQ.add(nemoCount);
				Thread.sleep(60);
				executor.execute(this);
			} catch (InterruptedException ex) {
			}
		}
	}

	// -- Timeline gets called in the JavaFX Main thread
	private void prepareTimeline() {
		// Every frame to take any data from queue and add to chart
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				addDataToSeries();
			}
		}.start();
	}

	private void addDataToSeries() {
		for (int i = 0; i < 20; i++) { // -- add 20 numbers to the plot+
			if (sharkDataQ.isEmpty())
				break;
			sharkSeries.getData().add(new AreaChart.Data(xSeriesData++, sharkDataQ.remove()));
		}

		for (int i = 0; i < 20; i++) { // -- add 20 numbers to the plot+
			if (nemoDataQ.isEmpty())
				break;
			nemoSeries.getData().add(new AreaChart.Data(xSeriesData, nemoDataQ.remove()));
		}
		// remove points to keep us at no more than MAX_DATA_POINTS
		if (sharkSeries.getData().size() > MAX_DATA_POINTS) {
			sharkSeries.getData().remove(0, sharkSeries.getData().size() - MAX_DATA_POINTS);
		}
		if (nemoSeries.getData().size() > MAX_DATA_POINTS) {
			nemoSeries.getData().remove(0, nemoSeries.getData().size() - MAX_DATA_POINTS);
		}
		// update
		xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
		xAxis.setUpperBound(xSeriesData - 1);
	}

}
