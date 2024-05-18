package com.example.denstockjavafx.windows;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.denstockjavafx.charts.candlestick.BarData;
import com.example.denstockjavafx.charts.candlestick.CandleStickChart;
import com.example.denstockjavafx.utils.providers.ApiKeysProvider;
import com.example.denstockjavafx.stocks.StockAPI;
import static javafx.application.Application.launch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StockChart {

    public static Node buildStockChart(){
        VBox root = new VBox();
        root.getStyleClass().add("stock-chart-root");

        HBox selectorSection = createSelectorOption();

        TextField searchField = createSearchField();
        Button searchButton = createSearchButton();
        ComboBox<String> timeIntervalSelector = createTimeIntervalSelector();

        searchField.getStyleClass().add("stock-text-field");
        searchButton.getStyleClass().add("stock-button");
        timeIntervalSelector.getStyleClass().add("stock-combo-box");

        selectorSection.getChildren().addAll(searchField, searchButton, timeIntervalSelector);

        root.getChildren().add(selectorSection);

        // Builds chart upon button click
        searchButton.setOnMouseClicked(mouseEvent -> {

            StockAPI stockAPI = new StockAPI(ApiKeysProvider.AlphaKey);
            ArrayList<Map<String, String>> stocks;
            try {

                String symbol = searchField.getText();
                String timeInterval = timeIntervalSelector.getValue();

                CandleStickChart candleStickChart;
                switch (timeInterval){
                    case "1 Day" -> {
                        stocks = stockAPI.getIntradayStocks(symbol,5);
                        candleStickChart = new CandleStickChart(symbol + " - " + timeInterval, buildBars(stocks), new SimpleDateFormat(" HH:mm"));
                    }
                    case "1 Week" -> {
                        stocks = getLatestIntervalData(stockAPI.getDailyStocks(symbol),Calendar.WEEK_OF_YEAR);
                        candleStickChart = new CandleStickChart(symbol + " - " + timeInterval, buildBars(stocks), new SimpleDateFormat("yyyy-MM-dd"));
                    }
                    case "1 Month" -> {
                        stocks = getLatestIntervalData(stockAPI.getDailyStocks(symbol),Calendar.MONTH);
                        candleStickChart = new CandleStickChart(symbol + " - " + timeInterval, buildBars(stocks), new SimpleDateFormat("yyyy-MM-dd"));
                    }
                    case "1 Year" -> {
                        stocks = getLatestIntervalData(stockAPI.getDailyStocks(symbol),Calendar.YEAR);
                        candleStickChart = new CandleStickChart(symbol + " - " + timeInterval, buildBars(stocks), new SimpleDateFormat("yyyy-MM-dd"));
                    }
                    default -> {
                        candleStickChart = null;
                    }
                }

                // Remove old chart if a new one is requested
                if (root.getChildren().size() >= 2){
                    root.getChildren().remove(root.getChildren().size()-1);
                }
                root.getChildren().add(candleStickChart);

            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
            catch (NullPointerException e){
                if (root.getChildren().size() >= 2){
                    root.getChildren().remove(root.getChildren().size()-1);
                }

                Label notFoundLabel = new Label("Ticker was not found");
                notFoundLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: rgb(255,255,255,0.40);\n");

                HBox centerContainer = new HBox();
                centerContainer.setAlignment(Pos.CENTER);
                VBox.setVgrow(centerContainer, Priority.ALWAYS);
                centerContainer.getChildren().add(notFoundLabel);

                root.getChildren().add(centerContainer);

                Duration lifeDuration = Duration.seconds(2);
                Timeline timeline = new Timeline(new KeyFrame(lifeDuration, event -> {
                    root.getChildren().remove(centerContainer);
                }));
                timeline.play();
            }
        });

        return root;
    }

    private static HBox createSelectorOption(){
        HBox selectorSection = new HBox();
        selectorSection.setPadding(new Insets(0, 10, 20, 0));
        return selectorSection;
    }
    private static TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPrefWidth(200);
        return searchField;
    }

    private static Button createSearchButton() {
        Button searchButton = new Button();
        HBox.setMargin(searchButton, new Insets(3, 0, 0, -35));

        String searchIconPath = "/images/search.png";
        ImageView searchIcon = new ImageView(new Image(Objects.requireNonNull(StockChart.class.getResourceAsStream(searchIconPath))));
        searchIcon.setFitWidth(16);
        searchIcon.setFitHeight(16);
        searchButton.setGraphic(searchIcon);

        return searchButton;
    }

    private static ComboBox<String> createTimeIntervalSelector() {
        ComboBox<String> timeIntervalSelector = new ComboBox<>();

        timeIntervalSelector.setPadding(new Insets(5,10,5,5));
        HBox.setMargin(timeIntervalSelector, new Insets(0, 0, 0, 20));

        timeIntervalSelector.getItems().addAll("1 Day", "1 Week", "1 Month", "1 Year");
        timeIntervalSelector.setValue("1 Day");

        return timeIntervalSelector;
    }
    private static ArrayList<Map<String, String>> getLatestIntervalData(ArrayList<Map<String, String>> stockData, int field) {
        ArrayList<Map<String, String>> filteredStockData = new ArrayList<>();

        Calendar cutoffDate = Calendar.getInstance();
        cutoffDate.add(field, -1);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        for (Map<String, String> stock : stockData) {
            try {
                Calendar stockRecordDate = Calendar.getInstance();
                stockRecordDate.setTime(dateFormatter.parse(stock.get("timestamp")));

                if (stockRecordDate.after(cutoffDate) || stockRecordDate.equals(cutoffDate)) {
                    filteredStockData .add(stock);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filteredStockData ;
    }

    // BarData holds the information about every Candlestick component
    private static List<BarData> buildBars(ArrayList<Map<String, String>> stocks) throws ParseException {

        final List<BarData> bars = new ArrayList<>();

        for (Map<String, String> stock : stocks){
            double open = Double.parseDouble(stock.get("open"));
            double close = Double.parseDouble(stock.get("close"));
            double high = Double.parseDouble(stock.get("high"));
            double low = Double.parseDouble(stock.get("low"));
            long volume = Long.parseLong(stock.get("volume"));

            String timestampString = stock.get("timestamp");
            GregorianCalendar calendarTimestamp = getGregorianCalendar(timestampString);

            BarData bar = new BarData(calendarTimestamp, open, high, low, close, volume);
            bars.add(bar);
        }

        bars.sort(new BarDataComparator());
        return bars;
    }

    private static GregorianCalendar getGregorianCalendar(String timestampString) throws ParseException {
        SimpleDateFormat dateFormat;

        if (timestampString.length() == 10) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        Date timestamp = dateFormat.parse(timestampString);
        GregorianCalendar calendarTimestamp = new GregorianCalendar();
        calendarTimestamp.setTime(timestamp);

        return calendarTimestamp;
    }

    public static class BarDataComparator implements Comparator<BarData> {
        @Override
        public int compare(BarData date1, BarData date2) {
            long timeInMillis1 = date1.getDateTime().getTimeInMillis();
            long timeInMillis2 = date2.getDateTime().getTimeInMillis();
            return Long.compare(timeInMillis1, timeInMillis2);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}
