package com.cn.fiveonefive.gphq.dto;

/**
 * Created by hb on 2016/3/25.
 */
public class KBean {
    private double open;// 开盘价
    private double high;// 最高价
    private double low;// 最低价
    private double close;// 收盘价
    private double volume;//成交额
    private String date;// 日期，如：2013-09-18

    public KBean() {
        super();
    }

    public KBean(double open, double high, double low, double close, String date) {
        super();
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
