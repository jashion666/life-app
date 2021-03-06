package com.app;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        ListNode resultNode = new ListNode(2);
        ListNode ddd = resultNode.next = new ListNode(4);
        ddd.next = new ListNode(3);

        ListNode resultNode1 = new ListNode(5);
        ListNode ccc = resultNode1.next = new ListNode(6);
        ccc.next = new ListNode(4);


        System.out.println(addTwoNumbers(resultNode, resultNode1));
    }

    private int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        outer:
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    result[0] = i;
                    result[1] = j;
                    break outer;
                }
            }
        }

        return result;
    }

    private ListNode addTwoNumbers(ListNode l1, ListNode l2) {


        String str1 = getListNodeString(l1);
        String str2 = getListNodeString(l2);
        String result = new StringBuffer().append(Integer.valueOf(str1) + Integer.valueOf(str2)).reverse().toString();

        ListNode resultNode = null;
        // 失败
        for (int i = 0; i < result.length(); i++) {
            if (resultNode == null) {
                resultNode = new ListNode(Integer.valueOf(String.valueOf(result.charAt(i))));
            } else {
                resultNode.next = new ListNode(Integer.valueOf(String.valueOf(result.charAt(i))));
            }

        }

        return resultNode;
    }

    private String getListNodeString(ListNode l1) {
        StringBuilder result = new StringBuilder();
        while (l1 != null) {
            result.append(l1.val);
            l1 = l1.next;
        }
        return result.reverse().toString();
    }

    @Test
    public void test6() throws IOException {
//        String ret = Files.readString(Path.of("E:\\project\\vba\\文件传送\\temp文件做成\\0472201102_F8211802.csv"), StandardCharsets.UTF_8);
        String ret = Files.readString(Path.of("E:\\project\\vba\\文件传送\\SC_伝送サービス\\F8211802A1770103149.csv"), StandardCharsets.UTF_8);
        List<String> codeList = getCodeList();

        for (String str : codeList) {
            String ret1 = ret.replaceAll("1770103149", str);
            String path = "\\\\007-D1024\\forwkh\\v$\\SharedFolder\\MIRACLE_D\\SENDDATA\\" + str + "\\" + "201905\\" + "F8211905.csv";
            File file = new File(path);
            if (!file.isDirectory()) {
                file.getParentFile().mkdirs();
            }
            Files.write(Paths.get(path), ret1.getBytes());
        }

    }

    @Test
    public void test7() throws IOException {
        List<String> codeList = Files.readAllLines(Path.of("E:\\project\\vba\\文件传送\\最新的js.js"), StandardCharsets.UTF_8);
        File file = new File("D:\\webdownload\\js\\filesummary1.js");
        if (!file.isDirectory()) {
            file.getParentFile().mkdirs();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : codeList) {
            if (str.contains("//")) {
                continue;
            }
            stringBuilder.append(str).append("\r\n");

        }
        Files.write(Paths.get("D:\\webdownload\\js\\filesummary1.js"), stringBuilder.toString().getBytes());
    }

    private List<String> getCodeList() throws IOException {
        return Files.readAllLines(Path.of("E:\\project\\vba\\文件传送\\temp文件做成\\code.txt"), StandardCharsets.UTF_8);
    }

    @Test
    public void test8() throws IOException {
        List<Long> longs = new ArrayList<>();
        longs.add(1L);
        longs.add(2L);
        Long [] ss = longs.toArray(new Long[0]);
        Long [] sss = new Long[]{1l};
        System.out.println(ss);
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}
