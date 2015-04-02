package com.kingteller.client.bean.map;

import com.kingteller.framework.annotation.sqlite.Id;
import com.kingteller.framework.annotation.sqlite.Table;

@Table(name = "atm_pic_upload")
public class ATM {
	
	@Id(column = "id")
	private int id;
	private String jqbh;
	private String namejson;
	private String filejson;
	private String watermark;

	public String getJqbh() {
		return jqbh;
	}

	public void setJqbh(String jqbh) {
		this.jqbh = jqbh;
	}

	public String getNamejson() {
		return namejson;
	}

	public void setNamejson(String namejson) {
		this.namejson = namejson;
	}

	public String getFilejson() {
		return filejson;
	}

	public void setFilejson(String filejson) {
		this.filejson = filejson;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWatermark() {
		return watermark;
	}

	public void setWatermark(String watermark) {
		this.watermark = watermark;
	}
}
