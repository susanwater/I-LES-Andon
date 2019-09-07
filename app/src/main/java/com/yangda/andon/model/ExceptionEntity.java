package com.yangda.andon.model;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ExceptionEntity implements Serializable {


    String createBy;//(string, optional),

    Date createTime;//(string, optional),

    String downImageUrl;//(string, optional):异常按钮图片地址 ,

    int id;//(integer, optional):主键 ,

    String name;//(string, optional):异常类型 ,

    //params(object, optional),

    String remark;//(string, optional):异常类型 1安全 2质量 3物料 4工艺 5工具设备 ,

    String searchValue;//(string, optional),

    String status;//(string, optional):1表示按下 0表示正常 ,

    String upImageUrl;//(string, optional):正常按钮图片地址 ,

    String updateBy;//(string, optional),

    Date updateTime;//(string, optional)


}
