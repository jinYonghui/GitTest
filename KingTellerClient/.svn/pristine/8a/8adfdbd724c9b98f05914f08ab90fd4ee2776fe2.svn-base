package com.kingteller.framework.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.kingteller.framework.http.FilesMultipartEntity.ProgressListener;

/**
 * 上传多文件需要这个参数设置
 * @author 王定波
 * 需要包httpcore-4.3.2.jar、httpmime-4.3.2.jar
 */
public class AjaxFilesParams extends AjaxParams {
	private static String ENCODING = "UTF-8";

	protected ConcurrentHashMap<String, String> urlParams;
	protected ConcurrentHashMap<String, List<File>> filesParams;
	private AjaxCallBack<? extends Object>  callback;

	public AjaxFilesParams() {
		init();
	}

	public AjaxFilesParams(Map<String, String> source) {
		init();

		for (Map.Entry<String, String> entry : source.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public AjaxFilesParams(String key, String value) {
		init();
		put(key, value);
	}

	public AjaxFilesParams(Object... keysAndValues) {
		init();
		int len = keysAndValues.length;
		if (len % 2 != 0)
			throw new IllegalArgumentException(
					"Supplied arguments must be even");
		for (int i = 0; i < len; i += 2) {
			String key = String.valueOf(keysAndValues[i]);
			String val = String.valueOf(keysAndValues[i + 1]);
			put(key, val);
		}
	}

	public void put(String key, String value) {
		if (key != null && value != null) {
			urlParams.put(key, value);
		}
	}

	/**
	 * 添加 inputStream 到请求中.
	 * 
	 * @param key
	 *            the key name for the new param.
	 * @param stream
	 *            the input stream to add.
	 * @param fileName
	 *            the name of the file.
	 * @param contentType
	 *            the content type of the file, eg. application/json
	 */
	public void put(String key, File file) {
		if (key != null && file!=null && file.exists()) {
			if (!filesParams.containsKey(key)) {
				List<File> files = new ArrayList<File>();
				files.add(file);
				filesParams.put(key, files);
			} else {
				List<File> files = filesParams.get(key);
				files.add(file);
				filesParams.put(key, files);
			}
		}
	}

	public void remove(String key) {
		urlParams.remove(key);
		filesParams.remove(key);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParams
				.entrySet()) {
			if (result.length() > 0)
				result.append("&");

			result.append(entry.getKey());
			result.append("=");
			result.append(entry.getValue());
		}

		for (ConcurrentHashMap.Entry<String, List<File>> entry : filesParams
				.entrySet()) {
			if (result.length() > 0)
				result.append("&");

			result.append(entry.getKey());
			result.append("=");
			result.append("FILE");
		}

		return result.toString();
	}

	/**
	 * Returns an HttpEntity containing all request parameters
	 */
	@SuppressWarnings("deprecation")
	public HttpEntity getEntity() {
		HttpEntity entity = null;

		if (!filesParams.isEmpty()) {
			// MultipartEntity multipartEntity = new MultipartEntity();
			FilesMultipartEntity multipartEntity =new FilesMultipartEntity(new ProgressListener() {
				
				@Override
				public void onProgess(long total, long num) {
					// TODO Auto-generated method stub
					if(callback!=null)
					callback.onUplodinging(total, num);
				}
			});
			// Add string params
			for (ConcurrentHashMap.Entry<String, String> entry : urlParams
					.entrySet()) {
				multipartEntity.addPart(
						entry.getKey(),
						new StringBody(entry.getValue(), ContentType.create(
								"text/plain", Consts.UTF_8)));
			}

			for (ConcurrentHashMap.Entry<String, List<File>> entry : filesParams
					.entrySet()) {
				List<File> files = entry.getValue();

				for (int i = 0; i < files.size(); i++) {
					File file = files.get(i);
					if (file.exists()) {
						multipartEntity.addPart(entry.getKey(), new FileBody(file));
					}
				}

			}

			entity = multipartEntity;
		} else {
			try {
				entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return entity;
	}
	
	public void setUploadCallBack(AjaxCallBack<? extends Object> callBack)
	{
		this.callback=callBack;
	}
	
	private void init() {
		urlParams = new ConcurrentHashMap<String, String>();
		filesParams = new ConcurrentHashMap<String, List<File>>();
	}


}