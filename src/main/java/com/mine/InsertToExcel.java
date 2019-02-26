package com.mine;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * 时间 : 2018/8/19.
 */
public class InsertToExcel {

    public static void insert(XSSFWorkbook workBook, String[][][] value,List<String> txtValue) {
        dealSheet0(workBook.getSheetAt(0), value);
        dealSheet2(workBook.getSheetAt(2), value);
        dealSheet3(workBook.getSheetAt(3), value,txtValue);
    }


    private static void dealSheet0(XSSFSheet sheet, String[][][] value) {

        XSSFRow row;
        XSSFCell cell;
        String str;
        String[] valueRow;
        //x方向
        //头四行
        int i = 3;
        for (int k = 0; k < 4; k++, i++) {
            row = sheet.createRow(i);
            valueRow = new String[]{"GX01", value[0][0][k], "0.25", "0", "0", "0", "0", "0"};
            dealRow(row, valueRow);
        }

        //中间行
        for (int j = 0; j < value.length - 2; j++) {
            str = (j + 1) < 10 ? "GX0" + (j + 1) : "GX" + (j + 1);
            for (int k = 0; k < 4; k++, i++) {
                row = sheet.createRow(i);
                valueRow = new String[]{str, value[j + 1][0][k], "-0.25", "0", "0", "0", "0", "0"};
                dealRow(row, valueRow);
            }
            str = (j + 2) < 10 ? "GX0" + (j + 2) : "GX" + (j + 2);
            for (int k = 0; k < 4; k++, i++) {
                row = sheet.createRow(i);
                valueRow = new String[]{str, value[j + 1][0][k], "0.25", "0", "0", "0", "0", "0"};
                dealRow(row, valueRow);
            }
        }

        //尾四行
        str = (value.length - 1) < 10 ? "GX0" + (value.length - 1) : "GX" + (value.length - 1);
        for (int k = 0; k < 4; k++, i++) {
            row = sheet.createRow(i);
            valueRow = new String[]{str, value[value.length-1][0][k], "-0.25", "0", "0", "0", "0", "0"};
            dealRow(row, valueRow);
        }


        //y方向
        //头四行
        for (int k = 0; k < 4; k++, i++) {
            row = sheet.createRow(i);
            valueRow = new String[]{"GY01", value[0][0][k], "0", "0.25", "0", "0", "0", "0"};
            dealRow(row, valueRow);
        }

        //中间行
        for (int j = 0; j < value.length - 2; j++) {
            str = (j + 1) < 10 ? "GY0" + (j + 1) : "GY" + (j + 1);
            for (int k = 0; k < 4; k++, i++) {
                row = sheet.createRow(i);
                valueRow = new String[]{str, value[j + 1][0][k], "0", "-0.25", "0", "0", "0", "0"};
                dealRow(row, valueRow);
            }
            str = (j + 2) < 10 ? "GY0" + (j + 2) : "GY" + (j + 2);
            for (int k = 0; k < 4; k++, i++) {
                row = sheet.createRow(i);
                valueRow = new String[]{str, value[j + 1][0][k], "0", "0.25", "0", "0", "0", "0"};
                dealRow(row, valueRow);
            }
        }

        //尾四行
        str = (value.length - 1) < 10 ? "GY0" + (value.length - 1) : "GY" + (value.length - 1);
        for (int k = 0; k < 4; k++, i++) {
            row = sheet.createRow(i);
            valueRow = new String[]{str, value[value.length-1][0][k], "0", "-0.25", "0", "0", "0", "0"};
            dealRow(row, valueRow);
        }
    }

    private static void dealSheet2(XSSFSheet sheet, String[][][] value) {
        //获取首行里的值，除了A列，其他列的值一样，A列按楼层增加
        XSSFRow row = sheet.getRow(3);
        String[] oldValue = new String[14];
        for (int i = 0; i < 14; i++) {
            oldValue[i] = GetValueFromExcel.getValueFromCell(row.getCell(i));
        }
        int floor = value.length - 1;
        String str;
        for (int i = 4; i < floor + 3; i++) {
            str = (i - 2) < 10 ? "CF0" + (i - 2) : "CF" + (i - 2);
            row = sheet.createRow(i);
            oldValue[0] = str;
            dealRow(row, oldValue);
        }
    }

    private static void dealSheet3(XSSFSheet sheet, String[][][] value, List<String> txtValue) {
        int floor = value.length - 1;
        if (floor != txtValue.size()){
            System.out.println("记事本里的楼层数与原始表里的楼层数不一致");
        }
        //A列与楼层相关  CF01，CF02， 每层四行数据
        //B列恒为1
        //C列1,2,3,4,  重复
        //D列为 前两个数  value[0][1]里面的四个数最小值-1000，后两个数为 value[0][1]里面的四个数最大值+1000 ,这样四个数重复
        String str1 = Util.getPrecisionString(Double.valueOf(Util.getMinFromArray(value[0][1])) - 1000, 0);
        String str2 = Util.getPrecisionString(Double.valueOf(Util.getMaxFromArray(value[0][1])) + 1000, 0);
        //E列为  第一个和第四个数为 value[0][2]里面的四个数最大值+1000，第二个和第三个数为 value[0][2]里面的四个数最小值-1000，四个数重复
        String str3 = Util.getPrecisionString(Double.valueOf(Util.getMinFromArray(value[0][2])) - 1000, 0);
        String str4 = Util.getPrecisionString(Double.valueOf(Util.getMaxFromArray(value[0][2])) + 1000, 0);
        //F列为  前四个数为 10 ，后边的值从记事本wmass里的累计高度部分获取，（最后一个不取）：

        String[] v1 = new String[]{"","1","1",str1,str4,"10"};
        String[] v2 = new String[]{"","1","2",str1,str3,"10"};
        String[] v3 = new String[]{"","1","3",str2,str3,"10"};
        String[] v4 = new String[]{"","1","4",str2,str4,"10"};
        String[][] v = new String[][]{v1,v2,v3,v4};
        XSSFRow row ;
        //头四行
        String str = "CF01";
        int f = 3;
        for (int i = 0; i < 4; i++,f++) {
           row = sheet.createRow(f);
            v[i][0] = str;
           dealRow(row,v[i]);
        }
        for (int i = 2 ; i <= floor ; i++){
            str = i < 10 ? "CF0"+i : "CF"+i ;
            for (int k = 0; k < 4; k++,f++){
                row = sheet.createRow(f);
                v[k][0] = str;
                v[k][5] = txtValue.get(i-2);
                dealRow(row,v[k]);
            }
        }
    }

    private static void dealRow(XSSFRow row, String[] value) {
        for (int i = 0; i < value.length; i++) {
            dealCell(row.createCell(i), value[i]);
        }
    }

    private static void dealCell(XSSFCell cell, String value) {
        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(value);
    }

}
