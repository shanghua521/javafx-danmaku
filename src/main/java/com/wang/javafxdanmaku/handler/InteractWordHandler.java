package com.wang.javafxdanmaku.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.handler.entity.Interact;
import com.wang.javafxdanmaku.pane.DanMuPane;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
public class InteractWordHandler implements Consumer<JsonNode> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void accept(JsonNode jsonNode) {
        var data = jsonNode.get("data");
        // msg_type 1 为进入直播间 2 为关注 3为分享直播间
        try {
            var interact = objectMapper.readValue(data.traverse(), Interact.class);
            // 关注了直播间
            var msgType = interact.getMsgType();
            if (msgType == 2) {
                Platform.runLater(() -> DanMuPane.getInstance().addFollowMessage(interact.getUname()));
            } else if (msgType == 1) { // 进入直播间
                Platform.runLater(() -> DanMuPane.getInstance().addInteractMessage(interact.getUname()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
