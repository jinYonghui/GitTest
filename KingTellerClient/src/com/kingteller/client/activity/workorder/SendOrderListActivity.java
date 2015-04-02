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
import com.kingteller.client.activity.workorder.frament.SendOrderFragment;
import com.kingteller.client.adapter.PagerAdapter;
import com.kingteller.client.adapter.TopTabAdapter;
import com.kingteller.client.view.TabView;
import com.kingteller.client.view.TabView.OnItemTabListener;

public class SendOrderListActivity extends BaseSendOrderFragmentActivity implements
		OnClickListener, OnItemTabListener {

	private SendOrderFragment noAcceptFragment,ongoingFragment,finishFragment;
	private PagerAdapter pagerAdapter;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private TopTabAdapter tabAdapter;
	private TabView layout_tab;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_send_order_list);
		initUI();
		initData();
	}

	private void initUI() {
		title_title.setText("我的派单");
		title_right.setBackgroundResource(R.drawable.icon_add);
		title_search.setBackgroundResource(R.drawable.icon_search);
		
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		title_search.setOnClickListener(this);

		viewPager = (ViewPager) findViewById(R.id.viewPaper2);
		layout_tab = (TabView) findViewById(R.id.layout_tab);

		tabAdapter = new TopTabAdapter(this, getResources().getStringArray(
				R.array.sendorder));
		layout_tab.setAdapter(tabAdapter);
		layout_tab.setOnItemListener(this);
	}

	private void initData() {

		fragments = new ArrayList<Fragment>();
		noAcceptFragment = new SendOrderFragment().getInstance("tab1");
		ongoingFragment = new SendOrderFragment().getInstance("tab2");
		finishFragment = new SendOrderFragment().getInstance("tab3");
		
		fragments.add(noAcceptFragment);
		fragments.add(ongoingFragment);
		fragments.add(finishFragment);

		pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(pagerAdapter);
		
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
					ongoingFragment.getSendOrders();
					tabAdapter.setSelectPostion(1);
				} else if (fragments.get(arg0) == noAcceptFragment) {
					noAcceptFragment.getSendOrders();
					tabAdapter.setSelectPostion(0);
				}else if(fragments.get(arg0) == finishFragment){
					finishFragment.getSendOrders();
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
			ongoingFragment.getSendOrders();
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
			startActivity(new Intent(this, RapairSendOrderActivity.class));
			break;
		case R.id.button_search:
			startActivity(new Intent(this,SendOrderSearchListActivity.class));
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
