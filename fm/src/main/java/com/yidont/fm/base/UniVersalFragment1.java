package com.yidont.fm.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yidont.flux.Flux;
import com.yidont.fm.R;
import com.yidont.fm.perms.AppSettingsDialog;
import com.yidont.fm.perms.EasyPermissions;
import com.yidont.fm.perms.RCPerms;

import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Voctex on 2016/11/11.
 * <p>
 * 普通片段，无侧滑效果，有动态权限功能
 */
public abstract class UniVersalFragment1 extends SupportFragment implements
        EasyPermissions.PermissionCallbacks {

    protected ViewGroup mViewGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null != mViewGroup || savedInstanceState != null) {
            if (mViewGroup == null) {
                mViewGroup = baseCreateView(inflater, container, savedInstanceState);
            }else {
                ViewGroup parent = (ViewGroup) mViewGroup.getParent();
                if (null != parent) {
                    parent.removeView(mViewGroup);
                }
            }
        } else {
            mViewGroup = baseCreateView(inflater, container, savedInstanceState);
        }
        return mViewGroup;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Flux.getDefault().unregister(this);

    }

    /**
     * 抽象具体的子类界面
     */
    protected abstract ViewGroup baseCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

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
//            Toast.makeText(__mActivity, R.string.perms_ask_settings_content, Toast.LENGTH_SHORT)
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

}
