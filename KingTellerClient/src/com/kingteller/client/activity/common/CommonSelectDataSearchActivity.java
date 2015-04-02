package com.kingteller.client.activity.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.CommonSelectDataAdapter;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.selectdata.ATMCodeBean;
import com.kingteller.client.bean.common.selectdata.WorkServiceStateBean;
import com.kingteller.client.bean.workorder.AssignWorkerNameBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.ConditionView;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 通用条件数据选择
 * @author 王定波
 *
 */
public class CommonSelectDataSearchActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {
	
	private ConditionView conditionView;// 通用查询条件
	private List<CommonSelectData> listData = new ArrayList<CommonSelectData>();
	private CommonSelectDataAdapter adapter;
	private int currentPage = 1;
	private String[] typedatas;
	private int type;
	private final static int KEY_TITLE=0;
	private final static int KEY_FIELD_NAME=1;
	private final static int KEY_FIELD_TITLE=2;
	private final static int KEY_URL=3;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_common_select_data_search);
		initUI();
	    getData();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		// TODO Auto-generated method stub
		type=getIntent().getIntExtra(CommonSelectDataActivity.TYPE, 0);
		typedatas=getResources().getStringArray(type);
		
		title_title.setText(typedatas[KEY_TITLE]);
		title_right.setText("搜索");
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);

		conditionView = (ConditionView) findViewById(R.id.condition);
		conditionView.setFieldTitle(typedatas[KEY_FIELD_TITLE]);
		adapter = new CommonSelectDataAdapter(this, listData);
		getListviewObj().getListview().setAdapter(adapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(true);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);
	}

	private void getData() {
		
		if (currentPage == 1) {
			getListviewObj().setStatus(LoadingEnum.LOADING);
		}

		switch (type) {
		case R.array.serverState://服务站
			getServerState();
			break;
		case R.array.atmcode://ATM
			getAtm();
			break;

		default:
			break;
		}
		
		
	}
	
	/**
	 * 获取机型
	 */
	private void getAtm() {
		// TODO Auto-generated method stub
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params =new AjaxParams();
		params.put(typedatas[KEY_FIELD_NAME],conditionView.getFieldValue());
		params.put("page", String.valueOf(currentPage));
		
		fh.post(ConfigUtils.CreateUrl(this,typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<ATMCodeBean>>(this,
						new TypeToken<BasePageBean<ATMCodeBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<ATMCodeBean> basePageBean) {
						BasePageBean<CommonSelectData> pagedata=(BasePageBean) basePageBean;
						List<CommonSelectData> lists=new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectData data=new CommonSelectData();
							data.setText(basePageBean.getList().get(i).getJqbh());
							data.setValue(basePageBean.getList().get(i).getJqbh());
							data.setObj(basePageBean.getList().get(i));
							lists.add(data);
						}
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}

	/**
	 * 得到服务站
	 */
	public void getServerState()
	{
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params =new AjaxParams();
		params.put(typedatas[KEY_FIELD_NAME],conditionView.getFieldValue());
		params.put("page", String.valueOf(currentPage));
		
		fh.post(ConfigUtils.CreateUrl(this,typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<WorkServiceStateBean>>(this,
						new TypeToken<BasePageBean<WorkServiceStateBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<WorkServiceStateBean> basePageBean) {
						BasePageBean<CommonSelectData> pagedata=(BasePageBean) basePageBean;
						List<CommonSelectData> lists=new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectData data=new CommonSelectData();
							data.setText(basePageBean.getList().get(i).getOrgName());
							data.setValue(basePageBean.getList().get(i).getOrgId());
							lists.add(data);
						}
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}
	
	
	
	private void setData(BasePageBean<CommonSelectData> pagedata)
	{
		List<CommonSelectData> data = pagedata.getList();
		if (data.size() > 0) {
			if (pagedata.getCurrentPage() == 1)
				adapter.setLists(data);
			else
				adapter.addLists(data);

			getListviewObj().setStatus(LoadingEnum.LISTSHOW);
			// 如果当前页等于总页数，则不需要先是load more按钮
			if (pagedata.getCurrentPage() == pagedata
					.getTotalPage())
				getListviewObj().getListview()
						.setLoadMoreEnabled(false);

		} else {
			getListviewObj().setStatus(LoadingEnum.NODATA,
					"没有数据");
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_left:
			this.finish();
			break;
		case R.id.button_right:
			onRefresh();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int pos, long arg3) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.putExtra(CommonSelectDataActivity.DATA, (CommonSelectData) adapterView.getItemAtPosition(pos));
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		getData();
		getListviewObj().getListview().setLoadMoreEnabled(true);
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		getData();
	}
}