package com.kingteller.client.activity.logisticmonitor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.activity.logisticmonitor.fragment.OtherConsignFragment;
import com.kingteller.client.bean.logisticmonitor.ResultStatus;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class OtherTaskActivity extends BaseFragmentActivity implements
		OnClickListener {

	private Button btn_accept, btn_back, btn_start, btn_complete, btn_return;
	private String rwdzt;
	private String swdh;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			onFresh(rwdzt);
		};
	};
	
	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);

		setContentView(R.layout.layout_other_task);
		initUI();
		initData();
	}

	public void initUI() {
		title_title.setText("任务内容");
		title_left.setOnClickListener(this);
		btn_accept = (Button) findViewById(R.id.btn_accept);
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_complete = (Button) findViewById(R.id.btn_complete);
		btn_return = (Button) findViewById(R.id.btn_return);
		btn_back = (Button) findViewById(R.id.btn_backo);
		rwdzt = getIntent().getStringExtra("rwdzt");
		swdh = getIntent().getStringExtra("swdh");

		onFresh(rwdzt);

		btn_accept.setOnClickListener(this);
		btn_start.setOnClickListener(this);
		btn_complete.setOnClickListener(this);
		btn_return.setOnClickListener(this);
		btn_back.setOnClickListener(this);

	}

	public void initData() {
		OtherConsignFragment consignFragment = new OtherConsignFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.otherconsign_li, consignFragment).commit();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_left:
		case R.id.btn_backo:
			finish();
			break;
		case R.id.btn_accept:
		case R.id.btn_start:
		case R.id.btn_complete:
		case R.id.btn_return:
			new KTAlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("您"+onChangeStatus(rwdzt)+"？")
			.setPositiveButton("确定",
					new KTAlertDialog.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialogInterface,
								int pos) {
							updateStatus();
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
			break;
		default:
			break;
		}
	}

	public void updateStatus() {
		String t = null;
		if ("swlb_yp".equals(rwdzt)) {
			t = "swlb_js";
		} else if ("swlb_js".equals(rwdzt)) {
			t = "swlb_ks";
		} else if ("swlb_ks".equals(rwdzt)) {
			t = "swlb_wc";
		} else if ("swlb_wc".equals(rwdzt)) {
			t = "swlb_fh";
		}
		if (!CommonUtils.isNetAvaliable(OtherTaskActivity.this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("rwdzt", t);
		params.put("swdh", swdh);

		fh.get(ConfigUtils.CreateNoVersionUrl(OtherTaskActivity.this,
				URLConfig.WljkstuatsUrl), params,
				new AjaxHttpCallBack<ResultStatus>(OtherTaskActivity.this,
						new TypeToken<ResultStatus>() {
						}.getType(), true) {

					@Override
					public void onFinish() {}

					@Override
					public void onDo(ResultStatus data) {
						int status = 0;
						status = Integer.parseInt(data.getResult());
						if(status > 0){
							rwdzt = data.getRwdzt();
							new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									handler.sendEmptyMessage(0);
								}
							}).start();
						}else{
							toastMsg(data.getMessage());
						}
					};
				});
	}

	private void onFresh(String rwdzt) {
		if ("swlb_yp".equals(rwdzt)) {
			btn_accept.setText("确认接收");
		} else if ("swlb_js".equals(rwdzt)) {
			btn_accept.setText("开始");
		} else if ("swlb_ks".equals(rwdzt)) {
			btn_accept.setText("完成");
		} else if ("swlb_wc".equals(rwdzt)) {
			btn_accept.setText("确认返回");
		}else{
			finish();
		}
	}
	
	private String onChangeStatus(String rwdzt){
		if ("swlb_yp".equals(rwdzt)) {
			return "确认接收";
		} else if ("swlb_js".equals(rwdzt)) {
			return "确认开始";
		} else if ("swlb_ks".equals(rwdzt)) {
			return "确认完成";
		} else if ("swlb_wc".equals(rwdzt)) {
			return "确认返回";
		}
		return "";
	}

}
