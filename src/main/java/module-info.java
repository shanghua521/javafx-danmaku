module com.wang.javafxdanmaku {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.wang.javafxdanmaku to javafx.fxml;
    exports com.wang.javafxdanmaku;
}