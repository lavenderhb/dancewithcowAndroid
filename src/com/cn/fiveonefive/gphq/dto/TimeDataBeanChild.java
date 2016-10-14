package com.cn.fiveonefive.gphq.dto;

/**
 * Created by hb on 2016/3/28.
 */
public class TimeDataBeanChild extends TimeDataBean{
    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    private String high;
    private String low;


}
