package com.wang.javafxdanmaku;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.List;

public class HomePagePane extends AnchorPane {

    private final Paint fontColor = Paint.valueOf("#515A6E");

    public HomePagePane() {
        super();
        // 添加直播间输入框以及按钮
        var live = new HBox();
        var liveId = new TextField();
        var addLive = new Label("添加直播间");

        liveId.setPrefWidth(186);
        liveId.setPrefHeight(31);
        liveId.setPromptText("直播间ID");
        liveId.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), new CornerRadii(5, 0, 0, 5, false), new Insets(0))));

        var defaultBorder = new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(5, 0, 0, 5, false), new BorderWidths(1)));
        var focusedBorder = new Border(new BorderStroke(Paint.valueOf("#4FBDEA"), BorderStrokeStyle.SOLID, new CornerRadii(5, 0, 0, 5, false), new BorderWidths(1)));
        liveId.setBorder(defaultBorder);
        liveId.focusedProperty().addListener((observable, oldValue, newValue) -> liveId.setBorder(newValue ? defaultBorder : focusedBorder));
        liveId.setOnMouseEntered(event -> liveId.setBorder(focusedBorder));
        liveId.setOnMouseExited(event -> liveId.setBorder(liveId.isFocused() ? focusedBorder : defaultBorder));

        addLive.setPrefWidth(94);
        addLive.setPrefHeight(31);
        addLive.setAlignment(Pos.CENTER);
        addLive.setCursor(Cursor.HAND);
        addLive.setBackground(new Background(new BackgroundFill(Paint.valueOf("#F8F8F9"), new CornerRadii(0, 5, 5, 0, false), new Insets(0))));
        addLive.setBorder(new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(0, 5, 5, 0, false), new BorderWidths(1, 1, 1, 0))));

        live.getChildren().addAll(liveId, addLive);
        // 登录框
        var userInfoPane = new AnchorPane();
        userInfoPane.setPrefWidth(585);
        userInfoPane.setPrefHeight(128);
        userInfoPane.setStyle("-fx-padding: 9");
        userInfoPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), new CornerRadii(5, 5, 5, 5, false), new Insets(10))));
        userInfoPane.setBorder(new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(5, 5, 5, 5, false), new BorderWidths(1, 1, 1, 1))));

        var picImage = new Image("https://i2.hdslb.com/bfs/face/e78923505591db435a3be102152271081d69a4e4.jpg", 70, 70, true, true);
        var picView = new ImageView(picImage);
        // 头像圆角
        Rectangle clip = new Rectangle(70, 70);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        picView.setClip(clip);

//        var dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.valueOf("#80808077"), 1.5, 0, 0, 0);
//        userInfoPane.setOnMouseExited(event -> userInfoPane.setEffect(null));
//        userInfoPane.setOnMouseEntered(event -> userInfoPane.setEffect(dropShadow));
        var username = new Label("殇花思密达");
        username.setTextFill(fontColor);
        username.setFont(Font.loadFont("file:fonts/SourceHanSansCN-Bold-2.otf", 14));

        // 用户信息
        var userinfo = new HBox(5);
        var uidLabel = new Label("UID");
        var uidVal = new Label("277705166");
        var liveIdLabel = new Label("直播间");
        var liveIdVal = new Label("8223873");
        var liveTimeForMonthLabel = new Label("当月直播时长");
        var liveTimeForMonthVal = new Label("0");
        var sanLabel = new Label("SAN值");
        var sanVal = new Label("12");
        var userInfoFont = Font.loadFont("file:fonts/SourceHanSansCN-Regular-2.otf", 12);
        userinfo.getChildren().addAll(uidLabel, uidVal, liveIdLabel, liveIdVal, liveTimeForMonthLabel, liveTimeForMonthVal, sanLabel, sanVal);
        setFonts(userinfo.getChildren(), fontColor, userInfoFont);

        var levelFont = Font.loadFont("file:fonts/SourceHanSansCN-Regular-2.otf", 14);

        var level = new HBox(20);
        level.setAlignment(Pos.CENTER);

        var uLHBox = new HBox(10);
        var uLLabel = new Label("UL.3");
        var uLProgress = new Label();
        var uLProgressValue = new Label();
        var uLPercentage = new Label("48%");

        uLHBox.setAlignment(Pos.CENTER);
        uLHBox.getChildren().addAll(uLLabel, uLProgress, uLPercentage);
        setFonts(uLHBox.getChildren(), fontColor, levelFont);

        uLProgress.setPrefWidth(130);
        uLProgress.setMaxHeight(6);
        uLProgress.setMinHeight(6);
        uLProgress.setBackground(new Background(new BackgroundFill(Color.valueOf("#F3F3F3"), new CornerRadii(50), new Insets(0))));

        uLProgressValue.setPrefWidth(63.05);
        uLProgressValue.setMaxHeight(6);
        uLProgressValue.setMinHeight(6);
        uLProgressValue.setBackground(new Background(new BackgroundFill(Color.valueOf("#4FBDEA"), new CornerRadii(50), new Insets(0))));
        uLProgressValue.setTranslateX(127.5);
        uLProgressValue.setTranslateY(57);

        var uPHBox = new HBox(10);
        var uPLabel = new Label("UP.9");
        var uPProgress = new Label();
        var uPProgressValue = new Label();
        var uPPercentage = new Label("26%");

        uPHBox.setAlignment(Pos.CENTER);
        uPHBox.getChildren().addAll(uPLabel, uPProgress, uPPercentage);
        setFonts(uPHBox.getChildren(), fontColor, levelFont);

        uPProgress.setPrefWidth(130);
        uPProgress.setMaxHeight(6);
        uPProgress.setMinHeight(6);
        uPProgress.setBackground(new Background(new BackgroundFill(Color.valueOf("#F3F3F3"), new CornerRadii(50), new Insets(0))));

        uPProgressValue.setPrefWidth(63.05);
        uPProgressValue.setMaxHeight(6);
        uPProgressValue.setMinHeight(6);
        uPProgressValue.setBackground(new Background(new BackgroundFill(Color.valueOf("#4FBDEA"), new CornerRadii(50), new Insets(0))));
        uPProgressValue.setTranslateX(357.5);
        uPProgressValue.setTranslateY(57);

        level.getChildren().addAll(uLHBox, uPHBox);

        var tools = new HBox(5);
        var liveRoom = new Label("我的直播间");
        var liveSetting = new Label("开播设置");
        var liveCentre = new Label("直播中心");
        var biliSpace = new Label("主站中心");
        var messageCentre = new Label("消息中心");

        var logout = new Label("登出");

        var toolFont = Font.loadFont("file:fonts/SourceHanSansCN-Regular-2.otf", 12.5);
        var toolList = List.of(liveRoom, liveSetting, liveCentre, biliSpace, messageCentre);
        var defaultToolBorder = new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1)));
        var blueToolBorder = new Border(new BorderStroke(Paint.valueOf("#4FBDEA"), BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1)));
        var blueTextColor = Paint.valueOf("#4FBDEA");
        toolList.forEach(label -> {
            label.setFont(toolFont);
            label.setTextFill(fontColor);
            label.setPadding(new Insets(5));
            label.setBorder(defaultToolBorder);
            label.setOnMouseEntered(event -> {
                label.setTextFill(blueTextColor);
                label.setBorder(blueToolBorder);
            });
            label.setOnMouseExited(event -> {
                label.setTextFill(fontColor);
                label.setBorder(defaultToolBorder);
            });
        });
        tools.getChildren().addAll(toolList);

        userInfoPane.getChildren().addAll(picView, username, userinfo, level, uLProgressValue, uPProgressValue, tools);

        AnchorPane.setTopAnchor(picView, 0.0);
        AnchorPane.setLeftAnchor(picView, 0.0);

        AnchorPane.setTopAnchor(username, 2.0);
        AnchorPane.setLeftAnchor(username, 77.0);

        AnchorPane.setTopAnchor(userinfo, 25.0);
        AnchorPane.setLeftAnchor(userinfo, 77.0);

        AnchorPane.setTopAnchor(level, 45.0);
        AnchorPane.setLeftAnchor(level, 77.0);

        AnchorPane.setTopAnchor(tools, 82.0);
        AnchorPane.setLeftAnchor(tools, 0.0);

        AnchorPane.setTopAnchor(live, 10.0);
        AnchorPane.setLeftAnchor(live, 10.0);

        AnchorPane.setTopAnchor(userInfoPane, 10.0);
        AnchorPane.setLeftAnchor(userInfoPane, 300.0);
        this.getChildren().addAll(live, userInfoPane);
    }

    private void setFonts(ObservableList<Node> labelList, Paint fontColor, Font font) {
        labelList.forEach(label -> {
            var _label = (Label) label;
            _label.setTextFill(fontColor);
            _label.setFont(font);
        });
    }
}