package com.kingteller.client.activity.knowledge;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.KnowledgeAdapter;
import com.kingteller.client.bean.account.User;
import com.kingteller.client.bean.common.BasePageBean;
import com.kingteller.client.bean.knowledge.KnowledgeBean;
import com.kingteller.client.config.URLConfig;
import com.kingteller.client.netauth.AjaxHttpCallBack;
import com.kingteller.client.netauth.KTHttpClient;
import com.kingteller.client.utils.ConditionUtils;
import com.kingteller.client.utils.ConfigUtils;
import com.kingteller.client.view.ConditionView;
import com.kingteller.client.view.XListView.IXListViewListener;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.condition.Condition;
import com.kingteller.framework.http.AjaxParams;
import com.kingteller.framework.utils.Logger;
/**
 * 错误代码搜索界面处理类
 * @author tom
 *
 */
public class KnowledgeActivity extends BaseActivity implements OnClickListener,IXListViewListener,
OnItemClickListener{
	
	private ConditionView errorCode;//错误代码
	private ConditionView component;//故障部件
	private List<KnowledgeBean> knowledgeData = new ArrayList<KnowledgeBean>();
	private KnowledgeAdapter adapter;
	private int currentPage=1;
	

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentView(R.layout.layout_error_code_search);
		initUI();
		//getData();
	}

	


	/**
	 * 初始化界面
	 */
	private void initUI() {
		// TODO Auto-generated method stub
		title_title.setText("解决方案搜索");
		title_right.setText("搜索");
		title_left.setOnClickListener(this);
		title_right.setOnClickListener(this);
		
		errorCode = (ConditionView) findViewById(R.id.errorCode);
		component = (ConditionView) findViewById(R.id.component);
		adapter=new KnowledgeAdapter(this,knowledgeData);
		getListviewObj().getListview().setAdapter(adapter);
		getListviewObj().getListview().setRefreshEnabled(true);
		getListviewObj().getListview().setLoadMoreEnabled(true);
		getListviewObj().getListview().setOnRefreshListener(this);
		getListviewObj().getListview().setOnItemClickListener(this);
		getListviewObj().setStatus(LoadingEnum.NODATA,"请输入条件进行搜索");
		
	}
	private void getData() {
		if(currentPage==1){
			getListviewObj().setStatus(LoadingEnum.LOADING);
		}
		User user = User.getInfo(this);
		KTHttpClient fh = new KTHttpClient(true);
		//获取condition实例
		LinearLayout conditionLayout=(LinearLayout)findViewById(R.id.knowledge_condition);
		Condition cond=ConditionUtils.getCondition(this, conditionLayout);
		AjaxParams params = ConditionUtils.getAjaxParams(cond, currentPage);
		Logger.e("condition", params.getParamString());
		Logger.e("url", ConfigUtils.CreateUrl(this, URLConfig.KnowledgeUrl));
		fh.post(ConfigUtils.CreateUrl(this, URLConfig.KnowledgeUrl), params,
				new AjaxHttpCallBack<BasePageBean<KnowledgeBean>>(this,
						new TypeToken<BasePageBean<KnowledgeBean>>() {
						}.getType(), true) {

					@Override
					public void onFinish() {
						getListviewObj().getListview().stopRefresh();
						getListviewObj().getListview().stopLoadMore();
					}

					@Override
					public void onDo(BasePageBean<KnowledgeBean> basePageBean) {
						List data=basePageBean.getList();
						if (data.size() > 0) {
							if (basePageBean.getCurrentPage()==1)
								adapter.setLists(data);
							else
								adapter.addLists(data);

							getListviewObj()
										.setStatus(LoadingEnum.LISTSHOW);
							//如果当前页等于总页数，则不需要先是load more按钮
							if (basePageBean.getCurrentPage()==basePageBean.getTotalPage())
								getListviewObj().getListview()
										.setLoadMoreEnabled(false);

						} else{
							getListviewObj().setStatus(LoadingEnum.NODATA,"没有数据");
						}
						
					};

				});		

		
	}
	
	private List<KnowledgeBean> getKnowledgeData(){
		return knowledgeData;
	}


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
			case R.id.button_left:
				this.finish();
				break;
			case R.id.button_right:
				onRefresh();
				break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRefresh() {
		currentPage=1;
		getData();
		getListviewObj().getListview().setLoadMoreEnabled(true);
	}


	@Override
	public void onLoadMore() {
		currentPage++;
		getData();
	}
}
