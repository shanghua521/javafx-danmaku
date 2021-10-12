package com.wang.javafxdanmaku.entity.live;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveRoomInfo {

    private int uid;
    private int room_id;
    private String title;
    private String cover;
    private int area_id;
    private String area_name;
    private String parent_area_name;

}
