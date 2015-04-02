package com.kingteller.client.activity.workorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.workorder.AssignWorkerNameBean;
import com.kingteller.client.bean.workorder.MachineinfoSimpleBean;
import com.kingteller.client.bean.workorder.RapairSendOrderBean;
import com.kingteller.client.bean.workorder.ReturnBackStatus;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.CommonSelcetUtils;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.GroupListView;
import com.kingteller.client.view.GroupListView.AddViewCallBack;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.client.view.KingTellerEditText;
import com.kingteller.client.view.KingTellerEditText.OnChangeListener;
import com.kingteller.client.view.KingTellerEditText.OnDialogClickLister;
import com.kingteller.client.view.WhryGroupView;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class RapairSendOrderActivity extends BaseActivity implements
		OnClickListener {

	private KingTellerEditText machine;
	private KingTellerEditText trouble;
	private KingTellerEditText isyy;
	private KingTellerEditText selectTime;
	private static GroupListView weihu_group_list;
	private Button btn_submit;
	private Button btn_cancel;
	private RapairSendOrderBean rsob = new RapairSendOrderBean();
	private MachineinfoSimpleBean misb;
	public Gson gson = new Gson();
	private Calendar c = Calendar.getInstance();

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_repair_send_order);
		initUI();
		initData();
	}

	private void initUI() {
		// TODO Auto-generated method stub

		title_title.setText("新建工单");
		title_left.setOnClickListener(this);

		machine = (KingTellerEditText) findViewById(R.id.machine);
		trouble = (KingTellerEditText) findViewById(R.id.trouble);
		isyy = (KingTellerEditText) findViewById(R.id.isyy);
		selectTime = (KingTellerEditText) findViewById(R.id.selectTime);
		weihu_group_list = (GroupListView) findViewById(R.id.group_list);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_submit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		weihu_group_list.setAddViewCallBack(new AddViewCallBack() {

			@Override
			public void addView(GroupListView view) {
				// TODO Auto-generated method stub
				WhryGroupView wgview = new WhryGroupView(
						RapairSendOrderActivity.this, true);
				((TextView) wgview.findViewById(R.id.numId)).setText(String
						.valueOf(weihu_group_list.getLayoutList()
								.getChildCount() + 1)
						+ ".");
				view.addItem(wgview);
			}
		});

		machine.setOnDialogClickLister(new OnDialogClickLister() {

			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RapairSendOrderActivity.this,
						MachineSearchActivity.class);
				startActivityForResult(intent,
						CommonCodeConfig.SELECT_RAPAIRSENDORDER);
			}
		});

		isyy.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChanged(CommonSelectData data) {
				// TODO Auto-generated method stub
				if (data.getValue().equals("1")) {
					selectTime.setVisibility(View.VISIBLE);
					String time = c.get(Calendar.YEAR) + "-"
							+ (c.get(Calendar.MONTH) + 1) + "-"
							+ (c.get(Calendar.DAY_OF_MONTH) + 1) + " "
							+ "09:00:00";
					selectTime.setFieldTextAndValue(time);
				} else if (data.getValue().equals("0")) {
					selectTime.setVisibility(View.GONE);
					selectTime.setFieldTextAndValue("");
				}
			}
		});
		
		CommonUtils.hideInputMethod(this);

	}

	public void initData() {
		isyy.setLists(CommonSelcetUtils
				.getSelectList(CommonSelcetUtils.radios01));
		isyy.setFieldTextAndValueFromValue("0");

		for (int i = 0; i < 3; i++) {
			WhryGroupView wgview;
			if(i == 0){
				wgview = new WhryGroupView(
						RapairSendOrderActivity.this, false);
			}else{
				wgview = new WhryGroupView(
						RapairSendOrderActivity.this, true);
			}
			((TextView) wgview.findViewById(R.id.numId)).setText(String.valueOf(i + 1) + ".");
			weihu_group_list.addItem(wgview);
		}
	}

	public static void resetNumId(){
		int count = weihu_group_list.getLayoutList().getChildCount();
		for(int i = 0;i < count;i ++){
			((TextView)((LinearLayout)weihu_group_list.getLayoutList().getChildAt(i)).findViewById(R.id.numId))
			.setText(String.valueOf(i + 1) + ".");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CommonCodeConfig.SELECT_RAPAIRSENDORDER:
			if (resultCode == RESULT_OK) {
				misb = (MachineinfoSimpleBean) data.getSerializableExtra("machineData");
				if (misb != null) {
					machine.setFieldTextAndValue(misb.getJqbh() + "("
							+ misb.getAtmh() + ")",misb.getSsbscid());
					getAvailableWorker(misb.getJqid());
					rsob.setJqid(misb.getJqid());
					rsob.setJqbh(misb.getJqbh());

				}
			}
			break;
		case CommonCodeConfig.SELECT_XZRY:
			if (resultCode == RESULT_OK) {
				CommonSelectData commonSelectData = (CommonSelectData) data
						.getSerializableExtra("workerNameData");
				((KingTellerEditText) ((LinearLayout) ((Button) weihu_group_list
						.getLayoutList().getTag()).getParent())
						.findViewById(R.id.editText))
						.setFieldTextAndValue(commonSelectData.getText(),
								commonSelectData.getValue());
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.btn_submit:
			if (CommonUtils.isEmpty(machine.getFieldValue())) {
				toastMsg("机器不能为空");
				return;
			}
			if (CommonUtils.isEmpty(trouble.getFieldValue())) {
				toastMsg("任务信息不能为空");
				return;
			}
			if (CommonUtils.isEmpty(isyy.getFieldValue())) {
				toastMsg("是否预约不能为空");
				return;
			}
			if (isyy.getFieldValue().equals("1")) {
				if (CommonUtils.isEmpty(selectTime.getFieldText())) {
					toastMsg("选择时间不能为空");
					return;
				}
			}
			new KTAlertDialog.Builder(RapairSendOrderActivity.this)
			.setTitle("提示")
			.setMessage("您确定要派发这张工单吗？")
			.setPositiveButton("确定",
					new KTAlertDialog.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialogInterface, int pos) {
							dialogInterface.dismiss();
							submitRapairSendOrder();
							finish();
						}
					})
			.setNegativeButton("取消",
					new KTAlertDialog.OnClickListener() {
						public void onClick(
								DialogInterface dialogInterface,
								int paramAnonymousInt) {
							dialogInterface.dismiss();
						}
					}).create().show();

			break;
		case R.id.btn_cancel:
			new KTAlertDialog.Builder(RapairSendOrderActivity.this)
					.setTitle("提示")
					.setMessage("您确定要取消这张工单吗？")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									dialogInterface.dismiss();
									finish();
								}
							})
					.setNegativeButton("取消",
							new KTAlertDialog.OnClickListener() {
								public void onClick(
										DialogInterface dialogInterface,
										int paramAnonymousInt) {
									dialogInterface.dismiss();
								}
							}).create().show();
			break;
		default:
			break;
		}
	}

	private void getAvailableWorker(String jqId) {
		KTHttpClient fh = new KTHttpClient(true);
		// 获取condition实例
		AjaxParams params = new AjaxParams();
		params.put("jqId", jqId);

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.XttjwhryUrl), params,
				new AjaxHttpCallBack<BasePageBean<AssignWorkerNameBean>>(this,
						new TypeToken<BasePageBean<AssignWorkerNameBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(
							BasePageBean<AssignWorkerNameBean> basePageBean) {
						setData(basePageBean.getList());
					};
				});

	}

	private void setData(List<AssignWorkerNameBean> data) {
		if (data.size() != 0) {
			String workFlag = "";
			if (data.get(0).getWorkFlag() == 0) {
				workFlag = "休假";
			} else if (data.get(0).getWorkFlag() == 2) {
				workFlag = "请假";
			} else {
				workFlag = "空闲";
			}
			KingTellerEditText editText = ((KingTellerEditText) ((LinearLayout) weihu_group_list
					.getChildAt(0).getParent()).findViewById(R.id.editText));
			/*
			 * editTex.setFieldTextAndValue(data.get(0).getUserName()+"    "+"系统推荐"
			 * +"\n"+data.get(0).getUserAccount()
			 * +"    "+data.get(0).getLinkPhone
			 * ()+"    "+workFlag,data.get(0).getUserId());
			 */
			CommonSelectData commonSelectData = new CommonSelectData();
			commonSelectData.setText(data.get(0).getUserName() + "\t\t"
					+ "系统推荐" + "\n" + data.get(0).getUserAccount() + "\t\t"
					+ data.get(0).getLinkPhone() + "\t\t" + workFlag);
			commonSelectData.setValue(data.get(0).getUserId());
			editText.setFieldTextAndValue(commonSelectData);
		}
	}

	private String getFromData() {
		rsob.setTroubleRemark(trouble.getFieldText());
		rsob.setArrangeType(isyy.getFieldValue());
		if (isyy.getFieldValue().equals("1")) {
			rsob.setPrearrangeDateStr(selectTime.getFieldText());
		} else {
			rsob.setPrearrangeDateStr("");
		}
		List<AssignWorkerNameBean> lists = new ArrayList<AssignWorkerNameBean>();
		AssignWorkerNameBean awnb;
		int count = weihu_group_list.getLayoutList().getChildCount();
		KingTellerEditText editText;
		String[] str1;
		String[] str2;
		String[] str3;

		for (int i = 0; i < count; i++) {
			editText = (KingTellerEditText) ((LinearLayout) (weihu_group_list
					.getLayoutList().getChildAt(i)))
					.findViewById(R.id.editText);
			if (!CommonUtils.isEmpty(editText.getFieldValue())) {
				str1 = editText.getFieldText().toString().split("\n");
				str2 = str1[0].split("\t\t");
				str3 = str1[1].split("\t\t");
				awnb = new AssignWorkerNameBean();
				awnb.setUserName(str2[0]);
				if (str2.length == 2) {
					awnb.setSystemProposeFlag("1");
				} else {
					awnb.setSystemProposeFlag("0");
				}
				awnb.setUserAccount(str3[0]);
				awnb.setLinkPhone(str3[1]);
				awnb.setUserId(editText.getFieldStringValue());
				lists.add(awnb);
			}
		}

		rsob.setWnList(lists);

		return ConditionUtils.getJsonFromObj(rsob);
	}

	private void submitRapairSendOrder() {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		// 获取condition实例
		AjaxParams params = new AjaxParams();
		params.put("assign", getFromData());
		params.put("flag", "1");
		RapairSendOrderBean rsob = gson.fromJson(getFromData(),
				new TypeToken<RapairSendOrderBean>() {
				}.getType());
		if (rsob.getWnList().size() <= 0) {
			toastMsg("维护人员不能为空");
			return;
		}
		for (int i = 0; i < rsob.getWnList().size(); i++) {
			for (int j = rsob.getWnList().size() - 1; j > i; j--) {
				if (rsob.getWnList().get(j).getUserAccount().equals(rsob.getWnList().get(i).getUserAccount())) {
					toastMsg(rsob.getWnList().get(j).getUserName()+"只能选择一次");
					return;
				}
			}
		}

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.TjpdUrl), params,
				new AjaxHttpCallBack<ReturnBackStatus>(this,
						new TypeToken<ReturnBackStatus>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(ReturnBackStatus data) {
						if (data.getResult().equals("success")) {
							finish();
						}
						toastMsg(data.getMessage());
					};
				});

	}

}
