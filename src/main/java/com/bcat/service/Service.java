package com.bcat.service;

import com.bcat.constant.CommonConstant;
import com.bcat.utils.CommonUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Service {

    protected static String receiver;

    /**
     * 判断字符串中是否含有某些key字符串。
     * @param content 判断的字符串
     * @param keys key字符串数组
     * @return 字符串中含有一个相同key的内容，即返回 true，否则返回 false 。
     */
    protected static boolean isContains(String content, String[] keys){
        for (String key : keys){
            if (content.contains(key)) return true;
        }
        return false;
    }

    /**
     * 判断某些字符串中是否含有key字符串。
     * @param contents 判断的字符串数组
     * @param key key字符串
     * @return 字符串中含有一个相同key的内容，即返回 true，否则返回 false 。
     */
    protected static boolean isContains(String[] contents, String key){
        for (String content : contents){
            if (content.contains(key)) return true;
        }
        return false;
    }

    /**
     * 读取发送标记文件，判断文件中是否含有标记字符串。
     * @param signStr 标记字符串
     * @param fileName 文件名称
     * @return 发现存在标记字符串，返回 true，<br>否则返回 false .
     */
    protected static boolean readSendSign(String signStr, String fileName){
        try {
            File file = new File(fileName);
            if(!file.exists()) return false;

            BufferedReader br = new BufferedReader(new FileReader(file.getName()));
            String rl;
            while ((rl = br.readLine()) != null){
                if (signStr.equals(rl)) {
                    br.close();
                    return true;
                }
            }
        }catch (IOException e){
            return false;
        }
        return false;
    }

    /**
     * 写入标记字符串到文件。
     * @param signStr 标记字符串
     * @param filename 文件名称
     */
    protected static void writeSendSign(String signStr, String filename){
        CommonUtil.writeStr(
                filename,
                signStr.concat("\n"),
                true
        );
    }
}
