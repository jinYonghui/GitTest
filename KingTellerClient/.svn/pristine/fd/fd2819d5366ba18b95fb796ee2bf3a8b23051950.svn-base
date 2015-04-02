package com.kingteller.client.activity.workorder;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.framework.utils.CommonUtils;

public class RapairReportWebViewActivity extends BaseActivity implements
		OnClickListener {
	public static int state = 0;
	private ConnectivityManager cm;
	private String url;
	private WebView webView;
	private String orderno ;
	private String orderProperty;
	private String webUrl;
	private User user;
	private String uid;
	private String pwd;
	

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			KingTellerProgress.closeProgress();
			webView.destroy();
			try {
				new KTAlertDialog.Builder(RapairReportWebViewActivity.this)
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
		
		init();
		initData();
	}

	private void init() {
		title_title.setText("维护报告");
		title_right.setVisibility(View.INVISIBLE);
		title_left.setOnClickListener(this);
		
		orderno = getIntent().getStringExtra("orderno");
		orderProperty = getIntent().getStringExtra("orderProperty");
		webUrl = getIntent().getStringExtra("webUrl");
		user = User.getInfo(this);
		uid = user.getUserName();
		pwd = user.getPassword();
		url = webUrl +"?uid="+uid+"&pwd="+pwd+"&orderno="+orderno+"&orderProperty="+orderProperty+"&flag=1";
		
	}

	private void checkNetConect() {
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (CommonUtils.IsAirplaneMode(RapairReportWebViewActivity.this))
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

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initData() {
		checkNetConect();
		webView = (WebView) findViewById(R.id.wv_question);
		WebSettings setting = webView.getSettings();
		setting.setDefaultTextEncodingName("utf-8");
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(false);
		webView.getSettings().setDomStorageEnabled(true);  
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); 
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.addJavascriptInterface(RapairReportWebViewActivity.this, "kingteller");

	//	KingTellerJsInterface js = new KingTellerJsInterface(this);
		
		//webView.addJavascriptInterface(js, "kingteller");

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
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				handler.proceed();
			}
			
			@Override
			public void onPageStarted(WebView view, final String url,
					Bitmap favicon) {
				// TODO Auto-generated method stub

				super.onPageStarted(view, url, favicon);
				KingTellerProgress.showProgress(RapairReportWebViewActivity.this,
						"正在加载");

			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);
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
