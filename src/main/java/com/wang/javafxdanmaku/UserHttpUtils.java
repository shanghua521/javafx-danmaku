package com.wang.javafxdanmaku;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.entity.LiveInfoResult;
import com.wang.javafxdanmaku.entity.UserInfoResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class UserHttpUtils {


    public static Optional<UserInfoResult> getUserInfo(String token) {
        var client = new OkHttpClient();
        var request = new Request.Builder().url("https://api.live.bilibili.com/xlive/web-ucenter/user/get_user_info")
                .addHeader("Cookie", token).build();
        try (var response = client.newCall(request).execute()) {
            String result = Objects.requireNonNull(response.body()).string();
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.ofNullable(objectMapper.readValue(result, UserInfoResult.class));
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<LiveInfoResult> getLiveInfo(String token) {
        var client = new OkHttpClient();
        var request = new Request.Builder().url("https://api.live.bilibili.com/xlive/web-ucenter/user/live_info")
                .addHeader("Cookie", token).build();
        try (var response = client.newCall(request).execute()) {
            String result = Objects.requireNonNull(response.body()).string();
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.ofNullable(objectMapper.readValue(result, LiveInfoResult.class));
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
