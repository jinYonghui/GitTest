package com.kingteller.client.utils;

import com.kingteller.client.view.ProgessDialog;

import android.content.Context;

/**
 * 进度通用工具类
 * 
 * @author 王定波
 * 
 */
public class KingTellerProgress {
	/**
	 * 关闭
	 */
	public static void closeProgress() {
		try {
			ProgessDialog.closeDialog();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 显示
	 * @param context
	 * @param msg
	 */
	public static void showProgress(Context context, CharSequence msg) {
		try {
			ProgessDialog.showDialog(context, msg);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
