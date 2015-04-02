package com.kingteller.client.bean.workorder;


import com.kingteller.framework.annotation.sqlite.Id;
import com.kingteller.framework.annotation.sqlite.Table;

/**
 * 数据库保存报告
 * 
 * @author 王定波
 * 
 */
@Table(name = "reportv2")
public class Report {
	@Id(column = "orderId")
	private String orderId; // 报告id
	private String reportType;// 报告类型
	private String reportData;// 报告数据
	private int isSuccess;//服务器保存是否成功 0失败 1成功

	
	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}


}
