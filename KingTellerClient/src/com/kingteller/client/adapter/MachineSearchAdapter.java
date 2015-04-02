package com.kingteller.client.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kingteller.R;
import com.kingteller.client.bean.workorder.MachineinfoSimpleBean;

public class MachineSearchAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<MachineinfoSimpleBean> machineInfolist = new ArrayList<MachineinfoSimpleBean>();
	private Context context;
	private MachineinfoSimpleBean machineInfo;
	private HashMap<Integer, Boolean> isCheckedMap ;
	public List<MachineinfoSimpleBean> machiceInfoCheckedList = new ArrayList<MachineinfoSimpleBean>();

	public MachineSearchAdapter(Context context, List<MachineinfoSimpleBean> machineInfolist) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.machineInfolist = machineInfolist;
	}

	public void setLists(List<MachineinfoSimpleBean> machineInfolist) {
		this.machineInfolist = machineInfolist;
		notifyDataSetChanged();
		if(isCheckedMap != null){
			isCheckedMap = null;
		}
		isCheckedMap = new HashMap<Integer, Boolean>();
		for(int i = 0;i < machineInfolist.size();i ++){
			isCheckedMap.put(i, false);
		}
		if(machiceInfoCheckedList.size() > 0){
			machiceInfoCheckedList.clear();
		}
	}

	public void addLists(List<MachineinfoSimpleBean> machineInfolist) {
		this.machineInfolist.addAll(machineInfolist);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (machineInfolist == null) {
			return 0;
		}
		return machineInfolist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return machineInfolist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int postion, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		machineInfo = machineInfolist.get(postion);
		
		//isCheckedMap.clear();
		ViewHoler viewHoler = null;
		if (v == null) {
			viewHoler = new ViewHoler();
			v = inflater.inflate(R.layout.item_machine_search, null);
			viewHoler.atmh = (TextView) v.findViewById(R.id.atmh);
			viewHoler.jqbh = (TextView) v.findViewById(R.id.jqbh);
			viewHoler.ssyh = (TextView) v.findViewById(R.id.ssyh);
			viewHoler.wdsbjc = (TextView) v.findViewById(R.id.wdsbjc);
			viewHoler.checkBox = (CheckBox) v.findViewById(R.id.checkBox);
			
			viewHoler.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0,boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if (!machiceInfoCheckedList.contains(machineInfolist.get(postion))) {
							machiceInfoCheckedList.add(machineInfolist.get(postion));
						}
						isCheckedMap.put(postion, isChecked);
					} else {
						if (machiceInfoCheckedList.contains(machineInfolist.get(postion))) {
							machiceInfoCheckedList.remove(machineInfolist.get(postion));
						}
						isCheckedMap.remove(postion);
					}
				}
			});
			viewHoler.checkBox.setChecked(isCheckedMap.get(postion) == false ? false :true);
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(((CheckBox) arg0.findViewById(R.id.checkBox)).isChecked()){
						((CheckBox) arg0.findViewById(R.id.checkBox)).setChecked(false);
						if (machiceInfoCheckedList.contains(machineInfolist.get(postion))) {
							machiceInfoCheckedList.remove(machineInfolist.get(postion));
						}
						isCheckedMap.remove(postion);
					}else{
						((CheckBox) arg0.findViewById(R.id.checkBox)).setChecked(true);
						if (!machiceInfoCheckedList.contains(machineInfolist.get(postion))) {
							machiceInfoCheckedList.add(machineInfolist.get(postion));
						}
						isCheckedMap.put(postion, true);
					}
				}
			});
			v.setTag(viewHoler);
		}else{
			viewHoler = (ViewHoler)v.getTag();
		}
		
		viewHoler.atmh.setText(machineInfo.getAtmh());
		viewHoler.jqbh.setText(machineInfo.getJqbh());
		viewHoler.ssyh.setText(machineInfo.getSsyh());
		viewHoler.wdsbjc.setText(machineInfo.getWdsbjc());
		
		return v;
	}
	
	private  class ViewHoler{
		public TextView atmh;
		public TextView jqbh;
		public TextView ssyh;
		public TextView wdsbjc;
		public CheckBox checkBox;
	}

	public List<MachineinfoSimpleBean> getMachineInfoCheckedList(){
		return machiceInfoCheckedList ;
	}
}
