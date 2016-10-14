package com.cn.fiveonefive.gphq.dto;

/**
 * Created by hb on 2016/9/21.
 */
public class DataBean{
    private String time;
    private float price;
    private float avg;
    private float done;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public float getDone() {
        return done;
    }

    public void setDone(float done) {
        this.done = done;
    }
}
