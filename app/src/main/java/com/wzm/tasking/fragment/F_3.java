package com.wzm.tasking.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wzm.tasking.R;
import com.wzm.tasking.activity.Web;


/**
 * Created by yyj on 2017/3/21 0021.
 */

public class F_3 extends Fragment {
    private LinearLayout mLayout;
    private Button mButton;
    private Button mButton1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        mLayout = (LinearLayout) view.findViewById(R.id.ll_fg3);
        mButton = (Button) view.findViewById(R.id.bt_fg3_developer);
        mButton1 = (Button) view.findViewById(R.id.bt_fg3_web);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    private void initData() {


    }

    private void initListener() {
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "unfinished", Toast.LENGTH_SHORT).show();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager manager;
                PackageInfo info = null;
                manager = getActivity().getPackageManager();
                try {
                    info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {

                }
                Toast.makeText(getActivity(), "Version: " + info.versionName + "\n" +
                                "Package: " + info.packageName + "\n"+
                        "Already latest", Toast.LENGTH_SHORT).show();
            }
        });
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("url", "http://m.is11409268.icoc.me/index.jsp");
                i.setClass(getActivity(), Web.class);
                startActivity(i);
            }
        });

    }

}


