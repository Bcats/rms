package com.bcat.service;

public class ZabbixService extends Service{

    /**
     * 判断字符串中是否包含关键字 “忽略”
     * @return if <code> true </code> host or question string contain "忽略" .<br>
     *         else <code> false </code>
     */
    protected static boolean hasNotIgnore(String host, String question){
        return isContains(new String[]{host, question}, "忽略");
    }

}
