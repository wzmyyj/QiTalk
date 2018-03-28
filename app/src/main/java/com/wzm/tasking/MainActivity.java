package com.wzm.tasking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.activity.Blacklist;
import com.wzm.tasking.activity.CreateGroup;
import com.wzm.tasking.activity.DisturbList;
import com.wzm.tasking.activity.FindUser;
import com.wzm.tasking.activity.GroupChat;
import com.wzm.tasking.activity.Language;
import com.wzm.tasking.activity.Login;
import com.wzm.tasking.activity.MyInfo;
import com.wzm.tasking.activity.NotifyType;
import com.wzm.tasking.activity.ShowFriendReason;
import com.wzm.tasking.activity.ShowLogoutReason;
import com.wzm.tasking.activity.SingleChat;
import com.wzm.tasking.activity.Theme;
import com.wzm.tasking.activity.UpdateAvatar;
import com.wzm.tasking.activity.UpdatePassword;
import com.wzm.tasking.fragment.F_1;
import com.wzm.tasking.fragment.F_2;
import com.wzm.tasking.fragment.F_3;
import com.wzm.tasking.tools.ThemeManager;
import com.wzm.tasking.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.event.ContactNotifyEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    //wel
    public static boolean wel;
    //JPush
    public static final String CREATE_GROUP_CUSTOM_KEY = "create_group_custom_key";
    public static final String SET_DOWNLOAD_PROGRESS = "set_download_progress";
    public static final String IS_DOWNLOAD_PROGRESS_EXISTS = "is_download_progress_exists";
    public static final String CUSTOM_MESSAGE_STRING = "custom_message_string";
    public static final String CUSTOM_FROM_NAME = "custom_from_name";
    public static final String DOWNLOAD_INFO = "download_info";
    public static final String DOWNLOAD_ORIGIN_IMAGE = "download_origin_image";
    public static final String DOWNLOAD_THUMBNAIL_IMAGE = "download_thumbnail_image";
    public static final String IS_UPLOAD = "is_upload";
    public static final String LOGOUT_REASON = "logout_reason";
    //Theme
    private TextView tv;
    private boolean is_dark = false;
    public static int th;
    public static int a = 0;
    private int[] tab_1 = new int[]{R.drawable.tab_1_0, R.drawable.tab_1_1, R.drawable.tab_1_2,
            R.drawable.tab_1_3, R.drawable.tab_1_4, R.drawable.tab_1_5,
            R.drawable.tab_1_6, R.drawable.tab_1_7, R.drawable.tab_1_8};
    private int[] tab_2 = new int[]{R.drawable.tab_2_0, R.drawable.tab_2_1, R.drawable.tab_2_2,
            R.drawable.tab_2_3, R.drawable.tab_2_4, R.drawable.tab_2_5,
            R.drawable.tab_2_6, R.drawable.tab_2_7, R.drawable.tab_2_8};
    private int[] tab_3 = new int[]{R.drawable.tab_3_0, R.drawable.tab_3_1, R.drawable.tab_3_2,
            R.drawable.tab_3_3, R.drawable.tab_3_4, R.drawable.tab_3_5,
            R.drawable.tab_3_6, R.drawable.tab_3_7, R.drawable.tab_3_8};
    //nav
    private CircleImageView img_header;
    private TextView tv_username;
    private TextView tv_sign;
    private LinearLayout ll_night;
    private ImageView img_night;
    private TextView tv_night;
    private LinearLayout ll_end;
    //content
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mData;
    //tab
    private LinearLayout ll_tab_1;
    private LinearLayout ll_tab_2;
    private LinearLayout ll_tab_3;
    private ImageView img_tab_1;
    private ImageView img_tab_2;
    private ImageView img_tab_3;
    private TextView tv_tab_1;
    private TextView tv_tab_2;
    private TextView tv_tab_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wel = true;
        JMessageClient.registerEventReceiver(this);
        initTheme();
        initView();
        initData();
        initListener();
    }


    private void initTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        is_dark = ThemeManager.isDark(this);
        if (is_dark) {
            setTheme(ThemeManager.getTheme(7));
            WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            tv = new TextView(this);
            tv.setBackgroundColor(0xbb000000);
            manager.addView(tv, params);
            th = 7;
        } else {
            th = ThemeManager.getIndex(this);
            setTheme(ThemeManager.getTheme(th));
        }
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        //content
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mViewPager = (ViewPager) findViewById(R.id.vp_main);
        //tab
        ll_tab_1 = (LinearLayout) findViewById(R.id.ll_main_tab_1);
        ll_tab_2 = (LinearLayout) findViewById(R.id.ll_main_tab_2);
        ll_tab_3 = (LinearLayout) findViewById(R.id.ll_main_tab_3);
        img_tab_1 = (ImageView) findViewById(R.id.img_main_tab_1);
        img_tab_2 = (ImageView) findViewById(R.id.img_main_tab_2);
        img_tab_3 = (ImageView) findViewById(R.id.img_main_tab_3);
        tv_tab_1 = (TextView) findViewById(R.id.tv_main_tab_1);
        tv_tab_2 = (TextView) findViewById(R.id.tv_main_tab_2);
        tv_tab_3 = (TextView) findViewById(R.id.tv_main_tab_3);
        //nav
        View headerView = mNavigationView.getHeaderView(0);
        img_header = (CircleImageView) headerView.findViewById(R.id.img_main_head);
        tv_username = (TextView) headerView.findViewById(R.id.tv_main_username);
        tv_sign = (TextView) headerView.findViewById(R.id.tv_main_sign);
        img_night = (ImageView) findViewById(R.id.img_main_night);
        tv_night = (TextView) findViewById(R.id.tv_main_night);
        ll_night = (LinearLayout) findViewById(R.id.ll_main_night);
        ll_end = (LinearLayout) findViewById(R.id.ll_main_end);

    }

    private void initData() {
        setSupportActionBar(mToolbar);

        setInfo(JMessageClient.getMyInfo());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);


        if (is_dark) {
            img_night.setImageResource(R.drawable.sun);
            tv_night.setText(R.string.light_model);
        } else {
            img_night.setImageResource(R.drawable.night);
            tv_night.setText(R.string.night_model);
        }

        tv_tab_1.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
        img_tab_1.setBackgroundResource(tab_1[th]);

        mData = new ArrayList<Fragment>();
        mData.add(new F_1());
        mData.add(new F_2());
        mData.add(new F_3());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public Fragment getItem(int a) {
                return mData.get(a);
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    private void initListener() {
        //nav
        img_header.setOnClickListener(this);
        ll_night.setOnClickListener(this);
        ll_end.setOnClickListener(this);
        img_header.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, UpdateAvatar.class);
                startActivity(i);
                return true;
            }
        });
        //tab
        ll_tab_1.setOnClickListener(this);
        ll_tab_2.setOnClickListener(this);
        ll_tab_3.setOnClickListener(this);
        //content
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                resetAll();
                switch (position) {
                    case 0:
                        tv_tab_1.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                        img_tab_1.setBackgroundResource(tab_1[th]);
                        mToolbar.setTitle(getResources().getString(R.string.message));
                        break;
                    case 1:
                        tv_tab_2.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                        img_tab_2.setBackgroundResource(tab_2[th]);
                        mToolbar.setTitle(getResources().getString(R.string.contacts));
                        break;
                    case 2:
                        tv_tab_3.setTextColor(getResources().getColor(ThemeManager.getColor(th)));
                        img_tab_3.setBackgroundResource(tab_3[th]);
                        mToolbar.setTitle(getResources().getString(R.string.discover));
                        break;
                }
            }

            private void resetAll() {
                tv_tab_1.setTextColor(Color.parseColor("#666666"));
                tv_tab_2.setTextColor(Color.parseColor("#666666"));
                tv_tab_3.setTextColor(Color.parseColor("#666666"));
                img_tab_1.setBackgroundResource(tab_1[8]);
                img_tab_2.setBackgroundResource(tab_2[8]);
                img_tab_3.setBackgroundResource(tab_3[8]);

            }

            @Override
            public void onPageScrolled(int position, float p_set, int p_px) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public void onEvent(NotificationClickEvent event) {

        Message msg = event.getMessage();
        Intent i = new Intent();
        switch (msg.getTargetType()) {
            case single:
                UserInfo userInfo = (UserInfo) msg.getTargetInfo();
                i.putExtra("u", userInfo.getUserName());
                if (!TextUtils.isEmpty(userInfo.getNotename())) {
                    i.putExtra("n", userInfo.getNotename());
                } else if (!TextUtils.isEmpty(userInfo.getNickname())) {
                    i.putExtra("n", userInfo.getNickname());
                } else {
                    i.putExtra("n", userInfo.getUserName());
                }
                i.setClass(getApplicationContext(), SingleChat.class);
                startActivity(i);
                break;
            case group:
                GroupInfo groupInfo = (GroupInfo) msg.getTargetInfo();
                i.putExtra("id", groupInfo.getGroupID());
                i.putExtra("n", groupInfo.getGroupName());
                i.setClass(getApplicationContext(), GroupChat.class);
                startActivity(i);
                break;
        }

    }

    public void onEvent(ContactNotifyEvent event) {
        String reason = event.getReason();
        String fromUsername = event.getFromUsername();
        Intent intent = new Intent(getApplicationContext(),
                ShowFriendReason.class);
        Log.d("h", "onEvent: " + event.getType());

        switch (event.getType()) {
            case invite_received:// 收到好友邀请
                intent.putExtra("invite_received", "fromUsername = " + fromUsername
                        + "\n" + "reason = " + reason);
                intent.putExtra("username", fromUsername);
                intent.setFlags(1);
                startActivity(intent);
                break;
            case invite_accepted:// 对方接收了你的好友邀请
                intent.putExtra("invite_accepted", fromUsername +
                        getResources().getString(R.string.invite_accepted));
                intent.putExtra("username", fromUsername);
                intent.setFlags(2);
                startActivity(intent);
                break;
            case invite_declined:// 对方拒绝了你的好友邀请
                intent.putExtra("invite_declined", fromUsername
                        + getResources().getString(R.string.invite_declined)
                        + "\n"
                        + getResources().getString(R.string.reason)
                        + event.getReason());
                intent.setFlags(3);
                startActivity(intent);
                break;
            case contact_deleted:// 对方将你从好友中删除
                JMessageClient.deleteSingleConversation(fromUsername);
                intent.putExtra("contact_deleted", fromUsername
                        + getResources().getString(R.string.contact_deleted));
                intent.putExtra("username", fromUsername);
                intent.setFlags(4);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void onEvent(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();
        UserInfo myInfo = event.getMyInfo();
        Intent intent = new Intent(getApplicationContext(),
                ShowLogoutReason.class);
        intent.putExtra(LOGOUT_REASON, "reason = " + reason + "\n"
                + "logout user name = " + myInfo.getUserName());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (a != 0) {
            if (is_dark) {
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                manager.removeView(tv);
            }
            recreate();
            a = 0;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setInfo(JMessageClient.getMyInfo());
    }

    private void setInfo(UserInfo userInfo){
        try {
            if (!TextUtils.isEmpty(userInfo.getNickname())) {
                tv_username.setText(userInfo.getNickname());
            } else {
                tv_username.setText(userInfo.getUserName());
            }
            if (!TextUtils.isEmpty(userInfo.getSignature())) {
                tv_sign.setText(userInfo.getSignature());
            } else {
                tv_sign.setText("......");
            }

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
        } catch (Exception e) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else {
                mDrawer.openDrawer(GravityCompat.START);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent();
        switch (item.getItemId()) {
            case R.id.action_find:
                i.setClass(MainActivity.this, FindUser.class);
                startActivity(i);
                return true;
            case R.id.action_chat:
                i.setClass(MainActivity.this, CreateGroup.class);
                startActivity(i);
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),
                        "No Settings", Toast.LENGTH_SHORT).show();

                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent i = new Intent();
        switch (item.getItemId()) {
            case R.id.nav_1:
                i.setClass(MainActivity.this, MyInfo.class);
                startActivity(i);
                break;
            case R.id.nav_2:
                i.setClass(MainActivity.this, NotifyType.class);
                startActivity(i);
                break;
            case R.id.nav_3:
                i.setClass(MainActivity.this, DisturbList.class);
                startActivity(i);
                break;
            case R.id.nav_4:
                i.setClass(MainActivity.this, Blacklist.class);
                startActivity(i);
                break;
            case R.id.nav_5:
                i.setClass(MainActivity.this, Theme.class);
                startActivity(i);
                break;
            case R.id.nav_6:
                i.setClass(MainActivity.this, Language.class);
                startActivity(i);
                break;
            case R.id.nav_7:
                i.setClass(MainActivity.this, UpdatePassword.class);
                startActivity(i);
                break;
            case R.id.nav_8:
                JMessageClient.logout();
                i.setClass(MainActivity.this, Login.class);
                startActivity(i);
                finish();
                break;
        }
//        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_main_head:
                Intent i = new Intent();
                i.setClass(MainActivity.this, MyInfo.class);
                startActivity(i);
                break;
            case R.id.ll_main_night:
                ThemeManager.changeDark(MainActivity.this);
                if (is_dark) {
                    WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    manager.removeView(tv);
                }
                recreate();
                break;
            case R.id.ll_main_end:
                finish();
                break;
            case R.id.ll_main_tab_1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.ll_main_tab_2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ll_main_tab_3:
                mViewPager.setCurrentItem(2);
                break;


        }
    }


}
