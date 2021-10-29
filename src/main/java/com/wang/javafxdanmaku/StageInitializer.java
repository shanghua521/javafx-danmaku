package com.wang.javafxdanmaku;

import com.wang.javafxdanmaku.DanmakuApplication.StageReadyEvent;
import javafx.stage.StageStyle;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        var stage = event.getStage();
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("bilibili - danmaku");
        stage.show();
    }
}
