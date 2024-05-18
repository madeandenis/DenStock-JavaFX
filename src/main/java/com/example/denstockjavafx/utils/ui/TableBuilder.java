package com.example.denstockjavafx.utils.ui;

import com.example.denstockjavafx.models.StockInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class TableBuilder {

    public static TableView<StockInfo> buildStockInfoTable(List<Map<String, String>> data) {
        TableView<StockInfo> tableView = new TableView<>();

        TableColumn<StockInfo, String> tickerColumn = new TableColumn<>("Ticker");
        TableColumn<StockInfo, String> priceColumn = new TableColumn<>("Price");
        TableColumn<StockInfo, String> changeAmountColumn = new TableColumn<>("Change Amount");
        TableColumn<StockInfo, String> changePercentageColumn = new TableColumn<>("Change Percentage");
        TableColumn<StockInfo, String> volumeColumn = new TableColumn<>("Volume");

        // Extracts property values from StockInfo objects
        tickerColumn.setCellValueFactory(cellData -> cellData.getValue().tickerProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        changeAmountColumn.setCellValueFactory(cellData -> cellData.getValue().changeAmountProperty());
        changePercentageColumn.setCellValueFactory(cellData -> cellData.getValue().changePercentageProperty());
        volumeColumn.setCellValueFactory(cellData -> cellData.getValue().volumeProperty());

        tickerColumn.getStyleClass().add("ticker-column");
        volumeColumn.getStyleClass().add("volume-column");

        tableView.getColumns().addAll(tickerColumn, priceColumn, changeAmountColumn, changePercentageColumn, volumeColumn);

        ObservableList<StockInfo> stockInfoList = FXCollections.observableArrayList();
        for (Map<String, String> stockElement : data) {
            stockInfoList.add(new StockInfo(
                    stockElement.get("ticker"),
                    stockElement.get("price"),
                    stockElement.get("change_amount"),
                    stockElement.get("change_percentage"),
                    stockElement.get("volume")
            ));
        }
        tableView.setItems(stockInfoList);

        return tableView;
    }

}
