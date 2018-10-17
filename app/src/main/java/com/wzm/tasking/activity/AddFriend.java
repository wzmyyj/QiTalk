package com.wzm.tasking.activity;


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

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.api.BasicCallback;


public class AddFriend extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mEt_reason;
    private Button mButton;
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
        setContentView(R.layout.activity_add_friend);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_add_friend);
        mEt_reason = (EditText) findViewById(R.id.et_reason);
        mButton = (Button) findViewById(R.id.bt_add_friend);

    }

    private void initData() {
        setSupportActionBar(mToolbar);
        Intent i = getIntent();
        username = i.getStringExtra("u");

    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = mEt_reason.getText().toString();
                ContactManager.sendInvitationRequest(username, null, reason,
                        new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.invite_success),
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.invite_fail)
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }


}
