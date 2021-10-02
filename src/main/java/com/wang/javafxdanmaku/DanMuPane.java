package com.wang.javafxdanmaku;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class DanMuPane extends Application {

    private final Font messageFont = Font.loadFont(FontsResourcesPath.SIYUANREGULAR, 15);
    private int i = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(320);
        anchorPane.setPrefHeight(50);

        anchorPane.setStyle("-fx-background-color: rgba(0,0,0,0.80)");

        var font = Font.loadFont(FontsResourcesPath.SIYUANREGULAR, 15);

        var bottomView = new HBox(16);

        var followHBox = new HBox(65);
        var followNumValue = new Label("78");
        var followNumLabel = new Label("关注人数");


        followNumValue.setFont(font);
        followNumValue.setTextFill(Color.WHITE);

        followNumLabel.setFont(font);
        followNumLabel.setTextFill(Color.WHITE);
        followHBox.getChildren().addAll(followNumLabel, followNumValue);

        var viewNumHBox = new HBox(74);
        var viewNumValue = new Label("1");
        var viewNumLabel = new Label("房间人气");

        viewNumValue.setFont(font);
        viewNumValue.setTextFill(Color.WHITE);
        viewNumLabel.setFont(font);
        viewNumLabel.setTextFill(Color.WHITE);
        viewNumHBox.getChildren().addAll(viewNumLabel, viewNumValue);

        bottomView.getChildren().addAll(viewNumHBox, followHBox);
        anchorPane.getChildren().addAll(bottomView);

        VBox messages = new VBox(7);

        var button = new Button("添加消息");

        var myScheduleServices = new MyScheduleServices(messages, primaryStage);
        myScheduleServices.setPeriod(Duration.millis(100));

        var scene = new Scene(anchorPane);
        scene.setFill(null);

        var screenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> {
            primaryStage.setX(65);
            primaryStage.setY((screenBounds.getHeight() + 65));
            myScheduleServices.start();
        });

        button.setOnAction(event -> {

            primaryStage.setY(primaryStage.getY() - 22);
            var message = getMessage();
            var timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);

            message.setUserData(timeout);
            messages.getChildren().add(message);

            primaryStage.setHeight(primaryStage.getHeight() + 22);
        });


        anchorPane.getChildren().addAll(button, messages);

        AnchorPane.setLeftAnchor(bottomView, 10.0);
        AnchorPane.setBottomAnchor(bottomView, 5.0);

        AnchorPane.setLeftAnchor(messages, 15.0);
        AnchorPane.setBottomAnchor(messages, 32.0);

        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("bilibili - danmaku");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox getMessage() {
        var singleMessage = new HBox();
        var username = new Label("殇花思密达：");
        var message = new Label("test" + i++);

        username.setFont(messageFont);
        username.setTextFill(Color.CORNFLOWERBLUE);

        message.setFont(messageFont);
        message.setTextFill(Color.WHITE);

        singleMessage.getChildren().addAll(username, message);
        return singleMessage;
    }
}

class MyScheduleServices extends ScheduledService<List<Node>> {

    private final VBox messages;
    private final Stage stage;

    public MyScheduleServices(VBox messages, Stage stage) {
        this.stage = stage;
        this.messages = messages;
    }

    @Override
    protected Task<List<Node>> createTask() {
        return new Task<>() {
            @Override
            protected List<Node> call() {
                var children = messages.getChildren();
                if (!children.isEmpty()) {
                    return children.filtered(node -> (Long) node.getUserData() < System.currentTimeMillis());
                }
                return null;
            }

            @Override
            protected void updateValue(List<Node> value) {
                if (value != null && value.size() != 0) {
                    stage.setHeight(stage.getHeight() - 22);
                    stage.setY(stage.getY() + 22);
                    messages.getChildren().removeAll(value);
                }
                super.updateValue(value);
            }
        };
    }

}