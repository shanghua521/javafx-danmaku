package com.wang.javafxdanmaku;

import javafx.application.HostServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.HttpCookie;

@Component
public class GlobalData {

    public static HttpCookie bilibiliCookie = null;
    public static HttpCookie biliJct = null;

    public static String liveRoomId;
    public static ConfigurableApplicationContext applicationContext;
    public static HostServices hostServices;
}
