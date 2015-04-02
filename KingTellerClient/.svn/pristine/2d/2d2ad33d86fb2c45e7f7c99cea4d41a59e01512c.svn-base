package com.kingteller.client.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kingteller.R;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.framework.utils.CommonUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FunctionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<String> list = new ArrayList<String>();
	private Context context;
	private List<String> menulist = new ArrayList<String>();
	private int type;
	public static final int MAINMENU = 1;
	public static final int MAPMENU = 2;
	public static final int WLJKMENU = 3;

	/**
	 * 
	 * @param context
	 * @param rightstr
	 * @param type
	 *            1主菜单，2地图菜单
	 */
	public FunctionAdapter(Context context, String rightstr, int type) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.type = type;
		initData();
		setData(rightstr);
		
	}

	private void initData() {
		// TODO Auto-generated method stub

		if (type == MAINMENU) {// 初始化主菜单
			menulist.add("MOBILE_JNKH");
			menulist.add("MOBILE_RWD");
			menulist.add("MOBILE_ZSK");
			menulist.add("MOBILE_SPBG");
			menulist.add("MOBILE_SJK");
			menulist.add("MOBILE_ATMWZ");
			menulist.add("MOBILE_MAP");
			menulist.add("notice");
			menulist.add("saleshow");
			menulist.add("mysale");
			menulist.add("saleshen");
			menulist.add("MOBILE_PROJECTSTATISTIC");
			menulist.add("MOBILE_PROJECTORDERAUDIT");
			menulist.add("MOBILE_PROJECTDEALORDER");
			menulist.add("MOBILE_ASSIGN");
			menulist.add("WLJK_MOBILE");
			menulist.add("MOBILE_PXKHSJ");
			
			
		} else if (type == MAPMENU) {

			// 初始化地图菜单
			menulist.add("MOBILE_MAP_USER");
			menulist.add("MOBILE_MAP_FWZ");
			menulist.add("MOBILE_MAP_LOCATION");
			menulist.add("MOBILE_MAP_MACHINE");
			menulist.add("MOBILE_MAP_DF");
		}else if(type == WLJKMENU){
			menulist.add("WLJK_MOBILE_HDGL");
			menulist.add("WLJK_MOBILE_TXQTSWBG");
			menulist.add("WLJK_MOBILE_QTSWGL");
			menulist.add("WLJK_MOBILE_TXRWBG");
		}
	}

	public void setData(String rightstr) {
		// TODO Auto-generated method stub
		
		
			list = new ArrayList<String>();
		if (!CommonUtils.isEmpty(rightstr)) {
			{
				String[] allright = rightstr.split(",");
				for (int i = 0; i < allright.length; i++) {
					if (context.getResources().getIdentifier(allright[i],
							"string", context.getPackageName()) != 0 && menulist.contains(allright[i]))
						list.add(allright[i]);
				}
			}
		} 
		
		if(type == MAINMENU)
			list.add("notice");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null)
			return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHoler viewHoler;
		if (convertView == null) {
			viewHoler = new ViewHoler();
			convertView = inflater.inflate(R.layout.item_toolbar, null);
			viewHoler.item_icon = (ImageView) convertView
					.findViewById(R.id.item_icon);
			viewHoler.item_title = (TextView) convertView
					.findViewById(R.id.item_title);

			convertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) convertView.getTag();
		}
		viewHoler.item_title.setText(context.getResources().getIdentifier(
				list.get(position), "string", context.getPackageName()));

		viewHoler.item_icon.setImageResource(context.getResources()
				.getIdentifier("icon_" + list.get(position).toLowerCase(),
						"drawable", context.getPackageName()));

		return convertView;
	}

	private static class ViewHoler {

		public TextView item_title;
		public ImageView item_icon;

	}

}
