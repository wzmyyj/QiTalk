package com.wzm.tasking.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.adapter.ChatMessageAdapter;
import com.wzm.tasking.bean.ChatMessage;
import com.wzm.tasking.tools.HttpTuring;
import com.wzm.tasking.tools.ThemeManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;


@SuppressLint("HandlerLeak")
public class OtherChat extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView mList;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mData;
    private EditText mEt_input;
    private Button mBt_send;
    private UserInfo myInfo;
    private Bitmap bitmap,myAvatar;
    private String name="Ashe",name1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            fromMessage.setBitmap(bitmap);
            fromMessage.setName(name);
            mData.add(fromMessage);
            mAdapter.notifyDataSetChanged();
            mList.setSelection(mAdapter.getCount());
        }

        ;
    };

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
        setContentView(com.wzm.tasking.R.layout.chat_activity);
        mToolbar = (Toolbar) findViewById(com.wzm.tasking.R.id.toolbar_chat);
        mList = (ListView) findViewById(com.wzm.tasking.R.id.lv_chat);
        mEt_input = (EditText) findViewById(com.wzm.tasking.R.id.et_chat_input);
        mBt_send = (Button) findViewById(com.wzm.tasking.R.id.bt_chat_send);
    }

    private void initData() {
        mToolbar.setTitle(name);
        setSupportActionBar(mToolbar);

        myAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.ic_header);
        myInfo = JMessageClient.getMyInfo();
        if (!TextUtils.isEmpty(myInfo.getNickname())) {
            name1=myInfo.getNickname();
        } else {
            name=myInfo.getUserName();
        }
        myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap != null) {
                    myAvatar=bitmap;
                } else {
                    myAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.ic_header);
                }
            }
        });
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_blue);
        Intent i = getIntent();
        String msg = i.getStringExtra("msg");

        mData = new ArrayList<ChatMessage>();
        mAdapter = new ChatMessageAdapter(this, mData);
        mList.setAdapter(mAdapter);

        if (msg != null) {
            mData.add(new ChatMessage(bitmap, name, new Date(), msg, ChatMessage.Type.IN_COMING));
            mAdapter.notifyDataSetChanged();
        }else{
            mData.add(new ChatMessage(bitmap, name, new Date(), "您好！很高兴为您服务。", ChatMessage.Type.IN_COMING));
            mAdapter.notifyDataSetChanged();
        }
    }
    private void initListener(){
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg = mEt_input.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    return;
                }
                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUT_COMING);
                toMessage.setName(name1);
                toMessage.setBitmap(myAvatar);
                mData.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mEt_input.setText("");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mList.setSelection(mAdapter.getCount());
                    }
                }, 100);

                new Thread() {
                    public void run() {
                        ChatMessage fromMessage = HttpTuring.sendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    }

                    ;
                }.start();
            }
        });

        mEt_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                mList.setSelection(mAdapter.getCount());
                            }
                        }, 100);
                        break;
                }
                return false;
            }
        });

    }

}
