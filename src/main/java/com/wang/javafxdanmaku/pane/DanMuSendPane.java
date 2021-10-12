package com.wang.javafxdanmaku.pane;

import com.wang.javafxdanmaku.FontsResourcesPath;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class DanMuSendPane extends AnchorPane {

    private final Font messageFont = Font.loadFont(FontsResourcesPath.SIYUANREGULAR, 15);

    public DanMuSendPane() {
        super();
        var textField = new TextField();
        textField.setPrefWidth(320);
        textField.setPrefHeight(50);
        textField.setPromptText("弹幕内容");
        textField.setStyle("-fx-background-color: rgba(0,0,0,0);-fx-text-fill: white");

        this.setStyle("-fx-background-color: rgba(0,0,0,0.80);");
        this.getChildren().addAll(textField);

        textField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                var message = getMessage(textField.getText());
                var timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
                message.setUserData(timeout);

                var messages = DanMuPane.getInstance().messages;
                var mask = DanMuPane.getInstance().mask;

                var children = messages.getChildren();
                if (children.size() >= 17) {
                    children.remove(children.get(0));
                } else {
                    mask.setPrefHeight(mask.getPrefHeight() - 22);
                }
                children.add(message);
            }
        });
    }

    private HBox getMessage(String txtMessage) {
        var singleMessage = new HBox();
        var username = new Label("殇花思密达：");
        var messageLabel = new Label(txtMessage);

        username.setFont(messageFont);
        username.setTextFill(Color.CORNFLOWERBLUE);

        messageLabel.setFont(messageFont);
        messageLabel.setTextFill(Color.WHITE);

        singleMessage.getChildren().addAll(username, messageLabel);
        return singleMessage;
    }
}
