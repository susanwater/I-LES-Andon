package com.yangda.andon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import lombok.Data;


@Data
public class ShowEntity implements Serializable {


    int ajaxTime;// (integer, optional): 页面请求timer接口时间 ,
    String code;// (string, optional): 返回成功标志 ,
    int count;// (integer, optional): 总数量 ,
    int count1;// (integer, optional): 当前数 ,
    String currentOrderNo;// (string, optional): 当前订单号 ,
    String currentType;// (string, optional): 当前型号 ,
    String imageUrl;// (string, optional): 当前型号图片地址 ,
    String lineName;// (string, optional): 产线名称 ,
    String msg;// (string, optional): 内容 ,
    String newxType;// (string, optional): 下一型号 ,
    String nextOrderNo;// (string, optional): 下一个订单号 ,
    int period;// (integer, optional): 周期时间 ,
    int pretime;// (integer, optional): 客户节拍 ,
    int productId;// (integer, optional): 产品ID ,
    int scheId;// (integer, optional): 当前生产的主键，用于下一步传值 ,
    Station station;//(工序, optional):工序 ,
    ArrayList<Substeps> substeps = new ArrayList();//(Array[分步], optional):分步集合
    String workshopName;// (string, optional): 车间名称

    String isHangUp;//": "string", 是否要倒计时 0 要倒记时

    Boolean lastStep;//最后一步  标志

    int buttonStauts;//0是下班
    int materButtonStatus;//物料状态
    int buttonStatusOff;//0是暂停

    @Data
    public class Station implements Serializable {

        String createBy;// (string, optional),
        Date createTime;// (string, optional): 创建时间 ,
        int currentCount;// (integer, optional): 当前值 ,
        String downTimer;// (string, optional): 前端忽略 ,
        int flag;// (integer, optional): 是否投产1投产，0没有投产 ,
        int isSmile;// (integer, optional): 前端忽略 ,
        int lineId;// (integer, optional): 所属车间ID ,
        int meterId;// (integer, optional): 节拍ID ,
        String meterName;// (string, optional): 节拍名称 ,
        int meterValue;// (integer, optional): 节拍值 ,
        int meterValueFix;// 新节拍时间
        String modifyTime;// (string, optional): 修改时间 ,
        //params (object, optional),
        //pretime (integer, optional): 前端忽略 ,
        ProductScheduling productScheduling;//(排产明细, optional),
        String remark;// (string, optional),
        String searchValue;// (string, optional),
        String showMv;// (string, optional): 前端忽略 ,
        int sort;// (integer, optional): 工序排序，用于下一工序 ,
        int stationId;// (integer, optional): 工序ID ,
        String stationName;// (string, optional): 工序名称 ,
        String stationValue;// (string, optional): 工序值 ,
        int targertCount;// (integer, optional): 目标值 ,
        String updateBy;// (string, optional),
        String updateTime;// (string, optional),
        int ydCount;// (integer, optional): 前端忽略

    }


    @Data
    public class ProductScheduling implements Serializable {

        int count;// (integer, optional): 生产数量 ,
        int count1;// (integer, optional): 已生成数量 ,
        String createBy;// (string, optional),
        Date createTime;// (string, optional): 创建时间 ,
        int currentstation;// (integer, optional): 当前工序ID ,
        Date downSpanTime;// (string, optional): 累计停线时间 ,
        Date downTime;// (string, optional): 停线时间 ,
        int flag;// (integer, optional): 标志，后台用，前台忽略 ,
        Date gonoTime;// (string, optional): 继续时间 ,
        int id;// (integer, optional): 主键 ,
        int isTouChan;// (integer, optional): 是否投产 ,
        int lineId;// (integer, optional): 产线ID ,
        String lineName;// (string, optional): 产线名称 ,
        int meterValue;// (integer, optional): 节拍值 ,
        String orderNo;// (string, optional): 订单号 ,
        //params (object, optional),
        int productId;// (integer, optional): 产品ID ,
        String productName;// (string, optional): 产品名称 ,
        String remark;// (string, optional),
        String searchValue;// (string, optional),
        Date startTime;// (string, optional): 开始时间 ,
        int status;// (integer, optional): 状态1开始，2下班 3 暂停 1 继续 4 完成 5取消 ,
        String updateBy;// (string, optional),
        Date updateTime;// (string, optional),
        int workshopId;// (integer, optional): 车间ID ,
        String workshopName;// (string, optional): 车间名称

    }

    @Data
    public class Substeps implements Serializable {

        String createBy;// (string, optional),
        Date createTime;// (string, optional),
        Date modifyTime;// (string, optional),
        //params (object, optional),
        String remark;// (string, optional),
        String searchValue;// (string, optional),
        int stationId;// (integer, optional): 所属工序 ,
        String stationName;// (string, optional): 所属工序名称 ,
        int substepId;// (integer, optional): 分步主键 ,
        String substepName;//(string, optional): 分步名称 ,
        String substepValue;// (string, optional): 分步值 ,
        String updateBy;// (string, optional),
        Date updateTime;// (string, optional)

    }


}
