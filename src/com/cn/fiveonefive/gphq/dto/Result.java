package com.cn.fiveonefive.gphq.dto;

/**
 * Created by hb on 2016/3/25.
 */
public class Result {
    public int getResultCode() {
        return resultCode;
    }

    private int resultCode;
    private String resultData;



    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }
}
