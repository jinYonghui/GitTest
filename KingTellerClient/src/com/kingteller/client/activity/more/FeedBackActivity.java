package com.kingteller.client.activity.more;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.bean.common.BaseBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerProgress;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 意见反馈
 * @author 王定波
 *
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {

	private EditText feedback_edittext;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_feedback);
		initUI();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("意见反馈");
		title_left.setOnClickListener(this);
		title_right.setBackgroundResource(R.drawable.icon_right);
		title_right.setOnClickListener(this);
		feedback_edittext = (EditText) findViewById(R.id.feedback_edittext);
	}

	private void submitFeedback() {

		if (CommonUtils.isEmpty(feedback_edittext.getText().toString())) {
			toastMsg("反馈内容不能为空");
			return;
		}

		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("没有网络，请检查网络是否可用");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("content", feedback_edittext.getText().toString());

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.SubmitFeedBackUrl),
				params, new AjaxHttpCallBack<BaseBean>(this, true) {
					@Override
					public void onDo(BaseBean data) {
						// TODO Auto-generated method stub
						if (data.getStatus().equals("1")) {
							toastMsg("数据提交成功");
							finish();
						} else {
							toastMsg("数据提交失败:" + data.getMsg());
						}

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						KingTellerProgress.closeProgress();
					}

					@Override
					public void onStart() {
						// 开始http请求的时候回调
						KingTellerProgress.showProgress(FeedBackActivity.this,
								"正在提交数据...");
					}

				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.button_right:
			submitFeedback();
			break;
		default:
			break;
		}
	}
}
