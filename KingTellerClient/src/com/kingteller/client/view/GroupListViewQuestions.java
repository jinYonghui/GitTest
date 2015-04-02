package com.kingteller.client.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kingteller.R;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.CommonSelectGZBJ;
import com.kingteller.framework.utils.CommonUtils;

public class GroupListViewQuestions extends LinearLayout implements
		android.view.View.OnClickListener {

	private AddViewCallBack addViewCallBack;
	private LinearLayout layout_list;

	public LinearLayout getLayoutList() {
		return layout_list;
	}

	public GroupListViewQuestions(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public GroupListViewQuestions(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		LayoutInflater.from(getContext()).inflate(R.layout.st_group_list_view,
				this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 0);
		setLayoutParams(lp);
		layout_list = (LinearLayout) findViewById(R.id.layout_list);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_add:
			if (addViewCallBack != null) {
				addViewCallBack.addView(GroupListViewQuestions.this);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 添加一个视图
	 * @param view
	 */
	public void addItem(View view) {
		layout_list.addView(view);
	}

	/**
	 * 得到所有视图的数据，视图必须为GroupViewBase的扩展类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getListData() {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < layout_list.getChildCount(); i++) {
			list.add((T) ((GroupViewBase) layout_list.getChildAt(i)).getData());
		}
		return list;
	}

	public LinearLayout getLinearLayout(int index){
		return (LinearLayout)layout_list.getChildAt(index);
	}
	
	/*public int getChildCount(){
		return layout_list.getChildCount();
	}
	*/
	/**
	 * 设置值
	 * @param data
	 */
	public void setItemData(CommonSelectData data) {
		((KingTellerEditText) layout_list.getTag()).setFieldTextAndValue(data);
	}
	
	public void setItemEditTextData(CommonSelectData data){
		((EditText) layout_list.getTag()).setText(data.getText());
	}
	
	public void setItemData(String text){
		((KingTellerEditText) layout_list.getTag()).setFieldTextAndValue(text);
	}

	/**
	 * 多选设置逗号分隔的
	 * @param data
	 */
	public void setItemDataMul(CommonSelectData data) {
		KingTellerEditText view = (KingTellerEditText) layout_list.getTag();
		if (CommonUtils.isEmpty(view.getFieldValue()))
			setItemData(data);
		else
			((KingTellerEditText) layout_list.getTag()).setFieldTextAndValue(
					view.getFieldText() + "," + data.getText(),
					view.getFieldValue() + "," + data.getValue());
	}

	public void setAddViewCallBack(AddViewCallBack addViewCallBack) {
		this.addViewCallBack = addViewCallBack;
	}

	/**
	 * 添加视图回调接口
	 * @author 王定波
	 *
	 */
	public interface AddViewCallBack {
		public void addView(GroupListViewQuestions view);
	}

	public void setItemData(CommonSelectGZBJ data) {
		// TODO Auto-generated method stub
		((KingTellerEditText) layout_list.getTag()).setFieldTextAndValue(data);
	}

	
}
