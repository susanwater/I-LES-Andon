package com.haier.ledai.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.haier.ledai.R;
import com.haier.ledai.ui.base.BaseFragment;

/**
 * Created by Admin on 17/7/20.
 */
public class TestFragment2 extends BaseFragment {

    Button button;

    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //取值
        Bundle args = getArguments();
        if (args != null) {

        }
        //返回布局   命名 类型_页面意思
        return inflater.inflate(R.layout.fragment_test2, container, false);
    }

    @Override
    public void initView() {
        button = (Button)findViewById(R.id.btn);
        button.setOnClickListener(this);
    }

    @Override
    public void doOnClick(View v) {
        super.doOnClick(v);
        int id = v.getId();
        switch (id){

            case R.id.btn:

                finishFragment();

                break;
            default:
                break;
        }
    }
}
