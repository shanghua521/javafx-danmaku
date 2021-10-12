package com.wang.javafxdanmaku.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserBarrage {
    @JsonProperty("mode")
    private Integer mode;
    @JsonProperty("color")
    private Integer color;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("room_id")
    private Integer roomId;
}
