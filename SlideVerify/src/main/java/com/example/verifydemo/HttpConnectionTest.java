package com.example.verifydemo;

/**
 * Created by apple on 2018/2/11.
 */
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionTest {

    //交互验证的的Listenclient
    void Listenclient(String usrStr, JSONObject jsonObject )throws JSONException, UnsupportedEncodingException{
        //System.out.println(jsonObject);
        //String str = URLEncoder.encode(String.valueOf(jsonObject), "UTF-8");
        //System.out.println(str);
        try {
            URL url = new URL(usrStr);//创建一个URL
            HttpURLConnection connection  = (HttpURLConnection)url.openConnection();//通过该url获得与服务器的连接
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");//设置请求方式为post
            connection.setConnectTimeout(3000);//设置超时为3秒
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置传送类型
            connection.setRequestProperty("ticket", "dsfsdfdsfdsfdsf##8***");
//				connection.setRequestProperty("user_password", "12345");
//				connection.setRequestProperty("user_email", "632024528@qq.con");
//				try {
//	                JSONObject user = new JSONObject();
//	                user.put("email", "123@163.com");
//	                user.put("password", "123456789");
//	                //将JSON数据添加到输出流中
//	                OutputStream ot = connection.getOutputStream();
//	                ot.write(user.toString().getBytes());
//	                PrintWriter out = new PrintWriter(ot);
//	                out.flush();
//	            } catch (Exception e) {
//	            }
//				  try(
//			              //接收服务器端发回的响应
//			           BufferedReader in = new BufferedReader(new InputStreamReader
//			                    (connection.getInputStream() , "utf-8")))
//			            {
//			                String line;
//			                while ((line = in.readLine())!= null)
//			                {
//			                    result += "\n" + line;
//			                }
//			            }
//			        }
//			        catch(Exception e)
//			        {
//			            System.out.println("POST异常" + e);
//			            e.printStackTrace();
//			        }
            //将JSON数据添加到输出流中
            OutputStream ot = connection.getOutputStream();
            ot.write(jsonObject.toString().getBytes());
            PrintWriter out = new PrintWriter(ot);
            out.flush();
//				PrintWriter out = new PrintWriter(connection.getOutputStream());
//	            out.print(str);//写入输出流
//	            out.flush();//立即刷新
//	            out.close();

            //post方法要传送的键值对
            connection.connect();
            //读取响应
            int responseCode = connection.getResponseCode();//获得连接的状态码
            if(responseCode == 200){//200表示连接服务器成功，且获得正确响应
                System.out.println("连接服务器成功");
                // final Intent it = new Intent(this, PushConfigActivity.class); //你要转向的Activity
                // startActivity(it);
            }else{
                System.out.println("连接服务器失败"+responseCode);
            }
        } catch (Exception  e) {
            e.printStackTrace();
        }
        //return result;
    }
}
