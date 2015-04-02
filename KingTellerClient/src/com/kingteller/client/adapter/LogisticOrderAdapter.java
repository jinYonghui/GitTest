package com.kingteller.client.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kingteller.R;
import com.kingteller.client.activity.logisticmonitor.TaskNRActivity;
import com.kingteller.client.bean.logisticmonitor.LogisticOrderBean;

public class LogisticOrderAdapter  extends BaseAdapter {

	private LayoutInflater inflater;
	private List<LogisticOrderBean> logisticOrderlist = new ArrayList<LogisticOrderBean>();
	private Context context;
	private LogisticOrderBean logisticOrder;

	public LogisticOrderAdapter(Context context,List<LogisticOrderBean> logisticOrderlist){
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.logisticOrderlist = logisticOrderlist;
	}

	public void setLists(List<LogisticOrderBean> logisticOrderlist) {
		this.logisticOrderlist = logisticOrderlist;
		notifyDataSetChanged();
	}

	public void addLists(List<LogisticOrderBean> logisticOrderlist) {
		this.logisticOrderlist.addAll(logisticOrderlist);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (logisticOrderlist == null) {
			return 0;
		}
		return logisticOrderlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return logisticOrderlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}   

	@Override
	public View getView(final int postion, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		logisticOrder = logisticOrderlist.get(postion);
		
		ViewHoler viewHoler = null;
		if (v == null) {
			viewHoler = new ViewHoler();
			v = inflater.inflate(R.layout.item_tasklist, null);
			viewHoler.rwdh = (TextView) v.findViewById(R.id.rwdh);
			viewHoler.tj = (TextView) v.findViewById(R.id.tj);
			viewHoler.sl = (TextView) v.findViewById(R.id.sl);
			viewHoler.pdsj = (TextView) v.findViewById(R.id.pdsj);
			viewHoler.tyzt1 = (TextView) v.findViewById(R.id.tyzt1);
			viewHoler.tyzt2 = (TextView) v.findViewById(R.id.tyzt2);
			viewHoler.listView = (ListView) v.findViewById(R.id.listView);
			v.setTag(viewHoler);
		}else{
			viewHoler = (ViewHoler)v.getTag();
		}

		viewHoler.rwdh.setText(logisticOrder.getRwdh());
		viewHoler.tj.setText(logisticOrder.getRwlx()+"["+logisticOrder.getTylx()+"]");
		viewHoler.sl.setText(logisticOrder.getSl());
		viewHoler.pdsj.setText(logisticOrder.getPdsj());
		viewHoler.tyzt1.setText(logisticOrder.getTyzt1());
		viewHoler.tyzt2.setText(logisticOrder.getTyzt2());
		
		
		
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(context,TaskNRActivity.class));
			}
		});
		return v;
	}
	
	private static class ViewHoler{
		public TextView rwdh;
		public TextView tj;
		public TextView sl;
		public TextView pdsj;
		public TextView tyzt1;
		public TextView tyzt2;
		public ListView listView;
	} 
}
