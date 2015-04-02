package com.kingteller.client.activity.account.fragment;

import java.io.UnsupportedEncodingException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.base.BaseFragment;
import com.kingteller.client.activity.more.AboutUsActivity;
import com.kingteller.client.activity.more.FeedBackActivity;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.workorder.LoadNewDataBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.service.KingTellerService;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.SQLiteHelper;
import com.kingteller.client.utils.UpdateUtils;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.framework.http.AjaxCallBack;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.modules.zxing.core.CaptureActivity;

/**
 * 更多设置的Fragment
 * @author 王定波
 *
 */
public class MoreFragment extends BaseFragment implements OnClickListener {

	private View mContentView;
	private Button title_title;
	private Context context;
	private ToggleButton switch_zd_push;
	private ToggleButton switch_sy_push;
	private ImageView icon_app_new;
	private TextView text_username;
	private TextView text_name;
	private TextView text_department;
	private TextView text_service_ip;
	private TextView text_port;
	private TextView text_push_status;
	private TextView app_version;
	private ToggleButton switch_lock_screen;
	private String[] maxTime;
	private SQLiteHelper helper;
	private ImageView icon_database_new;
	private static boolean onClick = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContentView = inflater.inflate(R.layout.layout_more, null);
		context = inflater.getContext();
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initUI();
		initData();
		
		helper = new SQLiteHelper(context);
		maxTime = helper.getMaxTimeArray();
		
		//sjkgx(false);
	}

	private void initData() {
		// TODO Auto-generated method stub
		User user = User.getInfo(context);
		switch_sy_push.setChecked(ConfigUtils.getConfigMeta(
				ConfigUtils.KEY_PUSH_SY, true));
		switch_zd_push.setChecked(ConfigUtils.getConfigMeta(
				ConfigUtils.KEY_PUSH_ZD, true));
		switch_lock_screen.setChecked(ConfigUtils.getConfigMeta(
				ConfigUtils.KEY_LOCK_SCREEN, true));
		switch_sy_push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConfigUtils.setConfigMeta(ConfigUtils.KEY_PUSH_SY,
						switch_sy_push.isChecked());

			}
		});

		switch_zd_push.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConfigUtils.setConfigMeta(ConfigUtils.KEY_PUSH_ZD,
						switch_zd_push.isChecked());

			}
		});

		switch_lock_screen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConfigUtils.setConfigMeta(ConfigUtils.KEY_LOCK_SCREEN,
						switch_lock_screen.isChecked());

			}
		});

		icon_app_new.setVisibility(ConfigUtils.getConfigMeta(
				ConfigUtils.KEY_APP_NEW, false) ? View.VISIBLE : View.GONE);

		text_username.setText(user.getUserName());
		text_name.setText(user.getName());
		text_department.setText(user.getOrgName());
		text_service_ip.setText(ConfigUtils.getIpDomain(context));
		text_port.setText(ConfigUtils.getPort(context));
		text_push_status.setText(user.isPushReg() ? "成功" : "失败");
		
		app_version.setText(UpdateUtils.getCurrentVersionName(context));

	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title = (Button) mContentView.findViewById(R.id.button_middle);
		title_title.setText("系统设置");
		mContentView.findViewById(R.id.aboutus_field).setOnClickListener(this);
		mContentView.findViewById(R.id.feedback_field).setOnClickListener(this);
		
		mContentView.findViewById(R.id.check_databases).setOnClickListener(this);

		mContentView.findViewById(R.id.update_field).setOnClickListener(this);
		mContentView.findViewById(R.id.button_loginout)
				.setOnClickListener(this);
		switch_zd_push = (ToggleButton) mContentView
				.findViewById(R.id.switch_zd_push);
		switch_sy_push = (ToggleButton) mContentView
				.findViewById(R.id.switch_sy_push);
		switch_lock_screen = (ToggleButton) mContentView
				.findViewById(R.id.switch_lock_screen);

		text_username = (TextView) mContentView
				.findViewById(R.id.text_username);
		text_name = (TextView) mContentView.findViewById(R.id.text_name);
		text_department = (TextView) mContentView
				.findViewById(R.id.text_department);
		text_service_ip = (TextView) mContentView
				.findViewById(R.id.text_service_ip);
		text_port = (TextView) mContentView.findViewById(R.id.text_port);
		text_push_status = (TextView) mContentView
				.findViewById(R.id.text_push_status);
		
		app_version = (TextView) mContentView.findViewById(R.id.app_version);

		icon_app_new = (ImageView) mContentView.findViewById(R.id.icon_app_new);
		
		icon_database_new = (ImageView) mContentView.findViewById(R.id.icon_database_new);

		//mContentView.findViewById(R.id.erweima_field).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.aboutus_field:
			startActivity(new Intent(context, AboutUsActivity.class));
			break;
		case R.id.update_field:
			UpdateUtils.CheckUpdate(context, true);
			break;
		case R.id.feedback_field:
			startActivity(new Intent(context, FeedBackActivity.class));
			break;
		/*case R.id.erweima_field:
			startActivity(new Intent(context,CaptureActivity.class));
			break;*/
		case R.id.button_loginout:
			new KTAlertDialog.Builder(context)
					.setTitle("提示")
					.setMessage("您确定要注销并退出吗？")
					.setPositiveButton("确定",
							new KTAlertDialog.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int pos) {
									loginout();
									JPushInterface.stopPush(context);
									KingTellerApplication.getApplication()
											.exit(true);
									dialogInterface.dismiss();
									getActivity().finish();

									context.stopService(new Intent(
											KingTellerService.KingTellerServiceAction));

								}
							})
					.setNegativeButton("取消",
							new KTAlertDialog.OnClickListener() {
								public void onClick(
										DialogInterface dialogInterface,
										int paramAnonymousInt) {
									dialogInterface.dismiss();
								}
							}).create().show();
			break;
		case R.id.check_databases:
			sjkgx(true);
			onClick = false;
			icon_database_new.setVisibility(View.GONE);
			final ProgressDialog dialog = new ProgressDialog(context);
	        dialog.setCancelable(false);
	        dialog.setCanceledOnTouchOutside(false);
	        
	        dialog.setTitle("数据库");
	        dialog.show();
	        new Thread(new Runnable() {  
	        	  
	            @Override  
	            public void run() {  
	                // TODO Auto-generated method stub  
	                try {  
	                    Thread.sleep(2000);  
	                    dialog.cancel();  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }).start();  
			break;
		default:
			break;
		}

	}

	public void sjkgx(final boolean optType){
		if (!CommonUtils.isNetAvaliable(context)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("handleWdateStr", maxTime[0]);
		params.put("troubledateStr", maxTime[1]);
		params.put("workdateStr",maxTime[2]);
		params.put("wldateStr",maxTime[3] );
		params.put("treedateStr", maxTime[4]);

		fh.post(ConfigUtils.CreateUrl(context, URLConfig.SjkgxUrl), params,
				new AjaxHttpCallBack<LoadNewDataBean>(context,
						new TypeToken<LoadNewDataBean>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(LoadNewDataBean basePageBean)  {
						if(!optType){
							int databaseSize = 0;
							databaseSize = basePageBean.getHandleSubByList().size() + basePageBean.getTroubleRemarkAllInfo().size()+
									basePageBean.getWlInfoAll().size() + basePageBean.getTreeInfoAll().size() + 
									basePageBean.getWorkTypeHandleSubInfo().size();
							
							if(databaseSize > 0){
								icon_database_new.setVisibility(View.VISIBLE);
							}else{
								icon_database_new.setVisibility(View.GONE);
							}
						}else{
							try {
								helper.insertHandleSub(basePageBean.getHandleSubByList(),maxTime[0]);
								helper.insertTroubleRemark(basePageBean.getTroubleRemarkAllInfo(),maxTime[1]);
								helper.insertSmWlInfo(basePageBean.getWlInfoAll(),maxTime[3]);
								helper.insertSmWlTreeInfo(basePageBean.getTreeInfoAll(),maxTime[4]);
								helper.insertWorkTypeHandleSub(basePageBean.getWorkTypeHandleSubInfo(),maxTime[2]);
							} catch (SQLException e) {
								e.printStackTrace();
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
					};
				});
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		try {
			super.onResume();
			initData();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 注销session
	 */
	private void loginout() {

		KTHttpClient fh = new KTHttpClient(true);
		fh.post(ConfigUtils.CreateUrl(context, URLConfig.LoginoutUrl),
				new AjaxCallBack<String>() {
				});
	}
}
