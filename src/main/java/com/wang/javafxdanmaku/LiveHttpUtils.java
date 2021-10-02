package com.wang.javafxdanmaku;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.entity.LiveRoomInfoResult;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;

public class LiveHttpUtils {

    @SneakyThrows
    public static Optional<LiveRoomInfoResult> getLiveInfo(String roomId) {
        var urlConnection = new URL("https://live.bilibili.com/" + roomId + "").openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:92.0) Gecko/20100101 Firefox/92.0");
        urlConnection.connect();

        var inputStream = urlConnection.getInputStream();
        String line;

        var stringBuilder = new StringBuilder();
        var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        String result = stringBuilder.toString();

        result = "{" + result.substring(result.indexOf("\"room_info"), result.indexOf(",\"anchor_info")) + "}";

        var objectMapper = new ObjectMapper();
        var liveInfoResult = objectMapper.readValue(result, LiveRoomInfoResult.class);

        return Optional.ofNullable(liveInfoResult);
    }

}
