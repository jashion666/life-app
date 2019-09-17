package app.miniprogram.application.recognition.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/9/3.
 */
@NoArgsConstructor
@Data
public class RecognitionEntity {

    private long log_id;
    private int words_result_num;
    private List<WordsResultBean> words_result;
    private String wordsResult;
    private Integer count;

    @RequiredArgsConstructor
    @Data
    public static class WordsResultBean {
        private String words;
    }
}
