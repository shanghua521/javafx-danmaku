package com.wang.javafxdanmaku.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.pane.DanMuPane;
import javafx.application.Platform;
import lombok.SneakyThrows;

import java.util.function.Consumer;

public class DanMuMsgHandler implements Consumer<JsonNode> {

    @SneakyThrows
    @Override
    public void accept(JsonNode jsonNode) {
        var info = jsonNode.get("info");

        var context = info.get(1);
        var username = info.get(2).get(1);

        Platform.runLater(() -> DanMuPane.getInstance().addMessage(context.asText(), username.asText()));
    }
}
