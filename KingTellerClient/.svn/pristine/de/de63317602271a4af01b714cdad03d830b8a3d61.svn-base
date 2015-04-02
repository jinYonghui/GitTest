package com.kingteller.client.recever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.kingteller.R;
import com.kingteller.client.bean.account.Push;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.FloatWindowManager;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 推送消息弹出通知栏
 * 
 * @author 王定波
 * 
 */
public class KingtellerNotifiaction {

	// notifiaction显示的内容
	public static int notifiactionCount;
	public static NotificationManager manager;
	private static KingTellerDb finalDb;

	@SuppressWarnings("deprecation")
	public static void showNotification(Context context, Push data) {

		finalDb = KingTellerDb.create(context);
		// 查询数据库是否有了
		if (!CommonUtils.isEmpty(data.getId())) {
			// 数据库处理防止异常
			try {
				Push datatmp = finalDb.findById(data.getId(), Push.class);
				if (datatmp == null) {
					finalDb.save(data);
				} else {
					// 重复了
					return;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		KingtellerNotifiaction.notifiactionCount++;

		// 设置点击事件 发送广播
		Intent intent = new Intent(context, KingTellerBroadCastRecever.class);
		intent.setAction(KingTellerBroadCastRecever.MAINACTIVITYACTION);
		PendingIntent pendIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.notice_logo;

		// 设置滚动的信息
		if (CommonUtils.isEmpty(data.getMsg()))
			notification.tickerText = context.getString(R.string.app_name);
		else {
			if (CommonUtils.isEmpty(data.getSender()))
				notification.tickerText = data.getMsg();
			else
				notification.tickerText = data.getSender() + ":"
						+ data.getMsg();
		}

		notification.setLatestEventInfo(context, context
				.getString(R.string.push_title), String.format(
				context.getString(R.string.push_msg), notifiactionCount),
				pendIntent);

		notification.contentView = new RemoteViews(context.getPackageName(),
				R.layout.item_notifacation_push);

		// 设置显示的数据
		if (!CommonUtils.isEmpty(data.getMsg())) {
			notification.contentView.setTextViewText(R.id.notifacationtv,
					data.getMsg());
		} else {
			notification.contentView.setTextViewText(R.id.notifacationtv,
					String.format(context.getString(R.string.push_msg),
							notifiactionCount));
		}

		// 设置标题
		if (CommonUtils.isEmpty(data.getSender()))
			notification.contentView.setTextViewText(
					R.id.title,
					context.getString(R.string.push_title)
							+ String.format(
									context.getString(R.string.push_title_f),
									notifiactionCount));
		else
			notification.contentView.setTextViewText(
					R.id.title,
					data.getSender()
							+ String.format(
									context.getString(R.string.push_title_f),
									notifiactionCount));

		// 设置时间
		if (!CommonUtils.isEmpty(data.getSend_time()))
			notification.contentView.setTextViewText(R.id.time,
					data.getSend_time());

		// 设置一些属性
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		
		if (ConfigUtils.getConfigMeta(ConfigUtils.KEY_PUSH_SY, true))
			notification.defaults |= Notification.DEFAULT_SOUND;

		if (ConfigUtils.getConfigMeta(ConfigUtils.KEY_PUSH_ZD, true))
			notification.defaults |= Notification.DEFAULT_VIBRATE;

		/**
		 * 增加闪灯
		 */
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 5000; // 闪光时间，毫秒
		notification.flags|=Notification.FLAG_SHOW_LIGHTS; 

		manager.notify(CommonCodeConfig.NotifiactionPushID, notification);

		if (FloatWindowManager.isWindowShowing())
			FloatWindowManager.updateFloat(context);

	}
}
