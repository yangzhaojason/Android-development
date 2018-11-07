package com.example.verifydemo;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration; //getApplicationContext()
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.token.verifysdk.VerifyCoder;
import com.token.verifysdk.VerifyCoder.VerifyListener;

public class VerifyFullScreenActivity extends Activity {
    private WebView mWebView;
    private ProgressBar pg1;

    //开启监听,调用外部验证的.jar libs
    private VerifyListener listener = new VerifyListener() {
        @Override //编译器可以给你验证@Override下面的方法名是否是你父类中所有的
        public void onVerifySucc(String ticket, String randstr) {
            // TODO Auto-generated method stub  5
            Intent it = new Intent();
            it.putExtra("ticket", ticket);
            it.putExtra("randstr", randstr);
            setResult(Activity.RESULT_OK, it);
            finish(); //
        }
        @Override
        public void onVerifyFail() { //认证失败
            // TODO Auto-generated method stub
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        @Override
        public void onIframeLoaded(int state, String info) {

        }
        @Override
        public void onIFrameResize(float width, float height) {
            //全屏验证码可不用处理该方法
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置当前的窗体，无标题
        String content2 = getIntent().getStringExtra("jsurl"); //Intent传递数据 (得到返回数据)
        if (content2 == null) {
            finish();
            return;
        }
        //获取到jsurl后调用接口VerifyCoder.getVerifyCoder()
        VerifyCoder verify = VerifyCoder.getVerifyCoder(); //这是libs里面的认证的接口 获取单例
        verify.setShowtitle(true);
        //显示出验证码webview getApplicationContext()获取当前应用的上下文
        mWebView = verify.getWebView(getApplicationContext(), content2, listener); //获取验证码WebView,context调用验证码时当前界面的上下文，
                                                                            //requestcode对应onActivityResult的requestCode通过这里传递的
        mWebView.requestFocus(); //
        mWebView.forceLayout(); //
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
