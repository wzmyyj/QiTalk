package com.wzm.tasking.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yyj on 2017/3/22 0022.
 */

public class Theme extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView mList;
    private SimpleAdapter sim_adapter;
    private List<Map<String, Object>> dataList;
    private int[] mImgList = new int[]{R.drawable.theme_bg_0, R.drawable.theme_bg_1,
            R.drawable.theme_bg_2, R.drawable.theme_bg_3,
            R.drawable.theme_bg_4, R.drawable.theme_bg_5,
            R.drawable.theme_bg_6, R.drawable.theme_bg_d};
    private String[] mTextList;
    private int th = 0;
    private TextView tv;
    private boolean is_dark = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initView();
        initData();
        initListener();


    }

    private void initTheme() {
        is_dark = ThemeManager.isDark(this);
        if (is_dark) {
            th = 7;
            WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            tv = new TextView(this);
            tv.setBackgroundColor(0xbb000000);
            manager.addView(tv, params);
        } else {
            th = ThemeManager.getIndex(this);
        }
        setTheme(ThemeManager.getTheme(th));
    }

    private void initView() {
        setContentView(R.layout.activity_theme);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_theme);
        mList = (ListView) findViewById(R.id.lv_theme);
    }

    private void initData() {
        setSupportActionBar(mToolbar);

        mTextList = new String[]{getResources().getString(R.string.theme_0),
                getResources().getString(R.string.theme_1),
                getResources().getString(R.string.theme_2),
                getResources().getString(R.string.theme_3),
                getResources().getString(R.string.theme_4),
                getResources().getString(R.string.theme_5),
                getResources().getString(R.string.theme_6),
                getResources().getString(R.string.theme_7)};

        dataList = new ArrayList<Map<String, Object>>();
        sim_adapter = new SimpleAdapter(this, getDataList(),
                R.layout.activity_theme_item, new String[]{"pic", "text",
                "right"}, new int[]{R.id.img_theme,
                R.id.tv_theme, R.id.img_theme_1});
        mList.setAdapter(sim_adapter);

    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ThemeManager.changeTheme(Theme.this, position);
                if (is_dark) {
                    WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    manager.removeView(tv);
                }
                recreate();
                MainActivity.a = 1;
                Toast.makeText(Theme.this,
                        getResources().getString(R.string.you_picked)
                                + mTextList[position], Toast.LENGTH_SHORT).show();
            }
        });
    }


    private List<? extends Map<String, ?>> getDataList() {
        for (int i = 0; i < mImgList.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", mImgList[i]);
            map.put("text", mTextList[i]);
            if (i == th) {
                map.put("right", R.drawable.right);
            }
            dataList.add(map);
        }

        return dataList;
    }


}
