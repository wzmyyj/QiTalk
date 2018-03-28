package com.wzm.tasking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wzm.tasking.activity.OtherChat;

import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        // Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
//			System.out.println("收到了自定义消息。消息内容是："
//					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
//			System.out.println("收到了通知֪ͨ);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
//			System.out.println("用户点击打开了通知");
            Intent i = new Intent(context, OtherChat.class);
            i.putExtra("msg",
                    "WZM: " + bundle.getString(JPushInterface.EXTRA_ALERT));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            // Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

    }

}
