package com.example.verifydemo;

import com.token.verifysdk.VerifyCoder;
import com.token.verifysdk.VerifyCoder.VerifyListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VerifyPopupActivity extends Activity {
    private WebView mWebView;
    private RelativeLayout mContainer;
    private ProgressBar mProgressBar;
    private float mDensity;
    private float mScale = 0.7f; //默认弹框验证码宽度是屏幕宽度*0.7
    private final float F_DEFAULT_POPUP_IFRAME_WIDTH = 18.2f*16;
    private final int F_MAX_IFRAME_WIDTH_SCALE = 2;
    private final int F_CAP_TYPE_CLICK_CHAR_ZH = 4;//图中点字(中文)
    private final int F_CAP_TYPE_CLICK_CHAR_EN = 6;//图中点字(英文)
    private final int F_CAP_TYPE_SLIDE_PUZZLE = 7;//滑动拼图
    private VerifyListener mListener = new VerifyListener() {

        @Override
        public void onVerifySucc(String ticket, String randstr) {
            // TODO Auto-generated method stub
            Intent it = new Intent();
            it.putExtra("ticket", ticket);
            it.putExtra("randstr", randstr);
            setResult(3, it);
            finish();
        }

        @Override
        public void onVerifyFail() {
            // TODO Auto-generated method stub
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        @Override
        public void onIframeLoaded(int state, String info) {
            //收到验证码页面(包括图片)加载完成回调时，把Loading隐藏，WebView显示
            mProgressBar.setVisibility(View.INVISIBLE);
            mWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onIFrameResize(float width, float height) {
            //验证
            android.view.WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.width = (int)(width*mDensity);
            attributes.height = (int)(height*mDensity);
            getWindow().setAttributes(attributes);
        }

    };

    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        String jsurl = getIntent().getStringExtra("jsurl");
        if (jsurl == null) {
            finish();
            return;
        }

        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;
        int windowWidth = metrics.widthPixels;

        /*
        * 以滑动拼图弹框验证码为例，取弹框验证码宽度为屏幕宽度0.7
        * 滑动拼图标准宽18.2*16dp，标准高16.1*16dp,最大缩放比例2 ----capType=7
        * 图中点字标准宽18.2*16dp，标准高19.6*16dp,最大缩放比例2 ----capType=4,6
        * */
        int iframeWidthPX = (int) (windowWidth * mScale);
        int iframeWidthDP = (int)(iframeWidthPX/mDensity);
        if (iframeWidthDP >= (int)(F_DEFAULT_POPUP_IFRAME_WIDTH*F_MAX_IFRAME_WIDTH_SCALE)){
            iframeWidthDP = (int)(F_DEFAULT_POPUP_IFRAME_WIDTH*F_MAX_IFRAME_WIDTH_SCALE);
            iframeWidthPX = (int)(iframeWidthDP*mDensity);
        }
        //根据验证码类型和弹框宽度，获取验证码弹框高度
        int iframeHeightDP = VerifyCoder.getPopupIframeHeightByWidthAndCaptype(iframeWidthDP,F_CAP_TYPE_SLIDE_PUZZLE);
        int iframeHeightPX = (int)(iframeHeightDP * mDensity);

        //设置主题色，弹框验证码，弹框宽度
        VerifyCoder verifyCoder = VerifyCoder.getVerifyCoder();
        verifyCoder.setJson("themeColor:'ff0000',type:'popup',fwidth:"+iframeWidthDP);
        mWebView = verifyCoder.getWebView(getApplicationContext(), jsurl, mListener);
        mWebView.requestFocus();
        mWebView.forceLayout();

        //业务可根据自己需要实现不同的loading展现
        setContentView(R.layout.activity_verify_popup);
        mContainer = (RelativeLayout)findViewById(R.id.container);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mWebView.setVisibility(View.INVISIBLE);
        mContainer.addView(mWebView);
        android.view.WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = iframeWidthPX;
        attributes.height = iframeHeightPX;
        getWindow().setAttributes(attributes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.freeMemory();
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        VerifyCoder.getVerifyCoder().release();
    }

}
