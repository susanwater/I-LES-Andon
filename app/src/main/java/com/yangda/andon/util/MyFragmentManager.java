package com.yangda.andon.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.yangda.andon.R;

/**
 * Created by Admin on 17/6/1.
 */
public class MyFragmentManager {

    public static void addFragment(FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
        transaction.add(Window.ID_ANDROID_CONTENT, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager fm, Fragment fragment, int id) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
        transaction.add(id, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager fm, Fragment fragment, String tag) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_fragment_in, R.anim.anim_fragment_out,
                R.anim.anim_fragment_close_in, R.anim.anim_fragment_close_out);
        transaction.add(Window.ID_ANDROID_CONTENT, fragment, tag);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentNotBackStack(FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentNotBackStack(FragmentManager fm, Fragment fragment, int id) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(id, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void removeFragment(FragmentManager fm, Fragment fragment) {
        if (fm == null || fragment == null)
            return;

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }

}
