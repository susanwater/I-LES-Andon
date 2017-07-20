package com.haier.ledai.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.haier.ledai.R;
import com.haier.ledai.ui.base.BaseFragment;


/**
 * Created by Admin on 17/6/2.
 */
public class TabsFragment extends BaseFragment implements TabHost.OnTabChangeListener {

    public static final String TAB_HOME     = "home";
    public static final String TAB_MINE     = "mine";

    private static final String[] tableTags={TAB_HOME,TAB_MINE};
    private View mRoot;
    private TabHost mTabHost;
    private BaseFragment[] frgaments=new BaseFragment[2];

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int currentTab=0;
        if(savedInstanceState!=null){
            currentTab=savedInstanceState.getInt("currentTab");
            FragmentManager fm = getFragmentManager();
            if (fm != null){
                for(int i=0;i<frgaments.length;i++){
                    frgaments[i]=(BaseFragment)fm.findFragmentByTag(tableTags[i]);
                }
            }
        }
        mRoot = inflater.inflate(R.layout.fragment_tabs, container, false);
        mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
        setupTabs();



        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(currentTab);
        updateTab(tableTags[currentTab]);

        return mRoot;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentTab", mTabHost.getCurrentTab());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onTabChanged(String tabId) {
        updateTab(tabId);
    }

    public void setNewestTab(int current_tab) {
        mTabHost.setCurrentTab(current_tab);
    }

    private void setupTabs() {
        mTabHost.setup(); // important!
        mTabHost.addTab(newTab(tableTags[0], R.string.home_name, R.drawable.tab_selector_home));
        mTabHost.addTab(newTab(tableTags[1], R.string.home_mine, R.drawable.tab_selector_mine));
        for (int i=0; i<mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setOnClickListener(clickHandler);
        }
    }

    private View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int toTab = 0;
            final String tag = (String)v.getTag();
            if (tag.equals(tableTags[0])) {
                toTab = 0;

            }
            if (tag.equals(tableTags[1])) {
                toTab = 1;

            }
            if (toTab == mTabHost.getCurrentTab())
                return;
            mTabHost.setCurrentTab(toTab);
        }
    };

    private TabHost.TabSpec newTab(String tag, int labelId, int drawableid) {
        final View indicator = LayoutInflater.from(getActivity()).inflate(
                R.layout.tab_item,
                (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        indicator.setTag(tag);

        final DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int tab_width = dm.widthPixels/2;

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)indicator.getLayoutParams();
        params.width = tab_width;
        indicator.setLayoutParams(params);
        ((TextView) indicator.findViewById(R.id.text)).setText(labelId);
        ((ImageView)indicator.findViewById(R.id.img_of_tab)).setImageResource(drawableid);

        final TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(R.id.layout_table);
        return tabSpec;
    }

    private void updateTab(String tabId) {
        final FragmentManager fm = getFragmentManager();
        if (fm == null)
            return;
        FragmentTransaction transaction=fm.beginTransaction();
        BaseFragment fragment = (BaseFragment)fm.findFragmentByTag(tabId);
        if (fragment == null) {
            final int index = mTabHost.getCurrentTab();
            if (index == 0) {
                fragment = new HomeFragment();
            } else if (index == 1) {
                fragment = new MineFragment();

            } else if (index == 2) {

            }
            else if (index == 3) {

            }
            else
                return;
            frgaments[index]=fragment;
            transaction
                    .add(android.R.id.tabcontent, fragment, tabId)
                    .commitAllowingStateLoss();
        }
        else {
            for(BaseFragment mFragment:frgaments){
                if(mFragment==null){
                    continue;
                }
                if(mFragment == fragment){

                    transaction.show(mFragment);
                }else{
                    transaction.hide(mFragment);
                }
            }
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return null;
    }

    @Override
    public void initView()
    {

    }

}
