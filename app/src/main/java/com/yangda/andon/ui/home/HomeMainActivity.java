package com.yangda.andon.ui.home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.yangda.andon.R;
import com.yangda.andon.ui.base.BaseActivity;
import com.yangda.andon.util.Util;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Admin on 17/7/19.
 */
public class HomeMainActivity extends BaseActivity {

    //要切换显示的四个Fragment
    private ProductFragment productFragment;
    private LampFragment dynamicFragment;
    private OrderFragment messageFragment;
    private HelpFragment personFragment;

    private int currentId = R.id.tv_main;// 当前选中id,默认是主页

    private TextView tvMain, tvDynamic, tvMessage, tvPerson;//底部四个TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);

        tvMain = (TextView) findViewById(R.id.tv_main);
        tvMain.setSelected(true);//首页默认选中
        tvDynamic = (TextView) findViewById(R.id.tv_dynamic);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        tvPerson = (TextView) findViewById(R.id.tv_person);

        /**
         * 默认加载首页
         */
        productFragment = new ProductFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, productFragment).commit();

        tvMain.setOnClickListener(tabClickListener);
        tvDynamic.setOnClickListener(tabClickListener);
        tvMessage.setOnClickListener(tabClickListener);
        tvPerson.setOnClickListener(tabClickListener);



    }


    private View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() != currentId) {//如果当前选中跟上次选中的一样,不需要处理
                changeSelect(v.getId());//改变图标跟文字颜色的选中
                changeFragment(v.getId());//fragment的切换
                currentId = v.getId();//设置选中id
            }
        }
    };

    /**
     * 改变fragment的显示
     *
     * @param resId
     */
    private void changeFragment(int resId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务

        hideFragments(transaction);//隐藏所有fragment
        if(resId==R.id.tv_main){//主页
            if(productFragment==null){//如果为空先添加进来.不为空直接显示
                productFragment = new ProductFragment();
                transaction.add(R.id.main_container,productFragment);
            }else {
                transaction.show(productFragment);
            }
        }else if(resId==R.id.tv_dynamic){//动态
            if(dynamicFragment==null){
                dynamicFragment = new LampFragment();
                transaction.add(R.id.main_container,dynamicFragment);
            }else {
                transaction.show(dynamicFragment);
            }
        }else if(resId==R.id.tv_message){//消息中心
            if(messageFragment==null){
                messageFragment = new OrderFragment();
                transaction.add(R.id.main_container,messageFragment);
            }else {
                transaction.show(messageFragment);
            }
        }else if(resId==R.id.tv_person){//我
            if(personFragment==null){
                personFragment = new HelpFragment();
                transaction.add(R.id.main_container,personFragment);
            }else {
                transaction.show(personFragment);
            }
        }
        transaction.commit();//一定要记得提交事务
    }

    /**
     * 显示之前隐藏所有fragment
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction){
        if (productFragment != null)//不为空才隐藏,如果不判断第一次会有空指针异常
            transaction.hide(productFragment);
        if (dynamicFragment != null)
            transaction.hide(dynamicFragment);
        if (messageFragment != null)
            transaction.hide(messageFragment);
        if (personFragment != null)
            transaction.hide(personFragment);
    }

    /**
     * 改变TextView选中颜色
     * @param resId
     */
    private void changeSelect(int resId) {
        tvMain.setSelected(false);
        tvDynamic.setSelected(false);
        tvMessage.setSelected(false);
        tvPerson.setSelected(false);

        switch (resId) {
            case R.id.tv_main:
                tvMain.setSelected(true);
                break;
            case R.id.tv_dynamic:
                tvDynamic.setSelected(true);
                break;
            case R.id.tv_message:
                tvMessage.setSelected(true);
                break;
            case R.id.tv_person:
                tvPerson.setSelected(true);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }
}
