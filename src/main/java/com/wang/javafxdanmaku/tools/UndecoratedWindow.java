package com.wang.javafxdanmaku.tools;

import com.wang.javafxdanmaku.OptionAnimation;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UndecoratedWindow {

    private double mouseDragDeltaX = 0;
    private double mouseDragDeltaY = 0;
    private EventHandler<MouseEvent> mousePressedHandler;
    private EventHandler<MouseEvent> mouseDraggedHandler;
    private WeakEventHandler<MouseEvent> weakMousePressedHandler;
    private WeakEventHandler<MouseEvent> weakMouseDraggedHandler;

    /**
     * Give the Node that will be dragged (entire window or simply your Title
     * Bar), and the Stage that will be moved.
     *
     * @param node node
     * @param stage stage
     */
    public void allowDrag(Node node, Stage stage) {
        var optionAnimation = new OptionAnimation(stage);
        mousePressedHandler = (MouseEvent event) -> {
            mouseDragDeltaX = node.getTranslateX() - event.getSceneX();
            mouseDragDeltaY = node.getTranslateY() - event.getSceneY();
            optionAnimation.animation(1, 0.8).play();
            node.setCursor(Cursor.MOVE);
        };
        weakMousePressedHandler = new WeakEventHandler<>(mousePressedHandler);
        node.setOnMousePressed(weakMousePressedHandler);

        mouseDraggedHandler = (MouseEvent event) -> {
            stage.setX(event.getScreenX() + mouseDragDeltaX);
            stage.setY(event.getScreenY() + mouseDragDeltaY);
        };
        weakMouseDraggedHandler = new WeakEventHandler<>(mouseDraggedHandler);
        node.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> optionAnimation.animation(0.8, 1).play());
        node.setOnMouseDragged(weakMouseDraggedHandler);
    }
}