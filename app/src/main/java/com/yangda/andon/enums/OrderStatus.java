package com.yangda.andon.enums;

public enum OrderStatus {

    start("开始"), closed("下班"), finish("完成"), pause("暂停"), gono("继续"), cancel("取消");

    private String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
