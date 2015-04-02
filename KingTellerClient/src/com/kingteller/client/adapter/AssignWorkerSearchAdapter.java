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
import com.kingteller.client.bean.workorder.AssignWorkerNameBean;

public class AssignWorkerSearchAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private AssignWorkerNameBean assignWorkerNameBean;
	private HashMap<Integer, Boolean> isCheckedMap = new HashMap<Integer, Boolean>();
	private List<AssignWorkerNameBean> assignWorkerNamelist = new ArrayList<AssignWorkerNameBean>();
	private List<AssignWorkerNameBean> workerNameCheckedList = new ArrayList<AssignWorkerNameBean>();

	public AssignWorkerSearchAdapter(Context context, List<AssignWorkerNameBean> assignWorkerNamelist) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.assignWorkerNamelist = assignWorkerNamelist;
	}

	public void setLists(List<AssignWorkerNameBean> assignWorkerNamelist) {
		this.assignWorkerNamelist = assignWorkerNamelist;
		notifyDataSetChanged();
	}

	public void addLists(List<AssignWorkerNameBean> assignWorkerNamelist) {
		this.assignWorkerNamelist.addAll(assignWorkerNamelist);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (assignWorkerNamelist == null) {
			return 0;
		}
		return assignWorkerNamelist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return assignWorkerNamelist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int postion, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		assignWorkerNameBean = assignWorkerNamelist.get(postion);
		
		isCheckedMap.clear();
		ViewHoler viewHoler = null;
		if (v == null) {
			viewHoler = new ViewHoler();
			v = inflater.inflate(R.layout.item_assign_worker, null);
			viewHoler.userAccount = (TextView) v.findViewById(R.id.userAccount);
			viewHoler.userName = (TextView) v.findViewById(R.id.userName);
			viewHoler.linkPhone = (TextView) v.findViewById(R.id.linkPhone);
			viewHoler.work_flag = (TextView) v.findViewById(R.id.work_flag);
			viewHoler.checkBox = (CheckBox) v.findViewById(R.id.checkBox);
			
			viewHoler.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0,boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						if (!workerNameCheckedList.contains(assignWorkerNamelist.get(postion))) {
							workerNameCheckedList.add(assignWorkerNamelist.get(postion));
						}
						isCheckedMap.put(postion, isChecked);
					} else {
						if (workerNameCheckedList.contains(assignWorkerNamelist.get(postion))) {
							workerNameCheckedList.remove(assignWorkerNamelist.get(postion));
						}
						isCheckedMap.remove(postion);
					}

				}
			});
			viewHoler.checkBox.setChecked(isCheckedMap.get(postion) == null ? false :true);
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					if(((CheckBox)arg0.findViewById(R.id.checkBox)).isChecked()){
						((CheckBox)arg0.findViewById(R.id.checkBox)).setChecked(false);
						if (workerNameCheckedList.contains(assignWorkerNamelist.get(postion))) {
							workerNameCheckedList.remove(assignWorkerNamelist.get(postion));
						}
						isCheckedMap.remove(postion);
					}else{
						((CheckBox)arg0.findViewById(R.id.checkBox)).setChecked(true);
						if (!workerNameCheckedList.contains(assignWorkerNamelist.get(postion))) {
							workerNameCheckedList.add(assignWorkerNamelist.get(postion));
						}
						isCheckedMap.put(postion, true);
					}
				}
			});
			
			v.setTag(viewHoler);
		}else{
			viewHoler = (ViewHoler)v.getTag();
		}
		
		viewHoler.userAccount.setText(assignWorkerNameBean.getUserAccount());
		viewHoler.userName.setText(assignWorkerNameBean.getUserName());
		if(assignWorkerNameBean.getWorkFlag() == 0){
			viewHoler.work_flag.setText("休假");
		}else if(assignWorkerNameBean.getWorkFlag() == 2){
			viewHoler.work_flag.setText("请假");
		}else{
			viewHoler.work_flag.setText("空闲");
		}
		viewHoler.linkPhone.setText(assignWorkerNameBean.getLinkPhone());
		
		return v;
	}
	
	private  class ViewHoler{
		public TextView userAccount;
		public TextView userName;
		public TextView linkPhone;
		public TextView work_flag;
		public CheckBox checkBox;
	}

	public List<AssignWorkerNameBean> getWorkerNameCheckedList(){
		return workerNameCheckedList ;
	}
	
}
