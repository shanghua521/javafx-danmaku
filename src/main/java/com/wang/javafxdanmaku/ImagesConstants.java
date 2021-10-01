package com.wang.javafxdanmaku;

import javafx.scene.image.ImageView;

public class ImagesConstants {

    public static final ImageView imageViewLightExit = new ImageView("file:images/menuItem/light-exit.png");
    public static final ImageView imageViewDarkExit = new ImageView("file:images/menuItem/dark-exit.png");
    public static final ImageView imageViewLightMinimize = new ImageView("file:images/menuItem/light-minimize.png");
    public static final ImageView imageViewDarkMinimize = new ImageView("file:images/menuItem/dark-minimize.png");
    public static final ImageView imageViewWhiteHomePage = new ImageView("file:images/menuItem/white-home.png");
    public static final ImageView imageViewDarkHomePage = new ImageView("file:images/menuItem/dark-home.png");
    public static final ImageView imageViewWhiteHistory = new ImageView("file:images/menuItem/white-time.png");
    public static final ImageView imageViewDarkHistory = new ImageView("file:images/menuItem/dark-time.png");
    public static final ImageView imageViewWhiteStatistics = new ImageView("file:images/menuItem/white-statistics.png");
    public static final ImageView imageViewDarkStatistics = new ImageView("file:images/menuItem/dark-statistics.png");

    static {
        imageViewLightExit.setPreserveRatio(true);
        imageViewLightExit.setFitWidth(11);
        imageViewDarkExit.setPreserveRatio(true);
        imageViewDarkExit.setFitWidth(11);
        imageViewLightMinimize.setPreserveRatio(true);
        imageViewLightMinimize.setFitWidth(20);
        imageViewDarkMinimize.setPreserveRatio(true);
        imageViewDarkMinimize.setFitWidth(20);
        imageViewWhiteHomePage.setFitWidth(19);
        imageViewWhiteHomePage.setPreserveRatio(true);
        imageViewDarkHomePage.setFitWidth(19);
        imageViewDarkHomePage.setPreserveRatio(true);
        imageViewWhiteHistory.setFitWidth(26);
        imageViewWhiteHistory.setSmooth(true);
        imageViewWhiteHistory.setPreserveRatio(true);
        imageViewDarkHistory.setFitWidth(26);
        imageViewDarkHistory.setSmooth(true);
        imageViewDarkHistory.setPreserveRatio(true);
        imageViewWhiteStatistics.setFitWidth(20);
        imageViewWhiteStatistics.setPreserveRatio(true);
        imageViewDarkStatistics.setFitWidth(20);
        imageViewDarkStatistics.setPreserveRatio(true);
    }
}
