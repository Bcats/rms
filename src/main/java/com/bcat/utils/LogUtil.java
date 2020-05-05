package com.bcat.utils;

import javax.swing.*;

public class LogUtil {

    private JTextArea logTextArea;

    public LogUtil(JTextArea logTextArea){
        this.logTextArea = logTextArea;
    }

    public void info(String info){
        String log = logTextArea.getText() + "\n [ info ]  "+ TimeUtil.getDayTime() + info;
        logTextArea.setText(log);
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }

    public void error(String error){
        String log = logTextArea.getText() + "\n [ error ]  "+ TimeUtil.getDayTime() + error;
        logTextArea.setText(log);
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }

}
