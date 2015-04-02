package com.kingteller.client.activity.logisticmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.adapter.OtherTaskConsignAdapter;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.logisticmonitor.FareDerail;
import com.kingteller.client.bean.logisticmonitor.OtherTaskConsignBean;
import com.kingteller.client.bean.logisticmonitor.ResultStatus;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.GroupListView;
import com.kingteller.client.view.GroupListView.AddViewCallBack;
import com.kingteller.client.view.GroupViewBase;
import com.kingteller.client.view.KingTellerEditText;
import com.kingteller.client.view.ListViewForScrollView;
import com.kingteller.client.view.OtherTaskFareGroupView;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class WriteOtherTaskActivity extends BaseFragmentActivity implements
		OnClickListener {

	private ListViewForScrollView lvfsv;
	private GroupListView costType_group_list;
	private OtherTaskConsignAdapter otherTaskConsignAdapter;
	private List<OtherTaskConsignBean> otherTaskConsignlist = new ArrayList<OtherTaskConsignBean>();
	private Button btn_back,btn_submit;
	private KingTellerEditText qcqlcs;
	private KingTellerEditText fhckhlcs;
	

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.layout_other_task_write);
		initUI();
		initData();
	}

	public void initUI() {
		title_title.setText("任务内容");
		title_left.setOnClickListener(this);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		lvfsv = (ListViewForScrollView) findViewById(R.id.otherconsign_li);
		lvfsv.setDivider(null);

		costType_group_list = (GroupListView) findViewById(R.id.costType_group_list);
		costType_group_list.setAddViewCallBack(new AddViewCallBack() {

			@Override
			public void addView(GroupListView view) {
				// TODO Auto-generated method stub
				view.addItem(new OtherTaskFareGroupView(
						WriteOtherTaskActivity.this, true));
			}
		});

		OtherTaskFareGroupView bjView = new OtherTaskFareGroupView(this, false);
		bjView.setOnClickListener(this);
		costType_group_list.addItem(bjView);
		
		qcqlcs = (KingTellerEditText) findViewById(R.id.qcqlcs);
		fhckhlcs = (KingTellerEditText) findViewById(R.id.fhckhlcs);
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		
		CommonUtils.hideInputMethod(this);
	}

	public void initData() {
		otherTaskConsignAdapter = new OtherTaskConsignAdapter(this, otherTaskConsignlist);
		getOtherTaskConsign();
		lvfsv.setAdapter(otherTaskConsignAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_submit:
			submit();
			break;

		default:
			break;
		}

	}
	public boolean checkSubmit(){
		
		int count = costType_group_list.getLayoutList().getChildCount();
		if(count > 0){			
			for(int i = 0; i < count;i ++){
				GroupViewBase listView = (GroupViewBase)costType_group_list.getLayoutList().getChildAt(i);
				String fylx = ((KingTellerEditText)listView.findViewById(R.id.fylx)).getFieldValue();
				String je = ((KingTellerEditText)listView.findViewById(R.id.je)).getFieldValue();
				if(CommonUtils.isEmpty(fylx)){
					toastMsg("费用类型不能为空！");
					return false;
				}
				if(CommonUtils.isEmpty(je)){
					toastMsg("费用不能为空！");
					return false;
				}
			}
		}else{
			toastMsg("费用信息必须填写！");
			return false;
		}
		
		if(CommonUtils.isEmpty(fhckhlcs.getFieldValue())){
			toastMsg("请填写返回里程数！");
			return false;
		}
		return true;
	}
	
	public String getFromData(){
		HashMap<String, Object> params = ConditionUtils.getHashMapForm(this,
				(LinearLayout) findViewById(R.id.layout_from), false);
		
		List<FareDerail> fareList = new ArrayList<FareDerail>();
		int count = costType_group_list.getLayoutList().getChildCount();		
		for(int i = 0; i < count;i ++){
			FareDerail fd = new FareDerail();
			GroupViewBase listView = (GroupViewBase)costType_group_list.getLayoutList().getChildAt(i);
			String fylx = ((KingTellerEditText)listView.findViewById(R.id.fylx)).getFieldValue();
			String je = ((KingTellerEditText)listView.findViewById(R.id.je)).getFieldValue();
			fd.setFylx(fylx);
			fd.setJe(je);
			fareList.add(fd);
		}
		params.put("fareList", fareList);
		params.put("swdh", getIntent().getStringExtra("swdh"));
		Log.e("28346238462",ConditionUtils.getJsonFromObj(params));
		return ConditionUtils.getJsonFromObj(params);
	}
	
	public void submit(){
		if(checkSubmit()){
		
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		// 获取condition实例
		AjaxParams params = new AjaxParams();
		params.put("params", this.getFromData());

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.WljkOtSubmitUrl), params,
				new AjaxHttpCallBack<ResultStatus>(this,
						new TypeToken<ResultStatus>() {}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(ResultStatus data) {
						if (data.getResult().equals("success")) {
							finish();
						}
						toastMsg(data.getMessage());
					};
				});
		}
	}
	
	public void getOtherTaskConsign() {	
		if(!CommonUtils.isNetAvaliable(this)){
			Toast.makeText(this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Intent intent = this.getIntent(); 
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("swdh", intent.getStringExtra("swdh"));
		params.put("cl", intent.getStringExtra("cl"));

		fh.get(ConfigUtils.CreateNoVersionUrl(this, URLConfig.WljkcUrl),
				params, new AjaxHttpCallBack<BasePageBean<OtherTaskConsignBean>>(this,
						new TypeToken<BasePageBean<OtherTaskConsignBean>>() {
						}.getType(), true) {
					
					@Override
					public void onFinish() {
					}
					
					@Override
					public void onDo(BasePageBean<OtherTaskConsignBean> data) {
						if("".equals(data.getStatus()) ){
							if (data.getList().size() > 0) {
								otherTaskConsignlist = data.getList();
								otherTaskConsignAdapter.setLists(data.getList());
								qcqlcs.setTextAndValue(data.getList().get(0).getExpand1().toString(), data.getList().get(0).getExpand1().toString());
							} else {
								
							}
						}else{
							toastMsg(data.getMsg());
						}
					};
				});
	}
}
