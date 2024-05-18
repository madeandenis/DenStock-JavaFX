package com.example.denstockjavafx.utils.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TimeZoneClocks {
    private final Map<String, Label[]> clocks;
    private final HBox clocksBox;

    public TimeZoneClocks() {
        clocks = new HashMap<>();
        clocksBox = new HBox(10);
        clocksBox.setAlignment(Pos.CENTER);

        createClocks();
    }

    public void createClocks(){
        createClock("Romania", "Europe/Bucharest");
        createClock("USA", "America/New_York");
        createClock("Tokyo", "Asia/Tokyo");
    }
    public HBox getClocksBox() {
        return clocksBox;
    }

    private void createClock(String label, String timeZoneId) {
        Label cityLabel = new Label(label);
        Label hourLabel = new Label();
        Label minuteLabel = new Label();
        Label secondLabel = new Label();

        cityLabel.getStyleClass().add("city-label");
        hourLabel.getStyleClass().add("time-label");
        minuteLabel.getStyleClass().add("time-label");
        secondLabel.getStyleClass().add("time-label");

        clocks.put(label, new Label[]{hourLabel, minuteLabel, secondLabel});

        updateClock(timeZoneId, hourLabel, minuteLabel, secondLabel);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateClock(timeZoneId, hourLabel, minuteLabel, secondLabel))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        HBox timeBox = new HBox(5, hourLabel, minuteLabel, secondLabel);
        VBox clockBox = new VBox(cityLabel, timeBox);

        clockBox.getStyleClass().add("clock-box");
        clockBox.setAlignment(Pos.CENTER);

        clocksBox.getChildren().add(clockBox);
    }

    private void updateClock(String timeZoneId, Label hourLabel, Label minuteLabel, Label secondLabel) {
        LocalTime currentTime = LocalTime.now(ZoneId.of(timeZoneId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        hourLabel.setText(currentTime.format(formatter).substring(0, 2));
        minuteLabel.setText(currentTime.format(formatter).substring(3, 5));
        secondLabel.setText(currentTime.format(formatter).substring(6));
    }
}
