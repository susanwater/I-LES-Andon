package com.haier.ledai.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.haier.ledai.R;
import com.haier.ledai.ui.base.BaseFragment;
import com.haier.ledai.util.Util;

/**
 * Created by Admin on 17/6/1.
 */
public class HomeFragment extends BaseFragment {


    Button btn;

    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //取值
        Bundle args = getArguments();
        if (args != null) {

        }
        //返回布局   命名 类型_页面意思
        return inflater.inflate(R.layout.fragment_home, container, false);
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

                MineFragment exampleFragment = new MineFragment();
                startFragmentForResult(exampleFragment,1001);

                Util.showToast(getActivity(),"this is me");

                break;

            default:
                break;

        }


    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==1001){
            Log.d("ss","");
            boolean ds = data.getBooleanExtra("ff",true);
            Util.showToast(getActivity(),ds?"ss":"ff");
            int g;
        }

        super.onFragmentResult(requestCode, resultCode, data);
    }

}
