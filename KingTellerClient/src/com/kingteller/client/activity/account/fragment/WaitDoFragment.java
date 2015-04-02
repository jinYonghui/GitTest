package com.kingteller.client.activity.account.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.account.MainActivity;
import com.kingteller.client.activity.base.BaseFragment;
import com.kingteller.client.activity.common.CommonListActivity;
import com.kingteller.client.activity.common.CommonWebViewActivity;
import com.kingteller.client.activity.workorder.AduitReportActivity;
import com.kingteller.client.activity.workorder.WorkOrderActivity;
import com.kingteller.client.adapter.WaitDoAdapter;
import com.kingteller.client.bean.account.Notice;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.account.WaitDoBean;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.FileUtils;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 待办事项的Fragment
 * @author 王定波
 *
 */
public class WaitDoFragment extends BaseFragment implements IXListViewListener,
		OnItemClickListener {

	private View mContentView;
	private Button title_title;
	private Context context;
	private WaitDoAdapter waitDoAdpater;
	private boolean isfirst = true;
	private List<WaitDoBean> waitlist = new ArrayList<WaitDoBean>();
	private KingTellerDb finalDb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContentView = inflater.inflate(R.layout.layout_waitdo, null);
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

	private void initUI() {
		// TODO Auto-generated method stub
		title_title = (Button) mContentView.findViewById(R.id.button_middle);
		title_title.setText("提醒");
		waitDoAdpater = new WaitDoAdapter(context, waitlist);

		
		getListviewObj().getListview().setAdapter(waitDoAdpater);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(false);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);
		getListviewObj().setOnClickLister(LoadingEnum.NODATA, res);
		getListviewObj().setOnClickLister(LoadingEnum.NETERROR, res);

	}

	private OnClickListener res = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			getListviewObj().setStatus(LoadingEnum.LOADING);
			onRefresh();
		}
	};

	private void initData() {
		// TODO Auto-generated method stub
		// getWaitDos();
		finalDb = KingTellerDb.create(context);
		if (isfirst) {
			isfirst = false;
		} else {
			if (waitlist.size() > 0)
				getListviewObj().setStatus(LoadingEnum.LISTSHOW);
		}
	}

	private void getWaitDos() {
		// TODO Auto-generated method stub
		User user = User.getInfo(context);
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("userAccount", user.getUserName());
		params.put("orgId", user.getOrgId());
		params.put("roleCode", user.getRoleCode());
		params.put("userId", user.getUserId());

		fh.post(ConfigUtils.CreateUrl(context, URLConfig.WaitDoUrl), params,
				new AjaxHttpCallBack<List<WaitDoBean>>(context,
						new TypeToken<List<WaitDoBean>>() {
						}.getType(), true) {

					@Override
					public void onSuccess(String response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						try {
							FileUtils.writeFile(KingTellerConfig.WATIDOFILE,
									response);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
					}

					@Override
					public void onError(int errorNo, String strMsg) {
						// TODO Auto-generated method stub
						try {
							super.onError(errorNo, strMsg);
							getListviewObj().setStatus(LoadingEnum.NETERROR,
									getString(R.string.net_error_try));
						} catch (Exception e) {
							// TODO: handle exception
						}

					}

					@Override
					public void onDo(List<WaitDoBean> data) {
						waitlist = data;
						waitDoAdpater.setLists(data);
						if (data.size() > 0) {
							getListviewObj().setStatus(LoadingEnum.LISTSHOW);
							KingTellerUtils.MainUpdateStatus(context,
									MainActivity.POSITION_WAITDOFRAGMENT,
									false, true);
						} else {
							getListviewObj().setStatus(LoadingEnum.NODATA,
									getString(R.string.no_data_try));
							KingTellerUtils.MainUpdateStatus(context,
									MainActivity.POSITION_WAITDOFRAGMENT,
									false, false);
						}
					};

				});

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getWaitDos();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		try {
			super.onResume();
			getWaitDos();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 设置是否已经读了
	 * 
	 * @param data
	 */
	private void setRead(WaitDoBean data) {
		Notice nd = new Notice();
		nd.setId(data.getOnlyId());
		Notice datatmp = finalDb.findById(nd.getId(), Notice.class);
		if (datatmp == null) {
			finalDb.save(nd);
			waitDoAdpater.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int postion,
			long arg3) {
		// TODO Auto-generated method stub
		WaitDoBean data = (WaitDoBean) view.getAdapter().getItem(postion);
		if (data != null) {
			// 点击添加到数据库
			setRead(data);

			if (!CommonUtils.isEmpty(data.getFlowCode())) {
				// 进入销售下单
				if (data.getFlowCode().equals("saleBillFlowNoContract")
						|| data.getFlowCode().equals("saleBillFlowContracted")) {

					if (!CommonUtils.isEmpty(data.getToDoTaskUrl())) {
						Intent intent = new Intent(context,
								CommonWebViewActivity.class);
						String url = data.getToDoTaskUrl().substring(0,
								data.getToDoTaskUrl().indexOf("?"));
						intent.putExtra("id", data.getBusId());
						intent.putExtra("flowCode", data.getFlowCode());

						intent.putExtra(CommonWebViewActivity.URL,
								ConfigUtils.CreateLocalUrl(context, url));
						intent.putExtra(CommonWebViewActivity.TITLE,
								data.getTitle());
						if (url.indexOf("my_sales_view.html") != -1) {
							intent.putExtra(CommonWebViewActivity.TITLE,
									"我的销售单");
						} else {
							intent.putExtra(CommonWebViewActivity.TITLE,
									"销售单审核");
						}
						startActivity(intent);
					} else if (!CommonUtils.isEmpty(data.getBeanName())) {

						Intent intent = new Intent(context,
								CommonListActivity.class);
						intent.putExtra(CommonListActivity.TITLE,
								data.getFlowTitle());
						intent.putExtra("data", data);
						startActivity(intent);
					} else {
						toastMsg("该功能正在建设中，敬请关注！");
					}

				}
				// 进入工单列表
				else if (data.getFlowCode().equals("logisticsFlow")
						|| data.getFlowCode().equals("otherMatter")
						|| data.getFlowCode().equals("assignOrder")) {
					startActivity(new Intent(context, WorkOrderActivity.class));
				}
				// 报告审核 workReportFlow,logisticsReportFlow,otherReportFlow
				else if (data.getFlowCode().indexOf("ReportFlow")!=-1) {
					if(data.getActCode().equals("act0")){
						Intent intent = new Intent();
						intent.putExtra("reportFlowCode", "reportFlowCode");
						intent.setClass(context, WorkOrderActivity.class);
						startActivity(intent);
						//startActivity(new Intent(context, WorkOrderActivity.class));
					}else if(data.getActCode().equals("act1")){
						startActivity(new Intent(context, AduitReportActivity.class));
					}
					//toastMsg("该功能尚在改进中,请关注");
				}

			} else {
				// flowcode为空的情况
				if (!CommonUtils.isEmpty(data.getBusId())) {
					Intent intent = new Intent(context,
							CommonWebViewActivity.class);
					intent.putExtra(
							CommonWebViewActivity.URL,
							ConfigUtils.CreateUrl(
									context,
									String.format(URLConfig.OtherBusIdUrl,
											data.getBusId())));
					intent.putExtra(CommonWebViewActivity.TITLE,
							data.getFlowTitle());
					startActivity(intent);
				} else if (!CommonUtils.isEmpty(data.getToDoTaskUrl())) {
					Intent intent = new Intent(context,
							CommonWebViewActivity.class);
					intent.putExtra(
							CommonWebViewActivity.URL,
							ConfigUtils.CreateUrl(context,
									data.getToDoTaskUrl()));
					intent.putExtra(CommonWebViewActivity.TITLE,
							data.getFlowTitle());
					startActivity(intent);
				} else if (!CommonUtils.isEmpty(data.getBeanName())) {
					// 合起来的工单
					Intent intent = new Intent(context,
							CommonListActivity.class);
					intent.putExtra("data", data);
					intent.putExtra(CommonListActivity.TITLE,
							data.getFlowTitle());
					startActivity(intent);
				} else
					toastMsg("该功能正在建设中，敬请关注！");

			}
		}
	}
}
