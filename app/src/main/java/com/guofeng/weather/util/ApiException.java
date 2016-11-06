package com.guofeng.weather.util;

/**
 * Created by GUOFENG on 2016/11/5.
 */

public class ApiException extends RuntimeException {

    public static final int NO_MORE_REQUESTS = 100;
    public static final int UNKNOW_CITY = 101;

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    private static String getApiExceptionMessage(int code) {
        String message;
        switch (code) {
            case NO_MORE_REQUESTS:
                message = "Sorry! 今日API免费次数用完啦，感谢支持";
                break;
            case UNKNOW_CITY:
                message = "Sorry！暂时不支持此天气查询";
                break;
            default:
                message = "未知错误";
        }
        return message;
    }

}
