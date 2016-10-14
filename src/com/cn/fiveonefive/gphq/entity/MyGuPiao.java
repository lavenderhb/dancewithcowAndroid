package com.cn.fiveonefive.gphq.entity;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by hb on 2016/3/22.
 */
@Table(name = "mygupiao")
public class MyGuPiao extends EntityBase{
    @Column(column = "code")
    private String code;
    @Column(column = "name")
    private String name;
    @Column(column = "sympol")
    private String symbol;
//    @Column(name = "price")
//    private String price;
//    @Column(name = "percent")
//    private String percent;

//    public String getPercent() {
//        return percent;
//    }
//
//    public void setPercent(String percent) {
//        this.percent = percent;
//    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }



}
