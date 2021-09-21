package com.wang.javafxdanmaku;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

public record OptionAnimation(Stage stage) {

    public Timeline animation(double from, double to) {
        var timeline = new Timeline();
        var keyValue1 = new KeyValue(stage.opacityProperty(), from, Interpolator.LINEAR);
        var keyFrame1 = new KeyFrame(Duration.seconds(0), keyValue1);
        var keyValue2 = new KeyValue(stage.opacityProperty(), to, Interpolator.LINEAR);
        var keyFrame2 = new KeyFrame(Duration.seconds(0.2), keyValue2);
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        return timeline;
    }
}
