package com.wang.javafxdanmaku;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DanMaKuSpringBootApplication {

    public static void main(String[] args) {
        Application.launch(DanmakuApplication.class, args);
    }

}
