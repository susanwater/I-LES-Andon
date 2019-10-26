package com.yangda.andon.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.framework.net.OnGetBinListener;
import com.framework.util.DeviceHelper;
import com.yangda.andon.R;
import com.yangda.andon.model.HelpEntity;
import com.yangda.andon.net.HarNet;
import com.yangda.andon.system.Preferences;
import com.yangda.andon.ui.base.BaseFragment;
import com.yangda.andon.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dahai on 2019/05/25
 */
public class HelpFragment extends BaseFragment {

    //public static String prifix = "http://118.89.188.210:8000";
    //public static String prifix = "http://222.173.253.38:8181";// 生产公网
    public static String prifix = "http://192.168.1.127:8000";




    public static String URL = prifix + "/zbService";

    private List<HelpEntity> arr = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private HomeAdapter mAdapter;


    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //返回布局   命名 类型_页面意思
        return inflater.inflate(R.layout.fragment_help_old, container, false);
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.help_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayout.VERTICAL));//setItemDecoration()方法用于为RecyclerView子项的一些装饰（我个人的理解）
        intData();


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void intData() {

        Map<Object, Object> map = new HashMap<>();
        if (Preferences.DEBUG) {
            map.put("equipmentId",Preferences.mac);
        } else {
            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
        }
        HarNet.getBin(this.getContext(), null, map, URL + "/helpfile", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
            @Override
            public void onGetBinError(String msg) {

            }

            @Override
            public void onGet(int percent) {

            }

            @Override
            public void onGetFinish(String bt) {
                Log.d("请求日志：", bt);
                //BaseResponseModel mtr = JsonUtil.fromJson(bt, new TypeReference<BaseResponseModel>() {});
                arr = JSON.parseArray(bt, HelpEntity.class);
                mAdapter = new HelpFragment.HomeAdapter();
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                    @Override
                    public void onItemClick(int Position, HelpEntity date) {
                        //Util.showToast(getActivity(), date.getHelpFilePath());
                        Intent i = new Intent(HelpFragment.this.getContext(), PDFActivity.class);
                        i.putExtra("url", date.getHelpFilePath());
                        startActivity(i);
                    }
                });
            }
        });

    }


    @Override
    public void doOnClick(View v) {
        super.doOnClick(v);
        int id = v.getId();
        switch (id) {

            case R.id.btn:

                LampFragment exampleFragment = new LampFragment();
                startFragmentForResult(exampleFragment, 1001);

                int i;
                Util.showToast(getActivity(), "this is me");

                break;

            default:
                break;

        }


    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            Log.d("ss", "");
            boolean ds = data.getBooleanExtra("ff", true);
            Util.showToast(getActivity(), ds ? "ss" : "ff");
            int g;
        }
        super.onFragmentResult(requestCode, resultCode, data);
    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        //声明自定义的监听接口
        private OnRecyclerItemClickListener monItemClickListener;

        //提供set方法供Activity或Fragment调用
        public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
            monItemClickListener = listener;
        }

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.MyViewHolder holder = new HelpFragment.HomeAdapter.MyViewHolder(LayoutInflater.from(HelpFragment.this.getContext()).inflate(R.layout.help_item_old, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position) {
            HelpEntity o = arr.get(position);
            holder.tv_no.setText(o.getId() + "");
            holder.tv_desc.setText(o.getHelpName());
            holder.tv_click.setText(o.getHelpDesc());
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_no;
            TextView tv_desc;
            TextView tv_click;

            public MyViewHolder(View view) {
                super(view);
                tv_no = (TextView) view.findViewById(R.id.tv_no);
                tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                tv_click = (TextView) view.findViewById(R.id.tv_click);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (monItemClickListener != null) {
                            monItemClickListener.onItemClick(getAdapterPosition(), arr.get(getAdapterPosition()));
                        }
                    }
                });
            }
        }
    }


    public interface OnRecyclerItemClickListener {
        void onItemClick(int Position, HelpEntity dataBeanList);
    }


}
