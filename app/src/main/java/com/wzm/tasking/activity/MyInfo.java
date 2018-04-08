package com.wzm.tasking.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;
import com.wzm.tasking.view.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class MyInfo extends AppCompatActivity {
    private Toolbar mToolbar;
    private CircleImageView img_header;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
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
        setContentView(R.layout.activity_my_info);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_my_info);
        img_header = (CircleImageView) findViewById(R.id.img_my_info);
        tv_1 = (TextView) findViewById(R.id.tv_my_info_1);
        tv_2 = (TextView) findViewById(R.id.tv_my_info_2);
        tv_3 = (TextView) findViewById(R.id.tv_my_info_3);
        tv_4 = (TextView) findViewById(R.id.tv_my_info_4);
        tv_5 = (TextView) findViewById(R.id.tv_my_info_5);
        tv_6 = (TextView) findViewById(R.id.tv_my_info_6);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        setInfo(JMessageClient.getMyInfo());
    }

    private void setInfo(UserInfo userInfo) {
        String gender, birthday = "";
        if (userInfo.getGender() == UserInfo.Gender.male) {
            gender = getResources().getString(R.string.male);
        } else if (userInfo.getGender() == UserInfo.Gender.female) {
            gender = getResources().getString(R.string.female);
        } else {
            gender = getResources().getString(R.string.unknown);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd");
        if (userInfo.getBirthday() > 0) {
            birthday = sdf.format(new Date(userInfo.getBirthday()));
        }
        tv_1.setText(userInfo.getUserName());
        tv_2.setText(userInfo.getNickname());
        tv_3.setText(userInfo.getSignature());
        tv_4.setText(gender);
        tv_5.setText(birthday);
        tv_6.setText(userInfo.getRegion());
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap != null) {
                    img_header.setImageBitmap(bitmap);
                } else {
                    img_header.setImageResource(R.drawable.ic_header);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setInfo(JMessageClient.getMyInfo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_write) {
            Intent i = new Intent();
            i.setClass(MyInfo.this, UpdateMyInfo.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MyInfo.this, SaveAvatar.class);
                startActivity(i);
            }
        });

    }
}
