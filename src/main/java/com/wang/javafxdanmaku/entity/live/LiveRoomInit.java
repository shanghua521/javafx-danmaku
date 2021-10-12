package com.wang.javafxdanmaku.entity.live;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LiveRoomInit {

    @JsonProperty("room_id")
    private Integer roomId;
    @JsonProperty("short_id")
    private Integer shortId;
    @JsonProperty("uid")
    private Integer uid;
    @JsonProperty("need_p2p")
    private Integer needP2p;
    @JsonProperty("is_hidden")
    private Boolean isHidden;
    @JsonProperty("is_locked")
    private Boolean isLocked;
    @JsonProperty("is_portrait")
    private Boolean isPortrait;
    @JsonProperty("live_status")
    private Integer liveStatus;
    @JsonProperty("hidden_till")
    private Integer hiddenTill;
    @JsonProperty("lock_till")
    private Integer lockTill;
    @JsonProperty("encrypted")
    private Boolean encrypted;
    @JsonProperty("pwd_verified")
    private Boolean pwdVerified;
    @JsonProperty("live_time")
    private Long liveTime;
    @JsonProperty("room_shield")
    private Integer roomShield;
    @JsonProperty("is_sp")
    private Integer isSp;
    @JsonProperty("special_type")
    private Integer specialType;
}
