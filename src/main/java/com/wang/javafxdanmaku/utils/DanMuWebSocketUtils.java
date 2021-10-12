package com.wang.javafxdanmaku.utils;

import org.java_websocket.client.WebSocketClient;

public class DanMuWebSocketUtils {

    private static final int[] DEC = {
            00, 01, 02, 03, 04, 05, 06, 07, 8, 9, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15,
    };

    public static int getDec(int index) {
        // Fast for correct values, slower for incorrect ones
        try {
            return DEC[index - '0'];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    public static byte[] fromHexString(String input) {
        if (input == null) {
            return null;
        }

        char[] inputChars = input.toCharArray();
        byte[] result = new byte[input.length() >> 1];
        for (int i = 0; i < result.length; i++) {
            int upperNibble = getDec(inputChars[2 * i]);
            int lowerNibble = getDec(inputChars[2 * i + 1]);
            result[i] = (byte) ((upperNibble << 4) + lowerNibble);
        }
        return result;
    }

    public static void send(WebSocketClient webSocketClient, byte[] headerBytes, byte[] dataBytes) {
        byte[] bytes = new byte[headerBytes.length + dataBytes.length];
        System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
        System.arraycopy(dataBytes, 0, bytes, headerBytes.length, dataBytes.length);

        webSocketClient.send(bytes);
    }

}
