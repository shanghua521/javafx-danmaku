package com.wang.javafxdanmaku.entity.live;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wang.javafxdanmaku.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveRoomNewsResult extends BaseEntity {
    private LiveRoomNews data;
}
