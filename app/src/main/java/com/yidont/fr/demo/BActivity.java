package com.yidont.fr.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yidont.fm.base.UniversalActivity;
import com.yidont.fm.base.UniversalSwipeBackActivity;
import com.yidont.fm.perms.AfterPermissionGranted;
import com.yidont.fm.perms.EasyPermissions;
import com.yidont.fm.perms.RCPerms;

import java.util.List;

public class BActivity extends UniversalSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = new Button(this);
        btn.setText("2222222222222");

        setContentView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

}
