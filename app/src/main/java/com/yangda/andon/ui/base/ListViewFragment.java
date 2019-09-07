package com.yangda.andon.ui.base;

import android.support.v4.widget.SwipeRefreshLayout;

import com.framework.widget.SwipeRefresh;
import com.yangda.andon.util.Util;


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
