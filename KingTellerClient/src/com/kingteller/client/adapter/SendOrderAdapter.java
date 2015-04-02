package com.kingteller.client.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingteller.R;
import com.kingteller.client.bean.workorder.ReturnBackStatus;
import com.kingteller.client.bean.workorder.SendOrderBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class SendOrderAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<SendOrderBean> sendOrderList = new ArrayList<SendOrderBean>();
		private Context context;
		private String tabName;
		private SendOrderBean sendOrder;
		private Callback callBack;

		public SendOrderAdapter(Context context,List<SendOrderBean> sendOrderList){
			inflater = LayoutInflater.from(context);
			this.context = context;
			this.sendOrderList = sendOrderList;
		}
		
		public SendOrderAdapter(Context context, List<SendOrderBean> sendOrderList,String tabName) {
			inflater = LayoutInflater.from(context);
			this.sendOrderList = sendOrderList;
			this.tabName = tabName;
			this.context = context;
		}

		public void setLists(List<SendOrderBean> sendOrderList) {
			this.sendOrderList = sendOrderList;
			notifyDataSetChanged();
		}

		public void addLists(List<SendOrderBean> sendOrderList) {
			this.sendOrderList.addAll(sendOrderList);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (sendOrderList == null) {
				return 0;
			}
			return sendOrderList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return sendOrderList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}   

		@Override
		public View getView(final int postion, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			sendOrder = sendOrderList.get(postion);
			
			ViewHoler viewHoler = null;
			if (v == null) {
				viewHoler = new ViewHoler();
				v = inflater.inflate(R.layout.item_sendorder, null);
				viewHoler.orderNo = (TextView) v.findViewById(R.id.orderNo);
				viewHoler.cmFlag = (ImageView) v.findViewById(R.id.cmFlag);
				viewHoler.atmMsg = (TextView) v.findViewById(R.id.atmMsg);
				viewHoler.troubleRemark = (TextView) v.findViewById(R.id.troubleRemark);
				viewHoler.assignWorkerName = (TextView) v.findViewById(R.id.assignWorkerName);
				viewHoler.assignMan = (TextView) v.findViewById(R.id.assignMan);
				viewHoler.assignTime = (TextView) v.findViewById(R.id.assignTime);
				viewHoler.status = (TextView) v.findViewById(R.id.status);
				viewHoler.prearrangeDateStr = (TextView) v.findViewById(R.id.prearrangeDateStr);
				viewHoler.wdsbjc = (TextView) v.findViewById(R.id.wdsbjc);
				viewHoler.btn_button = (Button) v.findViewById(R.id.btn_button);
				v.setTag(viewHoler);
			}else{
				viewHoler = (ViewHoler)v.getTag();
			}
			
			if(sendOrder.getCmFlag().equals("1")){
				viewHoler.cmFlag.setVisibility(View.VISIBLE);
			}else{
				viewHoler.cmFlag.setVisibility(View.GONE);
			}
			
			viewHoler.orderNo.setText(sendOrder.getOrderNo());
			viewHoler.atmMsg.setText(sendOrder.getATMCode()+"("+sendOrder.getATMBankcode()+")");
			viewHoler.troubleRemark.setText(sendOrder.getTroubleRemark());
			viewHoler.assignWorkerName.setText(sendOrder.getAssignWorkerName());
			viewHoler.assignMan.setText(sendOrder.getAssignName());
			viewHoler.assignTime.setText(sendOrder.getAssignTime());
			viewHoler.wdsbjc.setText(sendOrder.getWdsbjc());
			
			//工单状态：new-新单(20)，accept-接障(30)，arrive-到达(99)，begin-开始维护(40),finish-维护结束(1)
			if(tabName.equals("tab1") || tabName.equals("tab2")){
				if( "new".equals(sendOrder.getStatus())){
					viewHoler.btn_button.setText("接障");
				}else if("accept".equals(sendOrder.getStatus())){
					viewHoler.btn_button.setText("到达现场");
				}else if("arrive".equals(sendOrder.getStatus())){
					viewHoler.btn_button.setText("开始维护");
				}else if("begin".equals(sendOrder.getStatus())){
					viewHoler.btn_button.setText("维护结束");
				}
			}else{
				viewHoler.btn_button.setVisibility(View.GONE);
			}
			if(!CommonUtils.isEmpty(sendOrder.getStatus())){
				
				if( "new".equals(sendOrder.getStatus())){
					viewHoler.status.setText("新单");
				}else if("accept".equals(sendOrder.getStatus())){
					viewHoler.status.setText("接障");
				}else if("arrive".equals(sendOrder.getStatus())){
					viewHoler.status.setText("到达现场");
				}else if("begin".equals(sendOrder.getStatus())){
					viewHoler.status.setText("开始维护");
				}else if("finish".equals(sendOrder.getStatus())){
					viewHoler.status.setText("维护结束");
				}else{
					viewHoler.status.setText(sendOrder.getStatus());
				}
				((LinearLayout)viewHoler.status.getParent()).setVisibility(View.VISIBLE);
			}else{
				((LinearLayout)viewHoler.status.getParent()).setVisibility(View.GONE);
			}

			if(sendOrder.getArrangeType().equals("1")){
				viewHoler.prearrangeDateStr.setText(sendOrder.getPrearrangeDateStr());
				((LinearLayout)viewHoler.prearrangeDateStr.getParent()).setVisibility(View.VISIBLE);
			}else{
				((LinearLayout)viewHoler.prearrangeDateStr.getParent()).setVisibility(View.GONE);
			}
			
			final SendOrderBean sob = sendOrder; 
			
			viewHoler.btn_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					KTAlertDialog dialog = new KTAlertDialog.Builder(context)
					.setTitle("提示")
					.setMessage("您确定要"+((Button)arg0).getText().toString()+"吗？")
					.setPositiveButton("确定",new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int pos) {
									submitStatus(sob.getOrderId(), sob.getStatus(), sob.getOrderType());
									dialogInterface.dismiss();
								}
							})
					.setNegativeButton("取消",new KTAlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialogInterface,int paramAnonymousInt) {
									dialogInterface.dismiss();
								}
							}).create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
					
				}
			});
			
			return v;
		}
		
		private static class ViewHoler{
			public TextView orderNo;
			public ImageView cmFlag;
			public TextView atmMsg;
			public TextView troubleRemark;
			public TextView assignWorkerName;
			public TextView assignMan;
			public TextView assignTime;
			public TextView status;
			public TextView prearrangeDateStr;
			public TextView wdsbjc;
			public Button btn_button;
		} 
		
		public void submitStatus(String orderId,String status,String orderType){
			KingTellerProgress.showProgress(context, "正在提交...");
			KTHttpClient fh = new KTHttpClient(true);
			AjaxParams params = new AjaxParams();
			params.put("orderId",orderId);
			params.put("status",status);
			params.put("orderType", orderType);
			
			fh.get(ConfigUtils.CreateNoVersionUrl(context, URLConfig.WebRwdztUrl),
					params, new AjaxHttpCallBack<ReturnBackStatus>(context,true) {
				
						@Override
						public void onFinish() {
							KingTellerProgress.closeProgress();
						}
						
						@Override
						public void onDo(ReturnBackStatus data) {
							if( data.getResult().equals("fail")){
								callBack.onFalse(data.getMessage());
							}else if(data.getResult().equals("success")){
								callBack.onSuccess();
							}
						};
					});
		}
		
		public void setCallBack(Callback callBack) {
			this.callBack = callBack;
		}

		public interface Callback{
			public void onSuccess();
			public void onFalse(String msg);
		}
}
