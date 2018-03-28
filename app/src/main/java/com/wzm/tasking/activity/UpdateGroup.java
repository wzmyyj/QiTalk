package com.wzm.tasking.activity;

import android.app.ProgressDialog;
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
import com.wzm.tasking.tools.ThemeManager;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.api.BasicCallback;


/**
 * @desc :修改群组信息
 */
public class UpdateGroup extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mEt_groupName;
    private Button mBt_updateGroupName;
    private EditText mEt_groupDesc;
    private Button mBt_updateGroupDesc;
    private ProgressDialog mProgressDialog = null;
    private long ID;

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
        setContentView(R.layout.activity_update_group);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_update_group);
        mEt_groupName = (EditText) findViewById(R.id.et_group_name);
        mBt_updateGroupName = (Button) findViewById(R.id.bt_update_group_name);
        mEt_groupDesc = (EditText) findViewById(R.id.et_group_desc);
        mBt_updateGroupDesc = (Button) findViewById(R.id.bt_update_group_desc);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        Intent i = getIntent();
        ID = i.getLongExtra("id", 0);
        JMessageClient.getGroupInfo(ID, new GetGroupInfoCallback() {
            @Override
            public void gotResult(int i, String arg1, GroupInfo groupinfo) {
                if (i == 0) {
                    mEt_groupName.setText(groupinfo.getGroupName());
                    mEt_groupDesc.setText(groupinfo.getGroupDescription());
                }
            }
        });
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBt_updateGroupName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = mEt_groupName.getText().toString();

                mProgressDialog = ProgressDialog.show(UpdateGroup.this,
                        getResources().getString(R.string.Tip),
                        getResources().getString(R.string.loading));
                mProgressDialog.setCanceledOnTouchOutside(true);

                JMessageClient.updateGroupName(ID, name, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.update_success),
                                    Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.update_fail),
                                    Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });

            }
        });

        mBt_updateGroupDesc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog = ProgressDialog.show(UpdateGroup.this,
                        getResources().getString(R.string.Tip),
                        getResources().getString(R.string.loading));
                mProgressDialog.setCanceledOnTouchOutside(true);

                final String desc = mEt_groupDesc.getText().toString();
                JMessageClient.updateGroupDescription(ID, desc,
                        new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.update_success),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    mProgressDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getResources().getString(R.string.update_fail),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    mProgressDialog.dismiss();

                                }
                            }
                        });

            }
        });
    }

}
