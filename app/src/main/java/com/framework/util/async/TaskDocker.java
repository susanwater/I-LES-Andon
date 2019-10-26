package com.framework.util.async;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Admin on 17/1/20.
 */
public class TaskDocker<T> {

    private List taskCodeList;
    private List taskList;
    private List<String> resultList;

    public TaskDocker(){

        taskCodeList = new LinkedList<String>();
        taskList = new LinkedList<T>();
        resultList = new LinkedList<String>();

    }

    public List getTaskCodeList() {
        return taskCodeList;
    }

    public void setTaskCodeList(List taskCodeList) {
        this.taskCodeList = taskCodeList;
    }

    public List getTaskList() {
        return taskList;
    }

    public void setTaskList(List taskList) {
        this.taskList = taskList;
    }

    public List<String> getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }
}
