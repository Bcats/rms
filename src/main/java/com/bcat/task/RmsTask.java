package com.bcat.task;

public class RmsTask {
    protected String cookie;
    protected int timeOut;

    private RmsTask(){}

    protected RmsTask(String cookie, int timeOut){
        this.cookie = cookie;
        this.timeOut = timeOut;
    }
}
