package com.kingteller.client.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.kingteller.R;
import com.kingteller.client.activity.common.CommonSelectClgcActivity;
import com.kingteller.client.activity.common.CommonSelectDataActivity;
import com.kingteller.client.activity.common.TreeChooserActivity;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.workorder.BJBean;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.utils.CommonSelcetUtils;
import com.kingteller.client.view.KingTellerEditText.OnChangeListener;
import com.kingteller.client.view.KingTellerEditText.OnDialogClickLister;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 维护信息
 * 
 * @author 王定波
 * 
 */
public class BjGroupView extends GroupViewBase {

	private KingTellerEditText troubleType;
	private KingTellerEditText installBjWlId;
	private KingTellerEditText downBjWlId;
	private KingTellerEditText isChangeModule;
	private KingTellerEditText troubleRemarkId;
	private KingTellerEditText handleSubId;
	private KingTellerEditText troubleModule;
	private LinearLayout layout_wl;
	private KingTellerEditText otherDescription;
	//private AutoCompleteTextView autoComplete;
	
	public BjGroupView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BjGroupView(Context context, boolean isdel) {
		super(context, isdel);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		LayoutInflater.from(getContext()).inflate(R.layout.item_add_bj, this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 0);
		setLayoutParams(lp);

		if (isdel)
			findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
		else
			findViewById(R.id.btn_delete).setVisibility(View.GONE);

		findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((LinearLayout) getParent()).removeView(BjGroupView.this);
			}
		});

		findViewById(R.id.btn_downBjWlId).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						downBjWlId.setFieldTextAndValue("");
					}
				});

		findViewById(R.id.btn_installBjWlId).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						installBjWlId.setFieldTextAndValue("");
					}
				});

		layout_wl = (LinearLayout) findViewById(R.id.layout_wl);
		troubleType = (KingTellerEditText) findViewById(R.id.troubleType);
		installBjWlId = (KingTellerEditText) findViewById(R.id.installBjWlId);
		downBjWlId = (KingTellerEditText) findViewById(R.id.downBjWlId);
		troubleModule = (KingTellerEditText) findViewById(R.id.troubleModule);
		isChangeModule = (KingTellerEditText) findViewById(R.id.isChangeModule);
		troubleRemarkId = (KingTellerEditText) findViewById(R.id.troubleRemarkId);
		handleSubId = (KingTellerEditText) findViewById(R.id.handleSubId);
		otherDescription = (KingTellerEditText) findViewById(R.id.otherDescription);
		
		troubleType.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),
						CommonSelectDataActivity.class);
				intent.putExtra(CommonSelectDataActivity.TYPE, R.array.bjmk);

				((Activity) getContext()).startActivityForResult(intent,
						CommonCodeConfig.SELECT_BJMK);
				((LinearLayout) getParent()).setTag(troubleType);
			}
		});
		
		installBjWlId.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				if (CommonUtils.isEqualEmpty(troubleModule.getFieldValue())) {
					toastMsg("故障部件必须填写");
				} else {
					Intent intent = new Intent(getContext(),
							TreeChooserActivity.class);
					intent.putExtra(CommonSelectDataActivity.TITLE, "安装物料");
					intent.putExtra(CommonSelectDataActivity.TYPE, R.array.bjwl);
					intent.putExtra(CommonSelectDataActivity.EXTRADATA,
							CommonUtils.ToString(troubleModule.getFieldValue()));
					((Activity) getContext()).startActivityForResult(intent,
							CommonCodeConfig.REQUEST_BJWL);
					((LinearLayout) getParent()).setTag(installBjWlId);
				}
			}
		});

		downBjWlId.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				if (CommonUtils.isEqualEmpty(troubleModule.getFieldValue())) {
					toastMsg("故障部件必须填写");
				} else {
					Intent intent = new Intent(getContext(),
							TreeChooserActivity.class);
					intent.putExtra(CommonSelectDataActivity.TITLE, "原装物料");
					intent.putExtra(CommonSelectDataActivity.TYPE, R.array.bjwl);
					intent.putExtra(CommonSelectDataActivity.EXTRADATA,
							CommonUtils.ToString(troubleModule.getFieldValue()));
					((Activity) getContext()).startActivityForResult(intent,
							CommonCodeConfig.REQUEST_BJWL);
					((LinearLayout) getParent()).setTag(downBjWlId);
				}
			}
		});

		
		troubleModule.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				//Intent intent = new Intent(getContext(),TreeChooserActivity.class);
				if (CommonUtils.isEqualEmpty(troubleType.getFieldValue())) {
					toastMsg("故障类别必须填写");
				} else{
					Intent intent = new Intent(getContext(),CommonSelectDataActivity.class);
					intent.putExtra(CommonSelectDataActivity.TITLE, "故障部件");
					intent.putExtra(CommonSelectDataActivity.TYPE, R.array.bjwl);
	
					intent.putExtra(CommonSelectDataActivity.EXTRADATA, CommonUtils
							.isEmpty(troubleType.getFieldValue()) ? "0"
							: troubleType.getFieldValue());
					((Activity) getContext()).startActivityForResult(intent,
							CommonCodeConfig.REQUEST_GZBJ);
					((LinearLayout) getParent()).setTag(troubleModule);
				}
			}
		});

		
		isChangeModule.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				if (data.getValue().equals("1")) {
					layout_wl.setVisibility(View.VISIBLE);
				} else if (data.getValue().equals("2")) {
					layout_wl.setVisibility(View.GONE);
				}
			}
		});

		
		troubleRemarkId.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				if (CommonUtils.isEqualEmpty(troubleType.getFieldValue())) {
					toastMsg("故障类别必须填写");
				} else{
					Intent intent = new Intent(getContext(),
							CommonSelectDataActivity.class);
					intent.putExtra(CommonSelectDataActivity.TYPE, R.array.gzms);
					intent.putExtra(CommonSelectDataActivity.EXTRADATA, troubleType.getFieldValue());
					intent.putExtra(CommonSelectDataActivity.TROUBLEMODULE,CommonUtils.ToString(troubleModule.getFieldValue()));
	
					((Activity) getContext()).startActivityForResult(intent,
							CommonCodeConfig.SELECT_GZMS);
					((LinearLayout) getParent()).setTag(troubleRemarkId);
				}
			}
		});

		
		handleSubId.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				if (CommonUtils.isEqualEmpty(troubleRemarkId.getFieldValue())) {
					toastMsg("故障描述必须填写");
				}else{
					Intent intent = new Intent(getContext(),
							CommonSelectClgcActivity.class);
					intent.putExtra(CommonSelectDataActivity.TYPE, R.array.clgc);
					intent.putExtra(CommonSelectDataActivity.EXTRADATA,
							CommonUtils.isEqualEmpty(troubleRemarkId.getFieldValue())?"":
								troubleRemarkId.getFieldValue());
					
					((Activity) getContext()).startActivityForResult(intent,
							CommonCodeConfig.SELECT_CLGC);
					((LinearLayout) getParent()).setTag(handleSubId);
				}	
			}
		});		
		
		isChangeModule.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.radios01));
		isChangeModule.setFieldTextAndValueFromValue("1");
		
		isChangeModule.setOnChangeListener(new OnChangeListener(){

			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				if(data.getValue().equals("0")){
					installBjWlId.setFieldTextAndValue("");
					installBjWlId.setFieldEnabled(false);
					downBjWlId.setFieldTextAndValue("");
					downBjWlId.setFieldEnabled(false);
				}else if(data.getValue().equals("1")){
					downBjWlId.setFieldEnabled(true);
					installBjWlId.setFieldEnabled(true);
				}
			}
		});
		
	}
	
	@Override
	public boolean checkData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BJBean getData() {
		// TODO Auto-generated method stub
		BJBean bean = new BJBean();
		bean.setTroubleModule(troubleModule.getFieldValue());
		bean.setTroubleModuleLike(troubleModule.getFieldText());
		bean.setTroubleType(troubleType.getFieldValue());
		bean.setTroubleTypeLike(troubleType.getFieldText());
		bean.setDownBjWlId(downBjWlId.getFieldValue());
		bean.setDownBjWlName(downBjWlId.getFieldText());
		bean.setInstallBjWlId(installBjWlId.getFieldValue());
		bean.setInstallBjWlName(installBjWlId.getFieldText());
		bean.setHandleSubId(handleSubId.getFieldValue());
		bean.setHandleSub(handleSubId.getFieldText());
		if(!CommonUtils.isEmpty(otherDescription.getFieldValue())){
			bean.setRemark(otherDescription.getFieldValue());
		}else{
			bean.setRemark("");
		}
		bean.setTroubleRemarkId(troubleRemarkId.getFieldValue());
		bean.setTroubleRemark(troubleRemarkId.getFieldText());
		bean.setHandleSub(handleSubId.getFieldText());

		bean.setIsChangeModule(Long.valueOf(isChangeModule.getFieldValue()));

		return bean;
	}

	public void setData(BJBean data) {

		troubleType.setFieldTextAndValue(data.getTroubleTypeLike(),
				data.getTroubleType());
		installBjWlId.setFieldTextAndValue(data.getInstallBjWlName(),
				data.getInstallBjWlId());
		downBjWlId.setFieldTextAndValue(data.getDownBjWlName(),
				data.getDownBjWlId());
		troubleRemarkId.setFieldTextAndValue(data.getTroubleRemark(),
				data.getTroubleRemarkId());
		handleSubId.setFieldTextAndValue(data.getHandleSub(),
				data.getHandleSubId());
		troubleModule.setFieldTextAndValue(data.getTroubleModuleLike(),
				data.getTroubleModule());
		otherDescription.setFieldTextAndValue(data.getRemark());
		isChangeModule.setFieldTextAndValueFromValue(String.valueOf(data
				.getIsChangeModule()));
	}

}
