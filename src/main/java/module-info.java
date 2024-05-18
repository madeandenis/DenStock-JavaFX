module com.example.denstockjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires jfxtras.labs;
    requires java.desktop;
    requires java.logging;

    exports com.example.denstockjavafx.windows;

    opens com.example.denstockjavafx to javafx.fxml;
    exports com.example.denstockjavafx;
    exports com.example.denstockjavafx.clients;
    opens com.example.denstockjavafx.clients to javafx.fxml;
    exports com.example.denstockjavafx.stocks;
    opens com.example.denstockjavafx.stocks to javafx.fxml;
    exports com.example.denstockjavafx.charts.candlestick;
    opens com.example.denstockjavafx.charts.candlestick to javafx.fxml;
    opens com.example.denstockjavafx.models;
}
