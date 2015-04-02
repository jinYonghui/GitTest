package com.kingteller.client.activity.pxkhsj;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.activity.pxkhsj.fragment.PxkhSjListFragment;

public class DoPxkhsjListActivity extends BaseFragmentActivity implements OnClickListener {

	PxkhSjListFragment pxkhfragment;
	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_logistic_order);
		initUI();
		initData();
	}
	
	private void initUI(){
		title_title.setText("考核试卷列表");
		title_left.setOnClickListener(this);
	}
	
	private void initData(){
		pxkhfragment = new PxkhSjListFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.relativeLayout, pxkhfragment).commit(); 
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.button_left:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		pxkhfragment.getAllSjPaperListDatas();
	}

}
