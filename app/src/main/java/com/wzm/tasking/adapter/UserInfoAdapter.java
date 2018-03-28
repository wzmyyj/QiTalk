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
import cn.jpush.im.android.api.model.UserInfo;

public class UserInfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<UserInfo> mData;

    public UserInfoAdapter(Context context, List<UserInfo> Data) {
        mInflater = LayoutInflater.from(context);
        this.mData = Data;
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
    public View getView(final int position, View view, ViewGroup parent) {
        final UserInfo userInfo = mData.get(position);
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.fragment_2_item_friend, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) view.findViewById(R.id.img_user_list_item);
            viewHolder.mTextView = (TextView) view.findViewById(R.id.tv_user_list_item);
            viewHolder.mImageView1 = (ImageView) view.findViewById(R.id.img_user_list_item_1);
            viewHolder.mImageView2 = (ImageView) view.findViewById(R.id.img_user_list_item_2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String name = "";
        if (!TextUtils.isEmpty(userInfo.getNotename())) {
            name = userInfo.getNotename();
        } else if (!TextUtils.isEmpty(userInfo.getNickname())) {
            name = userInfo.getNickname();
        } else {
            name = userInfo.getUserName();
        }
        viewHolder.mTextView.setText(name);

        if (userInfo.getNoDisturb() > 0) {
            viewHolder.mImageView1.setImageResource(R.drawable.ic_disturb);
            viewHolder.mImageView1.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mImageView1.setImageBitmap(null);
            viewHolder.mImageView1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userInfo.getNoteText())) {
            if (userInfo.getNoteText().equals("1")) {
                viewHolder.mImageView2.setImageResource(R.drawable.ic_black);
                viewHolder.mImageView2.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mImageView2.setImageBitmap(null);
                viewHolder.mImageView2.setVisibility(View.GONE);
            }
        } else {
            if (userInfo.getBlacklist() > 0) {
                viewHolder.mImageView2.setImageResource(R.drawable.ic_black);
                viewHolder.mImageView2.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mImageView2.setImageBitmap(null);
                viewHolder.mImageView2.setVisibility(View.GONE);
            }
        }

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


        return view;
    }

    private final class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        ImageView mImageView1;
        ImageView mImageView2;
    }

}
