package com.wang.javafxdanmaku.pane;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.*;
import com.wang.javafxdanmaku.entity.FirstSecurityData;
import com.wang.javafxdanmaku.entity.UserInfoResult;
import com.wang.javafxdanmaku.entity.live.LiveInfoResult;
import com.wang.javafxdanmaku.entity.live.LiveRoomInfo;
import com.wang.javafxdanmaku.entity.live.LiveRoomInfoResult;
import com.wang.javafxdanmaku.utils.DanMuWebSocketUtils;
import com.wang.javafxdanmaku.utils.LiveHttpUtils;
import com.wang.javafxdanmaku.utils.UserHttpUtils;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import struct.JavaStruct;
import struct.StructException;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class HomePagePane extends AnchorPane {

    public final static String heartByte = "0000001f0010000100000002000000015b6f626a656374204f626a6563745d";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static DanMuMessageClient danMuMessageClient = null;
    private final Paint fontColor = Paint.valueOf("#515A6E");
    private final StringProperty liveTextId = new SimpleStringProperty();
    private int leftOrRight = 0;
    private Pane liveRoomInfoPane;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final InputStream inputStreamSIYUANREGULAR = getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR);

    public HomePagePane() {
        super();
        var addLiveRoomPane = addLiveRoomPane();

//        var liveRoomInfoPane = liveRoomInfoPane();

        var userInfoPane = userInfoPane();
        setLoginPane(userInfoPane);

        var modulePane = modulePane();

        AnchorPane.setTopAnchor(userInfoPane, 10.0);
        AnchorPane.setLeftAnchor(userInfoPane, 300.0);

        AnchorPane.setTopAnchor(modulePane, userInfoPane.getPrefHeight() + 22);
        AnchorPane.setLeftAnchor(modulePane, 300.0);

        AnchorPane.setTopAnchor(addLiveRoomPane, 10.0);
        AnchorPane.setLeftAnchor(addLiveRoomPane, 10.0);

//        AnchorPane.setTopAnchor(liveRoomInfoPane, 51.0);
//        AnchorPane.setLeftAnchor(liveRoomInfoPane, 10.0);

        this.getChildren().addAll(addLiveRoomPane, userInfoPane, modulePane);
    }

    private Pane getLiveRoomInfoPane(LiveRoomInfoResult liveRoomInfoResult) {
        LiveRoomInfo liveRoomInfo = liveRoomInfoResult.getRoom_info();

        var anchorPane = new AnchorPane();

        var mainLiveRoom = new AnchorPane();
        mainLiveRoom.setPrefWidth(280);
        mainLiveRoom.setPrefHeight(350);
        mainLiveRoom.setBorder(new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(5), null)));

        var inMainLiveRoom = new AnchorPane();

        var mainRoomLabel = new Label("主房间");
        mainRoomLabel.setTextFill(Paint.valueOf("#4FBDEA"));
        mainRoomLabel.setFont(Font.loadFont(inputStreamSIYUANREGULAR, 14));

        var mainRoomIdLabel = new Label("直播间ID: " + liveRoomInfo.getRoom_id() + "");
        mainRoomLabel.setTextFill(Paint.valueOf("#666E80"));
        mainRoomLabel.setFont(Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 14));

        var mainRoomHBox = new HBox(10);
        mainRoomHBox.getChildren().addAll(mainRoomLabel, mainRoomIdLabel);

        var connectLiveRoomButton = new Button("连接");
        connectLiveRoomButton.setPrefWidth(55);
        connectLiveRoomButton.setCursor(Cursor.HAND);
        connectLiveRoomButton.setTextFill(Color.WHITE);
        connectLiveRoomButton.setStyle("-fx-background-color: #23ADE5");
        connectLiveRoomButton.setFont(Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 13));
        connectLiveRoomButton.setOnMouseClicked(event -> connectLiveRoom(String.valueOf(liveRoomInfo.getRoom_id())));
        connectLiveRoomButton.setOnMouseExited(event -> connectLiveRoomButton.setStyle("-fx-background-color: #23ADE5"));
        connectLiveRoomButton.setOnMouseEntered(event -> connectLiveRoomButton.setStyle("-fx-background-color: #4FBDEA"));

        var mainRoomTitleLabel = new Label("直播间标题");
        var mainRoomTitleValue = new Label(liveRoomInfo.getTitle());

        var mainRoomAreaLabel = new Label("直播间分区");
        var mainRoomAreaValue = new Label(liveRoomInfo.getParent_area_name() + " · " + liveRoomInfo.getArea_name());

        var mainRoomCoverLabel = new Label("直播间封面");

        var cover = new ImageView(liveRoomInfo.getCover());
        cover.setFitWidth(245);
        cover.setPreserveRatio(true);

        var liveRoomAreaHBox = new HBox(25);
        liveRoomAreaHBox.getChildren().addAll(mainRoomAreaLabel, mainRoomAreaValue);

        var liveRoomInfoVBox = new VBox(5);
        liveRoomInfoVBox.getChildren().addAll(mainRoomTitleLabel, mainRoomTitleValue, liveRoomAreaHBox, mainRoomCoverLabel, cover);

        var mainRoomLabelList = List.of(mainRoomTitleLabel, mainRoomTitleValue, mainRoomAreaLabel, mainRoomAreaValue, mainRoomCoverLabel);

        var paint = Paint.valueOf("#515A6E");
        var font = Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 12);
        mainRoomLabelList.forEach(label -> {
            label.setTextFill(paint);
            label.setFont(font);
        });

        AnchorPane.setTopAnchor(connectLiveRoomButton, 20.0);
        AnchorPane.setTopAnchor(liveRoomInfoVBox, 50.0);

        inMainLiveRoom.getChildren().addAll(liveRoomInfoVBox);
        inMainLiveRoom.getChildren().addAll(mainRoomHBox, connectLiveRoomButton);

        AnchorPane.setTopAnchor(inMainLiveRoom, 15.0);
        AnchorPane.setLeftAnchor(inMainLiveRoom, 15.0);
        AnchorPane.setTopAnchor(inMainLiveRoom, 15.0);
        AnchorPane.setBottomAnchor(inMainLiveRoom, 15.0);

        mainLiveRoom.getChildren().addAll(inMainLiveRoom);

        anchorPane.getChildren().addAll(mainLiveRoom);
        return anchorPane;
    }

    private void connectLiveRoom(String liveRoomId) {
        var liveRoomInitResult = LiveHttpUtils.getLiveRoomInit(liveRoomId);
        var liveRoomNewsResult = LiveHttpUtils.getLiveRoomNews(liveRoomId);
        var liveDanMuConfResult = LiveHttpUtils.getLiveDanMuConf(liveRoomId);

        if (liveRoomInitResult.isPresent() && liveRoomNewsResult.isPresent() && liveDanMuConfResult.isPresent()) {
            var liveRoomInit = liveRoomInitResult.get().getData();
            var liveRoomNews = liveRoomNewsResult.get().getData();
            log.info("真实房间号 {} 短房间号 {} 主播名字为 {}", liveRoomInit.getRoomId(), liveRoomInit.getShortId(), liveRoomNews.getUname());

            var liveConf = liveDanMuConfResult.get().getData();
            var token = liveConf.getToken();
            var firstSecurityData = new FirstSecurityData();

            firstSecurityData.setKey(token);
            firstSecurityData.setUid(277705166L);
            firstSecurityData.setRoomid(Long.valueOf(liveRoomNews.getRoomId()));

            try {
                // 这个 URL 如果是 wss://broadcastlv.chat.bilibili.com:443/sub 就会连接成功，如果其他的就会失败，但是浏览器内就是用的其他的链接
                var firstSecurityDataJsonValue = objectMapper.writeValueAsString(firstSecurityData);
                var dataBytes = firstSecurityDataJsonValue.getBytes(StandardCharsets.UTF_8);

                // 创建 Head 头
                var barrageHeader = new BarrageHeader(dataBytes.length + 16, (char) 16, (char) 1, 7, 1);
                var headerBytes = JavaStruct.pack(barrageHeader);

//                var wsUrl = GetWsUrl(liveConf.getHost_list());
//                danMuMessageClient = new DanMuMessageClient(new URI(wsUrl));

                danMuMessageClient = new DanMuMessageClient(new URI("wss://broadcastlv.chat.bilibili.com:443/sub"));
                danMuMessageClient.connectBlocking();

                DanMuWebSocketUtils.send(danMuMessageClient, headerBytes, dataBytes);
                byte[] heartBytes = DanMuWebSocketUtils.fromHexString(heartByte);
                danMuMessageClient.send(heartBytes);

                var heartThread = new Thread(() -> {
                    while (true) {
                        if (danMuMessageClient.isOpen()) {
                            try {
                                Thread.sleep(30000);
                                danMuMessageClient.send(heartBytes);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                heartThread.start();
            } catch (JsonProcessingException | InterruptedException | StructException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private Pane addLiveRoomPane() {
        var liveId = new TextField();
        // 添加直播间输入框以及按钮
        var live = new HBox();
        var addLive = new Label("添加直播间");

        liveTextId.bind(liveId.textProperty());

        liveId.setPrefWidth(186);
        liveId.setPrefHeight(31);
        liveId.setPromptText("直播间ID");
        liveId.setFont(Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 12));
        liveId.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), new CornerRadii(5, 0, 0, 5, false), null)));

        var defaultBorder = new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(5, 0, 0, 5, false), null));
        var focusedBorder = new Border(new BorderStroke(Paint.valueOf("#4FBDEA"), BorderStrokeStyle.SOLID, new CornerRadii(5, 0, 0, 5, false), null));

        liveId.setBorder(defaultBorder);
        liveId.focusedProperty().addListener((observable, oldValue, newValue) -> liveId.setBorder(newValue ? defaultBorder : focusedBorder));
        liveId.setOnMouseEntered(event -> liveId.setBorder(focusedBorder));
        liveId.setOnMouseExited(event -> liveId.setBorder(liveId.isFocused() ? focusedBorder : defaultBorder));

        addLive.setPrefWidth(94);
        addLive.setPrefHeight(31);
        addLive.setCursor(Cursor.HAND);
        addLive.setAlignment(Pos.CENTER);
        addLive.setFont(Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 12));
        addLive.setBackground(new Background(new BackgroundFill(Paint.valueOf("#F8F8F9"), new CornerRadii(0, 5, 5, 0, false), null)));
        addLive.setBorder(new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(0, 5, 5, 0, false), new BorderWidths(1, 1, 1, 0))));

        addLive.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                new Thread(() -> {
                    var liveInfo = LiveHttpUtils.getLiveInfo(liveId.getText());
                    liveInfo.ifPresent(liveRoomInfoResult -> Platform.runLater(() -> {
                        GlobalData.liveRoomId = liveId.getText();
                        if (liveRoomInfoPane != null) {
                            this.getChildren().remove(liveRoomInfoPane);
                        }
                        liveRoomInfoPane = getLiveRoomInfoPane(liveRoomInfoResult);
                        this.getChildren().add(liveRoomInfoPane);
                        AnchorPane.setTopAnchor(liveRoomInfoPane, 51.0);
                        AnchorPane.setLeftAnchor(liveRoomInfoPane, 10.0);
                    }));
                }).start();
            }
        });

        live.getChildren().addAll(liveId, addLive);
        return live;
    }

    private AnchorPane userInfoPane() {
        var userInfoPane = new AnchorPane();
        userInfoPane.setPrefWidth(585);
        userInfoPane.setPrefHeight(128);
        userInfoPane.setStyle("-fx-padding: 9");
        userInfoPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), new CornerRadii(5, false), new Insets(10))));
        userInfoPane.setBorder(new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(5, false), null)));
        return userInfoPane;
    }

    private AnchorPane modulePane() {
        var modulePane = new AnchorPane();
        modulePane.setPrefWidth(585);
        modulePane.setPrefHeight(228);

        var appModule = new Label("应用模块");
        appModule.setPrefWidth(89);
        appModule.setPrefHeight(30);
        appModule.setCursor(Cursor.HAND);
        appModule.setAlignment(Pos.CENTER);
        appModule.setTextFill(Color.valueOf("#515A6E"));
        appModule.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourceHanSansCN-Bold-2.otf"), 14));

        var obsModule = new Label("OBS模块");
        obsModule.setPrefWidth(89);
        obsModule.setPrefHeight(30);
        obsModule.setCursor(Cursor.HAND);
        obsModule.setAlignment(Pos.CENTER);
        obsModule.setTextFill(Color.valueOf("#515A6E"));
        obsModule.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourceHanSansCN-Bold-2.otf"), 14));

        var clickColor = Paint.valueOf("#23ADE5");
        var defaultColor = Paint.valueOf("#515A6E");

        var line = new Rectangle();
        line.setWidth(89);
        line.setHeight(2);
        line.setFill(Paint.valueOf("#23ADE5"));

        // 应用模块 OBS 模块下线条切换特效
        var goLeft = new Timeline();
        goLeft.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(line.translateXProperty(), 89)),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(line.translateXProperty(), 0)));

        var goRight = new Timeline();
        goRight.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(line.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(line.translateXProperty(), 89)));

        AnchorPane.setLeftAnchor(obsModule, appModule.getPrefWidth());

        AnchorPane.setTopAnchor(line, appModule.getPrefHeight());

        modulePane.getChildren().addAll(appModule, obsModule, line);

        var appAndObsPane = new AnchorPane();

        var appPane = new GridPane();
        appPane.setVgap(7.4);
        appPane.setHgap(7.4);

        var danmakuItem = moduleItem(ImagesConstants.imageViewWhiteHomePage, "弹幕视图");
        var presentItem = moduleItem(ImagesConstants.imageViewWhiteHomePage, "礼物素材");
        var luckyDrawItem = moduleItem(ImagesConstants.imageViewWhiteHomePage, "抽奖面板");
        var toolItem = moduleItem(ImagesConstants.imageViewWhiteHomePage, "工具栏");
        var screenBounds = Screen.getPrimary().getVisualBounds();

        danmakuItem.setOnMouseClicked(event -> {
            Stage danMuStage = new Stage();
            var danMuPane = DanMuPane.getInstance();

            Platform.runLater(() -> {
                danMuStage.setX(65);
                danMuStage.setY((screenBounds.getHeight() - 350));
            });
            Scene danMuScene = new Scene(danMuPane);
            danMuScene.setFill(null);
            danMuStage.setResizable(false);
            danMuStage.setAlwaysOnTop(true);
            danMuStage.initStyle(StageStyle.TRANSPARENT);
            danMuStage.setTitle("bilibili - danmaku");
            danMuStage.setScene(danMuScene);
            danMuStage.show();
        });
        toolItem.setOnMouseClicked(event -> {
            Stage danMuStage = new Stage();
            var danMuSendPane = new DanMuSendPane();

            Platform.runLater(() -> {
                danMuStage.setX(screenBounds.getWidth() - 395);
                danMuStage.setY((screenBounds.getHeight()));
            });
            Scene danMuScene = new Scene(danMuSendPane);
            danMuScene.setFill(null);
            danMuStage.setResizable(false);
            danMuStage.setAlwaysOnTop(true);
            danMuStage.initStyle(StageStyle.TRANSPARENT);
            danMuStage.setTitle("bilibili - danmaku");
            danMuStage.setScene(danMuScene);
            danMuStage.show();
        });
        presentItem.setOnMouseClicked(event -> System.out.println(jdbcTemplate));

        appPane.addRow(0, danmakuItem);
        appPane.addRow(0, presentItem);
        appPane.addRow(0, luckyDrawItem);
        appPane.addRow(1, toolItem);

        var obsPane = new GridPane();
        obsPane.setVgap(7.4);
        obsPane.setHgap(7.4);

        appAndObsPane.getChildren().addAll(appPane, obsPane);

        var clip = new Rectangle(589.4 * 2, 589.4);
        appAndObsPane.setClip(clip);

        double sixHundred = 600;

        AnchorPane.setLeftAnchor(obsPane, sixHundred);

        var obsDanmakuItem = moduleItem(ImagesConstants.imageViewWhiteHomePage, "弹幕视图");
        var chooseSongItem = moduleItem(ImagesConstants.imageViewWhiteHomePage, "点歌列表");

        obsPane.addRow(0, obsDanmakuItem);
        obsPane.addRow(0, chooseSongItem);

        modulePane.getChildren().addAll(appAndObsPane);

        // 模块 grid 特效
        var appModuleShow = new Timeline();
        appModuleShow.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(appAndObsPane.translateXProperty(), -sixHundred), new KeyValue(clip.layoutXProperty(), sixHundred)),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(appAndObsPane.translateXProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(clip.layoutXProperty(), 0, Interpolator.EASE_BOTH)));

        var obsModuleShow = new Timeline();
        obsModuleShow.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(appAndObsPane.translateXProperty(), 0), new KeyValue(clip.layoutXProperty(), 0)),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(appAndObsPane.translateXProperty(), -sixHundred, Interpolator.EASE_BOTH), new KeyValue(clip.layoutXProperty(), sixHundred, Interpolator.EASE_BOTH)));


        // 应用模块，OBS 模块事件
        appModule.setOnMouseEntered(event -> {
            if (leftOrRight == 1) appModule.setTextFill(clickColor);
        });
        appModule.setOnMouseExited(event -> {
            if (leftOrRight == 1) appModule.setTextFill(defaultColor);
        });
        appModule.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (goLeft.getStatus() != Animation.Status.RUNNING && goRight.getStatus() != Animation.Status.RUNNING && line.getTranslateX() == 89.0) {
                    goLeft.play();
                }
                if (appModuleShow.getStatus() != Animation.Status.RUNNING && obsModuleShow.getStatus() != Animation.Status.RUNNING && appAndObsPane.getTranslateX() == -sixHundred) {
                    appModuleShow.play();
                }
                leftOrRight = 0;
                appModule.setTextFill(clickColor);
                obsModule.setTextFill(defaultColor);
            }
        });

        obsModule.setOnMouseEntered(event -> {
            if (leftOrRight == 0) obsModule.setTextFill(clickColor);
        });
        obsModule.setOnMouseExited(event -> {
            if (leftOrRight == 0) obsModule.setTextFill(defaultColor);
        });
        obsModule.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (goRight.getStatus() != Animation.Status.RUNNING && goLeft.getStatus() != Animation.Status.RUNNING && line.getTranslateX() == 0.0) {
                    goRight.play();
                }
                if (obsModuleShow.getStatus() != Animation.Status.RUNNING && appModuleShow.getStatus() != Animation.Status.RUNNING && appAndObsPane.getTranslateX() == 0.0) {
                    obsModuleShow.play();
                }
                leftOrRight = 1;
                obsModule.setTextFill(clickColor);
                appModule.setTextFill(defaultColor);
            }
        });

        AnchorPane.setTopAnchor(appAndObsPane, appModule.getPrefHeight() + 10);

        return modulePane;
    }

    private Pane moduleItem(ImageView pic, String txt) {
        var anchorPane = new AnchorPane();

        anchorPane.setPrefWidth(190);
        anchorPane.setPrefHeight(50);
        anchorPane.setCursor(Cursor.HAND);
        anchorPane.setBackground(new Background(new BackgroundFill(null, new CornerRadii(3), null)));
        anchorPane.setBorder(new Border(new BorderStroke(Paint.valueOf("DDDEEA"), BorderStrokeStyle.SOLID, new CornerRadii(3), null)));

        var imageView = new ImageView(pic.getImage());
        imageView.setFitWidth(25);
        imageView.setPreserveRatio(true);

        var image = new Label();
        image.setGraphic(imageView);
        image.setPrefWidth(50);
        image.setPrefHeight(50);
        image.setAlignment(Pos.CENTER);
        image.setBackground(new Background(new BackgroundFill(Paint.valueOf("#23ADE5"), new CornerRadii(3, 0, 0, 3, false), null)));

        var txtLabel = new Label(txt);

        txtLabel.setPrefWidth(80);
        txtLabel.setPrefHeight(50);
        txtLabel.setAlignment(Pos.CENTER);
        txtLabel.setTextFill(Paint.valueOf("#515A6E"));
        txtLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourceHanSansCN-Bold-2.otf"), 16));

        AnchorPane.setTopAnchor(image, 0.5);
        AnchorPane.setLeftAnchor(image, 0.5);
        AnchorPane.setLeftAnchor(txtLabel, 50.0);

        anchorPane.getChildren().addAll(image, txtLabel);

        return anchorPane;
    }

    private void setLoginPane(AnchorPane userInfoPane) {
        var loginButton = new Label("登录Bilibili账号");
        loginButton.setPrefWidth(114);
        loginButton.setPrefHeight(32);
        loginButton.setCursor(Cursor.HAND);
        loginButton.setTextFill(Color.WHITE);
        loginButton.setAlignment(Pos.CENTER);
        loginButton.setFont(Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 12));

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
                    var bilibiliCookie = GlobalData.bilibiliCookie;
                    // 获取用户信息
                    var token = bilibiliCookie.getName() + "=" + bilibiliCookie.getValue();
                    var userInfoResult = UserHttpUtils.getUserInfo(token);
                    var liveInfoResult = UserHttpUtils.getLiveInfo(token);

                    if (userInfoResult.isEmpty() || liveInfoResult.isEmpty()) {
                        return;
                    }
                    setLoginSuccessPane(userInfoPane, userInfoResult.get(), liveInfoResult.get());
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

    private void setLoginSuccessPane(AnchorPane userInfoPane, UserInfoResult userInfoResult, LiveInfoResult liveInfoResult) {


//        var dropShadow = new DropShadow(BlurType.GAUSSIAN, Color.valueOf("#80808077"), 1.5, 0, 0, 0);
//        userInfoPane.setOnMouseExited(event -> userInfoPane.setEffect(null));
//        userInfoPane.setOnMouseEntered(event -> userInfoPane.setEffect(dropShadow));
        var username = new Label();
        username.setTextFill(fontColor);
        username.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/SourceHanSansCN-Bold-2.otf"), 14));

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
        var userInfoFont = Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 12);

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

        var levelFont = Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 14);

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


        var toolFont = Font.loadFont(getClass().getResourceAsStream(FontsResourcesPath.SIYUANREGULAR), 12.5);
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
                        GlobalData.hostServices.showDocument(String.valueOf(label.getUserData()));
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