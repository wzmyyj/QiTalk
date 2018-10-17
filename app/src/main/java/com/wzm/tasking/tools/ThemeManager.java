package com.wzm.tasking.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.wzm.tasking.R;

/**
 * Created by yyj on 2017/3/23 0023.
 */

public class ThemeManager {
    private static int[] themeList = {R.style.AppTheme_Blue, R.style.AppTheme_Sky,
            R.style.AppTheme_Pink, R.style.AppTheme_Red, R.style.AppTheme_Green,
            R.style.AppTheme_Yellow, R.style.AppTheme_Purple, R.style.AppTheme_Dark};
    private static int[] colorList = {R.color.colorApp, R.color.colorApp1,
            R.color.colorApp2, R.color.colorApp3, R.color.colorApp4,
            R.color.colorApp5, R.color.colorApp6, R.color.colorApp7,};

    public static int getTheme(int i) {
        return themeList[i];
    }

    public static int getColor(int i) {
        return colorList[i];
    }

    public static int getIndex(Context context) {
        SharedPreferences sha = context.getSharedPreferences("theme", 0);
        int j = sha.getInt("th", 0);
        return j;
    }

    public static boolean isDark(Context context) {
        SharedPreferences sha = context.getSharedPreferences("theme", 0);
        boolean d = sha.getBoolean("dark", false);
        return d;
    }

    public static void changeTheme(Context context, int i) {
        SharedPreferences sha = context.getSharedPreferences("theme", 0);
        SharedPreferences.Editor ed = sha.edit();
        if (i == 7) {
            ed.putBoolean("dark", true);
        } else {
            ed.putInt("th", i);
            ed.putBoolean("dark", false);
        }
        ed.commit();
    }

    public static void changeDark(Context context) {
        SharedPreferences sha = context.getSharedPreferences("theme", 0);
        SharedPreferences.Editor ed = sha.edit();
        boolean d = sha.getBoolean("dark", false);
        if (d) {
            ed.putBoolean("dark", false);
        } else {
            ed.putBoolean("dark", true);
        }
        ed.commit();
    }


}
