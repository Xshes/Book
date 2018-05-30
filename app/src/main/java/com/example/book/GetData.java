package com.example.book;

/**
 * Created by youzi on 2018/5/30.
 */
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.ByteArrayOutputStream;


public class GetData {
    public static String url="http://101.132.168.185:8080/api/";
    //获取返回的json
    public static String getJson(String path)  {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = StreamTool.read(in);
                String html = new String(data, "UTF-8");
                return html;
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //Post获取返回的json
    public static String postJson(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("Post");
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = StreamTool.read(in);
                String html = new String(data, "UTF-8");
                return html;
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

class StreamTool {
    //从流中读取数据
    public static byte[] read(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len=0;
        while((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
