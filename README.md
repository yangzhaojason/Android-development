# Android development of Tencent Sliding Verification

## 1、Interface call process

![img](https://mc.qcloudimg.com/static/img/2257609feb6fb4c458c319aaf199fdb2/image.png)

1) The background obtains the js address of the verification code by calling Tianpu's CaptchIframeQuery interface.
2) Return the obtained js address to the web client.
3) The client loads and verifies the verification c

ode according to the retrieved js address.
4) After the user verification is completed, submit the ticket returned by Tianyu to the background.
5) The background calls Tian Yu's CaptchaCheck interface to verify whether the ticket has passed the verification.

## 2、Android client API

1) Copy VerifySDK.jar to the libs directory and associate it to the project.

   [Android-SDK下载](https://mc.qcloudimg.com/static/archive/48720dad0a66293a8837a60b88ceef4e/archive.zip)

2) Add a statement if AndroidManifest.xml does not declare the following permissions

```
<uses-permission android:name="android.permission.INTERNET" />
```

3) Add a statement in AndroidManifest.xml

```
<activity android:name="com.token.verifysdk.VerifyActivity"></activity>
```

4) Get the jsurl from the background before issuing the verification code (refer to the interface of the server development to obtain the verification code JSURL)

5) After getting the jsurl, call the interface VerifyCoder.getVerifyCoder().startVerifyActivityForResult(Context context, String jsurl, int requestCode) and implement onActivityResult to receive notification of whether the verification is successful.

```
/*
 *  Parameter Description：
 *  context         调用验证码时当前界面的上下文，
 *                  用于(Activity) context).startActivityForResult，
 *                  请勿使用application的上下文
 *  jsurl           验证码的js链接，从我们后台获取
 *  requestCode     对应onActivityResult的requestCode，可自定义
 */
VerifyCoder.getVerifyCoder().startVerifyActivityForResult(Context context,String jsurl,int requestCode)

//onActivityResult实现实例：
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {//此处对应startVerifyActivityForResult的参数值
            if(resultCode==Activity.RESULT_OK){
                Log.e("onActivityResult", "verifysucc");
                Toast.makeText(MainActivity.this, "验证成功",2000).show();
            }
            else{
                Toast.makeText(MainActivity.this, "未验证成功",2000).show();
            }
        }
    }
```

6) If you are confused, you need to add a script

```
<arg value="-libraryjars ${lib}/VerifySDK.jar"/>
<arg value="-keep public class com.token.verifysdk{*; }" />
```

## 3、Android client API other interface description

```
public static VerifyCoder getVerifyCoder()      //获取单例
public void release()                           //重置参数，释放资源
public void setShowtitle(boolean showtitle)     //是否显示验证码页面标题栏
Public void setJson(String json)                //用于扩展参数，如实现自定义样式等
public WebView getWebView(Context context,String jsurl,VerifyListener listener) //获取验证码WebView
```

## 4、My Demo "SlideVerify" introduction

The calling interface of this API is the most difficult part of my video streaming program. It is also often asked during interviews. Due to the need for confidentiality of the project, only the core interface functions can be exposed.