package com.example.denstockjavafx.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public record StockInfo(String ticker, String price, String changeAmount, String changePercentage, String volume) {

    // StringProperty allows binding properties directly to UI elements
    public StringProperty tickerProperty() {
        return new SimpleStringProperty(ticker);
    }

    public StringProperty priceProperty() {
        return new SimpleStringProperty(price);
    }

    public StringProperty changeAmountProperty() {
        return new SimpleStringProperty(changeAmount);
    }

    public StringProperty changePercentageProperty() {
        return new SimpleStringProperty(changePercentage);
    }

    public StringProperty volumeProperty() {
        return new SimpleStringProperty(volume);
    }
}
