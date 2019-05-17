package com.app.appclient;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AppClientApplicationTests {
    private String[] fileNameArray = {"enji_20180301_1.csv",
            "enji_20180301_2.csv",
            "enji_20180301_1.csv",
            "tokoen_20180301_1.csv",
            "tokoen_20180301_2.csv",
            "tokoen_20180301_1.csv",
            "kintaikyuyo_20180301_1.txt",
            "kintaikyuyo_20180301_2.txt",
            "kintaikyuyo_20180301_1.txt",
            "sisetu_kaisyo_20180301_1.csv",
            "sisetu_kaisyo_20180301_2.csv",
            "sisetu_kaisyo_20180301_1.csv",
            "01-kojin_20180301.txt",
            "02-syuro_20180301.txt",
            "03-jusyo_20180301.txt",
            "04-sikaku_20180301.txt",
            "05-idou_20180301.txt",
            "06-syokureki_20180301.txt",
            "07-kyusyoku_20180301.txt",
            "08-syogu2_20180301.txt",
            "01-kojin_20180301.txt",
            "02-syuro_20180301.txt",
            "03-jusyo_20180301.txt",
            "04-sikaku_20180301.txt",
            "05-idou_20180301.txt",
            "06-syokureki_20180301.txt",
            "07-kyusyoku_20180301.txt",
            "08-syogu2_20180301.txt",
            "09-patosyuro_20180301.txt",
            "10-jitansyuro_20180301.txt",
            "10-jitansyuro_20180301_2.txt",
            "01-kojin_20180301.txt",
            "02-syuro_20180301.txt",
            "03-jusyo_20180301.txt",
            "04-sikaku_20180301.csv",
            "05-idou_20180301.txt",
            "06-syokureki_20180301.txt",
            "07-kyusyoku_20180301.csv",
            "08-syogu2_20180301.txt",
            "09-patosyuro_20180301.txt",
            "10-jitansyuro_20180301.txt",
            "01-kojin_20180301.txt",
            "02-syuro_20180301.csv",
            "03-jusyo_20180301.txt",
            "04-sikaku_20180301.txt",
            "05-idou_20180301.txt",
            "06-syokureki_20180301.txt",
            "07-kyusyoku_20180301.txt",
            "08-syogu2_20180301.txt",
            "09-patosyuro_20180301.csv",
            "10-jitansyuro_20180301.txt",
            "01-kojin_20180301.txt",
            "02-syuro_20180301.txt",
            "03-jusyo_20180301.txt",
            "04-sikakua_20180301.txt",
            "05-idou_20180301.txt",
            "06-syokurekia_20180301.txt",
            "07-kyusyoku_20180301.txt",
            "08-syogu2_20180301.txt",
            "09-patosyuro_20180301.txt",
            "10-jitansyuro_20180301.txt",
            "01-kojina_20180301.txt",
            "02-syuro_20180301.txt",
            "03-jusyo_20180301.txt",
            "04-sikaku_20180301.txt",
            "05-idou_20180301.txt",
            "06-syokureki_20180301.txt",
            "07-kyusyoku_20180301.txt",
            "08-syogu2_20180301.txt",
            "09-patosyuro_20180301.txt",
            "10-jitansyuroa_20180301.txt",
            "01-kojin_20180301.txt",
            "02-syuro_20180301.txt",
            "03-jusyo_20180301.txt",
            "04-sikaku_20180301.txt",
            "05-idou_20180301.txt",
            "06-syokureki_20180301.txt",
            "07-kyusyoku_20180301.txt",
            "08-syogu2_20180301.txt",
            "09-patosyuro_20180301.txt",
            "10-jitansyuro_20180301.txt",

    };

    @Test
    public void contextLoads() {
        String path = "D:\\开发文档\\幼儿园\\file";
        Arrays.asList(this.fileNameArray).forEach(fileName -> {
            File file = new File(path + "\\" + fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Map<String, Boolean> map = new HashMap<>();
        map.put("1", false);
        Arrays.asList(this.fileNameArray).forEach(fileName -> {
            if (map.containsKey(fileName)) {
                map.put(fileName, false);
            }
        });


    }

    @Test
    public void resign() {
        int health = 100;
        while (true) {
            System.out.println("辛辛苦苦的工作");

            if (workOvertime(12)) {
                health--;
            }

            if (health < 50) {
                System.out.println("尊敬的领导，告辞！");
                break;
            }
        }
    }

    private boolean workOvertime(Integer workTime) {
        return workTime > 8;
    }

}
