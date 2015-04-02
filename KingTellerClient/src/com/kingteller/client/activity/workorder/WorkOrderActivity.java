package com.kingteller.client.activity.workorder;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.activity.workorder.frament.WorkOrderFragment;
import com.kingteller.client.adapter.PagerAdapter;
import com.kingteller.client.adapter.TopTabAdapter;
import com.kingteller.client.view.TabView;
import com.kingteller.client.view.TabView.OnItemTabListener;

public class WorkOrderActivity extends BaseFragmentActivity implements
		OnClickListener, OnItemTabListener {

	private WorkOrderFragment ongoingFragment, reportFragment,dealFragment;
	private PagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private TopTabAdapter tabAdapter;
	private TabView layout_tab;
	private String reportFlowCode="";

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_workorder);
		initUI();
		initData();
	}

	private void initUI() {
		/*loadData = (Button) findViewById(R.id.loadData);
		loadData.setText("加载数据");
		loadData.setOnClickListener(this);*/
		title_title.setText("我的工单");
		title_right.setBackgroundResource(R.drawable.icon_search);

		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);

		viewPager = (ViewPager) findViewById(R.id.viewPaper1);
		layout_tab = (TabView) findViewById(R.id.layout_tab);

		tabAdapter = new TopTabAdapter(this, getResources().getStringArray(
				R.array.workorder));
		layout_tab.setAdapter(tabAdapter);
		layout_tab.setOnItemListener(this);
		reportFlowCode = getIntent().getStringExtra("reportFlowCode");
	}

	private void initData() {

		fragments = new ArrayList<Fragment>();
		ongoingFragment = new WorkOrderFragment().getInstance("tab1");
		reportFragment = new WorkOrderFragment().getInstance("tab2");
		dealFragment = new WorkOrderFragment().getInstance("tab3");
		
		fragments.add(ongoingFragment);
		fragments.add(reportFragment);
		fragments.add(dealFragment);

		pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(pagerAdapter);
		
		/*if(!CommonUtils.isEmpty(reportFlowCode)){
			viewPager.setCurrentItem(1);
		}*/
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (fragments.get(arg0) == ongoingFragment) {
					ongoingFragment.getWorkOrders();
					tabAdapter.setSelectPostion(0);
				} else if (fragments.get(arg0) == reportFragment) {
					reportFragment.getWorkOrders();
					tabAdapter.setSelectPostion(1);
				}else if(fragments.get(arg0) == dealFragment){
					dealFragment.getWorkOrders();
					tabAdapter.setSelectPostion(2);
				}

			}

		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (viewPager.getCurrentItem() == 1) {
			reportFragment.getWorkOrders();
		}else if(viewPager.getCurrentItem() == 2){
			dealFragment.getWorkOrders();
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.button_right:
			startActivity(new Intent(this, WorkOrderSearchActivity.class));
			break;
		default:
			break;
		}
	}
	
	@Override
	public void OnItemClick(int pos) {
		// TODO Auto-generated method stub
		tabAdapter.setSelectPostion(pos);
		viewPager.setCurrentItem(pos);
	}
}
