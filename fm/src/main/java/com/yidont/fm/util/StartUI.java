package com.yidont.fm.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by jp on 2016/10/28.</br>
 */

public class StartUI {

    public static final String KEYBUNDLE="keybundle";

    /**
     * 获取一个key的fragment对象
     * @param key    fragment的类路径
     * @param bundle 所携带的Bundle数据
     */
    public static Fragment newInstanceUIF(String key, Bundle bundle) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) Class.forName(key).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (fragment == null) {
            throw new NullPointerException("fragment为null");
        }
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }



    public static void startActivity(Context mContext, String key) {
        startActivity(mContext,key,null);
    }

    public static void startActivity(Context mContext, String key, Bundle bundle) {
        try {
            Class clazz = Class.forName(key);
            Intent tent = new Intent(mContext, clazz);
            if (bundle!=null) {
                tent.putExtra(KEYBUNDLE, bundle);
            }
            mContext.startActivity(tent);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
