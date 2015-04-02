package com.kingteller.client.activity.common;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.CommonSelectGtgjAdapter;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;

public class CommonSelectGtgjActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {

	private List<CommonSelectData> listData = new ArrayList<CommonSelectData>();
	private CommonSelectGtgjAdapter adapter;
	private int type;
	public final static String TITLE = "title";
	public final static String TYPE = "type";
	public final static String DATA = "data";
	private Button submit,cancel;
	private final static String[] vehicleText = { "火车", "公交", "摩托",
		"大巴", "轮船", "地铁", "的士", "飞机"};

	private final static String[] vehicleValue = { "ff80808142e951150142e9a284f036f9", "ff80808142e951150142e9a48fc537c1",
		"ff80808142e951150142e9a4c37637cc", "ff80808142e951150142e9a514ca37da", "ff80808142e951150142e9a553b937f8", 
		"ff80808142e951150142e9a5881637fd", "ff80808142e951150142e9a5a90e3801","ff80808142e951150142e9a609e23815" };

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.common_list_view_gtgj);
		submit = (Button) findViewById(R.id.submit);
		cancel = (Button) findViewById(R.id.cancel);
		initUI();
		getData();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		// TODO Auto-generated method stub
		type = getIntent().getIntExtra(TYPE, 0);

		title_title.setText("交通工具");
		title_left.setOnClickListener(this);

		adapter = new CommonSelectGtgjAdapter(this, listData);
		getListviewObj().getListview().setAdapter(adapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(false);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);
		
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
	}

	private void getData() {
		switch (type) {
		case R.array.jtgj:// 获取交通工具
			getJTGJ();
			break;
		default:
			break;
		}
	}


	/**
	 * 获取交通工具
	 */
	private void getJTGJ() {
		List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
		for (int i = 0; i < vehicleValue.length; i++) {
			CommonSelectData data = new CommonSelectData();
			data.setText(vehicleText[i]);
			data.setValue(vehicleValue[i]);
			lists.add(data);
		}
		if (lists.size() > 0) {
			adapter.setLists(lists);
			getListviewObj().setStatus(LoadingEnum.LISTSHOW);
		} else {
			getListviewObj().setStatus(LoadingEnum.NODATA, "没有数据");
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
		case R.id.submit:
			setItemDataMul();
			this.finish();
			break;
		case R.id.cancel:
			this.finish();
			break;
		}
	}

	public void setItemDataMul(){
		Intent intent = new Intent();
		List<CommonSelectData> listData = adapter.getCheckedDataList();
		StringBuilder strBuiler1 = new StringBuilder();
		StringBuilder strBuiler2 = new StringBuilder();
		for(int i= 0; i < listData.size();i++){
			if( i == listData.size() -1){
				strBuiler1.append(listData.get(i).getText());
				strBuiler2.append(listData.get(i).getValue());
			}else{
				strBuiler1.append(listData.get(i).getText());
				strBuiler1.append(",");
				strBuiler2.append(listData.get(i).getValue());
				strBuiler2.append(",");
			}
		}
		CommonSelectData commonSelectData = new CommonSelectData();
		commonSelectData.setText(strBuiler1.toString());
		commonSelectData.setValue(strBuiler2.toString());
		intent.putExtra(DATA,(CommonSelectData)commonSelectData);
		setResult(RESULT_OK, intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRefresh() {
		getData();
		getListviewObj().getListview().setLoadMoreEnabled(true);
	}

	@Override
	public void onLoadMore() {
	}

}
