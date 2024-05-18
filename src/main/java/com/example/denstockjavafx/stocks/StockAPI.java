package com.example.denstockjavafx.stocks;

import com.example.denstockjavafx.clients.ApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockAPI {

    private final String apiKey;
    public StockAPI(String apiKey){
        this.apiKey = apiKey;
    }
    private JsonObject fetchIntradayStockData(String symbol, int interval) throws IOException {
        String url = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=%dmin&apikey=%s",
                symbol, interval, apiKey
        );
        return ApiClient.executeGetRequest(url);
    }
    private JsonObject fetchDailyStockData(String symbol) throws IOException {
        String url = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s",
                symbol,apiKey
        );
        return ApiClient.executeGetRequest(url);
    }
    private JsonObject fetchWeeklyStockData(String symbol) throws IOException {
        String url = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=%s&apikey=%s",
                symbol, apiKey
        );
        return ApiClient.executeGetRequest(url);
    }
    private JsonObject fetchMonthlyStockData(String symbol) throws IOException {
        String url = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=%s&apikey=%s",
                symbol, apiKey
        );
        return ApiClient.executeGetRequest(url);
    }

    public JsonObject fetchTopGainersAndLosersData() throws IOException {
        String url = String.format(
                "https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=%s",
                apiKey
        );
        return ApiClient.executeGetRequest(url);
    }

    private ArrayList<Map<String, String>> parseStockDataFromJson(JsonObject data, String timeSeriesKey) {

        ArrayList<Map<String, String>> parsedData = new ArrayList<>();

        JsonObject timeSeries = data.getAsJsonObject(timeSeriesKey);

        for (String timestamp : timeSeries.keySet()) {
            JsonObject timestampObject = (JsonObject) timeSeries.get(timestamp);

            Map<String, String> stockInfo = new HashMap<>();
            stockInfo.put("timestamp", timestamp);

            for (String key : timestampObject.keySet()) {
                // Remove the numbering from the key
                String keyValue = timestampObject.get(key).getAsString();
                key = key.substring(3);
                stockInfo.put(key, keyValue);
            }

            parsedData.add(stockInfo);
        }
        return parsedData;
    }

    public ArrayList<Map<String, String>> getIntradayStocks(String symbol, int interval) throws IOException {
        JsonObject result = fetchIntradayStockData(symbol, interval);
        String timeSeriesKey = String.format("Time Series (%dmin)", interval);
        return parseStockDataFromJson(result,timeSeriesKey);
    }

    public ArrayList<Map<String, String>> getDailyStocks(String symbol) throws IOException {
        JsonObject result = fetchDailyStockData(symbol);
        String timeSeriesKey = "Time Series (Daily)";
        return parseStockDataFromJson(result, timeSeriesKey);
    }
    public ArrayList<Map<String, String>> getWeeklyStocks(String symbol) throws IOException {
        JsonObject result = fetchWeeklyStockData(symbol);
        String timeSeriesKey = "Weekly Time Series";
        return parseStockDataFromJson(result, timeSeriesKey);
    }

    public ArrayList<Map<String, String>> getMonthlyStocks(String symbol) throws IOException {
        JsonObject result = fetchMonthlyStockData(symbol);
        String timeSeriesKey = "Monthly Time Series";
        return parseStockDataFromJson(result, timeSeriesKey);
    }

    private ArrayList<Map<String, String>> parseStockMarketInfoFromJson(JsonObject data, String categoryKey) {
        ArrayList<Map<String, String>> parsedData = new ArrayList<>();

        JsonElement categoryElement = data.get(categoryKey);

        if (categoryElement.isJsonArray()) {
            JsonArray categoryArray = categoryElement.getAsJsonArray();
            for (JsonElement element : categoryArray) {
                JsonObject stockDataObj = element.getAsJsonObject();
                Map<String, String> stockInfo = new HashMap<>();

                for (Map.Entry<String, JsonElement> entry : stockDataObj.entrySet()) {
                    String infoKey = entry.getKey();
                    String infoValue = entry.getValue().getAsString();
                    stockInfo.put(infoKey, infoValue);
                }
                parsedData.add(stockInfo);
            }
        }

        return parsedData;
    }

    public ArrayList<Map<String, String>> getTopGainers() throws IOException {
        JsonObject result = fetchTopGainersAndLosersData();
        String categoryKey = "top_gainers";
        return parseStockMarketInfoFromJson(result,categoryKey);
    }
    public ArrayList<Map<String, String>> getTopLosers() throws IOException {
        JsonObject result = fetchTopGainersAndLosersData();
        String categoryKey = "top_losers";
        return parseStockMarketInfoFromJson(result,categoryKey);
    }
    public ArrayList<Map<String, String>> getMostActivelyTraded() throws IOException {
        JsonObject result = fetchTopGainersAndLosersData();
        String categoryKey = "most_actively_traded";
        return parseStockMarketInfoFromJson(result,categoryKey);
    }


}
