package com.wzm.tasking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzm.tasking.R;
import com.wzm.tasking.tools.Expression;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.callback.DownloadCompletionCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;


public class MessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Message> mData;
    private MyClickListener mListener;


    public MessageAdapter(Context context, List<Message> data, MyClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.mData = data;
        mListener = listener;
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
        Message message = mData.get(position);
        if (message.getDirect() == MessageDirect.receive) {
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
    public View getView(final int position, View view, ViewGroup parent) {
        final Message message = mData.get(position);
        ViewHolder viewHolder = null;
        if (view == null) {
            if (getItemViewType(position) == 0) {
                view = mInflater.inflate(R.layout.chat_item_0, parent,
                        false);
            } else {
                view = mInflater.inflate(R.layout.chat_item_1, parent,
                        false);
            }
            viewHolder = new ViewHolder();
            viewHolder.mDate = (TextView) view
                    .findViewById(R.id.tv_chat_item_date);
            viewHolder.mName = (TextView) view
                    .findViewById(R.id.tv_chat_item_user);
            viewHolder.mHeader = (ImageView) view
                    .findViewById(R.id.img_chat_item_user);
            viewHolder.mText = (TextView) view
                    .findViewById(R.id.tv_chat_item_text);
            viewHolder.mImage = (ImageView) view
                    .findViewById(R.id.img_chat_item_img);
            viewHolder.mImage2 = (ImageView) view
                    .findViewById(R.id.img_chat_item_img2);
            view.setTag(viewHolder);
            viewHolder.mFL = (FrameLayout) view
                    .findViewById(R.id.fl_chat_item);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mImage2.setVisibility(View.GONE);
        final ViewHolder finalViewHolder = viewHolder;
        //time
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        viewHolder.mDate.setText(df.format(new Date(message.getCreateTime())));
        //userInfo
        UserInfo userInfo = message.getFromUser();
        if (!TextUtils.isEmpty(userInfo.getNotename())) {
            viewHolder.mName.setText(userInfo.getNotename());
        } else if (!TextUtils.isEmpty(userInfo.getNickname())) {
            viewHolder.mName.setText(userInfo.getNickname());
        } else {
            viewHolder.mName.setText(userInfo.getUserName());
        }
        userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                if (bitmap != null) {
                    finalViewHolder.mHeader.setImageBitmap(bitmap);
                } else {
                    finalViewHolder.mHeader.setImageResource(R.drawable.ic_header);
                }
            }
        });
        switch (message.getStatus()) {
            case send_success:
            case receive_success:
                viewHolder.mFL.setBackgroundResource(R.drawable.chat_bg);
                break;
            case send_fail:
            case receive_fail:
                viewHolder.mFL.setBackgroundResource(R.drawable.chat_bg3);
                break;
            case send_going:
            case receive_going:
                viewHolder.mFL.setBackgroundResource(R.drawable.chat_bg2);
                break;
        }

        //content
        MessageContent content = message.getContent();
        switch (message.getContentType()) {
            case text:
                viewHolder.mText.setVisibility(View.VISIBLE);
                viewHolder.mImage.setVisibility(View.GONE);
                TextContent textContent = (TextContent) content;
                String text = textContent.getText();
                SpannableString ss = Expression.getSpannableString(mInflater.getContext(), text);
                viewHolder.mText.setText(ss);
                break;
            case image:
                viewHolder.mText.setVisibility(View.GONE);
                viewHolder.mImage.setVisibility(View.VISIBLE);
                final ImageContent imageContent = (ImageContent) content;
                Glide.with(mInflater.getContext())
                        .load(imageContent.getLocalThumbnailPath())
                        .centerCrop()
                        .placeholder(R.mipmap.gallery_pick_photo)
                        .into(viewHolder.mImage);
                viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageContent.downloadOriginImage(message, new DownloadCompletionCallback() {
                            @Override
                            public void onComplete(int i, String s, File file) {
                                Glide.with(mInflater.getContext())
                                        .load(file)
                                        .centerCrop()
                                        .placeholder(R.mipmap.gallery_pick_photo)
                                        .into(finalViewHolder.mImage2);
                                finalViewHolder.mImage2.setVisibility(View.VISIBLE);
                                finalViewHolder.mImage.setVisibility(View.GONE);
                            }
                        });
                    }
                });
                break;
            case eventNotification:
                viewHolder.mText.setVisibility(View.VISIBLE);
                viewHolder.mImage.setVisibility(View.GONE);
                EventNotificationContent eventContent = (EventNotificationContent) content;
                String eventText = eventContent.getEventText();
                viewHolder.mText.setText(eventText);
                break;
        }

        viewHolder.mHeader.setOnClickListener(mListener);
        viewHolder.mHeader.setTag(position);
        return view;
    }

    private final class ViewHolder {
        TextView mDate;
        ImageView mHeader;
        TextView mName;
        TextView mText;
        ImageView mImage;
        ImageView mImage2;
        FrameLayout mFL;

    }

    public static abstract class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            myOnClick((int) v.getTag(), v);
        }

        public abstract void myOnClick(int p, View v);
    }

}
