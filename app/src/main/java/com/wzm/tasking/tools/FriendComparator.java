package com.wzm.tasking.tools;

import java.util.Comparator;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by yyj on 2017/8/8 0008.
 */

public class FriendComparator implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        UserInfo a = (UserInfo) lhs;
        UserInfo b = (UserInfo) rhs;
        return (int) (b.getUserID() - a.getUserID());
    }
}