package com.wang.javafxdanmaku;

import javafx.scene.image.ImageView;

public class ImagesConstants {

    public static final ImageView imageViewLightExit = new ImageView("/images/menuItem/light-exit.png");
    public static final ImageView imageViewDarkExit = new ImageView("/images/menuItem/dark-exit.png");
    public static final ImageView imageViewLightMinimize = new ImageView("/images/menuItem/light-minimize.png");
    public static final ImageView imageViewDarkMinimize = new ImageView("/images/menuItem/dark-minimize.png");
    public static final ImageView imageViewWhiteHomePage = new ImageView("/images/menuItem/white-home.png");
    public static final ImageView imageViewDarkHomePage = new ImageView("/images/menuItem/dark-home.png");
    public static final ImageView imageViewWhiteHistory = new ImageView("/images/menuItem/white-time.png");
    public static final ImageView imageViewDarkHistory = new ImageView("/images/menuItem/dark-time.png");
    public static final ImageView imageViewWhiteStatistics = new ImageView("/images/menuItem/white-statistics.png");
    public static final ImageView imageViewDarkStatistics = new ImageView("/images/menuItem/dark-statistics.png");

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
        imageViewWhiteHistory.setFitWidth(28);
        imageViewWhiteHistory.setPreserveRatio(true);
        imageViewDarkHistory.setFitWidth(28);
        imageViewDarkHistory.setPreserveRatio(true);
        imageViewWhiteStatistics.setFitWidth(25);
        imageViewWhiteStatistics.setPreserveRatio(true);
        imageViewDarkStatistics.setFitWidth(25);
        imageViewDarkStatistics.setPreserveRatio(true);
    }
}
