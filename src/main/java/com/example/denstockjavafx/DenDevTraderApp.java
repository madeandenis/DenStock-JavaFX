package com.example.denstockjavafx;

import com.example.denstockjavafx.windows.StockChart;
import com.example.denstockjavafx.windows.StockList;
import com.example.denstockjavafx.windows.TickerSearch;
import com.example.denstockjavafx.utils.ui.TimeZoneClocks;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;

import java.io.File;
import java.util.Objects;

import java.awt.*;


public class DenDevTraderApp extends Application {

    private VBox root;
    private HBox header;
    private HBox tabBar;
    private HBox tabList;
    private ScrollPane workspaceContainer;
    private FlowPane workspace;

    private void init(Stage primaryStage) {

        header = createHeader();
        tabBar = createTabBar();

        workspace = new FlowPane();

        workspaceContainer = new ScrollPane();
        workspaceContainer.setFitToWidth(true);
        workspaceContainer.setFitToHeight(true);
        workspaceContainer.setContent(workspace);

        root = new VBox(header,tabBar,workspaceContainer);

        root.getStyleClass().add("root-container");
        workspace.getStyleClass().add("windows-container");
        workspaceContainer.getStyleClass().add("scroll-container");

        Dimension monitorSize = Toolkit.getDefaultToolkit().getScreenSize();
        Scene mainScene = new Scene(root, monitorSize.getWidth() / 1.5, monitorSize.getHeight() / 1.1);
        addCssStyles(mainScene);

        primaryStage.setTitle("DenStock");
        primaryStage.setScene(mainScene);
    }

    private void addCssStyles(Scene scene) {
        File cssDir = new File("src/main/resources/css");
        File[] cssFiles = cssDir.listFiles();

        assert cssFiles != null;

        for (File cssFile : cssFiles) {
            if (cssFile.isFile()) {
                scene.getStylesheets().add(
                        Objects.requireNonNull(
                                this.getClass().getResource("/css/" + cssFile.getName())
                        ).toExternalForm()
                );
            }
        }
    }


    // Components methods
    private HBox createHeader(){
        Label logo = new Label("DenDev\nTrader");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox clocksBox = new TimeZoneClocks().getClocksBox();

        HBox header = new HBox(logo,spacer,clocksBox);

        header.getStyleClass().add("header");
        logo.getStyleClass().add("logo");

        return header;
    }

    public HBox createTabBar(){
        tabList = new HBox();
        tabList.setSpacing(5);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> tabOpener = new ComboBox<>();
        tabOpener.setPrefWidth(150);
        tabOpener.getItems().addAll("Stock Chart", "Search Ticker", "Top Gainer", "Top Losers", "Most actively traded");
        tabOpener.setValue("Stock Chart");

        setupSelectionListener(tabOpener,tabList);

        tabBar = new HBox(tabList,spacer,tabOpener);
        tabBar.getStyleClass().add("tab-bar");

        return tabBar;
    }
    private void setupSelectionListener(ComboBox<String> comboBox, HBox tabList) {
        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedValue) -> {
            if (selectedValue != null) {
                Label tabLabel = new Label(selectedValue);
                tabLabel.getStyleClass().add("tab-label");

                tabList.getChildren().add(tabLabel);
                createWindow(selectedValue);
            }
        });
    }

    public void createWindow(String selectedValue){
        Window window = new Window(selectedValue);
        switch (selectedValue){
            case "Stock Chart":
                window.setPrefSize(800,500);
                break;
            case "Search Ticker":
                window.setPrefSize(200,400);
                break;
            case "Top Gainer", "Top Losers", "Most actively traded":
                window.setPrefSize(460,400);
                break;
        }
        window.getRightIcons().add(new CloseIcon(window));
        window.getRightIcons().add(new MinimizeIcon(window));

        window.setTitleBarStyleClass("window-titlebar");

        window.setOnCloseAction(event -> {
            for (Node node : tabList.getChildren()) {
                if ( ((Label) node).getText().equals(selectedValue)) {
                    tabList.getChildren().remove(node);
                    break;
                }
            }
        });

        Node windowContent = getWindowContent(selectedValue);
        window.getContentPane().getChildren().add(windowContent);

        workspace.getChildren().add(window);
    }

    private Node getWindowContent(String selectedValue) {
        switch (selectedValue) {
            case "Stock Chart" -> {
                return StockChart.buildStockChart();
            }
            case "Search Ticker" -> {
                return TickerSearch.createTickerSearch();
            }
            case "Top Gainer" -> {
                return StockList.createStockList(StockList.Filter.TopGainers);
            }
            case "Top Losers" -> {
                return StockList.createStockList(StockList.Filter.TopLosers);
            }
            case "Most actively traded" -> {
                return StockList.createStockList(StockList.Filter.MostActivelyTraded);
            }
            default -> {
                return null;
            }
        }
    }


    @Override
    public void start(Stage primaryStage){
        init(primaryStage);
        primaryStage.show();

    }
    public static void main(String[] args) {launch(args);}

}