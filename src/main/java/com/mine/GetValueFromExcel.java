package com.mine;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 时间 : 2018/8/19.
 */
public class GetValueFromExcel {

    /**
     * 获取原始表里的A,H,I列的值
     */
    public static String[][][] getValueOfAHI(XSSFSheet sheet) {
        try {
            //获取楼层数
            Iterator it = sheet.iterator();
            int floor = 0;
            XSSFRow r;
            String v;
            while (it.hasNext()) {
                r = (XSSFRow) it.next();
               v = getValueFromCell(r.getCell(0));
                if (v == null || "".equals(v)) {
                    break;
                }
                floor++;
            }
            if ((floor - 3) % 4 != 0) {
                throw new RuntimeException("数据量不是四的整数倍");
            }
            ;
            //表头三行和前四行数据，
            //获得楼层数量（每层四行数据）
            floor = (floor - 3 - 4) / 4;
            //楼层》》》A,H,I三列 》》》每列四个数
            String[][][] value = new String[floor + 1][3][4];
            XSSFRow row;
            for (int i = 0; i <= floor; i++) {
                for (int j = 3; j < 7; j++) {
                    row = sheet.getRow(i * 4 + j);
                    value[i][0][j - 3] = getValueFromCell(row.getCell(0));
                    value[i][1][j - 3] = getValueFromCell(row.getCell(7));
                    value[i][2][j - 3] = getValueFromCell(row.getCell(8));
                }
            }
            printArray(value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ 原始表获取A列的值发生异常 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        }
        return null;
    }


    /**
     * 获取楼层层高获取累计层高
     * @param sheet
     * @param col
     * @return
     */
    public static List<String> getFloorH(XSSFSheet sheet, int col){
        Iterator it = sheet.iterator();
        it.next();
        List<String> list = new ArrayList<>();
        String h;
        XSSFRow row;
        XSSFCell cell;
        while(it.hasNext()) {
            row = (XSSFRow) it.next();
            cell = row.getCell(col);
            h = getValueFromCell(cell);
            list.add(h);
        }
        return list;
    }

    private static void printArray(String[][][] array) {
        System.out.println("原始表数据");
        System.out.println("======================================");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    System.out.print(array[i][j][k] + ",  ");
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("======================================");
    }

    public static String getValueFromCell(XSSFCell cell){
        String v;
        try {
            v = cell.getStringCellValue();
        } catch (Exception e) {
            try {
                v = Util.getPrecisionString(cell.getNumericCellValue(),0);
            } catch (Exception e1) {
                v = cell.getRawValue();
            }
        }
        return  v;
    }

}
