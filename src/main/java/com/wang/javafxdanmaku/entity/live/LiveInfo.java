package com.wang.javafxdanmaku.entity.live;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wang.javafxdanmaku.entity.Master;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveInfo {

    private int room_id;
    private int live_time;
    private double bili_coins;
    private int san;
    private Master master;
}

