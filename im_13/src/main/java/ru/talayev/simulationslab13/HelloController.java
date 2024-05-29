package ru.vorotov.simulationslab13;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController implements Initializable {
    @FXML
    private TextField dollarTextField;
    @FXML
    private TextField euroTextField;
    @FXML
    private TextField daysField;
    @FXML
    private LineChart<Integer, Double> chart;

    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private final double k = 0.02;
    Random random = new Random();

    double dollarPrice, euroPrice;
    int days;

    int counter;
    boolean isRunning;

    double drift = 0.1;
    double volatility = 0.1;
    double dt = 0.1;

    public void onCalcButtonClick(ActionEvent actionEvent) {
        isRunning = true;
        chart.getData().clear();
        // инициализация графика
        XYChart.Series<Integer, Double> dollarSeries = new XYChart.Series<>();
        XYChart.Series<Integer, Double> euroSeries = new XYChart.Series<>();
        dollarSeries.setName("Dollar");
        euroSeries.setName("Euro");
        chart.getData().add(dollarSeries);
        chart.getData().add(euroSeries);

        dollarPrice = Double.parseDouble(dollarTextField.getText());
        euroPrice = Double.parseDouble(euroTextField.getText());
        days = Integer.parseInt(daysField.getText());
        dollarSeries.getData().add(new XYChart.Data<>(0, dollarPrice));
        euroSeries.getData().add(new XYChart.Data<>(0, euroPrice));
        counter = 0;

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                dollarPrice = calculate(dollarPrice);
                euroPrice = calculate(euroPrice);
                Platform.runLater(() -> dollarSeries.getData().add(new XYChart.Data<>(counter, dollarPrice)));
                Platform.runLater(() -> euroSeries.getData().add(new XYChart.Data<>(counter, euroPrice)));
                counter++;
                if (!isRunning) {
                    timer.cancel();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 100);
    }

    public void onStopButtonClick(ActionEvent actionEvent) {
        isRunning = false;
    }

    private double calculate(double price) {
        double tempGenNorm = generateNormalRandom(random,drift,volatility);
        double temp = price * Math.pow(Math.E, (drift - volatility / 2.0) * dt + Math.sqrt(volatility) * Math.sqrt(dt) * tempGenNorm);
        return temp;
    }

    private double generateNormalRandom(Random random, double drift, double vol) {
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();

        double res = Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);

        return res;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
    }
}