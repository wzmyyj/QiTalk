package com.wzm.tasking.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.adapter.MyPagerAdapter;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;


public class Login extends AppCompatActivity {
    //content
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    //login
    private EditText mEd_login_username;
    private EditText mEd_login_password;
    private CheckBox mCh_save;
    private Button mBt_forget;
    private Button mBt_login;
    //register
    private EditText mEd_register_username;
    private EditText mEd_register_password;
    private EditText mEd_register_password2;
    private Button mBt_register;
    //save
    private SharedPreferences sha;
    private SharedPreferences.Editor ed;
    private ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initView();
        initData();
        initListener();
    }

    private void initTheme() {
        if (ThemeManager.isDark(this)) {
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
            int th = ThemeManager.getIndex(this);
            setTheme(ThemeManager.getTheme(th));
        }
    }

    private void initView() {
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_login);
        mTabLayout = (TabLayout) findViewById(R.id.tab_login);
        mViewPager = (ViewPager) findViewById(R.id.vp_login);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.activity_login_1, null);
        View view2 = inflater.inflate(R.layout.activity_login_2, null);
        List<View> viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        String[] titles = new String[]{getResources().getString(R.string.login),
                getResources().getString(R.string.register)};
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(viewList, titles);
        mViewPager.setAdapter(pagerAdapter);
        //login
        mEd_login_username = (EditText) view1.findViewById(R.id.ed_login_username);
        mEd_login_password = (EditText) view1.findViewById(R.id.ed_login_password);
        mCh_save = (CheckBox) view1.findViewById(R.id.ch_login_save);
        mBt_forget = (Button) view1.findViewById(R.id.bt_login_forget);
        mBt_login = (Button) view1.findViewById(R.id.bt_login);
        //register
        mEd_register_username = (EditText) view2.findViewById(R.id.ed_register_username);
        mEd_register_password = (EditText) view2.findViewById(R.id.ed_register_password);
        mEd_register_password2 = (EditText) view2.findViewById(R.id.ed_register_password2);
        mBt_register = (Button) view2.findViewById(R.id.bt_register);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        mTabLayout.setupWithViewPager(mViewPager);
        sha = getSharedPreferences("log", Activity.MODE_PRIVATE);
        ed = sha.edit();
        if (sha.getBoolean("is_S", false)) {
            mEd_login_username.setText(sha.getString("username", ""));
            mEd_login_password.setText(sha.getString("password", ""));
            mCh_save.setChecked(true);
        } else {
            mCh_save.setChecked(false);
        }


    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBt_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.call_developer),
                        Toast.LENGTH_SHORT).show();
            }
        });
        mBt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog = ProgressDialog.show(
                        Login.this,
                        getResources().getString(R.string.Tip),
                        getResources().getString(R.string.loading));
                mProgressDialog.setCanceledOnTouchOutside(true);

                final String username = mEd_login_username.getText().toString();
                final String password = mEd_login_password.getText().toString();
                if (mCh_save.isChecked()) {
                    ed.putString("username", username);
                    ed.putString("password", password);
                    ed.putBoolean("is_S", true);

                } else {
                    ed.putBoolean("is_S", false);
                }
                ed.commit();

                JMessageClient.login(username, password, new BasicCallback() {
                    @Override
                    public void gotResult(int responseCode, String LoginDesc) {
                        if (responseCode == 0) {
                            JMessageClient.getUserInfo(username, null,
                                    new GetUserInfoCallback() {
                                        @Override
                                        public void gotResult(int i, String msg,
                                                              UserInfo userInfo) {
                                            if (i == 0) {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),
                                                        getResources().getString(R.string.login_success),
                                                        Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent();
                                                intent.setClass(getApplicationContext(),
                                                        MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            } else {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),
                                                        getResources().getString(R.string.loading_fail)
                                                                + msg, Toast.LENGTH_LONG).show();
                                                JMessageClient.logout();
                                            }

                                        }

                                    });


                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.login_fail)
                                            + ":" + LoginDesc,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mBt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mEd_register_username.getText().toString();
                final String password = mEd_register_password.getText().toString();
                final String password2 = mEd_register_password2.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Login.this,
                            getResources().getString(R.string.username_cannot_be_null),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(Login.this,
                            getResources().getString(R.string.password_too_short),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password2.equals(password)) {
                    Toast.makeText(Login.this,
                            getResources().getString(R.string.password_not_match),
                            Toast.LENGTH_SHORT).show();
                    mEd_register_password2.setText("");
                    return;
                }
                mProgressDialog = ProgressDialog.show(Login.this,
                        getResources().getString(R.string.Tip),
                        getResources().getString(R.string.loading));

                JMessageClient.register(username, password,
                        new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode,
                                                  String registerDesc) {
                                if (responseCode == 0) {
                                    if (mProgressDialog != null &&
                                            mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                    mEd_login_username.setText(username);
                                    mEd_login_password.setText(password);
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.register_success),
                                            Toast.LENGTH_LONG).show();
                                    mViewPager.setCurrentItem(0);
                                } else {
                                    if (mProgressDialog != null &&
                                            mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.register_fail)
                                                    + ":" + registerDesc,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
}
