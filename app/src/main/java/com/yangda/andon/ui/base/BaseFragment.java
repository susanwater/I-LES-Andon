package com.yangda.andon.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;


import com.yangda.andon.util.CheckDoubleClick;
import com.yangda.andon.util.KeyBoardHelper;
import com.yangda.andon.util.MyFragmentManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17/6/1.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener,OnBackFragmentListener,FragmentResultCallback {

    /** 是否在onPause时关闭软键盘 */
    private boolean isHideKeyBoard;

    /** 返回监听 */
    private FragmentResultCallback callback;

    /** 打开下一个activity的请求码 */
    private int mRequestCode;

    private Bundle bundle;

    private boolean isInitialize;

    private static FragmentActivity mActivity;

    public SwipeRefreshLayout swipeRefreshLayout;


    public boolean isHideKeyBoard() {
        return isHideKeyBoard;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHideKeyBoard(true);// 默认为true
    }

    @Override
    public boolean onBack() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            mRequestCode = mBundle.getInt("requestCode", 0);
        }
        return doOnCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        isInitialize = true;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim = null;
        try {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        } catch (Resources.NotFoundException e) {
        } finally {
            if (anim != null) {
                anim.setAnimationListener(new Animation.AnimationListener() {

                    public void onAnimationStart(Animation animation) {
                        // 动画开始
                    }

                    public void onAnimationRepeat(Animation animation) {
                        // 动画循环
                    }

                    public void onAnimationEnd(Animation animation) {
                        // 动画结束
                        BaseFragment.this.onAnimationEnd();
                    }
                });
            } else {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (isInitialize()) {
                            BaseFragment.this.onAnimationEnd();
                        }
                    }
                }, 500);
            }
        }
        return anim;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onClick(View v) {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        doOnClick(v);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (CheckDoubleClick.isFastDoubleClick())
            return;
        doOnItemClick(parent, view, position, id);
    }



    /**
     *
     * 创建 view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @see [类、类#方法、类#成员]
     */
    public abstract View doOnCreateView(LayoutInflater inflater,
                                        ViewGroup container, Bundle savedInstanceState);

    /**
     * 在viewpager中第一次初始化view后时才会调用(用于初始化控件)
     *
     * @see [类、类#方法、类#成员]
     */
    public abstract void initView();

    /**
     *
     * onclick点击事件监听（子类不要需重写onclick）
     *
     * @see [类、类#方法、类#成员]
     */
    public void doOnClick(View v) {

    }

    /**
     * 根据id查找view
     *
     * @param viewId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public View findViewById(int viewId) {
        if (getView() != null) {
            return getView().findViewById(viewId);
        }
        return null;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {

    }

    /***
     * 列表项选择监听
     *
     * @param parent
     * @param view
     * @param position
     *            当前item位置
     * @param id
     *            item在listView中的相对位置
     * @see [类、类#方法、类#成员]
     */
    public void doOnItemClick(AdapterView<?> parent, View view, int position,
                              long id) {

    }

    /***
     * 动画结束执行方法
     *
     * @param
     *
     */
    public void onAnimationEnd() {

    }


    /**
     * 打开一个新的ChildFragment
     *
     * @see [类、类#方法、类#成员]
     */
    public void startChildFragment(BaseFragment fragment) {
        fragment.commitAddValues();
        MyFragmentManager.addFragment(getChildFragmentManager(), fragment);
    }

    /**
     * 打开一个新的ChildFragment,带返回值
     *
     * @param fragment
     * @param requestCode
     * @see [类、类#方法、类#成员]
     */
    public void startChildFragmentForResult(BaseFragment fragment,
                                            int requestCode) {
        fragment.setFragmentResultCallback(this);
        fragment.addValues("requestCode", requestCode);
        fragment.commitAddValues();
        //MyFragmentManager.addFragment(getFragmentManager(), fragment);

        startChildFragment(fragment);
    }

    /**
     * 打开一个新的fragment
     *
     * @see [类、类#方法、类#成员]
     */
    public void startFragment(BaseFragment fragment) {
        fragment.commitAddValues();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MyFragmentManager.addFragment(activity.getSupportFragmentManager(),
                    fragment);
        } else {
            MyFragmentManager.addFragment(getFragmentManager(), fragment);
        }
    }

    /**
     * 打开一个新的fragment,带返回值
     *
     * @param fragment
     * @param requestCode
     * @see [类、类#方法、类#成员]
     */
    public void startFragmentForResult(BaseFragment fragment, int requestCode) {
        fragment.setFragmentResultCallback(this);
        fragment.addValues("requestCode", requestCode);
        startFragment(fragment);
    }

    /**
     * 打开一个新的DialogFragment
     *
     * @see [类、类#方法、类#成员]
     */
    public void startDialogFragment(BaseDialogFragment dialogFragment) {
        if (dialogFragment == null) {
            new NullPointerException("dialogFragment is null");
        }
        dialogFragment.commitAddValues();
        dialogFragment.showAllowingStateLoss(getActivity());
    }

    /**
     * 打开一个新的DialogFragment,带返回值的
     *
     * @see [类、类#方法、类#成员]
     */
    public void startDialogFragmentForResult(
            BaseDialogFragment dialogFragment, int requestCode) {
        dialogFragment.setFragmentResultCallback(this);
        dialogFragment.addValues("requestCode", requestCode);
        startDialogFragment(dialogFragment);
    }

    /**
     * 退出当前fragment
     *
     * @see [类、类#方法、类#成员]
     */
    public void finishFragment() {
        finishFragment(0);
    }

    /***
     * 退出当前fragment
     *
     * @param resultCode
     *            返回结果码
     * @see [类、类#方法、类#成员]
     */
    public void finishFragment(int resultCode) {
        finishFragment(resultCode, null);
    }

    /**
     * 退出当前fragment
     *
     * @param resultCode
     *            返回结果码
     * @param intent
     *            返回数据
     * @see [类、类#方法、类#成员]
     */
    public void finishFragment(int resultCode, Intent intent) {
        getFragmentManager().popBackStackImmediate();
        fragmentCallbackResult(resultCode, intent);
    }

    /**
     * 退出当前fragment(返回到Activity)
     *
     * @param resultCode
     *            返回结果码
     * @param intent
     *            返回数据
     * @see [类、类#方法、类#成员]
     */
    public void finishFragmentToActivity(int resultCode, Intent intent) {
        int count = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            getFragmentManager().popBackStackImmediate();
        }
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.onActivityResult(mRequestCode, resultCode, intent);
        }
    }

    /***
     * fragemnt值回传
     *
     * @param resultCode
     * @param intent
     * @see [类、类#方法、类#成员]
     */
    public void fragmentCallbackResult(int resultCode, Intent intent) {
        if (callback != null) {
            callback.onFragmentResult(mRequestCode, resultCode, intent);
        }
    }

    /**
     * 设置副fragment回调接口
     *
     * @param callback
     * @see [类、类#方法、类#成员]
     */
    protected void setFragmentResultCallback(FragmentResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
        BaseActivity activity=(BaseActivity)getActivity();
        if(activity!=null){
            activity.addFragmentBackListener(this);
        }
        List<Fragment> fragments = null;
        if (getActivity() != null) {
            fragments = getActivity().getSupportFragmentManager()
                    .getFragments();
        } else {
            fragments = mActivity.getSupportFragmentManager().getFragments();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null)
            getView().setClickable(true);


    }

    @Override
    public void onPause() {
        super.onPause();

        if (isHideKeyBoard()) {
            KeyBoardHelper.hide(getActivity());
        }

    }




    public void setHideKeyBoard(boolean isHideKeyBoard) {
        this.isHideKeyBoard = isHideKeyBoard;
    }


    /***
     * 是否初始化
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isInitialize() {
        return isInitialize;
    }

    /**
     * 传值
     *
     * @param key
     * @param value
     *            值
     * @see [类、类#方法、类#成员]
     */
    public void addValues(String key, Object value) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (value instanceof Boolean) {
            bundle.putBoolean(key, (Boolean) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (Byte) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (Character) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (Short) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (Float) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (Double) value);
        } else if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Serializable) {
            bundle.putSerializable(key, (Serializable) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof ArrayList) {
            ArrayList list = (ArrayList) value;
            if (list.get(0) instanceof Parcelable) {
                bundle.putParcelableArrayList(key, list);
            } else if (list.get(0) instanceof CharSequence) {
                bundle.putCharSequenceArrayList(key, list);
            } else if (list.get(0) instanceof Integer) {
                bundle.putIntegerArrayList(key, list);
            } else if (list.get(0) instanceof String) {
                bundle.putStringArrayList(key, list);
            } else {
                new Exception("传的参数错误");
            }
        } else if (value instanceof SparseArray) {
            if (((SparseArray) value).get(0) instanceof Parcelable) {
                bundle.putSparseParcelableArray(key, (SparseArray) value);
            } else {
                new Exception("传的参数错误");
            }
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else {
            new Exception("传的参数错误");
        }
    }

    /**
     * 提交要接收的值
     *
     * @see [类、类#方法、类#成员]
     */
    public void commitAddValues() {
        if (bundle != null) {
            setArguments(bundle);
        }
    }

}
