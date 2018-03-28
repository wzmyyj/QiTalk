package com.wzm.tasking.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.adapter.MyPagerAdapter;
import com.wzm.tasking.adapter.UGAdapter;
import com.wzm.tasking.fragment.F_2;
import com.wzm.tasking.tools.FriendComparator;
import com.wzm.tasking.tools.GroupComparator;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetNoDisurbListCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static com.wzm.tasking.fragment.F_2.mData_g;

public class DisturbList extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ListView mList_1;
    private ListView mList_2;
    private SwipeRefreshLayout mSRL_1;
    private SwipeRefreshLayout mSRL_2;
    private UGAdapter mAdapter1;
    private UGAdapter mAdapter2;
    private List<UserInfo> mData1;
    private List<GroupInfo> mData2;

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
        setContentView(R.layout.activity_disturb_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_disturb_list);
        mTabLayout = (TabLayout) findViewById(R.id.tab_disturb_list);
        mViewPager = (ViewPager) findViewById(R.id.vp_disturb_list);
        LayoutInflater inflater = this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.activity_list, null);
        View view2 = inflater.inflate(R.layout.activity_list, null);
        List<View> viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        String[] titles = new String[]{getResources().getString(R.string.user),
                getResources().getString(R.string.group)};
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(viewList, titles);
        mViewPager.setAdapter(pagerAdapter);
        mList_1 = (ListView) view1.findViewById(R.id.lv_list);
        mList_2 = (ListView) view2.findViewById(R.id.lv_list);
        mSRL_1 = (SwipeRefreshLayout) view1.findViewById(R.id.srl_list);
        mSRL_2 = (SwipeRefreshLayout) view2.findViewById(R.id.srl_list);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        mTabLayout.setupWithViewPager(mViewPager);

        mSRL_1.setColorSchemeColors(getResources().
                getColor(ThemeManager.getColor(MainActivity.th)));
        mSRL_2.setColorSchemeColors(getResources().
                getColor(ThemeManager.getColor(MainActivity.th)));

        mData1 = new ArrayList<>();
        mAdapter1 = new UGAdapter(this, mData1, null);
        mList_1.setAdapter(mAdapter1);
        this.registerForContextMenu(mList_1);

        mData2 = new ArrayList<>();
        mAdapter2 = new UGAdapter(this, null, mData2);
        mList_2.setAdapter(mAdapter2);
        this.registerForContextMenu(mList_2);

        getNoDisturbList();
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mList_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                String username = mData1.get(position).getUserName();
                i.putExtra("u", username);
                i.setClass(DisturbList.this, GetUserInfo.class);
                startActivity(i);
                finish();
            }
        });
        mList_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                long ID = mData2.get(position).getGroupID();
                i.putExtra("id", ID);
                i.setClass(DisturbList.this, GetGroupInfo.class);
                startActivity(i);
                finish();
            }
        });
        mSRL_1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSRL_1.setRefreshing(true);
                getNoDisturbList();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getResources().
                                        getString(R.string.update_data_success),
                                Toast.LENGTH_SHORT).show();
                        mSRL_1.setRefreshing(false);
                    }
                }, 500);
            }
        });
        mSRL_2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSRL_2.setRefreshing(true);
                getNoDisturbList();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getResources().
                                        getString(R.string.update_data_success),
                                Toast.LENGTH_SHORT).show();
                        mSRL_2.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, 1, 1, getResources().getString(R.string.out_disturb));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final int p = menuInfo.position;
        switch (mTabLayout.getSelectedTabPosition()) {
            case 0:
                del_user(p);
                break;
            case 1:
                del_group(p);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void getNoDisturbList() {
        new Thread() {
            @Override
            public void run() {
                mData1.clear();
                mData2.clear();
                JMessageClient.getNoDisturblist(new GetNoDisurbListCallback() {
                    @Override
                    public void gotResult(int i, String s, List<UserInfo> list, List<GroupInfo> list1) {
                        if (i == 0) {
                            if (list != null) {
                                Comparator comp = new FriendComparator();
                                Collections.sort(list, comp);
                                for (UserInfo userInfo : list) {
                                    mData1.add(userInfo);
                                }
                                mAdapter1.notifyDataSetChanged();
                            }
                            if (list1 != null) {
                                Comparator comp = new GroupComparator();
                                Collections.sort(list1, comp);
                                for (GroupInfo groupInfo : list1) {
                                    mData2.add(groupInfo);
                                }
                                mAdapter2.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.loading_fail),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }.start();
    }

    private void del_user(final int p) {
        try {
            UserInfo Info = mData1.get(p);
            if (Info.isFriend()) {
                for (UserInfo userInfo : F_2.mData_f) {
                    if (userInfo.getUserName().equals(mData1.get(p).getUserName())) {
                        Info = userInfo;
                        break;
                    }
                }
            }
            Info.setNoDisturb(0, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        mData1.remove(p);
                        mAdapter1.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.set_success),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.set_fail),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void del_group(final int p) {
        try {
            GroupInfo Info = mData2.get(p);
            for (GroupInfo groupInfo : mData_g) {
                if (groupInfo.getGroupID() == mData2.get(p).getGroupID()) {
                    Info = groupInfo;
                    break;
                }
            }
            Info.setNoDisturb(0, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        mData2.remove(p);
                        mAdapter2.notifyDataSetChanged();
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


        } catch (Exception e) {
        }

    }
}
