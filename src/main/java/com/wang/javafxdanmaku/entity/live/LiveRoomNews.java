package com.wang.javafxdanmaku.entity.live;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveRoomNews {

    @JsonProperty("roomid")
    private Integer roomId;
    @JsonProperty("uid")
    private Integer uid;
    @JsonProperty("content")
    private String content;
    @JsonProperty("ctime")
    private String ctime;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("uname")
    private String uname;
}
