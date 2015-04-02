package com.kingteller.client.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.google.gson.Gson;
import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.account.MainActivity;
import com.kingteller.client.bean.account.InvalidateSessionBean;
import com.kingteller.client.bean.account.LoginBean;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.common.BaseBean;
import com.kingteller.client.bean.map.AddressBean;
import com.kingteller.client.bean.workorder.CommonMaintainInfo;
import com.kingteller.client.bean.workorder.Report;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.framework.utils.EncroptionUtils;
import com.kingteller.framework.utils.Logger;

public class KingTellerUtils {
	private static LocationManagerProxy mAMapLocationManager;

	/**
	 * 高德定位
	 * 
	 * @param context
	 * @param handler
	 */
	
	public static void GDLocation(Context context, final Handler handler) {
		// TODO Auto-generated method stub
		mAMapLocationManager = LocationManagerProxy.getInstance(context);
		mAMapLocationManager.setGpsEnable(true);
		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 100,
				new AMapLocationListener() {

					@Override
					public void onProviderDisabled(String provider) {

					}

					@Override
					public void onProviderEnabled(String provider) {

					}

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {

					}

					/**
					 * 此方法已经废弃
					 */
					@Override
					public void onLocationChanged(Location arg0) {
						// TODO Auto-generated method stub

					}

					/**
					 * 定位成功后回调函数
					 */
					@Override
					public void onLocationChanged(AMapLocation location) {

						if (location != null) {

							String desc = "";
							Bundle locBundle = location.getExtras();
							if (locBundle != null) {
								// cityCode = locBundle.getString("citycode");
								desc = locBundle.getString("desc");
							}
							Logger.i("address", desc);

							AddressBean address = new AddressBean();
							address.setLat(location.getLatitude());
							address.setLng(location.getLongitude());
							address.setName(desc);
							address.setAddress(desc);
							address.setCity(location.getCity());

							KingTellerApplication.getApplication()
									.setCurAddress(address);

							if (mAMapLocationManager != null) {
								mAMapLocationManager.removeUpdates(this);
								// mAMapLocationManager.destory();
							}
							mAMapLocationManager = null;

						}
					}
				});

	}

	/**
	 * 得到默认当前位置
	 * 
	 * @return
	 */
	public static AddressBean getDefaultAddress() {

		AddressBean dss = new AddressBean();
		dss.setName("广州");
		dss.setLat(23.118783);
		dss.setLng(113.353272);
		dss.setCity("广州");
		dss.setAddress("广州");
		return dss;
	}

	/**
	 * 更新首页信息
	 * 
	 * @param context
	 * @param pos
	 * @param isupdatedata
	 * @param dataisnew
	 */
	public static void MainUpdateStatus(Context context, int pos,
			boolean isupdatedata, boolean dataisnew) {
		Intent intentupdate = new Intent();
		intentupdate.putExtra(MainActivity.POSITION, pos);
		intentupdate.setAction(MainActivity.UPDATESTATUSACTION);
		intentupdate.putExtra(MainActivity.ISUPDATEDATA, isupdatedata);
		intentupdate.putExtra(MainActivity.ISDATANEW, dataisnew);
		context.sendBroadcast(intentupdate);
	}

	/**
	 * 生成字符串
	 * 
	 * @param response
	 * @return
	 */
	public static String getBaseJsonFromString(String response) {
		Gson gson = new Gson();
		BaseBean data = new BaseBean();
		data.setMsg(response);
		data.setStatus("1");
		return gson.toJson(data);
	}

	public static void SetJPushSetting(Context context) {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
				context);
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为点击后自动消失

		builder.notificationDefaults = Notification.DEFAULT_LIGHTS;
		// if (ConfigUtils.getConfigMeta(ConfigUtils.KEY_PUSH_SY, true))
		// builder.notificationDefaults |= Notification.DEFAULT_SOUND;
		// if (ConfigUtils.getConfigMeta(ConfigUtils.KEY_PUSH_ZD, true))
		// builder.notificationDefaults |= Notification.DEFAULT_VIBRATE;
		// 设置为铃声（
		// Notification.DEFAULT_SOUND）或者震动（
		// Notification.DEFAULT_VIBRATE）
		JPushInterface.setPushNotificationBuilder(1, builder);
	}

	/**
	 * 检查回话是否失效
	 */
	public static void CheckAuthSession(final Context context) {
		if (KingTellerApplication.getApplication().IsLogin()) {
			KTHttpClient fh = new KTHttpClient(true);
			fh.post(ConfigUtils.CreateUrl(context, URLConfig.CheckSessionUrl),
					new AjaxHttpCallBack<InvalidateSessionBean>(false) {
						@Override
						public void onDo(InvalidateSessionBean data) {
							// TODO Auto-generated method stub
							// 若返回参数invalidateSession为1代表会话已失效，为2代表会话未失效
							if (data.getInvalidateSession().equals("1")) {
								// 重新登录
								login(context);
							} else if (data.getInvalidateSession().equals("2")) {

							}
						}
					});
		}
	}

	/**
	 * 重新登录
	 */
	private static void login(final Context context) {
		final User user = User.getInfo(context);
		AjaxParams params = new AjaxParams();
		params.put("userAccount", user.getUserName());
		params.put("loginPassword",
				EncroptionUtils.EncryptSHA(user.getPassword()));
		params.put("iemi", KingTellerApplication.getDeviceToken());
		params.put("aliasRegisterSuccess", "1");

		KTHttpClient fh = new KTHttpClient(false);
		fh.post(ConfigUtils.CreateUrl(context, URLConfig.LoginUrl), params,
				new AjaxHttpCallBack<LoginBean>(false) {

					@Override
					public void onDo(LoginBean data) {
						if (data.getLoginError() == 1) {
							// 保存用户信息
							data.setUsername(user.getUserName());
							data.setPassword(user.getPassword());
							User.SaveInfo(context, data);

							// 保存回话cookie
							KingTellerApplication
									.getApplication()
									.setAccessToken(
											CommonUtils
													.getAuthCookie(onGetHeader("Set-Cookie")));

						}
					};

				});
	}

	/**
	 * 本方法判断自己些的一个Service是否已经运行
	 * 
	 * @return
	 */
	public static boolean isServiceWorked(Context context) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(200);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.kingteller.client.service.KingTellerService")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到报告从数据库靠id
	 * 
	 * @param context
	 * @param reportId
	 * @param clazz
	 * @return
	 */
	public static <T> T getReportDataFromString(String reportData,
			Class<T> clazz) {
		try {

			if (reportData == null)
				return null;
			Gson gson = new Gson();
			T obj = gson.fromJson(reportData, clazz);
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	/**
	 * 得到报告从数据库靠id
	 * 
	 * @param context
	 * @param reportId
	 * @param clazz
	 * @return
	 */
	public static Report getReportFromDataBase(Context context, String reportId) {

		KingTellerDb db = KingTellerDb.create(context);
		Report report = db.findById(reportId, Report.class);
		return report;

	}

	/**
	 * 得到报告从数据库靠id
	 * 
	 * @param context
	 * @param reportId
	 * @param clazz
	 * @return
	 */
	public static String getReportDataFromDataBase(Context context,
			String reportId) {
		try {
			KingTellerDb db = KingTellerDb.create(context);
			Report report = db.findById(reportId, Report.class);
			if (report == null)
				return "";
			else
				return report.getReportData();

		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}

	/**
	 * 保存报告到数据库
	 * 
	 * @param context
	 * @param reportId
	 * @param reportType
	 * @param reportData
	 */
	public static void saveReportToDataBase(Context context, String orderId,
			String reportType, int isSuccess, String reportData) {
		KingTellerDb db = KingTellerDb.create(context);
		Report rp = db.findById(orderId, Report.class);
		if (rp == null) {
			rp = new Report();
			rp.setReportData(reportData);
			rp.setOrderId(orderId);
			rp.setReportType(reportType);
			rp.setIsSuccess(isSuccess);
			db.save(rp);
		} else {
			rp.setReportData(reportData);
			rp.setOrderId(orderId);
			rp.setReportType(reportType);
			rp.setIsSuccess(isSuccess);
			db.update(rp);
		}

	}

	/**
	 * 保存维护信息到数据库
	 */
	public static void saveMaintainInfoDataBase(Context context,String maintainId
			,String maintainData,int isSuccess){
		KingTellerDb db = KingTellerDb.create(context);
		CommonMaintainInfo cmi = db.findById(maintainId, CommonMaintainInfo.class);
		if(cmi == null){
			cmi = new CommonMaintainInfo();
			cmi.setMaintainData(maintainData);
			cmi.setMaintainId(maintainId);
			cmi.setIsSuccess(isSuccess);
			db.save(cmi);
		}else{
			cmi = new CommonMaintainInfo();
			cmi.setMaintainData(maintainData);
			cmi.setMaintainId(maintainId);
			cmi.setIsSuccess(isSuccess);
			db.update(cmi);
		}
	}
	
	public static void saveMaintainInfoClgcDataBase(KingTellerDb db,String maintainId
			,String maintainData,int isSuccess){
		CommonMaintainInfo cmi = db.findById(maintainId, CommonMaintainInfo.class);
		if(cmi == null){
			cmi = new CommonMaintainInfo();
			cmi.setMaintainData(maintainData);
			cmi.setMaintainId(maintainId);
			cmi.setIsSuccess(isSuccess);
			db.save(cmi);
		}else{
			cmi = new CommonMaintainInfo();
			cmi.setMaintainData(maintainData);
			cmi.setMaintainId(maintainId);
			cmi.setIsSuccess(isSuccess);
			db.update(cmi);
		}
	}
	
	public static KingTellerDb create(Context context){
		KingTellerDb db = KingTellerDb.create(context);
		return db;
	}
	
	public static String getMaintainInfoClgcDataBase(KingTellerDb db,String maintainId){
		try{
			CommonMaintainInfo cmi = db.findById(maintainId, CommonMaintainInfo.class);
			if (cmi == null)
				return "";
			else
				return cmi.getMaintainData();

		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * 获取维护信息
	 */
	public static String getMaintainInfoDataBase(Context context,
			String maintainId) {
		try {
			KingTellerDb db = KingTellerDb.create(context);
			CommonMaintainInfo cmi = db.findById(maintainId, CommonMaintainInfo.class);
			if (cmi == null)
				return "";
			else
				return cmi.getMaintainData();

		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	
	/**
	 * 删除报告数据库
	 * 
	 * @param context    
	 * @param reportId
	 * @param reportType
	 * @param reportData
	 */
	public static void deleteReportFromDataBase(Context context,
			String orderId, String reportType) {
		KingTellerDb db = KingTellerDb.create(context);
		db.deleteById(Report.class, orderId);

	}

	/**
	 * 生成json
	 * 
	 * @param obj
	 * @return
	 */
	public static String convertJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Type typeofT) {
		Gson gson = new Gson();
		return gson.fromJson(json, typeofT);
	}

}
