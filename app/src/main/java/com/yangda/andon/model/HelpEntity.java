package com.yangda.andon.model;


import java.io.Serializable;

public class HelpEntity implements Serializable {

    private int id;
    private String helpName;
    private String helpDesc;
    private String helpFilePath;


    public String getHelpName() {
        return helpName;
    }

    public void setHelpName(String helpName) {
        this.helpName = helpName;
    }

    public String getHelpDesc() {
        return helpDesc;
    }

    public void setHelpDesc(String helpDesc) {
        this.helpDesc = helpDesc;
    }

    public String getHelpFilePath() {
        return helpFilePath;
    }

    public void setHelpFilePath(String helpFilePath) {
        this.helpFilePath = helpFilePath;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HelpEntity{" +
                "id=" + id +
                ", helpName='" + helpName + '\'' +
                ", helpDesc='" + helpDesc + '\'' +
                ", helpFilePath='" + helpFilePath + '\'' +
                '}';
    }
}
