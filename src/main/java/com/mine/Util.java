package com.mine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Util {

	public static String getMaxFromArray(String[] array){
		Double max = Double.valueOf(array[0]);
		for (int i = 0; i < array.length ; i++){
			max = Math.max(max,Math.abs((Double.valueOf(array[i]))));
		}
		return Util.getPrecisionString(max,0);
	}

	public static String getMinFromArray(String[] array){
		Double min = Double.valueOf(array[0]);
		for (int i = 0; i < array.length ; i++){
			min = Math.min(min,Math.abs((Double.valueOf(array[i]))));
		}
		return Util.getPrecisionString(min,0);
	}

	/**
	 * 四舍五入 返回String类型
	 * @param value
	 * @param precision 保留几位小数
	 * @return
	 */
	public static String getPrecisionString(String value,int precision){
		if(value == null || value == "") return "0";
		double v =Math.abs(Double.valueOf(value));
		if(precision < 0) return String.format("%.0f",v);
		return String.format("%." + precision + "f",v);
	}

	/**
	 * 四舍五入
	 * @param value
	 * @param precision
	 * @return
	 */
	public static String getPrecisionString(Double value,int precision){
		if(value == null ) return "0";
		if(precision < 0) return String.format("%.0f",value); 
		return String.format("%." + precision + "f",value); 
	}

	/**
	 * 四舍五入 返回Double类型
	 * @param value
	 * @param precision 保留几位小数
	 * @return
	 */
	public static Double getPrecisionDouble(String value,int precision){
		if(value == null || value == "") return 0D;
		double v =Math.abs(Double.valueOf(value));
		if(precision < 1)	return Double.valueOf(String.format("%.0f",v)); 
		return Double.valueOf((String.format("%." + precision + "f",v))); 
	}

	/**
	 * 获取每一行对应的元素数量，按照空格进行划分
	 * @param path
	 * @throws IOException
	 */
	public static void getLineNoteCount(String path) {
		try {
			FileInputStream fileIn = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fileIn,"GBK"));
			String line;
			String[] strs;
			int i = 1;
			while ((line = br.readLine()) != null) {
				strs = line.trim().replaceAll(" +", " ").split(" ");
				System.out.println(i++ + " :"+strs.length);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
