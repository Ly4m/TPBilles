package main;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

public class AreaChartSample extends Application {
	private static final int MAX_DATA_POINTS = 50;

	private Series sharkSeries;
	private Series nemoSeries;
	private int xSeriesData = 0;
	private ConcurrentLinkedQueue<Number> sharkDataQ = new ConcurrentLinkedQueue<Number>();
	private ConcurrentLinkedQueue<Number> nemoDataQ = new ConcurrentLinkedQueue<Number>();
	private ExecutorService executor;
	private AddToQueue addToQueue;
	private Timeline timeline2;
	private NumberAxis xAxis;

	private void init(Stage primaryStage) {
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
		sc.setTitle("Poululation chart");

		// -- Chart Series
		sharkSeries = new AreaChart.Series<Number, Number>();
		sharkSeries.setName("Shark");
		sc.getData().add(sharkSeries);

		nemoSeries = new AreaChart.Series<Number, Number>();
		nemoSeries.setName("Nemo");
		sc.getData().add(nemoSeries);

		primaryStage.setScene(new Scene(sc));
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
		primaryStage.show();

		// -- Prepare Executor Services
		executor = Executors.newCachedThreadPool();
		addToQueue = new AddToQueue();
		executor.execute(addToQueue);
		// -- Prepare Timeline
		prepareTimeline();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private class AddToQueue implements Runnable {
		public void run() {
			try {
				// add a item of random data to queue
				sharkDataQ.add(Math.random() * 100);
				nemoDataQ.add(Math.random() * 100);

				Thread.sleep(50);
				executor.execute(this);
			} catch (InterruptedException ex) {
				Logger.getLogger(AreaChartSample.class.getName()).log(Level.SEVERE, null, ex);
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