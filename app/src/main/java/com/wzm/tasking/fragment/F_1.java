package com.wzm.tasking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.activity.GroupChat;
import com.wzm.tasking.activity.SingleChat;
import com.wzm.tasking.adapter.ConversationAdapter;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * Created by yyj on 2017/3/21 0021.
 */

public class F_1 extends Fragment {

    private SwipeRefreshLayout mSRL;
    private ListView mList;
    private TextView mTextView;
    private List<Conversation> mData, mConversationList;
    private ConversationAdapter mAdapter;
    private Handler handler = new Handler();
    private MyRunnable myRunnable = new MyRunnable();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        mSRL = (SwipeRefreshLayout) view.findViewById(R.id.srl_fg1);
        mList = (ListView) view.findViewById(R.id.lv_fg1);
        mTextView = (TextView) view.findViewById(R.id.tv_fg1);
        this.registerForContextMenu(mList);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    private void initData() {
        mData = new ArrayList<>();
        mConversationList = new ArrayList<>();
        mAdapter = new ConversationAdapter(getActivity(), mData);
        mList.setAdapter(mAdapter);
        mSRL.setColorSchemeColors(getActivity().getResources().
                getColor(ThemeManager.getColor(MainActivity.th)));
    }

    private void initListener() {
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mData.get(position).resetUnreadCount();
                mAdapter.notifyDataSetChanged();
                Intent i = new Intent();
                switch (mData.get(position).getType()) {
                    case single:
                        UserInfo userInfo = (UserInfo) mData.get(position).getTargetInfo();
                        i.putExtra("u", userInfo.getUserName());
                        if (!TextUtils.isEmpty(userInfo.getNotename())) {
                            i.putExtra("n", userInfo.getNotename());
                        } else if (!TextUtils.isEmpty(userInfo.getNickname())) {
                            i.putExtra("n", userInfo.getNickname());
                        } else {
                            i.putExtra("n", userInfo.getUserName());
                        }
                        i.setClass(getActivity(), SingleChat.class);
                        startActivity(i);
                        break;
                    case group:
                        GroupInfo groupInfo = (GroupInfo) mData.get(position).getTargetInfo();
                        i.putExtra("id", groupInfo.getGroupID());
                        i.putExtra("n", groupInfo.getGroupName());
                        i.setClass(getActivity(), GroupChat.class);
                        startActivity(i);
                        break;
                }

            }
        });
        mSRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSRL.setRefreshing(true);
                try {
                    mData.clear();
                    mConversationList = JMessageClient.getConversationList();
                    for (Conversation conversation : mConversationList) {
                        mData.add(conversation);
                    }
                    mAdapter.notifyDataSetChanged();
                    if (mData.size() > 0) {
                        mTextView.setText("");
                    } else {
                        mTextView.setText(R.string.no_chat);
                    }
                } catch (Exception e) {

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getResources().
                                        getString(R.string.update_data_success),
                                Toast.LENGTH_SHORT).show();
                        mSRL.setRefreshing(false);
                    }
                }, 500);

            }
        });

    }

    public void onEvent(MessageEvent event) {
        handler.post(myRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        JMessageClient.registerEventReceiver(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.post(myRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, 0, 1, getResources().getString(R.string.change_isRead));
        menu.add(1, 1, 1, getResources().getString(R.string.delete_message));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int p = menuInfo.position;
        switch (item.getItemId()) {
            case 0:
                int un = mData.get(p).getUnReadMsgCnt();
                if (un > 0) {
                    mData.get(p).resetUnreadCount();
                    mAdapter.notifyDataSetChanged();
                } else {
                    mData.get(p).setUnReadMessageCnt(1);
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case 1:
                mConversationList.get(p).deleteAllMessage();
                switch (mConversationList.get(p).getType()) {
                    case single:
                        UserInfo userInfo = (UserInfo) mConversationList.get(p).getTargetInfo();
                        JMessageClient.deleteSingleConversation(userInfo.getUserName());
                        break;
                    case group:
                        GroupInfo groupInfo = (GroupInfo) mConversationList.get(p).getTargetInfo();
                        JMessageClient.deleteGroupConversation(groupInfo.getGroupID());
                        break;
                }
                mData.remove(p);
                mAdapter.notifyDataSetChanged();
                if (mData.size() == 0) {
                    mTextView.setText(R.string.no_chat);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }


    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            try {
                mData.clear();
                mConversationList = JMessageClient.getConversationList();
                for (Conversation conversation : mConversationList) {
                    mData.add(conversation);
                }
                mAdapter.notifyDataSetChanged();
                if (mData.size() > 0) {
                    mTextView.setText("");
                } else {
                    mTextView.setText(R.string.no_chat);
                }
            } catch (Exception e) {

            }

        }
    }
}


