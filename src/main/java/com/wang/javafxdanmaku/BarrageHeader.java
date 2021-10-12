package com.wang.javafxdanmaku;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import struct.StructClass;
import struct.StructField;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author TODAY 2021/5/18 22:21
 */
@Data
@StructClass
@NoArgsConstructor
@AllArgsConstructor
public class BarrageHeader implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 数据包长度 为int
    @StructField(order = 0)
    private int packageLength;
    // 数据包头部长度 为char 为16
    @StructField(order = 1)
    private char packageHeadLength;
    // 数据包协议版本char 0未压缩的json格式数据 1客户端心跳通常为人气值 4字节整数 2为带zlib压缩过的json格式数据 数据包协议版本 为char 有0，1，2
    @StructField(order = 2)
    private char packageVersion;
    // 数据包协议类型 int 目前已知有2，3，5，7，8
    @StructField(order = 3)
    private int packageType;
    //序列号 int 目前已知有0，1
    @StructField(order = 4)
    private int packageOther;

    public BarrageHeader(int packageLength) {
        this(packageLength + 16, (char) 16,
                (char) 1,
                7,
                1);

    }

}
