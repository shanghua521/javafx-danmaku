package com.wang.javafxdanmaku;

import com.wang.javafxdanmaku.pane.HomePagePane;
import com.wang.javafxdanmaku.tools.UndecoratedWindow;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

import static com.wang.javafxdanmaku.ImagesConstants.*;

public class DanmakuApplication extends Application {

    private static final SimpleIntegerProperty menuIndex = new SimpleIntegerProperty();
    private static final SimpleStringProperty title = new SimpleStringProperty("首页");
    private static final List<String> titles = List.of("首页", "历史弹幕", "弹幕统计");

    static {
        menuIndex.addListener((observable, oldValue, newValue) -> title.set(titles.get(newValue.intValue())));
    }

    private final UndecoratedWindow undecoratedWindowForLeftView = new UndecoratedWindow();
    private final UndecoratedWindow undecoratedWindowForTopView = new UndecoratedWindow();
    private final List<Label> menuButtons = new ArrayList<>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        var anchorPane = new AnchorPane();
        var leftView = getLeftView(anchorPane, primaryStage);
        var topView = getTopView(primaryStage);

        anchorPane.setStyle("-fx-background-color: #FFFFFF");

        var hostServices = this.getHostServices();

        var homePagePane = new HomePagePane(hostServices);
        homePagePane.setStyle("-fx-background-color: white");
        homePagePane.prefHeightProperty().bind(anchorPane.heightProperty().subtract(topView.prefHeightProperty()));
        homePagePane.prefWidthProperty().bind(anchorPane.widthProperty().subtract(leftView.prefWidthProperty()));
        topView.prefWidthProperty().bind(anchorPane.widthProperty().subtract(leftView.prefWidthProperty()));
        anchorPane.getChildren().addAll(leftView, topView, homePagePane);

        AnchorPane.setLeftAnchor(topView, leftView.getPrefWidth());
        AnchorPane.setLeftAnchor(homePagePane, leftView.getPrefWidth());
        AnchorPane.setTopAnchor(homePagePane, topView.getPrefHeight() + 1);
        Scene scene = new Scene(anchorPane, 960, 600);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("bilibili - danmaku");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public VBox getLeftView(AnchorPane anchorPane, Stage stage) {
        var leftView = new VBox();
        leftView.setPrefWidth(64);
        leftView.prefHeightProperty().bind(anchorPane.heightProperty());
        leftView.setStyle("-fx-background-color: #323232");
        leftView.setOnMouseReleased(event -> leftView.setCursor(Cursor.DEFAULT));

        var exitAndMinimizeView = new HBox(10);
        exitAndMinimizeView.setPrefHeight(48);
        exitAndMinimizeView.setAlignment(Pos.CENTER);
        exitAndMinimizeView.setPrefWidth(leftView.getPrefWidth());
        exitAndMinimizeView.addEventHandler(MouseEvent.MOUSE_PRESSED, Event::consume);
        exitAndMinimizeView.addEventHandler(MouseEvent.MOUSE_RELEASED, Event::consume);
        exitAndMinimizeView.addEventHandler(MouseEvent.MOUSE_DRAGGED, Event::consume);
        exitAndMinimizeView.setPadding(new Insets(0, 0, 4.5, 0));

        var exitButton = new Label();
        exitButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                stage.close();
            }
        });
        labelExitMinimizeInit(exitButton, imageViewLightExit, imageViewDarkExit);

        var minimizeButton = new Label();
        minimizeButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                stage.setIconified(true);
            }
        });
        labelExitMinimizeInit(minimizeButton, imageViewLightMinimize, imageViewDarkMinimize);

        exitAndMinimizeView.getChildren().addAll(exitButton, minimizeButton);

        var homePage = new Label();
        menuButtonInit(homePage, leftView, imageViewWhiteHomePage, imageViewDarkHomePage);
        homePage.setGraphic(imageViewWhiteHomePage);
        homePage.setStyle("-fx-background-color: #23ADE5");

        var historyPage = new Label();
        menuButtonInit(historyPage, leftView, imageViewWhiteHistory, imageViewDarkHistory);

        var statisticsPage = new Label();
        menuButtonInit(statisticsPage, leftView, imageViewWhiteStatistics, imageViewDarkStatistics);

        menuButtons.addAll(List.of(homePage, historyPage, statisticsPage));

        leftView.getChildren().add(exitAndMinimizeView);
        leftView.getChildren().addAll(menuButtons);

        undecoratedWindowForLeftView.allowDrag(leftView, stage);
        return leftView;
    }

    private void menuButtonInit(Label label, Pane parent, ImageView whiteImage, ImageView darkImage) {
        label.setPrefHeight(54);
        label.setUserData(darkImage);
        label.setCursor(Cursor.HAND);
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(parent.getPrefWidth());
        label.setStyle("-fx-background-color: #323232");
        label.setGraphic(darkImage);
        label.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                label.setGraphic(whiteImage);
                label.setStyle("-fx-background-color: #23ADE5");
                menuIndex.set(menuButtons.indexOf(label));
                menuButtons.stream().filter(_label -> _label != label).forEach(this::menuButtonReset);
            }
        });
        label.addEventHandler(MouseEvent.MOUSE_PRESSED, Event::consume);
        label.addEventHandler(MouseEvent.MOUSE_RELEASED, Event::consume);
        label.addEventHandler(MouseEvent.MOUSE_DRAGGED, Event::consume);
        label.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> labelEnterAndExitStyle(label, "#525252", whiteImage));
        label.addEventHandler(MouseEvent.MOUSE_EXITED, event -> labelEnterAndExitStyle(label, "#323232", darkImage));
    }

    private void labelEnterAndExitStyle(Label label, String color, ImageView imageView) {
        if (menuIndex.get() != menuButtons.indexOf(label)) {
            label.setStyle("-fx-background-color: " + color + "");
            label.setGraphic(imageView);
        }
    }

    private void menuButtonReset(Label label) {
        var darkImage = label.getUserData();
        label.setStyle("-fx-background-color: #323232");
        label.setGraphic((Node) darkImage);
    }

    private FlowPane getTopView(Stage stage) {
        var topView = new FlowPane();
        topView.setPrefHeight(47);
        topView.setStyle("-fx-background-color: #FFFFFF");
        topView.setPadding(new Insets(14, 0, 14, 14));
        topView.setOnMouseReleased(event -> topView.setCursor(Cursor.DEFAULT));
        topView.setBorder(new Border(new BorderStroke(Paint.valueOf("#DDDEEA"), BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 1, 0))));

        var title = new Label("首页");
        title.setTextFill(Color.valueOf("#515A6E"));
        title.setFont(Font.loadFont(FontsResourcesPath.SIYUANBOLD, 18));
        title.textProperty().bind(DanmakuApplication.title);
        topView.getChildren().addAll(title);

        undecoratedWindowForTopView.allowDrag(topView, stage);
        return topView;
    }

    private void labelExitMinimizeInit(Label label, ImageView lightImage, ImageView darkImage) {
        label.setPrefWidth(10);
        label.setGraphic(darkImage);
        label.addEventHandler(MouseEvent.MOUSE_PRESSED, Event::consume);
        label.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> label.setGraphic(lightImage));
        label.addEventHandler(MouseEvent.MOUSE_EXITED, event -> label.setGraphic(darkImage));
    }
}