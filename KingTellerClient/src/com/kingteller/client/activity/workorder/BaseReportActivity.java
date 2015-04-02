package com.kingteller.client.activity.workorder;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseUploadActivity;
import com.kingteller.client.activity.common.CommonSelectDataActivity;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.TreeBean;
import com.kingteller.client.bean.workorder.FreeData;
import com.kingteller.client.bean.workorder.ReturnBackStatus;
import com.kingteller.client.bean.workorder.WorkOrderBean;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.view.FeeGroupView;
import com.kingteller.client.view.FeeRPGroupView;
import com.kingteller.client.view.GroupListView;
import com.kingteller.client.view.GroupPicGridView.OnItemClickLister;
import com.kingteller.client.view.GroupUploadFileView;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.client.view.KingTellerEditText;
import com.kingteller.client.view.KingTellerScrollView;
import com.kingteller.client.view.KingTellerScrollView.ScrollViewListener;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public abstract class BaseReportActivity extends BaseUploadActivity implements
		OnClickListener {
	protected GroupListView fee_group_list;
	private int selectPos;
	private GroupUploadFileView Gpview;
	protected WorkOrderBean workBean;
	protected Button btn_temp_save;
	protected Button btn_submit;
	protected Button btn_back;
	protected Button btn_sp;
	protected KingTellerScrollView layout_scroll;
	private int sx = 0;// 记录位置
	private int sy = 0;// 记录位置
	protected String dataKey = "assign";
	public final static int OPT_ADUIT = 1;// 审核类型
	public final static String OPT_TYPE = "optType";// 类型字段
	protected int optType = 0;
	private LinearLayout layout_audit_common;
	protected KingTellerEditText auditRemark;
	protected KingTellerEditText auditContent;
	protected TextView auditTitle;

	public final static String OPT_STATE_TYPE = "stateType";// 未处理和已处理
	public final static String OPT_UNTREATED = "untreated";
	public final static String OPT_PROCESSED = "processed";
	private String state_type = "";
	protected String isCheckout = "";// 查看报告

	private LinearLayout layout_hide;
	private ImageButton hide;
	private Long backflag;
	

	public void initData() {
		// TODO Auto-generated method stub
		// 获取传递过来的数据
		workBean = (WorkOrderBean) this.getIntent().getExtras()
				.getSerializable("reportBean");

	}

	public void initUI() {
		// TODO Auto-generated method stub
		setSelectAllFile(true);
		optType = getIntent().getIntExtra(OPT_TYPE, 0);
		state_type = getIntent().getStringExtra(OPT_STATE_TYPE);
		isCheckout = getIntent().getStringExtra("isCheckout");
		backflag = getIntent().getLongExtra("backflag", 0);
		fee_group_list = (GroupListView) findViewById(R.id.group_list);
		if (!CommonUtils.isEmpty(dataKey) && dataKey.equals("assign")) {
			//维护报告费用类型
			fee_group_list.addItem(new FeeRPGroupView(BaseReportActivity.this,
					false));
		} else
			fee_group_list.addItem(new FeeGroupView(BaseReportActivity.this,
					false));
		fee_group_list.setAddBtnHidden(true);
		Gpview = (GroupUploadFileView) findViewById(R.id.item_add_pic);
		// 附件
		Gpview.setOnItemClickLister(new OnItemClickLister() {

			@Override
			public void OnItemClick(View view, int pos) {
				// TODO Auto-generated method stub
				selectPos = pos;
				popdialog.show();
			}
		});
		title_left.setOnClickListener(this);

		layout_audit_common = (LinearLayout) findViewById(R.id.layout_audit_common);
		auditRemark = (KingTellerEditText) findViewById(R.id.auditRemark);
		auditContent = (KingTellerEditText) findViewById(R.id.auditContent);
		auditTitle = (TextView) findViewById(R.id.auditTitle);

		btn_temp_save = (Button) findViewById(R.id.btn_temp_save);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_sp = (Button) findViewById(R.id.btn_sp);

		layout_hide = (LinearLayout) findViewById(R.id.layout_hide);
		hide = (ImageButton) findViewById(R.id.hide);

		btn_submit.setOnClickListener(this);
		btn_temp_save.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_sp.setOnClickListener(this);
		title_right.setOnClickListener(this);
		hide.setOnClickListener(this);

		layout_scroll = (KingTellerScrollView) findViewById(R.id.layout_scroll);
		if (layout_scroll != null) {
			layout_scroll.setScrollViewListener(new ScrollViewListener() {

				@Override
				public void onScrollChanged(KingTellerScrollView scrollView,
						int x, int y, int oldx, int oldy) {
					// TODO Auto-generated method stub
					// Logger.e("pos", sx+"_"+sy+"_"+x+"_"+y+"_"+oldx+"_"+oldy);
					if (oldx == sx && oldy == sy && y != sy && sy != 0) {
						sx = 0;
						sy = 0;
						layout_scroll.scrollTo(oldx, oldy);

					}
				}
			});
		}

		if (optType == BaseReportActivity.OPT_ADUIT) {
			// 设置信息不可写 只可以看
			LinearLayout layout_form = (LinearLayout) findViewById(R.id.layout_form);
			if (layout_form != null) {
				int length = layout_form.getChildCount();
				for (int i = 0; i < length; i++) {
					if (layout_form.getChildAt(i).getClass().getName()
							.equals(KingTellerEditText.class.getName())) {
						((KingTellerEditText) layout_form.getChildAt(i))
								.setFieldEnabled(false);
					}
				}
			}
		}

		switch (optType) {
		case OPT_ADUIT:
			if (state_type.equals(OPT_PROCESSED)) {
				layout_audit_common.setVisibility(View.VISIBLE);
				onlyRead();
				btn_submit.setVisibility(View.GONE);
				btn_temp_save.setVisibility(View.GONE);
				btn_back.setVisibility(View.GONE);
				btn_sp.setVisibility(View.GONE);
			} else {
				layout_audit_common.setVisibility(View.VISIBLE);
				btn_submit.setVisibility(View.GONE);
				btn_temp_save.setVisibility(View.GONE);
			}
			break;
		default:
			if (backflag > 0) {
				onlyRead();
				layout_audit_common.setVisibility(View.VISIBLE);
			} else {
				layout_audit_common.setVisibility(View.GONE);
			}
			if (CommonUtils.isEmpty(isCheckout)) {
			} else {
				btn_submit.setVisibility(View.GONE);
				btn_temp_save.setVisibility(View.GONE);
			}
			btn_back.setVisibility(View.GONE);
			btn_sp.setVisibility(View.GONE);
			break;
		}

		// 加载背景不透明
		getListviewObj().setBackground(false);
		CommonUtils.hideInputMethod(this);

	}

	public void onlyRead() {
		int length = layout_audit_common.getChildCount();
		for (int i = 0; i < length; i++) {
			if (layout_audit_common.getChildAt(i).getClass().getName()
					.equals(KingTellerEditText.class.getName())) {
				((KingTellerEditText) layout_audit_common.getChildAt(i))
						.setFieldEnabled(false);
			}
		}
	}

	/**
	 * 文件返回操作
	 */
	@Override
	public void OnCallBackPhoto(String picPath) {
		// TODO Auto-generated method stub
		super.OnCallBackPhoto(picPath);
		Gpview.setItemImageView(selectPos, picPath);
		Gpview.addPicItem();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CommonCodeConfig.REQUEST_FEETYPE:
			if (resultCode == RESULT_OK) {
				fee_group_list.setItemData(((TreeBean) data
						.getSerializableExtra(CommonSelectDataActivity.DATA))
						.getCommonSelectData());
			}
			break;
		case CommonCodeConfig.REQUEST_MODE:
			if (resultCode == RESULT_OK) {
				if (workBean.getOrderType().equals("maintenance")) {
					fee_group_list
							.setItemDataMul((CommonSelectData) data
									.getSerializableExtra(CommonSelectDataActivity.DATA));
				} else {
					fee_group_list
							.setItemData((CommonSelectData) data
									.getSerializableExtra(CommonSelectDataActivity.DATA));
				}
			}
			break;
		default:
			break;
		}

	}

	public boolean checkNet(boolean isSubmit) {
		if (!CommonUtils.isNetAvaliable(this)) {
			new KTAlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("网络不可用，是否离线保存？")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									KingTellerUtils.saveReportToDataBase(
											BaseReportActivity.this,
											workBean.getOrderId(),
											workBean.getOrderType(), 0,
											getFromData());
									dialogInterface.dismiss();
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

			return false;
		} /*else if (!CommonUtils.isWifi(this) && !isSubmit) {
			new KTAlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("你的网络不是WIFI网络，是否上传附件？")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
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
			return false;
		}*/

		return true;
	}

	public abstract String getFromData();

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
		sx = layout_scroll.getScrollX();
		sy = layout_scroll.getScrollY();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {

		case R.id.btn_sp:
			new KTAlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("您确定要审批报告吗?")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									doAuditFlow(0);
									dialogInterface.dismiss();
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
		case R.id.btn_back:
			if(CommonUtils.isEmpty(auditContent.getFieldValue())){
				toastMsg("需要填写退回意见");
				return;
			}
			new KTAlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("您确定要退回该报告吗?")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									doAuditFlow(1);
									dialogInterface.dismiss();
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
		case R.id.btn_temp_save:
			if (checkNet(false))
				submitReport(1);
			break;
		case R.id.btn_submit:
			if (checkNet(true))
				submitReport(2);

			break;
		case R.id.button_left:
			returnBack();
			break;
		case R.id.hide:
			if(layout_hide.getVisibility() == View.GONE){
				layout_hide.setVisibility(View.VISIBLE);
				hide.setBackground(getResources().getDrawable(R.drawable.arrow_up));
			}else if(layout_hide.getVisibility() == View.VISIBLE){
				hide.setBackground(getResources().getDrawable(R.drawable.arrow_down));
				layout_hide.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 提交报告
	 * 
	 * @param flag
	 *            flag:1：暂存 2：提交　
	 */
	public void submitReport(final int flag) {
		
		if (flag == 2) {
			List<FreeData> fcost = fee_group_list.getListData();
			for (int i = 0; i < fcost.size(); i++) {
				if(CommonUtils.isEmpty(fcost.get(i).getFeeType())){
					toastMsg("费用类型不能为空");
					return;
				}
				if(CommonUtils.isEmpty(fcost.get(i).getFeeMoney()) || fcost.get(i).getFeeMoney().equals(0)){
					toastMsg("费用金额不能为空");
					return;
				}
				if(fcost.get(i).getFeeType().contains("维护车费") || fcost.get(i).getFeeType().equals("车船费")){
					if(CommonUtils.isEmpty(fcost.get(i).getFeeMode())){
						toastMsg("交通工具不能为空");
						return;
					}
					if(CommonUtils.isEmpty(fcost.get(i).getBusLine())){
						toastMsg("费用说明不能为空");
						return;
					}
				}
			}
		}

		final String assign = getFromData();
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put(dataKey, assign);
		ajaxParams.put("flag", String.valueOf(flag));

		fh.post(ConfigUtils.CreateNoVersionUrl(this, URLConfig.otherMatterUrl),
				ajaxParams, new AjaxHttpCallBack<ReturnBackStatus>(this, true) {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						KingTellerProgress.showProgress(
								BaseReportActivity.this, "正在保存数据...");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						KingTellerProgress.closeProgress();
					}

					@Override
					public void onError(int errorNo, String strMsg) {
						// TODO Auto-generated method stub
						KingTellerUtils.saveReportToDataBase(
								BaseReportActivity.this, workBean.getOrderId(),
								workBean.getOrderType(), 0, assign);
					}

					@Override
					public void onDo(ReturnBackStatus data) {
						// TODO Auto-generated method stub
						if (data.getResult().equals("success")) {
							if (flag == 1) {
								toastMsg("保存成功");
								KingTellerUtils.saveReportToDataBase(
										BaseReportActivity.this,
										workBean.getOrderId(),
										workBean.getOrderType(), 1, assign);
							} else if (flag == 2) {
								KingTellerUtils.deleteReportFromDataBase(
										BaseReportActivity.this,
										workBean.getOrderId(),
										workBean.getOrderType());
								/*SharedPreferencesUtils.saveData(BaseReportActivity.this, workBean.getOrderId(), 
										TimeFormatUtils.getStringFromDate(new Date()));*/
								
								toastMsg(data.getMessage());
								finish();
							}

						} else {
							KingTellerUtils.saveReportToDataBase(
									BaseReportActivity.this,
									workBean.getOrderId(),
									workBean.getOrderType(), 0, assign);
							toastMsg(data.getMessage());
						}

					}
				});
	}

	private void back() {
		if (optType == 0) {
			String data = KingTellerUtils.getReportDataFromDataBase(this,
					workBean.getOrderId());
			try {
				final String fromdata = getFromData();

				if (!data.equals(fromdata))
					new KTAlertDialog.Builder(this)
							.setTitle("提醒")
							.setMessage("您的数据尚未提交，是否保存本地？")
							.setPositiveButton("保存",
									new KTAlertDialog.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialogInterface,
												int pos) {
											KingTellerUtils
													.saveReportToDataBase(
															BaseReportActivity.this,
															workBean.getOrderId(),
															workBean.getOrderType(),
															0, fromdata);
											dialogInterface.dismiss();
											finish();

										}
									})
							.setNegativeButton("不保存",
									new KTAlertDialog.OnClickListener() {
										public void onClick(
												DialogInterface dialogInterface,
												int paramAnonymousInt) {
											dialogInterface.dismiss();
											finish();
										}
									}).create().show();
				else
					finish();
			} catch (Exception e) {
				// TODO: handle exception
				finish();
			}

		} else
			finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		returnBack();	
	}

	private void returnBack(){
		if(!CommonUtils.isEmpty(isCheckout)){
			finish();
		}else{
			back();
		}
	}
	
	/**
	 * 审批
 	 * @param flag
	 *            1退回 0审批
	 */
	private void doAuditFlow(final int flag) {

		String assign = getFromData();
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put(dataKey, assign);
		ajaxParams.put("flag", String.valueOf(flag));

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.SfthUrl), ajaxParams,
				new AjaxHttpCallBack<ReturnBackStatus>(this, true) {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						KingTellerProgress.showProgress(
								BaseReportActivity.this, "正在提交...");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						KingTellerProgress.closeProgress();
					}

					@Override
					public void onDo(ReturnBackStatus data) {
						toastMsg(data.getMessage());
						if (data.getResult().equals("success")) {
							finish();
						}

					};
				});
	}

}
