package com.kingteller.client.activity.logisticmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.FunctionAdapter;
import com.kingteller.client.bean.account.User;

public class WLJKMainActivity extends BaseActivity implements
		OnItemClickListener {
	private GridView gridview;
	private FunctionAdapter adapter;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_map_main);
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("物流监控");
		title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		gridview = (GridView) findViewById(R.id.gridview);
		adapter = new FunctionAdapter(this, User.getInfo(this).getRight(),
				FunctionAdapter.WLJKMENU);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int postion,
			long arg3) {
		// TODO Auto-generated method stub
		String action = (String) adapterView.getAdapter().getItem(postion);
		if (action.equals("WLJK_MOBILE_HDGL")) {
			startActivity(new Intent(this,LogisticOrderListActivity.class));
		} else if (action.equals("WLJK_MOBILE_TXQTSWBG")) {
            startActivity(new Intent(this,WriteOtherTaskListActivity.class));
		} else if (action.equals("WLJK_MOBILE_QTSWGL")) {
			startActivity(new Intent(this,OtherTaskListActivity.class));
		} else if (action.equals("WLJK_MOBILE_TXRWBG")) {
			
		} else {
			toastMsg("功能尚未开放，请关注");
		}

	}
}
