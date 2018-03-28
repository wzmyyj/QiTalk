package com.wzm.tasking.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;

import cn.jpush.im.android.api.JMessageClient;

public class NotifyType extends AppCompatActivity implements View.OnClickListener {
    public static final int FLAG_NOTIFY_DEFAULT = 2147483647;
    public static final int FLAG_NOTIFY_SILENCE = 0;
    public static final int FLAG_NOTIFY_DISABLE = -2147483648;
    public static final int FLAG_NOTIFY_WITH_SOUND = 1;
    public static final int FLAG_NOTIFY_WITH_VIBRATE = 2;
    public static final int FLAG_NOTIFY_WITH_LED = 4;
    private Button bt_0;
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private Button bt_5;
    private Toolbar mToolbar;
    private int th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initView();
        initData();
        initListener();
    }


    private void initTheme() {
        th = MainActivity.th;
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
        setContentView(R.layout.activity_notify_type);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_notify_type);
        bt_0 = (Button) findViewById(R.id.bt_notify_type_0);
        bt_1 = (Button) findViewById(R.id.bt_notify_type_1);
        bt_2 = (Button) findViewById(R.id.bt_notify_type_2);
        bt_3 = (Button) findViewById(R.id.bt_notify_type_3);
        bt_4 = (Button) findViewById(R.id.bt_notify_type_4);
        bt_5 = (Button) findViewById(R.id.bt_notify_type_5);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        setButtonTextColor();
    }


    private void initListener() {
        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_notify_type_0:
                JMessageClient.setNotificationFlag(FLAG_NOTIFY_DEFAULT);
                break;
            case R.id.bt_notify_type_1:
                JMessageClient.setNotificationFlag(FLAG_NOTIFY_DISABLE);
                break;
            case R.id.bt_notify_type_2:
                JMessageClient.setNotificationFlag(FLAG_NOTIFY_SILENCE);
                break;
            case R.id.bt_notify_type_3:
                JMessageClient.setNotificationFlag(FLAG_NOTIFY_WITH_SOUND);
                break;
            case R.id.bt_notify_type_4:
                JMessageClient.setNotificationFlag(FLAG_NOTIFY_WITH_VIBRATE);
                break;
            case R.id.bt_notify_type_5:
                JMessageClient.setNotificationFlag(FLAG_NOTIFY_WITH_LED);
                break;
        }
        setButtonTextColor();
        showToast();
    }


    private void setButtonTextColor() {
        bt_0.setTextColor(getResources().getColor(R.color.colorGray_3));
        bt_1.setTextColor(getResources().getColor(R.color.colorGray_3));
        bt_2.setTextColor(getResources().getColor(R.color.colorGray_3));
        bt_3.setTextColor(getResources().getColor(R.color.colorGray_3));
        bt_4.setTextColor(getResources().getColor(R.color.colorGray_3));
        bt_5.setTextColor(getResources().getColor(R.color.colorGray_3));
        int flag = JMessageClient.getNotificationFlag();
        switch (flag) {
            case FLAG_NOTIFY_DEFAULT:
                bt_0.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                break;
            case FLAG_NOTIFY_DISABLE:
                bt_1.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                break;
            case FLAG_NOTIFY_SILENCE:
                bt_2.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                break;
            case FLAG_NOTIFY_WITH_SOUND:
                bt_3.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                break;
            case FLAG_NOTIFY_WITH_VIBRATE:
                bt_4.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                break;
            case FLAG_NOTIFY_WITH_LED:
                bt_5.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                break;
        }

    }

    private void showToast() {
        Toast.makeText(getApplicationContext(),
                getResources().getString(R.string.set_success),
                Toast.LENGTH_SHORT).show();
    }
}
