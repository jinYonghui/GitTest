package com.kingteller.client.activity.common;

import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerJsInterface;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.framework.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 通用webview
 * 
 * @author 王定波
 * 
 */

public class CommonWebViewActivity extends BaseActivity implements
		OnClickListener {
	public static final String FROM_NOW = "from_now";
	//private static final String TAG = CommonWebViewActivity.class.getSimpleName();
	public static final String TITLE = "title";
	public static final String URL = "url";
	public static int state = 0;
	private ConnectivityManager cm;
	private boolean fromNow = false;
	int progress = 0;
	private String title;
	private String url;
	private WebView webView;

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			KingTellerProgress.closeProgress();
			webView.destroy();
			try {
				new KTAlertDialog.Builder(CommonWebViewActivity.this)
						.setTitle("连接失败")
						.setMessage("抱歉，您的网络不稳定！")
						.setPositiveButton("确定",
								new KTAlertDialog.OnClickListener() {
									public void onClick(
											DialogInterface paramAnonymous2DialogInterface,
											int pos) {
										finish();
									}
								}).create().show();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.layout_webview);
		Bundle bundles = getIntent().getExtras();
		title = "正在读取中";
		if (bundles != null) {
			fromNow = bundles.containsKey(FROM_NOW);
			url = bundles.getString(URL);
			if (!fromNow) {
				title = bundles.getString(TITLE);
			}
		}
		init();
		initData();
	}
	
	private void init() {
		title_title.setText(title);
		title_left.setOnClickListener(this);
		title_right.setVisibility(View.INVISIBLE);
		if (fromNow)
			title_title.getLayoutParams().width = 260;
	}

	private void checkNetConect() {
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (CommonUtils.IsAirplaneMode(CommonWebViewActivity.this))
			new KTAlertDialog.Builder(this)
					.setTitle("连接失败")
					.setMessage("您的手机正处于飞行模式！")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									finish();
								}
							}).create().show();
		else if (cm.getActiveNetworkInfo() != null
				&& !cm.getActiveNetworkInfo().isAvailable())
			new KTAlertDialog.Builder(this)
					.setTitle("连接失败")
					.setMessage("抱歉，您的网络不稳定！")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									finish();
								}
							}).create().show();
	}


	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		checkNetConect();
		webView = (WebView) findViewById(R.id.wv_question);
		WebSettings setting = webView.getSettings();
		setting.setDefaultTextEncodingName("utf-8");
		setting.setJavaScriptEnabled(true);

		KingTellerJsInterface js = new KingTellerJsInterface(this);
		String id = getIntent().getStringExtra("id");
		String flowCode = getIntent().getStringExtra("flowCode");
		if (!CommonUtils.isEmpty(id))
			js.setParam("params", "{'id':'" + id + "','flowCode':'" + flowCode
					+ "'}");

		webView.addJavascriptInterface(js, "kingteller");

		CookieManager.getInstance().setCookie(
				ConfigUtils.getIpDomain(this) + ":" + ConfigUtils.getPort(this)
						+ "/",
				KingTellerConfig.COOKIENAME
						+ "="
						+ KingTellerApplication.getApplication()
								.getAccessToken());
		CookieSyncManager.getInstance().sync();

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// 开始

				super.onPageFinished(view, url);
				KingTellerProgress.closeProgress();

			}

			@Override
			public void onPageStarted(WebView view, final String url,
					Bitmap favicon) {
				// TODO Auto-generated method stub

				super.onPageStarted(view, url, favicon);
				KingTellerProgress.showProgress(CommonWebViewActivity.this,
						"正在加载");

			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
				if (fromNow)
					title_title.setText(title);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				// Required functionality here
				return super.onJsAlert(view, url, message, result);
			}
		});

		webView.loadUrl(url);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_left:
			finish();
			break;

		}

	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent keyEvent) {
		if (state == 0 && keycode == KeyEvent.KEYCODE_BACK
				&& keyEvent.getRepeatCount() == 0) {
			KingTellerProgress.closeProgress();
			if (webView.canGoBack() && keycode == KeyEvent.KEYCODE_BACK) {
				webView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keycode, keyEvent);
	}

}
