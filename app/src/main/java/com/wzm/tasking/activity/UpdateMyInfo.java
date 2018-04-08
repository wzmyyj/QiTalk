package com.wzm.tasking.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.GlideImageLoader;
import com.wzm.tasking.tools.ThemeManager;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

@SuppressLint("NewApi")
public class UpdateMyInfo extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView mImg_avatar;
    private EditText mEt_nickname;
    private EditText mEt_birthday;
    private EditText mEt_region;
    private EditText mEt_signature;
    private Button mBt_save;
    private TextView mTv_update;
    private InputMethodManager mImm;
    private RadioButton mRb_male;
    private RadioButton mRb_female;
    private RadioButton mRb_unknown;

    private ProgressDialog mProgressDialog;

    private String TAG = "---Yancy---";

    private List<String> path = new ArrayList<>();

    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initView();
        initData();
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
        setContentView(R.layout.activity_update_my_info);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_update_my_info);
        mImg_avatar = (ImageView) findViewById(R.id.img_update_my_info_avatar);
        mEt_nickname = (EditText) findViewById(R.id.et_update_my_info_nickname);
        mEt_birthday = (EditText) findViewById(R.id.et_update_my_info_birthday);
        mEt_region = (EditText) findViewById(R.id.et_update_my_info_region);
        mEt_signature = (EditText) findViewById(R.id.et_update_my_info_signature);

        mRb_male = (RadioButton) findViewById(R.id.rb_update_my_info_male);
        mRb_female = (RadioButton) findViewById(R.id.rb_update_my_info_female);
        mRb_unknown = (RadioButton) findViewById(R.id.rb_update_my_info_unknown);

        mBt_save = (Button) findViewById(R.id.bt_update_my_info_save);
        mTv_update = (TextView) findViewById(R.id.tv_update_my_info_update);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        UserInfo myInfo = JMessageClient.getMyInfo();
        mEt_nickname.setText(myInfo.getNickname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (myInfo.getBirthday() > 0) {
            mEt_birthday.setText(sdf.format(new Date(myInfo.getBirthday())));
        }
        mEt_region.setText(myInfo.getRegion());
        mEt_signature.setText(myInfo.getSignature());
        if (myInfo.getGender() == UserInfo.Gender.male) {
            mRb_male.setChecked(true);
        } else if (myInfo.getGender() == UserInfo.Gender.female) {
            mRb_female.setChecked(true);
        } else {
            mRb_unknown.setChecked(true);
        }
        myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap != null) {
                    mImg_avatar.setImageBitmap(bitmap);
                } else {
                    mImg_avatar.setImageResource(R.drawable.ic_header);
                }
            }
        });
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mImg_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path.clear();
                galleryConfig.getBuilder().pathList(path).build();
                initPermissions();
//                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(UpdateMyInfo.this);
            }
        });
        mBt_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!mImm.isActive()) {
                    mImm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                mTv_update.setText("");

                final UserInfo myInfo = JMessageClient.getMyInfo();
                if (myInfo != null) {
                    if (!TextUtils.isEmpty(mEt_nickname.getText().toString())) {
                        myInfo.setNickname(mEt_nickname.getText().toString());

                        JMessageClient.updateMyInfo(UserInfo.Field.nickname,
                                myInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            mTv_update.append("update nickname success"
                                                    + "\n");
                                        } else {
                                            mTv_update.append("update nickname fail"
                                                    + "\n");
                                        }
                                    }
                                });
                    } else {
                        mTv_update.append("update nickname fail" + "\n");
                    }
                    if (!TextUtils.isEmpty(mEt_region.getText().toString())) {
                        myInfo.setRegion(mEt_region.getText().toString());
                        JMessageClient.updateMyInfo(UserInfo.Field.region,
                                myInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            mTv_update.append("update region success"
                                                    + "\n");
                                        } else {
                                            mTv_update.append("update region fail"
                                                    + "\n");

                                        }
                                    }
                                });
                    } else {
                        mTv_update.append("update region fail" + "\n");
                    }
                    if (!TextUtils.isEmpty(mEt_signature.getText().toString())) {
                        myInfo.setSignature(mEt_signature.getText().toString());
                        JMessageClient.updateMyInfo(UserInfo.Field.signature,
                                myInfo, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            mTv_update.append("update signature success"
                                                    + "\n");
                                        } else {
                                            mTv_update.append("update signature fail"
                                                    + "\n");
                                        }
                                    }
                                });
                    } else {
                        mTv_update.append("update signature fail" + "\n");
                    }
                    if (!TextUtils.isEmpty(mEt_birthday.getText().toString())) {

                        final String data = mEt_birthday.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        try {
                            final long time = sdf.parse(data).getTime();
                            myInfo.setBirthday(time);
                            JMessageClient.updateMyInfo(
                                    UserInfo.Field.birthday, myInfo,
                                    new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            if (i == 0) {
                                                mTv_update.append("update birthday success"
                                                        + "\n");
                                            } else {
                                                mTv_update.append("update birthday fail"
                                                        + "\n");
                                            }
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                        mTv_update.append("update birthday fail" + "\n");
                    }
                    final String gender;
                    if (mRb_male.isChecked()) {
                        myInfo.setGender(UserInfo.Gender.male);
                        gender = getResources().getString(R.string.male);
                    } else if (mRb_female.isChecked()) {
                        myInfo.setGender(UserInfo.Gender.female);
                        gender = getResources().getString(R.string.female);
                    } else {
                        myInfo.setGender(UserInfo.Gender.unknown);
                        gender = getResources().getString(R.string.unknown);
                    }

                    JMessageClient.updateMyInfo(UserInfo.Field.gender, myInfo,
                            new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    if (i == 0) {
                                        mTv_update.append("update gender success"
                                                + "\n");

                                    }
                                }
                            });
                } else {
                    Toast.makeText(UpdateMyInfo.this, "update fail : info == null",
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(UpdateMyInfo.this,
                        getResources().getString(R.string.save_success),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void uploadPic(String picturePath) {

        mProgressDialog = ProgressDialog.show(UpdateMyInfo.this,
                getResources().getString(R.string.Tip),
                getResources().getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(true);
        if (picturePath != null) {
            File file = new File(picturePath);
            try {
                JMessageClient.updateUserAvatar(file,
                        new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getResources().getString(R.string.update_success),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    JMessageClient.getMyInfo().getAvatarBitmap(new GetAvatarBitmapCallback() {
                                        @Override
                                        public void gotResult(int i, String s, Bitmap bitmap) {
                                            if (bitmap != null) {
                                                mImg_avatar.setImageBitmap(bitmap);
                                            } else {
                                                mImg_avatar.setImageResource(R.drawable.ic_header);
                                            }
                                        }
                                    });
                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(
                                            getApplicationContext(),
                                            getResources().getString(R.string.update_fail),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mProgressDialog.dismiss();
        }
    }


    //
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
                uploadPic(path.get(0));
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
                .multiSelect(false)                      // 是否多选   默认：false
                .multiSelect(false, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)                             // 配置多选时 的多选数量。    默认：9
                .crop(true)                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop(true, 1, 1, 500, 500)             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .isOpenCamera(false)                    // 是否直接打开相机
                .build();

    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(UpdateMyInfo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission", "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateMyInfo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i("Permission", "拒绝过了");
                Toast.makeText(UpdateMyInfo.this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i("Permission", "进行授权");
                ActivityCompat.requestPermissions(UpdateMyInfo.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
            }
        } else {
            Log.i("Permission", "不需要授权 ");
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(UpdateMyInfo.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 8) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "同意授权");
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(UpdateMyInfo.this);
            } else {
                Log.i("Permission", "拒绝授权");
            }
        }
    }
}
