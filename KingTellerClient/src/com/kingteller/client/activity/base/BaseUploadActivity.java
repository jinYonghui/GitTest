package com.kingteller.client.activity.base;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;

import com.kingteller.R;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.view.PopDialog;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.modules.filechooser.FileChooserActivity;


/**
 * 文件上传基础Activity
 * @author 王定波
 *
 */
public class BaseUploadActivity extends BaseActivity implements OnClickListener {
	protected PopDialog popdialog;
	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	private Uri photoUri;
	//是否可以上传文件
	private boolean selectAllFile = false;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		popdialog = new PopDialog(this, R.layout.popmenu_pic);

		popdialog.getView().findViewById(R.id.button_my_info_cancle)
				.setOnClickListener(this);
		popdialog.getView().findViewById(R.id.button_my_info_gallery)
				.setOnClickListener(this);
		popdialog.getView().findViewById(R.id.button_my_info_photo)
				.setOnClickListener(this);
		popdialog.getView().findViewById(R.id.button_file)
				.setOnClickListener(this);
		setSelectAllFile(selectAllFile);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_my_info_cancle:
			popdialog.dismiss();
			break;
		case R.id.button_my_info_gallery:
			pickPhoto();
			popdialog.dismiss();
			break;
		case R.id.button_my_info_photo:
			takePhoto();
			popdialog.dismiss();
			break;
		case R.id.button_file:
			pickFile();
			popdialog.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			photoUri = this.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			/** ----------------- */
			startActivityForResult(intent,
					CommonCodeConfig.SELECT_PIC_BY_TACK_PHOTO);
		} else {
			toastMsg("内存卡不存在");
		}
	}

	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.setType("image/*");
		// intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory("android.intent.category.OPENABLE");
		startActivityForResult(intent,
				CommonCodeConfig.SELECT_PIC_BY_PICK_PHOTO);

	}

	/***
	 * 文件选择器
	 */
	private void pickFile() {
		Intent intent = new Intent(this, FileChooserActivity.class);
		startActivityForResult(intent, CommonCodeConfig.SELECT_FILE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CommonCodeConfig.SELECT_PIC_BY_TACK_PHOTO:
		case CommonCodeConfig.SELECT_PIC_BY_PICK_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				doPhoto(requestCode, data);
			} else {
				toastMsg("未选择");
			}
			break;
		case CommonCodeConfig.SELECT_FILE:
			if (resultCode == Activity.RESULT_OK) {
				doFile(data);
			} else {
				toastMsg("未选择");
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 选择文件获取路径
	 * 
	 * @param data
	 */
	private void doFile(Intent data) {
		if (data == null) {
			toastMsg("选择文件出错");
			return;
		}
		Uri fileUri = data.getData();
		if (fileUri == null) {
			toastMsg("选择文件出错");
			return;
		}

		String path = fileUri.getPath();

		if (path != null) {
			OnCallBackPhoto(path);

		} else {
			toastMsg("选择图片文件不正确");
		}

	}

	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		String picPath = null;
		if (requestCode == CommonCodeConfig.SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				toastMsg("选择图片文件出错");
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				toastMsg("选择图片文件出错");
				return;
			}
		}

		if (photoUri.toString().startsWith("content://")) {
			String[] pojo = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
			if (cursor != null) {
				int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
				cursor.moveToFirst();
				picPath = cursor.getString(columnIndex);

			}
		} else {
			picPath = photoUri.getPath();
		}

		if (picPath != null && CommonUtils.isPicure(picPath)) {
			OnCallBackPhoto(picPath);

		} else {
			picPath = null;
			toastMsg("选择图片文件不正确");
		}
	}

	public void OnCallBackPhoto(String picPath) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (popdialog != null && popdialog.isShowing())
			popdialog.dismiss();
	}

	public boolean isSelectAllFile() {
		return selectAllFile;
	}

	/**
	 * 设置是否显示所有文件上传类型
	 * @param selectAllFile
	 */
	public void setSelectAllFile(boolean selectAllFile) {
		this.selectAllFile = selectAllFile;
		popdialog.getView().findViewById(R.id.button_file)
				.setVisibility(selectAllFile ? View.VISIBLE : View.GONE);
	}

}