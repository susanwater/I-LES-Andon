package com.yangda.andon.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.framework.net.OnGetBinListener;
import com.framework.util.DeviceHelper;
import com.framework.util.JsonUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yangda.andon.R;
import com.yangda.andon.model.BaseResponseModel;
import com.yangda.andon.model.EventBean;
import com.yangda.andon.model.ExceptionEntity;
import com.yangda.andon.model.ShowEntity;
import com.yangda.andon.net.HarNet;
import com.yangda.andon.system.Preferences;
import com.yangda.andon.ui.base.BaseFragment;
import com.yangda.andon.util.SingleTimer;
import com.yangda.andon.util.Util;

import org.codehaus.jackson.type.TypeReference;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.yangda.andon.ui.home.HelpFragment.URL;

/**
 * Created by Admin on 17/6/1.
 */
public class ProductFragment extends BaseFragment {


    private LinearLayout llt_finish;

    ShowEntity showEntity;

    TextView tv_currentOrderNo, tv_nextOrderNo, tv_pretime, tv_period, tv_meterValue_dao, tv_meterValue, tv_currentType, tv_newxType, tv_stationName, tv_stationValue;
    ImageView img_show;

    private RecyclerView mRecyclerView;

    HomeAdapter mAdapter;

    List<ShowEntity.Substeps> arr;

    TextView tv_stepName1, tv_stepValue1, tv_stepName2, tv_stepValue2, tv_stepName3, tv_stepValue3;

    boolean cancel = false;


    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //返回布局   命名 类型_页面意思
        EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_home_old, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {

        tv_currentOrderNo = (TextView) findViewById(R.id.tv_currentOrderNo);

        tv_nextOrderNo = (TextView) findViewById(R.id.tv_nextOrderNo);

        img_show = (ImageView) findViewById(R.id.img_show);

        tv_pretime = (TextView) findViewById(R.id.tv_pretime);//客户节拍
        tv_period = (TextView) findViewById(R.id.tv_period);//客户
        tv_meterValue_dao = (TextView) findViewById(R.id.tv_meterValue_dao);//客户
        tv_meterValue = (TextView) findViewById(R.id.tv_meterValue);//客户

        tv_currentType = (TextView) findViewById(R.id.tv_currentType);
        tv_newxType = (TextView) findViewById(R.id.tv_newxType);
        tv_stationName = (TextView) findViewById(R.id.tv_stationName);
        tv_stationValue = (TextView) findViewById(R.id.tv_stationValue);

        mRecyclerView = (RecyclerView) findViewById(R.id.step_recyclerview);
        //在此处修改布局排列方向
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(layoutManager);

        tv_stepName1 = (TextView) findViewById(R.id.tv_stepName1);
        tv_stepValue1 = (TextView) findViewById(R.id.tv_stepValue1);
        tv_stepName2 = (TextView) findViewById(R.id.tv_stepName2);
        tv_stepValue2 = (TextView) findViewById(R.id.tv_stepValue2);
        tv_stepName3 = (TextView) findViewById(R.id.tv_stepName3);
        tv_stepValue3 = (TextView) findViewById(R.id.tv_stepValue3);

        /**
         * 完成
         */
        llt_finish = (LinearLayout) findViewById(R.id.llt_finish);
        llt_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isFastClick()){
                    Util.showToast(getActivity(), "当前不能连续点击，请等待5秒钟后，再继续操作！！！");
                    return;
                }
                if (showEntity == null) return;
                Map<Object, Object> map = new HashMap<>();
                if (Preferences.DEBUG) {
                    map.put("equipmentId", Preferences.mac);
                } else {
                    map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
                }
                map.put("productId", 1);
                map.put("schedulingId", showEntity.getScheId());
                HarNet.getBin(ProductFragment.this.getContext(), null, map, URL + "/complete", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                        BaseResponseModel mtr = JsonUtil.fromJson(bt, new TypeReference<BaseResponseModel>() {
                        });
                        Util.showToast(getActivity(), mtr.getMsg());

                        if (0 == mtr.getCode()) {
                            if (midTime == 0 && showEntity.getLastStep()) {
                                tv_period.setBackgroundResource(R.mipmap.timebg);
                                FlashHelper.getInstance().stopFlick(tv_period);
                            }

                            if (timer != null) {
                                midTime = 0;
                                closeTimer();
                            }
                            if (mAdapter != null) {
                                mAdapter = null;
                            }
                            if (arr != null) {
                                arr = null;
                                arr = new ArrayList<ShowEntity.Substeps>();
                            }

                            if (showEntity != null) {
                                showEntity = null;
                            }


                            //////////////----------------2019/10/29 加------/////////////
                            tv_currentOrderNo.setText("");
                            tv_nextOrderNo.setText("");

                            tv_pretime.setText("");
                            tv_period.setText("");
                            tv_meterValue_dao.setText("");
                            tv_meterValue.setText("");

                            tv_currentType.setText("");
                            tv_newxType.setText("");

                            tv_stationName.setText("");
                            tv_stationValue.setText("");
                            tv_stepName1.setText("");
                            tv_stepValue1.setText("");
                            tv_stepName2.setText("");
                            tv_stepValue2.setText("");
                            tv_stepName3.setText("");
                            tv_stepValue3.setText("");
                            //////////////----------------2019/10/29 加------/////////////

                            initData();
                        }
                    }
                });

            }
        });


        initData();

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            if (LampFragment.showFlag) {
                //网络数据刷新
                //tv_meterValue_dao.setText(" ");//单步倒计时时间
                midTime = 0;
                closeTimer();//关定时器
                if (mAdapter != null) {
                    mAdapter = null;
                }
                if (arr != null) {
                    arr = null;
                    //arr = new ArrayList<ShowEntity.Substeps>();
                }

                if (showEntity != null) {
                    showEntity = null;
                }
                LampFragment.setShowFlag(false);
                initData();
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBean event) {
        Log.e("------event---进来了没有", event.getMsg());
        if ("start".equals(event.getMsg()) || "gono".equals(event.getMsg())) {
            cancel = false;
            if (showEntity != null && timer == null) {
                closeTimer();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        if (midTime > 0) {
                            midTime--;
                            long hh = midTime / 60 / 60 % 60;
                            long mm = midTime / 60 % 60;
                            long ss = midTime % 60;
                            //System.out.println("还剩" + hh + "小时" + mm + "分钟" + ss + "秒");
                            Message message = new Message();
                            Bundle b = new Bundle();
                            b.putLong("hh", hh);
                            b.putLong("mm", mm);
                            b.putLong("ss", ss);
                            message.setData(b);
                            mTestHandler.sendMessage(message);
                        }
                    }
                }, 0, 1000);
                Log.e("---------timer ", event.getMsg() + "---- 开始跑了 ----");
            }
        } else {
            cancel = true;// 点了取消
            if (timer != null) {
                if ("cancel".equals(event.getMsg())) {
                    midTime = 0;
                }
                closeTimer();//关定时器
                Log.e("---------timer ", event.getMsg() + "---- timer cancel ----");

                tv_meterValue_dao.setText(getTime(midTime));//单步倒计时时间
            }
        }


    }

    /**
     * 关闭定时器
     */
    public synchronized void closeTimer() {
        if (timer != null) {
            Log.e("---------timer ", "----关闭定时器 timer closeTimer() ----");
            timer.purge();
            timer.cancel();
            timer = null;
        }
        //    -------------2019/10/26 加-------------
       // if(tv_meterValue_dao!=null) {
            //tv_meterValue_dao.setText(getTime(0));//单步倒计时时间
        //}
        //    -------------2019/10/26 加-------------
    }


    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            long h = b.getLong("hh");
            long m = b.getLong("mm");
            long s = b.getLong("ss");
            boolean flag = false;
            String hh = null;
            if (h == 0) {
                flag = true;
                hh = "00";
            } else {
                hh = h + "";
                flag = false;
            }
            String mm = null;
            if (m == 0) {
                flag = true;
                mm = "00";
            } else {
                mm = m + "";
                flag = false;
            }
            String ss = null;
            if (s == 0) {
                flag = true;
                ss = "00";
            } else {
                ss = s + "";
                flag = false;
            }
            String time = hh + ":" + mm + ":" + ss;
            tv_meterValue_dao.setText(time);//单步倒计时时间

            if (midTime == 10 && showEntity.getLastStep()) {
                //tv_period.setBackgroundColor(0x66000000);
                tv_period.setBackgroundColor(Color.rgb(255, 0, 0));
                FlashHelper.getInstance().startFlick(tv_period);
            }
            if (midTime == 0) {
                if (showEntity==null)return;
                if (!showEntity.getLastStep()) {
                    nextStep();
                }
            }
        }
    };


    private void nextStep() {
        Map<Object, Object> map = new HashMap<>();
        if (Preferences.DEBUG) {
            map.put("equipmentId", Preferences.mac);
        } else {
            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
        }
        map.put("productId", showEntity.getScheId());
        map.put("schedulingId", showEntity.getScheId());

        HarNet.getBin(this.getContext(), null, map, URL + "/nextStep", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                BaseResponseModel mtr = JsonUtil.fromJson(bt, new TypeReference<BaseResponseModel>() {
                });
                Util.showToast(getActivity(), mtr.getMsg());
                if (0 == mtr.getCode()) {
                    initData();
                }
            }
        });
    }


    private String getTime(long midTime) {
        long h = midTime / 60 / 60 % 60;
        long m = midTime / 60 % 60;
        long s = midTime % 60;
        String hh = null;
        if (h == 0) {
            hh = "00";
        } else {
            hh = h + "";
        }
        String mm = null;
        if (m == 0) {
            mm = "00";
        } else {
            mm = m + "";
        }
        String ss = null;
        if (s == 0) {
            ss = "00";
        } else {
            ss = s + "";
        }
        String time = hh + ":" + mm + ":" + ss;
        return time;
    }


    public static long midTime = 0;


    public static Timer timer = null;
    //public static Timer timer = SingleTimer.getInstanceTimer();

    public void initData() {
        if (cancel) { // 点了取消，清空原来的数据
            tv_currentOrderNo.setText("");
            tv_nextOrderNo.setText("");

            tv_pretime.setText("");
            tv_period.setText("");
            tv_meterValue_dao.setText("");
            tv_meterValue.setText("");

            tv_currentType.setText("");
            tv_newxType.setText("");

            tv_stationName.setText("");
            tv_stationValue.setText("");
            tv_stepName1.setText("");
            tv_stepValue1.setText("");
            tv_stepName2.setText("");
            tv_stepValue2.setText("");
            tv_stepName3.setText("");
            tv_stepValue3.setText("");

            midTime = 0;
        }
        final Map<Object, Object> map = new HashMap<>();
        if (Preferences.DEBUG) {
            map.put("equipmentId", Preferences.mac);
        } else {
            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
        }
        //map.put("productId", showEntity.getScheId());
        map.put("schedulingId", 0);
        HarNet.getBin(this.getContext(), null, map, URL + "/show", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                showEntity = JSON.parseObject(bt, ShowEntity.class);
                Util.showToast(getActivity(), showEntity.getMsg());

                tv_currentOrderNo.setText("当前订单：" + showEntity.getCurrentOrderNo());
                tv_nextOrderNo.setText("下一个订单：" + showEntity.getNextOrderNo());
                ImageLoader.getInstance().displayImage(showEntity.getImageUrl(), img_show);
                long kehu = showEntity.getPretime() * 60;
                tv_pretime.setText(getTime(kehu));// 客户节拍
                long zhouqi = showEntity.getPeriod() * 60;
                tv_period.setText(getTime(zhouqi));// 周期时间
                if (showEntity.getStation() != null) {
                    long danbu = showEntity.getStation().getMeterValueFix();
                    tv_meterValue.setText(getTime(danbu));// 单步时间 后台给的是秒
                }
                //tv_meterValue_dao.setText(showEntity.getPretime() + "");// 单步倒计时时间
                if (showEntity.getStation() != null) {
                    midTime = showEntity.getStation().getMeterValue();//后台给的是秒
                }

                if ("0".equals(showEntity.getCode()) && "0".equals(showEntity.getIsHangUp())) {
                    closeTimer();//关定时器
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (midTime > 0) {
                                midTime--;
                                long hh = midTime / 60 / 60 % 60;
                                long mm = midTime / 60 % 60;
                                long ss = midTime % 60;
                                //System.out.println("还剩" + hh + "小时" + mm + "分钟" + ss + "秒");
                                Message message = new Message();
                                Bundle b = new Bundle();
                                b.putLong("hh", hh);
                                b.putLong("mm", mm);
                                b.putLong("ss", ss);
                                message.setData(b);
                                mTestHandler.sendMessage(message);
                            }
                        }
                    }, 0, 1000);
                }

                if (showEntity.getCurrentType() != null) {
                    tv_currentType.setText("当前型号 ：" + showEntity.getCurrentType());
                }
                if (showEntity.getNewxType() != null) {
                    tv_newxType.setText("下一步型号 ：" + showEntity.getNewxType().trim());
                }

                if (showEntity.getStation() != null) {
                    tv_stationName.setText(showEntity.getStation().getStationName());
                }

                if (showEntity.getStation() != null) {
                    tv_stationValue.setText(showEntity.getStation().getStationValue());
                }

                if (showEntity.getSubsteps() != null && showEntity.getSubsteps().size() > 0) {
                    arr = showEntity.getSubsteps();
                    //mAdapter = new HomeAdapter();
                    //mRecyclerView.setAdapter(mAdapter);

                    if (arr == null) return;
                    if (arr.size() < 1) return;
                    if (arr.get(0) != null) {
                        if (arr.get(0).getSubstepName() != null) {
                            tv_stepName1.setText(arr.get(0).getSubstepName());
                        }
                        if (arr.get(0).getSubstepValue() != null) {
                            tv_stepValue1.setText(arr.get(0).getSubstepValue());
                        }
                    }
                    if (arr.size() < 2) return;
                    if (arr.get(1) != null) {
                        if (arr.get(1).getSubstepName() != null) {
                            tv_stepName2.setText(arr.get(1).getSubstepName());
                        }
                        if (arr.get(1).getSubstepValue() != null) {
                            tv_stepValue2.setText(arr.get(1).getSubstepValue());
                        }
                    }
                    if (arr.size() < 3) return;
                    if (arr.get(2) != null) {
                        if (arr.get(2).getSubstepName() != null) {
                            tv_stepName3.setText(arr.get(2).getSubstepName());
                        }
                        if (arr.get(2).getSubstepValue() != null) {
                            tv_stepValue3.setText(arr.get(2).getSubstepValue());
                        }
                    }

                }
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


    class HomeAdapter extends RecyclerView.Adapter<ProductFragment.HomeAdapter.MyViewHolder> {

        //声明自定义的监听接口
        private LampFragment.OnRecyclerItemClickListener monItemClickListener;

        //提供set方法供Activity或Fragment调用
        public void setRecyclerItemClickListener(LampFragment.OnRecyclerItemClickListener listener) {
            monItemClickListener = listener;
        }

        @Override
        public ProductFragment.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ProductFragment.HomeAdapter.MyViewHolder holder = new ProductFragment.HomeAdapter.MyViewHolder(LayoutInflater.from(ProductFragment.this.getContext()).inflate(R.layout.step_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ProductFragment.HomeAdapter.MyViewHolder holder, int position) {
            ShowEntity.Substeps o = arr.get(position);
            holder.tv_stepName.setText(o.getSubstepName());
            holder.tv_stepValue.setText(o.getSubstepValue());
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_stepName, tv_stepValue;

            public MyViewHolder(final View view) {
                super(view);
                tv_stepName = (TextView) view.findViewById(R.id.tv_stepName);
                tv_stepValue = (TextView) view.findViewById(R.id.tv_stepValue);
            }
        }
    }


}
