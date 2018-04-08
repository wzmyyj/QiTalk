package com.wzm.tasking.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;

public class SaveUserAvatar extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;
    private ImageView mImageView;
    private Button mBt_save;
    public static Bitmap mBitmap;

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
        setContentView(R.layout.activity_user_avatar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_user_avatar);
        mBt_save = (Button) findViewById(R.id.bt_save);
        mImageView = (ImageView) findViewById(R.id.img_show_image);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        mProgressDialog = ProgressDialog.show(SaveUserAvatar.this,
                getResources().getString(R.string.Tip),
                getResources().getString(R.string.loading));
        mProgressDialog.setCanceledOnTouchOutside(true);
        GetUserInfo.Info.getBigAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);
                    mBitmap = bitmap;
                }
                mProgressDialog.dismiss();

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
        mBt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmap(mBitmap, GetUserInfo.Info.getUserName());
            }

        });
    }

    private void saveBitmap(Bitmap bitmap, String string) {
        if (bitmap == null) {
            Toast.makeText(this,
                    getResources().getString(R.string.bitmap_is_null),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String filePath = Environment.getExternalStorageDirectory()
                + File.separator;
        File f = new File(filePath, string + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(this,
                    getResources().getString(R.string.save_success),
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        this.sendBroadcast(intent);
    }
}
