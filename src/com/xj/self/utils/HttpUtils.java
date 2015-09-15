package com.xj.self.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xj.self.entity.ChatMessage;
import com.xj.self.entity.ChatMessage.Type;
import com.xj.self.entity.Result;

/**
 * 使用google的Gson进行json操作
 *
 */
public final class HttpUtils {
    
    private static String API_KEY = "534dc342ad15885dffc10d7b5f813451";
    private static String URL = "http://www.tuling123.com/openapi/api";
    
    /**
     * 发送一个消息，并得到返回的消息
     * @param msg
     * @return
     */
    public static ChatMessage sendMsg(String msg){
        ChatMessage message = new ChatMessage();
        String url = setParams(msg);
        String res = doGet(url);
        Gson gson = new Gson();
        Result result = gson.fromJson(res, Result.class);
        
        if(result.getCode()>400000 || TextUtils.isEmpty(result.getText())){
            message.setMsg("该功能等待开发...");
        }else{
            message.setMsg(result.getText());
        }
        message.setType(Type.INPUT);
        message.setDate(new Date());
        
        return message;
    }

    /**
     * Get请求，获得返回数据
     * @param urlStr
     * @return
     */
    private static String doGet(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try{
           url = new URL(urlStr);
           conn = (HttpURLConnection) url.openConnection();
           conn.setReadTimeout(5*1000);
           conn.setConnectTimeout(5*1000);
           conn.setRequestMethod("GET");
           if(conn.getResponseCode() == 200){
               is = conn.getInputStream();
               baos = new ByteArrayOutputStream();
               int len = -1;
               byte[] buf = new byte[128];
               while((len = is.read(buf))!=-1){
                   baos.write(buf, 0, len);
               }
               baos.flush();
               return baos.toString();
           }else{
               throw new RuntimeException("服务器连接错误！");
           }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("服务器连接错误！");
        }finally{
            try{
                if(is != null)
                    is.close();
                if (baos != null)  
                    baos.close();  
                conn.disconnect();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 拼接Url
     * @param msg
     * @return
     */
    private static String setParams(String msg) {
        try{
            msg = URLEncoder.encode(msg,"UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        
        return URL + "?key=" + API_KEY + "&info=" + msg;
    }

}