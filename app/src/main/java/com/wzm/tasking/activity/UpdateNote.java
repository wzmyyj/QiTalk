package com.wzm.tasking.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;

import cn.jpush.im.api.BasicCallback;

import static com.wzm.tasking.activity.GetUserInfo.Info;


public class UpdateNote extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mEt_note;
    private Button mBt_update;
    private String username;

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
        setContentView(R.layout.activity_update_note);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_set_user_info);
        mEt_note = (EditText) findViewById(R.id.et_set_note);
        mBt_update = (Button) findViewById(R.id.bt_set_note);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        username = Info.getUserName();
        mEt_note.setText(Info.getNotename());
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_note();
            }
        });
    }

    private void update_note() {
        final String note = mEt_note.getText().toString();
        Info.updateNoteName(note, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.update_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.update_fail),
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


}
