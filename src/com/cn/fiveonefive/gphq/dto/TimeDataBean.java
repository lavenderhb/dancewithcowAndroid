package com.cn.fiveonefive.gphq.dto;

import java.util.List;

public class TimeDataBean {
	private String count;
	private String symbol;
	private List data;
	private String name;
	private String yestclose;
	private String lastVolume;
	private String date;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYestclose() {
		return yestclose;
	}

	public void setYestclose(String yestclose) {
		this.yestclose = yestclose;
	}

	public String getLastVolume() {
		return lastVolume;
	}

	public void setLastVolume(String lastVolume) {
		this.lastVolume = lastVolume;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
