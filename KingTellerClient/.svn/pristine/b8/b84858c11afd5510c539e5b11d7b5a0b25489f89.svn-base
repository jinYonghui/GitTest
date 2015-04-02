package com.kingteller.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.workorder.BaseReportActivity;
import com.kingteller.client.activity.workorder.CleanReportActivity;
import com.kingteller.client.activity.workorder.checkout.LogisticsReportCheckoutActivity;
import com.kingteller.client.activity.workorder.checkout.OtherMatterReportCheckoutActivity;
import com.kingteller.client.activity.workorder.checkout.RepairReportCheckoutActivity;
import com.kingteller.client.bean.workorder.AduitReportBean;
import com.kingteller.client.bean.workorder.CleanReportBean;
import com.kingteller.client.bean.workorder.LogisticsReportBean;
import com.kingteller.client.bean.workorder.OtherMatterReportBean;
import com.kingteller.client.bean.workorder.RepairReportBean;
import com.kingteller.client.bean.workorder.WorkOrderBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

@SuppressLint("UseSparseArrays")
public class AduitReportAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<AduitReportBean> aduitList;
	private AduitReportBean aduitBean;
	private List<AduitReportBean> aduitCheckedList = new ArrayList<AduitReportBean>();
	private String executionState;
	private HashMap<Integer, Boolean> isCheckedMap = new HashMap<Integer, Boolean>();
	
	public AduitReportAdapter(Context context,List<AduitReportBean> aduitList,String executionState){
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.aduitList = aduitList;
		this.executionState = executionState;
	}
	
	public void setLists(List<AduitReportBean> aduitList) {
		this.aduitList = aduitList;
		notifyDataSetChanged();
	}

	public void addLists(List<AduitReportBean> aduitList) {
		this.aduitList.addAll(aduitList);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if( aduitList == null){
			return 0;
		}
		return aduitList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return aduitList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int pos, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		aduitBean = aduitList.get(pos);
		aduitCheckedList.clear();
		isCheckedMap.clear();
		ViewHoler viewHoler = null;
		if (v == null) {
			viewHoler = new ViewHoler();
			v = inflater.inflate(R.layout.item_aduit_report, null);
			viewHoler.checkBox = (CheckBox) v.findViewById(R.id.checkBox);
			viewHoler.orderNo = (TextView) v.findViewById(R.id.orderNo);
			viewHoler.orderType = (TextView) v.findViewById(R.id.orderType);
			viewHoler.assignMan = (TextView) v.findViewById(R.id.assignMan);
			viewHoler.assignTime = (TextView) v.findViewById(R.id.assignTime);
			viewHoler.atmMsg = (TextView) v.findViewById(R.id.atmMsg);
			viewHoler.taskInfo = (TextView) v.findViewById(R.id.taskInfo);
			viewHoler.relativeLayout = (RelativeLayout) v.findViewById(R.id.relativelayout);
			if(executionState.equals(BaseReportActivity.OPT_PROCESSED)){
				viewHoler.checkBox.setVisibility(View.GONE);
			}
			v.setTag(viewHoler);
		}else{
			viewHoler = (ViewHoler)v.getTag();
		}
		viewHoler.orderNo.setText(aduitBean.getOrderNo());
		viewHoler.orderType.setText(getOrderType(aduitBean.getReportProperty()));
		viewHoler.assignMan.setText(aduitBean.getExeUserName());
		/*if(executionState.equals(BaseReportActivity.OPT_UNTREATED)){
			viewHoler.assignTime.setText(SharedPreferencesUtils.getData(context, aduitBean.getOrderId()));
		}else{*/
			viewHoler.assignTime.setText(aduitBean.getSubmitTime());
		//}
		viewHoler.atmMsg.setText(aduitBean.getATMCode());
		viewHoler.taskInfo.setText(aduitBean.getTroubleRemark());
		
		viewHoler.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0,boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					if (!aduitCheckedList.contains(aduitList.get(pos))) {
						aduitCheckedList.add(aduitList.get(pos));
					}
					isCheckedMap.put(pos, isChecked);
				} else {
					if (aduitCheckedList.contains(aduitList.get(pos))) {
						aduitCheckedList.remove(aduitList.get(pos));
					}
					isCheckedMap.remove(pos);
				}

			}
		});
		viewHoler.checkBox.setChecked(isCheckedMap.get(pos) == null ? false :true);
		viewHoler.relativeLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				singleAudit(executionState,aduitList.get(pos));
			}
		});
		//未处理;
		return v;
	}
	
	private static class ViewHoler{
		public CheckBox checkBox;
		public TextView orderNo;
		public TextView orderType;
		public TextView assignMan;
		public TextView assignTime;
		public TextView atmMsg;
		public TextView taskInfo;
		public RelativeLayout relativeLayout;
	} 
	
	public List<AduitReportBean> getAduitCheckedList(){
		return aduitCheckedList ;
	}
	
	public String getOrderType(String orderType){
		String type = "";
		if(CommonUtils.isEmpty(orderType)){
			type = "";
		}else if("maintenance".equals(orderType)){
			type = "维护";
		}else if("otherMatter".equals(orderType)){
			type = "其他事物";
		}else if("logistics".equals(orderType)){
			type = "物流";
		}else if("clean".equals(orderType)){
			type = "清洁";
		}
		return type;
	}
	
	public void singleAudit(final String state,AduitReportBean aduitReportBean){
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("reportId", aduitReportBean.getReportId());
		params.put("reportType", aduitReportBean.getReportProperty());
		params.put("orderId", aduitReportBean.getOrderId());
		if(aduitReportBean.getReportProperty().equals("otherMatter")){
			fh.post(ConfigUtils.CreateUrl(context, URLConfig.WebDgspUrl),
					params, new AjaxHttpCallBack<OtherMatterReportBean>(context,
							new TypeToken<OtherMatterReportBean>() {
							}.getType(), true) {

						@Override
						public void onFinish() {
						}

						@Override
						public void onDo(OtherMatterReportBean data) {
							setJump(data.getOrderId(), "otherMatter", state,BaseReportActivity.OPT_ADUIT,OtherMatterReportCheckoutActivity.class);
						};
					});
		}else if(aduitReportBean.getReportProperty().equals("logistics")){
			fh.post(ConfigUtils.CreateUrl(context, URLConfig.WebDgspUrl),
					params, new AjaxHttpCallBack<LogisticsReportBean>(context,
							new TypeToken<LogisticsReportBean>() {
							}.getType(), true) {

						@Override
						public void onFinish() {
						}

						@Override
						public void onDo(LogisticsReportBean data) {
							setJump(data.getOrderId(), "logistics", state,BaseReportActivity.OPT_ADUIT,LogisticsReportCheckoutActivity.class);
						};
					});
		}else if(aduitReportBean.getReportProperty().equals("clean")){
			fh.post(ConfigUtils.CreateUrl(context, URLConfig.WebDgspUrl),
					params, new AjaxHttpCallBack<CleanReportBean>(context,
							new TypeToken<CleanReportBean>() {
							}.getType(), true) {

						@Override
						public void onFinish() {
						}

						@Override
						public void onDo(CleanReportBean data) {
							setJump(data.getOrderId(), "clean", state,BaseReportActivity.OPT_ADUIT, CleanReportActivity.class);
						};
					});
		}else if(aduitReportBean.getReportProperty().equals("maintenance")){
			fh.post(ConfigUtils.CreateUrl(context, URLConfig.WebDgspUrl),
					params, new AjaxHttpCallBack<RepairReportBean>(context,
							new TypeToken<RepairReportBean>() {
							}.getType(), true) {

						@Override
						public void onFinish() {
						}

						@Override
						public void onDo(RepairReportBean data) {
							setJump(data.getOrderId(), "maintenance", state,BaseReportActivity.OPT_ADUIT,RepairReportCheckoutActivity.class);
						};
					});
		}else {
			Toast.makeText(context, "数据错误", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setJump(String orderId,String dataType,String state,int optType,Class<?> clazz){
		WorkOrderBean workOrderBean = new WorkOrderBean();
		workOrderBean.setOrderId(orderId);
		workOrderBean.setOrderType(dataType);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("reportBean", workOrderBean);
		intent.putExtras(bundle);
		intent.putExtra(BaseReportActivity.OPT_TYPE, optType);
		intent.putExtra(BaseReportActivity.OPT_STATE_TYPE, state);
		context.startActivity(intent.setClass(this.context,clazz));
	}
}
