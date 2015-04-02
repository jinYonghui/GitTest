package com.kingteller.client.view;

import com.kingteller.R;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.map.ATMGroup;
import com.kingteller.client.bean.map.CheckMacineBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.view.GroupPicGridView.OnItemClickLister;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 添加ATP的视图
 * @author 王定波
 *
 */
public class ATMGroupView extends LinearLayout {

	private Button btn_delete;
	private GroupPicGridView layout_add_pic;
	private ImageView item_search;
	private RadioButton atmcodeID;
	private RadioButton storterID;
	private EditText atmcodeET;
	private TextView sn_str;
	private OnATMGroupLister onATMGroupLister;
	private TextView jqbhText;
	private int status = -1;// -1未输入，-2需要重新验证；0没有图片；1已经有图片

	public ATMGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public ATMGroupView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public ATMGroup getData() {
		ATMGroup data = new ATMGroup();
		if (atmcodeID.isChecked())
			data.setJqbh("S/N" + atmcodeET.getText().toString());
		else
			data.setJqbh(atmcodeET.getText().toString());
		data.setIsatm(atmcodeID.isChecked());
		data.setPiclist(layout_add_pic.getImages());
		data.setStatus(status);
		return data;
	}

	private void initView() {
		// TODO Auto-generated method stub
		LayoutInflater.from(getContext()).inflate(R.layout.item_add_atm, this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 0);
		setLayoutParams(lp);

		btn_delete = (Button) findViewById(R.id.btn_delete);
		layout_add_pic = (GroupPicGridView) findViewById(R.id.layout_add_pic);
		item_search = (ImageView) findViewById(R.id.item_search);
		atmcodeID = (RadioButton) findViewById(R.id.atmcodeID);
		storterID = (RadioButton) findViewById(R.id.storterID);
		atmcodeET = (EditText) findViewById(R.id.atmcodeET);
		sn_str = (TextView) findViewById(R.id.sn_str);
		jqbhText = (TextView) findViewById(R.id.jqbhText);
		atmcodeID.setChecked(true);

		atmcodeET.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (s.length() > 0) {
					status = -2;
				} else {
					status = -1;
				}
			}

		});

		atmcodeID.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
				// TODO Auto-generated method stub
				if (ischecked) {
					sn_str.setVisibility(View.VISIBLE);
				} else
					sn_str.setVisibility(View.GONE);

			}
		});

		storterID.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
				// TODO Auto-generated method stub
				if (ischecked) {
					sn_str.setVisibility(View.GONE);
				} else
					sn_str.setVisibility(View.VISIBLE);
			}
		});

		item_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				CheckMacine();
			}
		});

		layout_add_pic.setOnItemClickLister(new OnItemClickLister() {

			@Override
			public void OnItemClick(View view, int pos) {
				// TODO Auto-generated method stub
				if (onATMGroupLister != null)
					onATMGroupLister.OnItemClick(ATMGroupView.this, view, pos);
			}
		});

		btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (onATMGroupLister != null)
					onATMGroupLister.OnDelClick(ATMGroupView.this);
			}
		});

	}

	public void setDelVisibility(int status) {
		btn_delete.setVisibility(status);
	}

	private void CheckMacine() {

		// User user = User.getInfo(this);
		String maccode = atmcodeET.getText().toString().trim();
		if (CommonUtils.isEmpty(maccode)) {
			toastMsg("请重新输入机器编号");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();

		if (atmcodeID.isChecked())
			maccode = "S/N" + maccode;

		params.put("jqbh", maccode);
		params.put("userid", User.getInfo(getContext()).getUserId());

		fh.post(ConfigUtils.CreateUrl(getContext(), URLConfig.CheckMacineUrl),
				params, new AjaxHttpCallBack<CheckMacineBean>(getContext(),
						true) {

					@Override
					public void onFinish() {
						KingTellerProgress.closeProgress();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						KingTellerProgress
								.showProgress(getContext(), "正在检测...");
					}

					@Override
					public void onDo(CheckMacineBean data) {
						if (data.getPicFlag().trim().equals("lose")) {
							toastMsg("请重新输入机器编号");
							status = -2;
							return;
						} else if (data.getPicFlag().trim().equals("无图片")) {
							status = 0;
						} else
							status = 1;
						jqbhText.setText(" 图片状态:" + data.getPicFlag() + "\n "
								+ "网点地址:" + data.getWddz());
					};

				});
	}

	public void setOnATMGroupLister(OnATMGroupLister onATMGroupLister) {
		this.onATMGroupLister = onATMGroupLister;
	}

	public GroupPicGridView getLayoutAddPic() {
		return layout_add_pic;
	}

	public void setLayoutAddPic(GroupPicGridView layout_add_pic) {
		this.layout_add_pic = layout_add_pic;
	}

	public void toastMsg(String str) {
		Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
	}

	public interface OnATMGroupLister {
		public void OnItemClick(ATMGroupView aview, View view, int pos);

		public void OnDelClick(ATMGroupView aview);

	}
}
