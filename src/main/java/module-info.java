module com.wang.javafxdanmaku {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jdk.jsobject;
    requires okhttp3;
    requires annotations;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    requires static lombok;

    opens com.wang.javafxdanmaku to javafx.fxml;
    opens com.wang.javafxdanmaku.entity to com.fasterxml.jackson.databind;

    exports com.wang.javafxdanmaku;
    exports com.wang.javafxdanmaku.entity;
}