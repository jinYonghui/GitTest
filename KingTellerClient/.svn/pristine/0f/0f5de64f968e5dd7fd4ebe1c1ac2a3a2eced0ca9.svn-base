package com.kingteller.client.utils;

import java.io.File;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.Toast;

import com.kingteller.client.activity.account.MainActivity;
import com.kingteller.client.bean.more.AppUpdateBean;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.view.DownLoadProgressView;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.framework.http.AjaxCallBack;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.framework.utils.Logger;
import com.kingteller.framework.utils.MD5Utils;

/**
 * 应用升级处理
 * @author 王定波
 *
 */
public class UpdateUtils {

	public static String getCurrentVersionName(Context context) {
		// TODO Auto-generated method stub

		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_PERMISSIONS).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionName;
	}

	/**
	 * 比较版本号
	 * @param context
	 * @param version
	 * @return
	 */
	public static boolean CompareVerSion(Context context, String version) {
		String nowVersion = getCurrentVersionName(context);
		String[] nowS = nowVersion.split("\\.");
		String[] gS = version.split("\\.");
		for (int i = 0; i < nowS.length; i++) {
			if (Integer.parseInt(gS[i]) > Integer.parseInt(nowS[i])) {
				return true;
			} else if (Integer.parseInt(gS[i]) < Integer.parseInt(nowS[i])) {
				return false;
			}
		}

		return false;
	}

	//获取版本号(内部识别号) 
    private static int getVersionCode(Context context) {  
        try {  
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);  
            return pi.versionCode;  
        } catch (NameNotFoundException e) {  
            // TODO Auto-generated catch block   
            return 0;  
        }  
    }  
	/**
	 * 检查升级
	 * @param context
	 * @param isshow 是否提示
	 */
	public static void CheckUpdate(final Context context, final boolean isshow) {
		if (!CommonUtils.isNetAvaliable(context)) {
			Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
			return;
		}

		AjaxParams params = new AjaxParams();
		Logger.e("version_code", String.valueOf(getVersionCode(context)));
		params.put("version_code", String.valueOf(getVersionCode(context)));

		KTHttpClient fh = new KTHttpClient(true);
		fh.get(ConfigUtils.CreateNoVersionUrl(context, URLConfig.APPUpdateUrl), params,
				new AjaxHttpCallBack<AppUpdateBean>(context, false) {
					@Override
					public void onDo(final AppUpdateBean data) {
						if (data.isSuccessFul()) {
							KTAlertDialog.Builder builder = new KTAlertDialog.Builder(
									getContext());
							builder.setTitle("新版本" + data.getTitle());
							builder.setMessage(data.getContent());
							builder.setPositiveButton("现在更新",
									new KTAlertDialog.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											down(context, ConfigUtils
													.CreateUrl(context,
															data.getUrl()));

										}
									});
							// 稍后更新
							builder.setNegativeButton("暂不更新",
									new KTAlertDialog.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							KTAlertDialog noticeDialog = builder.create();
							noticeDialog.setCanceledOnTouchOutside(false);
							noticeDialog.show();

							ConfigUtils.setConfigMeta(ConfigUtils.KEY_APP_NEW,
									true);
							KingTellerUtils.MainUpdateStatus(context,
									MainActivity.POSITION_MOREFRAGMENT, true,
									true);

						} else {
							ConfigUtils.setConfigMeta(ConfigUtils.KEY_APP_NEW,
									false);
							KingTellerUtils.MainUpdateStatus(context,
									MainActivity.POSITION_MOREFRAGMENT, true,
									false);
							if (isshow)
								Toast.makeText(context, "已经是最新版本",
										Toast.LENGTH_SHORT).show();
						}

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						if (isshow)
							KingTellerProgress.closeProgress();
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						if (isshow)
							KingTellerProgress.showProgress(context,
									"正在检查版本...");
					}

				});
	}

	private static void down(final Context context, String url) {
		String filename = KingTellerConfig.CACHE_PATH.SD_DOWNLOAD + "/"
				+ MD5Utils.toMD5(url) + ".apk";
		File file = new File(filename);
		if (file.exists()) {
			//installApk(context, file);
			file.delete();
			//return;
		}

		final DownLoadProgressView view = new DownLoadProgressView(context);
		view.setProgress(0);

		final KTAlertDialog dialog = new KTAlertDialog.Builder(context).setView(view).setTitle("正在下载")
				.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		KTHttpClient fh = new KTHttpClient(false);
		fh.download(url, filename, new AjaxCallBack<File>() {
			@Override
			public void onLoading(long count, long current) {
				// TODO Auto-generated method stub
				int progress=(int)(100*current/count);
				view.setProgress(progress);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				if(dialog.isShowing())
				dialog.dismiss();
			}

			@Override
			public void onSuccess(File t) {
				// TODO Auto-generated method stub
				if(dialog.isShowing())
				dialog.dismiss();
				installApk(context, t);
			}
		});
	}

	/***
	 * 安装
	 * 
	 * @param context
	 * @param file
	 */
	private static void installApk(Context context, File file) {
		Intent intent = new Intent();
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
		context.startActivity(intent);
	}

}
