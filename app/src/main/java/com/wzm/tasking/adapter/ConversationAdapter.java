package com.wzm.tasking.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wzm.tasking.R;
import com.wzm.tasking.tools.Expression;
import com.wzm.tasking.view.GroupView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

public class ConversationAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Conversation> mData;

    public ConversationAdapter(Context context, List<Conversation> data) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Conversation conversation = mData.get(position);
        ViewHolder viewHolder = null;
        if (view == null) {

            view = mInflater.inflate(R.layout.fragment_1_item, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.mImage = (GroupView) view
                    .findViewById(R.id.img_chat_list_item);
            viewHolder.mFirst = (TextView) view
                    .findViewById(R.id.tv_chat_list_item_0);
            viewHolder.mName = (TextView) view
                    .findViewById(R.id.tv_chat_list_item_name);
            viewHolder.mText = (TextView) view
                    .findViewById(R.id.tv_chat_list_item_text);
            viewHolder.mCount = (TextView) view
                    .findViewById(R.id.tv_chat_list_item_count);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Message latestMessage = conversation.getLatestMessage();
        int unReadMsgCnt = conversation.getUnReadMsgCnt();
        String text = "", count = "", name = "", lastName = "";
        if (latestMessage != null) {
            MessageContent content = latestMessage.getContent();
            switch (content.getContentType()) {
                case text:
                    TextContent stringExtra = (TextContent) content;
                    text = stringExtra.getText();
                    break;
                case image:
                    text = "[图片]";
                    break;
                case eventNotification:
                    EventNotificationContent eventContent = (EventNotificationContent) content;
                    text = eventContent.getEventText();
                    break;
            }

        }
        if (unReadMsgCnt > 0) {
            count = "" + unReadMsgCnt;
            viewHolder.mCount.setText(count);
            viewHolder.mCount.setBackgroundResource(R.drawable.hip);
        } else {
            viewHolder.mCount.setBackgroundResource(R.color.colorClarity);
        }
        if (latestMessage != null) {
            if (latestMessage.getFromUser().getUserName() ==
                    JMessageClient.getMyInfo().getUserName()) {
                lastName = "";
            } else if (!TextUtils.isEmpty(latestMessage.getFromUser().getNotename())) {
                lastName = latestMessage.getFromUser().getNotename();
            } else if (!TextUtils.isEmpty(latestMessage.getFromUser()
                    .getNickname())) {
                lastName = latestMessage.getFromUser().getNickname();
            } else {
                lastName = latestMessage.getFromUser().getUserName();
            }
        }
        int size = (int) viewHolder.mText.getTextSize();
        viewHolder.mText.setText(lastName + ": ");
        SpannableString ss = Expression.getSpannableString(mInflater.getContext(), text, size + 5, size + 5);
        viewHolder.mText.append(ss);

        final Bitmap mBitmap = BitmapFactory.decodeResource(mInflater.getContext().
                getResources(), R.drawable.ic_header);
        switch (conversation.getType()) {
            case single:
                UserInfo info = (UserInfo) conversation.getTargetInfo();
                if (!TextUtils.isEmpty(info.getNotename())) {
                    name = info.getNotename();
                } else if (!TextUtils.isEmpty(info.getNickname())) {
                    name = info.getNickname();
                } else {
                    name = info.getUserName();
                }
                viewHolder.mName.setText(name);
                viewHolder.mImage.setTag(position);
                final List<Bitmap> bitmapList = new ArrayList<Bitmap>();
                final ConversationAdapter.ViewHolder finalViewHolder = viewHolder;
                info.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        if (finalViewHolder.mImage.getTag() != null
                                && finalViewHolder.mImage.getTag().equals(position))
                            if (bitmap != null) {
                                bitmapList.add(bitmap);
                            } else {
                                bitmapList.add(mBitmap);
                            }
                        try {
                            finalViewHolder.mImage.setImageBitmaps(bitmapList);
                        } catch (Exception e) {

                        }
                    }
                });

                break;

            case group:
                GroupInfo group = (GroupInfo) conversation.getTargetInfo();
                viewHolder.mName.setText(group.getGroupName());
                viewHolder.mImage.setTag(position);
                final List<Bitmap> bitmapList1 = new ArrayList<Bitmap>();
                final ConversationAdapter.ViewHolder finalViewHolder1 = viewHolder;
                final int m = group.getGroupMembers().size();
                for (int i = 0; i < m && i < 5; i++) {
                    bitmapList1.add(null);

                }

                for (int i = 0; i < m && i < 5; i++) {
                    final int p = i;
                    group.getGroupMembers().get(p).getAvatarBitmap(new GetAvatarBitmapCallback() {
                        @Override
                        public void gotResult(int i, String s, Bitmap bitmap) {
                            if (bitmap != null) {
                                bitmapList1.set(p, bitmap);
                            } else {
                                bitmapList1.set(p, mBitmap);
                            }
                            try {
                                if (finalViewHolder1.mImage.getTag() != null
                                        && finalViewHolder1.mImage.getTag().equals(position))
                                    finalViewHolder1.mImage.setImageBitmaps(bitmapList1);
                            } catch (Exception e) {

                            }


                        }
                    });
                }

                break;

        }
        return view;
    }

    private final class ViewHolder {
        GroupView mImage;
        TextView mFirst;
        TextView mName;
        TextView mText;
        TextView mCount;
    }

}
