package com.kingteller.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.adapter.FloatWaitDoAdapter;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.account.WaitDoBean;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.FileUtils;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class FloatWindowView extends LinearLayout {

	private ListViewObj listviewObj;
	private FloatWaitDoAdapter adpater;
	private Gson gson;

	public FloatWindowView(Context context) {
		super(context);
		LayoutInflater.from(context)
				.inflate(R.layout.layout_float_window, this);

		initUI();
		initData();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		listviewObj = new ListViewObj(this);
		findViewById(R.id.list_loading_view).setBackgroundResource(
				R.color.transparent);
		adpater = new FloatWaitDoAdapter(getContext(),
				new ArrayList<WaitDoBean>());
		listviewObj.getListview().setAdapter(adpater);
		listviewObj.getListview().setRefreshEnabled(false);
		listviewObj.getListview().setLoadMoreEnabled(false);

		findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				setVisibility(GONE);
			}
		});

	}

	private void initData() {
		gson = new Gson();
		// TODO Auto-generated method stub
		if (FileUtils.FileExist(KingTellerConfig.WATIDOFILE)) {
			String data = FileUtils.readFile(KingTellerConfig.WATIDOFILE);
			if (!CommonUtils.isEmpty(data)) {
				try {
					List<WaitDoBean> list = gson.fromJson(data,
							new TypeToken<List<WaitDoBean>>() {
							}.getType());
					adpater.setLists(list);

					if (list.size() > 0)
						listviewObj.setStatus(LoadingEnum.LISTSHOW);
					else
						listviewObj.setStatus(LoadingEnum.NODATA);
				} catch (Exception e) {
					// TODO: handle exception
					listviewObj.setStatus(LoadingEnum.NODATA);
				}

			} else {
				listviewObj.setStatus(LoadingEnum.NODATA);
			}

		} else {

		}
	}

	public void getWaitDos() {
		// TODO Auto-generated method stub
		User user = User.getInfo(getContext());
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("userAccount", user.getUserName());
		params.put("orgId", user.getOrgId());
		params.put("roleCode", user.getRoleCode());
		params.put("userId", user.getUserId());

		fh.post(ConfigUtils.CreateUrl(getContext(), URLConfig.WaitDoUrl),
				params, new AjaxHttpCallBack<List<WaitDoBean>>(
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
					public void onDo(List<WaitDoBean> data) {
						adpater.setLists(data);
						if (data.size() > 0) {
							listviewObj.setStatus(LoadingEnum.LISTSHOW);
						} else {
							listviewObj.setStatus(LoadingEnum.NODATA);
						}
					};

				});

	}

}
