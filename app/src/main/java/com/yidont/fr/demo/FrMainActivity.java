package com.yidont.fr.demo;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.yidont.fm.base.UniversalActivity;
import com.yidont.fm.perms.AfterPermissionGranted;
import com.yidont.fm.perms.EasyPermissions;
import com.yidont.fm.perms.RCPerms;

import java.util.List;

public class FrMainActivity extends UniversalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_main);
        applyPhoneState();

    }

    /**
     * 申请设备id的权限
     */
    @AfterPermissionGranted(RCPerms.RC_PHONE_STATE)
    private void applyPhoneState() {

        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            //请求获得imei的方法

        } else {
            Log.i("tag_voctex", "apply fot the permission");
            EasyPermissions.requestPermissions(this, "请赋予获取手机设备ID的权限，否则会影响程序运行",
                    RCPerms.RC_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
    }
}
