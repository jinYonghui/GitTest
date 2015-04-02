package com.kingteller.client.recever;

import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.FloatWindowManager;
import com.kingteller.framework.utils.Logger;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * 锁屏广播接收器
 * @author 王定波
 *
 */
public class ScreenReceiver extends BroadcastReceiver {

	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (Intent.ACTION_SCREEN_ON.equals(action) && isLockScreen(context) && ConfigUtils.getConfigMeta(
				ConfigUtils.KEY_LOCK_SCREEN, true)) {
			// 开屏

			if (!FloatWindowManager.isWindowShowing())
				handler.post(new Runnable() {
					@Override
					public void run() {
						FloatWindowManager.createFloatWindow(context);
					}
				});
		} else if (Intent.ACTION_USER_PRESENT.equals(action)) {
			// 解锁

			if (FloatWindowManager.isWindowShowing())
				handler.post(new Runnable() {
					@Override
					public void run() {
						FloatWindowManager.removeFloatWindow(context);
					}
				});
		} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
			// 锁屏
			if (!FloatWindowManager.isWindowShowing() && isLockScreen(context) && ConfigUtils.getConfigMeta(
					ConfigUtils.KEY_LOCK_SCREEN, true))
				handler.post(new Runnable() {
					@Override
					public void run() {
						FloatWindowManager.createFloatWindow(context);
					}
				});
		}
	}

	/**
	 * 判断是否锁屏界面
	 * 
	 * @return
	 */
	public boolean isLockScreen(Context context) {
		KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

		if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
			return true;
		}
		return false;
	}

}
