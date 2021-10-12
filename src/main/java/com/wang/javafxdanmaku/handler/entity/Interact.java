package com.wang.javafxdanmaku.handler.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Interact {

    @JsonProperty("msg_type")
    private Integer msgType;
    @JsonProperty("roomid")
    private Integer roomid;
    @JsonProperty("score")
    private Long score;
    @JsonProperty("timestamp")
    private Integer timestamp;
    @JsonProperty("uid")
    private Long uid;
    @JsonProperty("uname")
    private String uname;
    @JsonProperty("uname_color")
    private String unameColor;
}

