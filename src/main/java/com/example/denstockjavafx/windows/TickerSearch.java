package com.example.denstockjavafx.windows;

import com.example.denstockjavafx.stocks.TickerAPI;
import com.example.denstockjavafx.utils.providers.ApiKeysProvider;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class TickerSearch {

    private static final TickerAPI TICKER_API = new TickerAPI(ApiKeysProvider.AlphaKey);
    private static ArrayList<Map<String, String>> searchResults;
    private static ListView<String> searchResultsView;


    public static Node createTickerSearch() {
        TextField searchField = createSearchField();
        Button searchButton = createSearchButton();

        searchField.getStyleClass().add("stock-text-field");
        searchButton.getStyleClass().add("stock-button");

        HBox searchBar = createSearchBar(searchField, searchButton);

        searchResultsView = new ListView<>();
        searchResultsView.setStyle("-fx-font-size: 12px;");

        searchButton.setOnMouseClicked(event -> searchButtonClicked(searchField));
        searchResultsView.setOnMouseClicked(TickerSearch::searchResultViewClicked);

        return createRoot(searchBar, searchResultsView);
    }

    private static TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPrefWidth(200);
        return searchField;
    }

    private static Button createSearchButton() {
        Button searchButton = new Button();
        HBox.setMargin(searchButton, new Insets(-2, 0, 0, -35));

        String searchIconPath = "/images/search.png";
        ImageView searchIcon = new ImageView(new Image(Objects.requireNonNull(TickerSearch.class.getResourceAsStream(searchIconPath))));
        searchIcon.setFitWidth(16);
        searchIcon.setFitHeight(16);
        searchButton.setGraphic(searchIcon);

        return searchButton;
    }

    private static HBox createSearchBar(TextField searchField, Button searchButton) {
        HBox searchBar = new HBox(searchField, searchButton);

        searchBar.setPadding(new Insets(0, 0, 15, 0));
        HBox.setMargin(searchButton, new Insets(5, 0, 0, -35));

        return searchBar;
    }

    private static void searchButtonClicked(TextField searchField) {
        try {
            String keyword = searchField.getText();

            searchResults = TICKER_API.getBestMatches(keyword);
            ArrayList<String> matchingSymbols = TICKER_API.findKeyValues(searchResults, TickerAPI.TickerJSONKeys.symbol);

            searchResultsView.setItems(FXCollections.observableArrayList(matchingSymbols));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void searchResultViewClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            String selectedItem = searchResultsView.getSelectionModel().getSelectedItem();
            int selectedItemIndex = searchResultsView.getSelectionModel().getSelectedIndex();
            if (selectedItem != null) {
                TickerInfo.displayTickerInfoStage(selectedItem, searchResults.get(selectedItemIndex));
            }
        }
    }

    private static VBox createRoot(HBox searchBar, ListView<String> resultListView) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #202020; -fx-padding: 15px;");
        root.getChildren().addAll(searchBar, resultListView);
        return root;
    }

}
