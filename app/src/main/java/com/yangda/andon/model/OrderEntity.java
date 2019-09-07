package com.yangda.andon.model;

import com.yangda.andon.enums.OrderStatus;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderEntity implements Serializable {

    private int no;
    private String orderNo;
    private String productNO;
    private int orderNum;
    private int finishNum;
    //private OrderStatus OrderStatus;
    public String statusName;

}
