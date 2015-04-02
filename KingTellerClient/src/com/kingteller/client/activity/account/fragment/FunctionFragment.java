package com.kingteller.client.activity.account.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragment;
import com.kingteller.client.activity.common.CommonListActivity;
import com.kingteller.client.activity.common.CommonWebViewActivity;
import com.kingteller.client.activity.knowledge.KnowledgeActivity;
import com.kingteller.client.activity.logisticmonitor.WLJKMainActivity;
import com.kingteller.client.activity.map.MapMainActivity;
import com.kingteller.client.activity.pxkhsj.DoPxkhsjListActivity;
import com.kingteller.client.activity.workorder.AduitReportActivity;
import com.kingteller.client.activity.workorder.SendOrderListActivity;
import com.kingteller.client.activity.workorder.WorkOrderActivity;
import com.kingteller.client.adapter.FunctionAdapter;
import com.kingteller.client.bean.account.FunctionBean;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.account.WaitDoBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 功能Fragment
 * @author 王定波
 *
 */
public class FunctionFragment extends BaseFragment implements
		OnItemClickListener {

	private View mContentView;
	private Button title_title;
	private Context context;
	private GridView gridview;
	private FunctionAdapter adapter;
	private boolean isfirst = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContentView = inflater.inflate(R.layout.layout_function, null);
		context = inflater.getContext();
		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initUI();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub

		if (isfirst) {
			isfirst = false;
		} else {

		}
	}

	private void initUI() {
		// TODO Auto-generated method stub
		title_title = (Button) mContentView.findViewById(R.id.button_middle);
		title_title.setText("我的工作");
		gridview = (GridView) mContentView.findViewById(R.id.gridview);
		adapter = new FunctionAdapter(context,
				User.getInfo(context).getRight(), FunctionAdapter.MAINMENU);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(this);

		getListviewObj().setVisibility(View.GONE);
		gridview.setVisibility(View.VISIBLE);
	}

	private void getRights() {
		// TODO Auto-generated method stub
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("userAccount", User.getInfo(context).getUserName());

		fh.post(ConfigUtils.CreateUrl(context, URLConfig.GetRightUrl), params,
				new AjaxHttpCallBack<FunctionBean>(context, true) {

					@Override
					public void onFinish() {

					}

					@Override
					public void onDo(FunctionBean data) {
						// TODO Auto-generated method stub
						User user = User.getInfo(context);
						user.setRight(data.getContent());
						User.SaveInfo(context, user);
						adapter.setData(data.getContent());
						adapter.notifyDataSetChanged();
					}

				});

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int postion,
			long arg3) {
		// TODO Auto-generated method stub
		User user = User.getInfo(context);
		String action = (String) adapterView.getAdapter().getItem(postion);
		String weburl = "";

		if (action.equals("MOBILE_JNKH")) {
			weburl = ConfigUtils.CreateUrl(context, URLConfig.WebJnkhUrl);
		} else if (action.equals("MOBILE_RWD")) {
			// weburl = ConfigUtils.CreateUrl(context, URLConfig.WebRwdUrl);
			startActivity(new Intent(context, WorkOrderActivity.class));
		}else if(action.equals("MOBILE_ASSIGN")){
			startActivity(new Intent(context,SendOrderListActivity.class));
		} else if (action.equals("MOBILE_ZSK")) {
			//weburl = ConfigUtils.CreateUrl(context, URLConfig.WebZskhUrl);
			startActivity(new Intent(context, KnowledgeActivity.class));
		} else if (action.equals("MOBILE_SPBG")) {
			//weburl = ConfigUtils.CreateUrl(context, URLConfig.WebSpbgUrl);
			startActivity(new Intent(context,AduitReportActivity.class));
		} else if (action.equals("saleshen")) {
			weburl = ConfigUtils.CreateLocalUrl(context, URLConfig.WebShUrl);
		} else if (action.equals("mysale")) {
			weburl = ConfigUtils
					.CreateLocalUrl(context, URLConfig.WebMysaleUrl);
		} else if (action.equals("MOBILE_PROJECTSTATISTIC")) {
			weburl = ConfigUtils.CreateLocalUrl(context, URLConfig.ProjectUrl);
		} else if (action.equals("MOBILE_PROJECTORDERAUDIT")) {
			weburl = ConfigUtils.CreateLocalUrl(context,
					URLConfig.ProjectAuditUrl);
		}

		else if (action.equals("MOBILE_PROJECTDEALORDER")) {
			weburl = ConfigUtils.CreateLocalUrl(context,
					URLConfig.ProjectDEALORDERUrl);
		}

		else if (action.equals("notice")) {
			WaitDoBean data = new WaitDoBean();
			data.setFlag("notice");
			data.setBeanName("noticeService.queryMobileList");
			Intent intent = new Intent(context, CommonListActivity.class);
			intent.putExtra("data", data);
			startActivity(intent);
		} else if (action.equals("MOBILE_MAP")) {
			startActivity(new Intent(context, MapMainActivity.class));
		}else if(action.equals("WLJK_MOBILE")) {
			startActivity(new Intent(context, WLJKMainActivity.class));
		}else if(action.equals("MOBILE_PXKHSJ")){
			startActivity(new Intent(context, DoPxkhsjListActivity.class));
		}else {
			toastMsg("功能尚未开放，请关注");
		}

		if (!CommonUtils.isEmpty(weburl)) {
			Intent intent = new Intent(context, CommonWebViewActivity.class);
			intent.putExtra(
					CommonWebViewActivity.TITLE,
					getString(getResources().getIdentifier(action, "string",
							context.getPackageName())));
			intent.putExtra(
					CommonWebViewActivity.URL,
					String.format(weburl, user.getUserName(),
							user.getPassword()));
			startActivity(intent);
		}
	}

	public void setData() {
		try {
			super.onResume();
			getRights();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
