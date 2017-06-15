package com.yidont.fm.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

import com.yidont.flux.Flux;
import com.yidont.fm.R;
import com.yidont.fm.SingleFragmentUIA;
import com.yidont.fm.perms.AppSettingsDialog;
import com.yidont.fm.perms.EasyPermissions;
import com.yidont.fm.perms.RCPerms;

import java.util.List;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Created by Voctex on 2016/11/2.
 * 可侧滑退出，有动态权限管理的Activity
 */
public class UniversalSwipeBackActivity extends SwipeBackActivity implements
        EasyPermissions.PermissionCallbacks {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SingleFragmentUIA.setWindowStatusBarColor(this, R.color.themecolors);
        super.onCreate(savedInstanceState);
        mContext = this;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Flux.getDefault().unregister(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RCPerms.RC_SETTINGS_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
//            Toast.makeText(this, R.string.perms_ask_settings_content, Toast.LENGTH_SHORT)
//                    .show();


            // 申请权限回调回来的结果
            dealPermsResult();
        }
    }


    protected void dealPermsResult() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {


        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.perms_ask_again_content))
                    .setTitle(getString(R.string.perms_title_settings))
                    .setPositiveButton(getString(R.string.perms_settings))
                    .setNegativeButton(getString(R.string.perms_cancel), null /* click listener */)
                    .setRequestCode(RCPerms.RC_SETTINGS_CODE)
                    .build()
                    .show();
        }
    }


    @Override
    public void finish() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        super.finish();
    }
}
