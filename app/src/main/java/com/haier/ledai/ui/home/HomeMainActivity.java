package com.haier.ledai.ui.home;

import android.os.Bundle;

import com.haier.ledai.R;
import com.haier.ledai.ui.base.BaseActivity;
import com.haier.ledai.util.Util;


/**
 * Created by Admin on 17/7/19.
 */
public class HomeMainActivity extends BaseActivity {

    private long exitTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return ;
        }
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Util.showToast(HomeMainActivity.this,"再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {

            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {

        }
        super.onSaveInstanceState(outState);
    }
}
