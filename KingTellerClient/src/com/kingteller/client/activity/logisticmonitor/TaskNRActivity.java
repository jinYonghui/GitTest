package com.kingteller.client.activity.logisticmonitor;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;

public class TaskNRActivity extends BaseActivity implements OnClickListener{

	private final static String DB_PATH = "/data/data/com.kingteller/databases/";
	private final static String DB_NAME = "sjk.db";
	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);

		setContentView(R.layout.layout_task_nr1);
		initUI();
	}

	private void initUI() {
		title_title.setText("任务内容");
		
		title_left.setOnClickListener(this);
		
		/*LinearLayout layout_form = (LinearLayout) findViewById(R.id.layout_form);
		if (layout_form != null) {
			int length = layout_form.getChildCount();
			for (int i = 0; i < length; i++) {
				if (layout_form.getChildAt(i).getClass().getName()
						.equals(KingTellerEditText.class.getName())) {
					((KingTellerEditText) layout_form.getChildAt(i))
							.setFouces(false);
				}
			}
		}*/
		
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
