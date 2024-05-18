package com.example.denstockjavafx.windows;

import com.example.denstockjavafx.models.StockInfo;
import com.example.denstockjavafx.utils.providers.ApiKeysProvider;
import com.example.denstockjavafx.stocks.StockAPI;
import com.example.denstockjavafx.utils.ui.TableBuilder;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
public class StockList {

    public enum Filter {
        TopGainers,
        TopLosers,
        MostActivelyTraded

    }
    public static Node createStockList(Filter listFilter) {
        ArrayList<Map<String, String>> tableData = fetchStockListData(listFilter);
        TableView<StockInfo> tableView = TableBuilder.buildStockInfoTable(tableData);

        int indexOfChangeAmountColumn = 2;
        int indexOfChangePercentageColumn = 3;

        if (listFilter == Filter.TopGainers) {
            tableView.getColumns().get(indexOfChangeAmountColumn).getStyleClass().add("positive-change-amount");
            tableView.getColumns().get(indexOfChangePercentageColumn).getStyleClass().add("positive-change-percentage");
        } else if (listFilter == Filter.TopLosers) {
            tableView.getColumns().get(indexOfChangeAmountColumn).getStyleClass().add("negative-change-amount");
            tableView.getColumns().get(indexOfChangePercentageColumn).getStyleClass().add("negative-change-percentage");
        }

        return tableView;
    }


    private static ArrayList<Map<String, String>> fetchStockListData(Filter sceneType) {
        try {
            StockAPI stockAPI = new StockAPI(ApiKeysProvider.AlphaKey);

            ArrayList<Map<String, String>> tableData = new ArrayList<>();

            if (sceneType == Filter.TopGainers) {
                tableData = stockAPI.getTopGainers();
            } else if (sceneType == Filter.TopLosers) {
                tableData = stockAPI.getTopLosers();
            } else if (sceneType == Filter.MostActivelyTraded) {
                tableData = stockAPI.getMostActivelyTraded();
            }

            return tableData;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
