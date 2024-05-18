package com.example.denstockjavafx.windows;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Objects;

public class TickerInfo {
    private static Scene createTickerInfoScene(String tickerSymbol, Map<String, String> tickerDetails) {
        ListView<String> tickerDetailsView = new ListView<>();
        ObservableList<String> listViewItems = tickerDetailsView.getItems();

        String stockName = "";

        for (Map.Entry<String, String> entry : tickerDetails.entrySet()) {
            String itemContent = entry.getKey() + " : " + entry.getValue();
            listViewItems.add(itemContent);

            if (Objects.equals(entry.getKey(), "name")) {
                stockName = entry.getValue();
            }
        }

        Label titleLabel = new Label(tickerSymbol + " - " + stockName);
        titleLabel.setStyle("-fx-background-color: #151515;" +
                " -fx-text-fill: #ACBA21;" +
                " -fx-font-size: 20px;" +
                " -fx-padding: 10;" +
                " -fx-pref-width: 500;" +
                " -fx-pref-height: 20;");

        VBox root = new VBox(titleLabel, tickerDetailsView);

        Scene scene = new Scene(root, 230, 400);
        scene.getStylesheets().add(Objects.requireNonNull(TickerInfo.class.getResource("/css/listViewStyle.css")).toExternalForm());
        return scene;
    }

    public static void displayTickerInfoStage(String selectedItem, Map<String, String> selectedTickerInfo) {
        Stage stage = new Stage();
        stage.setTitle(selectedItem);
        stage.setScene(createTickerInfoScene(selectedItem, selectedTickerInfo));
        stage.show();
    }
}
