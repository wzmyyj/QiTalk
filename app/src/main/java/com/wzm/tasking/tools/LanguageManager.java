package com.wzm.tasking.tools;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by yyj on 2017/7/21 0021.
 */

public class LanguageManager {

    public static int getLa(Context context) {
        SharedPreferences sha = context.getSharedPreferences("language", 0);
        int la = sha.getInt("la", 0);
        return la;
    }

    public static void setLa(Context context, int i) {
        SharedPreferences sha = context.getSharedPreferences("language", 0);
        SharedPreferences.Editor ed = sha.edit();
        ed.putInt("la", i);
        ed.commit();
    }
}
