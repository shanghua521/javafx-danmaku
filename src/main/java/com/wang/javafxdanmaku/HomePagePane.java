package com.wang.javafxdanmaku;

import com.wang.javafxdanmaku.entity.LiveInfoResult;
import com.wang.javafxdanmaku.entity.UserInfoResult;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class HomePagePane extends AnchorPane {

    private final Paint fontColor = Paint.valueOf("#515A6E");

    public HomePagePane(HostServices hostServices) {
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

        var userInfoPane = userInfoPane();
        setLoginPane(userInfoPane, hostServices);

        AnchorPane.setTopAnchor(userInfoPane, 10.0);
        AnchorPane.setLeftAnchor(userInfoPane, 300.0);

        AnchorPane.setTopAnchor(live, 10.0);
        AnchorPane.setLeftAnchor(live, 10.0);

        this.getChildren().addAll(live, userInfoPane);
    }


    private AnchorPane userInfoPane() {
        var userInfoPane = new AnchorPane();
        userInfoPane.setPrefWidth(585);
        userInfoPane.setPrefHeight(128);
        userInfoPane.setStyle("-fx-padding: 9");
        userInfoPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), new CornerRadii(5, false), new Insets(10))));
        userInfoPane.setBorder(new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(5, false), new BorderWidths(1))));
        return userInfoPane;
    }

    private void setLoginPane(AnchorPane userInfoPane, HostServices hostServices) {
        var loginButton = new Label("登录Bilibili账号");
        loginButton.setPrefWidth(114);
        loginButton.setPrefHeight(32);
        loginButton.setCursor(Cursor.HAND);
        loginButton.setTextFill(Color.WHITE);
        loginButton.setAlignment(Pos.CENTER);
        loginButton.setFont(Font.loadFont("file:fonts/SourceHanSansCN-Regular-2.otf", 12));

        var defaultBackground = new Background(new BackgroundFill(Color.valueOf("#27AEE5"), new CornerRadii(5), null));
        var mouseEnterBackground = new Background(new BackgroundFill(Color.valueOf("#4FBDEA"), new CornerRadii(5), null));
        loginButton.setBackground(defaultBackground);
        loginButton.setOnMouseExited(event -> loginButton.setBackground(defaultBackground));
        loginButton.setOnMouseEntered(event -> loginButton.setBackground(mouseEnterBackground));

        loginButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                var bilibiliLoginStage = new Stage();
                var bilibiliLoginPane = new BilibiliLoginPane();

                // 登录成功会调用这个方法
                bilibiliLoginPane.loginSucceedProperty.addListener((observable, oldValue, newValue) -> {
                    var bilibiliCookie = bilibiliLoginPane.myCookieStore.bilibiliCookie;
                    // 获取用户信息
                    var token = bilibiliCookie.getName() + "=" + bilibiliCookie.getValue();
                    var userInfoResult = UserHttpUtils.getUserInfo(token);
                    var liveInfoResult = UserHttpUtils.getLiveInfo(token);

                    if (userInfoResult.isEmpty() || liveInfoResult.isEmpty()) {
                        return;
                    }
                    setLoginSuccessPane(userInfoPane, userInfoResult.get(), liveInfoResult.get(), hostServices);
                    bilibiliLoginStage.close();
                });

                bilibiliLoginStage.initModality(Modality.APPLICATION_MODAL);

                Scene scene = new Scene(bilibiliLoginPane, 800, 380);
                bilibiliLoginStage.setResizable(false);
                bilibiliLoginStage.centerOnScreen();
                bilibiliLoginStage.setTitle("bilibili - danmaku");
                bilibiliLoginStage.setScene(scene);
                bilibiliLoginStage.show();
            }
        });

        AnchorPane.setTopAnchor(loginButton, 0.0);
        AnchorPane.setLeftAnchor(loginButton, 0.0);

        userInfoPane.getChildren().setAll(loginButton);
    }

    private void setLoginSuccessPane(AnchorPane userInfoPane, UserInfoResult userInfoResult, LiveInfoResult liveInfoResult, HostServices hostServices) {


//        var dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.valueOf("#80808077"), 1.5, 0, 0, 0);
//        userInfoPane.setOnMouseExited(event -> userInfoPane.setEffect(null));
//        userInfoPane.setOnMouseEntered(event -> userInfoPane.setEffect(dropShadow));
        var username = new Label();
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

        var userData = userInfoResult.getData();

        username.setText(userData.getUname());
        uidVal.setText(String.valueOf(userData.getUid()));
        liveIdVal.setText(String.valueOf(liveInfoResult.getData().getRoom_id()));
        liveTimeForMonthVal.setText(String.valueOf(liveInfoResult.getData().getLive_time()));

        var picImage = new Image(userData.getFace(), 70, 70, true, true);
        var picView = new ImageView(picImage);
        // 头像圆角
        Rectangle clip = new Rectangle(70, 70);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        picView.setClip(clip);

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

        var uLRatio = (double) userData.getUser_intimacy() / (double) userData.getUser_next_intimacy();
        uLLabel.setText("UL." + userData.getUser_level());
        uLPercentage.setText(Math.round(uLRatio * 100) + "%");

        uLHBox.setAlignment(Pos.CENTER);
        uLHBox.getChildren().addAll(uLLabel, uLProgress, uLPercentage);
        setFonts(uLHBox.getChildren(), fontColor, levelFont);

        uLProgress.setPrefWidth(130);
        uLProgress.setMaxHeight(6);
        uLProgress.setMinHeight(6);
        uLProgress.setBackground(new Background(new BackgroundFill(Color.valueOf("#F3F3F3"), new CornerRadii(50), null)));

        uLProgressValue.setPrefWidth(63.05);
        uLProgressValue.setMaxHeight(6);
        uLProgressValue.setMinHeight(6);
        uLProgressValue.setBackground(new Background(new BackgroundFill(Color.valueOf("#4FBDEA"), new CornerRadii(50), null)));
        uLProgressValue.setTranslateX(127.5);
        uLProgressValue.setTranslateY(60);

        uLProgressValue.setPrefWidth(uLRatio * 130);

        var uPHBox = new HBox(10);
        var uPLabel = new Label("UP.9");
        var uPProgress = new Label();
        var uPProgressValue = new Label();
        var uPPercentage = new Label("26%");

        var master = liveInfoResult.getData().getMaster();
        var upRatio = (double) master.getCurrent() / (double) master.getNext();

        uPLabel.setText("UP." + master.getLevel());
        uPPercentage.setText(Math.round(upRatio * 100) + "%");

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
        uPProgressValue.setTranslateY(60);

        uPProgressValue.setPrefWidth(upRatio * 130.0);

        level.getChildren().addAll(uLHBox, uPHBox);

        var tools = new HBox(5);
        var liveRoom = new Label("我的直播间");
        var liveSetting = new Label("开播设置");
        var liveCentre = new Label("直播中心");
        var biliSpace = new Label("主站中心");
        var messageCentre = new Label("消息中心");

        liveRoom.setUserData("https://live.bilibili.com/" + liveInfoResult.getData().getRoom_id());
        liveSetting.setUserData("https://link.bilibili.com/p/center/index#/my-room/start-live");
        liveCentre.setUserData("https://link.bilibili.com/p/center/index");
        biliSpace.setUserData("https://space.bilibili.com/" + userInfoResult.getData().getUid());
        messageCentre.setUserData("https://message.bilibili.com/#/reply");

        var logout = new Label("登出");


        var toolFont = Font.loadFont("file:fonts/SourceHanSansCN-Regular-2.otf", 12.5);
        var toolList = List.of(liveRoom, liveSetting, liveCentre, biliSpace, messageCentre, logout);
        var defaultToolBorder = new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1)));
        var blueToolBorder = new Border(new BorderStroke(Paint.valueOf("#4FBDEA"), BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1)));
        var blueTextColor = Paint.valueOf("#4FBDEA");
        toolList.forEach(label -> {
            label.setFont(toolFont);
            label.setTextFill(fontColor);
            label.setCursor(Cursor.HAND);
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
            label.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (label.getUserData() != null) {
                        hostServices.showDocument(String.valueOf(label.getUserData()));
                    }
                }
            });
        });
        tools.getChildren().addAll(toolList);

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

        AnchorPane.setRightAnchor(logout, 0.0);
        AnchorPane.setBottomAnchor(logout, 0.0);

        userInfoPane.getChildren().setAll(picView, username, userinfo, level, uLProgressValue, uPProgressValue, tools, logout);
    }

    private void setFonts(ObservableList<Node> labelList, Paint fontColor, Font font) {
        labelList.forEach(label -> {
            var _label = (Label) label;
            _label.setTextFill(fontColor);
            _label.setFont(font);
        });
    }
}