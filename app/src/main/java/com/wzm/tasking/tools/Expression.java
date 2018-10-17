package com.wzm.tasking.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import sj.qqkeyboard.DefQqEmoticons;

/**
 * Created by yyj on 2017/8/16 0016.
 */

public class Expression {
    public static SpannableString getSpannableString(Context context, String text, int w, int h) {
        SpannableString ss = new SpannableString(text);
        for (int i = 0; i < DefQqEmoticons.sQqEmoticonKey.length; i++) {
            String s = DefQqEmoticons.sQqEmoticonKey[i];
            if (text.contains(s)) {
                try {
                    Drawable drawable = context.getResources()
                            .getDrawable(DefQqEmoticons.sQqEmoticonHashMap.get(s));
                    if (w == 0) w = drawable.getIntrinsicWidth();
                    if (h == 0) h = drawable.getIntrinsicHeight();
                    drawable.setBounds(0, 0, w, h);
                    int j = 0, a = 0;
                    while (a > -1) {
                        a = text.indexOf(s, j);
                        if (a > -1) {
                            ss.setSpan(new ImageSpan(drawable), a, a + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            j = a + 5;
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
        return ss;
    }

    public static SpannableString getSpannableString(Context context, String text) {
        return getSpannableString(context, text, 0, 0);
    }

}
