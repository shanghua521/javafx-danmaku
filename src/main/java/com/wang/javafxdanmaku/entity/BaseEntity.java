package com.wang.javafxdanmaku.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseEntity {

    private int code;
    private String message;
    private int ttl;

}
