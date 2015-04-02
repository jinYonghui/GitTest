package com.kingteller.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kingteller.R;
import com.kingteller.client.adapter.SelectListAdapter;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.CommonSelectGZBJ;
import com.kingteller.client.bean.common.KingTellerDateTime;
import com.kingteller.client.view.datatype.DataTypes;
import com.kingteller.framework.utils.CommonUtils;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 通用的EditTextView类
 * 
 * @author 王定波
 * 
 */
public class KingTellerEditText extends LinearLayout implements
		android.view.View.OnClickListener {
	private EditText edittext;
	private Context mContext;

	private TextView title;
	private TextView textview;
	private java.lang.String fieldName;
	private java.lang.String fieldValue;
	private java.lang.String fieldTitle;
	private boolean fieldRequested;
	private int fieldType;
	private String fieldHint;
	private String fieldText;
	private int fieldBindData;
	private KTAlertDialog dialog;
	private List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
	private boolean fieldEnabled;
	private int fieldLines;
	private OnChangeListener onChangeListener;
	private OnDialogClickLister onDialogClickLister;

	public KingTellerEditText(Context context) {
		super(context);
		mContext = context;
		initView(context);
	}

	public KingTellerEditText(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
				R.styleable.kingteller_edittext);
		setFieldName(typedArray
				.getString(R.styleable.kingteller_edittext_fieldName));
		fieldValue = typedArray
				.getString(R.styleable.kingteller_edittext_fieldValue);
		fieldTitle = typedArray
				.getString(R.styleable.kingteller_edittext_fieldTitle);
		fieldRequested = typedArray.getBoolean(
				R.styleable.kingteller_edittext_fieldRequested, false);
		fieldType = typedArray.getInt(
				R.styleable.kingteller_edittext_fieldType, DataTypes.String);
		fieldHint = typedArray
				.getString(R.styleable.kingteller_edittext_fieldHint);
		fieldText = typedArray
				.getString(R.styleable.kingteller_edittext_fieldText);
		fieldBindData = typedArray.getResourceId(
				R.styleable.kingteller_edittext_fieldBindData, 0);
		fieldEnabled = typedArray.getBoolean(
				R.styleable.kingteller_edittext_fieldEnabled, true);
		fieldLines = typedArray.getInt(
				R.styleable.kingteller_edittext_fieldLines, 1);

		if (!CommonUtils.isEmpty(fieldText) && CommonUtils.isEmpty(fieldValue))
			fieldValue = fieldText;
		else if (!CommonUtils.isEmpty(fieldValue)
				&& CommonUtils.isEmpty(fieldText))
			fieldText = fieldValue;

		if (CommonUtils.isEmpty(fieldHint)) {
			fieldHint = (fieldType == DataTypes.Select
					|| fieldType == DataTypes.Dialog
					|| fieldType == DataTypes.Date
					|| fieldType == DataTypes.Time || fieldType == DataTypes.DateTime) ? "请选择"
					: "请输入";
			if (!fieldEnabled)
				fieldHint = "";
		}

		if (fieldBindData != 0) {
			setLists(getResources().getStringArray(fieldBindData));
		}

		typedArray.recycle();
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater.from(mContext).inflate(R.layout.kingteller_edittext,
				this);
		edittext = (EditText) findViewById(R.id.edittext);
		title = (TextView) findViewById(R.id.title);
		textview = (TextView) findViewById(R.id.textview);

		edittext.setText(fieldValue);
		setFieldEnabled(fieldEnabled);
		setFieldLines(fieldLines);

		if (fieldType > DataTypes.Number) {
			edittext.setVisibility(View.GONE);
			textview.setVisibility(View.VISIBLE);
		} else {
			edittext.setVisibility(View.VISIBLE);
			textview.setVisibility(View.GONE);
		}

		if (!CommonUtils.isEmpty(fieldHint)) {
			edittext.setHint(fieldHint);
			textview.setHint(fieldHint);
		}

		textview.setOnClickListener(this);

		title.setText(fieldTitle);

		if (fieldType == DataTypes.Number) {
			edittext.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}

		if (fieldRequested)
			title.setTextColor(getResources().getColor(R.color.orange));
	}

	/**
	 * 获取标题
	 * @return
	 */
	public String getFieldTitle() {
		return title.getText().toString();
	}

	public void setTextSize(float size){
		edittext.setTextSize(size);
	}
	
	public void setFouces(boolean bool){
		edittext.setFocusable(bool);
		if (!CommonUtils.isEmpty(fieldHint)) {
			edittext.setHint("");
			textview.setHint("");
		}
	}
	
	/**
	 * 设置输入的类型
	 * @param type
	 */
	public void setInputType(int type) {
		edittext.setInputType(type);
	}

	public void setMaxLength(int paramInt) {
		EditText localEditText = edittext;
		InputFilter[] arrayOfInputFilter = new InputFilter[1];
		arrayOfInputFilter[0] = new InputFilter.LengthFilter(paramInt);
		localEditText.setFilters(arrayOfInputFilter);
	}

	public void setFieldTitle(String str) {
		title.setText(str);
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

		String oldvalue = getFieldValue();
		setFieldValue(value);
		setFieldText(text);

		if ((oldvalue != null && value != null && !oldvalue.equals(value))
				|| (oldvalue == null && value != null)
				|| (oldvalue != null && value == null)) {
			CommonSelectData data = new CommonSelectData();
			data.setText(text);
			data.setValue(value);
			if (onChangeListener != null)
				onChangeListener.onChanged(data);
		}

	}

	public void setTextAndValue(CommonSelectData data){
		setTextAndValue(data.getText(),data.getValue());
	}
	
	public void setTextAndValue(String text, String value){
		textview.setText(value);
		edittext.setText(text);
		String oldvalue = getFieldValue();
		setFieldValue(value);
		setFieldText(text);

		if ((oldvalue != null && value != null && !oldvalue.equals(value))
				|| (oldvalue == null && value != null)
				|| (oldvalue != null && value == null)) {
			CommonSelectData data = new CommonSelectData();
			data.setText(text);
			data.setValue(value);
			if (onChangeListener != null)
				onChangeListener.onChanged(data);
		}

	}
	
	/**
	 * 设置值
	 * 
	 * @param data
	 */
	public void setFieldTextAndValue(CommonSelectData data) {
		setFieldTextAndValue(data.getText(), data.getValue());
	}
	
	public void setFieldTextAndValue(CommonSelectGZBJ data) {
		// TODO Auto-generated method stub
		setFieldTextAndValue(data.getText(), data.getValue());
	}

	/**
	 * 快速设置value和text的值
	 * 
	 * @param text
	 */
	public void setFieldTextAndValue(String text) {
		setFieldTextAndValue(text, text);
	}

	/**
	 * 根据value设置value和text的值
	 * 
	 * @param value
	 */
	public void setFieldTextAndValueFromValue(String value) {
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).getValue().equals(value)) {
				setFieldTextAndValue(lists.get(i).getText(), lists.get(i)
						.getValue());
				break;
			}
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.textview:
			if (!isFieldEnabled())
				break;
			switch (fieldType) {
			case DataTypes.DateTime:
				PickDialog datetimedialog = new PickDialog(getContext(),
						new DateTimePickView.PickerDateTimeListener() {
							public void onDateChanged(Date date) {
								KingTellerDateTime kdate = new KingTellerDateTime()
										.initNow();
								kdate.parseDate(date);

								setTextAndValue(kdate.getDateTimeString(),kdate.getDateTimeString());

							}
						}, "选择日期");
				datetimedialog.show();
				break;
			case DataTypes.DateTimePD:
				PickDialog datetimepddialog = new PickDialog(getContext(),
						new DateTimePickView.PickerDateTimeListener() {
							public void onDateChanged(Date date) {
								KingTellerDateTime kdate = new KingTellerDateTime()
										.initNowPD();
								kdate.parseDate(date);

								setFieldTextAndValue(kdate.getDateTimeString());

							}
						}, "选择日期","DataTypes.DateTimePD");
				datetimepddialog.show();
				break;
			case DataTypes.Date:
				PickDialog datedialog = new PickDialog(getContext(),
						new DatePickView.PickerDateListener() {
							public void onDateChanged(Date date) {
								KingTellerDateTime kdate = new KingTellerDateTime()
										.initNow();
								kdate.parseDate(date);
								setFieldTextAndValue(kdate.getDateTimeString());
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

	/**
	 * 获取控件的值
	 * @return
	 */
	public String getFieldValue() {
		if ((fieldType == DataTypes.String || fieldType == DataTypes.Number)
				&& fieldEnabled)
			return edittext.getText().toString().trim();
		else
			return this.fieldValue;

	}
	
	public String getFieldStringValue(){
		return this.fieldValue;
	}

	private void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * 获取控件显示的值
	 * @return
	 */
	public String getFieldText() {
		if (fieldType == DataTypes.String)
			return edittext.getText().toString().trim();
		else
			return fieldText;
	}

	private void setFieldText(String fieldText) {
		this.fieldText = fieldText;
	}

	/**
	 * 当类型为Select的时候设置的值
	 * @param lists
	 */
	public void setLists(List<CommonSelectData> lists) {
		this.lists = lists;
	}

	/**
	 * 当类型为Select的时候设置的值
	 * @param liststr
	 */
	public void setLists(String[] liststr) {
		lists = new ArrayList<CommonSelectData>();
		for (int i = 0; i < liststr.length; i++) {
			CommonSelectData data = new CommonSelectData();
			data.setText(liststr[i]);
			data.setValue(liststr[i]);
			lists.add(data);
		}
	}

	public boolean isFieldEnabled() {
		return fieldEnabled;
	}

	/**
	 * 设置空间是否有效
	 * @param fieldEnabled
	 */
	public void setFieldEnabled(boolean fieldEnabled) {
		this.fieldEnabled = fieldEnabled;
		edittext.setEnabled(fieldEnabled);
		if (!fieldEnabled) {
			edittext.setTextColor(getResources().getColor(R.color.gray));
			title.setTextColor(getResources().getColor(R.color.gray));
			textview.setTextColor(getResources().getColor(R.color.gray));
			edittext.setHint("");
			textview.setHint("");
		} else {
			edittext.setTextColor(getResources().getColor(R.color.black));
			title.setTextColor(getResources().getColor(R.color.black));
			textview.setTextColor(getResources().getColor(R.color.black));
		}
	}
	
	public void setFieldMachineEnabled(boolean fieldEnabled){
		this.fieldEnabled = fieldEnabled;
		edittext.setEnabled(fieldEnabled);
		if (!fieldEnabled) {
			edittext.setTextColor(getResources().getColor(R.color.gray));
			title.setTextColor(getResources().getColor(R.color.gray));
			textview.setTextColor(getResources().getColor(R.color.gray));
			edittext.setHint("");
			textview.setHint("");
		} else {
			edittext.setTextColor(getResources().getColor(R.color.orange));
			title.setTextColor(getResources().getColor(R.color.orange));
			textview.setTextColor(getResources().getColor(R.color.black));
		}
	}

	public void setFieldCheckoutEnabled(boolean fieldEnabled){
		this.fieldEnabled = fieldEnabled;
		edittext.setEnabled(fieldEnabled);
		if (!fieldEnabled) {
			edittext.setTextColor(getResources().getColor(R.color.black));
			title.setTextColor(getResources().getColor(R.color.black));
			textview.setTextColor(getResources().getColor(R.color.black));
			edittext.setHint("");
			textview.setHint("");
		}
	}
	
	public int getFieldLines() {
		return fieldLines;
	}

	/**
	 * 设置控件的行数
	 * @param fieldLines
	 */
	public void setFieldLines(int fieldLines) {
		this.fieldLines = fieldLines;
		if (fieldLines > 1) {
			edittext.setSingleLine(false);
			edittext.setLines(fieldLines);
			edittext.setGravity(Gravity.TOP);
			title.setGravity(Gravity.TOP);

			textview.setSingleLine(false);
			textview.setLines(fieldLines);
			textview.setGravity(Gravity.TOP);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, getResources()
							.getDimensionPixelSize(R.dimen.common_line_height)
							* fieldLines);
			edittext.setLayoutParams(lp);
			textview.setLayoutParams(lp);

		} else {
			edittext.setSingleLine(true);
			edittext.setGravity(Gravity.CENTER_VERTICAL);
			textview.setGravity(Gravity.CENTER_VERTICAL);
			textview.setSingleLine(true);
			title.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);
			edittext.setLayoutParams(lp);
			textview.setLayoutParams(lp);
		}
	}

	public boolean isFieldRequested() {
		return fieldRequested;
	}

	public void setFieldRequested(boolean fieldRequested) {
		this.fieldRequested = fieldRequested;
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

	public java.lang.String getFieldName() {
		return fieldName;
	}

	public void setFieldName(java.lang.String fieldName) {
		this.fieldName = fieldName;
	}

	public void setOnChangeListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}

	public void setOnDialogClickLister(OnDialogClickLister onDialogClickLister) {
		this.onDialogClickLister = onDialogClickLister;
	}

	/**
	 * 设置数值改变的回调接口
	 * @author 王定波
	 *
	 */
	public interface OnChangeListener {
		public abstract void onChanged(CommonSelectData data);
	}

	/**
	 * 设置类型为Dialog时的点击的接口
	 * @author 王定波
	 *
	 */
	public interface OnDialogClickLister {
		public abstract void OnDialogClick();
	}
	public interface onDialogClickListener{
		public abstract void OnDialogClick(CommonSelectData data);
	}
	
}
