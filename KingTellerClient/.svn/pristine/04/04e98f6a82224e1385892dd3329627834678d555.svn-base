package com.kingteller.client.activity.common;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.kingteller.R;
import com.kingteller.client.activity.base.BaseActivity;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.client.utils.BitmapUtils;

/**
 * 图片浏览Activity
 * @author 王定波
 *
 */
public class PicViewActivity extends BaseActivity {
	public final static String PICPATH = "picpath";
	private ImageView image;
	private Button button_left;

	@Override
	public void onCreate(Bundle paramBundle) {
		// TODO Auto-generated method stub
		super.onCreate(paramBundle);
		setContentWithOutTitle(R.layout.layout_pic_view);
		initUI();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		String picpath = getIntent().getStringExtra(PICPATH);
		image.setImageBitmap(BitmapUtils.decodeStream(picpath,
				KingTellerConfig.DefaultImgMaxWidth,
				KingTellerConfig.DefaultImgMaxHeight));

	}

	private void initUI() {
		// TODO Auto-generated method stub
		button_left = (Button) findViewById(R.id.button_left);
		button_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		image = (ImageView) findViewById(R.id.image);

	}
}
