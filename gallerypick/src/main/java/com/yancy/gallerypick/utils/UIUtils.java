package com.yancy.gallerypick.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yancy.gallerypick.R;

import java.lang.reflect.Field;

/**
 * UI工具类
 * Created by Yancy on 2015/9/19.
 */
public class UIUtils {

    /**
     * @return : 获取状态栏的高度
     */
    private static int getStatusBarHeight() {
        Class<?> c;
        Object obj;
        Field field;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return x;
    }

    /**
     * 如果系统为4.4以上 ，则隐藏状态栏
     *
     * @param activity activity
     * @param resource 资源文件最外层 layout Id
     */
    public static void hideTitleBar(Activity activity, int resource) {
        if (Build.VERSION.SDK_INT > 18) {  // 如果系统为4.4以上 ，则隐藏状态栏
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup whole_layout = (ViewGroup) activity.findViewById(resource);  // 页面整体布局
            whole_layout.setPadding(0, activity.getResources().getDimensionPixelSize(getStatusBarHeight()), 0, 0);
            whole_layout.setBackgroundResource(getColor(activity));
        }
    }

    public static int getColor(Activity activity) {
        int color = R.color.gallery_blue;
        SharedPreferences sha = activity.getSharedPreferences("theme", 0);
        boolean d = sha.getBoolean("dark", false);
        if (d) {
            color = R.color.colorApp7;
        } else {
            int j = sha.getInt("th", 0);
            switch (j) {
                case 0:
                    color = R.color.colorApp;
                    break;
                case 1:
                    color = R.color.colorApp1;
                    break;
                case 2:
                    color = R.color.colorApp2;
                    break;
                case 3:
                    color = R.color.colorApp3;
                    break;
                case 4:
                    color = R.color.colorApp4;
                    break;
                case 5:
                    color = R.color.colorApp5;
                    break;
                case 6:
                    color = R.color.colorApp6;
                    break;
            }
        }
        return color;
    }

}
/*
 *   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 *     ┃　　　┃
 *     ┃　　　┃
 *     ┃　　　┗━━━┓
 *     ┃　　　　　　　┣┓
 *     ┃　　　　　　　┏┛
 *     ┗┓┓┏━┳┓┏┛
 *       ┃┫┫　┃┫┫
 *       ┗┻┛　┗┻┛
 *        神兽保佑
 *        代码无BUG!
 */