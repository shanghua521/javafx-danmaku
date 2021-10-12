package com.wang.javafxdanmaku.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class FirstSecurityData implements Serializable {
    @Serial
    private static final long serialVersionUID = -7201841645277556079L;
    //用户uid 默认无登陆状态为0 非必选
    private Long uid = 0L;
    //房间号 必选
    private Long roomid;
    //协议版本 目前为2
    private Integer protover = 2;
    //平台 可以为web
    private String platform = "web";
    //客户端版本 已知1.11.0 1.8.5 1.5.15 1.13.1 无用
    private String clientver = "1.14.3";
    //未知 可以是2
    private Integer type = 2;
    //用户标识  通过接口https://api.live.bilibili.com/room/v1/Danmu/getConf?room_id=房间号&platform=pc&player=web获取 在里面为token
    private String key;
}
