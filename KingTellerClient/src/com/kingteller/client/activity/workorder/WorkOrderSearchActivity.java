package com.kingteller.client.activity.workorder;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.WorkOrderAdapter;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.KingTellerDateTime;
import com.kingteller.client.bean.map.StaffSearchCondition;
import com.kingteller.client.bean.workorder.WorkOrderBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.ConditionView;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.condition.Condition;
import com.kingteller.framework.http.AjaxParams;

/**
 * 历史任务单查询
 * 
 * @author jinyh
 * */

public class WorkOrderSearchActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {
	private ConditionView startDate;
	private ConditionView endDate;
	private ConditionView orderNo;
	private KingTellerDateTime kingTellerDateTime;
	private List<WorkOrderBean> workOrderList = new ArrayList<WorkOrderBean>();
	private WorkOrderAdapter adapter;
	private int currentPage = 1;

	private StaffSearchCondition condition = new StaffSearchCondition();

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_workorder_search);
		initUI();
		initData();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("查询工单");
		title_right.setText("搜索");
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);

		orderNo = (ConditionView) findViewById(R.id.orderNo);
		startDate = (ConditionView) findViewById(R.id.start_date);
		endDate = (ConditionView) findViewById(R.id.end_date);
		adapter = new WorkOrderAdapter(this, workOrderList,"");

		kingTellerDateTime = new KingTellerDateTime().initNow();
		getListviewObj().getListview().setAdapter(adapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(true);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);
		getListviewObj().setStatus(LoadingEnum.NODATA, "请输入条件进行搜索");
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		startDate.setFieldTextAndValue(kingTellerDateTime.getDateString());
		endDate.setFieldTextAndValue(kingTellerDateTime.getDateString());
	}

	public void getWorkOrderData() {
		if (currentPage == 1) {
			getListviewObj().setStatus(LoadingEnum.LOADING);
		}
		KTHttpClient fh = new KTHttpClient(true);
		// 获取condition实例
		LinearLayout conditionLayout = (LinearLayout) findViewById(R.id.workorder_condition);
		Condition cond = ConditionUtils.getCondition(this, conditionLayout);
		AjaxParams params = ConditionUtils.getAjaxParams(cond, currentPage);
		params.put("workDateRange1", startDate.getFieldValue());
		params.put("workDateRange2", endDate.getFieldValue());
		params.put("orderNo", orderNo.getFieldValue());
		params.put("statuflag", "5");

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.WebRwdUrl), params,
				new AjaxHttpCallBack<BasePageBean<WorkOrderBean>>(this,
						new TypeToken<BasePageBean<WorkOrderBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<WorkOrderBean> basePageBean) {
						List<WorkOrderBean> data = basePageBean.getList();
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
		currentPage=1;
		getWorkOrderData();
		getListviewObj().getListview().setLoadMoreEnabled(true);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		currentPage++;
		getWorkOrderData();
	}
}
