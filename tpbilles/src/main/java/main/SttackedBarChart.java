package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class SttackedBarChart extends Application {
	@Override
	public void start(Stage stage) throws InterruptedException {
		Scene scene = new Scene(new Group());
		stage.setTitle("Wator");
		stage.setWidth(500);
		stage.setHeight(500);
		PieChart.Data sharkData = new PieChart.Data("shark", 40);
		PieChart.Data nemoData = new PieChart.Data("nemo", 60);

		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(sharkData, nemoData);
		final PieChart chart = new PieChart(pieChartData);

		chart.setTitle("Proportion");

		((Group) scene.getRoot()).getChildren().add(chart);

		stage.setScene(scene);
		stage.show();

		pieChartData.get(1).setPieValue(Math.random() * 100);

	}

	public static void main(String[] args) {
		launch(args);
	}
}