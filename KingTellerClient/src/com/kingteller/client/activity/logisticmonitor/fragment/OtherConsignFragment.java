package com.kingteller.client.activity.logisticmonitor.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragment;
import com.kingteller.client.adapter.OtherTaskConsignAdapter;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.logisticmonitor.OtherTaskConsignBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class OtherConsignFragment extends BaseFragment implements IXListViewListener{

	private View view;
	private Context context;
	private OtherTaskConsignAdapter otherTaskConsignAdapter;
	private List<OtherTaskConsignBean> otherTaskConsignlist = new ArrayList<OtherTaskConsignBean>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		
		otherTaskConsignAdapter = new OtherTaskConsignAdapter(context, otherTaskConsignlist);
		getListviewObj().getListview().setAdapter(otherTaskConsignAdapter);
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
		getOtherTaskConsign();
	}

	public void getOtherTaskConsign() {	
		if(!CommonUtils.isNetAvaliable(getActivity())){
			Toast.makeText(getActivity(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
			//toastMsg();
			return;
		}
		
		Intent intent = getActivity().getIntent(); 
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("swdh", intent.getStringExtra("swdh"));

		fh.get(ConfigUtils.CreateNoVersionUrl(context, URLConfig.WljkcUrl),
				params, new AjaxHttpCallBack<BasePageBean<OtherTaskConsignBean>>(context,
						new TypeToken<BasePageBean<OtherTaskConsignBean>>() {
						}.getType(), true) {
					
					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
					}
					
					@Override
					public void onDo(BasePageBean<OtherTaskConsignBean> data) {
						if("".equals(data.getStatus()) ){
							if (data.getList().size() > 0) {
								otherTaskConsignlist = data.getList();
								otherTaskConsignAdapter.setLists(data.getList());
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
	@Override
	public void onRefresh() {
		getOtherTaskConsign();
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

}
