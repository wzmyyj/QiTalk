package com.wzm.tasking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzm.tasking.R;
import com.wzm.tasking.activity.Web;
import com.wzm.tasking.bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ChatMessage> mData;


    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mData.get(position);
        if (chatMessage.getType() == ChatMessage.Type.IN_COMING) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ChatMessage chatMessage = mData.get(position);
        ViewHolder viewHolder = null;
        if (view == null) {
            if (getItemViewType(position) == 0) {
                view = mInflater.inflate(R.layout.chat_item_0, parent,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.mDate = (TextView) view
                        .findViewById(R.id.tv_chat_item_date);
                viewHolder.mText = (TextView) view
                        .findViewById(R.id.tv_chat_item_text);
                viewHolder.mName = (TextView) view
                        .findViewById(R.id.tv_chat_item_user);
                viewHolder.mImage = (ImageView) view
                        .findViewById(R.id.img_chat_item_user);
            } else {
                view = mInflater.inflate(R.layout.chat_item_1, parent,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.mDate = (TextView) view
                        .findViewById(R.id.tv_chat_item_date);
                viewHolder.mText = (TextView) view
                        .findViewById(R.id.tv_chat_item_text);
                viewHolder.mName = (TextView) view
                        .findViewById(R.id.tv_chat_item_user);
                viewHolder.mImage = (ImageView) view
                        .findViewById(R.id.img_chat_item_user);
            }
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        viewHolder.mDate.setText(df.format(chatMessage.getDate()));
        viewHolder.mText.setText(chatMessage.getMsg());

        final String url = chatMessage.getUrl();
        if (url != null) {
            viewHolder.mText.setText(chatMessage.getMsg());
            SpannableString ss = new SpannableString(url);
            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent i = new Intent();
                    i.putExtra("url", url);
                    i.setClass(mInflater.getContext(), Web.class);
                    mInflater.getContext().startActivity(i);
                }
            }, 0, url.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.mText.append("\n");
            viewHolder.mText.append(ss);
            viewHolder.mText.setMovementMethod(new LinkMovementMethod());
        }
        viewHolder.mName.setText(chatMessage.getName());
        if (chatMessage.getBitmap() != null)
            viewHolder.mImage.setImageBitmap(chatMessage.getBitmap());
        return view;
    }

    private final class ViewHolder {
        TextView mDate;
        TextView mText;
        TextView mName;
        ImageView mImage;
    }

}
