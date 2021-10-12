package com.wang.javafxdanmaku.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.GlobalData;
import com.wang.javafxdanmaku.entity.UserBarrageMsg;
import com.wang.javafxdanmaku.entity.live.LiveConfResult;
import com.wang.javafxdanmaku.entity.live.LiveRoomInfoResult;
import com.wang.javafxdanmaku.entity.live.LiveRoomInitResult;
import com.wang.javafxdanmaku.entity.live.LiveRoomNewsResult;
import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
        InputStream result = doConnectInputStream(urlConnection);

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
        InputStream result = doConnectInputStream(urlConnection);

        var liveRoomNewsResult = objectMapper.readValue(result, LiveRoomNewsResult.class);

        return Optional.ofNullable(liveRoomNewsResult);
    }

    @SneakyThrows
    public static Optional<UserBarrageMsg> getUserBarrageMessage(String roomId) {
        var urlConnection = new URL("https://api.live.bilibili.com/xlive/web-room/v1/index/getInfoByUser?room_id=" + roomId).openConnection();
        urlConnection.setRequestProperty("referer", "https://live.bilibili.com/" + roomId);
        urlConnection.setRequestProperty("cookie", GlobalData.bilibiliCookie.getName() + "-" + GlobalData.bilibiliCookie.getValue());
        InputStream result = doConnectInputStream(urlConnection);

        var jsonNode = objectMapper.readTree(result);
        var data = jsonNode.get("data");
        var property = data.get("property");
        var userBarrageMsg = objectMapper.readValue(property.traverse(), UserBarrageMsg.class);
        return Optional.ofNullable(userBarrageMsg);
    }

    private static String doConnect(URLConnection urlConnection) throws IOException {
        urlConnection.setRequestProperty(userAgentKey, userAgentValue);
        urlConnection.connect();

        var inputStream = urlConnection.getInputStream();
        return inputStreamToString(inputStream);
    }

    @SneakyThrows
    private static String inputStreamToString(InputStream inputStream) {
        String line;

        var stringBuilder = new StringBuilder();
        var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    @SneakyThrows
    public static void sendBarrage(String msg, UserBarrageMsg userBarrageMsg) {
        var roomId = userBarrageMsg.getUserBarrage().getRoomId();
        var urlConnection = (HttpURLConnection) new URL("https://api.live.bilibili.com/msg/send").openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("cookie", GlobalData.bilibiliCookie.toString());

        Map<String, String> params = new HashMap<>();
        params.put("color", userBarrageMsg.getUserBarrage().getColor().toString());
        params.put("fontsize", "25");
        params.put("mode", userBarrageMsg.getUserBarrage().getMode().toString());
        params.put("msg", msg);
        params.put("rnd", String.valueOf(System.currentTimeMillis()).substring(0, 10));
        params.put("roomid", roomId.toString());
        params.put("bubble", userBarrageMsg.getBubble().toString());
        var biliJctValue = GlobalData.biliJct.getValue();
        params.put("csrf_token", biliJctValue);
        params.put("csrf", biliJctValue);
        StringBuilder queryStr = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            queryStr.append(entry.getKey()).append('=').append(entry.getValue());
            if (iterator.hasNext()) {
                queryStr.append('&');
            }
        }

        PrintStream ps = new PrintStream(urlConnection.getOutputStream());
        ps.print(queryStr);
        ps.close();

        var inputStream = urlConnection.getInputStream();
        System.out.println(inputStreamToString(inputStream));
    }

    private static InputStream doConnectInputStream(URLConnection urlConnection) throws IOException {
        urlConnection.setRequestProperty(userAgentKey, userAgentValue);
        urlConnection.connect();

        return urlConnection.getInputStream();
    }


}
