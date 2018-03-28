package com.wzm.tasking.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzm.tasking.MainActivity;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.ThemeManager;


public class Web extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private RelativeLayout rl_back;
    private RelativeLayout rl_forward;
    private RelativeLayout rl_refresh;
    private RelativeLayout rl_reset;
    private WebView web;
    private ProgressBar pb1;
    private String url;

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
        setContentView(R.layout.activity_web);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_web);
        rl_back = (RelativeLayout) findViewById(R.id.rl_web_back);
        rl_forward = (RelativeLayout) findViewById(R.id.rl_web_forward);
        rl_refresh = (RelativeLayout) findViewById(R.id.rl_web_refresh);
        rl_reset = (RelativeLayout) findViewById(R.id.rl_web_reset);
        web = (WebView) findViewById(R.id.wv_web);
        pb1 = (ProgressBar) findViewById(R.id.pb_web);
    }

    private void initData() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        Intent i = getIntent();
        url = i.getStringExtra("url");
        initWeb();
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web.canGoBack()) {
                    web.goBack();
                } else {
                    finish();
                }
            }
        });
        rl_back.setOnClickListener(this);
        rl_forward.setOnClickListener(this);
        rl_refresh.setOnClickListener(this);
        rl_reset.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCookie();
    }

    private void clearCookie() {
        CookieSyncManager.createInstance(getApplicationContext());
        CookieManager cookiemanager = CookieManager.getInstance();
        cookiemanager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
        web.setWebChromeClient(null);
        web.setWebViewClient(null);
        web.getSettings().setJavaScriptEnabled(false);
        web.clearCache(true);

    }

    private void initWeb() {
        if (url == null) {
            return;
        }
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mToolbar.setTitle(view.getTitle());
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings set = web.getSettings();
        set.setJavaScriptEnabled(true);
        set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mToolbar.setTitle(view.getTitle());
                if (newProgress < 70) {
                    pb1.setProgress(newProgress);
                } else {
                    pb1.setProgress(100);
                }

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_browser) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            Uri u = Uri.parse(url);
            i.setData(u);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // remark back;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_web_back:
                if (web.canGoBack()) {
                    web.goBack();
                }
                break;
            case R.id.rl_web_forward:
                if (web.canGoForward()) {
                    web.goForward();
                }
                break;
            case R.id.rl_web_refresh:
                web.reload();
                break;
            case R.id.rl_web_reset:
                initWeb();
                break;

        }
    }
}
