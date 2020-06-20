package com.bcat.utils;

import javax.swing.*;

public class LogUtil {

    private static final String STATUS_INFO = "info";
    private static final String STATUS_ERROR = "error";
    private final JTextArea logTextArea;

    public LogUtil(JTextArea logTextArea){
        this.logTextArea = logTextArea;
    }

    public void info(String detail){
        setLogTextArea(STATUS_INFO, detail);
    }

    public void error(String detail){
        setLogTextArea(STATUS_ERROR, detail);
    }

    private void setLogTextArea(String status, String detail){
        String log = " [ " + status + " ]  "+ TimeUtil.getDayTime() + detail + "\n";
        logTextArea.append(log);
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }

}
