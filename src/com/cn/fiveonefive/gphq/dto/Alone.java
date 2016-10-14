package com.cn.fiveonefive.gphq.dto;

/**
 * Created by hb on 2016/3/24.
 */
public class Alone {
    private String price;
    private String name;
    private String upPrice;
    private String percent;
    private String symbol;
    private String code;

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(String upPrice) {
        this.upPrice = upPrice;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
