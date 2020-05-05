package com.bcat.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JudgeUtil {

    public static List<List<String>> checkSend(List<List<String>> dataArrays)  {
        List<List<String>> sendDataArrays = new ArrayList<>();

        for (List<String> dataArray : dataArrays){
            String id = dataArray.get(0);
            String dateTime = dataArray.get(5);
            String status = dataArray.get(6);
            String advice = dataArray.get(8);
            String idStr = id.concat("\n");

            if (!isInOneHour(dateTime) || isInProcess(status) || isNoAdvice(advice) || isReceived(id)) {
                continue;
            }
            sendDataArrays.add(dataArray);
            writeSendId(idStr);
        }

        return sendDataArrays;
    }

    private static boolean isInOneHour(String dateTime){
        Date alarmDate = TimeUtil.toDate(dateTime, "yyyy-MM-dd HH:ss:mm");
        return alarmDate != null && TimeUtil.isInOneHour(alarmDate);
    }

    private static boolean isReceived(String id){
        try {
            File file =new File("received.log");
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedReader in = new BufferedReader(new FileReader("received.log"));
            String rl;
            while ((rl = in.readLine()) != null){
                if (id.equals(rl)) {
                    in.close();
                    return true;
                }
            }
        }catch (IOException e){
            return false;
        }
        return false;
    }

    private static boolean isInProcess(String status){
        if (status.equals("处理中") | status.equals("处理完毕")){
            return true;
        }
        return false;
    }

    private static boolean isNoAdvice(String advice){
        return advice.equals("否");
    }

    private static void writeSendId(String idStr){

        File file =new File("received.log");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter bufferedWritter = new BufferedWriter(new FileWriter("received.log",true));
            bufferedWritter.write(idStr);
            bufferedWritter.flush();
            bufferedWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
