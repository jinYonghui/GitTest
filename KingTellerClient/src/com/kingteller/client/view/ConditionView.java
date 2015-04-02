package com.kingteller.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kingteller.R;
import com.kingteller.client.adapter.SelectListAdapter;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.KingTellerDateTime;
import com.kingteller.client.view.KingTellerEditText.OnChangeListener;
import com.kingteller.client.view.KingTellerEditText.OnDialogClickLister;
import com.kingteller.client.view.datatype.DataTypes;
import com.kingteller.framework.condition.FieldCond;
import com.kingteller.framework.utils.CommonUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 条件通用View
 * 
 * @author 王定波
 * 
 */
public class ConditionView extends LinearLayout implements
		android.view.View.OnClickListener {

	private Context mContext;
	private String fieldName;
	private String fieldValue;
	private String fieldTitle;

	private boolean fieldRequested;
	private int fieldType;
	private int fieldOp;
	private TextView title;
	private EditText edittext;
	private TextView textview;
	private String fieldHint;
	private String fieldText;

	public final static int QUERY_TYPE_EQ = 0;
	public final static int QUERY_TYPE_LIKE = 1;
	public final static int EQ = 2;
	public final static int NOT_EQ = 3;
	public final static int LESS_THAN = 4;
	public final static int LESS_EQ_THAN = 5;
	public final static int LARGE_THAN = 6;
	public final static int LARGE_EQ_THAN = 7;
	public final static int LARGE_LESS = 8;
	public final static int LARGE_LESS_EQ = 9;
	public final static int LIKE = 10;
	public final static int LLIKE = 11;
	public final static int RLIKE = 12;
	public final static int ISNOT = 13;
	public final static int IN = 14;

	private String[] ConditionOP = { FieldCond.QUERY_TYPE_EQ,
			FieldCond.QUERY_TYPE_LIKE, FieldCond.EQ, FieldCond.NOT_EQ,
			FieldCond.LESS_THAN, FieldCond.LESS_EQ_THAN, FieldCond.LARGE_THAN,
			FieldCond.LARGE_EQ_THAN, FieldCond.LARGE_LESS,
			FieldCond.LARGE_LESS_EQ, FieldCond.LIKE, FieldCond.LLIKE,
			FieldCond.RLIKE, FieldCond.ISNOT, FieldCond.IN };

	private int[] ConditionType = {
			com.kingteller.framework.condition.DataTypes.VARCHAR,
			com.kingteller.framework.condition.DataTypes.NUMBER,
			com.kingteller.framework.condition.DataTypes.DATE,
			com.kingteller.framework.condition.DataTypes.DATE,
			com.kingteller.framework.condition.DataTypes.TIME,
			com.kingteller.framework.condition.DataTypes.VARCHAR,
			com.kingteller.framework.condition.DataTypes.VARCHAR };

	private List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
	private int fieldBindData;
	private KTAlertDialog dialog;
	private boolean fieldEnabled;
	private OnChangeListener onChangeListener;
	private OnDialogClickLister onDialogClickLister;

	public ConditionView(Context context) {
		super(context);
		mContext = context;
		initView(context);
	}

	public ConditionView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
				R.styleable.kingteller_edittext);
		fieldName = typedArray
				.getString(R.styleable.kingteller_edittext_fieldName);
		fieldValue = typedArray
				.getString(R.styleable.kingteller_edittext_fieldValue);
		fieldTitle = typedArray
				.getString(R.styleable.kingteller_edittext_fieldTitle);
		fieldText = typedArray
				.getString(R.styleable.kingteller_edittext_fieldText);

		fieldRequested = typedArray.getBoolean(
				R.styleable.kingteller_edittext_fieldRequested, false);
		fieldType = typedArray.getInt(
				R.styleable.kingteller_edittext_fieldType, DataTypes.String);
		fieldOp = typedArray.getInt(R.styleable.kingteller_edittext_fieldOp,
				fieldType == DataTypes.String ? LIKE : EQ);
		fieldHint = typedArray
				.getString(R.styleable.kingteller_edittext_fieldHint);
		fieldBindData = typedArray.getResourceId(
				R.styleable.kingteller_edittext_fieldBindData, 0);
		fieldEnabled = typedArray.getBoolean(
				R.styleable.kingteller_edittext_fieldEnabled, true);

		if (!CommonUtils.isEmpty(fieldText) && CommonUtils.isEmpty(fieldValue))
			fieldValue = fieldText;
		else if (!CommonUtils.isEmpty(fieldValue)
				&& CommonUtils.isEmpty(fieldText))
			fieldText = fieldValue;

		if (fieldBindData != 0) {
			setLists(getResources().getStringArray(fieldBindData));
		}

		typedArray.recycle();
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(mContext).inflate(R.layout.kingteller_edittext,
				this);
		title = (TextView) findViewById(R.id.title);
		edittext = (EditText) findViewById(R.id.edittext);
		textview = (TextView) findViewById(R.id.textview);
		setFieldEnabled(fieldEnabled);

		if (!CommonUtils.isEmpty(fieldTitle))
			title.setText(fieldTitle);

		if (!CommonUtils.isEmpty(fieldText))
			setFieldTextAndValue(fieldText, fieldValue);

		if (fieldType > DataTypes.Number) {
			edittext.setVisibility(View.GONE);
			textview.setVisibility(View.VISIBLE);
		} else {
			edittext.setVisibility(View.VISIBLE);
			textview.setVisibility(View.GONE);
		}

		if (!CommonUtils.isEmpty(fieldHint))
			edittext.setHint(fieldHint);

		textview.setOnClickListener(this);

	}

	public boolean isFieldEnabled() {
		return fieldEnabled;
	}

	public void setFieldEnabled(boolean fieldEnabled) {
		this.fieldEnabled = fieldEnabled;
		edittext.setEnabled(fieldEnabled);
		if (!fieldEnabled)
			edittext.setTextColor(getResources().getColor(R.color.gray));
		else
			edittext.setTextColor(getResources().getColor(R.color.black));
	}

	/**
	 * 设置value和text的值
	 * 
	 * @param text
	 * @param value
	 */
	public void setFieldTextAndValue(String text, String value) {
		if (fieldType > DataTypes.Number)
			textview.setText(text);
		else
			edittext.setText(text);
		setFieldValue(value);
		setFieldText(text);
	}

	/**
	 * 快速设置value和text的值
	 * 
	 * @param text
	 */
	public void setFieldTextAndValue(String text) {
		if (fieldType > DataTypes.Number)
			textview.setText(text);
		else
			edittext.setText(text);
		setFieldValue(text);
		setFieldText(text);
	}

	/**
	 * 获取对应的操作
	 * 
	 * @return
	 */
	public String getFieldOp() {

		return ConditionOP[fieldOp];
	}

	public void setFieldOp(int fieldOp) {
		this.fieldOp = fieldOp;
	}

	public boolean isFieldRequested() {
		return fieldRequested;
	}

	public void setFieldRequested(boolean fieldRequested) {
		this.fieldRequested = fieldRequested;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getFieldType() {
		return ConditionType[fieldType - 1];
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldTitle() {
		return title.getText().toString();
	}

	public void setFieldTitle(String fieldTitle) {
		title.setText(fieldTitle);
	}

	public boolean checkDataType() {
		switch (fieldType) {
		case DataTypes.Number:
			if (CommonUtils.isNumber(getFieldValue()))
				return true;
			else
				return false;
		default:
			return true;

		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.textview:
			switch (fieldType) {
			case DataTypes.DateTime:
				PickDialog datetimedialog = new PickDialog(getContext(),
						new DateTimePickView.PickerDateTimeListener() {
							public void onDateChanged(Date date) {
								KingTellerDateTime kdate = new KingTellerDateTime()
										.initNow();
								kdate.parseDate(date);
								setFieldTextAndValue(kdate.getDateTimeString());
							}
						}, "选择日期");
				datetimedialog.show();
				break;
			case DataTypes.Date:
				PickDialog datedialog = new PickDialog(getContext(),
						new DatePickView.PickerDateListener() {
							public void onDateChanged(Date date) {
								KingTellerDateTime kdate = new KingTellerDateTime()
										.initNow();
								kdate.parseDate(date);
								setFieldTextAndValue(kdate.getDateString());
							}
						}, "选择日期");
				datedialog.show();
				break;
			case DataTypes.Time:
				PickDialog startdialog = new PickDialog(getContext(),
						new TimePickView.PickerTimeListener() {
							public void onTimeChanged(int hour, int min) {
								setFieldTextAndValue(hour + ":" + min);
							}
						}, "选择时间");

				startdialog.show();
				break;
			case DataTypes.Select:
				SelectListAdapter adapter = new SelectListAdapter(getContext(),
						lists);
				dialog = new KTAlertDialog.Builder(getContext())
						.setTitle(getFieldTitle())
						.setItems(adapter, 0, new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> adapterView,
									View arg1, int pos, long arg3) {
								// TODO Auto-generated method stub
								CommonSelectData data = (CommonSelectData) adapterView
										.getItemAtPosition(pos);
								setFieldTextAndValue(data.getText(),
										data.getValue());
								dialog.dismiss();
								if (onChangeListener != null)
									onChangeListener.onChanged(data);

							}
						})
						.setNegativeButton("取消",
								new KTAlertDialog.OnClickListener() {

									@Override
									public void onClick(
											DialogInterface dialogInterface,
											int pos) {
										// TODO Auto-generated method stub
										dialogInterface.dismiss();
									}
								}).create();
				dialog.show();
				break;
			case DataTypes.Dialog:
				if (onDialogClickLister != null)
					onDialogClickLister.OnDialogClick();
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	public String getFieldValue() {
		if (fieldType == DataTypes.String)
			return edittext.getText().toString().trim();
		else
			return fieldValue;
	}

	private void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldText() {
		if (fieldType == DataTypes.String)
			return edittext.getText().toString().trim();
		else
			return fieldText;
	}

	private void setFieldText(String fieldText) {
		this.fieldText = fieldText;
	}

	public void setLists(List<CommonSelectData> lists) {
		this.lists = lists;
	}

	public void setLists(String[] liststr) {
		lists = new ArrayList<CommonSelectData>();
		for (int i = 0; i < liststr.length; i++) {
			CommonSelectData data = new CommonSelectData();
			data.setText(liststr[i]);
			data.setValue(liststr[i]);
			lists.add(data);
		}
	}

	public void setOnChangeListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}

	public void setOnDialogClickLister(OnDialogClickLister onDialogClickLister) {
		this.onDialogClickLister = onDialogClickLister;
	}

}
