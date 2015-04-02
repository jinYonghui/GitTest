package com.kingteller.client.activity.logisticmonitor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.activity.logisticmonitor.fragment.OtherTaskFragment;

public class OtherTaskListActivity extends BaseFragmentActivity 
   	implements OnClickListener{
	
	private OtherTaskFragment otFragment;
	private String opType="all";

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_othertask_order); 
		initUI();
		initData();
	}
	
	private void initUI(){
		title_title.setText("任务内容");
		title_left.setOnClickListener(this);
	}
	
	private void initData() {
		otFragment = new OtherTaskFragment(opType);
		getSupportFragmentManager().beginTransaction().add(R.id.relativeLayout, otFragment).commit();           
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		otFragment.getOtherTaskOrder();
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.button_left:
			finish();
			break;
		default:
			break;
		}
	}

}
