package com.haier.ledai.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;

import com.haier.ledai.util.KeyBoardHelper;

import java.util.Stack;

/**
 * Created by Admin on 17/6/1.
 */
public class BaseActivity extends FragmentActivity {

    private ActivityStack activityStack;
    /***/
    private static Stack<OnBackFragmentListener> fragmentStack;
    protected String PageCode = "";

    private boolean active;

    public boolean isActive() {
        return active;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        activityStack = ActivityStack.getInstance();
        activityStack.pushActivity(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        active = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyBoardHelper.hide(this);

    }

    @Override
    protected void onDestroy() {
        if(fragmentStack!=null){
            fragmentStack.clear();
            fragmentStack=null;
        }
        activityStack.popActivity(this);
        super.onDestroy();
        active = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        getWindow().getDecorView().clearAnimation();
        if (fragmentStack != null&&fragmentManager.getBackStackEntryCount()>0) {
            OnBackFragmentListener fragmentListener = currentOnBackFragmentListener();
            if (fragmentListener == null || fragmentListener.onBack()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 删除fragment返回监听
     */
    public void removeFragmentBackListener(OnBackFragmentListener backListener) {
        if (fragmentStack == null) {
            synchronized (this) {
                fragmentStack = new Stack<OnBackFragmentListener>();
            }
        }
        if (fragmentStack != null && fragmentStack.contains(backListener)) {
            fragmentStack.remove(backListener);
        }
    }

    /**
     * 添加fragment返回监听到堆栈
     */
    public void addFragmentBackListener(OnBackFragmentListener backListener) {
        if (fragmentStack == null) {
            synchronized (this) {
                fragmentStack = new Stack<OnBackFragmentListener>();
            }
        }
        fragmentStack.push(backListener);
    }

    /**
     * 获取当前fragment返回监听器（堆栈中最后一个压入的）
     */
    private OnBackFragmentListener currentOnBackFragmentListener() {
        OnBackFragmentListener backListener = null;
        if (fragmentStack != null&&!fragmentStack.isEmpty()) {
            backListener = fragmentStack.peek();
        }
        return backListener;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
