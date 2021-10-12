package com.wang.javafxdanmaku.entity.live;

import com.wang.javafxdanmaku.entity.HostServer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class LiveConf implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Short business_id;
    private String group;
    private List<HostServer> host_list;
    private Short max_delay;
    private Short refresh_rate;
    private Short refresh_row_factor;
    private String token;
}
