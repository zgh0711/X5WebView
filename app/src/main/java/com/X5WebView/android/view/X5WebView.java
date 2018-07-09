package com.X5WebView.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.X5WebView.android.R;
import com.X5WebView.android.event.EventCenter;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author ZGH
 * @date 2017/12/11
 */

public class X5WebView extends WebView {
    private ProgressBar mProgressBar;

    public X5WebView(Context context) {
        super(context);
        setBackgroundColor(85621);
    }

    public X5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //画进度条
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 6);
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = context.getResources().getDrawable(R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);

        this.setWebChromeClient(webChromeClient);
        initWebViewSettings();
    }

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE) {
                    mProgressBar.setVisibility(VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(webView, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView webView, String s) {
            if (!TextUtils.isEmpty(s)) {
                //将页面 title 通过 eventbus 发送出去
                EventCenter<String> event = new EventCenter<>(1, s);
                EventBus.getDefault().post(event);
            }
        }
    };

    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }
}
