package com.wzm.tasking.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.wzm.tasking.view.GroupView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.api.BasicCallback;

import static com.wzm.tasking.R.id.bt_group_info;

public class GetGroupInfo extends AppCompatActivity {
    private Toolbar mToolbar;
    private GroupView mImageView;
    public TextView tv_1;
    public TextView tv_2;
    private CheckBox cb_1;
    private CheckBox cb_2;
    private Button bt_add;
    private Button mButton;
    private long ID;
    public static GroupInfo Info;
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
        setContentView(R.layout.activity_group_info);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_group_info);
        mImageView = (GroupView) findViewById(R.id.img_group_info);
        tv_1 = (TextView) findViewById(R.id.tv_group_info_1);
        tv_2 = (TextView) findViewById(R.id.tv_group_info_2);
        cb_1 = (CheckBox) findViewById(R.id.cb_group_info_1);
        cb_2 = (CheckBox) findViewById(R.id.cb_group_info_2);
        bt_add = (Button) findViewById(R.id.bt_group_info_add);
        mButton = (Button) findViewById(bt_group_info);

    }

    private void initData() {
        setSupportActionBar(mToolbar);
        Intent i = getIntent();
        ID = i.getLongExtra("id", 0);
        b = i.getIntExtra("b", 0);
        for (GroupInfo groupInfo : F_2.mData_g) {
            if (groupInfo.getGroupID() == ID) {
                Info = groupInfo;
                setInfo(Info);
                return;
            }
        }
    }


    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Info == null) {
            return;
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b == 1) {
                    finish();
                    return;
                }
                Intent i = new Intent();
                i.putExtra("id", ID);
                i.putExtra("n", Info.getGroupName());
                i.setClass(GetGroupInfo.this, GroupChat.class);
                startActivity(i);
                finish();
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(GetGroupInfo.this, AddGroupMembers.class);
                startActivity(i);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(GetGroupInfo.this, GroupMembers.class);
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
                    set_block(0);
                } else {
                    set_block(1);
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
        getMenuInflater().inflate(R.menu.group_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Info == null) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_update:
                Intent i = new Intent();
                i.putExtra("id", ID);
                i.setClass(GetGroupInfo.this, UpdateGroup.class);
                startActivity(i);
                return true;
            case R.id.action_exit_group:
                showChoosePicDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Info = null;
        finish();
    }

    private void setInfo(GroupInfo groupInfo) {
        tv_1.setText(groupInfo.getGroupName());
        tv_2.setText(groupInfo.getGroupDescription());
        if (groupInfo.getNoDisturb() > 0) {
            cb_1.setChecked(true);
        } else {
            cb_1.setChecked(false);
        }
        if (groupInfo.isGroupBlocked() > 0) {
            cb_2.setChecked(true);
        } else {
            cb_2.setChecked(false);
        }
        final List<Bitmap> bitmapList1 = new ArrayList<Bitmap>();
        final int m = groupInfo.getGroupMembers().size();
        final Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_header);
        for (int i = 0; i < m && i < 5; i++) {
            bitmapList1.add(null);
        }
        for (int i = 0; i < m && i < 5; i++) {
            final int p = i;
            groupInfo.getGroupMembers().get(p).getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int i, String s, Bitmap bitmap) {
                    if (bitmap != null) {
                        bitmapList1.set(p, bitmap);
                    } else {
                        bitmapList1.set(p, mBitmap);
                    }
                    try {
                        mImageView.setImageBitmaps(bitmapList1);
                    } catch (Exception e) {

                    }
                }
            });
        }
    }

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.Tip));
        builder.setMessage(getResources().getString(R.string.group) +
                ": " + Info.getGroupName() + "   " +
                getResources().getString(R.string.exit_group));
        builder.setPositiveButton(getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit_group();
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.create().show();
    }

    private void exit_group() {
        JMessageClient.exitGroup(ID, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.exit_group_success),
                            Toast.LENGTH_SHORT).show();
                    JMessageClient.deleteGroupConversation(ID);
                    F_2.mData_g.remove(Info);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.exit_group_fail),
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

    private void set_block(final int l) {
        Info.setBlockGroupMessage(l, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    if (l > 0) {
                        cb_2.setChecked(true);
                    } else {
                        cb_2.setChecked(false);
                    }
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.set_success),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (l > 0) {
                        cb_2.setChecked(false);
                    } else {
                        cb_2.setChecked(true);
                    }
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.set_fail),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
