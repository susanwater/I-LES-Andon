package com.haier.ledai.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.framework.widget.HRFrameLayout4Loading;
import com.framework.widget.SwipeRefresh;
import com.haier.ledai.R;
import com.haier.ledai.ui.base.ListViewFragment;


/**
 * Created by Admin on 17/7/19.
 */
public class MineFragment extends ListViewFragment {

    TextView tvExample;
    Button btn;
    HRFrameLayout4Loading loading;

    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //取值
        Bundle args = getArguments();
        if (args != null) {

        }
        //返回布局   命名 类型_页面意思
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void initView() {
        btn = (Button)findViewById(R.id.btn);
        tvExample = (TextView)findViewById(R.id.tvExample);
        swipeRefresh = (SwipeRefresh)findViewById(R.id.sr);
        loading = (HRFrameLayout4Loading)findViewById(R.id.loading);

//        loading.showExceptionView();

        btn.setOnClickListener(this);
        super.initView();
    }
}
