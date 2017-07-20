package com.haier.ledai.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.haier.ledai.R;
import com.haier.ledai.ui.base.BaseFragment;

import org.codehaus.jackson.map.deser.ValueInstantiators;

/**
 * Created by Admin on 17/7/20.
 */
public class TestFragment extends BaseFragment {

    Button btn;

    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //取值
        Bundle args = getArguments();
        if (args != null) {

        }
        //返回布局   命名 类型_页面意思
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void initView() {

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);

    }

    @Override
    public void doOnClick(View v) {
        super.doOnClick(v);
        int id = v.getId();
        switch (id){
            case R.id.btn:

                TestFragment2 testFragment2 = new TestFragment2();
                startFragment(testFragment2);

                break;
            default:
                break;
        }
    }
}
