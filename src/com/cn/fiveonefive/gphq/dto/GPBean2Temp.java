package com.cn.fiveonefive.gphq.dto;


import java.util.List;

/**
 * Created by hb on 2016/10/9.
 */
public class GPBean2Temp {
    private List Comment;
    private List Value;

    public List getValue() {
        return Value;
    }

    public void setValue(List value) {
        Value = value;
    }

    public List getComment() {
        return Comment;
    }

    public void setComment(List comment) {
        Comment = comment;
    }
}
