package com.kingteller.client.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.kingteller.R;
import com.kingteller.client.activity.common.PicViewActivity;
import com.kingteller.client.bean.account.LoginBean;
import com.kingteller.client.bean.common.PicDesc;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.BitmapUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.GroupPicGridView.OnItemClickLister;
import com.kingteller.framework.http.AjaxFilesParams;
import com.kingteller.framework.utils.CommonUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 通用附件上传
 * 
 * @author 王定波
 * 
 */
public class GroupUploadFileView extends LinearLayout {

	private boolean isOpt = true;
	private final static int UPLOADFILE = 1;
	private static int nowUploadIndex = -1;
	private OnUploadLister uplodlister;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPLOADFILE:
				nowUploadIndex++;
				if (nowUploadIndex == list.size()) {
					if (uplodlister != null)
						uplodlister.OnUploadedListener(list);
					dialog.dismiss();
				} else {
					if (list.get(nowUploadIndex).getPath()
							.startsWith("http://")) {
						handler.sendEmptyMessage(UPLOADFILE);
						list.get(nowUploadIndex).setStatus(PicDesc.UPLOAD_SUCCESS);
					} else
						upLoadFile();
				}
				break;

			default:
				break;
			}
		};
	};
	private List<PicDesc> list = new ArrayList<PicDesc>();

	public void setOpt(boolean isOpt) {
		this.isOpt = isOpt;
	}

	private OnItemClickLister lister;
	private DownLoadProgressView progressView;
	private KTAlertDialog dialog;

	public GroupUploadFileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public GroupUploadFileView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		addPicItem();

		progressView = new DownLoadProgressView(getContext());
		progressView.setProgress(0);

		dialog = new KTAlertDialog.Builder(getContext()).setView(progressView)
				.setTitle("文件上传").create();
	}

	public void addAndsetItem(String path) {

		View item = LayoutInflater.from(getContext()).inflate(
				R.layout.item_add_pic, null);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 10, 0, 0);
		item.setLayoutParams(lp);

		ImageView item_add_pic = (ImageView) item
				.findViewById(R.id.item_add_pic);
		item_add_pic.setImageBitmap(BitmapUtils.decodeStream(path,
				KingTellerConfig.DefaultImgMaxWidth,
				KingTellerConfig.DefaultImgMaxHeight));
	}

	public void addPicItem() {

		final View item = LayoutInflater.from(getContext()).inflate(
				R.layout.item_add_file_desc, null);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		item.setLayoutParams(lp);

		Button item_delete = (Button) item.findViewById(R.id.item_delete);
		final ImageView item_add_pic = (ImageView) item
				.findViewById(R.id.item_add_pic);

		item_delete.setVisibility(View.GONE);

		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				int length = getChildCount();
				int pos = 0;
				for (int i = 0; i < length; i++) {
					if (getChildAt(i).equals(view)) {
						pos = i;
						break;
					}
				}

				if (lister != null) {
					Object path = item_add_pic.getTag();
					if (path != null) {
						if (CommonUtils.isPicure((String) path)) {
							Intent intent = new Intent(getContext(),
									PicViewActivity.class);
							intent.putExtra(PicViewActivity.PICPATH,
									(String) path);
							getContext().startActivity(intent);
						}
					} else {
						lister.OnItemClick(view, pos);
					}
				}
			}
		});
		item_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeView(item);
			}
		});

		addView(item);
	}

	public void setItemImageView(int pos, String path) {
		View item = getChildAt(pos);
		ImageView item_add_pic = (ImageView) item
				.findViewById(R.id.item_add_pic);
		EditText item_desc = (EditText) item.findViewById(R.id.item_desc);
		if (CommonUtils.isPicure(path))
			item_add_pic.setImageBitmap(BitmapUtils.decodeStream(path,
					KingTellerConfig.DefaultImgMaxWidth,
					KingTellerConfig.DefaultImgMaxHeight));
		else {
			File f = new File(path);
			item_add_pic.setImageResource(R.drawable.ic_file);
			item_desc.setText(f.getName());
		}
		item_add_pic.setTag(path);

		Button item_delete = (Button) item.findViewById(R.id.item_delete);

		if (isOpt) {
			item_delete.setVisibility(View.VISIBLE);
		} else
			item_delete.setVisibility(View.GONE);
	}

	/**
	 * 设置点击上传的监听
	 * @param lister
	 */
	public void setOnItemClickLister(OnItemClickLister lister) {
		this.lister = lister;
	}

	/**
	 * 上传文件返回要上传文件的数量 
	 * @return 要上传文件的数量
	 */
	public int upLoadFiles() {

		list = new ArrayList<PicDesc>();
		int length = getChildCount();
		for (int i = 0; i < length; i++) {
			View item = getChildAt(i);
			ImageView item_add_pic = (ImageView) item
					.findViewById(R.id.item_add_pic);
			EditText item_desc = (EditText) item.findViewById(R.id.item_desc);

			if (item_add_pic.getTag() != null) {
				PicDesc data = new PicDesc();
				data.setDesc(item_desc.getText().toString());
				data.setPath((String) item_add_pic.getTag());
				data.setStatus(PicDesc.UPLOAD_WAITING);
				list.add(data);
			}
		}

		if (list.size() > 0) {
			nowUploadIndex = -1;
			handler.sendEmptyMessage(UPLOADFILE);
			dialog.show();
		}

		return list.size();
	}

	/**
	 * 单独上传处理
	 */
	private void upLoadFile() {
		AjaxFilesParams params = new AjaxFilesParams();
		KTHttpClient fh = new KTHttpClient(true);
		fh.post(ConfigUtils.CreateUrl(getContext(), URLConfig.LoginUrl),
				params, new AjaxHttpCallBack<LoginBean>(getContext(), true) {

					@Override
					public void onStart() {
						progressView.setMsg("上传文件(" + (nowUploadIndex + 1) + ""
								+ list.size() + "):");
					}

					@Override
					public void onUplodinging(long count, long current) {
						// TODO Auto-generated method stub
						int progress = (int) (100 * current / count);
						progressView.setProgress(progress);
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(UPLOADFILE);
					}

					@Override
					public void onDo(LoginBean data) {
						if (uplodlister != null) {
							uplodlister
									.OnItemUploadedListener(null, true, "成功");
						}

						// 设置图片地址
						getChildAt(nowUploadIndex).findViewById(
								R.id.item_add_pic).setTag("");
						list.get(nowUploadIndex).setStatus(PicDesc.UPLOAD_SUCCESS);
						list.get(nowUploadIndex).setPath("");
					};

					@Override
					public void onError(int errorNo, String strMsg) {
						// TODO Auto-generated method stub
						if (uplodlister != null)
							uplodlister.OnItemUploadedListener(null, false,
									strMsg);
						list.get(nowUploadIndex).setStatus(PicDesc.UPLOAD_FAILED);
					}

				});
	}

	/**
	 * 获取上传的图片组
	 * @return
	 */
	public List<PicDesc> getDataList()
	{
		List<PicDesc> list = new ArrayList<PicDesc>();
		int length = getChildCount();
		for (int i = 0; i < length; i++) {
			View item = getChildAt(i);
			ImageView item_add_pic = (ImageView) item
					.findViewById(R.id.item_add_pic);
			EditText item_desc = (EditText) item.findViewById(R.id.item_desc);

			if (item_add_pic.getTag() != null) {
				PicDesc data = new PicDesc();
				data.setDesc(item_desc.getText().toString());
				data.setPath((String) item_add_pic.getTag());
				data.setStatus(PicDesc.UPLOAD_WAITING);
				list.add(data);
			}
		}
		return list;
	}
	
	public interface OnUploadLister {
		/**
		 * 全部上传的回调
		 */
		public void OnUploadedListener(List<PicDesc> list);

		/**
		 * 每一个上传回调
		 * 
		 * @param data
		 * @param isSuccess
		 * @param errMsg
		 */
		public void OnItemUploadedListener(PicDesc data, boolean isSuccess,
				String errMsg);
	}
}
