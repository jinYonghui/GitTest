package com.kingteller.client.bean.account;

import java.io.Serializable;

import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.framework.utils.MD5Utils;

/**
 * 待办的Bean
 * @author 王定波
 *
 */
public class WaitDoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3749194426487382532L;
	private String actCode;
	private String beanName;
	private String assignOrderDate;
	private String busCode;
	private String busId;
	private String content;
	private String curUserCode;
	private int currentPage;
	private String flag;
	private String flowCode;
	private String nav;
	private String title;
	private int pageSize;
	private String toDoTaskUrl;
	private int totalCount;
	private String flowTitle; //流程任务标题
	private String senderName ;   //任务创建者姓名
	private String sendTime ;   //任务创建时间
	private String backFlag;
	
	public String getActCode() {
		return actCode;
	}
	public void setActCode(String actCode) {
		this.actCode = actCode;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getAssignOrderDate() {
		return assignOrderDate;
	}
	public void setAssignOrderDate(String assignOrderDate) {
		this.assignOrderDate = assignOrderDate;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getToDoTaskUrl() {
		return toDoTaskUrl;
	}
	public void setToDoTaskUrl(String toDoTaskUrl) {
		this.toDoTaskUrl = toDoTaskUrl;
	}
	public String getNav() {
		return nav;
	}
	public void setNav(String nav) {
		this.nav = nav;
	}
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCurUserCode() {
		return curUserCode;
	}
	public void setCurUserCode(String curUserCode) {
		this.curUserCode = curUserCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public String getFlowTitle() {
		return flowTitle;
	}
	public void setFlowTitle(String flowTitle) {
		this.flowTitle = flowTitle;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getBackFlag() {
		return backFlag;
	}
	public void setBackFlag(String backFlag) {
		this.backFlag = backFlag;
	}
	/**
	 * 唯一标识Md5
	 * @return
	 */
	public String getOnlyId()
	{
		StringBuffer sb=new StringBuffer(CommonUtils.ToString(getBusId()));
		sb.append(CommonUtils.ToString(getSendTime()));
		sb.append(CommonUtils.ToString(getSenderName()));
		sb.append(CommonUtils.ToString(getTitle()));
		sb.append(CommonUtils.ToString(getToDoTaskUrl()));
		sb.append(CommonUtils.ToString(getBeanName()));
		return MD5Utils.toMD5(sb.toString());
	}

}
