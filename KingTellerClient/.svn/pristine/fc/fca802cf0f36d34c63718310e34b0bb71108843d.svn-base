package com.kingteller.client.view;


import com.kingteller.R;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

public class ProgessDialog extends Dialog {
	private static ProgessDialog mDialog;
	public TextView message;
	private int mMessageTextColor;

	private ProgessDialog(Context context, int theme) {
		super(context, theme);

		final Resources res = getContext().getResources();
		final TypedArray a = getContext().getTheme().obtainStyledAttributes(
				null, R.styleable.DialogStyle, R.attr.sdlDialogStyle, 0);
		final int defaultMessageTextColor = res
				.getColor(R.color.sdl_message_text);

		Drawable dialogBackground = a
				.getDrawable(R.styleable.DialogStyle_dialogBackground);

		mMessageTextColor = a.getColor(
				R.styleable.DialogStyle_messageTextColor,
				defaultMessageTextColor);
		a.recycle();
		getWindow().setBackgroundDrawable(dialogBackground);

		initView();
	}

	public static void closeDialog() {
		try {
			if (mDialog != null && mDialog.isShowing())
				new Runnable() {
					public void run() {
						try {
							if (mDialog != null && mDialog.isShowing()) {
								mDialog.cancel();
								mDialog = null;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		setContentView(R.layout.dialog_part_loading);
		message = (TextView) findViewById(R.id.message);
		message.setTextColor(mMessageTextColor);
	}

	public static void showDialog(Context context, CharSequence msg) {
		if (context != null) {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.message.setText(msg);
				return;
			} else {
				mDialog = new ProgessDialog(context, R.style.SDL_Dialog);
				mDialog.message.setText(msg);
				mDialog.show();
			}
		}
	}

}