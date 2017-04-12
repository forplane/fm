package com.yidont.fm.perms;

/**
 * Created by Voctex on 2016/9/1.
 * <p>
 * permission  code
 */
public class RCPerms {

    /**
     *
     */
    public static final int RC_SETTINGS_CODE = 16016;
    /**
     * 手机状态值，如imei
     */
    public static final int RC_PHONE_STATE = 120;
    /**
     * 写入内存卡权限
     */
    public static final int RC_WRITE_EXTERNAL_STORAGE = RC_PHONE_STATE + 1;
    /**
     * 读取内存卡权限
     */
    public static final int RC_READ_EXTERNAL_STORAGE = RC_WRITE_EXTERNAL_STORAGE + 1;
    /**
     * 相机权限
     */
    public static final int RC_CAMERA_PERM = RC_READ_EXTERNAL_STORAGE + 1;
    /**
     * 获取定位权限
     */
    public static final int RC_ACCESS_COARSE_LOCATION = RC_CAMERA_PERM + 1;
    /**
     * wifi状态
     */
    public static final int RC_WIFI_STATUS = RC_ACCESS_COARSE_LOCATION + 1;

//    public static final int RC_PHONE_STATE=123;
//    public static final int RC_PHONE_STATE=123;

}
