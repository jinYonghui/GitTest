package com.kingteller.client.activity.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseUploadActivity;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.common.BaseBean;
import com.kingteller.client.bean.map.ATM;
import com.kingteller.client.bean.map.ATMGroup;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.BitmapUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.view.GroupPicGridView;
import com.kingteller.client.view.GroupPicGridView.OnItemClickLister;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.http.AjaxFilesParams;
import com.kingteller.framework.utils.CommonUtils;

public class ATMUploadActivity extends BaseUploadActivity implements
		OnClickListener {
	private KingTellerDb finalDb;

	private LinearLayout ly;
	private Gson gson;
	private int selectPos;
	private GroupPicGridView selectGview;
	private CheckBox all_upload;
	private Button submit;
	private List<ATMGroup> datalist;

	private List<ATM> lists;
	private final static int SUCCESS = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				AjaxFilesParams params = (AjaxFilesParams) msg.obj;
				submitATM(params);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_atm_upload);
		initUI();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub

		finalDb = KingTellerDb.create(this);

		gson = new Gson();
		getData();

	}

	private void getData() {
		ly.removeAllViews();
		lists = finalDb.findAll(ATM.class);
		for (int i = 0; i < lists.size(); i++) {
			addItem(lists.get(i), i);
		}
	}

	@SuppressWarnings("unchecked")
	private void addItem(ATM data, int id) {
		View item = LayoutInflater.from(this).inflate(R.layout.item_atm_upload,
				null);
		item.setId(id);
		TextView item_name = (TextView) item.findViewById(R.id.item_name);
		CheckBox item_is_upload = (CheckBox) item
				.findViewById(R.id.item_is_upload);
		final GroupPicGridView item_add_pic = (GroupPicGridView) item
				.findViewById(R.id.item_add_pic);
		item_name.setText(data.getJqbh());
		item_add_pic.setOpt(false);

		try {
			item_add_pic.setImages((List<String>) gson.fromJson(
					data.getFilejson(), new TypeToken<List<String>>() {
					}.getType()));
		} catch (Exception e) {
			// TODO: handle exception
		}

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 0);
		item.setLayoutParams(lp);

		item_add_pic.setOnItemClickLister(new OnItemClickLister() {

			@Override
			public void OnItemClick(View view, int pos) {
				// TODO Auto-generated method stub
				selectPos = pos;
				selectGview = item_add_pic;
				popdialog.show();
			}
		});

		item.setTag(data.getWatermark());

		ly.addView(item);

	}

	@Override
	public void OnCallBackPhoto(String picPath) {
		// TODO Auto-generated method stub
		super.OnCallBackPhoto(picPath);
		selectGview.setItemImageView(selectPos, picPath);
		selectGview.addPicItem();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("图片上传");
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		title_right.setBackgroundResource(R.drawable.icon_add);
		ly = (LinearLayout) findViewById(R.id.ly);
		all_upload = (CheckBox) findViewById(R.id.all_upload);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);

		all_upload.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
				// TODO Auto-generated method stub
				int length = ly.getChildCount();
				for (int i = 0; i < length; i++) {
					View view = ly.getChildAt(i);
					CheckBox item_is_upload = (CheckBox) view
							.findViewById(R.id.item_is_upload);
					item_is_upload.setChecked(ischecked);

				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CommonCodeConfig.REQUEST_ADDATM:
			getData();
			break;

		default:
			break;
		}
	}

	/**
	 * 处理图片
	 */
	public void Chulicanshu() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KingTellerProgress.showProgress(ATMUploadActivity.this, "正在上传...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				AjaxFilesParams params = new AjaxFilesParams();

				params.put("userid", User.getInfo(ATMUploadActivity.this)
						.getUserId());
				for (int i = 0; i < datalist.size(); i++) {

					List<String> filelist = new ArrayList<String>();
					for (int j = 0; j < datalist.get(i).getPiclist().size(); j++) {
						File file = BitmapUtils.createBitmap(datalist.get(i)
								.getPiclist().get(j),
								KingTellerConfig.DefaultImgMaxWidth,
								KingTellerConfig.DefaultImgMaxHeight, datalist
										.get(i).getWatermark());
						params.put("files", file);
						filelist.add(file.getName());
					}
					datalist.get(i).setPiclist(filelist);
					params.put("name" + i, datalist.get(i).getJson());

				}

				Message msg = new Message();
				msg.what = SUCCESS;
				msg.obj = params;
				handler.sendMessage(msg);

			}
		}).start();

	}

	public void submitATM(AjaxFilesParams params) {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.UploadPicUrl), params,
				new AjaxHttpCallBack<BaseBean>(this, true) {

					@Override
					public void onFinish() {
						KingTellerProgress.closeProgress();

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						KingTellerProgress.showProgress(ATMUploadActivity.this,
								"正在上传...");
					}

					@Override
					public void onSuccess(String response) {
						// TODO Auto-generated method stub
						response = KingTellerUtils
								.getBaseJsonFromString(response);
						super.onSuccess(response);
					}

					@Override
					public void onDo(BaseBean data) {
						// TODO Auto-generated method stub
						if (data.getMsg().trim().equals("上传成功.")) {
							toastMsg("上传成功");
							for (int i = 0; i < datalist.size(); i++) {
								finalDb.deleteById(ATM.class,
										lists.get(datalist.get(i).getId())
												.getId());
								ly.removeView(ly.findViewById(datalist.get(i)
										.getId()));

							}

						} else
							toastMsg("上传失败");
					}

				});
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.button_right:
			startActivityForResult(new Intent(this, ATMAddActivity.class),
					CommonCodeConfig.REQUEST_ADDATM);
			break;
		case R.id.button_left:
			finish();
			break;
		case R.id.submit:
			datalist = new ArrayList<ATMGroup>();
			int length = ly.getChildCount();
			for (int i = 0; i < length; i++) {
				View sview = ly.getChildAt(i);
				CheckBox item_is_upload = (CheckBox) sview
						.findViewById(R.id.item_is_upload);
				TextView item_name = (TextView) sview
						.findViewById(R.id.item_name);

				GroupPicGridView item_add_pic = (GroupPicGridView) sview
						.findViewById(R.id.item_add_pic);

				ATMGroup data = new ATMGroup();
				data.setJqbh(item_name.getText().toString());
				data.setPiclist(item_add_pic.getImages());
				data.setId(sview.getId());
				data.setWatermark((String) sview.getTag());

				if (item_is_upload.isChecked())
					datalist.add(data);
			}
			if (datalist.size() == 0)
				toastMsg("请先选择");
			else {
				Chulicanshu();
			}
			break;
		default:
			break;
		}

	}
}
