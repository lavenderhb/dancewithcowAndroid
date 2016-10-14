package com.cn.fiveonefive.gphq.dto;

/**
 * Created by hb on 2016/3/23.
 */
public class BaseBean {
    private String symbol;
    private String name;
    private String code;



    /**
     * true 已经添加了
     * false 未添加
     */
//    private Boolean isAdd;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
