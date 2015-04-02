package com.kingteller.client.activity.workorder.frament;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragment;
import com.kingteller.client.activity.workorder.BaseReportActivity;
import com.kingteller.client.adapter.AduitReportAdapter;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.workorder.AduitReportBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class AduitReportFragment extends BaseFragment implements IXListViewListener {

	private View view;
	private Context context;
	private AduitReportAdapter aduitAdapter;
	private List<AduitReportBean> aduitlist = new ArrayList<AduitReportBean>();
	private String executionState;

	public AduitReportFragment() {
	}

	public AduitReportFragment getInstance(String executionState) {
		AduitReportFragment af = new AduitReportFragment();
		Bundle bun = new Bundle();
		bun.putString("executionState", executionState);
		af.setArguments(bun);
		return af;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.executionState = getArguments().getString("executionState");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.common_list_view, container, false);
		context = inflater.getContext();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initUI();
		initData();
	}

	private void initUI() {
		aduitAdapter = new AduitReportAdapter(context, aduitlist,executionState);

		getListviewObj().getListview().setAdapter(aduitAdapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(false);
		getListviewObj().getListview().setOnRefreshListener(this);

		getListviewObj().setOnClickLister(LoadingEnum.NODATA, res);
		getListviewObj().setOnClickLister(LoadingEnum.NETERROR, res);
	}

	private OnClickListener res = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			getListviewObj().setStatus(LoadingEnum.LOADING);
			onRefresh();
		}
	};

	private void initData() {
		getAduits();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getAduits();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		onRefresh();
		
	}
	
	
	public void getAduits() {

		if (!CommonUtils.isNetAvaliable(getActivity())) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		
		if(BaseReportActivity.OPT_UNTREATED.equals(executionState)){	
			params.put("flag", "0");
		}else if(BaseReportActivity.OPT_PROCESSED.equals(executionState)){
			params.put("flag", "1");
		}

		fh.post(ConfigUtils.CreateUrl(context, URLConfig.WebSpbgUrl),
				params, new AjaxHttpCallBack<BasePageBean<AduitReportBean>>(context,
						new TypeToken<BasePageBean<AduitReportBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
					}

					@Override
					public void onDo(BasePageBean<AduitReportBean> data) {
						
						if("".equals(data.getStatus()) ){
							if (data.getList().size() > 0) {
								aduitlist = data.getList();
								aduitAdapter.setLists(data.getList());
								getListviewObj().setStatus(LoadingEnum.LISTSHOW);
							} else {
								getListviewObj().setStatus(LoadingEnum.NODATA,getString(R.string.no_data_try));
							}
						}else{
							toastMsg(data.getMsg());
						}
					};
				});
	}
	
	public List<AduitReportBean> getAduitCheckedtList(){
		if(aduitAdapter.getAduitCheckedList() == null){
			toastMsg("您没有选择任何数据");
		}
		return aduitAdapter.getAduitCheckedList();
	}

}
