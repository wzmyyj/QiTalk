package com.wzm.tasking.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.fragment.F_2;
import com.wzm.tasking.tools.ThemeManager;
import com.wzm.tasking.view.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static com.wzm.tasking.R.id.bt_user_info;

public class GetUserInfo extends AppCompatActivity {
    private Toolbar mToolbar;
    private CircleImageView img_header;
    private TextView tv_0;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private CheckBox cb_1;
    private CheckBox cb_2;
    private Button mButton;
    private String username, name;
    public static UserInfo Info;
    private ProgressDialog mProgressDialog;
    private int b;

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
        setContentView(R.layout.activity_user_info);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_user_info);
        img_header = (CircleImageView) findViewById(R.id.img_user_info);
        tv_0 = (TextView) findViewById(R.id.tv_user_info_0);
        tv_1 = (TextView) findViewById(R.id.tv_user_info_1);
        tv_2 = (TextView) findViewById(R.id.tv_user_info_2);
        tv_3 = (TextView) findViewById(R.id.tv_user_info_3);
        tv_4 = (TextView) findViewById(R.id.tv_user_info_4);
        tv_5 = (TextView) findViewById(R.id.tv_user_info_5);
        tv_6 = (TextView) findViewById(R.id.tv_user_info_6);
        cb_1 = (CheckBox) findViewById(R.id.cb_user_info_1);
        cb_2 = (CheckBox) findViewById(R.id.cb_user_info_2);
        mButton = (Button) findViewById(bt_user_info);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        Intent i = getIntent();
        username = i.getStringExtra("u");
        b = i.getIntExtra("b", 0);

        for (UserInfo userInfo : F_2.mData_f) {
            if (userInfo.getUserName().equals(username)) {
                Info = userInfo;
                setInfo(Info);
                return;
            }
        }

        mProgressDialog = ProgressDialog.show(GetUserInfo.this,
                getResources().getString(R.string.Tip),
                getResources().getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(true);
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (i == 0) {
                    Info = userInfo;
                    setInfo(Info);
                    mProgressDialog.dismiss();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.loading_fail),
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setInfo(Info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!Info.isFriend()) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.he_is_not_your_friend),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_update_note:
                Intent i = new Intent();
                i.setClass(GetUserInfo.this, UpdateNote.class);
                startActivity(i);
                return true;
            case R.id.action_delete_friend:
                showChoosePicDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                if (Info.isFriend()) {
                    if (b == 1) {
                        finish();
                        return;
                    }
                    i.putExtra("u", username);
                    i.putExtra("n", name);
                    i.setClass(GetUserInfo.this, SingleChat.class);
                    startActivity(i);
                    finish();
                } else {
                    i.putExtra("u", username);
                    i.setClass(GetUserInfo.this, AddFriend.class);
                    startActivity(i);
                }
            }
        });
        img_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(GetUserInfo.this, SaveUserAvatar.class);
                startActivity(i);
            }
        });
        cb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb_1.isChecked()) {
                    set_noDisturb(0);
                } else {
                    set_noDisturb(1);
                }
            }
        });
        cb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cb_2.isChecked()) {
                    set_blacklist(0);
                } else {
                    set_blacklist(1);
                }
            }
        });
    }

    private void setInfo(UserInfo userInfo) {
        String gender, birthday = "";
        if (userInfo.getGender() == cn.jpush.im.android.api.model.UserInfo.Gender.male) {
            gender = getResources().getString(R.string.male);
        } else if (userInfo.getGender() == cn.jpush.im.android.api.model.UserInfo.Gender.female) {
            gender = getResources().getString(R.string.female);
        } else {
            gender = getResources().getString(R.string.unknown);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd");
        if (userInfo.getBirthday() > 0) {
            birthday = sdf.format(new Date(userInfo.getBirthday()));
        }
        tv_0.setText(userInfo.getNotename());
        tv_1.setText(userInfo.getUserName());
        tv_2.setText(userInfo.getNickname());
        tv_3.setText(userInfo.getSignature());
        tv_4.setText(gender);
        tv_5.setText(birthday);
        tv_6.setText(userInfo.getRegion());
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap != null) {
                    img_header.setImageBitmap(bitmap);
                } else {
                    img_header.setImageResource(R.drawable.ic_header);
                }
            }
        });
        if (userInfo.isFriend()) {
            mButton.setText(R.string.into_chat);
        } else {
            mButton.setText(R.string.add_friend);

        }
        if (!TextUtils.isEmpty(userInfo.getNotename())) {
            name = userInfo.getNotename();
        } else if (!TextUtils.isEmpty(userInfo.getNickname())) {
            name = userInfo.getNickname();
        } else {
            name = userInfo.getUserName();
        }
        if (userInfo.getNoDisturb() > 0) {
            cb_1.setChecked(true);
        } else {
            cb_1.setChecked(false);
        }
        if (!TextUtils.isEmpty(userInfo.getNoteText())) {
            if (userInfo.getNoteText().equals("1")) {
                cb_2.setChecked(true);
            } else {
                cb_2.setChecked(false);
            }
        } else {
            if (userInfo.getBlacklist() > 0) {
                cb_2.setChecked(true);
            } else {
                cb_2.setChecked(false);
            }
        }
    }

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Tip));
        builder.setMessage(getResources().getString(R.string.friend) +
                ": " + Info.getUserName() + "   " +
                getResources().getString(R.string.delete_friend));
        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        del_friend();
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.create().show();
    }

    private void del_friend() {
        Info.removeFromFriendList(new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    F_2.mData_f.remove(Info);
                    mButton.setText(R.string.add_friend);
                    tv_0.setText(null);
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.delete_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.delete_fail),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void set_noDisturb(final int d) {
        Info.setNoDisturb(d, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    if (d > 0) {
                        cb_1.setChecked(true);
                    } else {
                        cb_1.setChecked(false);
                    }
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.set_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (d > 0) {
                        cb_1.setChecked(false);
                    } else {
                        cb_1.setChecked(true);
                    }
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.set_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void set_blacklist(int l) {
        List<String> mList = new ArrayList<String>();
        mList.add(username);
        if (l == 0) {
            JMessageClient.delUsersFromBlacklist(mList, null,
                    new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                cb_2.setChecked(false);
                                Info.setNoteText("0");
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.set_success),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                cb_2.setChecked(true);
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.set_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            JMessageClient.addUsersToBlacklist(mList, null,
                    new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                cb_2.setChecked(true);
                                Info.setNoteText("1");
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.set_success),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                cb_2.setChecked(false);
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.set_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }

}
