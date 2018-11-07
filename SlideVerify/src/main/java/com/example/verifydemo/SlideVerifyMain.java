package com.example.verifydemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

//import com.alivc.live.pusher.demo.PushConfigActivity;

public class SlideVerifyMain extends AppCompatActivity implements View.OnClickListener{
    String mTestUrl = "https://support.captcha.qq.com/cgi-bin/open_cap/test.pl";//第三方业务使用验证码需替换此url //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.verifybt_fullscreen)).setOnClickListener(this); //确定(注册表注册完) 要去验证
        ((Button)findViewById(R.id.verifybt_popup)).setOnClickListener(this);
    }

    //调用验证函数
    private void gotoVerifyFullScreenActivity(String jsurl){
        try {
            Log.e("url=", jsurl); //jsurl 获取验证码JSURL的接口
            Intent it = new Intent(this,VerifyFullScreenActivity.class); //传值到verifyfullScreenActuvity类
            it.putExtra("jsurl", jsurl); //这个是附带的数据
            startActivityForResult(it,1); //开启
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void gotoVerifyPopupActivity(String jsurl){
        try {
            Log.e("url=", jsurl);
            Intent it = new Intent(this, VerifyPopupActivity.class);
            it.putExtra("jsurl", jsurl);
            //调用函数去验证
            startActivityForResult(it,1); //第二个参数：如果> = 0,当Activity结束时requestCode将归还在onActivityResult()中。
                                                     // 以便确定返回的数据是从哪个Activity中返回，用来标识目标activity。
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void onResume(){
        super.onResume(); //生命周期
    }

    //实现onActivityResult来接收是否验证成功的通知结果
    //在一个主界面(主Activity)上能连接往许多不同子功能模块(子Activity上去)，当子模块的事情做完之后就回到主界面，或许还同时返回一些子模块完成的数据交给主Activity处理。
    // 这样的数据交流就要用到回调函数onActivityResult。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                Log.e("onActivityResult", "verifysucc");
                String ticket = data.getStringExtra("ticket");
                String randstr = data.getStringExtra("randstr");
                Toast.makeText(SlideVerifyMain.this, "验证成功,票据为"+ticket,Toast.LENGTH_LONG).show();
                //滑动验证成功之后，和服务器进行交互，验证快手的账号、密码的正确
             }
        }
    }

    //
    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        // Android use X509 cert
        X509TrustManager[] trustAllCerts = new X509TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //按钮的监控
    @Override
    public void onClick(final View view) {
        int i = view.getId();
        if (i == R.id.verifybt_fullscreen || i == R.id.verifybt_popup) {
            //然后解析"滑动模块"认证的URL
//                String SerVerUrl = "";
//                ReceVerUrl JaMe = new  ReceVerUrl(SerVerUrl);
//                String Gurl = JaMe.getHtml(SerVerUrl);
            //String url = "https://captcha.guard.qcloud.com/template/TCapIframeApi.js?appid=1251633352&clientype=1&lang=2052&asig=vZ_wChvXyxSll35_Ip3pfvLYsEBCFWE_ikdN_7yhdUaMU8gpFDcoOfUXD-zM4KkhygEn3Nk7z5USfjfHHv32g1Enor5NOXfL";
            String url = "http://captcha.guard.qcloud.com/template/TCapIframeApi.js?appid=1252020920&clientype=1&lang=2052&asig=ERGIGQidN3S7v3M00t4zhiftNtWR64FfE2JuDnHgRHPKEW01U6ztQTYOGWagfBzl6PPciysZLjR3YFVUiqDOu2Ud4nPp5wDYaYN9zHodIxfF2_QKwtvFwELjk_Qui1NtuCnXzMQGM3L-0NSy7h87cw**";
            if (url != null) {
                if (view.getId() == R.id.verifybt_fullscreen) {
                    gotoVerifyFullScreenActivity(url);
                } else {
                    gotoVerifyPopupActivity(url);
                }
            } else {
                String msg = "Error for verification code address";
                Toast.makeText(SlideVerifyMain.this, msg, Toast.LENGTH_LONG).show();
            }
//                new Thread(new Runnable(){
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        trustAllHosts(); //认证机关
//                        HttpClient client  =new HttpClient(); //HttpClient请求
//                        byte[] response = client.request(MainActivity.this, mTestUrl); //腾讯服务器认证地址 1
//                        if(response==null){//失败了
//                            Log.d("requestfail", "response==null");
//                            return; //这里返回获取验证码js url错误之后的操作
//                        }
//                        String responseString = new String(response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(responseString); //定义一个json数据对象
//                            int code = jsonObject.optInt("code",-1); //获得code(error code)的值
//                            if(code==0){
//                                String url = jsonObject.optString("url"); //获取验证码的jsurl地址 2
//                                if(url!=null){
//                                    if (view.getId() == R.id.verifybt_fullscreen){
//                                        gotoVerifyFullScreenActivity(url); //依据传入的js url调用sdk展示验证码界面 3，
//                                                                          // 用户触碰滑块验证，android端获取到校验验证码 3-4
//                                                                         // 提交验证码票据向腾讯服务器验证 4-5
//                                    }else{
//                                        gotoVerifyPopupActivity(url);
//                                    }
//
//                                }
//                            }
//                            else{//获取jsurl失败
//                                String msg = jsonObject.optString("message");
//                                Toast.makeText(MainActivity.this, msg,Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//
//                }).start();

        } else {
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}