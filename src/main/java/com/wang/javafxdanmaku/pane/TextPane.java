package com.wang.javafxdanmaku.pane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TextPane extends Application {

    private int i = 0;

    @Override
    public void start(Stage primaryStage) {
        var root = new AnchorPane();
        var anchorPane = new AnchorPane();

        root.setStyle("-fx-background-color: #00000000");
        anchorPane.setStyle("-fx-background-color: #000000CC");

        anchorPane.prefHeightProperty().bind(root.heightProperty());
        anchorPane.prefWidthProperty().bind(root.widthProperty());

        var vBox = new VBox(7);
        AnchorPane.setBottomAnchor(vBox, 10.0);

        root.getChildren().addAll(anchorPane);

        var rectangle = new Rectangle();

        rectangle.widthProperty().bind(anchorPane.prefWidthProperty());

//        root.setClip(rectangle);

        Platform.runLater(() -> {
            rectangle.setHeight(anchorPane.getPrefHeight());
            rectangle.setLayoutY(anchorPane.getPrefHeight() - 55);
        });

        var add = new Button("增加");
        var remove = new Button("删除");

        add.setOnAction(event -> {
            var children = vBox.getChildren();
            if (children.size() >= 24) {
                rectangle.setLayoutY(rectangle.getLayoutY() + 23);
                children.remove(0);
            }
            if (children.size() >= 2) {
                rectangle.setLayoutY(rectangle.getLayoutY() - 23);
            }
            var label = new Label("test" + i++);
            label.setTextFill(Color.WHITE);
            children.add(label);
        });

        remove.setOnAction(event -> {
            var children = vBox.getChildren();
            if (!children.isEmpty()) {
                if (children.size() > 2) {
                    rectangle.setLayoutY(rectangle.getLayoutY() + 23);
                }
                children.remove(0);
            }
        });

        anchorPane.getChildren().addAll(add, remove, vBox);

        AnchorPane.setLeftAnchor(vBox, 10.0);

        AnchorPane.setRightAnchor(add, 10.0);
        AnchorPane.setBottomAnchor(add, 10.0);

        AnchorPane.setRightAnchor(remove, 100.0);
        AnchorPane.setBottomAnchor(remove, 10.0);

        var scene = new Scene(root);
        scene.setFill(null);

        primaryStage.setWidth(600);
        primaryStage.setHeight(600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.setTitle("bilibili - danmaku");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }
}
