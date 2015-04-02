package com.kingteller.client.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;

/**
 * 配置工具类
 * @author 王定波
 * 
 */
public class ConfigUtils {

	private final static String IPDOMAIN = "ipdomain";
	private final static String PORT = "port";

	public final static String KEY_PUSH_SY = "push_sy";
	public final static String KEY_PUSH_ZD = "push_zd";
	public final static String KEY_APP_NEW = "app_new";
	public final static String KEY_LOCK_SCREEN = "lock_screen";

	/**
	 * 设置域名
	 * 
	 * @param context
	 * @param IpDomain
	 */
	public static void setIpDomain(Context context, String IpDomain) {
		Editor sharedata = context
				.getSharedPreferences(
						KingTellerConfig.SHARED_PREFERENCES.CONFIG,
						Context.MODE_APPEND).edit();
		sharedata.putString(IPDOMAIN, IpDomain);
		sharedata.commit();
	}

	/**
	 * 设置端口
	 * 
	 * @param context
	 * @param data
	 */
	public static void setPort(Context context, String port) {
		Editor sharedata = context
				.getSharedPreferences(
						KingTellerConfig.SHARED_PREFERENCES.CONFIG,
						Context.MODE_APPEND).edit();
		sharedata.putString(PORT, port);
		sharedata.commit();
	}

	/**
	 * 得到域名
	 * 
	 * @param context
	 * @return
	 */
	public static String getIpDomain(Context context) {
		SharedPreferences sp = context
				.getSharedPreferences(
						KingTellerConfig.SHARED_PREFERENCES.CONFIG,
						Context.MODE_APPEND);
		return sp.getString(IPDOMAIN, URLConfig.DefalutIp);
	}

	/**
	 * 得到端口
	 * 
	 * @param context
	 * @return
	 */
	public static String getPort(Context context) {
		SharedPreferences sp = context
				.getSharedPreferences(
						KingTellerConfig.SHARED_PREFERENCES.CONFIG,
						Context.MODE_APPEND);
		return sp.getString(PORT, URLConfig.DefaultPort);
	}

	/**
	 * 生成URL
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public static String CreateUrl(Context context, String url) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(getIpDomain(context));
		sb.append(":");
		sb.append(getPort(context));
		sb.append(URLConfig.contextPath);
		sb.append(url);
		if (url.indexOf("?") == -1)
			sb.append("?version="
					+ context.getResources().getString(R.string.app_version));
		else
			sb.append("&version="
					+ context.getResources().getString(R.string.app_version));

		return sb.toString();
	}
	
	/**
	 * 生成不带版本的URL
	 * 
	 * @param context
	 * @param url
	 * @return
	 */
	public static String CreateNoVersionUrl(Context context, String url) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(getIpDomain(context));
		sb.append(":");
		sb.append(getPort(context));
		sb.append(URLConfig.contextPath);
		sb.append(url);

		return sb.toString();
	}

	/**
	 * 获取本地html文件
	 * 
	 * @param url
	 * @return
	 */
	public static String CreateLocalUrl(Context context, String url) {
		StringBuffer sb = new StringBuffer();
		sb.append("file:///android_asset");
		sb.append(URLConfig.contextPath);
		sb.append(url);
		if (url.indexOf("?") == -1)
			sb.append("?version="
					+ context.getResources().getString(R.string.app_version));
		else
			sb.append("&version="
					+ context.getResources().getString(R.string.app_version));

		return sb.toString();
	}

	/**
	 * 设置config的值
	 * @param key
	 * @param value
	 */
	public static void setConfigMeta(String key, boolean value) {
		Editor sharedata = KingTellerApplication
				.getApplication()
				.getSharedPreferences(
						KingTellerConfig.SHARED_PREFERENCES.CONFIG,
						Context.MODE_APPEND).edit();
		sharedata.putBoolean(key, value);
		sharedata.commit();
	}

	/**
	 * 得到config的值
	 * @param key
	 * @param defaultdata
	 * @return
	 */
	public static boolean getConfigMeta(String key, boolean defaultdata) {
		return KingTellerApplication
				.getApplication()
				.getSharedPreferences(
						KingTellerConfig.SHARED_PREFERENCES.CONFIG,
						Context.MODE_APPEND).getBoolean(key, defaultdata);
	}

}
