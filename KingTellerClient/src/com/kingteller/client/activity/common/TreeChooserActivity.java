package com.kingteller.client.activity.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseFragmentActivity;
import com.kingteller.client.activity.common.fragment.TreeListFragment;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.common.TreeBean;
import com.kingteller.client.bean.workorder.BjWlBean;
import com.kingteller.client.bean.workorder.FeeTypeBean;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.utils.KingTellerUtils;
import com.kingteller.client.utils.SQLiteHelper;
import com.kingteller.framework.KingTellerDb;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.CommonUtils;
import com.kingteller.framework.utils.Logger;

/**
 * 通用树形选择器
 * 
 * @author 王定波
 * 
 */
public class TreeChooserActivity extends BaseFragmentActivity implements
		OnBackStackChangedListener {

	public static final String PID = "pid";
	public static List<TreeBean> datalist = new ArrayList<TreeBean>();
	public static String ROOTPID = "0";
	private final static int KEY_TITLE = 0;
	private final static int KEY_URL = 1;

	private FragmentManager mFragmentManager;
	private String mPid;
	private String title;

	private int type;
	private String[] typedatas;
	private String eatra_data;
	private Gson gson = new Gson();
	private KingTellerDb azwldb = KingTellerUtils.create(this);
	private KingTellerDb yzwldb = KingTellerUtils.create(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_tree_chooser);

		ROOTPID="0";
		mFragmentManager = getSupportFragmentManager();
		mFragmentManager.addOnBackStackChangedListener(this);
		initData();
		
		if (savedInstanceState == null) {
			mPid = ROOTPID;
		} else {
			mPid = savedInstanceState.getString(PID);
		}

		title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		title_right.setBackgroundResource(R.drawable.btn_close);;
		title_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		
		type = getIntent().getIntExtra(CommonSelectDataActivity.TYPE, 0);
		eatra_data = getIntent().getStringExtra(
				CommonSelectDataActivity.EXTRADATA);
		title = getIntent().getStringExtra(CommonSelectDataActivity.TITLE);
		typedatas = getResources().getStringArray(type);
		
		datalist = new ArrayList<TreeBean>();
		Intent intent = getIntent();
		Bundle bundles = intent.getExtras();
		if (bundles != null
				&& bundles.containsKey(CommonSelectDataActivity.TYPE)) {
			int type = bundles.getInt(CommonSelectDataActivity.TYPE, 0);
			switch (type) {
			case R.array.feetype:
				getFeeTypes();
				break;
			case R.array.bjwl:
				/*String maintainDataGZBJ ="";
				if(title.equals("安装物料")){
					maintainDataGZBJ= KingTellerUtils.getMaintainInfoClgcDataBase(azwldb, eatra_data+"azwl");
				}else if(title.equals("原装物料")){
					maintainDataGZBJ= KingTellerUtils.getMaintainInfoClgcDataBase(yzwldb, eatra_data+"yzwl");
				}
				if(!CommonUtils.isEmpty(maintainDataGZBJ) ){
					List<BjWlBean> bjList = gson.fromJson(maintainDataGZBJ, new TypeToken<List<BjWlBean>>() {}.getType());
					List<TreeBean> list = new ArrayList<TreeBean>();
					for (int i = 0; i < bjList.size(); i++) {
						TreeBean data = new TreeBean();
						data.setId(bjList.get(i).getId());
						data.setPid(bjList.get(i).getParentId());
						data.setTitle(bjList.get(i).getWlName());
						list.add(data);
					}
					
					mPid=eatra_data;
					datalist = list;
					addfirstFragment();
				}else{
					getBjWl();
				}
				*/
				getBjwl();
				break;
			default:
				break;
			}

		}
		title = CommonUtils.isEmpty(title) ? typedatas[KEY_TITLE] : title;
		setTitle(title);

	}

	private void getFeeTypes() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		KTHttpClient fh = new KTHttpClient(true);

		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]),
				new AjaxHttpCallBack<BasePageBean<FeeTypeBean>>(this,
						new TypeToken<BasePageBean<FeeTypeBean>>() {
						}.getType(), true) {

					@Override
					public void onDo(final BasePageBean<FeeTypeBean> lists) {
						// TODO Auto-generated method stub
			
						for (int i = 0; i < lists.getList().size(); i++) {
							TreeBean data = new TreeBean();
							data.setId(lists.getList().get(i).getId());
							data.setPid(CommonUtils.isEmpty(lists.getList()
									.get(i).getPid()) ? ROOTPID : lists
									.getList().get(i).getPid());
							data.setTitle(lists.getList().get(i).geteName());
							datalist.add(data);
						}

						addfirstFragment();

					}
				});
	}

	//获取所有的故障部件
	
	private void getBjwl(){
		
		String myPath = SQLiteHelper.DB_PATH+SQLiteHelper.DB_NAME;
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(myPath,null);
		Cursor c = database.rawQuery("select tr._id,t.wl_name,tr.parent_id from tb_sm_wl_tree_info tr,tb_sm_wl_info t " +
				"where t._id=tr.wl_info_id and  tr.pathid like "+"\'%->"+eatra_data+"%\';",null);
		
		mPid=eatra_data;
		List<TreeBean> list = new ArrayList<TreeBean>();
		c.moveToFirst();
		
		for (int i = 0; i < c.getCount(); i++) { 
			TreeBean data = new TreeBean();
			byte[] bname = c.getBlob(c.getColumnIndex("wl_name"));
			try {
				data.setId(c.getString(c.getColumnIndex("_id")));
				data.setPid(c.getString(c.getColumnIndex("parent_id")));
				data.setTitle(new String(bname,"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			list.add(data);
			c.moveToNext(); 
		}

		datalist = list;
		addfirstFragment();
		
		c.close();
		database.close();
		
	}
	
	//获取故障部件
	private void getBjWl() {
		// TODO Auto-generated method stub
		if (!CommonUtils.isNetAvaliable(this)) {
			toastMsg("网络异常，请稍后重试");
			return;
		}

		ROOTPID=eatra_data;
		KTHttpClient fh = new KTHttpClient(true);
		AjaxParams params = new AjaxParams();
		params.put("pId",eatra_data);
		params.put("flag", "3");
		
		fh.post(ConfigUtils.CreateUrl(this, typedatas[KEY_URL]), params,
				new AjaxHttpCallBack<BasePageBean<BjWlBean>>(this,
						new TypeToken<BasePageBean<BjWlBean>>() {
						}.getType(), true) {
					@Override
					public void onDo(final BasePageBean<BjWlBean> lists) {
						// TODO Auto-generated method stub
						if(title.equals("安装物料")){
							KingTellerUtils.saveMaintainInfoClgcDataBase(azwldb, 
									eatra_data+"azwl", gson.toJson(lists.getList()).toString(), 1);
						}else if(title.equals("原装物料")){
							KingTellerUtils.saveMaintainInfoClgcDataBase(yzwldb, 
									eatra_data+"yzwl", gson.toJson(lists.getList()).toString(), 1);
						}
						
						List<TreeBean> list = new ArrayList<TreeBean>();
						for (int i = 0; i < lists.getList().size(); i++) {
							TreeBean data = new TreeBean();
							data.setId(lists.getList().get(i).getId());
							data.setPid(lists.getList().get(i).getParentId());
							data.setTitle(lists.getList().get(i).getWlName());
							list.add(data);
						}

						datalist = list;
						addfirstFragment();
					}
				});

	}

	private void addfirstFragment() {
		Logger.e("TreeChooseActivity",mPid);
		addFragment(mPid);
	}

	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		title_title.setText(title);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(PID, mPid);
	}

	@Override
	public void onBackStackChanged() {
		String mtitle = title;

		int count = mFragmentManager.getBackStackEntryCount();
		if (count > 0) {
			BackStackEntry fragment = mFragmentManager
					.getBackStackEntryAt(count - 1);
			mtitle = fragment.getName();
		}

		setTitle(mtitle);
	}

	/**
	 * Add the initial Fragment with given path.
	 * 
	 * @param path
	 *            The absolute path of the file (directory) to display.
	 */
	private void addFragment(String path) {
		TreeListFragment explorerFragment = TreeListFragment.newInstance(mPid);
		
		mFragmentManager.beginTransaction()
				.add(R.id.explorer_fragment, explorerFragment).commit();
	}

	/**
	 * "Replace" the existing Fragment with a new one using given path. We're
	 * really adding a Fragment to the back stack.
	 * 
	 * @param path
	 *            The absolute path of the file (directory) to display.
	 */
	private void replaceFragment(TreeBean data) {
		TreeListFragment explorerFragment = TreeListFragment.newInstance(data
				.getId());
		mFragmentManager.beginTransaction()
				.replace(R.id.explorer_fragment, explorerFragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(data.getTitle()).commit();
	}

	/**
	 * Finish this Activity with a result code and URI of the selected file.
	 * 
	 * @param file
	 *            The file selected.
	 */
	private void finishWithResult(TreeBean data) {
		if (data != null) {
			Intent intent = new Intent();
			intent.putExtra(CommonSelectDataActivity.DATA, data);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	/**
	 * Called when the user selects a File
	 * 
	 * @param file
	 *            The file that was selected
	 */
	public void onFileSelected(TreeBean data) {
		if (data != null) {
			mPid = data.getId();

			if (data.isLast()) {
				finishWithResult(data);

			} else {
				replaceFragment(data);
			}
		}
	}

}
