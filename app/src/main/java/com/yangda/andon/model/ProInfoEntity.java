package com.yangda.andon.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


@Data
public class ProInfoEntity implements Serializable {

    public int count;//生产数量
    public int count1;//已生成数量

    public String createBy;//
    public Date createTime;//创建时间
    public int currentstation;//(integer, optional):当前工序ID ,

    public String downSpanTime;//(string, optional):累计停线时间 ,

    public String downTime;//(string, optional):停线时间 ,

    public int flag;//(integer, optional):标志，后台用，前台忽略 ,

    public Date gonoTime;//(string, optional):继续时间 ,

    public int id;//(integer, optional):主键 ,

    public int isTouChan;//(integer, optional):是否投产 ,

    public int lineId;//(integer, optional):产线ID ,

    public String lineName;//(string, optional):产线名称 ,

    public int meterValue;///(integer, optional):节拍值 ,

    public String orderNo;//(string, optional):订单号 ,

    //params(object, optional),

    public int productId;//(integer, optional):产品ID ,

    public String productName;//(string, optional):产品名称 ,

    //remark(string, optional),

    //searchValue(string, optional),

    public Date startTime;//(string, optional):开始时间 ,

    public int status;//(integer, optional):状态1开始，2下班 3暂停 1继续 4完成 5取消 ,

    public String statusName;// (string, optional): 状态值，前端直接展示 ,

    ///updateBy(string, optional),

    //updateTime(string, optional),

    public int workshopId;//(integer, optional):车间ID ,

    public String workshopName;//(string, optional):车间名称


}
