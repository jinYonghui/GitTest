package com.kingteller.client.activity.workorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.kingteller.R;
import com.kingteller.client.bean.workorder.AttachmentBean;
import com.kingteller.client.bean.workorder.CostInfoBean;
import com.kingteller.client.bean.workorder.FreeData;
import com.kingteller.client.bean.workorder.OtherMatterReportBean;
import com.kingteller.client.bean.workorder.PickInfoBean;
import com.kingteller.client.bean.workorder.Report;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.CommonSelcetUtils;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.view.FeeGroupView;
import com.kingteller.client.view.FeeRPGroupView;
import com.kingteller.client.view.KingTellerEditText;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class OtherMatterReportActivity extends BaseReportActivity {

	private OtherMatterReportBean otherMatterReport;
	private List<AttachmentBean> attachList;// 附件
	private List<CostInfoBean> costInfoList;// 费用

	private KingTellerEditText orderNo;
	private KingTellerEditText workDateStr;
	private KingTellerEditText areaName;
	private KingTellerEditText machineCode;
	private KingTellerEditText wdName;
	private KingTellerEditText bankName;
	private KingTellerEditText workOrgName;
	private KingTellerEditText recordUserName;
	private KingTellerEditText assignDateStr;
	private KingTellerEditText confirmDateStr;
	private KingTellerEditText beginDateStr;
	private KingTellerEditText endDateStr;
	private KingTellerEditText linkName;
	private KingTellerEditText linkPhone;
	private KingTellerEditText workType;
	private KingTellerEditText workAddress;
	private KingTellerEditText troubleRemark;
	private KingTellerEditText maintainRemark;
	private KingTellerEditText assignNames;
	private KingTellerEditText remark;
	private LinearLayout layout_from;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_other_report);
		initUI();
		initData();
		
	}

	@Override
	public void initUI() {
		// TODO Auto-generated method stub
		super.initUI();

		orderNo = (KingTellerEditText) findViewById(R.id.orderNo);
		workDateStr = (KingTellerEditText) findViewById(R.id.workDateStr);
		areaName = (KingTellerEditText) findViewById(R.id.areaName);
		machineCode = (KingTellerEditText) findViewById(R.id.machineCode);
		wdName = (KingTellerEditText) findViewById(R.id.wdName);
		bankName = (KingTellerEditText) findViewById(R.id.bankName);
		workOrgName = (KingTellerEditText) findViewById(R.id.workOrgName);
		recordUserName = (KingTellerEditText) findViewById(R.id.recordUserName);
		assignDateStr = (KingTellerEditText) findViewById(R.id.assignDateStr);
		confirmDateStr = (KingTellerEditText) findViewById(R.id.confirmDateStr);
		beginDateStr = (KingTellerEditText) findViewById(R.id.beginDateStr);
		endDateStr = (KingTellerEditText) findViewById(R.id.endDateStr);
		linkName = (KingTellerEditText) findViewById(R.id.linkName);
		linkPhone = (KingTellerEditText) findViewById(R.id.linkPhone);
		workType = (KingTellerEditText) findViewById(R.id.workType);
		workAddress = (KingTellerEditText) findViewById(R.id.workAddress);
		troubleRemark = (KingTellerEditText) findViewById(R.id.troubleRemark);
		maintainRemark = (KingTellerEditText) findViewById(R.id.maintainRemark);
		assignNames = (KingTellerEditText) findViewById(R.id.assignNames);
		remark = (KingTellerEditText) findViewById(R.id.remark);

		layout_from = (LinearLayout) findViewById(R.id.layout_form);

		dataKey = "otherMatter";
		
		if(CommonUtils.isEmpty(isCheckout)){	
			switch (optType) {
			case OPT_ADUIT:	
				title_title.setText("其他事物报告审核");
				break;
			default:
				title_title.setText("其他事物报告填写");
				break;
			}
		}else{
			title_title.setText("其他事物报告");
		}

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		workType.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.workTypeOtherMatter));
		
		
		// 从数据库读取数据
		Report data = KingTellerUtils.getReportFromDataBase(this,
				workBean.getOrderId());

		if (data == null || optType != 0 || data.getReportData() == null)
			getOtherReports();
		else {
			setDataInfo(KingTellerUtils.getReportDataFromString(
					data.getReportData(), OtherMatterReportBean.class));
		}
	}

	public void getOtherReports() {
		
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KingTellerProgress.showProgress(this, "数据加载中...");

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("orderId", workBean.getOrderId());
		params.put("reportType", workBean.getOrderType());

		fh.get(ConfigUtils.CreateNoVersionUrl(this, URLConfig.WebQtbgUrl),
				params, new AjaxHttpCallBack<OtherMatterReportBean>(this, true) {

					@Override
					public void onFinish() {
						KingTellerProgress.closeProgress();
					}

					@Override
					public void onDo(OtherMatterReportBean data) {
						setDataInfo(data);
					};
				});
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	private void setDataInfo(OtherMatterReportBean data) {
		otherMatterReport = data;
		orderNo.setFieldTextAndValue(data.getOrderNo());
		workDateStr.setFieldTextAndValue(data.getWorkDateStr());
		areaName.setFieldTextAndValue(data.getAreaName());
		machineCode.setFieldTextAndValue(data.getMachineCode());
		wdName.setFieldTextAndValue(data.getWdName());
		bankName.setFieldTextAndValue(data.getBankName());
		workOrgName.setFieldTextAndValue(data.getWorkOrgName());
		recordUserName.setFieldTextAndValue(data.getRecordUserName());
		assignDateStr.setFieldTextAndValue(data.getAssignDateStr());
		confirmDateStr.setFieldTextAndValue(data.getConfirmDateStr());
		beginDateStr.setFieldTextAndValue(data.getBeginDateStr());
		endDateStr.setFieldTextAndValue(data.getEndDateStr());
		linkName.setFieldTextAndValue(data.getLinkName());
		linkPhone.setFieldTextAndValue(data.getLinkPhone());
		workType.setFieldTextAndValueFromValue(data.getWorkType());
		workAddress.setFieldTextAndValue(data.getWorkAddress());
		troubleRemark.setFieldTextAndValue(data.getTroubleRemark());
		maintainRemark.setFieldTextAndValue(data.getMaintainRemark());
		assignNames.setFieldTextAndValue(data.getAssignNames());
		remark.setFieldTextAndValue(data.getRemark());

		auditRemark.setFieldTextAndValue(data.getAuditRemark());
		auditContent.setFieldTextAndValue(data.getAuditContent());
		auditTitle.setText("其他事物报告意见");
		
		fee_group_list.getLayoutList().removeAllViews();

		costInfoList = data.getEpList();

		/**
		 * 判断他是否为空,因为需要从本地读取数据
		 * */
		for (int i = 0; i < data.getEpList().size(); i++) {
			FeeRPGroupView fview;
			if (i == 0)
				fview = new FeeRPGroupView(OtherMatterReportActivity.this, false);
			else
				fview = new FeeRPGroupView(OtherMatterReportActivity.this, true);
			FreeData fdata = new FreeData();
			fdata.setUserName(data.getEpList().get(i).getUsername());
			fdata.setUserId(data.getEpList().get(i).getUserId());
			fdata.setFeeMoney(String.valueOf(data.getEpList().get(i)
					.getCostFee()));
			fdata.setBusLine(data.getEpList().get(i).getDescript());
			
			fdata.setFeeMode(data.getEpList().get(i).getCostdescName());
			fdata.setFeeModeId(data.getEpList().get(i).getCostDesc());
			fdata.setFeeTypeId(String.valueOf(data.getEpList().get(i)
					.getTypeId()));
			fdata.setFeeType(data.getEpList().get(i).getTypeName());
			fdata.setId(data.getEpList().get(i).getId());
			
			fview.setData(fdata);
			fee_group_list.addItem(fview);
			
			getListviewObj().setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * 获取参数生成json
	 * 
	 * @return
	 */
	public String getFromData() {
		HashMap<String, Object> params = ConditionUtils.getHashMapForm(this,
				(LinearLayout) findViewById(R.id.layout_form), false);
		
		HashMap<String, Object> paramsaudit = ConditionUtils.getHashMapForm(this,
				(LinearLayout) findViewById(R.id.layout_audit_common), false);
		
		HashMap<String, Object> paramsinfo = ConditionUtils.getHashMapForm(this,
				(LinearLayout) findViewById(R.id.layout_hide), false);
		
		params.putAll(paramsinfo);
		params.putAll(paramsaudit);

		List<AttachmentBean> ss = new ArrayList<AttachmentBean>();
		ss.add(new AttachmentBean());
		ss.add(new AttachmentBean());

		List<PickInfoBean> ss2 = new ArrayList<PickInfoBean>();
		ss2.add(new PickInfoBean());
		ss2.add(new PickInfoBean());
		
		List<CostInfoBean> rcost = new ArrayList<CostInfoBean>();
		List<FreeData> fcost = fee_group_list.getListData();
		for (int i = 0; i < fcost.size(); i++) {
			CostInfoBean data = new CostInfoBean();
			data.setDescript(fcost.get(i).getBusLine());
			data.setCostFee(Double.valueOf(CommonUtils.isEmpty(fcost.get(i).getFeeMoney())?"0":fcost.get(i).getFeeMoney()));
			data.setCostDesc(fcost.get(i).getFeeModeId());
			//data.setId(fcost.get(i).getFeeModeId());
			//data.setModeName(fcost.get(i).getFeeMode());
			data.setCostdescName(fcost.get(i).getFeeMode());
			data.setTypeId(fcost.get(i).getFeeTypeId());
			data.setTypeName(fcost.get(i).getFeeType());
			data.setUserId(fcost.get(i).getUserId());
			data.setUsername(fcost.get(i).getUserName());
			data.setId(fcost.get(i).getId());
			rcost.add(data);
		}

		// 初始化其它字段
		params.put("apList", ss);
		params.put("epList", rcost);
		params.put("lppList", ss2);

		params.put("reportId", otherMatterReport.getReportId());
		params.put("orderNo", otherMatterReport.getOrderNo());
		params.put("orderId", otherMatterReport.getOrderId());

		return ConditionUtils.getJsonFromHashMap(params);
	}

}
