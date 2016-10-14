package com.cn.fiveonefive.gphq.dto;

/**
 * Created by hb on 2016/3/28.
 */
public class TimeViewDto {
    private String time;
    private String aPrice;
    private String price;
    private long doneNum;
    private String high;
    private String low;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getaPrice() {
        return aPrice;
    }

    public void setaPrice(String aPrice) {
        this.aPrice = aPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


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

    public long getDoneNum() {
        return doneNum;
    }

    public void setDoneNum(long doneNum) {
        this.doneNum = doneNum;
    }
}
