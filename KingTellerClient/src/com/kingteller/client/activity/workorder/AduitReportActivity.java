package com.kingteller.client.activity.workorder;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.activity.workorder.frament.AduitReportFragment;
import com.kingteller.client.adapter.PagerAdapter;
import com.kingteller.client.adapter.TopTabAdapter;
import com.kingteller.client.bean.workorder.ReturnBackStatus;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.client.view.TabView;
import com.kingteller.client.view.TabView.OnItemTabListener;
import com.kingteller.framework.http.AjaxParams;

public class AduitReportActivity extends BaseFragmentActivity implements
		OnClickListener,OnItemTabListener {

	private Button batchAudit, batchReturn;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private PagerAdapter pagerAdapter;
	private AduitReportFragment untreatedFragment,processedFragment;
	private TopTabAdapter tabAdapter;
	private TabView layout_tab;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_audit_report);
		initUI();
		initData();
	}

	private void initUI() {
		title_title.setText("审核报告列表");

		batchAudit = (Button) findViewById(R.id.batchAudit);
		batchReturn = (Button) findViewById(R.id.batchReturn);; 
		layout_tab = (TabView) findViewById(R.id.layout_tab);

		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		batchAudit.setOnClickListener(this);
		batchReturn.setOnClickListener(this);

		viewPager = (ViewPager) findViewById(R.id.viewPaper);
		
		fragments = new ArrayList<Fragment>();
		
		tabAdapter = new TopTabAdapter(this, getResources().getStringArray(
				R.array.aduitreport));
		layout_tab.setAdapter(tabAdapter);
		layout_tab.setOnItemListener(this);
	}

	private void initData() {
		fragments = new ArrayList<Fragment>();
		untreatedFragment = new AduitReportFragment().getInstance(BaseReportActivity.OPT_UNTREATED);
		processedFragment = new AduitReportFragment().getInstance(BaseReportActivity.OPT_PROCESSED);
		
		fragments.add(untreatedFragment);
		fragments.add(processedFragment);
		
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
				changeUI();
			}

		});
	}
	
	public void changeUI(){
		if(viewPager.getCurrentItem() == 0){
			tabAdapter.setSelectPostion(0);
			batchAudit.setVisibility(View.VISIBLE);
			batchReturn.setVisibility(View.VISIBLE);
		}else if(viewPager.getCurrentItem() == 1){
			tabAdapter.setSelectPostion(1);
			batchAudit.setVisibility(View.GONE);
			batchReturn.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.button_right:
			break;
		case R.id.batchAudit:
				if(untreatedFragment.getAduitCheckedtList().size() >= 1){
					new KTAlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("确定将选中的工作报告审核通过吗？")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									batchProcess("0");
									dialogInterface.dismiss();
								}
							})
					.setNegativeButton("取消",
							new KTAlertDialog.OnClickListener() {
								public void onClick(
										DialogInterface dialogInterface,
										int paramAnonymousInt) {
									dialogInterface.dismiss();
								}
							}).create().show();
				}else{
					toastMsg("您没有选择报告!");
				}	
			break;
		case R.id.batchReturn:
				if(untreatedFragment.getAduitCheckedtList().size() >= 1){
					new KTAlertDialog.Builder(this)
					.setTitle("提醒")
					.setMessage("确定将选中的工作报告退回吗？")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									batchProcess("1");
									dialogInterface.dismiss();
								}
							})
					.setNegativeButton("取消",
							new KTAlertDialog.OnClickListener() {
								public void onClick(
										DialogInterface dialogInterface,
										int paramAnonymousInt) {
									dialogInterface.dismiss();
								}
							}).create().show();
				}else{
					toastMsg("您没有选择报告!");
				}
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
	
	/**
	 * 批量处理
	 * 0：批量审核 1：批量退回flag
	 * */
	private void batchProcess(String flag){
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		//Log.e("11111111111111111111111",ConditionUtils.getJsonFromObj(untreatedFragment.getAduitCheckedtList()));
		params.put("report", ConditionUtils.getJsonFromObj(untreatedFragment.getAduitCheckedtList()));
		params.put("flag", flag);
		
		fh.post(ConfigUtils.CreateUrl(this, URLConfig.PlthUrl),params,
				new AjaxHttpCallBack<ReturnBackStatus>(this,
						new TypeToken<ReturnBackStatus>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(ReturnBackStatus data) {
						if(data.getResult().equals("success")){
							untreatedFragment.onRefresh();
							processedFragment.onRefresh();
						}
						toastMsg(data.getMessage());
					};
				});
	}
	
}
