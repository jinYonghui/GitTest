package com.kingteller.client.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import com.kingteller.client.config.KingTellerConfig;

/**
 * 文件工具类
 * @author 王定波
 *
 */
public final class FileUtils {

	/**
	 * 读取文件
	 * @param fileName
	 * @return
	 */
	public static String readFile(String fileName) {
		String res = null;
		try {
			File file = new File(KingTellerConfig.CACHE_PATH.SD_DADA +"/"+ fileName);
			FileInputStream fis = new FileInputStream(file);

			int length = fis.available();

			byte[] buffer = new byte[length];
			fis.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fis.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return res;
	}

	/**
	 * 写文件
	 * @param fileName
	 * @param write_str
	 */
	public static void writeFile(String fileName, String write_str) {

		try {
			File file = new File(KingTellerConfig.CACHE_PATH.SD_DADA +"/"+fileName);

			FileOutputStream fos;
			fos = new FileOutputStream(file);
			byte[] bytes = write_str.getBytes();
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 写文件
	 * @param fileName
	 * @param write_str
	 */
	public static void writeTxtFile(String fileName, String write_str) {

		try {
			File file = null;
			if(FileExist(fileName)){	
			}else{	
				file = new File(KingTellerConfig.CACHE_PATH.SD_DADA +"/"+fileName);
			}

			FileOutputStream fos;
			fos = new FileOutputStream(file);
			byte[] bytes = write_str.getBytes();
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean FileExist(String fileName) {
		File file = new File(KingTellerConfig.CACHE_PATH.SD_DADA +"/"+ fileName);
		return file.exists();
	}

	/**
	 * 读取Asset下的文件
	 * @param context
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readAssetFile(Context context, String fileName)
			throws IOException {

		String res = "";
		InputStream in = context.getResources().getAssets().open(fileName);

		int length = in.available();
		byte[] buffer = new byte[length];
		in.read(buffer);
		res = EncodingUtils.getString(buffer, "UTF-8");

		return res;

	}

}
