package com.kingteller.client.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 自定义滚动视图
 * @author 王定波
 *
 */
public class KingTellerScrollView extends ScrollView {

	private ScrollViewListener scrollViewListener = null;

	public KingTellerScrollView(Context context) {
		super(context);
	}

	public KingTellerScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public KingTellerScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public interface ScrollViewListener {
		public void onScrollChanged(KingTellerScrollView scrollView, int x,
				int y, int oldx, int oldy);
	}

}