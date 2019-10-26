package com.yangda.andon.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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
import com.framework.widget.HRFrameLayout4Loading;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yangda.andon.R;
import com.yangda.andon.model.BaseResponseModel;
import com.yangda.andon.model.EventBean;
import com.yangda.andon.model.ExceptionEntity;
import com.yangda.andon.model.ShowEntity;
import com.yangda.andon.net.HarNet;
import com.yangda.andon.system.Preferences;
import com.yangda.andon.ui.base.BaseFragment;
import com.yangda.andon.util.Util;

import org.codehaus.jackson.type.TypeReference;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yangda.andon.ui.home.HelpFragment.URL;


/**
 * Created by Admin on 17/7/19.
 */
public class LampFragment extends BaseFragment {

    TextView tv_workshopName, tv_lineName;
    Button btn;
    HRFrameLayout4Loading loading;
    LinearLayout llt_icon;
    private long exitTime = 0;
    LinearLayout llt_cancel, llt_gono, llt_yichang;

    ImageView img_gono, img_cancel, img_start, img_wuliao;

    List<ExceptionEntity> arr = new ArrayList<>();

    // private RecyclerView mRecyclerView;

    private HomeAdapter mAdapter;

    private boolean flag = true;

    ShowEntity showEntity;

    //private boolean flagStart = true;

    //private boolean flagPause = true;

    public static boolean showFlag = false;


    public String code = null;

    public static void setShowFlag(boolean showFlag) {
        LampFragment.showFlag = showFlag;
    }

    @Override
    public View doOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //返回布局   命名 类型_页面意思
        return inflater.inflate(R.layout.fragment_lamp_old, container, false);
    }


    @Override
    public void initView() {

        //mRecyclerView = (RecyclerView) findViewById(R.id.exception_recyclerview);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //在此处修改布局排列方向
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayout.HORIZONTAL));//setItemDecoration()方法用于为RecyclerView子项的一些装饰（我个人的理解）

        tv_workshopName = (TextView) findViewById(R.id.tv_workshopName);

        tv_lineName = (TextView) findViewById(R.id.tv_lineName);

        /**
         * 退出
         */
        llt_icon = (LinearLayout) findViewById(R.id.llt_icon);
        llt_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Util.showToast(getActivity(), "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    getActivity().finish();
                    System.exit(0);
                }
            }
        });


        /**
         * 取消
         */
        img_cancel = (ImageView) findViewById(R.id.img_cancel);
        //img_cancel.setFocusable(true);
        //img_cancel.setClickable(true);
        img_cancel.setImageResource(R.mipmap.cancel_gray);
        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isFastClick()){
                    Util.showToast(getActivity(), "当前不能连续点击，请等待5秒钟后，再继续操作！！！");
                    return;
                }
                if (showEntity == null) return;
                img_cancel.setImageResource(R.mipmap.cancel);
                AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
                //bulider.setIcon(R.drawable.ic_launcher);//在title的左边显示一个图片
                bulider.setTitle("提示");
                bulider.setMessage("您确定要取消当前订单吗？");
                bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();

                        Map<Object, Object> map = new HashMap<>();
                        if (Preferences.DEBUG) {
                            map.put("equipmentId", Preferences.mac);
                        } else {
                            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
                        }
                        map.put("productId", showEntity.getScheId());
                        map.put("schedulingId", showEntity.getScheId());
                        HarNet.getBin(LampFragment.this.getContext(), null, map, URL + "/cancle", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
                            @Override
                            public void onGetBinError(String msg) {
                                Util.showToast(getActivity(), msg);
                                img_cancel.setImageResource(R.mipmap.cancel_gray);
                            }

                            @Override
                            public void onGet(int percent) {

                            }

                            @Override
                            public void onGetFinish(String bt) {
                                Log.d("请求日志：", bt);
                                img_cancel.setImageResource(R.mipmap.cancel_gray);
                                BaseResponseModel mtr = JsonUtil.fromJson(bt, new TypeReference<BaseResponseModel>() {
                                });
                                Util.showToast(getActivity(), mtr.getMsg());
                                if (0 == mtr.getCode()) {
                                    EventBus.getDefault().post(new EventBean("cancel"));
                                    //img_cancel.setImageResource(R.mipmap.cancel_gray);
                                    //img_cancel.setImageDrawable(ContextCompat.getDrawable(LampFragment.this.getContext(), R.mipmap.cancel_gray));
                                    //img_cancel.setFocusable(false);
                                    //img_cancel.setClickable(false);
                                    showFlag = true;
                                }
                            }
                        });
                    }
                });
                bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        img_cancel.setImageResource(R.mipmap.cancel_gray);
                    }
                });
                bulider.create().show();


            }
        });


        /**
         * 开始/下班
         */
        img_start = (ImageView) findViewById(R.id.img_start);
        img_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isFastClick()){
                    Util.showToast(getActivity(), "当前不能连续点击，请等待5秒钟后，再继续操作！！！");
                    return;
                }
                if (showEntity == null) {
                    return;
                }
                Map<Object, Object> map = new HashMap<>();
                if (Preferences.DEBUG) {
                    map.put("equipmentId", Preferences.mac);
                } else {
                    map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
                }
                if (showEntity != null) {
                    map.put("productId", showEntity.getScheId());
                }
                map.put("schedulingId", 0);
                String path = "";
                if (showEntity.getButtonStauts() == 0) {
                    //0是下班，点按钮为开始
                    path = "/start";
                } else {
                    path = "/end";//下班
                }
                String[] arr = path.split("/");
                final String msg = arr[1];
                HarNet.getBin(LampFragment.this.getContext(), null, map, URL + path, HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                            //利用EventBus发送
                            EventBus.getDefault().post(new EventBean(msg));
                            if (showEntity.getButtonStauts() == 0) {
                                img_start.setImageResource(R.mipmap.start);
                                img_gono.setImageResource(R.mipmap.start);
                                showEntity.setButtonStauts(1);
                            } else {
                                img_start.setImageResource(R.mipmap.fallow);
                                img_gono.setImageResource(R.mipmap.fallow);
                                showEntity.setButtonStauts(0);
                            }
                            showFlag = true;
                        }
                    }
                });
            }
        });


        /**
         * 继续/休息
         */
        img_gono = (ImageView) findViewById(R.id.img_gono);
        img_gono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isFastClick()){
                    Util.showToast(getActivity(), "当前不能连续点击，请等待5秒钟后，再继续操作！！！");
                    return;
                }
                if (showEntity == null) {
                    return;
                }
                Map<Object, Object> map = new HashMap<>();
                if (Preferences.DEBUG) {
                    map.put("equipmentId", Preferences.mac);
                } else {
                    map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
                }
                map.put("productId", showEntity.getScheId());
                map.put("schedulingId", 0);
                String path = "";
                if (showEntity.getButtonStatusOff() == 0) {
                    path = "/gono";
                } else {
                    path = "/suspend";//休息
                    map.put("currentTime", ProductFragment.midTime);
                }
                String[] arr = path.split("/");
                final String msg = arr[1];
                HarNet.getBin(LampFragment.this.getContext(), null, map, URL + path, HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                            EventBus.getDefault().post(new EventBean(msg));
                            if (showEntity.getButtonStatusOff() == 0) {
                                img_start.setImageResource(R.mipmap.start);
                                img_gono.setImageResource(R.mipmap.start);
                                showEntity.setButtonStatusOff(1);
                            } else {
                                img_start.setImageResource(R.mipmap.fallow);
                                img_gono.setImageResource(R.mipmap.fallow);
                                showEntity.setButtonStatusOff(0);
                            }
                            showFlag = true;
                        }
                    }
                });
            }
        });


        /**
         * 拉取物料信息
         */
        img_wuliao = (ImageView) findViewById(R.id.img_wuliao);
        img_wuliao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isFastClick()){
                    Util.showToast(getActivity(), "当前不能连续点击，请等待5秒钟后，再继续操作！！！");
                    return;
                }
                if (showEntity == null) {
                    return;
                }
               /* if ("3".equals(code)) {
                    Util.showToast(getActivity(), "当前订单，物料拉取完毕，不需要在拉取物料！！！");
                    return;
                }*/
                Map<Object, Object> map = new HashMap<>();
                if (Preferences.DEBUG) {
                    map.put("equipmentId", Preferences.mac);
                } else {
                    map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
                }
                HarNet.getBin(LampFragment.this.getContext(), null, map, URL + "/pullMaterial", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                           /* if ("1".equals(code)) {
                                img_wuliao.setImageResource(R.mipmap.zl_bright);
                            } else if ("2".equals(code)) {
                                img_wuliao.setImageResource(R.mipmap.zl_gray);
                            }*/
                            initMaterialData();
                        }
                    }
                });
            }
        });

        llt_yichang = (LinearLayout) findViewById(R.id.llt_yichang);


    }


    @Override
    public void onResume() {
        super.onResume();
        initData();

        initDataExceptionList();

        initMaterialData();
    }

    public void initMaterialData() {
        Map<Object, Object> map = new HashMap<>();
        if (Preferences.DEBUG) {
            map.put("equipmentId", Preferences.mac);
        } else {
            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
        }
        HarNet.getBin(LampFragment.this.getContext(), null, map, URL + "/judgeMaterial", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                if (mtr == null) {
                    return;
                }
                Util.showToast(getActivity(), mtr.getMsg());
                code = mtr.getCode() + "";
                if (1 == mtr.getCode()) {
                    img_wuliao.setImageResource(R.mipmap.zy_gray);
                } else if (2 == mtr.getCode()) {
                    img_wuliao.setImageResource(R.mipmap.zy_bright);
                } else if (3 == mtr.getCode()) {
                    img_wuliao.setImageResource(R.mipmap.zy_gray);
                }
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            //showFlag = false;

            if (arr != null) {
                arr = null;
            }
            if (showEntity != null) {
                showEntity = null;
            }
            if (code != null) {
                code = null;
            }

            initData();
            initDataExceptionList();
            initMaterialData();

        }
    }

    public void initData() {
        Map<Object, Object> map = new HashMap<>();
        if (Preferences.DEBUG) {
            map.put("equipmentId", Preferences.mac);
        } else {
            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
        }
        //map.put("productId", showEntity.getScheId());
        map.put("schedulingId", 0);
        HarNet.getBin(LampFragment.this.getContext(), null, map, URL + "/show", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
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
                if (showEntity == null) return;
                Util.showToast(getActivity(), showEntity.getMsg());
                if (showEntity.getButtonStauts() == 0) {
                    //默认下班
                    img_start.setImageResource(R.mipmap.fallow);
                } else {
                    img_start.setImageResource(R.mipmap.start);
                }
                if (showEntity.getButtonStatusOff() == 0) {
                    img_gono.setImageResource(R.mipmap.fallow);
                } else {
                    img_gono.setImageResource(R.mipmap.start);
                }
                tv_workshopName.setText(showEntity.getWorkshopName());
                tv_lineName.setText(showEntity.getLineName());
            }
        });

    }


    public void initDataExceptionList() {
        Map<Object, Object> map = new HashMap<>();
        if (Preferences.DEBUG) {
            map.put("equipmentId", Preferences.mac);
        } else {
            map.put("equipmentId", DeviceHelper.getMacAddress(getContext()));
        }
        HarNet.getBin(LampFragment.this.getContext(), null, map, URL + "/exceptionList", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
            @Override
            public void onGetBinError(String msg) {
                Util.showToast(getActivity(), msg);
            }

            @Override
            public void onGet(int percent) {

            }

            @Override
            public void onGetFinish(String bt) {
                //Util.showToast(getActivity(), mtr.getMsg());
                arr = JSON.parseArray(bt, ExceptionEntity.class);

                if (arr != null && arr.size() > 0) {
                    llt_yichang.removeAllViews();
                    for (int i = 0; i < arr.size(); i++) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        layoutParams.gravity = Gravity.CENTER;  //必须要加上这句，setMargins才会起作用，而且此句还必须在setMargins下面
                        LinearLayout l = new LinearLayout(getContext());
                        //l.setLayoutParams(layoutParams);
                        l.setOrientation(LinearLayout.VERTICAL);
                        l.setGravity(Gravity.CENTER_VERTICAL);
                        llt_yichang.addView(l, layoutParams);
                        final ImageView imageView = new ImageView(getContext());
                        LinearLayout.LayoutParams iL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        iL.gravity = Gravity.CENTER;  //必须要加上这句，setMargins才会起作用，而且此句还必须在setMargins下面
                        imageView.setLayoutParams(iL);
                        if ("0".equals(arr.get(i).getStatus())) {
                            ImageLoader.getInstance().displayImage(arr.get(i).getUpImageUrl(), imageView);
                        } else {
                            ImageLoader.getInstance().displayImage(arr.get(i).getDownImageUrl(), imageView);
                        }
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setTag(i);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                if(!Util.isFastClick()){
                                    Util.showToast(getActivity(), "当前不能连续点击，请等待5秒钟后，再继续操作！！！");
                                    return;
                                }
                                final ExceptionEntity e = arr.get((Integer) v.getTag());
                                Map<Object, Object> map = new HashMap<>();
                                map.put("exceptionId", e.getId());
                                if (Preferences.DEBUG) {
                                    map.put("lineId", Preferences.mac);
                                } else {
                                    map.put("lineId", DeviceHelper.getMacAddress(getContext()));
                                }
                                if ("0".equals(e.getStatus())) {
                                    map.put("type", "end");//
                                } else {
                                    map.put("type", "start");//
                                }
                                HarNet.getBin(LampFragment.this.getContext(), null, map, URL + "/exception", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
                                    @Override
                                    public void onGetBinError(String msg) {
                                        Util.showToast(getActivity(), msg);
                                    }

                                    @Override
                                    public void onGet(int percent) {

                                    }

                                    @Override
                                    public void onGetFinish(String bt) {
                                        BaseResponseModel mtr = JsonUtil.fromJson(bt, new TypeReference<BaseResponseModel>() {
                                        });
                                        Util.showToast(getActivity(), mtr.getMsg());
                                        if (0 == mtr.getCode()) {
                                            if ("0".equals(e.getStatus())) {
                                                ImageLoader.getInstance().displayImage(e.getDownImageUrl(), imageView);
                                                e.setStatus("1");
                                            } else {
                                                e.setStatus("0");
                                                ImageLoader.getInstance().displayImage(e.getUpImageUrl(), imageView);
                                            }
                                            arr.set((Integer) v.getTag(), e);
                                        }
                                    }
                                });


                            }
                        });

                        l.addView(imageView, iL);

                        TextView t = new TextView(getContext());
                        t.setText(arr.get(i).getName());
                        t.setTextColor(getResources().getColor(R.color.white_light));
                        t.setTextSize(18f);
                        LinearLayout.LayoutParams tL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        tL.setMargins(0, 10, 0, 0);
                        tL.gravity = Gravity.CENTER;
                        t.setLayoutParams(tL);
                        l.addView(t, tL);
                    }
                }
                /*mAdapter = new HomeAdapter();
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                    @Override
                    public void onItemClick(int Position, ExceptionEntity date) {
                        //Util.showToast(getActivity(), date.getDownImageUrl());
                    }
                });*/
            }
        });
    }


    class HomeAdapter extends RecyclerView.Adapter<LampFragment.HomeAdapter.MyViewHolder> {

        //声明自定义的监听接口
        private OnRecyclerItemClickListener monItemClickListener;

        //提供set方法供Activity或Fragment调用
        public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
            monItemClickListener = listener;
        }

        @Override
        public LampFragment.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LampFragment.HomeAdapter.MyViewHolder holder = new LampFragment.HomeAdapter.MyViewHolder(LayoutInflater.from(LampFragment.this.getContext()).inflate(R.layout.exception_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(LampFragment.HomeAdapter.MyViewHolder holder, int position) {
            ExceptionEntity o = arr.get(position);
            //holder.img_excep.setText(o.getId() + "");
            if ("0".equals(o.getStatus())) {
                ImageLoader.getInstance().displayImage(o.getUpImageUrl(), holder.img_excep);
            } else {
                ImageLoader.getInstance().displayImage(o.getDownImageUrl(), holder.img_excep);
            }
            holder.tv_excep.setText(o.getName());
        }

        @Override
        public int getItemCount() {
            return arr.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView img_excep;
            TextView tv_excep;

            public MyViewHolder(final View view) {
                super(view);
                img_excep = (ImageView) view.findViewById(R.id.img_excep);
                tv_excep = (TextView) view.findViewById(R.id.tv_excep);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (monItemClickListener != null) {
                            //monItemClickListener.onItemClick(getAdapterPosition(), arr.get(getAdapterPosition()));
                            final ExceptionEntity e = arr.get(getAdapterPosition());

                            Map<Object, Object> map = new HashMap<>();
                            map.put("exceptionId", e.getRemark());
                            if (Preferences.DEBUG) {
                                map.put("lineId", Preferences.mac);
                            } else {
                                map.put("lineId", DeviceHelper.getMacAddress(getContext()));
                            }
                            if ("0".equals(e.getStatus())) {
                                map.put("type", "start");//
                            } else {
                                map.put("type", "end");//
                            }
                            HarNet.getBin(LampFragment.this.getContext(), null, map, URL + "/exception", HarNet.LoadingType.PAGELOADING, 0, new OnGetBinListener() {
                                @Override
                                public void onGetBinError(String msg) {
                                    Util.showToast(getActivity(), msg);
                                }

                                @Override
                                public void onGet(int percent) {

                                }

                                @Override
                                public void onGetFinish(String bt) {
                                    BaseResponseModel mtr = JsonUtil.fromJson(bt, new TypeReference<BaseResponseModel>() {
                                    });
                                    Util.showToast(getActivity(), mtr.getMsg());
                                    if (0 == mtr.getCode()) {
                                        if ("0".equals(e.getStatus())) {
                                            ImageLoader.getInstance().displayImage(e.getDownImageUrl(), img_excep);
                                            e.setStatus("1");
                                        } else {
                                            e.setStatus("0");
                                            ImageLoader.getInstance().displayImage(e.getUpImageUrl(), img_excep);
                                        }
                                        /*
                                        if (flag) {
                                            ImageLoader.getInstance().displayImage(e.getDownImageUrl(), img_excep);
                                            flag =false;
                                        } else {
                                            ImageLoader.getInstance().displayImage(e.getUpImageUrl(), img_excep);
                                            flag = true;
                                        }*/
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }


    public interface OnRecyclerItemClickListener {
        void onItemClick(int Position, ExceptionEntity dataBeanList);
    }


}
