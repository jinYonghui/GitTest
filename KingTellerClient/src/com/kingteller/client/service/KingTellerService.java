package com.kingteller.client.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.google.gson.Gson;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.map.AddressBean;
import com.kingteller.client.bean.map.UploadLocation;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.recever.ScreenReceiver;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.FloatWindowManager;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.framework.http.AjaxCallBack;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.Logger;


/**
 * 全局服务上传位置、检查是否登陆等操作
 * @author 王定波
 *
 */
public class KingTellerService extends Service {

	private static final String TAG = "KingTellerService";
	private IBinder binder = new KingTellerService.LocalBinder();
	private KTHttpClient fh;
	private User user;
	private Gson gson;
	private LocationManagerProxy aMapLocManager;
	public final static String KingTellerServiceAction = "com.kingteller.client.service.KingTellerServiceAction";
	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	private AMapLocationListener listener = new AMapLocationListener() {

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * 此方法已经废弃
		 */
		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * 定位回调函数
		 */
		@Override
		public void onLocationChanged(AMapLocation location) {
			// TODO Auto-generated method stub
			if (location != null) {
				String desc = "";
				Bundle locBundle = location.getExtras();
				if (locBundle != null) {
					desc = locBundle.getString("desc");
				}

				AddressBean address = new AddressBean();
				address.setLat(location.getLatitude());
				address.setLng(location.getLongitude());
				address.setName(desc);
				address.setAddress(desc);
				address.setCity(location.getCity());

				KingTellerApplication.getApplication().setCurAddress(address);

				UploadLocation(address);

			}

		}
	};

	private PhoneStateListener phonelistener = new PhoneStateListener() {
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 来电状态
				if (FloatWindowManager.isWindowShowing())
					handler.post(new Runnable() {
						@Override
						public void run() {
							FloatWindowManager
									.removeFloatWindow(getApplicationContext());
						}
					});
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接听状态
				if (FloatWindowManager.isWindowShowing())
					handler.post(new Runnable() {
						@Override
						public void run() {
							FloatWindowManager
									.removeFloatWindow(getApplicationContext());
						}
					});
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 挂断后回到空闲状态
				if (!FloatWindowManager.isWindowShowing() && isLockScreen() && ConfigUtils.getConfigMeta(
						ConfigUtils.KEY_LOCK_SCREEN, true))
					handler.post(new Runnable() {
						@Override
						public void run() {
							FloatWindowManager
									.createFloatWindow(getApplicationContext());
						}
					});
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 检查是否登陆有效
	 */
	private TimerTask timerTask = new TimerTask() {
		public void run() {
			KingTellerUtils.CheckAuthSession(KingTellerService.this);
		}
	};

	private Timer timerCheckauth;
	private ScreenReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {

		return binder;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");

		user = User.getInfo(this);
		gson = new Gson();

		aMapLocManager = LocationManagerProxy.getInstance(this);

		if (timerCheckauth == null)
			timerCheckauth = new Timer();

		// 浮动
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phonelistener,
				PhoneStateListener.LISTEN_CALL_STATE);

		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "onStart");
		super.onStart(intent, startId);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");

		try {

			/*
			 * mAMapLocManager.setGpsEnable(false);//
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			aMapLocManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork,
					KingTellerConfig.LocationTime * 60 * 1000, 0, listener);

			timerCheckauth.schedule(timerTask,
					KingTellerConfig.CheckAuhtTime * 60 * 1000,
					KingTellerConfig.CheckAuhtTime * 60 * 1000);

			// timerFloat.scheduleAtFixedRate(RefreshFloatTask, 0, 1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return START_REDELIVER_INTENT;
	}

	/**
	 * 停止定位
	 */
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(listener);
			aMapLocManager.destory();
		}
		aMapLocManager = null;

		if (timerCheckauth != null) {
			timerCheckauth.cancel();
			timerCheckauth = null;
		}

		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	// 定义内容类继承Binder
	public class LocalBinder extends Binder {
		// 返回本地服务
		KingTellerService getService() {
			return KingTellerService.this;
		}
	}

	/**
	 * 上传当前经纬度
	 */
	private void UploadLocation(AddressBean address) {
		if (KingTellerApplication.getApplication().IsLogin()) {
			fh = new KTHttpClient(true);
			user = User.getInfo(this);
			UploadLocation location = new UploadLocation();
			location.setImeiCode(KingTellerApplication.getDeviceToken());
			location.setUserAccount(user.getUserName());
			location.setLatidu(String.valueOf(address.getLat()));
			location.setLongtidu(String.valueOf(address.getLng()));
			AjaxParams params = new AjaxParams();
			params.put("longlati", gson.toJson(location));

			Logger.i(TAG, gson.toJson(location));

			fh.post(ConfigUtils.CreateUrl(this, URLConfig.UploadLocationUrl),
					params, new AjaxCallBack<String>() {
					});
		}

	}

	/**
	 * 判断是否锁屏界面
	 * 
	 * @return
	 */
	public boolean isLockScreen() {
		KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

		if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
			return true;
		}
		return false;
	}

}
