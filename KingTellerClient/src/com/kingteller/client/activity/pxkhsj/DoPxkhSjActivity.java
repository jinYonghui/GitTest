package com.kingteller.client.activity.pxkhsj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.pxkh.AnswerParamBean;
import com.kingteller.client.bean.pxkh.JdtParamBean;
import com.kingteller.client.bean.pxkh.KjBackDataBean;
import com.kingteller.client.bean.pxkh.QuestionParamBean;
import com.kingteller.client.bean.pxkh.XzParamBean;
import com.kingteller.client.bean.pxkh.XzTkParamBean;
import com.kingteller.client.bean.workorder.ReturnBackStatus;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.ChoiceQuestionView;
import com.kingteller.client.view.CompleteQuestionView;
import com.kingteller.client.view.GroupListViewQuestions;
import com.kingteller.client.view.KTAlertDialog;
import com.kingteller.client.view.LineEditText;
import com.kingteller.client.view.ShortAnswerQuestionView;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

@SuppressLint("HandlerLeak")
public class DoPxkhSjActivity extends BaseFragmentActivity implements
		OnClickListener {

	private int minute = -1;
	private int second = -1;
	private Timer timer;
	private TimerTask timerTask;
	private TextView countDown;
	private TextView fristQuestion;
	private TextView secondQuestion;
	private TextView thirdQuestion;
	private GroupListViewQuestions choiceQuestions;
	private GroupListViewQuestions completionQuestions;
	private GroupListViewQuestions shortAnswerQuestions;
	private Button btn_submit;
	private String paperId;
	private int timeLong;
	private List<XzTkParamBean> xztkList = new ArrayList<XzTkParamBean>();
	private List<AnswerParamBean> answerList = new ArrayList<AnswerParamBean>();
	private String xztCore="";
	private String tktCore="";
	private String jdtCore="";

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (minute == 0) {
				if (second == 0) {
					countDown.setText("时间到 !");
					submitData(getJsonData());

					if (timer != null) {
						timer.cancel();
						timer = null;
					}
					if (timerTask != null) {
						timerTask = null;
					}
				} else {
					second--;
					if (second >= 10) {
						countDown.setText("0" + minute + "分" + second+"秒");
					} else {
						countDown.setText("0" + minute + "分0" + second+"秒");
					}
				}
			} else {
				if (second == 0) {
					second = 59;
					minute--;
					if (minute >= 10) {
						countDown.setText(minute + "分" + second+"秒");
					} else {
						countDown.setText("0" + minute + "分" + second+"秒");
					}
				} else {
					second--;
					if (second >= 10) {
						if (minute >= 10) {
							countDown.setText(minute + "分" + second+"秒");
						} else {
							countDown.setText("0" + minute + "分" + second+"秒");
						}
					} else {
						if (minute >= 10) {
							countDown.setText(minute + "分0" + second+"秒");
						} else {
							countDown.setText("0" + minute + "分0" + second+"秒");
						}
					}
				}
			}
		};
	};

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_dopxkhsj);
		initUI();
		initData();
		changeUI();
		
	}

	private void initUI() {
		
		title_title.setText("专项考核卷(满分100分)");
		title_left.setVisibility(View.GONE);

		fristQuestion = (TextView) findViewById(R.id.fristQuestion);
		secondQuestion = (TextView) findViewById(R.id.secondQuestion);
		thirdQuestion = (TextView) findViewById(R.id.thirdQuestion);
		countDown = (TextView) findViewById(R.id.countDown);

		choiceQuestions = (GroupListViewQuestions) findViewById(R.id.choice_questions);
		completionQuestions = (GroupListViewQuestions) findViewById(R.id.completion_questions);
		shortAnswerQuestions = (GroupListViewQuestions) findViewById(R.id.short_answer_questions);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);

		paperId = getIntent().getStringExtra("paperId");
		timeLong = Integer.parseInt(getIntent().getStringExtra("timeLong"));

	}

	private void initData() {
		getXztQuestions();
		getTktQuestions();
		getJdtQuestions();
	}

  
	
	private void changeUI() {	
		
		minute = timeLong;
		second = 0;
		countDown.setText(minute + ":" + second);

		timerTask = new TimerTask() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		};

		timer = new Timer();
		timer.schedule(timerTask, 0, 1000);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_submit:
			new KTAlertDialog.Builder(DoPxkhSjActivity.this)
			.setTitle("提示")
			.setMessage("您是否提交？")
			.setPositiveButton("确定",
					new KTAlertDialog.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialogInterface,
								int pos) {
							submitData(getJsonData());
							dialogInterface.dismiss();
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
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return false;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask = null;
		}
		minute = -1;
		second = -1;
	}

	private void submitData(String assign) {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("assign", assign);

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.TjsjUrl), params,
				new AjaxHttpCallBack<ReturnBackStatus>(this,
						new TypeToken<ReturnBackStatus>() {
						}.getType(), true) {

					@Override
					public void onDo(ReturnBackStatus data) {
						toastMsg(data.getMessage());
						if (data.getResult().equals("success")) {
							finish();
						}
					};
				});
	}

	private void getXztQuestions() {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("paperId", paperId);
		params.put("userId", User.getInfo(this).getUserId());

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.XztUrl), params,
				new AjaxHttpCallBack<BasePageBean<XzParamBean>>(this,
						new TypeToken<BasePageBean<XzParamBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(BasePageBean<XzParamBean> data) {

						if(data.getList().get(0).getQuestions().size()>0){
							fristQuestion.setText("选择题" + "(每小题"
									+ data.getList().get(0).getCore() + "分)");
							xztCore = data.getList().get(0).getCore();
							for (int i = 0; i < data.getList().size(); i++) {
								xztkList = data.getList().get(i).getQuestions();
								setDataXztInfo(xztkList);
							}
						}
					};
				});
	}

	private void getTktQuestions() {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("paperId", paperId);
		params.put("userId", User.getInfo(this).getUserId());

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.TktUrl), params,
				new AjaxHttpCallBack<BasePageBean<XzParamBean>>(this,
						new TypeToken<BasePageBean<XzParamBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(BasePageBean<XzParamBean> data) {

						if(data.getList().get(0).getQuestions().size()>0){
							secondQuestion.setText("填空题" + "(每空"
									+ data.getList().get(0).getCore() + "分)");

							tktCore = data.getList().get(0).getCore();
							for (int i = 0; i < data.getList().size(); i++) {
								xztkList = data.getList().get(i).getQuestions();
								setDataTktInfo(xztkList);
							}
						}
						
					};
				});
	}

	private void getJdtQuestions() {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("paperId", paperId);
		params.put("userId", User.getInfo(this).getUserId());

		fh.post(ConfigUtils.CreateUrl(this, URLConfig.JdtUrl), params,
				new AjaxHttpCallBack<BasePageBean<JdtParamBean>>(this,
						new TypeToken<BasePageBean<JdtParamBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
					}

					@Override
					public void onDo(BasePageBean<JdtParamBean> data) {
						if(data.getList().get(0).getQuestions().size()>0){
							thirdQuestion.setText("简答题" + "(每题"+ data.getList().get(0).getCore() + "分)");
							jdtCore = data.getList().get(0).getCore();
							setDataJdtInfo(data.getList());
						}
						
					};
				});
	}

	private void setDataXztInfo(List<XzTkParamBean> data) {

		ChoiceQuestionView fview = null;
		for (int i = 0; i < data.size(); i++) {
			fview = new ChoiceQuestionView(this);
			fview.setData(data.get(i), i + 1);
			choiceQuestions.addView(fview);
		}
	}

	private void setDataTktInfo(List<XzTkParamBean> data) {
		CompleteQuestionView cview = null;
		for (int i = 0; i < data.size(); i++) {
			cview = new CompleteQuestionView(this);
			cview.setData(data.get(i).getQuest(), i + 1);
			completionQuestions.addView(cview);
		}
	}

	private void setDataJdtInfo(List<JdtParamBean> data) {
		ShortAnswerQuestionView saview = null;
		List<QuestionParamBean> questions = data.get(0).getQuestions();
		for (int i = 0; i < questions.size(); i++) {
			saview = new ShortAnswerQuestionView(this);
			saview.setData(questions.get(i), i + 1);
			shortAnswerQuestions.addView(saview);
		}
	}

	public String getJsonData() {
		List<KjBackDataBean> xztInfoList = getDataXztInfo();
		List<KjBackDataBean> tktInfoList = getDataTktInfo();
		List<KjBackDataBean> jdtInfoList = getDataJdtInfo();

		HashMap<String, Object> params = new HashMap<String, Object>();
		
		params.put("xztCore", CommonUtils.isEmpty(xztCore)?"":xztCore);
		params.put("tktCore", CommonUtils.isEmpty(tktCore)?"":tktCore);
		params.put("jdtCore", CommonUtils.isEmpty(jdtCore)?"":jdtCore);
		params.put("paperId", paperId);
		params.put("userId", User.getInfo(this).getUserId());
		params.put("xztList", xztInfoList);
		params.put("tktList", tktInfoList);
		params.put("jdtList", jdtInfoList);
		return ConditionUtils.getJsonFromHashMap(params);
	}

	private List<KjBackDataBean> getDataXztInfo() {
		List<KjBackDataBean> kbdbList = new ArrayList<KjBackDataBean>();
		KjBackDataBean kbdb = null;
		int count = choiceQuestions.getChildCount();
		for (int i = 1; i < count; i++) {
			String id = ((TextView) choiceQuestions.getChildAt(i).findViewById(R.id.contentId)).getText().toString();
			kbdb = new KjBackDataBean();
			kbdb.setId(id);
			ListView listview = (ListView) choiceQuestions.getChildAt(i).findViewById(R.id.listView2);
			int count1 = listview.getChildCount();
			List<String> strList = new ArrayList<String>();
			String str = null;
			for (int j = 0; j < count1; j++) {
				CheckBox radioButton = (CheckBox) listview.getChildAt(j).findViewById(R.id.radioButton);
				if (radioButton.isChecked()) {
					str = ((TextView) listview.getChildAt(j).findViewById(R.id.choiceId)).getText().toString();
					strList.add(str);
				}
			}
			if (strList.size() == 1) {
				kbdb.setValue(strList.get(0));
			} else if (strList.size() > 1) {
				StringBuffer strBuffer = new StringBuffer();
				for (int k = 0; k < strList.size(); k++) {
					strBuffer.append(strList.get(k));
					if (k != strList.size() - 1) {
						strBuffer.append(",");
					}
				}
				kbdb.setValue(strBuffer.toString());
			} else {
				kbdb.setValue("");
			}
			kbdbList.add(kbdb);
		}
		
		return kbdbList;
	}

	private List<KjBackDataBean> getDataTktInfo() {
		List<KjBackDataBean> kbdbList = new ArrayList<KjBackDataBean>();
		KjBackDataBean kbdb = null;
		int count = completionQuestions.getChildCount();
		for (int i = 1; i < count; i++) {
			ListView listview = (ListView) completionQuestions.getChildAt(i)
					.findViewById(R.id.listView);
			int count1 = listview.getChildCount();
			for (int j = 0; j < count1; j++) {
				kbdb = new KjBackDataBean();
				String id = ((TextView) listview.getChildAt(j).findViewById(
						R.id.completeId)).getText().toString();
				String value = ((LineEditText) listview.getChildAt(j)
						.findViewById(R.id.myEdit)).getText().toString();
				kbdb.setId(id);
				kbdb.setValue(value);
				kbdbList.add(kbdb);
			}
		}
		return kbdbList;
	}

	private List<KjBackDataBean> getDataJdtInfo() {
		List<KjBackDataBean> kbdbList = new ArrayList<KjBackDataBean>();
		KjBackDataBean kbdb = null;
		int count = shortAnswerQuestions.getChildCount();
		for (int i = 1; i < count; i++) {
			kbdb = new KjBackDataBean();
			String id = ((TextView) shortAnswerQuestions.getChildAt(i)
					.findViewById(R.id.shortAnswerId)).getText().toString();
			String value = ((EditText) shortAnswerQuestions.getChildAt(i)
					.findViewById(R.id.saEditText)).getText().toString();
			kbdb.setId(id);
			kbdb.setValue(value);
			kbdbList.add(kbdb);
		}
		return kbdbList;
	}
}
