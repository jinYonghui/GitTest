package com.kingteller.client.activity.workorder;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.activity.common.CommonSelectDataActivity;
import com.kingteller.client.adapter.SendOrderAdapter;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.KingTellerDateTime;
import com.kingteller.client.bean.workorder.SendOrderBean;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.ConditionView;
import com.kingteller.client.view.KingTellerEditText.OnDialogClickLister;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.condition.Condition;
import com.kingteller.framework.http.AjaxParams;

public class SendOrderSearchListActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {
	private ConditionView workDateRange1;
	private ConditionView workDateRange2;
	private ConditionView workerName;
	private ConditionView wdsbjc;
	private ConditionView jqbh;
	private KingTellerDateTime kingTellerDateTime;
	private List<SendOrderBean> workOrderList = new ArrayList<SendOrderBean>();
	private SendOrderAdapter adapter;
	private int currentPage = 1;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_sendorder_search);
		initUI();
		initData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CommonCodeConfig.SELECT_CXFWZRY:
			if (resultCode == RESULT_OK) {
				CommonSelectData cdata = (CommonSelectData) data
						.getSerializableExtra(CommonSelectDataActivity.DATA);
				workerName.setFieldTextAndValue(cdata.getText(), cdata.getValue());
			}
			break;
		default:
			break;
		}
	}
	
	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("查询派单");
		title_right.setBackgroundResource(R.drawable.icon_search);
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);

		workerName = (ConditionView) findViewById(R.id.workerName);
		wdsbjc = (ConditionView) findViewById(R.id.wdsbjc);
		jqbh = (ConditionView) findViewById(R.id.jqbh);
		workDateRange1 = (ConditionView) findViewById(R.id.workDateRange1);
		workDateRange2 = (ConditionView) findViewById(R.id.workDateRange2);
		adapter = new SendOrderAdapter(this, workOrderList, "search");

		kingTellerDateTime = new KingTellerDateTime().initNow();
		getListviewObj().getListview().setAdapter(adapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(true);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);
		getListviewObj().setStatus(LoadingEnum.NODATA, "请输入条件进行搜索");
		
		workerName.setOnDialogClickLister(new OnDialogClickLister() {
			
			@Override
			public void OnDialogClick() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendOrderSearchListActivity.this,
						CommonSelectDataActivity.class);
				intent.putExtra(CommonSelectDataActivity.TYPE, R.array.cxfwzry);
				startActivityForResult(intent, CommonCodeConfig.SELECT_CXFWZRY);
			}
		});
		
		User user = User.getInfo(this);
		if(user.getRoleCode().contains("FWZFZR_COMMON")){
			workerName.setVisibility(View.GONE);
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		workDateRange1.setFieldTextAndValue(kingTellerDateTime.getDateString());
		workDateRange2.setFieldTextAndValue(kingTellerDateTime.getDateString());
	}

	public void getSendOrderDatas() {
		if (currentPage == 1) {
			getListviewObj().setStatus(LoadingEnum.LOADING);
		}
		KTHttpClient fh = new KTHttpClient(true);
		// 获取condition实例
		LinearLayout conditionLayout = (LinearLayout) findViewById(R.id.workorder_condition);
		Condition cond = ConditionUtils.getCondition(this, conditionLayout);
		
		AjaxParams params = ConditionUtils.getAjaxParams(cond, currentPage);
		params.put("workDateRange1", workDateRange1.getFieldValue());
		params.put("workDateRange2", workDateRange2.getFieldValue());
		params.put("workerName", workerName.getFieldValue());
		params.put("wdsbjc",wdsbjc.getFieldValue());
		params.put("jqbh", jqbh.getFieldValue());
		params.put("flag", "2");

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.CxlspdUrl), params,
				new AjaxHttpCallBack<BasePageBean<SendOrderBean>>(this,
						new TypeToken<BasePageBean<SendOrderBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<SendOrderBean> basePageBean) {
						List<SendOrderBean> data = basePageBean.getList();
						if (data.size() > 0) {
							if (basePageBean.getCurrentPage() == 1)
								adapter.setLists(data);
							else
								adapter.addLists(data);

							getListviewObj().setStatus(LoadingEnum.LISTSHOW);
							// 如果当前页等于总页数，则不需要先是load more按钮
							if (basePageBean.getCurrentPage() == basePageBean
									.getTotalPage())
								getListviewObj().getListview()
										.setLoadMoreEnabled(false);

						} else {
							getListviewObj().setStatus(LoadingEnum.NODATA,
									"没有数据");
						}

					};

				});

	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.button_right:
			onRefresh();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		currentPage = 1;
		getSendOrderDatas();
		getListviewObj().getListview().setLoadMoreEnabled(true);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		currentPage++;
		getSendOrderDatas();
	}
}
