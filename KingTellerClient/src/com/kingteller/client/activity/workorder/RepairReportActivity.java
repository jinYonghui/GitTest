package com.kingteller.client.activity.workorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.kingteller.R;
import com.kingteller.client.activity.common.CommonSelectDataActivity;
import com.kingteller.client.activity.common.CommonSelectDataSearchActivity;
import com.kingteller.client.adapter.TopTabAdapter;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.CommonSelectGZBJ;
import com.kingteller.client.bean.common.TreeBean;
import com.kingteller.client.bean.common.selectdata.ATMCodeBean;
import com.kingteller.client.bean.workorder.AttachmentBean;
import com.kingteller.client.bean.workorder.BJBean;
import com.kingteller.client.bean.workorder.FreeData;
import com.kingteller.client.bean.workorder.RCostInfoBean;
import com.kingteller.client.bean.workorder.RepairReportBean;
import com.kingteller.client.bean.workorder.Report;
import com.kingteller.client.bean.workorder.ReturnBackStatus;
import com.kingteller.client.bean.workorder.WorkTypeBean;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.CommonSelcetUtils;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.utils.TimeFormatUtils;
import com.kingteller.client.view.BjGroupView;
import com.kingteller.client.view.FeeRPGroupView;
import com.kingteller.client.view.GroupListView;
import com.kingteller.client.view.GroupListView.AddViewCallBack;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.client.view.KingTellerEditText;
import com.kingteller.client.view.KingTellerEditText.OnChangeListener;
import com.kingteller.client.view.KingTellerEditText.OnDialogClickLister;
import com.kingteller.client.view.TabView;
import com.kingteller.client.view.TabView.OnItemTabListener;
import com.kingteller.client.view.WorkTypeGroupView;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 维护报告
 * 
 * @author 王定波
 * 
 */
public class RepairReportActivity extends BaseReportActivity implements
		OnItemTabListener,
		com.kingteller.client.view.WorkTypeGroupView.OnChangeListener {

	private KingTellerEditText orderId;
	private KingTellerEditText reportId;
	private KingTellerEditText jqbhExt;
	private KingTellerEditText atmhExt;
	private KingTellerEditText jqgsrusername;
	private KingTellerEditText wdlxr;
	private KingTellerEditText atmTypeCode;
	private KingTellerEditText wdlxdh;
	private KingTellerEditText workOrgId;
	private KingTellerEditText wddz;
	private KingTellerEditText wdsbmc;
	private KingTellerEditText workOrderPrompt;
	private KingTellerEditText workFinishFlag;
	private KingTellerEditText isbjyy;
	private KingTellerEditText arrangeType;
	private KingTellerEditText machId;
	private KingTellerEditText atmh;
	private KingTellerEditText arriveOvertimeMin;
	private KingTellerEditText maintainOvertimeMin;
	//private KingTellerEditText workDate;
	private KingTellerEditText maintainOverRemark;
	private KingTellerEditText atmcVerson;
	private KingTellerEditText spVerson;
	private KingTellerEditText expand6;
	private KingTellerEditText expand7;
	private KingTellerEditText serveAssessResultCode;

	private KingTellerEditText workTypeNewMove;

	private View workTypeNewMove_line;
	private TabView layout_tab;
	private TopTabAdapter tabAdapter;
	private LinearLayout layout_info;
	private LinearLayout layout_bj;
	private KingTellerEditText involvesSpareParts;
	private GroupListView bj_group_list;
	private KingTellerEditText skillServeNumber;
	private KingTellerEditText expand1;
	private RepairReportBean reportdata;
	private KingTellerEditText prearrangeDate;
	private KingTellerEditText arriveOverRemark;
	private GroupListView workType_group_list;
	//private KingTellerEditText troubleHandleResult;
	private LinearLayout layout_bj_info;
	private KingTellerEditText stopReason;
	private KingTellerEditText yesnotj;
	private KingTellerEditText tjdh;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_repair_report);

		initUI();
		initChangeUI();
		initData();
	}

	@Override
	public void initUI() {
		// TODO Auto-generated method stub
		dataKey = "assign";
		super.initUI();
		title_left.setOnClickListener(this);

		bj_group_list = (GroupListView) findViewById(R.id.bj_group_list);

		// 备件
		bj_group_list.setAddViewCallBack(new AddViewCallBack() {

			@Override
			public void addView(GroupListView view) {
				// TODO Auto-generated method stub
				view.addItem(new BjGroupView(RepairReportActivity.this, true));
			}
		});
		BjGroupView bjView = new BjGroupView(RepairReportActivity.this, false);
		bjView.setOnClickListener(this);
		bj_group_list.addItem(bjView);

		workType_group_list = (GroupListView) findViewById(R.id.workType_group_list);

		workType_group_list.setAddViewCallBack(new AddViewCallBack() {

			@Override
			public void addView(GroupListView view) {
				// TODO Auto-generated method stub
				WorkTypeGroupView wview = new WorkTypeGroupView(
						RepairReportActivity.this, true);
				wview.setOnChangeListener(RepairReportActivity.this);
				view.addItem(wview);
			}
		});
		WorkTypeGroupView wview = new WorkTypeGroupView(
				RepairReportActivity.this, false);
		wview.setOnChangeListener(this);
		workType_group_list.addItem(wview);
		workType_group_list.findViewById(R.id.add_workType).setVisibility(View.VISIBLE);

		// 初始化UI
		layout_tab = (TabView) findViewById(R.id.layout_tab);
		orderId = (KingTellerEditText) findViewById(R.id.orderId);
		skillServeNumber = (KingTellerEditText) findViewById(R.id.skillServeNumber);
		expand1 = (KingTellerEditText) findViewById(R.id.expand1);
		atmhExt = (KingTellerEditText) findViewById(R.id.atmhExt);
		jqgsrusername = (KingTellerEditText) findViewById(R.id.jqgsrusername);
		wdlxr = (KingTellerEditText) findViewById(R.id.wdlxr);
		atmTypeCode = (KingTellerEditText) findViewById(R.id.atmTypeCode);
		wdlxdh = (KingTellerEditText) findViewById(R.id.wdlxdh);
		workOrgId = (KingTellerEditText) findViewById(R.id.workOrgId);
		wddz = (KingTellerEditText) findViewById(R.id.wddz);
		wdsbmc = (KingTellerEditText) findViewById(R.id.wdsbmc);
		workOrderPrompt = (KingTellerEditText) findViewById(R.id.workOrderPrompt);
		workFinishFlag = (KingTellerEditText) findViewById(R.id.workFinishFlag);
		isbjyy = (KingTellerEditText) findViewById(R.id.isbjyy);
		arrangeType = (KingTellerEditText) findViewById(R.id.arrangeType);
		machId = (KingTellerEditText) findViewById(R.id.machId);
		atmh = (KingTellerEditText) findViewById(R.id.atmh);
		arriveOvertimeMin = (KingTellerEditText) findViewById(R.id.arriveOvertimeMin);
		maintainOvertimeMin = (KingTellerEditText) findViewById(R.id.maintainOvertimeMin);
		maintainOverRemark = (KingTellerEditText) findViewById(R.id.maintainOverRemark);

	//	workDate = (KingTellerEditText) findViewById(R.id.workDate);
		arriveOverRemark = (KingTellerEditText) findViewById(R.id.arriveOverRemark);
		atmcVerson = (KingTellerEditText) findViewById(R.id.atmcVerson);
		spVerson = (KingTellerEditText) findViewById(R.id.spVerson);
		expand6 = (KingTellerEditText) findViewById(R.id.expand6);
		expand7 = (KingTellerEditText) findViewById(R.id.expand7);
		//troubleHandleResult = (KingTellerEditText) findViewById(R.id.troubleHandleResult);
		//停机原因
		stopReason = (KingTellerEditText) findViewById(R.id.stopReason);
		//是否退机
		yesnotj = (KingTellerEditText) findViewById(R.id.yesnotj);
		tjdh = (KingTellerEditText) findViewById(R.id.tjdh);
		tjdh.setFouces(false);

		prearrangeDate = (KingTellerEditText) findViewById(R.id.prearrangeDate);

		workTypeNewMove = (KingTellerEditText) findViewById(R.id.workTypeNewMove);

		involvesSpareParts = (KingTellerEditText) findViewById(R.id.involvesSpareParts);
		serveAssessResultCode = (KingTellerEditText) findViewById(R.id.serveAssessResultCode);

		// line
		workTypeNewMove_line = (View) findViewById(R.id.workTypeNewMove_line);

		layout_info = (LinearLayout) findViewById(R.id.layout_info);
		layout_bj = (LinearLayout) findViewById(R.id.layout_bj);
		layout_bj_info= (LinearLayout) findViewById(R.id.layout_bj_info);

		tabAdapter = new TopTabAdapter(this, getResources().getStringArray(
				R.array.repairreport));
		layout_tab.setAdapter(tabAdapter);
		layout_tab.setOnItemListener(this);

		if (optType == BaseReportActivity.OPT_ADUIT) {
			// 设置信息不可写 只可以看
			int count1 = layout_info.getChildCount();
			for (int i = 0; i < count1; i++) {
				if (layout_info.getChildAt(i).getClass().getName()
						.equals(KingTellerEditText.class.getName())) {
					((KingTellerEditText) layout_info.getChildAt(i))
							.setFieldEnabled(false);
				}
			}

			int count2 = layout_bj.getChildCount();
			for (int i = 0; i < count2; i++) {
				if (layout_bj.getChildAt(i).getClass().getName()
						.equals(KingTellerEditText.class.getName())) {
					((KingTellerEditText) layout_bj.getChildAt(i))
							.setFieldEnabled(false);
				}
			}
		}

		if(CommonUtils.isEmpty(isCheckout)){	
			switch (optType) {
			case OPT_ADUIT:	
				title_title.setText("维护报告审核");
				break;
			default:
				title_title.setText("维护报告填写");
				break;
			}
		}else{
			title_title.setText("维护报告");
		}

		CommonUtils.hideInputMethod(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();

		workFinishFlag.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.workFinishFlag));
		isbjyy.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.radios12));
		arrangeType.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.radios01));
		workTypeNewMove.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.workTypeNewMove));
		// expand2.setLists(CommonSelcetUtils
		// .getSelectList(CommonSelcetUtils.radios12));
		involvesSpareParts.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.radios01));
		yesnotj.setLists(CommonSelcetUtils.getSelectList(CommonSelcetUtils.radios01));
		stopReason.setLists(CommonSelcetUtils.getSelectList(CommonSelcetUtils.stopReasonType));
		serveAssessResultCode.setLists(CommonSelcetUtils.getSelectList(CommonSelcetUtils.serveAssessResultCode));
		
		involvesSpareParts.setFieldTextAndValueFromValue("0");
		yesnotj.setFieldTextAndValueFromValue("0");

		// 从数据库读取数据
		Report data = KingTellerUtils.getReportFromDataBase(this,
				workBean.getOrderId());

		if (data == null || optType != 0 || data.getReportData() == null)
			getReportInfo();
		else {
			setDataInfo(KingTellerUtils.getReportDataFromString(
					data.getReportData(), RepairReportBean.class));
		}

		workOrgId.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RepairReportActivity.this,
						CommonSelectDataSearchActivity.class);
				intent.putExtra(CommonSelectDataActivity.TYPE,
						R.array.serverState);
				startActivityForResult(intent, CommonCodeConfig.SELECT_SERVICE);
			}
		});

		machId.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RepairReportActivity.this,
						CommonSelectDataSearchActivity.class);
				intent.putExtra(CommonSelectDataActivity.TYPE, R.array.atmcode);
				startActivityForResult(intent, CommonCodeConfig.SELECT_ATMCODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CommonCodeConfig.SELECT_GZMS:
			if (resultCode == RESULT_OK) {
				bj_group_list.setItemData((CommonSelectGZBJ) data
						.getSerializableExtra(CommonSelectDataActivity.DATA));
				List<BJBean> bjList = bj_group_list.getListData();
				for(int i = 0; i < bjList.size(); i ++){
					if(bjList.get(i).getTroubleRemark().contains("其它")){
						((BjGroupView) bj_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.liearOther).setVisibility(View.VISIBLE);
					}else{
						((BjGroupView) bj_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.liearOther).setVisibility(View.GONE);
					}
				}
				setChangeUIRwxx();
			}
			break;
		case CommonCodeConfig.SELECT_BJMK:
			if (resultCode == RESULT_OK) {
				bj_group_list.setItemData((CommonSelectData) data
						.getSerializableExtra(CommonSelectDataActivity.DATA));
				setChangeUI();
			}
			break;
		case CommonCodeConfig.SELECT_CLGC:
			if (resultCode == RESULT_OK) {
				bj_group_list.setItemData((CommonSelectData) data
						.getSerializableExtra(CommonSelectDataActivity.DATA));
				
				List<BJBean> bjList = bj_group_list.getListData();
				for(int i = 0; i < bjList.size(); i ++){
					if(!bjList.get(i).getTroubleRemark().contains("其它")){
						if(bjList.get(i).getHandleSub().contains("其它")){
							((BjGroupView) bj_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.liearOther).setVisibility(View.VISIBLE);
						}else{
							((BjGroupView) bj_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.liearOther).setVisibility(View.GONE);
						}
					}else{
						((BjGroupView) bj_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.liearOther).setVisibility(View.VISIBLE);
					}
				}
				
				setChangeUIClgc();
			}
			break;
		case CommonCodeConfig.REQUEST_BJWL:
			if (resultCode == RESULT_OK) {
				bj_group_list.setItemDataMul(((TreeBean) data
						.getSerializableExtra(CommonSelectDataActivity.DATA))
						.getCommonSelectData());
				
			}
			break;
		case CommonCodeConfig.SELECT_HANDLESUB:
			if(resultCode == RESULT_OK){
				workType_group_list.setItemData((CommonSelectData) data
						.getSerializableExtra(CommonSelectDataActivity.DATA));
				List<WorkTypeBean> bjList = workType_group_list.getListData();
				for(int i = 0; i < bjList.size(); i ++){
					if(bjList.get(i).getHandleSub().contains("其它")){
						((WorkTypeGroupView) workType_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.remark).setVisibility(View.VISIBLE);
						((WorkTypeGroupView) workType_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.remark_view).setVisibility(View.VISIBLE);
					}else{
						((WorkTypeGroupView) workType_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.remark).setVisibility(View.GONE);
						((WorkTypeGroupView) workType_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.remark_view).setVisibility(View.GONE);
					}
				}
			}
			break;
		case CommonCodeConfig.REQUEST_GZBJ:
			if (resultCode == RESULT_OK) {
				bj_group_list.setItemData((CommonSelectGZBJ)data.
						getSerializableExtra(CommonSelectDataActivity.DATA));
				setChangeUIGzbj();
			}
			break;
		case CommonCodeConfig.SELECT_SERVICE:
			if (resultCode == RESULT_OK) {
				workOrgId.setFieldTextAndValue((CommonSelectData) data
						.getSerializableExtra(CommonSelectDataActivity.DATA));
			}
			break;
		case CommonCodeConfig.SELECT_ATMCODE:
			if (resultCode == RESULT_OK) {
				CommonSelectData cdata = (CommonSelectData) data
						.getSerializableExtra(CommonSelectDataActivity.DATA);
				machId.setFieldTextAndValue(cdata);
				ATMCodeBean adata = (ATMCodeBean) cdata.getObj();

				atmh.setFieldTextAndValue(adata.getAtmh());
				expand1.setFieldTextAndValue(adata.getJqbh());
				atmhExt.setFieldTextAndValue(adata.getAtmh());
				wddz.setFieldTextAndValue(adata.getWddz());
				wdlxr.setFieldTextAndValue(adata.getWdlxr());
				wdlxdh.setFieldTextAndValue(adata.getWdlxdh());
				wdsbmc.setFieldTextAndValue(adata.getWdsbjc());
				atmTypeCode.setFieldTextAndValue(adata.getJxName());
				jqgsrusername.setFieldTextAndValue(adata.getJqgsrusername());

			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置一些动作
	 */
	private void initChangeUI() {
		// TODO Auto-generated method stub

		involvesSpareParts.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				if (data.getValue().equals("0")) {
					layout_bj_info.setVisibility(View.GONE);
					bj_group_list.getLayoutList().removeAllViews();
				} else if (data.getValue().equals("1")) {
					layout_bj_info.setVisibility(View.VISIBLE);
					if(bj_group_list.getLayoutList().getChildCount() == 0){
						bj_group_list.addItem(new BjGroupView(RepairReportActivity.this, false));
					}
				}
			}
		});
		
		workFinishFlag.setOnChangeListener(new OnChangeListener() {
			
			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				if(data.getValue().equals("1")){
					isbjyy.setVisibility(View.VISIBLE);
					stopReason.setVisibility(View.VISIBLE);
					findViewById(R.id.stopMachineReason_view).setVisibility(View.VISIBLE);
				}else{
					isbjyy.setVisibility(View.GONE);
					stopReason.setVisibility(View.GONE);
					findViewById(R.id.stopMachineReason_view).setVisibility(View.GONE);
				}
			}
		});

		arrangeType.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				if (data.getValue().equals("0")) {
					prearrangeDate.setVisibility(View.GONE);
					findViewById(R.id.prearrangeDateLine).setVisibility(View.GONE);
				} else if (data.getValue().equals("1")) {
					prearrangeDate.setVisibility(View.VISIBLE);
					findViewById(R.id.prearrangeDateLine).setVisibility(View.VISIBLE);
				}
			}
		});
		
		
		
	
	}

	private void getsftj(){
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("jqbh", machId.getFieldValue());
		
		fh.post(ConfigUtils.CreateUrl(this, URLConfig.SftjUrl), params,
				new AjaxHttpCallBack<ReturnBackStatus>(this, true) {

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(ReturnBackStatus data) {
						tjdh.setFieldTextAndValue(data.getMessage());
					};
				});
	}
	
	private void getReportInfo() {

		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("orderId", workBean.getOrderId());
		params.put("reportType", workBean.getOrderType());

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.WebQtbgUrl), params,
				new AjaxHttpCallBack<RepairReportBean>(this, true) {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						KingTellerProgress.showProgress(
								RepairReportActivity.this, "数据加载中...");
					}

					@Override
					public void onFinish() {
						KingTellerProgress.closeProgress();
					}

					@Override
					public void onDo(RepairReportBean data) {
						setDataInfo(data);
					};
				});
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	private void setDataInfo(RepairReportBean data) {
		reportdata = data;
		orderId.setFieldTextAndValue(data.getOrderNo(), data.getOrderId());
		skillServeNumber.setFieldTextAndValue(data.getSkillServeNumber());
		expand1.setFieldTextAndValue(data.getExpand1());
		atmhExt.setFieldTextAndValue(data.getAtmh());
		jqgsrusername.setFieldTextAndValue(data.getJqgsrusername());
		wdlxr.setFieldTextAndValue(data.getWdlxr());
		atmTypeCode.setFieldTextAndValue(data.getAtmTypeCode());
		wdlxdh.setFieldTextAndValue(data.getWdlxdh());
		workOrgId.setFieldTextAndValue(data.getOrgName(), data.getWorkOrgId());
		wddz.setFieldTextAndValue(data.getWddz());
		wdsbmc.setFieldTextAndValue(data.getWdsbmc());
		workOrderPrompt.setFieldTextAndValue(data.getWorkOrderPrompt());
		workFinishFlag.setFieldTextAndValueFromValue(data.getWorkFinishFlag());
		isbjyy.setFieldTextAndValueFromValue(data.getIsbjyy());
		workTypeNewMove.setFieldTextAndValueFromValue(data.getWorkTypeNewMove());
		serveAssessResultCode.setFieldTextAndValueFromValue(data.getServeAssessResultCode());
		
		yesnotj.setFieldTextAndValueFromValue(data.getYesnotj());
		
		stopReason.setFieldTextAndValueFromValue(data.getStopReason());
		if(data.getYesnotj().equals("1")){
			tjdh.setVisibility(View.VISIBLE);
			findViewById(R.id.tjdh_view).setVisibility(View.VISIBLE);
		}
		tjdh.setFieldTextAndValue(data.getTjdh());
		
		yesnotj.setOnChangeListener(new OnChangeListener() {
			
			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				if(data.getValue().equals("1")){
					tjdh.setVisibility(View.VISIBLE);
					findViewById(R.id.tjdh_view).setVisibility(View.VISIBLE);
					getsftj();
				}else{
					tjdh.setVisibility(View.GONE);
					findViewById(R.id.tjdh_view).setVisibility(View.GONE);
					tjdh.setFieldTextAndValue("");
				}
			}
		});
		if(!CommonUtils.isEmpty(data.getArrangeType())){
			arrangeType.setFieldTextAndValueFromValue(data.getArrangeType());
			if(arrangeType.getFieldValue().equals("1")){
				prearrangeDate.setFieldTextAndValue(data.getPrearrangeDate());
			}
		}else{
			arrangeType.setFieldTextAndValueFromValue("0");
		}
		machId.setFieldTextAndValue(data.getExpand1());
		atmh.setFieldTextAndValue(data.getAtmh());
		
		if(CommonUtils.isEmpty(data.getArriveOvertimeMin())){
			arriveOvertimeMin.setVisibility(View.GONE);
		}else{
			arriveOvertimeMin.setFieldTextAndValue(data.getArriveOvertimeMin());
			arriveOverRemark.setFieldTextAndValue(data.getArriveOverRemark());
			arriveOverRemark.setVisibility(View.VISIBLE);
			findViewById(R.id.arriveOverRemark_view).setVisibility(View.VISIBLE);
		}
		//troubleHandleResult.setFieldTextAndValue(data.getTroubleHandleResult());
		//workDate.setFieldTextAndValue(data.getWorkDate());
		
		if(CommonUtils.isEmpty(data.getMaintainOvertimeMin())){
			maintainOvertimeMin.setVisibility(View.GONE);
		}else{	
			maintainOvertimeMin.setFieldTextAndValue(data.getMaintainOvertimeMin());
			maintainOverRemark.setVisibility(View.VISIBLE);
			maintainOverRemark.setFieldTextAndValue(data.getMaintainOverRemark());
			findViewById(R.id.maintainOverRemark_view).setVisibility(View.VISIBLE);
		}
		atmcVerson.setFieldTextAndValue(data.getAtmcVerson());
		spVerson.setFieldTextAndValue(data.getSpVerson());
		expand6.setFieldTextAndValue(data.getExpand6());
		expand7.setFieldTextAndValue(data.getExpand7());

		involvesSpareParts.setFieldTextAndValueFromValue(data.getInvolvesSpareParts());

		auditRemark.setFieldTextAndValue(data.getAuditRemark());
		auditContent.setFieldTextAndValue(data.getAuditContent());
		auditTitle.setText("维护报告意见");
		
		fee_group_list.getLayoutList().removeAllViews();

		if (data.getEpList() != null)
			for (int i = 0; i < data.getEpList().size(); i++) {
				FeeRPGroupView fview;
				if (i == 0)
					fview = new FeeRPGroupView(RepairReportActivity.this, false);
				else
					fview = new FeeRPGroupView(RepairReportActivity.this, true);

				FreeData fdata = new FreeData();
				fdata.setUserName(data.getEpList().get(i)
						.getMaintainpersonname());
				fdata.setUserId(data.getEpList().get(i).getMaintainpersonid());
				fdata.setFeeMoney(String.valueOf(data.getEpList().get(i)
						.getTrafficefee()).equals("0") ? null : String.valueOf(data.getEpList().get(i)
								.getTrafficefee()));
				fdata.setBusLine(data.getEpList().get(i).getArriveroute());
				//fdata.setFeeMode(data.getEpList().get(i).getAccessname());
				fdata.setFeeModeId(data.getEpList().get(i).getExpand2());
				fdata.setFeeTypeId(String.valueOf(data.getEpList().get(i)
						.getExpand1()));
				fdata.setFeeType(data.getEpList().get(i).getExpand3());
				fdata.setIsGoBack(data.getEpList().get(i).getExpand4());
				fdata.setFeeMode(data.getEpList().get(i).getExpand5());
				
				fview.setData(fdata);
				fee_group_list.addItem(fview);
			}

		if (data.getMfList() != null) {
			if (data.getMfList().size() > 0)
				bj_group_list.getLayoutList().removeAllViews();
			for (int i = 0; i < data.getMfList().size(); i++) {
				BjGroupView bview = new BjGroupView(RepairReportActivity.this);
				bview.setData(data.getMfList().get(i));
				if(data.getMfList().get(i).getTroubleRemark().contains("其它") ||
						data.getMfList().get(i).getHandleSub().contains("其它")){
					bview.findViewById(R.id.liearOther).setVisibility(View.VISIBLE);
				}else{
					bview.findViewById(R.id.liearOther).setVisibility(View.GONE);
				}
				bj_group_list.addItem(bview);
			}
		}

		if (data.getRwList() != null) {
			if (data.getRwList().size() > 0){
				workType_group_list.getLayoutList().removeAllViews();
			}
			Date endTime = TimeFormatUtils.getDateByString(data.getMaintainEndTime());
			Date beginTime = TimeFormatUtils.getDateByString(data.getMaintainBeginTime());
			long minutes = (endTime.getTime() - beginTime.getTime())/(1000*60);
			if(data.getRwList().size() == 0){
				workType_group_list.getLayoutList().removeAllViews();
				final WorkTypeGroupView bview = new WorkTypeGroupView(RepairReportActivity.this,false);
				//((KingTellerEditText)bview.findViewById(R.id.costMinute)).setFieldEnabled(false);
				if(minutes == 0){
					minutes += 1;
				}
				((KingTellerEditText)bview.findViewById(R.id.costMinute)).setFieldTextAndValue(String.valueOf(minutes));
				((KingTellerEditText)bview.findViewById(R.id.workType)).setOnChangeListener(new OnChangeListener() {
					
					@Override
					public void onChanged(CommonSelectData data) {
						// TODO Auto-generated method stub
						((KingTellerEditText)bview.findViewById(R.id.remark)).setFieldTextAndValue("");
						bview.findViewById(R.id.remark).setVisibility(View.GONE);
						bview.findViewById(R.id.remark_view).setVisibility(View.GONE);
						if(data.getValue().equals("START") ||data.getValue().equals("RECEIVE_MACHINE") 
								||data.getValue().equals("SETUP_MACHINE")|| data.getValue().equals("DEBUG")){
							workTypeNewMove.setVisibility(View.VISIBLE);
							workTypeNewMove_line.setVisibility(View.VISIBLE);
							workTypeNewMove.setLists(CommonSelcetUtils
									.getSelectList(CommonSelcetUtils.workTypeNewMove));
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue("");
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldEnabled(true);
							((LinearLayout) bview.findViewById(R.id.worktypeLayout)).setVisibility(View.GONE);
							if(CommonUtils.isEmpty(workTypeNewMove.getFieldValue()))
							workTypeNewMove.setFieldTextAndValue("");
						}else if(data.getValue().equals("MOVE_MACHINE")){
							
							workTypeNewMove.setVisibility(View.GONE);
							workTypeNewMove_line.setVisibility(View.GONE);
							//workTypeNewMove.setFieldTextAndValue("移走机器","MOVE_OUT");
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue("");
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldEnabled(true);
							((LinearLayout) bview.findViewById(R.id.worktypeLayout)).setVisibility(View.GONE);
							if(CommonUtils.isEmpty(workTypeNewMove.getFieldValue()))
							workTypeNewMove.setFieldTextAndValue("");
						}
						else if(data.getValue().equals("NORMAL")){
							workTypeNewMove.setVisibility(View.GONE);
							workTypeNewMove_line.setVisibility(View.GONE);
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue("请在维护信息栏填写维护过程");
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldEnabled(false);
							((LinearLayout) bview.findViewById(R.id.worktypeLayout)).setVisibility(View.VISIBLE);
						}else{
							workTypeNewMove.setVisibility(View.GONE);
							workTypeNewMove_line.setVisibility(View.GONE);
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue("");
							((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldEnabled(true);
							((LinearLayout) bview.findViewById(R.id.worktypeLayout)).setVisibility(View.GONE);
						}
					}
				});
				workType_group_list.addItem(bview);
			}else if(data.getRwList().size() == 1){
				WorkTypeGroupView bview = new WorkTypeGroupView(RepairReportActivity.this,
						false);
				bview.setOnChangeListener(this);
				bview.setData(data.getRwList().get(0));
				//((KingTellerEditText)bview.findViewById(R.id.costMinute)).setFieldEnabled(false);
				if(!CommonUtils.isEmpty(data.getRwList().get(0).getCostMinute())){
					minutes = data.getRwList().get(0).getCostMinute();
				}
				if(minutes == 0){
					minutes += 1;
				}
				((KingTellerEditText)bview.findViewById(R.id.costMinute)).setFieldTextAndValue(String.valueOf(minutes));
				if(data.getRwList().get(0).getWorkType().equals("NORMAL")){
					((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue("请在维护信息栏填写维护过程");
				}else{
					((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue(data.getRwList().get(0).getHandleSub(),
							data.getRwList().get(0).getHandleSubId());
				}
				
				if(data.getRwList().get(0).getHandleSub().contains("其它")){
					bview.findViewById(R.id.remark).setVisibility(View.VISIBLE);
					bview.findViewById(R.id.remark_view).setVisibility(View.VISIBLE);
				}else{
					bview.findViewById(R.id.remark).setVisibility(View.GONE);
					bview.findViewById(R.id.remark_view).setVisibility(View.GONE);
				}
				
				workType_group_list.addItem(bview);
			}else{
				for (int i = 0; i < data.getRwList().size(); i++) {
					WorkTypeGroupView bview;
					if (i == 0) {
						bview = new WorkTypeGroupView(
								RepairReportActivity.this, false);
					}
					else
						bview = new WorkTypeGroupView(RepairReportActivity.this,true);
					
					bview.setOnChangeListener(this);
					bview.setData(data.getRwList().get(i));
					if(data.getRwList().get(i).getWorkType().equals("NORMAL")){
						((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue("请在维护信息栏填写维护过程");
					}else{
						((KingTellerEditText) bview.findViewById(R.id.handleSub)).setFieldTextAndValue(data.getRwList().get(i).getHandleSub(),
								data.getRwList().get(i).getHandleSubId());
					}
					if(data.getRwList().get(0).getHandleSub().contains("其它")){
						bview.findViewById(R.id.remark).setVisibility(View.VISIBLE);
						bview.findViewById(R.id.remark_view).setVisibility(View.VISIBLE);
					}else{
						bview.findViewById(R.id.remark).setVisibility(View.GONE);
						bview.findViewById(R.id.remark_view).setVisibility(View.GONE);
					}
					
					workType_group_list.addItem(bview);
				}
			}
		}

		getListviewObj().setVisibility(View.GONE);
	}

	/**
	 * 头部按钮点击
	 */
	@Override
	public void OnItemClick(int pos) {
		// TODO Auto-generated method stub
		tabAdapter.setSelectPostion(pos);
		if (pos == 0) {

			layout_info.setVisibility(View.VISIBLE);
			layout_bj.setVisibility(View.GONE);
		} else if (pos == 1) {

			layout_info.setVisibility(View.GONE);
			layout_bj.setVisibility(View.VISIBLE);
		}
		// layout_scroll.scrollTo(0, 0);
	}

	@Override
	public void submitReport(final int flag) {
		// TODO Auto-generated method stub

		List<WorkTypeBean> list = workType_group_list.getListData();

		if (flag == 2) {
			if (list.size() == 0) {
				toastMsg("工作类别必须有一项");
				return;
			} else {
				for (int j = 0; j < list.size(); j++) {
					if (CommonUtils.isEmpty(list.get(j).getWorkType())) {
						toastMsg("工作类别必须填写");
						return;
					}

					if (CommonUtils.isEmpty(list.get(j).getCostMinute()) || list.get(j).getCostMinute() == 0) {
						toastMsg("耗时时间必须填写");
						return;
					}

				/*	if (CommonUtils.isEmpty(list.get(j).getHandleSub())
							&& !list.get(j).getWorkType().equals("NORMAL")) {
						toastMsg("处理过程必须填写");
						return;
					}*/
					
					if(list.get(j).getWorkType().equals("NORMAL")){
						if(CommonUtils.isEmpty(list.get(j).getReason())){
							toastMsg("本次故障原因不能为空");
							return;
						}
					}
				}
				
				List<String> listtype = new ArrayList<String>();
				
				for (int j = 0; j < list.size(); j++) {
					listtype.add(list.get(j).getWorkType());
				}
				if (listtype.contains("START") || listtype.contains("MOVE_MACHINE") ||
						listtype.contains("RECEIVE_MACHINE") || listtype.contains("SETUP_MACHINE")
						|| listtype.contains("DEBUG")) {
					if(CommonUtils.isEmpty(workTypeNewMove.getFieldText())){
						toastMsg("机器类型选项不能为空");
						return;
					}
				}
			}

			if (CommonUtils.isEmpty(workFinishFlag.getFieldValue())
					|| workFinishFlag.getFieldValue().equals("99")) {
				toastMsg("机器是否正常服务不能为空");
				return;
			}

			if (!CommonUtils.isEmpty(arrangeType.getFieldValue())
					&& arrangeType.getFieldValue().equals("1")) {
				if (CommonUtils.isEmpty(prearrangeDate.getFieldValue())) {
					toastMsg("预约时间不能为空");
					return;
				} /*else if (!prearrangeDate.getFieldValue().startsWith(
						workDate.getFieldValue())) {
					toastMsg("预约时间必须和工作时间同一天");
					return;
				}*/
			}

			if (!CommonUtils.isEmpty(arriveOvertimeMin.getFieldValue())) {
				if (CommonUtils.isEmpty(arriveOverRemark.getFieldValue())) {
					toastMsg("响应超时说明不能为空");
					return;
				}
			}

			if (!CommonUtils.isEmpty(maintainOvertimeMin.getFieldValue())) {
				if (CommonUtils.isEmpty(maintainOverRemark.getFieldValue())) {
					toastMsg("维护超时说明不能为空");
					return;
				}
			}

		/*	if (CommonUtils.isEmpty(troubleHandleResult.getFieldValue())) {
				toastMsg("处理结果不能为空");
				return;
			}*/

			if (involvesSpareParts.getFieldValue().equals("1")) {
				List<BJBean> bjcost = bj_group_list.getListData();
				for (int i = 0; i < bjcost.size(); i++) {
					if (CommonUtils.isEmpty(bjcost.get(i).getTroubleType())) {
						toastMsg("维护信息栏目中的故障类别不能为空");
						return;
					}

					if (CommonUtils.isEmpty(bjcost.get(i).getTroubleModule())) {
						toastMsg("维护信息栏目中的故障部件不能为空");
						return;
					}

					if (bjcost.get(i).getIsChangeModule().equals("1")) {
						if (CommonUtils.isEmpty(bjcost.get(i)
								.getInstallBjWlId())) {
							toastMsg("维护信息栏目中的安装的物料不能为空");
							return;
						}

						if (CommonUtils.isEmpty(bjcost.get(i).getDownBjWlId())) {
							toastMsg("维护信息栏目中的原装的物料不能为空");
							return;
						}
					}

					if (CommonUtils.isEmpty(bjcost.get(i).getTroubleRemarkId())) {
						toastMsg("维护信息栏目中的故障描述不能为空");
						return;
					}

//					if (CommonUtils.isEmpty(bjcost.get(i).getHandleSubId())) {
//						toastMsg("维护信息栏目中的处理过程不能为空");
//						return;
//					}
					
					if(bjcost.get(i).getIsChangeModule() == 1){
						if(CommonUtils.isEmpty(bjcost.get(i).getInstallBjWlId())){
							toastMsg("维护信息栏目中的安装物料不能为空");
							return;
						}
						if(CommonUtils.isEmpty(bjcost.get(i).getDownBjWlId())){
							toastMsg("维护信息栏目中的原装物料不能为空");
							return;
						}
					}
				}
				
			}
			if(workFinishFlag.getFieldValue().equals("1")){
				if(CommonUtils.isEmpty(isbjyy.getFieldValue())){
					toastMsg("是否备件原因不能为空");
					return;
				}
			}
			
			/*if(!CommonUtils.isEmpty(tjdh.getFieldValue()) && !tjdh.getFieldValue().equals(machId.getFieldValue())){
				toastMsg("退机单号和机器编号不一致");
				return;
			}*/
			
		}
		

		List<String> listtype = new ArrayList<String>();

		for (int j = 0; j < list.size(); j++) {
			listtype.add(list.get(j).getWorkType());
		}

		if (listtype.contains("NORMAL") && flag == 2
				&& involvesSpareParts.getFieldValue().equals("0")) {
			new KTAlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("您的维护信息必须填写")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									dialogInterface.dismiss();

								}
							}).create().show();
		} else
			super.submitReport(flag);
	}

	/**
	 * 获取参数生成json
	 * 
	 * @return
	 */
	@Override
	public String getFromData() {

		HashMap<String, Object> params = ConditionUtils.getHashMapForm(this,
				(LinearLayout) findViewById(R.id.layout_info), false);
		HashMap<String, Object> paramsbj = ConditionUtils.getHashMapForm(this,
				(LinearLayout) findViewById(R.id.layout_bj), false);
		HashMap<String, Object> paramsinfo = ConditionUtils.getHashMapForm(
				this, (LinearLayout) findViewById(R.id.layout_hide), false);

		HashMap<String, Object> paramsaudit = ConditionUtils.getHashMapForm(
				this, (LinearLayout) findViewById(R.id.layout_audit_common),
				false);

		params.putAll(paramsbj);
		params.putAll(paramsaudit);
		params.putAll(paramsinfo);

		List<AttachmentBean> ss = new ArrayList<AttachmentBean>();
		ss.add(new AttachmentBean());
		ss.add(new AttachmentBean());

		List<RCostInfoBean> rcost = new ArrayList<RCostInfoBean>();
		List<FreeData> fcost = fee_group_list.getListData();
		for (int i = 0; i < fcost.size(); i++) {
			RCostInfoBean data = new RCostInfoBean();
			data.setArriveroute(fcost.get(i).getBusLine());
			data.setTrafficefee(CommonUtils.isEmpty(fcost.get(i)
					.getFeeMoney()) ? "0" : fcost.get(i).getFeeMoney());
			data.setExpand2(fcost.get(i).getFeeModeId());
			data.setExpand5(fcost.get(i).getFeeMode());
			data.setAccessname(fcost.get(i).getFeeMode());
			data.setExpand1(fcost.get(i).getFeeTypeId());
			data.setExpand3(fcost.get(i).getFeeType());
			data.setExpand4(fcost.get(i).getIsGoBack());
			data.setMaintainpersonid(fcost.get(i).getUserId());
			data.setMaintainpersonname(fcost.get(i).getUserName());
			rcost.add(data);
		}

		// 初始化其它字段
		params.put("apList", ss);
		params.put("epList", rcost);
		List<WorkTypeBean> wtbcost = new ArrayList<WorkTypeBean>();
		List<WorkTypeBean> wcost = workType_group_list.getListData();
		for (int i = 0; i < wcost.size(); i++) {
			WorkTypeBean data = new WorkTypeBean();
			data.setWorkType(wcost.get(i).getWorkType());
			data.setWorkTypeLike(wcost.get(i).getWorkTypeLike());
			data.setCostMinute(wcost.get(i).getCostMinute());
			if(wcost.get(i).getHandleSub().equals("请在维护信息栏填写维护过程")){
				data.setHandleSub("");
			}else{
				data.setHandleSub(wcost.get(i).getHandleSub());
				//data.setHandleSubLike(wcost.get(i).getHandleSubLike());
			}
			data.setHandleSubId(wcost.get(i).getHandleSubId());
			data.setReason(wcost.get(i).getReason());
			data.setRemark(((KingTellerEditText)((WorkTypeGroupView) workType_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.remark)).getFieldText());
			wtbcost.add(data);
		}
		params.put("rwList", wtbcost);
		if (involvesSpareParts.getFieldValue().equals("1"))
			params.put("mfList", bj_group_list.getListData());

		params.put("reportId", reportdata.getReportId());
		params.put("orderNo", orderId.getFieldText());
		params.put("orgName", workOrgId.getFieldText());
		params.put("machineId", reportdata.getMachineId());

		params.put("askTim", reportdata.getAskTim());
		params.put("arriveTime", reportdata.getArriveTime());
		params.put("assignOrderTime", reportdata.getAssignOrderTime());
		params.put("maintainBeginTime", reportdata.getMaintainBeginTime());
		params.put("maintainEndTime", reportdata.getMaintainEndTime());
		params.put("troubleHappenTime", reportdata.getTroubleHappenTime());
		params.put("troubleHandleResult", workFinishFlag.getFieldText());

		return ConditionUtils.getJsonFromHashMap(params);
	}
	
	@Override
	public void onWorkTypeChange(WorkTypeGroupView view, CommonSelectData data) {
		// TODO Auto-generated method stub
		
		List<WorkTypeBean> list = workType_group_list.getListData();

		List<String> listtype = new ArrayList<String>();
		
		for (int j = 0; j < list.size(); j++) {
			listtype.add(list.get(j).getWorkType());
		}
		listtype.add(data.getValue());

		if (listtype.contains("START") || listtype.contains("RECEIVE_MACHINE") || 
				listtype.contains("SETUP_MACHINE") || listtype.contains("DEBUG")) {
			workTypeNewMove.setVisibility(View.VISIBLE);
			workTypeNewMove_line.setVisibility(View.VISIBLE);
			workTypeNewMove.setLists(CommonSelcetUtils
					.getSelectList(CommonSelcetUtils.workTypeNewMove));
			if(CommonUtils.isEmpty(workTypeNewMove.getFieldValue()))
			workTypeNewMove.setFieldTextAndValue("");

		} else if (listtype.contains("MOVE_MACHINE")) {
			workTypeNewMove.setVisibility(View.GONE);
			workTypeNewMove_line.setVisibility(View.GONE);
			//workTypeNewMove.setFieldTextAndValue("移走机器","MOVE_OUT");
			if(CommonUtils.isEmpty(workTypeNewMove.getFieldValue()))
			workTypeNewMove.setFieldTextAndValue("");
		}

		if (!listtype.contains("START") && !listtype.contains("MOVE_MACHINE") &&
				!listtype.contains("RECEIVE_MACHINE") && !listtype.contains("SETUP_MACHINE")
				&& !listtype.contains("DEBUG")) {
			workTypeNewMove.setVisibility(View.GONE);
			workTypeNewMove_line.setVisibility(View.GONE);
		}
	}

	private void setChangeUI(){
		LinearLayout bj = (LinearLayout)((KingTellerEditText)bj_group_list.getLayoutList().getTag()).getParent();
		((KingTellerEditText) bj.findViewById(R.id.installBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.downBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.troubleRemarkId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.handleSubId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.troubleModule)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.otherDescription)).setFieldTextAndValue("");
		((LinearLayout) bj.findViewById(R.id.liearOther)).setVisibility(View.GONE);
	}
	
	private void setChangeUIGzbj(){
		LinearLayout bj = (LinearLayout)((KingTellerEditText)bj_group_list.getLayoutList().getTag()).getParent();
		((KingTellerEditText) bj.findViewById(R.id.installBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.downBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.troubleRemarkId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.handleSubId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.otherDescription)).setFieldTextAndValue("");
		((LinearLayout) bj.findViewById(R.id.liearOther)).setVisibility(View.GONE);
	}
	
	private void setChangeUIRwxx(){
		LinearLayout bj = (LinearLayout)((KingTellerEditText)bj_group_list.getLayoutList().getTag()).getParent();
		((KingTellerEditText) bj.findViewById(R.id.installBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.downBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.handleSubId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.otherDescription)).setFieldTextAndValue("");
		//((LinearLayout) bj.findViewById(R.id.liearOther)).setVisibility(View.GONE);
	}
	
	
	private void setChangeUIClgc(){
		LinearLayout bj = (LinearLayout)((KingTellerEditText)bj_group_list.getLayoutList().getTag()).getParent();
		((KingTellerEditText) bj.findViewById(R.id.installBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.downBjWlId)).setFieldTextAndValue("");
		((KingTellerEditText) bj.findViewById(R.id.otherDescription)).setFieldTextAndValue("");
	}
	
	
}
