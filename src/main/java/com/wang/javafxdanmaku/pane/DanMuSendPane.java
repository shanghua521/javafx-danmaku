package com.wang.javafxdanmaku.pane;

import com.wang.javafxdanmaku.FontsResourcesPath;
import com.wang.javafxdanmaku.GlobalData;
import com.wang.javafxdanmaku.entity.UserBarrageMsg;
import com.wang.javafxdanmaku.utils.LiveHttpUtils;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class DanMuSendPane extends AnchorPane {

    private UserBarrageMsg userBarrageMsg = null;

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
                if (GlobalData.liveRoomId != null && GlobalData.bilibiliCookie != null) {
                    if (userBarrageMsg == null) {
                        var userBarrageMessageOptional = LiveHttpUtils.getUserBarrageMessage(GlobalData.liveRoomId);
                        userBarrageMessageOptional.ifPresent(barrageMsg -> userBarrageMsg = barrageMsg);
                    }
                    LiveHttpUtils.sendBarrage(textField.getText(), userBarrageMsg);
                    textField.clear();
                } else {
                    System.out.println("请先登录");
                }
            }
        });
    }
}
