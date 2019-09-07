package com.yangda.andon.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HelpResponse implements Serializable {


    ArrayList<HelpEntity> arrayList = new ArrayList<>();


    public ArrayList<HelpEntity> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<HelpEntity> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public String toString() {
        return "HelpResponse{" +
                "arrayList=" + arrayList +
                '}';
    }
}
