package com.wzm.tasking.activity;

import android.app.ProgressDialog;
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

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;


public class UpdatePassword extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mBt_updatePassword;
    private EditText mEt_OldPassword;
    private EditText mEt_NewPassword;
    private EditText mEt_assertEquals;
    private Button mBt_assertEquals;
    private ProgressDialog mProgressDialog;

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
        setContentView(R.layout.activity_update_password);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_update_password);
        mBt_updatePassword = (Button) findViewById(R.id.bt_update_password);
        mEt_OldPassword = (EditText) findViewById(R.id.et_update_password_old);
        mEt_NewPassword = (EditText) findViewById(R.id.et_update_password_new);
        mEt_assertEquals = (EditText) findViewById(R.id.et_update_password_assertEquals);
        mBt_assertEquals = (Button) findViewById(R.id.bt_update_password_assertEquals);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBt_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String oldPassword = mEt_OldPassword.getText()
                        .toString();
                final String newPassword = mEt_NewPassword.getText()
                        .toString();
                mProgressDialog = ProgressDialog.show(UpdatePassword.this,
                        getResources().getString(R.string.Tip),
                        getResources().getString(R.string.loading));
                mProgressDialog.setCanceledOnTouchOutside(true);
                JMessageClient.updateUserPassword(oldPassword, newPassword,
                        new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode,
                                                  String updatePasswordDesc) {
                                if (responseCode == 0) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.update_success),
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.update_fail),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        mBt_assertEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPwd = mEt_assertEquals.getText().toString();
                boolean passwordValid = JMessageClient
                        .isCurrentUserPasswordValid(inputPwd);
                if (passwordValid) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.assertEquals_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.assertEquals_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
