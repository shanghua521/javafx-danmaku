module com.wang.javafxdanmaku {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    requires static lombok;
    requires jnativehook;
    requires java.desktop;
    requires Java.WebSocket;
    requires javastruct;
    requires hutool.core;

    requires org.slf4j;

    opens com.wang.javafxdanmaku to javafx.fxml;

    exports com.wang.javafxdanmaku;
    exports com.wang.javafxdanmaku.entity;
    exports com.wang.javafxdanmaku.utils;
    opens com.wang.javafxdanmaku.utils to javafx.fxml;
    exports com.wang.javafxdanmaku.pane;
    opens com.wang.javafxdanmaku.pane to javafx.fxml;
    exports com.wang.javafxdanmaku.entity.live;
    opens com.wang.javafxdanmaku.entity.live to com.fasterxml.jackson.databind;
    exports com.wang.javafxdanmaku.handler.entity to com.fasterxml.jackson.databind;
    opens com.wang.javafxdanmaku.entity to com.fasterxml.jackson.databind, javafx.fxml;
}