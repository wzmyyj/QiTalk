package com.wzm.tasking.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.LanguageManager;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Language extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView mList;
    private Button mButton;
    private SimpleAdapter sim_adapter;
    private List<Map<String, Object>> dataList;
    private int[] mImgList = {R.drawable.la_0, R.drawable.la_1, R.drawable.la_2,
            R.drawable.la_3, R.drawable.la_4, R.drawable.la_5};
    private String[] mTextList = {"System", "简体中文", "繁體中文", "English",
            "日本語", "한국어"};
    private int la = 0;

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
        setContentView(R.layout.activity_language);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_language);
        mList = (ListView) findViewById(R.id.lv_language);
        mButton = (Button) findViewById(R.id.bt_language);

    }

    private void initData() {
        setSupportActionBar(mToolbar);
        la = LanguageManager.getLa(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataList = new ArrayList<Map<String, Object>>();
        sim_adapter = new SimpleAdapter(this, getDataList(),
                R.layout.activity_language_item, new String[]{"pic", "text",
                "right"}, new int[]{R.id.img_language,
                R.id.tv_language, R.id.img_language_1});
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
                la = position;
                for (int i = 0; i < mImgList.length; i++) {
                    dataList.get(i).put("right", null);
                }
                dataList.get(position).put("right", R.drawable.right);
                sim_adapter.notifyDataSetChanged();
                Toast.makeText(Language.this,
                        getResources().getString(R.string.you_picked)
                                + mTextList[position], Toast.LENGTH_SHORT).show();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageManager.setLa(Language.this, la);
                Intent intent = new Intent(Language.this, Tasking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // 杀掉进程
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });


    }

    private List<? extends Map<String, ?>> getDataList() {
        for (int i = 0; i < mImgList.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", mImgList[i]);
            map.put("text", mTextList[i]);
            if (i == la) {
                map.put("right", R.drawable.right);
            }
            dataList.add(map);
        }

        return dataList;
    }
}
