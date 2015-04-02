package com.kingteller.client.activity.common;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.kingteller.R;
import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.adapter.SelectAddressAdapter;
import com.kingteller.client.bean.map.AddressBean;
import com.kingteller.client.view.datatype.LoadingEnum;
import com.kingteller.framework.utils.CommonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * 通用地图地址搜索选择器
 * @author 王定波
 *
 */
public class SelectAddressActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private boolean isStart;
	private EditText edit;
	private View address_icon;
	private LinearLayout search;
	private View delete;
	private SelectAddressAdapter searchAdapter;
	private ProgressBar progress;
	private boolean isFavAddrEdit = false;
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
	private PoiResult poiResult; // poi返回的结果
	public static final String ADDRESS = "address";

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.select_address);
		initUI();
		initData();
		initTitle();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.button_left:
			finish();
			break;
		case R.id.search:
			searchAddress();
			break;
		default:
			break;
		}
	}

	private void initData() {
		String from = getIntent().getStringExtra("from");
		if (from != null) {
			isFavAddrEdit = true;
		} else
			isFavAddrEdit = false;
		isStart = getIntent().getBooleanExtra("isStart", false);
		if (isStart) {
			String address = getIntent().getStringExtra("address");
			if (!CommonUtils.isEmpty(address)) {
				delete.setVisibility(View.VISIBLE);
				edit.setText(address);
			}
		}
	}

	private void initTitle() {

		if (isStart) {
			title_title.setText("起点");
			edit.setHint("起点");
			address_icon.setBackgroundResource(R.drawable.start_address0);
		} else {
			title_title.setText("终点");
			edit.setHint("终点");
			address_icon.setBackgroundResource(R.drawable.end_address0);
		}

		if (isFavAddrEdit) {
			title_title.setText("设置地址");
			edit.setHint("搜索");
			address_icon.setBackgroundResource(R.drawable.start_address0);
		}
		title_left.setOnClickListener(this);
		title_right.setVisibility(View.GONE);
		title_right.setOnClickListener(this);

	}

	private void initUI() {
		edit = (EditText) findViewById(R.id.edit);
		search = (LinearLayout) findViewById(R.id.search);
		search.setOnClickListener(this);
		address_icon = findViewById(R.id.address_icon);
		delete = findViewById(R.id.delete);
		progress = (ProgressBar) findViewById(R.id.progress);
		delete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				edit.setText("");
			}
		});

		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (s.length() > 0) {
					search.setVisibility(View.VISIBLE);
					delete.setVisibility(View.VISIBLE);
				} else {
					delete.setVisibility(View.GONE);
					search.setVisibility(View.GONE);
				}
			}

		});

		searchAdapter = new SelectAddressAdapter(this, null);

		getListviewObj().getListview().setAdapter(searchAdapter);
		getListviewObj().getListview().setRefreshEnabled(false);
		getListviewObj().getListview().setLoadMoreEnabled(false);
		getListviewObj().getListview().setOnItemClickListener(this);
		
		getListviewObj().setStatus(LoadingEnum.TIP,"请查询");

	}

	private void searchAddress() {
		String editstr = edit.getText().toString();
		if (editstr == null || "".equals(editstr.trim())) {
			toastMsg("请输入有效字符");
			return;
		}

		search.setVisibility(View.GONE);
		delete.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
		edit.setEnabled(false);

		query = new PoiSearch.Query(editstr, "", KingTellerApplication.getApplication().getCurAddress().getCity());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(50);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);// 设置查第一页

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(new OnPoiSearchListener() {

			/**
			 * POI信息查询回调方法
			 */
			@Override
			public void onPoiSearched(PoiResult result, int rCode) {
				// TODO Auto-generated method stub
				search.setVisibility(View.VISIBLE);
				delete.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				edit.setEnabled(true);

				if (rCode == 0) {
					if (result != null && result.getQuery() != null) {// 搜索poi的结果
						if (result.getQuery().equals(query)) {// 是否是同一条
							poiResult = result;
							// 取得搜索到的poiitems有多少页
							List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
							List<SuggestionCity> suggestionCities = poiResult
									.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

							if (poiItems != null && poiItems.size() > 0) {
								List<AddressBean> listaddress = new ArrayList<AddressBean>();
								for (int i = 0; i < poiItems.size(); i++) {
									AddressBean address = new AddressBean();
									address.setName(poiItems.get(i).getTitle());
									address.setAddress(poiItems.get(i).getSnippet());
									address.setLat(poiItems.get(i)
											.getLatLonPoint().getLatitude());
									address.setLng(poiItems.get(i)
											.getLatLonPoint().getLongitude());
									listaddress.add(address);
								}
								searchAdapter.setAddresses(listaddress);
								getListviewObj()
										.setStatus(LoadingEnum.LISTSHOW);

							} else if (suggestionCities != null
									&& suggestionCities.size() > 0) {
								// 显示城市
								getListviewObj().setStatus(LoadingEnum.NODATA);
							} else {
								// no_result
								getListviewObj().setStatus(LoadingEnum.NODATA);
							}
						}
					} else {
						// no_result
						getListviewObj().setStatus(LoadingEnum.NODATA);

					}
				} else if (rCode == 27) {
					// error_network
					getListviewObj().setStatus(LoadingEnum.NODATA);

				} else if (rCode == 32) {
					// error_key
					getListviewObj().setStatus(LoadingEnum.NODATA);
				} else {
					// error_other
					getListviewObj().setStatus(LoadingEnum.NODATA);

				}

			}

			/**
			 * POI详情查询回调方法
			 */
			@Override
			public void onPoiItemDetailSearched(PoiItemDetail arg0, int rCode) {
				// TODO Auto-generated method stub

			}
		});
		poiSearch.searchPOIAsyn();
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos,
			long arg3) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		AddressBean address = (AddressBean) adapterView.getAdapter().getItem(
				pos);

		intent.putExtra(ADDRESS, address);
		setResult(RESULT_OK, intent);
		finish();

	}

}
