package com.wang.javafxdanmaku.pane;

import com.wang.javafxdanmaku.FontsResourcesPath;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class DanMuPane extends AnchorPane {

    private static DanMuPane danMuPane = null;
    private final Font messageFont = Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 15);
    public VBox messages;
    public AnchorPane mask;
    public Label viewNumValue;

    private DanMuPane() {
        mask = new AnchorPane();
        mask.setPrefWidth(320);
        mask.setPrefHeight(399);
        mask.setStyle("-fx-background-color: rgba(0,0,0,0);");

        var anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(320);
        anchorPane.setStyle("-fx-background-color: rgba(0,0,0,0.8);");

        this.getChildren().addAll(mask, anchorPane);
        this.setStyle("-fx-background-color: rgba(0,0,0,0);");

        var font = Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 14);

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
        viewNumValue = new Label("1");
        var viewNumLabel = new Label("房间人气");

        viewNumValue.setFont(font);
        viewNumValue.setTextFill(Color.WHITE);
        viewNumLabel.setFont(font);
        viewNumLabel.setTextFill(Color.WHITE);
        viewNumHBox.getChildren().addAll(viewNumLabel, viewNumValue);

        bottomView.getChildren().addAll(viewNumHBox, followHBox);

        messages = new VBox(7);

        anchorPane.getChildren().addAll(bottomView, messages);

        var myScheduleServices = new DanMuTimeOutScheduleServices(messages, mask);
        myScheduleServices.setPeriod(Duration.millis(100));

        Platform.runLater(myScheduleServices::start);

        AnchorPane.setLeftAnchor(bottomView, 10.0);
        AnchorPane.setBottomAnchor(bottomView, 8.0);

        AnchorPane.setLeftAnchor(messages, 15.0);
        AnchorPane.setBottomAnchor(messages, 32.0);

        AnchorPane.setBottomAnchor(anchorPane, 0.0);
    }

    public static DanMuPane getInstance() {
        if (danMuPane == null) {
            danMuPane = new DanMuPane();
        }
        return danMuPane;
    }

    public void addFollowMessage(String username) {
        var messageHBox = initMessage("关注了直播间", username + " ");
        var timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
        messageHBox.setUserData(timeout);

        addMessagePan(messageHBox);
    }

    public void addInteractMessage(String username) {
        var messageHBox = initMessage("进入直播间", username + " ");
        var timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
        messageHBox.setUserData(timeout);

        addMessagePan(messageHBox);
    }

    public void addMessage(String message, String username) {
        var messageHBox = initMessage(message, username + "：");
        var timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
        messageHBox.setUserData(timeout);

        addMessagePan(messageHBox);
    }

    public void addFlowMessage(String message, String username) {
        var singleMessage = new HBox();
        var usernameLabel = new Label(username);
        var messageLabel = new Label(message);
        var timeout = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
        singleMessage.setUserData(timeout);
        usernameLabel.setFont(messageFont);
        usernameLabel.setTextFill(Color.CORNFLOWERBLUE);

        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(182.0);
        messageLabel.setFont(messageFont);
        messageLabel.setTextFill(Color.WHITE);

        singleMessage.getChildren().addAll(usernameLabel, messageLabel);
        addMessagePan(singleMessage);
    }

    private void addMessagePan(Pane messagePane) {
        var children = messages.getChildren();
        if (children.size() >= 17) {
            children.remove(children.get(0));
        } else {
            mask.setPrefHeight(mask.getPrefHeight() - 22);
        }
        children.add(messagePane);
    }

    private HBox initMessage(String txtMessage, String userTxt) {
        var singleMessage = new HBox();
        var username = new Label(userTxt);
        var messageLabel = new Label(txtMessage);

        username.setFont(messageFont);
        username.setTextFill(Color.CORNFLOWERBLUE);

        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(182.0);
        messageLabel.setFont(messageFont);
        messageLabel.setTextFill(Color.WHITE);

        singleMessage.getChildren().addAll(username, messageLabel);
        return singleMessage;
    }

}