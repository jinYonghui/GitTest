package com.kingteller.client.activity.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.bean.common.KingTellerDateTime;
import com.kingteller.client.bean.map.StaffSearchCondition;
import com.kingteller.client.view.KingTellerEditText;


/**
 * 员工定位的Activoty
 * @author 王定波
 *
 */
public class StaffSearchActivity extends BaseActivity implements
		OnClickListener {
	private KingTellerEditText endTime;
	private KingTellerEditText username;
	private KingTellerEditText department;
	private KingTellerEditText dataset;
	private KingTellerEditText startTime;
	private KingTellerEditText usecode;
	private StaffSearchCondition condition = new StaffSearchCondition();


	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_staff_search);
		initUI();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
	
		endTime.setFieldTextAndValue("23:59");
		startTime.setFieldTextAndValue("00:00");
		dataset.setFieldTextAndValue(new KingTellerDateTime().initNow().getDateString());

	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("设置员工搜索条件");
		title_right.setBackgroundResource(R.drawable.icon_right);
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		endTime = (KingTellerEditText) findViewById(R.id.endTime);
		username = (KingTellerEditText) findViewById(R.id.username);
		department = (KingTellerEditText) findViewById(R.id.department);
		dataset = (KingTellerEditText) findViewById(R.id.dataset);
		startTime = (KingTellerEditText) findViewById(R.id.startTime);
		usecode = (KingTellerEditText) findViewById(R.id.usecode);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.button_right:
			
			condition.setEndtime(endTime.getFieldValue());
			condition.setStarttime(startTime.getFieldValue());
			condition.setDate(dataset.getFieldValue());
			condition.setUsername(username.getFieldValue());
			condition.setDepartment(department.getFieldValue());
			condition.setUsecode(usecode.getFieldValue());

			Intent intent = new Intent();
			intent.putExtra("condition", condition);
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
