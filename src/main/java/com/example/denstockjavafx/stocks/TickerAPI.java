package com.example.denstockjavafx.stocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.denstockjavafx.clients.ApiClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TickerAPI {

    public enum TickerJSONKeys {
        symbol,
// Uncomment for future further implementations
//        id,
//        name,
//        type,
//        region,
//        marketOpen,
//        marketClose,
//        timezone,
//        currency,
//        matchScore
    }

    private final String apiKey;
    public TickerAPI(String apiKey){
        this.apiKey = apiKey;
    }
    private JsonObject searchForSymbolMatches(String keyword, String apiKey) throws IOException {
        String url = String.format(
                "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=%s&apikey=%s", keyword, apiKey
        );
        return ApiClient.executeGetRequest(url);
    }

    public ArrayList<Map<String,String>> getBestMatches(String keyword) throws IOException {
        JsonObject result = this.searchForSymbolMatches(keyword,this.apiKey);

        JsonArray bestMatchesJson = (JsonArray) result.get("bestMatches");
        ArrayList<Map<String,String>> bestMatches = new ArrayList<>();

        for (Object obj : bestMatchesJson){
            JsonObject match = (JsonObject) obj;

            Map<String,String> matchHashMap= new HashMap<>();
            for(String key : match.keySet()){
                String keyValue = match.get(key).getAsString();
                key = key.substring(3);
                matchHashMap.put(key,keyValue);
            }
            bestMatches.add(matchHashMap);
        }

        return bestMatches;
    }

    public ArrayList<String> findKeyValues(ArrayList<Map<String,String>>matches, TickerJSONKeys keyName) throws IOException {

        ArrayList<String> matchingSymbols = new ArrayList<>();

        for (Map<String,String> match : matches) {
            matchingSymbols.add(match.get(keyName.toString()));
        }

        return  matchingSymbols;
    }



}
