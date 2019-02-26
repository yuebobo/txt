package com.mine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhongxiang on
 *
 * @author : lzx
 * 时间 : 2018/8/19.
 */
public class GetValueFromTxt {



    /**
     * 获取WMASS.txt文件的 层高
     * 元素个数为15的行
     *
     * @param txtPath
     * @return
     */
    public static List<String> getValueForFloorHeigh(String txtPath) {
        System.out.println();
        System.out.println("获取累计层高：");
        String line;
        List<String> floorH = new ArrayList<>();
        String[] strs;
        FileInputStream fileIn = null;
        BufferedReader br = null;
        try {
            fileIn = new FileInputStream(txtPath);
            br = new BufferedReader(new InputStreamReader(fileIn, "GBK"));
            while ((line = br.readLine()) != null) {
                strs = line.trim().replaceAll(" +", " ").split(" ");
                if (strs.length == 15) {
                    floorH.add(Util.getPrecisionString(Double.valueOf(strs[14])*1000,0));
                    System.out.println(strs[14]);
                } else if (floorH.size() > 0) {
                    return floorH;
                }
            }
            System.out.println("$$$$$$$$$$$$" + txtPath + "WMASS.txt文件里的   累计高度  数据没有获取到");
            return null;
        } catch (FileNotFoundException e) {
            System.out.println("$$$$$$$$$$$$" + txtPath + "没有找到");
            return null;
        } catch (IOException e) {
            System.out.println("$$$$$$$$$$$$" + txtPath + "处理异常");
            return null;
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
