package com.wzm.tasking.tools;

import java.util.Comparator;

import cn.jpush.im.android.api.model.GroupInfo;

/**
 * Created by yyj on 2017/8/8 0008.
 */

public class GroupComparator implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        GroupInfo a = (GroupInfo) lhs;
        GroupInfo b = (GroupInfo) rhs;
        if (b.getGroupID() > a.getGroupID()) {
            return 1;
        } else {
            return -1;
        }
    }
}