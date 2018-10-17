package com.wzm.tasking.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.fragment.F_2;
import com.wzm.tasking.tools.ThemeManager;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;


/**
 *
 * @desc :同意或拒绝好友申请?
 */
public class ShowFriendReason extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mTv_showAddFriendInfo;
    private Button mAccept_invitation;
    private Button mDeclined_invitation;
    private EditText mEt_reason;
    private Intent intent;

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
        setContentView(R.layout.activity_friend_reason);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_friend_reason);
        mTv_showAddFriendInfo = (TextView) findViewById(R.id.tv_show_add_friend_info);
        mAccept_invitation = (Button) findViewById(R.id.accept_invitation);
        mDeclined_invitation = (Button) findViewById(R.id.declined_invitation);
        mEt_reason = (EditText) findViewById(R.id.et_reason);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        intent = getIntent();
        if (intent.getFlags() == 1) {
            mTv_showAddFriendInfo.append(intent
                    .getStringExtra("invite_received"));
        } else if (intent.getFlags() == 2) {
            mEt_reason.setVisibility(View.GONE);
            mAccept_invitation.setVisibility(View.GONE);
            mDeclined_invitation.setVisibility(View.GONE);
            mTv_showAddFriendInfo.append(intent
                    .getStringExtra("invite_accepted"));
            JMessageClient.getUserInfo(intent.getStringExtra("username"), new GetUserInfoCallback() {
                @Override
                public void gotResult(int i, String s, UserInfo userInfo) {
                    if (i == 0) {
                        F_2.mData_f.add(userInfo);
                    }
                }
            });
        } else if (intent.getFlags() == 3) {
            mEt_reason.setVisibility(View.GONE);
            mAccept_invitation.setVisibility(View.GONE);
            mDeclined_invitation.setVisibility(View.GONE);
            mTv_showAddFriendInfo.append(intent
                    .getStringExtra("invite_declined"));
        } else if (intent.getFlags() == 4) {
            mEt_reason.setVisibility(View.GONE);
            mAccept_invitation.setVisibility(View.GONE);
            mDeclined_invitation.setVisibility(View.GONE);
            mTv_showAddFriendInfo.append(intent
                    .getStringExtra("contact_deleted"));
            final String username = intent.getStringExtra("username");
            for(UserInfo userInfo: F_2.mData_f){
                if(userInfo.getUserName().equals(username)){
                    F_2.mData_f.remove(userInfo);
                }
            }


        }

    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 同意好友添加
        mAccept_invitation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = intent.getStringExtra("username");
                ContactManager.acceptInvitation(username, null,
                        new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
                                        @Override
                                        public void gotResult(int i, String s, UserInfo userInfo) {
                                            if (i == 0) {
                                                F_2.mData_f.add(userInfo);
                                            }
                                        }
                                    });
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.add_success),
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.add_fail),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // 拒绝好友添加
        mDeclined_invitation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = mEt_reason.getText().toString();
                ContactManager.declineInvitation(
                        intent.getStringExtra("username"), null, reason,
                        new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.declined_success),
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.delete_fail),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
