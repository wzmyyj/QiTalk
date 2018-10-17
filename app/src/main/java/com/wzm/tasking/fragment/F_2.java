package com.wzm.tasking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.activity.GetGroupInfo;
import com.wzm.tasking.activity.GetUserInfo;
import com.wzm.tasking.activity.OtherChat;
import com.wzm.tasking.activity.SingleChat;
import com.wzm.tasking.adapter.GroupInfoAdapter;
import com.wzm.tasking.adapter.UserInfoAdapter;
import com.wzm.tasking.tools.FriendComparator;
import com.wzm.tasking.tools.GroupComparator;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.ContactManager;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * Created by yyj on 2017/3/21 0021.
 */

public class F_2 extends Fragment {
    private SwipeRefreshLayout mSRL_1;
    private SwipeRefreshLayout mSRL_2;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_3;
    private ListView mList_g;
    private ListView mList_f;
    private ListView mList_r;
    private GroupInfoAdapter mAdapter_g;
    private UserInfoAdapter mAdapter_f;
    private SimpleAdapter mAdapter_r;
    public static List<UserInfo> mData_f;
    public static List<GroupInfo> mData_g;
    private List<Map<String, Object>> mData_r;
    private int s = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        mSRL_1 = (SwipeRefreshLayout) view.findViewById(R.id.srl_fg2_g);
        mSRL_2 = (SwipeRefreshLayout) view.findViewById(R.id.srl_fg2_f);
        mList_g = (ListView) view.findViewById(R.id.lv_fg2_g);
        mList_f = (ListView) view.findViewById(R.id.lv_fg2_f);
        mList_r = (ListView) view.findViewById(R.id.lv_fg2_r);
        ll_1 = (LinearLayout) view.findViewById(R.id.ll_fg2_1);
        ll_2 = (LinearLayout) view.findViewById(R.id.ll_fg2_2);
        ll_3 = (LinearLayout) view.findViewById(R.id.ll_fg2_3);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter_g.notifyDataSetChanged();
        mAdapter_f.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initData() {
        showView(0);
        mSRL_1.setColorSchemeColors(getActivity().getResources().
                getColor(ThemeManager.getColor(MainActivity.th)));
        mSRL_2.setColorSchemeColors(getActivity().getResources().
                getColor(ThemeManager.getColor(MainActivity.th)));

        mData_g = new ArrayList<>();
        mAdapter_g = new GroupInfoAdapter(getActivity(), mData_g);
        mList_g.setAdapter(mAdapter_g);

        mData_f = new ArrayList<>();
        mAdapter_f = new UserInfoAdapter(getActivity(), mData_f);
        mList_f.setAdapter(mAdapter_f);
        try {
            getFriendList();
            getGroupList();
        } catch (Exception e) {

        }
        mData_r = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pic", R.drawable.ic_blue);
        map.put("name", "Ashe");
        mData_r.add(map);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("pic", R.drawable.ic_header);
        map1.put("name", "Developer");
        mData_r.add(map1);
        mAdapter_r = new SimpleAdapter(getActivity(), mData_r,
                R.layout.fragment_2_item_friend, new String[]{"pic", "name",},
                new int[]{R.id.img_user_list_item, R.id.tv_user_list_item});
        mList_r.setAdapter(mAdapter_r);

    }

    private void initListener() {
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s == 1) {
                    showView(0);
                    return;
                }
                showView(1);
                if (mData_g.size() == 0) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.no_group),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s == 2) {
                    showView(0);
                    return;
                }
                showView(2);
                if (mData_f.size() == 0) {
                    Toast.makeText(getActivity(),
                            getResources().getString(R.string.no_friend),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s == 3) {
                    showView(0);
                    return;
                }
                showView(3);
            }
        });
        mList_g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("id", mData_g.get(position).getGroupID());
                i.setClass(getActivity(), GetGroupInfo.class);
                startActivity(i);
            }
        });
        mList_f.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("u", mData_f.get(position).getUserName());
                i.setClass(getActivity(), GetUserInfo.class);
                startActivity(i);
            }
        });
        mList_r.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent();
                    i.setClass(getActivity(), OtherChat.class);
                    startActivity(i);
                } else {
                    if (JMessageClient.getMyInfo().getUserName().equals("11111")) {
                        Toast.makeText(getActivity(), "You Are Developer.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent();
                        i.putExtra("u", "11111");
                        i.putExtra("n", "Developer");
                        i.setClass(getActivity(), SingleChat.class);
                        startActivity(i);
                    }
                }
            }
        });
        mSRL_1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroupList();
                mSRL_1.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getResources().
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
                getFriendList();
                mSRL_2.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getResources().
                                        getString(R.string.update_data_success),
                                Toast.LENGTH_SHORT).show();
                        mSRL_2.setRefreshing(false);
                    }
                }, 500);
            }
        });


    }


    private void getGroupList() {
        new Thread() {
            @Override
            public void run() {
                mData_g.clear();
                JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
                    @Override
                    public void gotResult(int i, String s, List<Long> list) {
                        if (i == 0) {
                            if (list == null) return;
                            final int h = list.size();
                            for (Long l : list) {
                                JMessageClient.getGroupInfo(l,
                                        new GetGroupInfoCallback() {

                                            @Override
                                            public void gotResult(int i,
                                                                  String s, GroupInfo groupInfo) {
                                                mData_g.add(groupInfo);
                                                if (mData_g.size() == h) {
                                                    Comparator comp = new GroupComparator();
                                                    Collections.sort(mData_g, comp);
                                                    mAdapter_g.notifyDataSetChanged();
                                                }

                                            }
                                        });

                            }
                        } else {
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.loading_fail),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }.start();


    }

    private void getFriendList() {
        new Thread() {
            @Override
            public void run() {
                mData_f.clear();
                ContactManager.getFriendList(new GetUserInfoListCallback() {
                    @Override
                    public void gotResult(int i, String s, List<UserInfo> list) {
                        if (i == 0) {
                            if (list == null) return;
                            Comparator comp = new FriendComparator();
                            Collections.sort(list, comp);
                            for (UserInfo userInfo : list) {
                                mData_f.add(userInfo);
                            }
                            mAdapter_f.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.loading_fail),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }.start();

    }

    private void showView(int i) {
        s = i;
        switch (s) {
            case 0:
                mSRL_1.setVisibility(View.GONE);
                mSRL_2.setVisibility(View.GONE);
                mList_r.setVisibility(View.GONE);
                break;
            case 1:
                mSRL_1.setVisibility(View.VISIBLE);
                mSRL_2.setVisibility(View.GONE);
                mList_r.setVisibility(View.GONE);
                break;
            case 2:
                mSRL_1.setVisibility(View.GONE);
                mSRL_2.setVisibility(View.VISIBLE);
                mList_r.setVisibility(View.GONE);
                break;
            case 3:
                mSRL_1.setVisibility(View.GONE);
                mSRL_2.setVisibility(View.GONE);
                mList_r.setVisibility(View.VISIBLE);
                break;
        }
    }


}


