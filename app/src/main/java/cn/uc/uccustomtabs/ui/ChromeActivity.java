package cn.uc.uccustomtabs.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import cn.uc.uccustomtabs.R;
import cn.uc.uccustomtabs.entities.ActionBtnParam;
import cn.uc.uccustomtabs.entities.CustomTabsProperties;
import cn.uc.uccustomtabs.entities.MenuItemParam;
import cn.uc.uccustomtabs.service.WebViewProvider;


/**
 * Created by Andrewlu.lzw on 2016/3/8.
 * 实现通过外部传入参数控制页面标题栏显示样式功能.
 */
public class ChromeActivity extends AppCompatActivity {
    private FrameLayout webViewContainer;
    private Toolbar toolBar;
    private CustomTabsProperties properties;
    private CustomTabsCallback callback;
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webViewContainer = (FrameLayout) findViewById(R.id.webViewContainer);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setSubtitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        properties = CustomTabsProperties.getFromIntent(getIntent());
        if (properties.getToken() != null) {
            callback = properties.getToken().getCallback();
        }

        //设置返回按钮事件.
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置页面进入/退出时的动画效果.
                //overridePendingTransition(0,0);
                onBackPressed();
            }
        });

        setUpActionBar();

        //加载网页.
        if (properties.getData() != null) {
            final String url = properties.getData().toString();
            WebViewProvider.getInstance().createWebView(url, new WebViewProvider.WebViewCreator() {
                @Override
                public void onWebViewCreated(WebView webView) {
                    mWebView = webView;
                    boolean isShowTitle = properties.isShowTitle();
                    if (isShowTitle) {
                        if (mWebView.getTitle() == null || mWebView.getTitle().isEmpty()) {
                            mWebView.setWebChromeClient(new WebChromeClient() {
                                @Override
                                public void onReceivedTitle(WebView view, String title) {
                                    toolBar.setTitle(mWebView.getTitle());
                                    toolBar.setSubtitle(properties.getData().toString());
                                }
                            });
                        } else {
                            //获取到网页的标题信息.
                            toolBar.setTitle(mWebView.getTitle());
                            toolBar.setSubtitle(properties.getData().toString());
                        }
                        //toolBar.setSubtitle(properties.getData().toString());
                    } else {
                        toolBar.setTitle(properties.getData().toString());
                        toolBar.setSubtitle(null);
                    }
                    webViewContainer.addView(mWebView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                }
            });
        }
    }

    public void onDestroy() {
        super.onDestroy();
        WebViewProvider.getInstance().recycleWebView(mWebView);
    }


    //设置页面效果.
    private void setUpActionBar() {
        int color = properties.getToolbarColor();
        Bitmap closeBtnIcon = properties.getCloseButtonIcon();
        toolBar.setBackgroundColor(color);
        if (closeBtnIcon != null) {
            toolBar.setNavigationIcon(new BitmapDrawable(getResources(), closeBtnIcon));
        }

        //选择是否禁止滚动隐藏标题栏.
        boolean autoHide = properties.isEnableUrlBarHiding();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        for (ActionBtnParam p : properties.getActionButtons()) {
            menu.add(0, p.getId(), p.getId(), p.getLabel())
                    .setIcon(new BitmapDrawable(getResources(), p.getIcon()))
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        if (properties.isShowDefaultShareMenuItem()) {
            menu.add(0, -1, 0, R.string.app_share_text).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

        if (properties.getMenuItems() != null) {
            int i = 0;
            for (MenuItemParam bundle : properties.getMenuItems()) {
                menu.add(0, bundle.getMenuId(), i++, bundle.getMenuText()).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        MenuItemParam p = properties.getMenuItem(id);
        if (p != null) {
            PendingIntent pendingIntent = p.getPendingIntent();
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            return true;
        }
        ActionBtnParam param = properties.getActionButton(id);
        if (param != null) {
            try {
                param.getPendingIntent().send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
            return true;
        }

        //default share menu.
        if (id == -1) {
            showDefaultShareMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDefaultShareMenu() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, properties.getData().toString());
        startActivity(Intent.createChooser(i, "分享到:"));
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (callback != null) {
            callback.onNavigationEvent(CustomTabsCallback.NAVIGATION_ABORTED, new Bundle());
        }
    }
}
