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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.CommonSelectDataAdapter;
import com.kingteller.client.adapter.CommonSelectGZBJDataAdapter;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.CommonSelectData;
import com.kingteller.client.bean.common.CommonSelectGZBJ;
import com.kingteller.client.bean.workorder.AssignWorkerNameBean;
import com.kingteller.client.bean.workorder.BjWlBean;
import com.kingteller.client.bean.workorder.ClgcWorktypeBean;
import com.kingteller.client.bean.workorder.FeeModeBean;
import com.kingteller.client.bean.workorder.HandleSubBean;
import com.kingteller.client.bean.workorder.RTroubleRemarkBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.CommonSelcetUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.utils.SQLiteHelper;
import com.kingteller.client.utils.SortByPinyin;
import com.kingteller.client.utils.SortByPinyinGZ;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;

/**
 * 不是树形全部
 * 
 * @author 王定波
 * 
 */
public class CommonSelectDataActivity extends BaseActivity implements
		OnClickListener, IXListViewListener, OnItemClickListener {

	private List<CommonSelectData> listData = new ArrayList<CommonSelectData>();
	private CommonSelectDataAdapter adapter;
	private List<CommonSelectGZBJ> listGzbjData = new ArrayList<CommonSelectGZBJ>();
	private CommonSelectGZBJDataAdapter gzbjAdapter;
	private String[] typedatas;
	private int type;
	private String eatra_data;
	private String troubleModule;
	public final static String TITLE = "title";
	public final static String TYPE = "type";
	public final static String DATA = "data";
	public final static String EXTRADATA = "extra_data";
	public final static String TROUBLEMODULE="troubleModule";
	private final static int KEY_TITLE = 0;
	private final static int KEY_URL = 1;
	private Gson gson = new Gson();
	private HashMap<String,String> hashMap = new HashMap<String,String>();
	private KingTellerDb clgcdb = KingTellerUtils.create(this);
	private KingTellerDb gzmkdb = KingTellerUtils.create(this);
	private KingTellerDb gzmsdb = KingTellerUtils.create(this);
	public KingTellerDb gzbjdb = KingTellerUtils.create(this);

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.common_list_view);
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
		troubleModule = getIntent().getStringExtra(TROUBLEMODULE);

		title_title.setText(typedatas[KEY_TITLE]);
		title_left.setOnClickListener(this);

		if(!typedatas[KEY_TITLE].equals("故障部件") && !typedatas[KEY_TITLE].equals("任务信息")){
			adapter = new CommonSelectDataAdapter(this, listData);
			getListviewObj().getListview().setAdapter(adapter);
			getListviewObj().getListview().setRefreshEnabled(true);
			getListviewObj().getListview().setLoadMoreEnabled(false);
			getListviewObj().getListview().setOnRefreshListener(this);
			getListviewObj().getListview().setOnItemClickListener(this);
		}else{
			gzbjAdapter = new CommonSelectGZBJDataAdapter(this, listGzbjData);
			getListviewObj().getListview().setAdapter(gzbjAdapter);
			getListviewObj().getListview().setRefreshEnabled(true);
			getListviewObj().getListview().setLoadMoreEnabled(false);
			getListviewObj().getListview().setOnRefreshListener(this);
			getListviewObj().getListview().setOnItemClickListener(this);
		}

	}

	private void getData() {

		switch (type) {
		case R.array.bjmk:// 故障模块
			/*String maintainDataGZMK = KingTellerUtils.getMaintainInfoClgcDataBase(gzmkdb, "gzmk");
			if(!CommonUtils.isEmpty(maintainDataGZMK)){
				List<BjWlBean> bjList = gson.fromJson(maintainDataGZMK, new TypeToken<List<BjWlBean>>() {}.getType());
				BasePageBean<CommonSelectData> pagedata = new BasePageBean<CommonSelectData>();
				List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
				for (int i = 0; i < bjList.size(); i++) {
					CommonSelectData data = new CommonSelectData();
					data.setText(bjList.get(i).getWlName());
					data.setValue(bjList.get(i).getId());
					
					lists.add(data);
				}
				SortByPinyin.sort(lists);
				pagedata.setList(lists);
				setData(pagedata);
			}else{
				getAllBJMK();
				getBJMK();
			}*/
			getBjmk();
			break;

		case R.array.gzms:// 故障描述
			/*String maintainDataGZMS = KingTellerUtils.getMaintainInfoClgcDataBase(gzmsdb, eatra_data+"gzms");
			if(!CommonUtils.isEmpty(maintainDataGZMS)){
				List<RTroubleRemarkBean> bjTroubleList = gson.fromJson(maintainDataGZMS, new TypeToken<List<RTroubleRemarkBean>>() {}.getType());
				BasePageBean<CommonSelectData> pagedata = new BasePageBean<CommonSelectData>();
				List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
				for (int i = 0; i < bjTroubleList.size(); i++) {
					
					if((!CommonUtils.isEmpty(bjTroubleList.get(i).getTwoLevelId())&&bjTroubleList.get(i).getTwoLevelId().equals(troubleModule)) 
							&& (!CommonUtils.isEmpty(bjTroubleList.get(i).getParentId()) && bjTroubleList.get(i).getParentId().equals(eatra_data))){
						CommonSelectData data = new CommonSelectData();
						data.setText(bjTroubleList.get(i).getTroubleRemark());
						data.setValue(bjTroubleList.get(i).getId());
						
						lists.add(data);
					}
				}
				if (lists.size() == 0) {
						CommonSelectData data = new CommonSelectData();
						data.setText("其它");
						data.setValue("201409291609a");

						lists.add(data);
				}
				SortByPinyin.sort(lists);
				pagedata.setList(lists);
				setData(pagedata);
				arrayGzms = new String[lists.size()];
				for(int i= 0; i < lists.size();i ++){
					arrayGzms[i] = lists.get(i).getText();
					hashMap.put(lists.get(i).getText(), lists.get(i).getValue());
				}
			}else{
				getAllGZMS();
				getGZMS();
			}*/
			getGzms();
			break;
		case R.array.clgc:// 处理过程
			String maintainDataCLGC = KingTellerUtils.getMaintainInfoClgcDataBase(clgcdb, eatra_data+"clgc");
			if(!CommonUtils.isEmpty(maintainDataCLGC) ){
				List<HandleSubBean> bjList = gson.fromJson(maintainDataCLGC, new TypeToken<List<HandleSubBean>>() {}.getType());
				BasePageBean<CommonSelectData> pagedata = new BasePageBean<CommonSelectData>();
				List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
				for (int i = 0; i < bjList.size(); i++) {
					CommonSelectData data = new CommonSelectData();
					data.setText(bjList.get(i).getHandleSub());
					data.setValue(bjList.get(i).getId());

					lists.add(data);
				}
				
				SortByPinyin.sort(lists);
				pagedata.setList(lists);
				setData(pagedata);
			}else{
				getAllCLGC();
				getCLGC();
			}
			break;
		case R.array.jtgj:// 获取交通工具
			getJTGJ();
			break;
		case R.array.bjwl:// 获取故障部件
			/*String maintainDataGZBJ = KingTellerUtils.getMaintainInfoClgcDataBase(gzbjdb, eatra_data+"gzbj");
			if(!CommonUtils.isEmpty(maintainDataGZBJ) ){
				List<BjWlBean> bjList = gson.fromJson(maintainDataGZBJ, new TypeToken<List<BjWlBean>>() {}.getType());
				BasePageBean<CommonSelectGZBJ> pagedata = new BasePageBean<CommonSelectGZBJ>();
				List<CommonSelectGZBJ> lists = new ArrayList<CommonSelectGZBJ>();
				for (int i = 0; i < bjList.size(); i++) {
					CommonSelectGZBJ data = new CommonSelectGZBJ();
					data.setText(bjList.get(i).getWlName());
					data.setValue(bjList.get(i).getId());
					data.setPathname(bjList.get(i).getPathname());
					lists.add(data);
				}
				pagedata.setList(lists);
				setGZBJData(pagedata);
			}else{
				getAllGZBJ();
				getGZBJ();
			}*/
			getGzbj();
			break;
		case R.array.clgcworktype:
			/*String clgcWorkTypeData = KingTellerUtils.getMaintainInfoDataBase(CommonSelectDataActivity.this, eatra_data+"clgcworkType");
			if(!CommonUtils.isEmpty(clgcWorkTypeData) ){
				List<ClgcWorktypeBean> bjList = gson.fromJson(clgcWorkTypeData, new TypeToken<List<ClgcWorktypeBean>>() {}.getType());
				BasePageBean<CommonSelectData> pagedata = new BasePageBean<CommonSelectData>();
				List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
				for (int i = 0; i < bjList.size(); i++) {
					CommonSelectData data = new CommonSelectData();
					data.setText(bjList.get(i).getHandleSub());
					data.setValue(bjList.get(i).getId());

					lists.add(data);
				}
				//SortByPinyin.sort(lists);
				pagedata.setList(lists);
				setData(pagedata);
			}else{
				getAllClgcWorkTypes();
				getClgcWorkType();
			}*/
			getClgcWorktype();
			break;
		case R.array.cxfwzry:
			getFwzrys();
			break;
		default:
			break;
		}

	}

	private void getAllClgcWorkTypes(){
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		for(int i = 0 ; i < CommonSelcetUtils.workTypeValue.length;i++){
			final String keyStr = CommonSelcetUtils.workTypeValue[i];
			KTHttpClient fh = new KTHttpClient(true);
			AjaxParams params = new AjaxParams();

			params.put("pId", keyStr);

			fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
					new AjaxHttpCallBack<BasePageBean<ClgcWorktypeBean>>(this,
							new TypeToken<BasePageBean<ClgcWorktypeBean>>() {
							}.getType(), true) {

						@Override
						public void onFinish() {
							getListviewObj().getListview().stopRefresh();
							getListviewObj().getListview().stopLoadMore();
						}
						
						@Override
						public void onDo(BasePageBean<ClgcWorktypeBean> basePageBean) {
							KingTellerUtils.saveMaintainInfoDataBase(CommonSelectDataActivity.this, 
									keyStr+"clgcworkType", gson.toJson(basePageBean.getList()).toString(), 1);
						};

					});
		}
	}
	
	private void getClgcWorktype(){
		
		String myPath = SQLiteHelper.DB_PATH+SQLiteHelper.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(myPath,null);
		//Cursor c = database.rawQuery("select _id,pathname from tb_sm_wl_tree_info where parent_id=?;",new String[]{eatra_data});
		Cursor c = database.rawQuery("select _id,handle_sub from tb_kfwh_worktype_handlesub where parent_id=?;",new String[]{eatra_data});
		
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
		
		//SortByPinyin.sort(lists);
		pagedata.setList(lists);
		setData(pagedata);
		c.close();
		database.close();
	}
	
	private void getClgcWorkType() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();

		params.put("pId", eatra_data);

		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<ClgcWorktypeBean>>(this,
						new TypeToken<BasePageBean<ClgcWorktypeBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}
					
					@Override
					public void onDo(BasePageBean<ClgcWorktypeBean> basePageBean) {
						
						KingTellerUtils.saveMaintainInfoDataBase(CommonSelectDataActivity.this, 
								eatra_data+"clgcworkType", gson.toJson(basePageBean.getList()).toString(), 1);
						BasePageBean<CommonSelectData> pagedata = (BasePageBean) basePageBean;
						List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectData data = new CommonSelectData();
							data.setText(basePageBean.getList().get(i).getHandleSub());
							data.setValue(basePageBean.getList().get(i).getId());

							lists.add(data);
						}
						SortByPinyin.sort(lists);
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}

	/**
	 * 获取交通工具
	 */
	private void getJTGJ() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();

		params.put("id", eatra_data);

		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<FeeModeBean>>(this,
						new TypeToken<BasePageBean<FeeModeBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<FeeModeBean> basePageBean) {
						BasePageBean<CommonSelectData> pagedata = (BasePageBean) basePageBean;
						List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectData data = new CommonSelectData();
							data.setText(basePageBean.getList().get(i)
									.getModeName());
							data.setValue(basePageBean.getList().get(i).getId());

							lists.add(data);
						}
						SortByPinyin.sort(lists);
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}

	private void getAllCLGC(){
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		String maintainDataGZMK = KingTellerUtils.getMaintainInfoClgcDataBase(gzmkdb, "gzmk");
		if (!CommonUtils.isEmpty(maintainDataGZMK)) {
			List<BjWlBean> bjList = gson.fromJson(maintainDataGZMK,new TypeToken<List<BjWlBean>>() {}.getType());
			for (int i = 0; i < bjList.size(); i++) {
				String maintainDataGZMS = KingTellerUtils.getMaintainInfoClgcDataBase(gzmsdb, bjList.get(i).getId()+"gzms");
				if(!CommonUtils.isEmpty(maintainDataGZMS)){
					List<RTroubleRemarkBean> trouList = gson.fromJson(maintainDataGZMS, new TypeToken<List<RTroubleRemarkBean>>(){}.getType());
					
					for(int j = 0; j < trouList.size();j ++){
						KTHttpClient fh = new KTHttpClient(true);
						AjaxParams params = new AjaxParams();
						params.put("pId", trouList.get(j).getId());
						
						final String pid = trouList.get(j).getId();
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
										KingTellerUtils.saveMaintainInfoClgcDataBase(clgcdb,
												pid+"clgc", gson.toJson(basePageBean.getList()).toString(), 1);
									};

								});
					}
				}
			}

		}
	
	}
	
	//查询服务站人员
		private void getFwzrys(){
			if(!CommonUtils.isNetAvaliable(this)){
				toastMsg("网络异常，请稍后重试");
				return;
			}
			
			KTHttpClient fh = new KTHttpClient(true);
			AjaxParams params = new AjaxParams();

			fh.get(ConfigUtils.CreateNoVersionUrl(this, URLConfig.CxfwzryUrl),
					params, new AjaxHttpCallBack<BasePageBean<AssignWorkerNameBean>>(this,
							new TypeToken<BasePageBean<AssignWorkerNameBean>>() {
							}.getType(), true) {
						
						@Override
						public void onFinish() {
							getListviewObj().getListview().stopRefresh();
						}
						
						@Override
						public void onDo(BasePageBean<AssignWorkerNameBean> basePageBean) {
							BasePageBean<CommonSelectData> pagedata=(BasePageBean) basePageBean;
							List<CommonSelectData> lists=new ArrayList<CommonSelectData>();
							for (int i = 0; i < basePageBean.getList().size(); i++) {
								CommonSelectData data=new CommonSelectData();
								data.setText(basePageBean.getList().get(i).getUserName());
								data.setValue(basePageBean.getList().get(i).getUserName());
								lists.add(data);
							}
							pagedata.setList(lists);
							setData(pagedata);
						};
					});
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
						KingTellerUtils.saveMaintainInfoClgcDataBase(clgcdb,
								eatra_data+"clgc", gson.toJson(basePageBean.getList()).toString(), 1);
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
	
	// 获取所有的故障描述
	private void getAllGZMS() {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		String maintainDataGZMK = KingTellerUtils.getMaintainInfoClgcDataBase(gzmkdb, "gzmk");
		if (!CommonUtils.isEmpty(maintainDataGZMK)) {
			List<BjWlBean> bjList = gson.fromJson(maintainDataGZMK,
					new TypeToken<List<BjWlBean>>() {}.getType());
			for (int i = 0; i < bjList.size(); i++) {
				KTHttpClient fh = new KTHttpClient(true);
				AjaxParams params = new AjaxParams();
				params.put("pId", bjList.get(i).getId());
				final String pid = bjList.get(i).getId();

				fh.post(ConfigUtils.CreateUrl(this, URLConfig.GzmsUrl),params,
						new AjaxHttpCallBack<BasePageBean<RTroubleRemarkBean>>(this,
								new TypeToken<BasePageBean<RTroubleRemarkBean>>() {}.getType(), true) {

							@Override
							public void onFinish() {
								getListviewObj().getListview().stopRefresh();
								getListviewObj().getListview().stopLoadMore();
							}

							@Override
							public void onDo(BasePageBean<RTroubleRemarkBean> basePageBean) {
								KingTellerUtils.saveMaintainInfoClgcDataBase(gzmsdb, pid + "gzms",gson.toJson(basePageBean.getList()).toString(), 1);
							};
						});
			}
		}
	}
	
	private void getGzms(){
		
		String myPath = SQLiteHelper.DB_PATH+SQLiteHelper.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(myPath,null);
		Cursor c = database.rawQuery("select _id,trouble_remark,path_name from tb_kfwh_trouble_remark where parent_id=? and two_level_id=?;",new String[]{eatra_data,troubleModule});
		
		BasePageBean<CommonSelectGZBJ> pagedata = new BasePageBean<CommonSelectGZBJ>();
		List<CommonSelectGZBJ> lists = new ArrayList<CommonSelectGZBJ>();

		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {  
			CommonSelectGZBJ data = new CommonSelectGZBJ();
			byte[] bname = c.getBlob(c.getColumnIndex("trouble_remark"));
			byte[] bname1 = c.getBlob(c.getColumnIndex("path_name"));

			try {
				data.setText(new String(bname,"UTF-8"));
				data.setValue(c.getString(c.getColumnIndex("_id")));
				data.setPathname(new String(bname1,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lists.add(data);
			c.moveToNext();   
		}   
		
		if(lists.size() == 0){
			c = database.rawQuery("select _id,trouble_remark,path_name from tb_kfwh_trouble_remark where parent_id=?;",new String[]{eatra_data});
			
			pagedata = new BasePageBean<CommonSelectGZBJ>();
			lists = new ArrayList<CommonSelectGZBJ>();

			c.moveToFirst();
			for (int i = 0; i < c.getCount(); i++) {  
				CommonSelectGZBJ data = new CommonSelectGZBJ();
				byte[] bname = c.getBlob(c.getColumnIndex("trouble_remark"));
				byte[] bname1 = c.getBlob(c.getColumnIndex("path_name"));

				try {
					data.setText(new String(bname,"UTF-8"));
					data.setValue(c.getString(c.getColumnIndex("_id")));
					data.setPathname(new String(bname1,"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lists.add(data);
				c.moveToNext();   
			}   
		}

		if (lists.size() == 0) {
			CommonSelectGZBJ data = new CommonSelectGZBJ();
			data.setText("其它");
			data.setValue("201409291609a");
			data.setPathname("其它");

			lists.add(data);
		}
		SortByPinyinGZ.sort(lists);
		pagedata.setList(lists);
		setGZBJData(pagedata);
		c.close();
		database.close();
	}
	
	/**
	 * 获取故障描述
	 */
	private void getGZMS() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("pId", eatra_data);

		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<RTroubleRemarkBean>>(this,
						new TypeToken<BasePageBean<RTroubleRemarkBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<RTroubleRemarkBean> basePageBean) {
						KingTellerUtils.saveMaintainInfoClgcDataBase(gzmsdb,
								eatra_data+"gzms", gson.toJson(basePageBean.getList()).toString(), 1);
						BasePageBean<CommonSelectData> pagedata = (BasePageBean) basePageBean;
						List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							
							if(!CommonUtils.isEmpty(basePageBean.getList().get(i).getTwoLevelId())
									&& basePageBean.getList().get(i).getTwoLevelId().equals(troubleModule)){
								CommonSelectData data = new CommonSelectData();
								data.setText(basePageBean.getList().get(i).getTroubleRemark());
								data.setValue(basePageBean.getList().get(i).getId());
								
								lists.add(data);
							}else{
								CommonSelectData data = new CommonSelectData();
								data.setText(basePageBean.getList().get(i).getTroubleRemark());
								data.setValue(basePageBean.getList().get(i).getId());
								
								lists.add(data);
							}
						}
						SortByPinyin.sort(lists);
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}
	
	//获取所有的故障部件
	private void getAllGZBJ(){
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		String maintainDataGZMK = KingTellerUtils.getMaintainInfoClgcDataBase(gzmkdb, "gzmk");
		if (!CommonUtils.isEmpty(maintainDataGZMK)) {
			List<BjWlBean> bjList = gson.fromJson(maintainDataGZMK,new TypeToken<List<BjWlBean>>() {}.getType());
			for (int i = 0; i < bjList.size(); i++) {

				KTHttpClient fh = new KTHttpClient(true);
				AjaxParams params = new AjaxParams();
				params.put("pId", bjList.get(i).getId());
				final String pid = bjList.get(i).getId();

				fh.post(ConfigUtils.CreateUrl(this, URLConfig.GzbjUrl),params,
						new AjaxHttpCallBack<BasePageBean<BjWlBean>>(this,
								new TypeToken<BasePageBean<BjWlBean>>() {
								}.getType(), true) {

							@Override
							public void onFinish() {
								getListviewObj().getListview().stopRefresh();
								getListviewObj().getListview().stopLoadMore();
							}

							@Override
							public void onDo(BasePageBean<BjWlBean> basePageBean) {
								KingTellerUtils.saveMaintainInfoClgcDataBase(gzbjdb,
										pid+"gzbj", gson.toJson(basePageBean.getList()).toString(), 1);
							};
						});
			}
		}
	}
	
	
	private void getGzbj(){
		
		String myPath = SQLiteHelper.DB_PATH+SQLiteHelper.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(myPath,null);
		//Cursor c = database.rawQuery("select _id,pathname from tb_sm_wl_tree_info where parent_id=?;",new String[]{eatra_data});
		Cursor c = database.rawQuery("select tr._id,t.wl_name,tr.pathname from tb_sm_wl_tree_info tr,tb_sm_wl_info t " +
				"where t._id=tr.wl_info_id and  tr.pathid like '%->"+eatra_data+"->%' and tr.mode_level != 1 ;",null);
		//Cursor c = database.rawQuery("select _id,wl_name,new_code,pathname from view_sm_wl_info where pathid like '%->"+eatra_data+"->%' and mode_level != 1;",null);
		BasePageBean<CommonSelectGZBJ> pagedata = new BasePageBean<CommonSelectGZBJ>();
		List<CommonSelectGZBJ> lists = new ArrayList<CommonSelectGZBJ>();
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {  
			CommonSelectGZBJ data = new CommonSelectGZBJ();
			byte[] bname = c.getBlob(c.getColumnIndex("pathname"));
			byte[] bname1 = c.getBlob(c.getColumnIndex("wl_name"));

			try {
				data.setText(new String(bname1,"UTF-8"));
				data.setValue(c.getString(c.getColumnIndex("_id")));
				data.setPathname(new String(bname,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lists.add(data);
			c.moveToNext();   
		}   
		pagedata.setList(lists);
		setGZBJData(pagedata);
		c.close();
		database.close();
	}
	
	/**
	 * 获取故障部件
	 */
	private void getGZBJ() {
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("pId", eatra_data);

		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<BjWlBean>>(this,
						new TypeToken<BasePageBean<BjWlBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(final BasePageBean<BjWlBean> basePageBean) {
						// TODO Auto-generated method stub
						KingTellerUtils.saveMaintainInfoClgcDataBase(gzbjdb,
								eatra_data+"gzbj", gson.toJson(basePageBean.getList()).toString(), 1);
						BasePageBean<CommonSelectGZBJ> pagedata = (BasePageBean) basePageBean;
						sort(basePageBean);
						List<CommonSelectGZBJ> lists = new ArrayList<CommonSelectGZBJ>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectGZBJ data = new CommonSelectGZBJ();
							data.setText(basePageBean.getList().get(i).getWlName());
							data.setValue(basePageBean.getList().get(i).getId());
							data.setPathname(basePageBean.getList().get(i).getPathname());
							lists.add(data);
						}
						pagedata.setList(lists);
						setGZBJData(pagedata);
					}
				});
	}

	private void getAllBJMK(){
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("flag", "1");
		fh.post(ConfigUtils.CreateUrl(this, URLConfig.GzlbUrl), params,
				new AjaxHttpCallBack<BasePageBean<BjWlBean>>(this,
						new TypeToken<BasePageBean<BjWlBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<BjWlBean> basePageBean) {
						KingTellerUtils.saveMaintainInfoClgcDataBase(gzmkdb,
								"gzmk", gson.toJson(basePageBean.getList()).toString(), 1);
					};

				});
	}
	
	
	public void getBjmk(){
		/**
		 * select * from tb_sm_wl_tree_info where mode_level='1';
		 * */
		
		String myPath = SQLiteHelper.DB_PATH+SQLiteHelper.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(myPath,null);
		Cursor c = database.rawQuery("select _id,pathname from tb_sm_wl_tree_info where mode_level=\'1\';",null);
		
		BasePageBean<CommonSelectData> pagedata = new BasePageBean<CommonSelectData>();
		List<CommonSelectData> lists = new ArrayList<CommonSelectData>();

		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {  
			CommonSelectData data = new CommonSelectData();
			byte[] bname = c.getBlob(c.getColumnIndex("pathname"));

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
		setData(pagedata);
		c.close();
		database.close();
		
	}
	
	/**
	 * 获取故障模块
	 */
	private void getBJMK() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("flag", "1");
		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<BjWlBean>>(this,
						new TypeToken<BasePageBean<BjWlBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<BjWlBean> basePageBean) {
						BasePageBean<CommonSelectData> pagedata = (BasePageBean) basePageBean;
						List<CommonSelectData> lists = new ArrayList<CommonSelectData>();
						for (int i = 0; i < basePageBean.getList().size(); i++) {
							CommonSelectData data = new CommonSelectData();
							data.setText(basePageBean.getList().get(i).getWlName());
							data.setValue(basePageBean.getList().get(i).getId());
							
							lists.add(data);
						}
						SortByPinyin.sort(lists);
						pagedata.setList(lists);
						setData(pagedata);
					};

				});
	}

	
	
	private void setGZBJData(BasePageBean<CommonSelectGZBJ> pagedata) {
		// TODO Auto-generated method stub
		List<CommonSelectGZBJ> data = pagedata.getList();
		if (data.size() > 0) {
			gzbjAdapter.setLists(data);
			getListviewObj().setStatus(LoadingEnum.LISTSHOW);
		} else {
			getListviewObj().setStatus(LoadingEnum.NODATA, "没有数据");
		}
	}

	private void setData(BasePageBean<CommonSelectData> pagedata) {
		List<CommonSelectData> data = pagedata.getList();
		if (data.size() > 0) {
			adapter.setLists(data);
			getListviewObj().setStatus(LoadingEnum.LISTSHOW);
		} else {
			getListviewObj().setStatus(LoadingEnum.NODATA, "没有数据");
		}
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sort(BasePageBean<BjWlBean> data){ 
		
		if(data != null && data.getList().size() == 1){
			return;
		}
		java.util.Collections.sort(data.getList(),new java.util.Comparator(){

			@Override
			public int compare(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				BjWlBean bj1 = (BjWlBean) arg0;
				BjWlBean bj2 = (BjWlBean) arg1;
				long long1 = bj1.getNodeLevel();
				long long2 = bj2.getNodeLevel();
				if(long1 == long2){
					String pathId1 = bj1.getPathid();
					String pathId2 = bj2.getPathid();
					int flag = pathId1.compareTo(pathId2);
					return flag;
				}else if( long1 > long2){
					return 1;
				}else{
					return -1;
				}
			}
			
		});	
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.button_left:
			this.finish();
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if(type == R.array.bjwl){
			intent.putExtra(DATA,
					(CommonSelectGZBJ) adapterView.getItemAtPosition(pos));
		}else if(type == R.array.gzms){
			intent.putExtra(DATA,
					(CommonSelectGZBJ) adapterView.getItemAtPosition(pos));
		}else{
			intent.putExtra(DATA,
					(CommonSelectData) adapterView.getItemAtPosition(pos));
		}
		setResult(RESULT_OK, intent);
		finish();
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