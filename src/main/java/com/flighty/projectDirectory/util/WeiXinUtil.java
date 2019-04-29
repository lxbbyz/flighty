package com.flighty.projectDirectory.util;

import java.util.UUID;

public class WeiXinUtil {

    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public  static String getAPP_ID() {
        return APP_ID;
    }

    public static String getSECRET() {
        return SECRET;
    }

    public  static String getMpAPP_ID() {
        return MpAPP_ID;
    }

    public static String getMpSECRET() {
        return MpSECRET;
    }

    /**
     * 微信公众号appid
     */
    private static String APP_ID = "wxd77225d57a5bb417";
    private static String SECRET = "eb89394cb881f99f3c7eb9eccc66d91d";

    /**
     * 微信小程序appid
     */
    private static String MpAPP_ID = "wx1a8feadfcd6179c8";
    private static String MpSECRET = "ddd5fa2006dcf47f48cd4ce0249971c6";


}

