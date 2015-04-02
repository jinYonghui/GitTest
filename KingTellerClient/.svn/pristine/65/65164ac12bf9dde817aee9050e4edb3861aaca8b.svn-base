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
import com.kingteller.client.adapter.SendOrderAdapter;
import com.kingteller.client.adapter.SendOrderAdapter.Callback;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.workorder.SendOrderBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class SendOrderFragment extends BaseFragment implements IXListViewListener {

	private View view;
	private Context context;
	private SendOrderAdapter sendOrderAdapter;
	private List<SendOrderBean> sendOrderlist = new ArrayList<SendOrderBean>();
	private String tabName;
	
	public SendOrderFragment(){}
	
	public SendOrderFragment getInstance(String tabName){
		SendOrderFragment wf = new SendOrderFragment();
		Bundle bun = new Bundle();
		bun.putString("tName", tabName);
		wf.setArguments(bun);
		return wf;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.tabName = getArguments().getString("tName");
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
		
		sendOrderAdapter = new SendOrderAdapter(context, sendOrderlist,tabName);
		sendOrderAdapter.setCallBack(new Callback() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				onRefresh();
			}
			
			@Override
			public void onFalse(String msg) {
				// TODO Auto-generated method stub
				onRefresh();
				toastMsg(msg);
			}
		});

		getListviewObj().getListview().setAdapter(sendOrderAdapter);
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
		getSendOrders();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getSendOrders();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
	}

	public void getSendOrders() {
		
		if(!CommonUtils.isNetAvaliable(getActivity())){
			toastMsg("网络异常，请稍后重试");
			return;
		}
		
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		if(tabName.equals("tab1")){
			params.put("flag", "0");
		}else if(tabName.equals("tab2")){
			params.put("flag", "1");
		}else if(tabName.equals("tab3")){
			params.put("flag", "3");
		}

		fh.get(ConfigUtils.CreateNoVersionUrl(context, URLConfig.PdlbUrl),
				params, new AjaxHttpCallBack<BasePageBean<SendOrderBean>>(context,
						new TypeToken<BasePageBean<SendOrderBean>>() {
						}.getType(), true) {
					
					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
					}
					
					@Override
					public void onDo(BasePageBean<SendOrderBean> data) {
						if("".equals(data.getStatus()) ){
							if (data.getList().size() > 0) {
								sendOrderlist = data.getList();
								sendOrderAdapter.setLists(data.getList());
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

}
