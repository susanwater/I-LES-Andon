package com.haier.ledai.ui.base;

import android.support.v4.widget.SwipeRefreshLayout;

import com.haier.ledai.util.Util;

import framework.widget.SwipeRefresh;

/**
 * Created by Admin on 17/7/19.
 */
public abstract class ListViewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,SwipeRefresh.OnLoadListener{


    public SwipeRefresh swipeRefresh;

    @Override
    public void initView() {

        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setOnLoadListener(this);
    }

    @Override
    public void onLoad() {

        Util.showToast(getActivity(),"up");

    }

    @Override
    public void onRefresh() {
        Util.showToast(getActivity(),"down");
    }
}
