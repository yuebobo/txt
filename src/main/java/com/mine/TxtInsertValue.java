package com.mine;

import java.io.*;

/**
 * 时间 : 2018/8/20.
 */
public class TxtInsertValue {

    public static void insertTxtSm(String basePath, String modelPath, String outPath) throws IOException {
        System.out.println("=================  小震加阻尼器   ===================================");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(modelPath), "GBK"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
        String line;

        //1.读取模板前半部分，直到元素个数为11个的时候，连续读取七行，这七行存在数组里，先不进行插入操作
        String[][] headPart = new String[7][];
        int m = 0;
        while ((line = br.readLine()) != null) {
            if (getLineArray(line).length == 11) {
                headPart[m++] = getLineArray(line);
                if (m == 7) break;
                continue;
            } else {
                writeAndFlush(bw,line);
            }
        }

        //2.取出这七组数据，并且分别获取到原始的7个txt文件里对应的值，然后修改相应的地方，插入，
        //用于存每个文件的第一行的两个数
        String[][] headTwo = new String[7][2];
        String name;
        String txtPaht;
        for (int i = 1; i <= 7; i++) {
            name = i < 6 ? "T" + i : "R" + (i - 5);
            txtPaht = basePath + "\\txt\\" + name + ".txt";
            getValueAndInsertSm(txtPaht, headTwo[i - 1], bw, headPart[i - 1], name);
        }

        //3.model后面的数据继续插入，直到 TABLE:  "CASE - MODAL HISTORY 1 - GENERAL" 对应的值需要做修改
        //OutSteps=3725   StepSize=.005
        //此处处理完后，后面的数据原样插入
        String[] last;
        int x = 0;
        while ((line = br.readLine()) != null) {
            last = getLineArray(line);
            if (last.length == 6 && x < 14) {
                line = arrayToString2(last, headTwo[x / 2]);
                writeAndFlush(bw,line);
                x++;
            } else {
                writeAndFlush(bw,line);
            }
        }
        bw.close();
        br.close();
    }

    public static void insertTxtBig(String basePath, String modelPath, String outPath) throws IOException {
        System.out.println("=============================  大震有控   ===================================");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(modelPath), "GBK"));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outPath));
        String line;

        //1.读取模板前半部分，直到元素个数为11个的时候，连续读取七行，这七行存在数组里，先不进行插入操作
        String[][] headPart = new String[7][];
        int m = 0;
        while ((line = br.readLine()) != null) {
            if (getLineArray(line).length == 11) {
                headPart[m++] = getLineArray(line);
                if (m == 7) break;
                continue;
            } else {
               writeAndFlush(bw,line);
            }
        }
        //2.取出这七组数据，并且分别获取到原始的7个txt文件里对应的值，然后修改相应的地方，插入，
        //用于存每个文件的第一行的两个数
        String[][] headTwo = new String[7][2];
        int count = 0;
        String name;
        String txtPaht;
        for (int i = 1; i <= 7; i++) {
            name = i < 6 ? "T" + i : "R" + (i - 5);
            txtPaht = basePath + "\\txt\\" + name + ".txt";
            getValueAndInsertBig(txtPaht, headTwo[i - 1], bw, headPart[i - 1], name);
        }

        //3.model后面的数据继续插入，直到 TABLE:  "CASE - MODAL HISTORY 1 - GENERAL" 对应的值需要做修改
        //OutSteps=3725   StepSize=.005
        //此处处理完后，后面的数据原样插入
        String[] last;
        int x = 0;
        while ((line = br.readLine()) != null) {
            last = getLineArray(line);
            if (last.length == 6 && x < 14) {
                line = arrayToString2(last, headTwo[x / 2]);
                writeAndFlush(bw,line);
                x++;
            } else {
                writeAndFlush(bw,line);
            }
        }
        bw.close();
        br.close();
    }


    /**
     * 处理每个文件，获取值，并把相应的值插入到目标文件中
     *
     * @param txt      源文件地址
     * @param headTwo  用于暂存每个文件的第一行的两个值
     * @param bw
     * @param headPart 每一部分的首行
     * @param name     每一个文件的标题  如 T1 T2  、、、 R1 R2
     * @throws IOException
     */
    private static void getValueAndInsertSm(String txt, String[] headTwo, BufferedWriter bw, String[] headPart, String name) {
        BufferedReader br = null;
        try {
            System.out.println();
            System.out.println(txt);
            br = new BufferedReader(new FileReader(txt));
            String line = br.readLine();
            System.out.println(line);
            String[] head = getLineArray(line);
            Double span = Double.valueOf(head[0].substring(3, head[0].length() - 1));
            //获取首行的两个值
            headTwo[0] = String.valueOf(span);
            headTwo[1] = head[1];
            System.out.println(headTwo[0] + "," + headTwo[1]);
            //插入每一部分的首行
            line = br.readLine();
            writeAndFlush(bw,arrayToString(headPart, line, "1", String.valueOf(span)));
            long current = 1;
            while ((line = br.readLine()) != null) {
                writeAndFlush(bw,getLine(name, current++, span, line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 处理每个文件，
     * 第一遍读取文件， 第一行的头两个数据，并且逐行读取，得到最大值所在的行的位置
     * 第二遍读取文件，把最大值所在位置的前后各一千个数值，插入到目标文件里
     *
     * @param txt      源文件地址
     * @param headTwo  用于暂存每个文件的第一行的两个值
     * @param bw       目标文件
     * @param headPart 每一部分的首行
     * @param name     每一个文件的标题  如 T1 T2  、、、 R1 R2
     * @throws IOException
     */
    private static void getValueAndInsertBig(String txt, String[] headTwo, BufferedWriter bw, String[] headPart, String name) {
        BufferedReader br = null;
        try {
            System.out.println();
            System.out.println(txt);
            br = new BufferedReader(new FileReader(txt));
            //获取首行的两个值
            String line = br.readLine();
            System.out.println(line);
            String[] head = getLineArray(line);
            Double span = Double.valueOf(head[0].substring(3, head[0].length() - 1));
            headTwo[0] = String.valueOf(span);
//            headTwo[1] = head[1];

            //获取最大值位置，以及总的行数
            Integer[] pos = getFirstLineValueAndBigPosition(txt);
            int position = pos[0];
            int allCount = pos[1];
            System.out.println(" 最大值位于 " + position + " 处，总的数据有" + allCount + "行");
            //1.最大位置处左侧数据不足一千，右侧超过一千，则左右都按左侧的数据量进行获取
            //2.左侧，右侧数据量都未超过一千，则按数据量少的获取
            //3.左侧超过一千，右侧为超过一千，则按右侧数据量进行获取
            //4.左右都超过一千，则左右都取一千
            int left = position - 1;
            int right = allCount - position;
            int start = 0;
            int end = 0;
            if (left < 1000 && right >= 1000) {
                start = 1;
                end = position + left;
            } else if (left < 1000 && right < 1000) {
                start = left - Math.min(left, right) + 1;
                end = position + Math.min(left, right);
            } else if (left >= 1000 && right < 1000) {
                start = position - right;
                end = position + right;
            } else if (left >= 1000 && right >= 1000) {
                start = position - 1000;
                end = position + 1000;
            } else {
                throw new RuntimeException("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  无法确定起始和终止的位置 $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            }
            headTwo[1] = String.valueOf(end - start + 1);
            System.out.println(headTwo[0] + "," + headTwo[1]);
            int index = 1;
            while ((line = br.readLine()) != null) {
                if (index++ == start) break;
            }
            //插入每一部分的首行
            writeAndFlush(bw,arrayToString(headPart, line, String.valueOf(start), String.valueOf(span)));
            //插入剩余数据
            int count = 1;
            while ((line = br.readLine()) != null) {
                writeAndFlush(bw,getLine(name, count++, span, line));
                if (index++ == end) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 确定最大值的位置，以及总数据量
     *
     * @param txt
     */
    private static Integer[] getFirstLineValueAndBigPosition(String txt) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(txt));
            String line = br.readLine();
            //逐行读取，确定最大值的位置
            int count = 0;
            int position = 1;
            Double maxValue = 0d;
            while ((line = br.readLine()) != null) {
                count++;
                if (compare(line, maxValue)){
                    position = count;
                    maxValue = Math.abs(Double.valueOf(line));
                }
            }
            return new Integer[]{position, count};
        } catch (Exception e) {
            System.out.println("获取最大值位置时出错");
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 数据比较
     *
     * @param newLine
     * @param maxValue
     * @return
     */
    private static boolean compare(String newLine, Double maxValue) {
        Double value = Math.abs(Double.valueOf(newLine));
        return value > maxValue;

    }

    private static String getLine(String name, long count, Double time, String value) {
        String v = String.valueOf(count * time);
        String t = String.valueOf(time);
        int afterLength = t.substring(t.lastIndexOf(".") + 1).length();
        v = Util.getPrecisionString(v, afterLength);
        return "   Name=" + name + "   Time=" + v + "   Value=" + value;
    }

    private static String arrayToString(String[] array, String value, String position, String interval) {
        array[9] = "Interval=" + interval;
        array[3] = "HeaderLines=" + position;
        array[2] = "Value=" + value;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append("   ").append(array[i]);
        }
        return buffer.toString();
    }

    private static String arrayToString2(String[] array, String[] value) {
        array[2] = array[2].substring(0, array[2].lastIndexOf("=")) + value[1];
        array[3] = array[3].substring(0, array[3].lastIndexOf("=")) + value[0];
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append("   ").append(array[i]);
        }
        return buffer.toString();
    }

    private static String[] getLineArray(String lineStr) {
        return lineStr.trim().replaceAll(" +", " ").split(" ");
    }

    public  static void writeAndFlush(BufferedWriter bw ,String value) throws IOException {
        bw.write(value);
        bw.newLine();
        bw.flush();
    }
}
