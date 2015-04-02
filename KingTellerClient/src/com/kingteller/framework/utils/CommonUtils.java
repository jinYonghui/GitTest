package com.kingteller.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.WindowManager;

public class CommonUtils {

	/**
	 * 判断对象是否为空
	 */
	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		else if (str.equals(""))
			return true;
		else
			return false;
	}
	
	public static boolean isEqualNull(String str){
		if(str == null){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean isEqualEmpty(String str) {
		if (str == null)
			return true;
		else if (str.equals(""))
			return true;
		else if(str.equals("请选择"))
			return true;
		else
			return false;
	}

	/**
	 * 判断对象是否为空
	 */
	public static boolean isEmpty(Long str) {
		if (str == null)
			return true;
		else if (str.equals(""))
			return true;
		else
			return false;
	}

	/**
	 * 判断对象是否为空
	 */
	public static boolean isEmpty(Integer str) {
		if (str == null)
			return true;
		else if (str.equals(""))
			return true;
		else
			return false;
	}

	/**
	 * 判断网络是否有效
	 */
	public static boolean isNetAvaliable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断飞行模式
	 */
	public static boolean IsAirplaneMode(Context context) {
		int isAirplaneMode = Settings.System.getInt(
				context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
				0);
		return (isAirplaneMode == 1) ? true : false;
	}

	/**
	 * 判断是否wifi
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 */
	public static void hideInputMethod(Context context) {

		((Activity) context).getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 判断SD卡是否可用
	 * 
	 * @return
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * 获取JsessionID
	 * 
	 * @return
	 */
	public static String getAuthCookie(String cookieValue) {
		String sessionId = "";
		if (cookieValue != null) {
			sessionId = cookieValue.substring(0, cookieValue.indexOf(";"));
			sessionId = sessionId.substring(sessionId.indexOf("=") + 1,
					sessionId.length());
		}
		return sessionId;
	}

	/**
	 * 打开浏览器
	 * 
	 * @return
	 */
	public static void openBrowser(Context context, String url) {

		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setData(Uri.parse(url));
		context.startActivity(intent);
	}

	/**
	 * 将字符转为2进制数据
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 检查是否数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		return str.matches("\\d+");
	}

	/**
	 * null转换
	 * 
	 * @param s
	 * @return
	 */
	public static String ToString(String s) {
		if (isEmpty(s))
			return "";
		else
			return s;
	}

	/**
	 * 判断是否图片
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isPicure(String path) {
		if (path.endsWith(".png") || path.endsWith(".PNG")
				|| path.endsWith(".jpg") || path.endsWith(".JPG")
				|| path.endsWith(".jpeg") || path.endsWith(".JPEG"))
			return true;
		else
			return false;
	}
}
