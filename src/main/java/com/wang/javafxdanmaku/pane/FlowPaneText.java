package com.wang.javafxdanmaku.pane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class FlowPaneText extends Application {
    @Override
    public void start(Stage primaryStage) {
        var vBox = new VBox(5);

        var pane = new HBox();
        var label11 = new Label("aaaa");
        var label21 = new Label("字字字字字字字字字字字字字字字字字字字字");

        HBox.setMargin(label21, new Insets(0, 0, 0, 50));

        label21.setWrapText(true);
        label11.setTextFill(Color.BLUE);
        label21.setTextFill(Color.RED);

        Platform.runLater(() -> {
            System.out.println(label21.getWidth());
            System.out.println(label21.getPrefWidth());
        });

//        label21.maxWidthProperty().bind(vBox.widthProperty().subtract(label11.widthProperty()));

        pane.getChildren().addAll(label11, label21);

        var hBox2 = new HBox(5);
        var label12 = new Label("aaaa");
        var label22 = new Label("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        label12.setWrapText(true);
        label22.setWrapText(true);

        hBox2.getChildren().addAll(label12, label22);

        vBox.getChildren().addAll(pane, hBox2);

        Scene scene = new Scene(vBox);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("bilibili - danmaku");
        primaryStage.setScene(scene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.show();
    }
}
