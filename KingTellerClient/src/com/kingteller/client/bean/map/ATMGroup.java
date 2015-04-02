package com.kingteller.client.bean.map;

import java.io.File;
import java.util.List;

public class ATMGroup {
	private int id;
	private String jqbh;
	private boolean isatm;
	private List<String> piclist;
	private int status;
	private String watermark;

	public String getJqbh() {
		return jqbh;
	}

	public void setJqbh(String jqbh) {
		this.jqbh = jqbh;
	}

	public boolean isIsatm() {
		return isatm;
	}

	public void setIsatm(boolean isatm) {
		this.isatm = isatm;
	}

	public List<String> getPiclist() {
		return piclist;
	}

	public void setPiclist(List<String> piclist) {
		this.piclist = piclist;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getJson() {
		StringBuffer s = new StringBuffer();
		s.append("{");

		s.append("\"atm\":\"" + jqbh + "\",");

		s.append("\"image\":\"" + getPaths() + "\",");
		s.append("\"yiji\":\"" + 0 + "\"");
		s.append("}");
		return s.toString();
	}

	private String getPaths() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < piclist.size(); i++) {
			if (i == piclist.size() - 1) {
				File file = new File(piclist.get(i));
				s.append(file.getName());
			} else {
				File file = new File(piclist.get(i));
				s.append(file.getName() + ",");
			}
		}
		return s.toString();
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
