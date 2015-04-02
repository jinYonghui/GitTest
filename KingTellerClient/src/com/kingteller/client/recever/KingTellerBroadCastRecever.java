package com.kingteller.client.recever;

import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.account.LoginActivity;
import com.kingteller.client.activity.account.MainActivity;
import com.kingteller.client.config.CommonCodeConfig;
import com.kingteller.client.netauth.exception.NetErrorCode;
import com.kingteller.client.service.KingTellerService;
import com.kingteller.client.utils.FloatWindowManager;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.framework.utils.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

/**
 * @Title KingTellerBroadCastRecever
 * @Package com.kingteller.client.recever
 * @Description KingTellerBroadCastRecever
 * @author 王定波
 */
public class KingTellerBroadCastRecever extends BroadcastReceiver {

	public static final String NETERRORACTION = "com.kingteller.client.recever.KingTellerBroadCastRecever.netErrorAction";
	public static final String AUTHERRORACTION = "com.kingteller.client.recever.KingTellerBroadCastRecever.authErrorAction";
	public static final String DATAERRORACTION = "com.kingteller.client.recever.KingTellerBroadCastRecever.dataErrorAction";
	public static final String BOOTSTARTACTION = "android.intent.action.BOOT_COMPLETED";// 系统重新启动
	public static final String MAINACTIVITYACTION = "com.kingteller.client.recever.KingTellerBroadCastRecever.OpenMainActivityAction";

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(NETERRORACTION)) {
			// context.startActivity(new Intent(content,
			// NetErrorShowActivity.class));
			Toast.makeText(context, NetErrorCode.NETERRORMSG,
					Toast.LENGTH_SHORT).show();
		} else if (action.equals(AUTHERRORACTION)) {

			KingTellerApplication.getApplication().exit(false);
			Toast.makeText(context, NetErrorCode.AUTHERRORMSG,
					Toast.LENGTH_SHORT).show();
			Intent il = new Intent(context, LoginActivity.class);
			il.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			il.putExtra(LoginActivity.ISFROM, true);
			context.startActivity(il);

		} else if (action.equals(DATAERRORACTION)) {
			Toast.makeText(context, NetErrorCode.DATAERRORMSG,
					Toast.LENGTH_SHORT).show();
		} else if (action.equals(BOOTSTARTACTION)) {
			// 启动服务
			if (KingTellerApplication.getApplication().IsLogin())
				context.startService(new Intent(
						KingTellerService.KingTellerServiceAction));
		} else if (MAINACTIVITYACTION.equals(action)) {
			try {
				KingtellerNotifiaction.notifiactionCount = 0;
				KingtellerNotifiaction.manager
						.cancel(CommonCodeConfig.NotifiactionPushID);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 打开自定义的Activity
			Intent i = new Intent(context, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}

	}

}
