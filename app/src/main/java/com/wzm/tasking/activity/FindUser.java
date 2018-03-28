package com.wzm.tasking.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;


public class FindUser extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mEt_find;
    private Button mBt_find;
    private String username;

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
        setContentView(R.layout.activity_find_user);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_find_friend);
        mEt_find = (EditText) findViewById(R.id.et_find_friend);
        mBt_find = (Button) findViewById(R.id.bt_find_friend);
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
        mBt_find.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                username = mEt_find.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.username_cannot_be_null),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfo myInfo = JMessageClient.getMyInfo();
                if (myInfo.getUserName().equals(username)) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), MyInfo.class);
                    startActivity(i);
                    finish();
                } else {
                    JMessageClient.getUserInfo(username, null,
                            new GetUserInfoCallback() {
                                @Override
                                public void gotResult(int i, String msg,
                                                      UserInfo userInfo) {
                                    if (i == 0) {
                                        Intent u = new Intent();
                                        u.putExtra("u", username);
                                        u.setClass(getApplicationContext(),
                                                GetUserInfo.class);
                                        startActivity(u);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                getResources().getString(R.string.cannot_find_this_uer),
                                                Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            });

                }
            }
        });
    }
}
