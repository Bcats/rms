package com.bcat.utils;

import java.io.*;

public class CommonUtil {

    /**
     * 取三个数(int类型)之中的最小值
     * @return 返回最小值（int）
     * */
    public static int getMinByInts(int int1, int int2, int int3){
        return (int1 >= int2) ? Math.min(int2, int3) : Math.min(int1, int3);
    }

    public static void writeStr(String path, String content, boolean append){
        File file = new File(path);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter bufferedWritter = new BufferedWriter(new FileWriter(file.getName(), append));
            bufferedWritter.write(content);
            bufferedWritter.flush();
            bufferedWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * qu
     */
    public static String readStr(String path){
        try {
            StringBuilder sb = new StringBuilder();
            String rl;
            File file = new File(path);

            if(!file.exists()) return null;
            BufferedReader br = new BufferedReader(new FileReader(file.getName()));
            while ((rl = br.readLine()) != null){
                sb.append(rl).append("\n");
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
