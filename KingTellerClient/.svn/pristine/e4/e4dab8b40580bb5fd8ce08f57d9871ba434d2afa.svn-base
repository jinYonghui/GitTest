package com.kingteller.client.activity.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.base.BaseUploadActivity;
import com.kingteller.client.activity.common.SelectAddressActivity;
import com.kingteller.client.activity.common.SelectMapAddressActivity;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.common.BaseBean;
import com.kingteller.client.bean.map.ATM;
import com.kingteller.client.bean.map.ATMGroup;
import com.kingteller.client.bean.map.AddressBean;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.BitmapUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.view.ATMGroupView;
import com.kingteller.client.view.ATMGroupView.OnATMGroupLister;
import com.kingteller.client.view.GroupPicGridView;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.http.AjaxFilesParams;
import com.kingteller.framework.utils.CommonUtils;

public class ATMAddActivity extends BaseUploadActivity {
	private LinearLayout layout_location;
	private CheckBox is_upload_img;
	private TextView atm_location;
	private AddressBean address;
	private Button btn_atm_add;
	private LinearLayout layout_atm_list;
	private int selectPos;
	private GroupPicGridView selectGview;
	private List<ATMGroup> datalist;
	private List<ATM> dataatmlist;
	private KingTellerDb finalDb;
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
		setContentView(R.layout.layout_atm_add);
		initUI();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		address = KingTellerApplication.getApplication().getCurAddress();
		atm_location.setText(address.getAddress());
		finalDb = KingTellerDb.create(this);
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("机器定位");
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		title_right.setText("提交");

		layout_location = (LinearLayout) findViewById(R.id.layout_location);
		is_upload_img = (CheckBox) findViewById(R.id.is_upload_img);
		atm_location = (TextView) findViewById(R.id.atm_location);
		btn_atm_add = (Button) findViewById(R.id.btn_atm_add);
		layout_atm_list = (LinearLayout) findViewById(R.id.layout_atm_list);

		layout_location.setOnClickListener(this);
		btn_atm_add.setOnClickListener(this);
		addATMGroup(true);

	}

	/**
	 * 添加ATM组
	 */
	private void addATMGroup(boolean isfirst) {
		final ATMGroupView view = new ATMGroupView(this);
		if (isfirst)
			view.setDelVisibility(View.GONE);

		view.setOnATMGroupLister(new OnATMGroupLister() {

			@Override
			public void OnItemClick(ATMGroupView aview, View view, int pos) {
				// TODO Auto-generated method stub
				selectPos = pos;
				selectGview = aview.getLayoutAddPic();
				popdialog.show();

			}

			@Override
			public void OnDelClick(ATMGroupView aview) {
				// TODO Auto-generated method stub
				layout_atm_list.removeView(aview);

			}
		});

		layout_atm_list.addView(view);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.button_right:
			checkData();
			break;
		case R.id.button_left:
			finish();
			break;

		case R.id.layout_location:
			startActivityForResult(new Intent(this,
					SelectMapAddressActivity.class),
					CommonCodeConfig.REQUEST_GETMAPADDRESS);

			break;
		case R.id.btn_atm_add:
			addATMGroup(false);
			break;
		default:
			break;
		}
	}

	public void checkData() {
		datalist = new ArrayList<ATMGroup>();
		int length = layout_atm_list.getChildCount();
		String picdesc = "";
		for (int i = 0; i < length; i++) {
			ATMGroupView view = (ATMGroupView) layout_atm_list.getChildAt(i);
			ATMGroup data = view.getData();
			// -1未输入，-2需要重新验证；0没有图片；1已经有图片

			switch (data.getStatus()) {
			case -1:
				toastMsg("机器编码未输入");
				return;
			case -2:
				toastMsg(data.getJqbh() + "没有检索,请先检索后提交");
				return;
			case 1:
				picdesc += data.getJqbh() + ",";
				break;
			default:
				break;
			}

			if (data.getPiclist().size() == 0) {
				toastMsg("必须选择图片");
				return;
			}
			datalist.add(data);
		}

		if (CommonUtils.isEmpty(picdesc))
			picdesc = "是否确定提交?";
		else
			picdesc += "已有图片，是否继续上传?";

		new KTAlertDialog.Builder(this).setTitle("提示").setMessage(picdesc)
				.setPositiveButton("确定", new KTAlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int pos) {
						Chulicanshu();
						dialogInterface.dismiss();
					}
				}).setNegativeButton("取消", new KTAlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialogInterface,
							int paramAnonymousInt) {
						dialogInterface.dismiss();
					}
				}).create().show();

	}

	/**
	 * 上传
	 * 
	 * @param params
	 */
	private void submitATM(AjaxFilesParams params) {
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
							for (int k = 0; k < dataatmlist.size(); k++) {
								finalDb.save(dataatmlist.get(k));
							}
							toastMsg("上传成功");
							finish();
						} else
							toastMsg("上传失败");
					}

				});
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

		KingTellerProgress.showProgress(ATMAddActivity.this, "正在上传...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Gson gson = new Gson();

				dataatmlist = new ArrayList<ATM>();
				AjaxFilesParams params = new AjaxFilesParams();
				params.put("longitude", String.valueOf(address.getLng()));
				params.put("latitude", String.valueOf(address.getLat()));
				params.put("upimage", is_upload_img.isChecked() ? "1" : "0");
				params.put("userid", User.getInfo(ATMAddActivity.this)
						.getUserId());
				for (int i = 0; i < datalist.size(); i++) {
					StringBuffer strs = new StringBuffer("机器编号:"
							+ datalist.get(i).getJqbh() + "  " + "上传人:"
							+ User.getInfo(ATMAddActivity.this).getName());
					strs.append("\n经度:" + address.getLng() + "     " + "纬度:"
							+ address.getLat());
					strs.append("\n地址:" + address.getAddress());

					if (is_upload_img.isChecked()) {
						List<String> filelist = new ArrayList<String>();
						for (int j = 0; j < datalist.get(i).getPiclist().size(); j++) {
							File file = BitmapUtils.createBitmap(datalist
									.get(i).getPiclist().get(j),
									KingTellerConfig.DefaultImgMaxWidth,
									KingTellerConfig.DefaultImgMaxHeight,
									strs.toString());

							params.put("files", file);
							filelist.add(file.getName());
						}
						datalist.get(i).setPiclist(filelist);
					} else {
						ATM data = new ATM();
						data.setJqbh(datalist.get(i).getJqbh());
						data.setNamejson(datalist.get(i).getJson());
						data.setFilejson(gson.toJson(datalist.get(i)
								.getPiclist()));
						data.setWatermark(strs.toString());
						dataatmlist.add(data);

					}
					params.put("viewjson" + i, datalist.get(i).getJson());
				}

				Message msg = new Message();
				msg.what = SUCCESS;
				msg.obj = params;
				handler.sendMessage(msg);

			}
		}).start();

	}

	@Override
	public void OnCallBackPhoto(String picPath) {
		// TODO Auto-generated method stub
		super.OnCallBackPhoto(picPath);
		selectGview.setItemImageView(selectPos, picPath);
		selectGview.addPicItem();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CommonCodeConfig.REQUEST_GETMAPADDRESS:
			if (RESULT_OK == resultCode) {
				address = (AddressBean) data
						.getSerializableExtra(SelectAddressActivity.ADDRESS);
				atm_location.setText(address.getAddress());
			}

			break;

		default:
			break;
		}
	}

}
