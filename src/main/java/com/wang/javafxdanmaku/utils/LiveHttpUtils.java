package com.wang.javafxdanmaku.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.entity.live.LiveConfResult;
import com.wang.javafxdanmaku.entity.live.LiveRoomInfoResult;
import com.wang.javafxdanmaku.entity.live.LiveRoomInitResult;
import com.wang.javafxdanmaku.entity.live.LiveRoomNewsResult;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class LiveHttpUtils {

    private static final String userAgentKey = "User-Agent";
    private static final String userAgentValue = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:92.0) Gecko/20100101 Firefox/92.0";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @SneakyThrows
    public static Optional<LiveRoomInfoResult> getLiveInfo(String roomId) {
        var urlConnection = new URL("https://live.bilibili.com/" + roomId + "").openConnection();
        String result = doConnect(urlConnection);

        result = "{" + result.substring(result.indexOf("\"room_info"), result.indexOf(",\"anchor_info")) + "}";

        var liveInfoResult = objectMapper.readValue(result, LiveRoomInfoResult.class);

        return Optional.ofNullable(liveInfoResult);
    }

    @SneakyThrows
    public static Optional<LiveConfResult> getLiveDanMuConf(String roomId) {
        var urlConnection = new URL("https://api.live.bilibili.com/xlive/web-room/v1/index/getDanmuInfo?id=" + roomId + "&type=0").openConnection();
        String result = doConnect(urlConnection);

        var liveConfResult = objectMapper.readValue(result, LiveConfResult.class);

        return Optional.ofNullable(liveConfResult);
    }

    @SneakyThrows
    public static Optional<LiveRoomInitResult> getLiveRoomInit(String roomId) {
        var urlConnection = new URL("https://api.live.bilibili.com/room/v1/Room/room_init?id=" + roomId).openConnection();
        String result = doConnect(urlConnection);

        var liveRoomInitResult = objectMapper.readValue(result, LiveRoomInitResult.class);

        return Optional.ofNullable(liveRoomInitResult);
    }

    @SneakyThrows
    public static Optional<LiveRoomNewsResult> getLiveRoomNews(String roomId) {
        var urlConnection = new URL("https://api.live.bilibili.com/room_ex/v1/RoomNews/get?roomid=" + roomId).openConnection();
        String result = doConnect(urlConnection);

        var liveRoomNewsResult = objectMapper.readValue(result, LiveRoomNewsResult.class);

        return Optional.ofNullable(liveRoomNewsResult);
    }

    private static String doConnect(URLConnection urlConnection) throws IOException {
        urlConnection.setRequestProperty(userAgentKey, userAgentValue);
        urlConnection.connect();

        var inputStream = urlConnection.getInputStream();
        String line;

        var stringBuilder = new StringBuilder();
        var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

}
