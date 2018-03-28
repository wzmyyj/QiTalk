package com.wzm.tasking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzm.tasking.R;
import com.wzm.tasking.view.GroupView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.GroupInfo;

public class GroupInfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<GroupInfo> mData;

    public GroupInfoAdapter(Context context, List<GroupInfo> Data) {
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
        final GroupInfo groupInfo = mData.get(position);
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.fragment_2_item_group, parent,
                    false);
            viewHolder = new GroupInfoAdapter.ViewHolder();
            viewHolder.mImageView = (GroupView) view.findViewById(R.id.img_group_list_item);
            viewHolder.mTextView = (TextView) view.findViewById(R.id.tv_group_list_item);
            viewHolder.mImageView1 = (ImageView) view.findViewById(R.id.img_group_list_item_1);
            viewHolder.mImageView2 = (ImageView) view.findViewById(R.id.img_group_list_item_2);
            view.setTag(viewHolder);
        } else {
            viewHolder = (GroupInfoAdapter.ViewHolder) view.getTag();
        }

        viewHolder.mTextView.setText(groupInfo.getGroupName());

        if (groupInfo.getNoDisturb() > 0) {
            viewHolder.mImageView1.setImageResource(R.drawable.ic_disturb);
            viewHolder.mImageView1.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mImageView1.setImageBitmap(null);
            viewHolder.mImageView1.setVisibility(View.GONE);
        }
        if (groupInfo.isGroupBlocked() > 0) {
            viewHolder.mImageView2.setImageResource(R.drawable.ic_black);
            viewHolder.mImageView2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mImageView2.setImageBitmap(null);
            viewHolder.mImageView2.setVisibility(View.GONE);
        }
        //avatar
        viewHolder.mImageView.setTag(position);
        final List<Bitmap> bitmapList = new ArrayList<Bitmap>();
        final Bitmap mBitmap = BitmapFactory.decodeResource(mInflater.getContext().
                getResources(), R.drawable.ic_header);
        final int m = groupInfo.getGroupMembers().size();
        if (m == 0) {
            bitmapList.add(mBitmap);
            viewHolder.mImageView.setImageBitmaps(bitmapList);
        } else {

            final ViewHolder finalViewHolder = viewHolder;

            for (int i = 0; i < m && i < 5; i++) {
                bitmapList.add(null);
            }
            for (int i = 0; i < m && i < 5; i++) {
                final int p = i;
                groupInfo.getGroupMembers().get(p).getAvatarBitmap(new GetAvatarBitmapCallback() {
                    @Override
                    public void gotResult(int i, String s, Bitmap bitmap) {
                        if (bitmap != null) {
                            bitmapList.set(p, bitmap);
                        } else {
                            bitmapList.set(p, mBitmap);
                        }
                        try {
                            if (finalViewHolder.mImageView.getTag() != null
                                    && finalViewHolder.mImageView.getTag().equals(position))
                                finalViewHolder.mImageView.setImageBitmaps(bitmapList);
                        } catch (Exception e) {

                        }

                    }
                });
            }
        }


        return view;
    }

    private final class ViewHolder {
        GroupView mImageView;
        TextView mTextView;
        ImageView mImageView1;
        ImageView mImageView2;
    }

}
