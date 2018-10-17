package com.wzm.tasking.tools;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wzm.tasking.bean.ChatMessage;
import com.wzm.tasking.bean.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;

public class HttpTuring {
    private static final String URL = "http://www.tuling123.com/openapi/api";
    private static final String API_KEY = "<--you turing appkey--->";

    public static ChatMessage sendMessage(String string) {
        ChatMessage chatMessage = new ChatMessage();
        String jsonRes = doGet(string);
        Gson gson = new Gson();
        Result result = null;

        try {
            result = gson.fromJson(jsonRes, Result.class);
            String msg = result.getText();
            String url = result.getUrl();
            if (msg.contains("http")) {
                String[] str = msg.split("http");
                msg = str[0];
                url = "http" + str[1];
            }
            chatMessage.setMsg(msg);
            chatMessage.setUrl(url);
        } catch (JsonSyntaxException e) {
            chatMessage.setMsg("服务器繁忙，请稍后再试！");
        }
        chatMessage.setDate(new Date());
        chatMessage.setType(ChatMessage.Type.IN_COMING);
        return chatMessage;
    }

    public static String doGet(String msg) {
        String result = "";
        String url = setParams(msg);
        ByteArrayOutputStream bao = null;
        InputStream is = null;

        java.net.URL urlNet;
        try {
            urlNet = new java.net.URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlNet
                    .openConnection();
            conn.setReadTimeout(3 * 1000);
            conn.setConnectTimeout(3 * 1000);
            conn.setRequestMethod("GET");

            is = conn.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];
            bao = new ByteArrayOutputStream();
            while ((len = is.read(buf)) != -1) {
                bao.write(buf, 0, len);
            }
            bao.flush();
            result = new String(bao.toByteArray());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bao != null) {
                try {
                    bao.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private static String setParams(String msg) {
        String url = "";
        try {
            url = URL + "?key=" + API_KEY + "&info="
                    + URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

}
