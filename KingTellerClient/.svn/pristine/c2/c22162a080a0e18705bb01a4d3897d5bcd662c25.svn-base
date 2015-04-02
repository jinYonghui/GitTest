package com.kingteller.client.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.bean.account.LoginBean;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.client.view.ChangeColorEditText;
import com.kingteller.client.view.LoginDialog;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.framework.utils.EncroptionUtils;

/**
 * 登陆Activity
 * @author 王定波
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private ChangeColorEditText username;
	private ChangeColorEditText password;
	private Button btn_login;
	private ImageView setURLIv;
	private String etUserNamestr;
	private String etPasswordstr;
	public static final String ISFROM = "is_from";
	public boolean isfrom = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (KingTellerApplication.getApplication().IsLogin()) {
			
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
			return;
		}

		setContentWithOutTitle(R.layout.layout_login);
		initUI();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		isfrom = getIntent().getBooleanExtra(ISFROM, false);

		// session失效执行
		User user = User.getInfo(this);
		username.setText(user.getUserName());
		password.setText(user.getPassword());
		if (!CommonUtils.isEmpty(user.getUserName())
				&& !CommonUtils.isEmpty(user.getPassword()))
			login();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		username = (ChangeColorEditText) findViewById(R.id.username);
		password = (ChangeColorEditText) findViewById(R.id.password);
		btn_login = (Button) findViewById(R.id.btn_login);
		setURLIv = (ImageView) findViewById(R.id.setURLIv);

		password.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		btn_login.setOnClickListener(this);
		setURLIv.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_login:
			login();
			break;
		case R.id.setURLIv:
			// 设置服务器
			new LoginDialog(this, R.style.Login_dialog).show();
			break;
		default:
			break;
		}
	}

	/**
	 * 登录执行
	 */
	private void login() {
		// TODO Auto-generated method stub
		etUserNamestr = username.getText().toString().trim();
		if (CommonUtils.isEmpty(etUserNamestr)) {
			toastMsg("用户名不能为空!");
			return;
		}
		etPasswordstr = password.getText().toString().trim();
		if (CommonUtils.isEmpty(etPasswordstr)) {
			toastMsg("密码不能为空!");
			return;
		}
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		AjaxParams params = new AjaxParams();
		params.put("userAccount", etUserNamestr);
		params.put("loginPassword", EncroptionUtils.EncryptSHA(etPasswordstr));
		params.put("iemi", KingTellerApplication.getDeviceToken());

		KTHttpClient fh = new KTHttpClient(false);
		fh.post(ConfigUtils.CreateUrl(this, URLConfig.LoginUrl), params,
				new AjaxHttpCallBack<LoginBean>(this, false) {

					@Override
					public void onStart() {
						// 开始http请求的时候回调
						KingTellerProgress.showProgress(LoginActivity.this,
								"登录中，请稍候...");
					}

					@Override
					public void onFinish() {
						KingTellerProgress.closeProgress();
					}

					@Override
					public void onDo(LoginBean data) {
						if (data.getLoginError() == 1) {
							// 保存用户信息
							data.setUsername(etUserNamestr);
							data.setPassword(etPasswordstr);
							User.SaveInfo(LoginActivity.this, data);

							// 保存回话cookie
							KingTellerApplication
									.getApplication()
									.setAccessToken(
											CommonUtils
													.getAuthCookie(onGetHeader("Set-Cookie")));
							
							if (!isfrom) {
								startActivity(new Intent(LoginActivity.this,
										MainActivity.class));
							}
							finish();

						} else {
							toastMsg(data.getLoginState());
						}
					};

				});

	}

}
