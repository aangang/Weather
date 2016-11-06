package com.guofeng.weather.util;

/**
 * Emoji转换类
 * Created by GUOFENG on 2016/11/6.
 */

public class Emoji {
    private final static int XIOAKU = 0x1F602;
    private final static int JINGYA = 0x1F631;
    private final static int XIEYAN = 0x1F612;

    public static String getEmoji(String emoji) {
        int emojiUnicode = chooseEmoji(emoji);
        return new String(Character.toChars(emojiUnicode));
    }

    private static int chooseEmoji(String emoji) {
        int code;
        switch (emoji) {
            case "笑哭":
                code = XIOAKU;
                break;
            case "惊讶":
                code = JINGYA;
                break;
            case "斜眼":
                code = XIEYAN;
                break;
            default:
                code = XIEYAN;
                break;
        }
        return code;
    }
}
