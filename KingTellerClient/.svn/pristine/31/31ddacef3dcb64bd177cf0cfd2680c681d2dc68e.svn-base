package com.kingteller.client.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kingteller.R;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 登陆弹出对话框
 * @author 王定波
 *
 */
public class LoginDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText address, port;
	private Button confim, cancel;

	public LoginDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoginDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_dialog);
		initUI();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	private void initUI() {
		// TODO Auto-generated method stub
		confim = (Button) this.findViewById(R.id.sure);
		cancel = (Button) this.findViewById(R.id.no);

		address = (EditText) this.findViewById(R.id.address);
		port = (EditText) this.findViewById(R.id.port);
		
		address.setText(ConfigUtils.getIpDomain(getContext()));
		port.setText(ConfigUtils.getPort(getContext()));

		confim.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.sure:
			String addressstr=address.getText().toString().trim();
			String portstr=port.getText().toString().trim();
			if(CommonUtils.isEmpty(addressstr))
			{
				toastMsg("服务器不能为空");
				return;
			}
			
			if(CommonUtils.isEmpty(portstr))
			{
				toastMsg("端口不能为空");
				return;
			}
			ConfigUtils.setIpDomain(getContext(), addressstr);
			ConfigUtils.setPort(getContext(), portstr);
			
			dismiss();
			break;
		case R.id.no:
			dismiss();
			break;
		default:
			break;
		}

	}
	
	public void toastMsg(String str) {
		Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
	}
}
