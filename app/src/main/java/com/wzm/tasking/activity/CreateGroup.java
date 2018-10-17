package com.wzm.tasking.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;


public class CreateGroup extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button mBt_create;
    private EditText mEt_groupDesc;
    private EditText mEt_groupName;
    private ProgressDialog mProgressDialog;
    private int h = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initView();
        initData();
        initListener();
    }


    private void initTheme() {
        int th = MainActivity.th;
        if (th == 7) {
            setTheme(ThemeManager.getTheme(7));
            WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            TextView tv = new TextView(this);
            tv.setBackgroundColor(0xbb000000);
            manager.addView(tv, params);
        } else {
            setTheme(ThemeManager.getTheme(th));
        }
    }

    private void initView() {
        setContentView(R.layout.activity_create_group);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_create_group);
        mEt_groupName = (EditText) findViewById(R.id.et_group_name);
        mEt_groupDesc = (EditText) findViewById(R.id.et_group_desc);
        mBt_create = (Button) findViewById(R.id.bt_create_group);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBt_create.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h != 0) {
                    return;
                }
                try {
                    createGroup();
                    h = 1;
                } catch (Exception e) {
                }

            }
        });
    }

    protected void createGroup() {
        mProgressDialog = ProgressDialog.show(CreateGroup.this,
                getResources().getString(R.string.Tip),
                getResources().getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(true);
        String name = mEt_groupName.getText().toString();
        String desc = mEt_groupDesc.getText().toString();
        // 创建群组
        JMessageClient.createGroup(name, desc, new CreateGroupCallback() {
            @Override
            public void gotResult(int i, String s, long l) {
                if (i == 0) {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.create_success),
                            Toast.LENGTH_SHORT).show();
                    Intent g = new Intent();
                    g.putExtra("id", l);
                    g.setClass(getApplicationContext(), GetGroupInfo.class);
                    startActivity(g);
                    finish();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.create_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
