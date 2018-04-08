package com.wzm.tasking.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.adapter.MessageAdapter;
import com.wzm.tasking.tools.GlideImageLoader;
import com.wzm.tasking.tools.ThemeManager;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import sj.qqkeyboard.DefQqEmoticons;


public class GroupChat extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView mList;
    private MessageAdapter mAdapter;
    private List<Message> mData;
    private EditText mEt_input;
    private Button mBt_send;
    private long ID;

    //exp
    private RelativeLayout mRL_1;
    private LinearLayout ll_exp;
    private GridView gv_exp;
    private List<String> key_exp;
    private List<Map<String, Object>> list_exp;
    private SimpleAdapter simple_exp;

    //image
    private RelativeLayout mRL_3;
    private String TAG = "---Yancy---";
    private List<String> path = new ArrayList<>();
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        initTheme();
        initView();
        initData();
        initExp();
        initGallery();
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
        setContentView(R.layout.chat_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        mList = (ListView) findViewById(R.id.lv_chat);
        mEt_input = (EditText) findViewById(R.id.et_chat_input);
        mBt_send = (Button) findViewById(R.id.bt_chat_send);

        //exp
        mRL_1 = (RelativeLayout) findViewById(R.id.rl_chat_1);
        ll_exp = (LinearLayout) findViewById(R.id.ll_chat_exp);
        gv_exp = (GridView) findViewById(R.id.gv_chat_exp);


        //image
        mRL_3 = (RelativeLayout) findViewById(R.id.rl_chat_3);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        Intent i = getIntent();
        ID = i.getLongExtra("id", 0);
        String n = i.getStringExtra("n");
        mToolbar.setTitle(n);
        mData = new ArrayList<>();
        mAdapter = new MessageAdapter(this, mData, mListener);
        mList.setAdapter(mAdapter);
        getHistory();
        JMessageClient.enterGroupConversation(ID);
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_exp.setVisibility(View.GONE);
                final String text = mEt_input.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    Message message = JMessageClient.createGroupTextMessage(ID, text);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        getResources().getString(R.string.send_fail) + s,
                                        Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    JMessageClient.sendMessage(message);
                    mData.add(message);
                    mAdapter.notifyDataSetChanged();
                    mEt_input.setText("");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mList.setSelection(mAdapter.getCount());
                        }
                    }, 100);


                }
            }
        });

        mEt_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        ll_exp.setVisibility(View.GONE);
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

        //exp
        mRL_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_exp.getVisibility() == View.GONE) {
                    ll_exp.setVisibility(View.VISIBLE);
                } else {
                    ll_exp.setVisibility(View.GONE);
                }
            }
        });
        gv_exp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SpannableString ss = new SpannableString(key_exp.get(i));
                Drawable drawable = getResources().getDrawable((Integer) list_exp.get(i).get("pic"));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ss.setSpan(new ImageSpan(drawable), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mEt_input.append(ss);
            }
        });


        //image
        mRL_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                path.clear();
                galleryConfig.getBuilder().pathList(path).build();
                initPermissions();
//                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(GroupChat.this);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Intent i = new Intent();
            i.putExtra("id", ID);
            i.putExtra("b", 1);
            i.setClass(GroupChat.this, GetGroupInfo.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        if (message.getTargetType() != ConversationType.group) {
            return;
        }

        GroupInfo groupInfo = (GroupInfo) message.getTargetInfo();
        if (groupInfo.getGroupID() != ID) {
            return;
        }
        mData.add(message);
        mAdapter = new MessageAdapter(this, mData, mListener);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mList.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mList.setSelection(mAdapter.getCount());
            }
        });
    }


    private void getHistory() {
        Conversation conversation = JMessageClient.getGroupConversation(ID);
        if (conversation == null) {
            return;
        }
        List<Message> messageList = conversation.getMessagesFromOldest(
                conversation.getLatestMessage().getId() - 50, 50);

        for (Message message : messageList) {
            mData.add(message);
            mAdapter.notifyDataSetChanged();
            mList.setSelection(mAdapter.getCount());

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Conversation conversation = JMessageClient.getGroupConversation(ID);
        if (conversation != null) {
            conversation.resetUnreadCount();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.exitConversation();
        JMessageClient.unRegisterEventReceiver(this);
        finish();
    }

    private MessageAdapter.MyClickListener mListener = new MessageAdapter.MyClickListener() {
        @Override
        public void myOnClick(int p, View v) {
            String u = mData.get(p).getFromUser().getUserName();
            Intent i = new Intent();
            if (u.equals("系统消息")) {
                return;
            }
            if (u.equals(JMessageClient.getMyInfo().getUserName())) {
                i.setClass(GroupChat.this, MyInfo.class);
            } else {
                i.putExtra("u", u);
                i.putExtra("b", 1);
                i.setClass(GroupChat.this, GetUserInfo.class);
            }
            startActivity(i);
        }
    };


    /**
     * exp message
     */
    //
    //
    //
    private void initExp() {
        list_exp = new ArrayList<>();
        key_exp = new ArrayList<>();
        for (int i = 0; i < DefQqEmoticons.sQqEmoticonKey.length; i++) {
            String s = DefQqEmoticons.sQqEmoticonKey[i];
            key_exp.add(s);
            Map m = new HashMap();
            m.put("pic", DefQqEmoticons.sQqEmoticonHashMap.get(s));
            list_exp.add(m);
        }
        simple_exp = new SimpleAdapter(this, list_exp,
                R.layout.chat_exp_item, new String[]{"pic"},
                new int[]{R.id.img_chat_exp_item});
        gv_exp.setAdapter(simple_exp);

    }

    /**
     * image message
     */
    //
    //
    //
    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
                path.clear();
                for (String s : photoList) {
                    Log.i(TAG, s);
                    path.add(s);
                }
                for (String string : path)
                    uploadPic(string);
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };

        //init galleryConfig
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.wzm.tasking.FileProvider")   // provider(必填)
                .pathList(path)                         // 记录已选的图片
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(false)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(false, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .isOpenCamera(false)                    // 是否直接打开相机
                .build();

    }


    private void uploadPic(String picturePath) {
        if (picturePath != null) {
            File file = new File(picturePath);
            try {
                final Message message = JMessageClient.createGroupImageMessage(ID, file);
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.send_fail) + s,
                                    Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                JMessageClient.sendMessage(message);
                mData.add(message);
                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mList.setSelection(mAdapter.getCount());
                    }
                }, 100);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(GroupChat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission", "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(GroupChat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("Permission", "拒绝过了");
                Toast.makeText(GroupChat.this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("Permission", "进行授权");
                ActivityCompat.requestPermissions(GroupChat.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
            }
        } else {
            Log.i("Permission", "不需要授权 ");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(GroupChat.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 8) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "同意授权");
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(GroupChat.this);
            } else {
                Log.i("Permission", "拒绝授权");
            }
        }
    }

}
