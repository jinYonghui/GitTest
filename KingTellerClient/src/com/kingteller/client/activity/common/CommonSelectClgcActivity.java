package com.kingteller.client.activity.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.CommonSelectGtgjAdapter;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.CommonSelectGZBJ;
import com.kingteller.client.bean.workorder.BjWlBean;
import com.kingteller.client.bean.workorder.HandleSubBean;
import com.kingteller.client.bean.workorder.TroubleRemarkBean;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.utils.SQLiteHelper;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

public class CommonSelectClgcActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {

	private List<CommonSelectData> listData = new ArrayList<CommonSelectData>();
	private CommonSelectGtgjAdapter adapter;
	private String[] typedatas;
	private int type;
	private String eatra_data;
	public final static String TITLE = "title";
	public final static String TYPE = "type";
	public final static String DATA = "data";
	public final static String EXTRADATA = "extra_data";
	private final static int KEY_TITLE = 0;
	private final static int KEY_URL = 1;
	private Button submit, cancel;
	private Gson gson = new Gson();
	public static boolean ISCONTAINOTHER = false; 
	private KingTellerDb clgcdb = KingTellerUtils.create(this);
	
	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.common_list_view_gtgj);
		submit = (Button) findViewById(R.id.submit);
		cancel = (Button) findViewById(R.id.cancel);
		initUI();
		getData();
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		// TODO Auto-generated method stub
		type = getIntent().getIntExtra(TYPE, 0);
		typedatas = getResources().getStringArray(type);
		eatra_data = getIntent().getStringExtra(EXTRADATA);

		title_title.setText(typedatas[KEY_TITLE]);
		title_left.setOnClickListener(this);

		adapter = new CommonSelectGtgjAdapter(this, listData);
		getListviewObj().getListview().setAdapter(adapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(false);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);

		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	private void getData() {
		switch (type) {
		case R.array.clgc://
			/*String maintainDataCLGC = KingTellerUtils.getMaintainInfoClgcDataBase(clgcdb, eatra_data+"clgc");
			if(!CommonUtils.isEmpty(maintainDataCLGC) ){
				List<HandleSubBean> bjList = gson.fromJson(maintainDataCLGC, new TypeToken<List<HandleSubBean>>() {}.getType());
				BasePageBean<CommonSelectData> pagedata = new BasePageBean<CommonSelectData>();
				List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
				List<CommonSelectData> lists = gson.fromJson(maintainDataCLGC, new TypeToken<List<CommonSelectData>>() {
				}.getType());
				for (int i = 0; i < bjList.size(); i++) {
					CommonSelectData data = new CommonSelectData();
					data.setText(bjList.get(i).getHandleSub());
					data.setValue(bjList.get(i).getId());

					lists.add(data);
				}

				pagedata.setList(lists);
				setClgcData(lists);
			}else{
				getAllCLGC();
			}*/
			//getCLGC();
			getClgc();
			break;
		default:
			break;
		}
	}

	private void getAllCLGC() {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		String maintainDataGZMS = KingTellerUtils.getMaintainInfoDataBase(
				CommonSelectClgcActivity.this, eatra_data + "gzms");
		if (!CommonUtils.isEmpty(maintainDataGZMS)) {
			List<TroubleRemarkBean> trouList = gson.fromJson(maintainDataGZMS,
					new TypeToken<List<TroubleRemarkBean>>() {
					}.getType());

			for (int j = 0; j < trouList.size(); j++) {
				KTHttpClient fh = new KTHttpClient(true);
				AjaxParams params = new AjaxParams();
				params.put("pId", trouList.get(j).getId());

				final String pid = trouList.get(j).getId();
				fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]),
						params,
						new AjaxHttpCallBack<BasePageBean<HandleSubBean>>(this,
								new TypeToken<BasePageBean<HandleSubBean>>() {
								}.getType(), true) {

							@Override
							public void onFinish() {
								getListviewObj().getListview().stopRefresh();
								getListviewObj().getListview().stopLoadMore();
							}

							@Override
							public void onDo(
									BasePageBean<HandleSubBean> basePageBean) {
								List<CommonSelectData> commonSelects = new ArrayList<CommonSelectData>();
								for (int i = 0; i < basePageBean.getList().size(); i++) {
									CommonSelectData commonSelectData = new CommonSelectData();
									commonSelectData.setText(basePageBean
											.getList().get(i).getHandleSub());
									commonSelectData.setValue(basePageBean
											.getList().get(i).getId());
									commonSelects.add(commonSelectData);
								}

								KingTellerUtils.saveMaintainInfoClgcDataBase(clgcdb, pid + "clgc",
										gson.toJson(commonSelects).toString(),1);
							};

						});
			}

		}

	}
	
	private void getClgc(){
		
		String myPath = SQLiteHelper.DB_PATH+SQLiteHelper.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(myPath,null);
		//Cursor c = database.rawQuery("select _id,pathname from tb_sm_wl_tree_info where parent_id=?;",new String[]{eatra_data});
		Cursor c = database.rawQuery("select _id,handle_sub from tb_kfwh_handle_sub where  re_id=?;",new String[]{eatra_data});
		BasePageBean<CommonSelectData> pagedata = new BasePageBean<CommonSelectData>();
		List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {  
			CommonSelectData data = new CommonSelectData();
			byte[] bname = c.getBlob(c.getColumnIndex("handle_sub"));

			try {
				data.setText(new String(bname,"UTF-8"));
				data.setValue(c.getString(c.getColumnIndex("_id")));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lists.add(data);
			c.moveToNext();   
		}   
		pagedata.setList(lists);
		setClgcData(lists);
		c.close();
		database.close();
	}
	
	/**
	 * 获取处理过程
	 */
	private void getCLGC() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("pId", eatra_data);

		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<HandleSubBean>>(this,
						new TypeToken<BasePageBean<HandleSubBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<HandleSubBean> basePageBean) {
						/*List<CommonSelectData> commonSelects = new ArrayList<CommonSelectData>();
						for(int i = 0;i< basePageBean.getList().size();i ++){
							CommonSelectData commonSelectData = new CommonSelectData();
							commonSelectData.setText(basePageBean.getList().get(i).getHandleSub());
							commonSelectData.setValue(basePageBean.getList().get(i).getId());
							commonSelects.add(commonSelectData);
						}
						
						KingTellerUtils.saveMaintainInfoClgcDataBase(clgcdb,
								eatra_data+"clgc", gson.toJson(commonSelects).toString(), 1);*/
						
						BasePageBean<CommonSelectData> pagedata = (BasePageBean) basePageBean;
						List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectData data = new CommonSelectData();
							data.setText(basePageBean.getList().get(i).getHandleSub());
							data.setValue(basePageBean.getList().get(i).getId());

							lists.add(data);
						}
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}
	
	/**
	 * 获取处理过程
	 *//*
	private void getCLGC() {
		// TODO Auto-generated method stub
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("pId", eatra_data);

		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<HandleSubBean>>(this,
						new TypeToken<BasePageBean<HandleSubBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<HandleSubBean> basePageBean) {
						
						KingTellerUtils.saveMaintainInfoDataBase(CommonSelectClgcActivity.this, 
								eatra_data, gson.toJson(basePageBean.getList()).toString(), 1);
						
						BasePageBean<CommonSelectData> pagedata = (BasePageBean) basePageBean;
						List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectData data = new CommonSelectData();
							data.setText(basePageBean.getList().get(i)
									.getHandleSub());
							data.setValue(basePageBean.getList().get(i).getHandleSub());

							lists.add(data);
						}
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}*/

	private void setData(BasePageBean<CommonSelectData> pagedata) {
		List<CommonSelectData> data = pagedata.getList();
		if (data.size() > 0) {
			adapter.setLists(data);
			getListviewObj().setStatus(LoadingEnum.LISTSHOW);
		} else {
			getListviewObj().setStatus(LoadingEnum.NODATA, "没有数据");
		}
	}
	
	private void setClgcData(List<CommonSelectData> data){
		if (data.size() > 0) {
			adapter.setLists(data);
			getListviewObj().setStatus(LoadingEnum.LISTSHOW);
		} else {
			getListviewObj().setStatus(LoadingEnum.NODATA, "没有数据");
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_left:
			this.finish();
			break;
		case R.id.button_right:
			onRefresh();
			break;
		case R.id.submit:
			setItemDataMul();
			this.finish();
			break;
		case R.id.cancel:
			this.finish();
			break;

		}
	}

	public void setItemDataMul() {
		Intent intent = new Intent();
		List<CommonSelectData> listData = adapter.getCheckedDataList();
		StringBuilder strBuiler1 = new StringBuilder();
		StringBuilder strBuiler2 = new StringBuilder();
		for (int i = 0; i < listData.size(); i++) {
			if (listData.size() == 1) {
				strBuiler1.append(listData.get(0).getText());
				strBuiler2.append(listData.get(0).getValue());
			} else {
				strBuiler1.append(listData.get(i).getText());
				strBuiler1.append(",");
				strBuiler2.append(listData.get(i).getValue());
				strBuiler2.append(",");
			}
		}
		CommonSelectData commonSelectData = new CommonSelectData();
		if(strBuiler1.toString().contains("其它")){
			ISCONTAINOTHER = true;
		}else{
			ISCONTAINOTHER = false;
		}
		commonSelectData.setText(strBuiler1.toString());
		commonSelectData.setValue(strBuiler2.toString());
		intent.putExtra(DATA, (CommonSelectData) commonSelectData);
		setResult(RESULT_OK, intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stu
		
	}

	@Override
	public void onRefresh() {
		getData();
		getListviewObj().getListview().setLoadMoreEnabled(true);
	}

	@Override
	public void onLoadMore() {
	}
}
