package com.kingteller.client.activity.map;

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
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 地图功能导航菜单
 * @author 王定波
 *
 */
public class MapMainActivity extends BaseActivity implements
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
		title_title.setText("地图功能");
		title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		gridview = (GridView) findViewById(R.id.gridview);
		adapter = new FunctionAdapter(this, User.getInfo(this).getRight(),
				FunctionAdapter.MAPMENU);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int postion,
			long arg3) {
		// TODO Auto-generated method stub
		String action = (String) adapterView.getAdapter().getItem(postion);
		if (action.equals("MOBILE_MAP_USER")) {
			startActivity(new Intent(this,StaffLocationActivity.class));

		} else if (action.equals("MOBILE_MAP_FWZ")) {
			CommonUtils.openBrowser(this,String.format(ConfigUtils.CreateUrl(this,URLConfig.WebFwzUrl), User
					.getInfo(this).getUserId()));

		} else if (action.equals("MOBILE_MAP_LOCATION")) {
			startActivity(new Intent(this,ATMUploadActivity.class));

		} else if (action.equals("MOBILE_MAP_MACHINE")) {
			CommonUtils.openBrowser(this,String.format(ConfigUtils.CreateUrl(this,URLConfig.WebATMCUrl), User
					.getInfo(this).getUserId()));

		} else if (action.equals("MOBILE_MAP_DF")) {
			startActivity(new Intent(this,MobileNavActivity.class));
		} else {
			toastMsg("功能尚未开放，请关注");
		}

	}
}
