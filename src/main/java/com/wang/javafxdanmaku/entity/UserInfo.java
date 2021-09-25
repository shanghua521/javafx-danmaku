package com.wang.javafxdanmaku.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public
class UserInfo {
    private int uid;
    private String uname;
    private String face;
    private int billCoin;
    private int silver;
    private int gold;
    private int achieve;
    private int vip;
    private int svip;
    private int user_level;
    private int user_next_level;
    private int user_intimacy;
    private int user_next_intimacy;
    private int is_level_top;
    private String user_level_rank;
    private int user_charged;
    private int identification;
}
