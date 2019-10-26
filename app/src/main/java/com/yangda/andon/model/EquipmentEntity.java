package com.yangda.andon.model;


import java.io.Serializable;

public class EquipmentEntity implements Serializable {


    /**
     * 设备号 mac
     */
    public String equipmentId;

    /**
     * 当前型号
     */
    public int productId;

    /**
     * 排产ID
     */
    public int schedulingId;


    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSchedulingId() {
        return schedulingId;
    }

    public void setSchedulingId(int schedulingId) {
        this.schedulingId = schedulingId;
    }

    @Override
    public String toString() {
        return "EquipmentEntity{" +
                "equipmentId='" + equipmentId + '\'' +
                ", productId=" + productId +
                ", schedulingId=" + schedulingId +
                '}';
    }
}

