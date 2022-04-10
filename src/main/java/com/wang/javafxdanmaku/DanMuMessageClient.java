package com.wang.javafxdanmaku;

import cn.hutool.core.util.ZipUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.javafxdanmaku.handler.DanMuMsgHandler;
import com.wang.javafxdanmaku.handler.InteractWordHandler;
import com.wang.javafxdanmaku.pane.DanMuPane;
import javafx.application.Platform;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import struct.JavaStruct;
import struct.StructException;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.zip.InflaterOutputStream;

@Slf4j
public class DanMuMessageClient extends WebSocketClient {

    private static final ScheduledThreadPoolExecutor executor =
            new ScheduledThreadPoolExecutor(5, new ThreadPoolExecutor.CallerRunsPolicy());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Consumer<JsonNode>> commandHandlers = new HashMap<>();

    static {
        commandHandlers.put("DANMU_MSG", new DanMuMsgHandler());
        commandHandlers.put("INTERACT_WORD", new InteractWordHandler());
    }

    public DanMuMessageClient(URI serverUri) {
        super(serverUri);
    }

    public static void handleZlibMessage(byte[] bytes) throws Exception {
        int offect = 0;
        String resultStr = null;
        int maxLen = bytes.length;
        int data_len;
        int head_len;
        int data_ver;
        int data_type;
        int data_other = 0;
        byte[] bs;
//		int index = 1;
//		LOGGER.debug(maxLen+"长的母数据包打印开始--------------------------------------------------------------------------------");
        while (offect < maxLen) {
            byte[] byte_c = subBytes(bytes, offect, maxLen - offect);

            BarrageHeader barrageHeadHandle = unpack(byte_c);
            data_len = barrageHeadHandle.getPackageLength();
            head_len = barrageHeadHandle.getPackageHeadLength();
            data_ver = barrageHeadHandle.getPackageVersion();
            data_type = barrageHeadHandle.getPackageType();
//            data_other = barrageHeadHandle.getPackageOther();

//			LOGGER.debug("子包<"+index+">:"+"("+data_len+","+head_len+","+data_ver+","+data_type+","+data_other+")");
            bs = subBytes(byte_c, head_len, data_len - head_len);
//			resultStr=HexUtils.toHexString(bs);
//            log.info("data_ver {} data_type {}", data_ver, data_type);
            if (data_ver == 2) {
                if (data_type == 5) {
                    resultStr = ZipUtil.unZlib(bs, "utf-8");
//					resultStr = ByteUtils.unicodeToString(resultStr);
                    log.debug("其他未处理消息(2):" + resultStr);
                } else {
                    resultStr = toHexString(bs);
                    log.debug("！！！！！！！！！！未知数据(2)v:" + data_ver + "t:" + data_type + ":" + resultStr);
                }
            } else if (data_ver == 1) {
                if (data_type == 3) {
                    log.debug("房间人气：{}", bytesToLong(bs));
                } else {
                    resultStr = toHexString(bs);
                    log.debug("！！！！！！！！！！未知数据(2)v:" + data_ver + "t:" + data_type + ":" + resultStr);
                }
            } else if (data_ver == 0 && data_type == 5) {
                resultStr = new String(bs, StandardCharsets.UTF_8);
                handleMessage(resultStr);

//                log.info("data_ver {} data_type {} resultStr：{}", data_ver, data_type, resultStr);
            } else {
                resultStr = toHexString(bs);
                log.debug("！！！！！！！！！！未知数据(2)v:" + data_ver + "t:" + data_type + ":" + resultStr);
            }
            log.debug("data_ver {} data_type {} resultStr：{}", data_ver, data_type, resultStr);
            offect += data_len;
        }
    }

    public static void handleMessage(String message) throws JsonProcessingException {
        var jsonNode = objectMapper.readTree(message);
        var cmd = jsonNode.get("cmd");

        var handler = commandHandlers.get(cmd.asText());
        if (handler != null) {
            handler.accept(jsonNode);
        }
    }

    public static long bytesToLong(byte[] bs) throws Exception {
        int bytes = bs.length;
        if (bytes > 1) {
            if ((bytes % 2) != 0 || bytes > 8) {
                throw new Exception("not support");
            }
        }
        return switch (bytes) {
            case 0 -> 0;
            case 1 -> (bs[0] & 0xff);
            case 2 -> (bs[0] & 0xff) << 8 | (bs[1] & 0xff);
            case 4 -> (bs[0] & 0xffL) << 24 | (bs[1] & 0xffL) << 16 | (bs[2] & 0xffL) << 8 | (bs[3] & 0xffL);
            case 8 -> (bs[0] & 0xffL) << 56 | (bs[1] & 0xffL) << 48 | (bs[2] & 0xffL) << 40 | (bs[3] & 0xffL) << 32 |
                    (bs[4] & 0xffL) << 24 | (bs[5] & 0xffL) << 16 | (bs[6] & 0xffL) << 8 | (bs[7] & 0xffL);
            default -> throw new Exception("not support");
        };
    }

    public static String toHexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        StringBuilder sb = new StringBuilder(bytes.length << 1);

        for (byte aByte : bytes) {
            sb.append(Integer.toHexString(aByte));
        }
        return sb.toString();
    }

    public static byte[] subBytes(byte[] bytes, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(bytes, begin, bs, 0, count);
        return bs;
    }

    private static BarrageHeader unpack(byte[] bytes) {
        BarrageHeader barrageHeader = new BarrageHeader();
        try {
            JavaStruct.unpack(barrageHeader, bytes);
        } catch (StructException ignored) {
        }
        return barrageHeader;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("连接成功");
    }

    @Override
    public void onMessage(String s) {
//        System.out.println(s);
    }

    @SneakyThrows
    @Override
    public void onMessage(ByteBuffer byteBuffer) {
        executor.execute(() -> {
            try {
                handleMessage(byteBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void handleMessage(ByteBuffer byteBuffer) throws Exception {
        var len = byteBuffer.limit() - byteBuffer.position();
        byte[] resultByte = new byte[len];
        byteBuffer.get(resultByte);

        var barrageHeader = new BarrageHeader();
        JavaStruct.unpack(barrageHeader, resultByte);

        int packageLength = barrageHeader.getPackageLength();
        int packageHeaderLength = barrageHeader.getPackageHeadLength();
        int packageVersion = barrageHeader.getPackageVersion();
        int packageType = barrageHeader.getPackageType();

        var count = packageLength - packageHeaderLength;
        byte[] baseResultBytes = new byte[count];
        System.arraycopy(resultByte, packageHeaderLength, baseResultBytes, 0, count);

        if (packageVersion == 2) {
            if (packageType == 5) {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                InflaterOutputStream zos = new InflaterOutputStream(bos);
                zos.write(baseResultBytes);
                zos.close();
                byte[] unZlibBytes = bos.toByteArray();
                handleZlibMessage(unZlibBytes);
            } else {
                String resultStr = toHexString(baseResultBytes);
                log.debug("！！！！！！！！！！未知数据(1)v:" + packageVersion + "t:" + packageType + ":" + resultStr);
            }
        } else if (packageVersion == 1) {
            if (packageType == 3) {
                var roomPeopleNum = bytesToLong(baseResultBytes);
                Platform.runLater(() -> DanMuPane.getInstance().viewNumValue.setText(String.valueOf(roomPeopleNum)));
                log.debug("房间人气 {}", roomPeopleNum);
            } else if (packageType == 8) {
                // 返回{code 0} 验证头消息成功后返回
                String resultStr = new String(baseResultBytes, StandardCharsets.UTF_8);

            } else {
                String resultStr = toHexString(baseResultBytes);
                log.warn("！！！！！！！！！！未知数据(1)v:" + packageVersion + "t:" + packageType + ":" + resultStr);
            }
        } else if (packageVersion == 0) {
            // 弹幕消息 ，目前发现的有发送的 gif，以及一个 STOP_LIVE_ROOM_LIST
            String messageString = new String(baseResultBytes, StandardCharsets.UTF_8);
            log.debug("弹幕消息 {}", messageString);
        } else {
            String resultStr = toHexString(baseResultBytes);
            log.warn("！！！！！！！！！！未知数据(1)v:" + packageVersion + "t:" + packageType + ":" + resultStr);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.warn("websocket connect close(连接已经断开)，纠错码: {} reason {}", code, reason);
    }

    @Override
    public void onError(Exception e) {
        log.error(e.getMessage());
    }
}
