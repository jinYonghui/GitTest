package com.kingteller.client.activity.map;

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
import com.kingteller.client.adapter.StaffLocationAdapter;
import com.kingteller.client.bean.map.StaffLocationBean;
import com.kingteller.client.bean.map.StaffSearchCondition;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class StaffLocationActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	private StaffLocationAdapter adpater;
	private StaffSearchCondition condition;
	private int nowpage = 1;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.common_list_view);
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("员工查询");
		title_right.setBackgroundResource(R.drawable.icon_search);
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);

		adpater = new StaffLocationAdapter(this,
				new ArrayList<StaffLocationBean>());
		getListviewObj().getListview().setAdapter(adpater);
		getListviewObj().getListview().setRefreshEnabled(false);
		getListviewObj().getListview().setLoadMoreEnabled(true);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);

		getListviewObj().setStatus(LoadingEnum.TIP, "请设置查询条件");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.button_right:
			Intent intent = new Intent(this, StaffSearchActivity.class);
			startActivityForResult(intent, CommonCodeConfig.REQUEST_STAFFSEARCH);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CommonCodeConfig.REQUEST_STAFFSEARCH:
			if (resultCode == RESULT_OK) {
				condition = (StaffSearchCondition) data
						.getSerializableExtra("condition");
				nowpage = 1;
				getStaffs();
			}
			break;

		default:
			break;
		}
	}

	private void getStaffs() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		// User user = User.getInfo(this);
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("monitorDay", condition.getDate());
		String[] strstart=condition.getStarttime().split(":");
		params.put("startHour",strstart[0]);
		params.put("startMinute", strstart[1]);
		String[] strend=condition.getEndtime().split(":");
		params.put("endHour",strend[0]);
		params.put("endMinute",strend[1]);
		params.put("userNameLike", condition.getUsername());
		params.put("page", String.valueOf(nowpage));
		params.put("userAccountLike", condition.getUsecode());
		params.put("roleName", "");
		params.put("userOrgName", condition.getDepartment());
		params.put("phoneFlag", "1");

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.StaffListUrl), params,
				new AjaxHttpCallBack<List<StaffLocationBean>>(this,
						new TypeToken<List<StaffLocationBean>>() {
						}.getType(), true) {

					@Override
					public void onSuccess(String response) {
						// TODO Auto-generated method stub
						if(CommonUtils.isEmpty(response))
							response="[]";
						super.onSuccess(response);
					}

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						if (nowpage == 1)
							getListviewObj().setStatus(LoadingEnum.LOADING);
					}

					@Override
					public void onDo(List<StaffLocationBean> data) {

						if (nowpage == 1)
							adpater.setLists(data);
						else
							adpater.addLists(data);

						if (data.size() > 0) {
							getListviewObj().setStatus(LoadingEnum.LISTSHOW);
							if (nowpage >= data.get(0).getTotalPage())
								getListviewObj().getListview()
										.setLoadMoreEnabled(false);

						} else if (nowpage == 1)
							getListviewObj().setStatus(LoadingEnum.NODATA);

					};

				});

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		StaffLocationBean data = (StaffLocationBean) adapterView.getAdapter()
				.getItem(pos);
		Intent intent = new Intent(this, StaffLocusActivity.class);
		intent.putExtra("data", data);
		startActivity(intent);

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		nowpage++;
		getStaffs();

	}

}
