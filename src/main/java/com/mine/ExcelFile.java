package com.mine;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * 时间 : 2018/8/19.
 */
public class ExcelFile {

    public static String[][][] getExcelValue(String path) {
        System.out.println(path);
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            XSSFWorkbook excel = new XSSFWorkbook(in);
            return GetValueFromExcel.getValueOfAHI(excel.getSheetAt(0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  文件未找到 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  IO异常 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取楼层累计层高
     * @param path
     * @return
     */
    public static List<String> getFloorH(String path) {
        System.out.println(path);
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            XSSFWorkbook excel = new XSSFWorkbook(in);
            return GetValueFromExcel.getFloorH(excel.getSheetAt(0),1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  文件未找到 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            return null;
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  IO异常 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void insertValue(String modelPath ,String outPath, String[][][] value, List<String> txtValue){
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(modelPath);
            out = new FileOutputStream(outPath);
            XSSFWorkbook excel = new XSSFWorkbook(in);
            InsertToExcel.insert(excel,value,txtValue);
            excel.write(out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  文件未找到 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  IO异常 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}