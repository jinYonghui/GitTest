package com.kingteller.client.activity.account;

import java.util.ArrayList;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.account.fragment.FunctionFragment;
import com.kingteller.client.activity.account.fragment.MoreFragment;
import com.kingteller.client.activity.account.fragment.WaitDoFragment;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.adapter.PagerAdapter;
import com.kingteller.client.adapter.ToolBarTabAdapter;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.service.KingTellerService;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.utils.UpdateUtils;
import com.kingteller.client.view.TabView;
import com.kingteller.client.view.TabView.OnItemTabListener;
import com.kingteller.framework.http.AjaxCallBack;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * 主Activity
 * 
 * @author 王定波
 * 
 */
public class MainActivity extends BaseFragmentActivity implements
		OnItemTabListener {

	private static final String TAG = "MainActivity";
	private TabView toolbar;
	private ToolBarTabAdapter toolbarAdapter;
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;

	public static final int POSITION_WAITDOFRAGMENT = 0;
	public static final int POSITION_FUNCTIONFRAGMENT = 1;
	public static final int POSITION_MOREFRAGMENT = 2;
	public static final String UPDATESTATUSACTION = "com.kingteller.client.activity.MainActivity.UpStatusAction";
	public static final String POSITION = "position";
	public static final String ISUPDATEDATA = "is_update_data";
	public static final String ISDATANEW = "is_data_new";
	private int backCount = 1;
	private JPushRegRunnable cRunnable = new JPushRegRunnable();

	private Handler currHandler = new Handler();
	private Handler initHandler = new Handler();
	private InitRunnable initRunnable = new InitRunnable();

	private boolean isJushReg = false;
	private static final int RegTryTimes = 3;// 注册失败重启次数
	private int NowRegTryTimes = 0;
	private boolean isFirst = true;

	/**
	 * 更新小点的广播接收器
	 */
	private BroadcastReceiver MainContentRecever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent data) {
			String action = data.getAction();
			if (action.equals(UPDATESTATUSACTION)) {
				int pos = data.getIntExtra(POSITION, -1);
				boolean isupdatedata = data
						.getBooleanExtra(ISUPDATEDATA, false);
				boolean isdatanew = data.getBooleanExtra(ISDATANEW, false);
				switch (pos) {
				case POSITION_WAITDOFRAGMENT:
					if (isupdatedata
							&& viewPager.getCurrentItem() == POSITION_WAITDOFRAGMENT) {
						viewPager.setCurrentItem(POSITION_WAITDOFRAGMENT);
						wFragment.onResume();
					} else if (isupdatedata)
						viewPager.setCurrentItem(POSITION_WAITDOFRAGMENT);
					break;
				case POSITION_FUNCTIONFRAGMENT:
					break;
				case POSITION_MOREFRAGMENT:
					if (isupdatedata
							&& viewPager.getCurrentItem() == POSITION_MOREFRAGMENT)
						mFragment.onResume();
					break;
				default:
					break;
				}
				toolbarAdapter.setNew(pos, isdatanew);

			}
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				backCount = 1;
				break;
			}
		}
	};
	private WaitDoFragment wFragment;
	private FunctionFragment fFragment;
	private MoreFragment mFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentWithOutTitle(R.layout.layout_main);

		initUI();
		initData();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		isFirst = false;
		toolbar = (TabView) findViewById(R.id.toolbar);
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		// 初始化屏幕尺寸
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		KingTellerConfig.SCREEN.Width = displayMetrics.widthPixels;

	}

	private void initData() {
		// TODO Auto-generated method stub
		toolbarAdapter = new ToolBarTabAdapter(this);
		toolbar.setAdapter(toolbarAdapter);
		toolbar.setOnItemListener(this);

		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		wFragment = new WaitDoFragment();
		fFragment = new FunctionFragment();
		mFragment = new MoreFragment();

		fragments.add(wFragment);
		fragments.add(fFragment);
		fragments.add(mFragment);

		pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(pagerAdapter);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub

				toolbarAdapter.setSelectPostion(pos);
				if (pos == POSITION_FUNCTIONFRAGMENT)
					fFragment.setData();
				else if (pos == POSITION_MOREFRAGMENT) {
					mFragment.onResume();
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		// 设置信息

		toolbarAdapter.setNew(POSITION_MOREFRAGMENT,
				ConfigUtils.getConfigMeta(ConfigUtils.KEY_APP_NEW, false));

		// 注册接收器
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UPDATESTATUSACTION);
		registerReceiver(MainContentRecever, intentFilter);

		// 启动服务
		if (!KingTellerUtils.isServiceWorked(this))
			startService(new Intent(KingTellerService.KingTellerServiceAction));

		if (JPushInterface.isPushStopped(this))
			JPushInterface.resumePush(getApplicationContext());

		// 设置推送样式
		// KingTellerUtils.SetJPushSetting(this);

		// 延迟设置别名
		currHandler.postDelayed(cRunnable, 10000);
		// 延时3秒
		initHandler.postDelayed(initRunnable, 3000);
		// 定位
		KingTellerUtils.GDLocation(MainActivity.this, null);

	}

	@Override
	public void onBackPressed() {
		if (backCount == 2) {
			moveTaskToBack(true);
		} else {
			Toast.makeText(this, "再按一次退出到桌面", Toast.LENGTH_SHORT).show();
			backCount++;
			mHandler.sendEmptyMessageDelayed(0, 2000);
		}
	}

	@Override
	public void OnItemClick(int pos) {
		// TODO Auto-generated method stub
		toolbarAdapter.setSelectPostion(pos);
		viewPager.setCurrentItem(pos);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!User.getInfo(this).isPushReg() && !isJushReg
				&& NowRegTryTimes == RegTryTimes) {
			// 失敗重新註冊别名
			isJushReg = true;
			currHandler.postDelayed(cRunnable, 60000);
		}
		// 检测session是否失效
		if (!isFirst)
			KingTellerUtils.CheckAuthSession(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (MainContentRecever != null) {
			unregisterReceiver(MainContentRecever);
			MainContentRecever = null;
		}
	}

	/**
	 * 极光推送设置别名返回
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Logger.i(TAG, logs + alias);
				setPushAlias(true);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Logger.i(TAG, logs + alias);
				setPushAlias(false);
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Logger.e(TAG, logs + alias);
				setPushAlias(false);
			}
		}

	};

	private class JPushRegRunnable implements Runnable {

		public void run() {
			// 设置别名
			JPushInterface.setAliasAndTags(MainActivity.this,
					User.getInfo(MainActivity.this).getAlias(), null,
					mAliasCallback);
			currHandler.removeCallbacks(this);
		}
	}

	private class InitRunnable implements Runnable {

		public void run() {
			// 检测更新
			UpdateUtils.CheckUpdate(MainActivity.this, false);
			RegJPushImei();
			initHandler.removeCallbacks(this);
		}
	}

	private void setPushAlias(boolean issuccess) {
		// 失败重新设置别名3次
		if (NowRegTryTimes < RegTryTimes && !issuccess) {
			NowRegTryTimes++;
			currHandler.postDelayed(cRunnable, 60000);
			Logger.i("try", NowRegTryTimes + "次");
		} else {
			isJushReg = false;
			Logger.i("success", "成功");
		}
		User.savePushReg(MainActivity.this, issuccess);
		AjaxParams params = new AjaxParams();
		params.put("userAccount", User.getInfo(this).getUserName());
		params.put("aliasRegisterSuccess", issuccess ? "1" : "0");

		// KTHttpClient fh = new KTHttpClient(true);
		// fh.post(ConfigUtils.CreateUrl(this, URLConfig.PushUpdateAliasUrl),
		// new AjaxHttpCallBack<UpdateAliasStatusBean>(false) {
		// @Override
		// public void onDo(UpdateAliasStatusBean data) {
		// // TODO Auto-generated method stub
		// }
		// });
	}

	/**
	 * 注册IMEI
	 */
	private void RegJPushImei() {
		User user = User.getInfo(this);
		AjaxParams params = new AjaxParams();
		params.put("userAccount", user.getUserName());
		params.put("userTelNo", "");
		params.put("userIemi", KingTellerApplication.getDeviceToken());
		params.put("userAlias", user.getAlias());
		params.put("tagName", user.getOrgName());
		KTHttpClient fh = new KTHttpClient(true);

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.RegJPushIMEIUrl), params,
				new AjaxCallBack<String>() {
				});

	}

}
