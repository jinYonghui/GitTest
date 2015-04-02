package com.kingteller.client.activity.logisticmonitor.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragment;
import com.kingteller.client.adapter.LogisticOrderAdapter;
import com.kingteller.client.bean.logisticmonitor.LogisticOrderBean;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;

public class LogisticOrderFragment extends BaseFragment implements IXListViewListener {

	private View view;
	private Context context;
	private LogisticOrderAdapter logisticOrderAdapter;
	private List<LogisticOrderBean> logisticOrderlist = new ArrayList<LogisticOrderBean>();
	
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
		
		logisticOrderAdapter = new LogisticOrderAdapter(context, logisticOrderlist);
		getListviewObj().getListview().setAdapter(logisticOrderAdapter);
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
		getLogisticOrders();
	}

	public void getLogisticOrders() {
		// TODO Auto-generated method stub
		LogisticOrderBean b1 = new LogisticOrderBean();
		b1.setRwdh("1");
		b1.setRwlx("托运");
		b1.setTylx("发机");
		b1.setSl("10");
		b1.setPdsj("2014-10-10");
		b1.setTyzt1("确认接收");
		b1.setTyzt2("到达");
		LogisticOrderBean b2 = new LogisticOrderBean();
		b2.setRwdh("2");
		b2.setRwlx("托运");
		b2.setTylx("退机");
		b2.setSl("20");
		b2.setPdsj("2014-10-09");
		b2.setTyzt1("到达");
		b2.setTyzt2("确认接收");
		logisticOrderlist.add(b1);
		logisticOrderlist.add(b2);
		
		logisticOrderAdapter.setLists(logisticOrderlist);
		getListviewObj().setStatus(LoadingEnum.LISTSHOW);
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getLogisticOrders();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
	}
}
