package com.mine;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * 时间 : 2018/8/19.
 */
public class Main {

    private static void workExcel(String path, String basePath) {
        //获取excel里的值
        String[][][] value = ExcelFile.getExcelValue(path);
        if (value == null) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  excel 没有从原始表里获得值 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            return;
        }

        String floorHPath = basePath + "\\model\\floorH.xlsx";
        List<String> h = ExcelFile.getFloorH(floorHPath);
        if (h == null || h.size() < 1) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$获取楼层累计层高失败");
        }

        if (value.length - 1 != h.size()){
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$楼层数不一致");
        }
        String modelPath = basePath + "\\model\\model.xlsx";
        String outPath = basePath + "\\out\\out" + System.currentTimeMillis() + ".xlsx";
        ExcelFile.insertValue(modelPath, outPath, value, h);
    }

    /**
     * 汇总七个记事本
     *
     * @param basePath
     * @throws IOException
     */
    private static void workTxt(String basePath) throws IOException {
        String modelPath1 = basePath + "\\model\\model.txt";
        String modelPath2 = modelPath1;
        String outPaht1 = basePath + "\\out\\小震out" + System.currentTimeMillis() + ".txt";
        String outPaht2 = basePath + "\\out\\大震out" + System.currentTimeMillis() + ".txt";
        TxtInsertValue.insertTxtSm(basePath, modelPath1, outPaht1);
        TxtInsertValue.insertTxtBig(basePath, modelPath2, outPaht2);
    }


    public static void work(String path) throws IOException {
        String basePath;
        System.out.println(path);
        if (path.contains("\\")) {
            basePath = path.substring(0, path.lastIndexOf("\\"));
        } else if (path.contains("/")) {
            basePath = path.substring(0, path.lastIndexOf("/"));
        } else {
            throw new FileNotFoundException();
        }
        System.out.println("基本路径：" + basePath);
        File file = new File(basePath);
        File[] fileList = file.listFiles();
        System.out.println("基本路径下的文件有：");
        for (File file2 : fileList) {
            System.out.println(file2.getPath());
        }
        //创建文件夹用于保存输出
        File outDir = new File(basePath + "\\out");
        if (!outDir.exists()) {//如果文件夹不存在
            outDir.mkdir();//创建文件夹
        }


        System.out.println("===================================  处理excel ============================================");
        workExcel(path, basePath);
        System.out.println("===================================  处理excel ============================================");
        System.out.println();
        System.out.println("===================================  处理txt ============================================");
        workTxt(basePath);
        System.out.println("===================================  处理txt ============================================");

    }


}
