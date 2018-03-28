package com.wzm.tasking.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.LanguageManager;
import com.wzm.tasking.tools.ThemeManager;

import java.util.Locale;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

public class Tasking extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLanguage();
        initTheme();
        setContentView(R.layout.activity_welcome);
        if (MainActivity.wel) {
            go_main();
            return;
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                go_main();
            }
        }, 1000);


    }


    private void go_main() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        UserInfo myInfo = JMessageClient.getMyInfo();
        if (myInfo != null) {
            intent.setClass(Tasking.this, MainActivity.class);
        } else {
            intent.setClass(Tasking.this, Login.class);
        }
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    private void initLanguage() {
        int la = LanguageManager.getLa(this);
        switchLanguage(la);

    }

    private void initTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (ThemeManager.isDark(this)) {
            setTheme(ThemeManager.getTheme(7));
        } else {
            int th = ThemeManager.getIndex(this);
            setTheme(ThemeManager.getTheme(th));
        }
    }


    public void switchLanguage(int la) {
        if (la == 0) {
            return;
        }
        //应用内配置语言
        Resources resources = getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        switch (la) {
            case 1:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 2:
                config.locale = Locale.TRADITIONAL_CHINESE;
                break;
            case 3:
                config.locale = Locale.ENGLISH;
                break;
            case 4:
                config.locale = Locale.JAPANESE;
                break;
            case 5:
                config.locale = Locale.KOREA;
                break;
        }
        resources.updateConfiguration(config, dm);
    }

}
