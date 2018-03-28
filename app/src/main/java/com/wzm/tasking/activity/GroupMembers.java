package com.wzm.tasking.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.adapter.UGAdapter;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class GroupMembers extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView mList;
    private UGAdapter mAdapter;
    private List<UserInfo> mData;

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
        setContentView(R.layout.activity_group_members);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_group_members);
        mList = (ListView) findViewById(R.id.lv_group_members);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        mData = new ArrayList<>();
        mData = GetGroupInfo.Info.getGroupMembers();
        mAdapter = new UGAdapter(this, mData, null);
        mList.setAdapter(mAdapter);
        this.registerForContextMenu(mList);
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
                Intent i = new Intent();
                String username = mData.get(position).getUserName();
                if (JMessageClient.getMyInfo().getUserName().equals(username)) {
                    i.setClass(GroupMembers.this, MyInfo.class);
                } else {
                    i.putExtra("u", username);
                    i.setClass(GroupMembers.this, GetUserInfo.class);
                }
                startActivity(i);
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, 1, 1, getResources().getString(R.string.delete_this_user));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        if (!GetGroupInfo.Info.getGroupOwner().equals(JMessageClient.getMyInfo().getUserName())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.you_are_not_group_owner),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        final int p = menuInfo.position;
        if (JMessageClient.getMyInfo().getUserName().equals(mData.get(p).getUserName())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.cannot_delete_yourself),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        List<String> mNames = new ArrayList<String>();
        mNames.add(mData.get(p).getUserName());
        try {
            final long ID = GetGroupInfo.Info.getGroupID();
            JMessageClient.removeGroupMembers(ID, null, mNames,
                    new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.delete_success),
                                        Toast.LENGTH_SHORT).show();
                                mData.remove(p);
                                mAdapter.notifyDataSetChanged();
                                JMessageClient.getGroupInfo(ID, new GetGroupInfoCallback() {
                                    @Override
                                    public void gotResult(int i, String s, GroupInfo groupInfo) {
                                        GetGroupInfo.Info = groupInfo;
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.delete_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
        }


        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
