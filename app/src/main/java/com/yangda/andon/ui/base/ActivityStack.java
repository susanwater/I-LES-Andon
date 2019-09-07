package com.yangda.andon.ui.base;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

/**
 * Created by padad on 17/6/1.
 */
public class ActivityStack {

    private static final ActivityStack instance = new ActivityStack();

    private static ArrayList<FragmentActivity> mActivityStack;

    public static ActivityStack getInstance() {
        return instance;
    }

    private ActivityStack() {
        mActivityStack = new ArrayList<FragmentActivity>();
    }

    public void pushActivity(FragmentActivity activity) {
        mActivityStack.add(activity);
    }

    public void popActivity(FragmentActivity activity) {
        if (mActivityStack.contains(activity)) {
            mActivityStack.remove(activity);
        }
    }

    public void popActivity(Class<? extends FragmentActivity> c) {
        for (FragmentActivity a : mActivityStack) {
            if (a.getClass() == c) {
                a.finish();
            }
        }
    }

    public FragmentActivity getTopActivity() {
        if (mActivityStack.size() == 0)
            return null;

        return mActivityStack.get(mActivityStack.size() - 1);
    }

    public void singleTop(Class<?> c) {
        for (FragmentActivity a : mActivityStack) {
            if (a.getClass() != c) {
                a.finish();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends FragmentActivity> T findActivity(Class<T> c) {
        for (FragmentActivity a : mActivityStack) {
            if (a.getClass().equals(c))
                return (T) a;
        }
        return null;
    }

    public void exit() {
        for (FragmentActivity a : mActivityStack) {
            a.finish();
        }
        if (mActivityStack != null) {
            mActivityStack.clear();
        }
        System.exit(0);
    }

}
