package com.wang.javafxdanmaku.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBarrageMsg {
    @JsonProperty("uname_color")
    private String unameColor;
    @JsonProperty("bubble")
    private Integer bubble;
    @JsonProperty("danmu")
    private UserBarrage userBarrage;
}
