package com.wzm.tasking.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class MyPagerAdapter extends PagerAdapter {
    private String[] titles;
    private List<View> viewList;

    public MyPagerAdapter(List<View> viewList, String[] titles) {
        this.viewList = viewList;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
