package com.kingteller.client.activity.workorder;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.AssignWorkerSearchAdapter;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.workorder.AssignWorkerNameBean;
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
import com.kingteller.framework.utils.CommonUtils;

public class AssignWorkerSearchActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {

	private Button btn_search;
	private ConditionView assignWorkerName;
	private AssignWorkerSearchAdapter adapter;
	private int currentPage = 1;
	private Button btn_submit;
	private Button btn_cancel;
	private List<AssignWorkerNameBean> assignWorkerNameList = new ArrayList<AssignWorkerNameBean>();
	private String ssbscid;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_assign_worker_name);

		initUI();
	}

	private void initUI() {

		title_title.setText("选择人员");
		title_left.setOnClickListener(this);
		ssbscid = getIntent().getStringExtra("ssbscid");
		User user = User.getInfo(this);
		if(!user.getRoleCode().contains("QYJL_COMMON") && !user.getRoleCode().contains("QYWY_JX")){
			getServicePeople();
		}

		btn_search = (Button) findViewById(R.id.btn_search);
		assignWorkerName = (ConditionView) findViewById(R.id.assignWorkerName);

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);

		btn_search.setBackgroundResource(R.drawable.icon_search);
		btn_search.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		adapter = new AssignWorkerSearchAdapter(this, assignWorkerNameList);

		getListviewObj().getListview().setAdapter(adapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(true);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);
		getListviewObj().setStatus(LoadingEnum.NODATA, "请输入条件进行搜索");

		CommonUtils.hideInputMethod(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.btn_search:
			if(CommonUtils.isEmpty(assignWorkerName.getFieldValue())){
				toastMsg("您的搜索条件不能为空");
				return;
			}
			getMachineinfoSimples();
			break;
		case R.id.btn_submit:
			if(adapter.getWorkerNameCheckedList().size() == 1){
				Intent intent = new Intent();
				AssignWorkerNameBean awnb = (AssignWorkerNameBean)adapter.getWorkerNameCheckedList().get(0);
				CommonSelectData commonSelectData = new CommonSelectData();
				String workFlag = "";
				if(awnb.getWorkFlag() == 0){
					workFlag = "休假";
				}else if(awnb.getWorkFlag() == 2){
					workFlag = "请假";
				}else{
					workFlag = "空闲";
				}
				commonSelectData.setText( awnb.getUserName()+"\t\t"+awnb.getSystemProposeFlag()+"\n"+awnb.getUserAccount()
						+"\t\t"+awnb.getLinkPhone()+"\t\t"+workFlag);
				commonSelectData.setValue(awnb.getUserId());
				intent.putExtra("workerNameData",commonSelectData);
				setResult(RESULT_OK, intent);
				finish();
			}else{
				toastMsg("您只能选择一个维护人员");
			}
			break;
		case R.id.btn_cancel:
			finish();
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
		getMachineinfoSimples();
		getListviewObj().getListview().setLoadMoreEnabled(true);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		currentPage++;
		getMachineinfoSimples();
	}

	public void getServicePeople(){
		if (currentPage == 1) {
			getListviewObj().setStatus(LoadingEnum.LOADING);
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("ssbscid", ssbscid);

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.CxfwzryUrl), params,
				new AjaxHttpCallBack<BasePageBean<AssignWorkerNameBean>>(this,
						new TypeToken<BasePageBean<AssignWorkerNameBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<AssignWorkerNameBean> basePageBean) {
						List<AssignWorkerNameBean> data = basePageBean.getList();
						if (data.size() > 0) {
							if (basePageBean.getCurrentPage() == 1)
								adapter.setLists(data);
							else
								adapter.addLists(data);

							getListviewObj().setStatus(LoadingEnum.LISTSHOW);
							// 如果当前页等于总页数，则不需要先是load more按钮
							if (basePageBean.getCurrentPage() == basePageBean.getTotalPage())
								getListviewObj().getListview().setLoadMoreEnabled(false);

						} else {
							getListviewObj().setStatus(LoadingEnum.NODATA,"没有数据");
						}
					};
				});

	}
	
	public void getMachineinfoSimples() {
		if (currentPage == 1) {
			getListviewObj().setStatus(LoadingEnum.LOADING);
		}
		KTHttpClient fh = new KTHttpClient(true);
		// 获取condition实例
		LinearLayout conditionLayout = (LinearLayout) findViewById(R.id.workorder_condition);
		Condition cond = ConditionUtils.getCondition(this, conditionLayout);
		AjaxParams params = ConditionUtils.getAjaxParams(cond, currentPage);
		params.put("user", assignWorkerName.getFieldValue());

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.WhryUrl), params,
				new AjaxHttpCallBack<BasePageBean<AssignWorkerNameBean>>(this,
						new TypeToken<BasePageBean<AssignWorkerNameBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<AssignWorkerNameBean> basePageBean) {
						List<AssignWorkerNameBean> data = basePageBean.getList();
						if (data.size() > 0) {
							if (basePageBean.getCurrentPage() == 1)
								adapter.setLists(data);
							else
								adapter.addLists(data);

							getListviewObj().setStatus(LoadingEnum.LISTSHOW);
							// 如果当前页等于总页数，则不需要先是load more按钮
							if (basePageBean.getCurrentPage() == basePageBean.getTotalPage())
								getListviewObj().getListview().setLoadMoreEnabled(false);

						} else {
							getListviewObj().setStatus(LoadingEnum.NODATA,"没有数据");
						}
					};
				});

	}

}
