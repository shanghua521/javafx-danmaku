package com.wang.javafxdanmaku;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.entity.LiveInfoResult;
import com.wang.javafxdanmaku.entity.UserInfoResult;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Optional;

public class UserHttpUtils {


    @SneakyThrows
    public static Optional<UserInfoResult> getUserInfo(String token) {
        var urlConnection = new URL("https://api.live.bilibili.com/xlive/web-ucenter/user/get_user_info").openConnection();
        urlConnection.setRequestProperty("Cookie", token);
        urlConnection.connect();
        var inputStream = urlConnection.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        var userInfoResult = objectMapper.readValue(inputStream, UserInfoResult.class);
        return Optional.ofNullable(userInfoResult);
    }

    @SneakyThrows
    public static Optional<LiveInfoResult> getLiveInfo(String token) {
        var urlConnection = new URL("https://api.live.bilibili.com/xlive/web-ucenter/user/live_info").openConnection();
        urlConnection.setRequestProperty("Cookie", token);
        urlConnection.connect();
        var inputStream = urlConnection.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        var liveInfoResult = objectMapper.readValue(inputStream, LiveInfoResult.class);
        return Optional.ofNullable(liveInfoResult);
    }

}
