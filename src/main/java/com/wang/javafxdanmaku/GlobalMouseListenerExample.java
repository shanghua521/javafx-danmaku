package com.wang.javafxdanmaku;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;

public class GlobalMouseListenerExample implements NativeMouseInputListener {
    private final Stage stage;
    private boolean mark = true;

    public GlobalMouseListenerExample(Stage stage) {
        this.stage = stage;
    }

    public void nativeMouseClicked(NativeMouseEvent e) {
        if (mark) {
            if (e.getX() >= stage.getX() && e.getX() <= stage.getX() + stage.getWidth() && e.getY() >= stage.getY() - 156 && e.getY() <= stage.getY() + stage.getHeight() - 156) {
                try {
                    Robot robot = new Robot();
                    robot.mouseMove(e.getX(), e.getY() + 156);
                    Platform.runLater(() -> {
                        stage.setOnHidden(event -> {
                            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);

                            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                        });
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        stage.setAlwaysOnTop(true);
                        stage.show();

                    });
                } catch (AWTException ignored) {
                }
                var nativeMouseEvent = new NativeMouseEvent(e.getID(), e.getModifiers(), e.getX(), e.getY(), 1);
//            GlobalScreen.postNativeEvent(nativeMouseEvent);
                mark = false;
                return;
            }
        }

        mark = true;
//        System.out.println("x: " + e.getX() + " Y: " + e.getY());
//        System.out.println("x: " + stage.getX() + " Y: " + (stage.getY() - 156.0));
//        System.out.println("x: " + (stage.getX() + stage.getWidth()) + " Y: " + (stage.getY() + stage.getHeight() - 156.0));
    }
}