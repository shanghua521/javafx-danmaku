package com.wang.javafxdanmaku.pane;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.List;

class DanMuTimeOutScheduleServices extends ScheduledService<List<Node>> {

    private final VBox messages;
    private final AnchorPane mask;

    public DanMuTimeOutScheduleServices(VBox messages, AnchorPane mask) {
        this.messages = messages;
        this.mask = mask;
    }

    @Override
    protected Task<List<Node>> createTask() {
        return new Task<>() {
            @Override
            protected List<Node> call() {
                var children = messages.getChildren();
                if (!children.isEmpty()) {
                    return children.filtered(node -> (Long) node.getUserData() < System.currentTimeMillis());
                }
                return null;
            }

            @Override
            protected void updateValue(List<Node> value) {
                super.updateValue(value);
                if (value != null && value.size() != 0) {
                    mask.setPrefHeight(mask.getPrefHeight() + value.size() * 22);
                    messages.getChildren().removeAll(value);
                }
            }
        };
    }

}
