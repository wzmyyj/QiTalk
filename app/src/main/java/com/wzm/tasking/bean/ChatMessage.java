package com.wzm.tasking.bean;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by yyj on 2017/6/15 0015.
 */

public class ChatMessage {
    private Bitmap bitmap;
    private String name;
    private Date date;
    private String msg;
    private String url;
    private Type type;

    public enum Type {
        IN_COMING, OUT_COMING
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ChatMessage() {
    }

    public ChatMessage(Bitmap bitmap, String name, Date date, String msg, Type type) {
        this.bitmap = bitmap;
        this.name = name;
        this.date = date;
        this.msg = msg;
        this.type = type;
    }

    public ChatMessage(Bitmap bitmap, String name, Date date, String msg, String url, Type type) {
        this.bitmap = bitmap;
        this.name = name;
        this.date = date;
        this.msg = msg;
        this.url = url;
        this.type = type;
    }


}
