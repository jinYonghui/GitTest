package com.kingteller.client;

import java.io.File;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import cn.jpush.android.api.JPushInterface;

import com.kingteller.client.bean.map.AddressBean;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.utils.SQLiteHelper;
import com.kingteller.framework.utils.CommonUtils;

/**
 * KingTellerApplication全局
 * @author 王定波
 *
 */
public class KingTellerApplication extends Application {

	public final static String TAG = "KingTellerApplication";
	private static KingTellerApplication application;

	private SharedPreferences preferences;
	File cacheDir = null;
	private AddressBean curAddress;// 当前位置
	
	@Override
	public void onCreate() {

		super.onCreate();
		application = this;
		// 初始化缓存目录
		initCacheDir();
		initData();
		
	}

	private void initData() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 设置默认当前地址
		setCurAddress(KingTellerUtils.getDefaultAddress());
		// 推送初始化
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		SQLiteHelper helper = new SQLiteHelper(application);
		try {
			helper.createDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//初始化异常捕获		
		/*CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());*/
		
	}
	
	/**
	 * 获取得到的JSESSION
	 * 
	 * @return
	 */
	public String getAccessToken() {
		return preferences.getString(
				KingTellerConfig.SHARED_PREFERENCES.AUTH_COOKIE, "");

	}

	/**
	 * 设置JSESSION
	 * 
	 * @param authcookie
	 */
	public void setAccessToken(String authcookie) {
		preferences.edit().putString(KingTellerConfig.SHARED_PREFERENCES.AUTH_COOKIE,
						authcookie).commit();

	}

	/**
	 * 判断用户是否登陆
	 * @return
	 */
	public boolean IsLogin() {
		String cookie = getAccessToken();
		if (cookie == null || cookie.equals("")) {
			return false;
		} else
			return true;
	}

	/**
	 * 初始化生成缓存目录
	 */
	private void initCacheDir() {
		if (CommonUtils.sdCardIsAvailable()) {
			File dir = new File(KingTellerConfig.CACHE_PATH.SD_DADA);
			File ddir = new File(KingTellerConfig.CACHE_PATH.SD_DOWNLOAD);
			File ldir = new File(KingTellerConfig.CACHE_PATH.SD_LOG);

			if (!dir.isDirectory()) {
				dir.mkdirs();
			}

			if (!ddir.isDirectory()) {
				ddir.mkdirs();
			}

			if (!ldir.isDirectory()) {
				ldir.mkdirs();
			}
		}
	}

/*	*//**
	 * 加载缓冲数据
	 * @throws FileNotFoundException 
	 * *//*
	public void loadCacheData() throws Exception{
		  String dbDirPath = "/data/data/com.kingteller/databases";
		  File dbDir = new File(dbDirPath);
		  if(!dbDir.exists())
		     dbDir.mkdir();
		  InputStream is = getResources().openRawResource(R.raw.data);
		  FileOutputStream os = new FileOutputStream(dbDirPath+"/data.db");
		  byte[] buffer = new byte[1024];
		  int count = 0;
		  while ((count = is.read(buffer)) > 0) {
		    os.write(buffer, 0, count);
		  }
		  is.close();
		  os.close();
	}*/
	/**
	 * 注销
	 * @param isLoginout 是否注销，清楚用户信息
	 */
	public void exit(boolean isLoginout) {
		preferences.edit().clear().commit();
		if (isLoginout)
			getSharedPreferences(KingTellerConfig.SHARED_PREFERENCES.USER,
					Context.MODE_APPEND).edit().clear().commit();
	}

	public static final KingTellerApplication getApplication() {
		return application;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取IMEI
	 * 
	 * @return
	 */
	public static String getDeviceToken() {
		return ((TelephonyManager) getApplication().getSystemService(
				TELEPHONY_SERVICE)).getDeviceId();
	}

	public SharedPreferences getPreferences() {
		// TODO Auto-generated method stub
		return preferences;
	}

	public AddressBean getCurAddress() {
		return curAddress;
	}

	public void setCurAddress(AddressBean curAddress) {
		this.curAddress = curAddress;
	}

}