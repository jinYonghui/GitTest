package com.kingteller.client.activity.base;

import com.kingteller.R;
import com.kingteller.client.view.ListViewObj;
import com.kingteller.framework.utils.CommonUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * Fragment的基类
 * @author 王定波
 *
 */
public class BaseFragment extends Fragment {
	private ListViewObj listviewObj;

	public void toastMsg(String str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (getView().findViewById(R.id.loading_view) != null)
			setListviewObj(new ListViewObj(getView()));
	}

	public ListViewObj getListviewObj() {
		return listviewObj;
	}

	public void setListviewObj(ListViewObj listviewObj) {
		this.listviewObj = listviewObj;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		try {
			super.onResume();
			// 检查网络是否可用
			if (getView().findViewById(R.id.net_error) != null) {
				if (!CommonUtils.isNetAvaliable(getActivity()))
					getView().findViewById(R.id.net_error).setVisibility(
							View.VISIBLE);
				else
					getView().findViewById(R.id.net_error).setVisibility(
							View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
