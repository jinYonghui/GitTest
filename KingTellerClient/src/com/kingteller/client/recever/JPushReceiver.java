package com.kingteller.client.recever;

import com.google.gson.Gson;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.account.MainActivity;
import com.kingteller.client.bean.account.JPushArrivedBean;
import com.kingteller.client.bean.account.Push;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.framework.http.AjaxCallBack;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.framework.utils.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {

	private static final String TAG = "JPushReceiver";
	private static final String EXTRA = "cn.jpush.android.EXTRA";
	private static final String TITLE = "cn.jpush.android.NOTIFICATION_CONTENT_TITLE";
	private static final String ALERT = "cn.jpush.android.ALERT";
	private Context context;
	private Gson gson;

	@Override
	public void onReceive(Context context, Intent intent) {

		gson = new Gson();
		this.context = context;
		Bundle bundle = intent.getExtras();
		Logger.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Logger.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Logger.d(
					TAG,
					"[JPushReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Logger.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			// 处理到达的信息
			String appendStr = bundle.getString(EXTRA);
			Logger.d(TAG, "[JPushReceiver] 接收到推送下来的通知: " + appendStr);
			Push pdata = new Push();
			pdata.setMsg(bundle.getString(ALERT));

			if (!CommonUtils.isEmpty(appendStr)
					&& !appendStr.trim().equals("[]")) {
				try {
					JPushArrivedBean data = gson.fromJson(appendStr,
							JPushArrivedBean.class);
					if (data.isMobileRecevie()) {
						if (!CommonUtils.isEmpty(data.getJpushSendNo())) {
							setPushArrived(String
									.valueOf(data.getJpushSendNo()));
						}
					}
					pdata.setId(data.getPushTransSmsIdentifyNo());
					pdata.setSender(data.getSenderUserName());
					pdata.setSend_time(data.getSenderDateStr());
				} catch (Exception ex) {
				}
			}
			KingTellerUtils.MainUpdateStatus(context,
					MainActivity.POSITION_WAITDOFRAGMENT, true, true);

			// //删除所有notification
			JPushInterface.clearAllNotifications(context);
			// 提示消息
			if (KingTellerApplication.getApplication().IsLogin()) {
				KingtellerNotifiaction.showNotification(context, pdata);
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Logger.d(TAG, "[JPushReceiver] 用户点击打开了通知");

			JPushInterface.reportNotificationOpened(context,
					bundle.getString(JPushInterface.EXTRA_MSG_ID));

			// 打开自定义的Activity
			Intent i = new Intent(context, MainActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Logger.d(
					TAG,
					"[JPushReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else {
			Logger.d(TAG,
					"[JPushReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey1:" + key + ", value1:" + bundle.getInt(key));
			} else {
				sb.append("\nkey2:" + key + ", value2:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	// private void processCustomMessage(Context context, Bundle bundle) {
	// if (MainActivity.isForeground) {
	// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	// Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
	// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
	// if (!CommonUtils.isEmpty(extras)) {
	// try {
	// JSONObject extraJson = new JSONObject(extras);
	// if (null != extraJson && extraJson.length() > 0) {
	// msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
	// }
	// } catch (JSONException e) {
	//
	// }
	//
	// }
	// context.sendBroadcast(msgIntent);
	// }
	// }

	private void setPushArrived(String msg) {
		AjaxParams params = new AjaxParams();
		params.put("jpushSendNo", msg);

		KTHttpClient fh = new KTHttpClient(true);
		fh.post(ConfigUtils.CreateUrl(context, URLConfig.JPushCallBackUrl),
				params, new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						Logger.i("ssss","成功"+t);
					}
				});
	}
}
