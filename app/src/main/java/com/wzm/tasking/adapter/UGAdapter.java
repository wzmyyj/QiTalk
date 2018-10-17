package com.wzm.tasking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzm.tasking.R;

import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by yyj on 2017/8/9 0009.
 */

public class UGAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<UserInfo> mData;
    private List<GroupInfo> mData1;
    private int type;

    public UGAdapter(Context context, List<UserInfo> mData, List<GroupInfo> mData1) {
        mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.mData1 = mData1;
        if (mData != null) {
            type = 1;
        } else {
            type = 2;
        }
    }

    @Override
    public int getCount() {
        if (type == 1) {
            return mData.size();
        } else {
            return mData1.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (type == 1) {
            return mData.get(position);
        } else {
            return mData1.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.fragment_2_item_friend, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.img_user_list_item);
            viewHolder.mTextView = (TextView) view.findViewById(R.id.tv_user_list_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        switch (type) {
            case 1:
                final UserInfo userInfo = mData.get(position);
                String name = "";
                if (!TextUtils.isEmpty(userInfo.getNotename())) {
                    name = userInfo.getNotename();
                } else if (!TextUtils.isEmpty(userInfo.getNickname())) {
                    name = userInfo.getNickname();
                } else {
                    name = userInfo.getUserName();
                }
                viewHolder.mTextView.setText(name);
                viewHolder.mImageView.setTag(position);
                final ViewHolder finalViewHolder = viewHolder;
                userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        if (finalViewHolder.mImageView.getTag() != null
                                && finalViewHolder.mImageView.getTag().equals(position))
                            if (bitmap != null) {
                                finalViewHolder.mImageView.setImageBitmap(bitmap);
                            } else {
                                finalViewHolder.mImageView.setImageResource(R.drawable.ic_header);
                            }
                    }
                });
                break;
            case 2:
                final GroupInfo groupInfo = mData1.get(position);
                viewHolder.mTextView.setText(groupInfo.getGroupName());
                viewHolder.mImageView.setImageResource(R.drawable.ic_header);
                break;
        }

        return view;
    }

    private final class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}
