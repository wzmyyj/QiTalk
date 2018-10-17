package com.wzm.tasking.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;


public class ShowLogoutReason extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTv_showLogoutInfo;

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
        setContentView(R.layout.activity_show_logout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_show_logout);
        mTv_showLogoutInfo = (TextView) findViewById(R.id.tv_show_logout_info);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        Intent intent = getIntent();
        mTv_showLogoutInfo.append(intent
                .getStringExtra(MainActivity.LOGOUT_REASON));
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onPause() {
        Intent intent = new Intent(ShowLogoutReason.this, Login.class);
        startActivity(intent);
        super.onDestroy();
    }
}
