package com.kingteller.client.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kingteller.R;
import com.kingteller.client.activity.common.CommonSelectDataActivity;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.workorder.WorkTypeBean;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.utils.CommonSelcetUtils;
import com.kingteller.client.view.KingTellerEditText.OnDialogClickLister;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 工作类别视图
 * 
 * @author 王定波
 * 
 */
public class WorkTypeGroupView extends GroupViewBase {

	private KingTellerEditText workType;
	private KingTellerEditText costMinute;
	private KingTellerEditText handleSub;
	private KingTellerEditText remark;
	private KingTellerEditText troubleReasonCode;
	private OnChangeListener onChangeListener;
	private Button btn_add;
	private View btn_delete;

	public WorkTypeGroupView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WorkTypeGroupView(Context context, boolean isdel) {
		super(context, isdel);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		LayoutInflater.from(getContext()).inflate(R.layout.item_add_worktype,
				this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 0);
		setLayoutParams(lp);

		btn_add = (Button) findViewById(R.id.btn_add);
		btn_delete = findViewById(R.id.btn_delete);

		if (isdel){
			findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
		}
		else{
			findViewById(R.id.btn_delete).setVisibility(View.GONE);
		}

		btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((LinearLayout) getParent()).removeView(WorkTypeGroupView.this);
			}
		});

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((LinearLayout) getParent()).addView(WorkTypeGroupView.this
						.clone());

			}
		});
		btn_add.setVisibility(GONE);

		workType = (KingTellerEditText) findViewById(R.id.workType);
		costMinute = (KingTellerEditText) findViewById(R.id.costMinute);
		handleSub = (KingTellerEditText) findViewById(R.id.handleSub);
		troubleReasonCode = (KingTellerEditText) findViewById(R.id.troubleReasonCode);
		remark = (KingTellerEditText) findViewById(R.id.remark);

		workType.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.workType));
		troubleReasonCode.setLists(CommonSelcetUtils.getSelectList(CommonSelcetUtils.troubleReasonCode));

		workType.setOnChangeListener(new com.kingteller.client.view.KingTellerEditText.OnChangeListener() {

			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				handleSub.setFieldTextAndValue("");
				troubleReasonCode.setFieldTextAndValue("");
				remark.setFieldTextAndValue("");
				remark.setVisibility(View.GONE);
				findViewById(R.id.remark_view).setVisibility(View.GONE);
				/*if (data.getValue().equals("BACK_MACHINE")) {
					toastMsg("工作类别如需改成退机,请联系信息管理员");
					//workType.setFieldTextAndValueFromValue("NORMAL");
					workType.setFieldTextAndValue("");
					return;
				} else {*/
					try {
						List<WorkTypeBean> list = ((GroupListView) ((LinearLayout) ((LinearLayout) getParent())
								.getParent()).getParent()).getListData();
						List<String> listtype = new ArrayList<String>();

						for (int j = 0; j < list.size(); j++) {
							listtype.add(list.get(j).getWorkType());
						}
						
						if (listtype.contains("START")
								&& listtype.contains("MOVE_MACHINE") ) {
							toastMsg("不能同时包含移机和开通机器");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						if(listtype.contains("START") && listtype.contains("BACK_MACHINE")){
							toastMsg("不能同时包含开通机器和退机");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						if(listtype.contains("MOVE_MACHINE") && listtype.contains("BACK_MACHINE")){
							toastMsg("不能同时包含移机和退机");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						
						if(listtype.contains("RECEIVE_MACHINE") && listtype.contains("SETUP_MACHINE")){
							toastMsg("不能同时包含接机和安装机器");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						
						if(listtype.contains("RECEIVE_MACHINE") && listtype.contains("START")){
							toastMsg("不能同时包含接机和开通机器");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						
						if(listtype.contains("RECEIVE_MACHINE") && listtype.contains("DEBUG")){
							toastMsg("不能同时包含接机和调试机器");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						
						if(listtype.contains("SETUP_MACHINE") && listtype.contains("DEBUG")){
							toastMsg("不能同时包含安装机器和调试机器");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						
						if(listtype.contains("SETUP_MACHINE") && listtype.contains("START")){
							toastMsg("不能同时包含安装机器和开通机器");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						
						if(listtype.contains("START") && listtype.contains("DEBUG")){
							toastMsg("不能同时包含开通机器和调试机器");
							//workType.setFieldTextAndValueFromValue("NORMAL");
							workType.setFieldTextAndValue("");
							return;
						}
						
						for (int j = 0; j < listtype.size(); j++) {
							for(int i = j;i <listtype.size();i++){
								if( i != j && listtype.get(i).equals(listtype.get(j))){
									toastMsg("不能重复选择同一个类别:"+data.getText());
									workType.setFieldTextAndValue("");
									return;
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					handleSub.setFieldTextAndValue("");
					

				//}
				if(data.getValue().equals("NORMAL")){
					handleSub.setFieldEnabled(false);
					handleSub.setFieldTextAndValue("请在维护信息栏填写维护过程");
					((LinearLayout)findViewById(R.id.worktypeLayout)).setVisibility(View.VISIBLE);
				}else{
					((LinearLayout)findViewById(R.id.worktypeLayout)).setVisibility(View.GONE);
					handleSub.setFieldEnabled(true);
				}
				if (onChangeListener != null) {
					onChangeListener.onWorkTypeChange(WorkTypeGroupView.this,data);
				}
				
			}
		});
		handleSub.setOnDialogClickLister(new OnDialogClickLister() {
			
			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				if(CommonUtils.isEqualEmpty(workType.getFieldValue())){
					toastMsg("工作类别不能为空");
				}else{
					Intent intent = new Intent(getContext(),CommonSelectDataActivity.class);
					intent.putExtra(CommonSelectDataActivity.TITLE, "处理过程");
					intent.putExtra(CommonSelectDataActivity.TYPE, R.array.clgcworktype);
					intent.putExtra(CommonSelectDataActivity.EXTRADATA,
							CommonUtils.ToString(workType.getFieldValue()));
					((Activity) getContext()).startActivityForResult(intent,
							CommonCodeConfig.SELECT_HANDLESUB);
					((LinearLayout) getParent()).setTag(handleSub);
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
	public WorkTypeBean getData() {
		// TODO Auto-generated method stub
		WorkTypeBean bean = new WorkTypeBean();
		bean.setHandleSubId(CommonUtils.isEmpty(handleSub.getFieldValue())?"":handleSub.getFieldValue());
		//bean.setHandleSub(CommonUtils.isEmpty(handleSub.getFieldValue())?"":handleSub.getFieldValue());
		bean.setHandleSub(CommonUtils.isEmpty(handleSub.getFieldText())?"":handleSub.getFieldText());
		bean.setWorkType(CommonUtils.isEmpty(workType.getFieldValue())? "":workType.getFieldValue());
		bean.setWorkTypeLike(CommonUtils.isEmpty(workType.getFieldText())?"":workType.getFieldText());
		bean.setCostMinute(CommonUtils.isEmpty(costMinute.getFieldValue()) ? 0
				: Long.valueOf(costMinute.getFieldValue()));
		bean.setReason(CommonUtils.isEmpty(troubleReasonCode.getFieldValue())?"":
			troubleReasonCode.getFieldValue());
		bean.setRemark(CommonUtils.isEmpty(remark.getFieldText())?"":remark.getFieldText());
		return bean;
	}

	public void setData(WorkTypeBean data) {
		if(data.getWorkType().equals("NORMAL")){
			handleSub.setFieldEnabled(false);
			//handleSub.setFieldTextAndValue("请在维护信息栏填写维护过程");
			((LinearLayout)findViewById(R.id.worktypeLayout)).setVisibility(View.VISIBLE);
		}else{
			((LinearLayout)findViewById(R.id.worktypeLayout)).setVisibility(View.GONE);
			//handleSub.setFieldTextAndValue(data.getHandleSubLike(),data.getHandleSub());
			handleSub.setFieldEnabled(true);
		}
		workType.setFieldTextAndValueFromValue(data.getWorkType());
		costMinute.setFieldTextAndValue(data.getCostMinute().toString());
		troubleReasonCode.setFieldTextAndValueFromValue(data.getReason());
		if(data.getHandleSub().contains("其它")){
			remark.setVisibility(View.VISIBLE);
			findViewById(R.id.remark_view).setVisibility(View.VISIBLE);
		}else{
			remark.setVisibility(View.GONE);
			findViewById(R.id.remark_view).setVisibility(View.GONE);
		}
		remark.setFieldTextAndValue(data.getRemark());
	}

	public void setOnChangeListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}

	public interface OnChangeListener {
		public void onWorkTypeChange(WorkTypeGroupView view,
				CommonSelectData data);
	}

	@Override
	protected WorkTypeGroupView clone() {
		// TODO Auto-generated method stub
		WorkTypeGroupView ca = new WorkTypeGroupView(getContext(), true);
		ca.setOnChangeListener(onChangeListener);
		return ca;
	}
}
