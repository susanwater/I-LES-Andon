package com.yangda.andon.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.framework.net.OnGetBinListener;
import com.framework.util.DeviceHelper;
import com.framework.util.JsonUtil;
import com.yangda.andon.R;
import com.yangda.andon.enums.OrderStatus;
import com.yangda.andon.model.BaseResponseModel;
import com.yangda.andon.model.HelpEntity;
import com.yangda.andon.model.OrderEntity;
import com.yangda.andon.model.ProInfoEntity;
import com.yangda.andon.net.HarNet;
import com.yangda.andon.system.Preferences;
import com.yangda.andon.ui.base.BaseFragment;
import com.yangda.andon.util.Util;

import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yangda.andon.ui.home.HelpFragment.URL;


/**
 * Created by dahai on 2019/05/25
 */
public class OrderFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private List<OrderEntity> mDatas;
    private List<ProInfoEntity> arr;
    private HomeAdapter mAdapter;

    TextView tv_today_num;

    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //返回布局   命名 类型_页面意思
        return inflater.inflate(R.layout.fragment_order_old, container, false);
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayout.VERTICAL));//setItemDecoration()方法用于为RecyclerView子项的一些装饰（我个人的理解）
        tv_today_num = (TextView) findViewById(R.id.tv_today_num);
        initData();

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            //网络数据刷新
            initData();
        }

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

    protected void initData() {
        Map<Object, Object> map = new HashMap<>();
        if (Preferences.DEBUG) {
            map.put("equipmentId", Preferences.mac);
        } else {
            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
        }
        //map.put("productId", 1);
        map.put("schedulingId", 0);
        HarNet.getBin(OrderFragment.this.getContext(), null, map, URL + "/proInfo", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
            @Override
            public void onGetBinError(String msg) {
                Util.showToast(getActivity(), msg);
            }

            @Override
            public void onGet(int percent) {

            }

            @Override
            public void onGetFinish(String bt) {
                Log.d("请求日志：", bt);
                arr = JSON.parseArray(bt, ProInfoEntity.class);
                mDatas = new ArrayList<OrderEntity>();

                //public int status;//(integer, optional):状态1开始，2下班 3暂停 1继续 4完成 5取消 ,

                int completeNum = 0;
                for (int i = 0; i < arr.size(); i++) {
                    OrderEntity o = new OrderEntity();
                    o.setNo(arr.get(i).getId());
                    o.setOrderNo(arr.get(i).getOrderNo());
                    o.setProductNO(arr.get(i).getProductName());
                    o.setOrderNum(arr.get(i).getCount());
                    o.setFinishNum(arr.get(i).getCount1());
                    completeNum = completeNum + arr.get(i).getCount1();
                    /*if (arr.get(i).getStatus() == 1) {
                        o.setOrderStatus(OrderStatus.start);
                    } else if (arr.get(i).getStatus() == 2) {
                        o.setOrderStatus(OrderStatus.closed);
                    } else if (arr.get(i).getStatus() == 3) {
                        o.setOrderStatus(OrderStatus.pause);
                    } else if (arr.get(i).getStatus() == 4) {
                        o.setOrderStatus(OrderStatus.gono);
                    } else if (arr.get(i).getStatus() == 5) {
                        o.setOrderStatus(OrderStatus.finish);
                    } else {
                        o.setOrderStatus(OrderStatus.cancel);
                    }*/
                    o.setStatusName(arr.get(i).getStatusName());

                    mDatas.add(o);
                }

                mAdapter = new HomeAdapter();
                mRecyclerView.setAdapter(mAdapter);


                tv_today_num.setText(completeNum + "");
            }
        });


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

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(OrderFragment.this.getContext()).inflate(R.layout.recyclerview_item_old, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            OrderEntity o = mDatas.get(position);

            holder.tv_no.setText(o.getNo() + "");
            holder.tv_orderNo.setText(o.getOrderNo());

            holder.tv_productNO.setText(o.getProductNO());
            holder.tv_orderNum.setText(o.getOrderNum() + "");

            holder.tv_finishNum.setText(o.getFinishNum() + "");

            /*if (OrderStatus.start.equals(o.getOrderStatus())) {
                holder.tv_OrderStatus.setText("开始");
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#c4e3fd"));
            } else if (OrderStatus.closed.equals(o.getOrderStatus())) {
                holder.tv_OrderStatus.setText("下班");
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#c4e3fd"));
            } else if (OrderStatus.pause.equals(o.getOrderStatus())) {
                holder.tv_OrderStatus.setText("暂停");
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#99c20c"));
            } else if (OrderStatus.gono.equals(o.getOrderStatus())) {
                holder.tv_OrderStatus.setText("继续");
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#cd3441"));
            } else if (OrderStatus.finish.equals(o.getOrderStatus())) {
                holder.tv_OrderStatus.setText("完成");
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#c4e3fd"));
            } else {
                holder.tv_OrderStatus.setText("取消");
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#99c20c"));
            }*/

            holder.tv_OrderStatus.setText(o.getStatusName());
            if ("开始".equals(o.getStatusName())) {
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#c4e3fd"));
            } else if ("下班".equals(o.getStatusName())) {
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#99c20c"));
            } else if ("暂停".equals(o.getStatusName())) {
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#99c20c"));
            } else if ("完成".equals(o.getStatusName())) {
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#09c10c"));
            } else {
                holder.tv_OrderStatus.setTextColor(Color.parseColor("#cd3441"));
            }

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends ViewHolder {

            TextView tv_no;
            TextView tv_orderNo;
            TextView tv_productNO;
            TextView tv_orderNum;
            TextView tv_finishNum;
            TextView tv_OrderStatus;

            public MyViewHolder(View view) {
                super(view);
                tv_no = (TextView) view.findViewById(R.id.tv_no);
                tv_orderNo = (TextView) view.findViewById(R.id.tv_orderNo);
                tv_productNO = (TextView) view.findViewById(R.id.tv_productNO);
                tv_orderNum = (TextView) view.findViewById(R.id.tv_orderNum);
                tv_finishNum = (TextView) view.findViewById(R.id.tv_finishNum);
                tv_OrderStatus = (TextView) view.findViewById(R.id.tv_OrderStatus);
            }
        }
    }


}
