package com.yangda.andon.model;

import java.io.Serializable;

/**
 * Created by Admin on 17/7/19.
 */
public class BaseResponseModel implements Serializable {
    public String msg;
    public int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseResponseModel{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
}
