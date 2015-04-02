package com.kingteller.client.bean.account;

import com.kingteller.client.KingTellerApplication;
import com.kingteller.client.config.KingTellerConfig;
import com.kingteller.framework.utils.CommonUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author 用户信息
 * 
 */
public class User {
	private String name;
	private String orgId;
	private String orgName;
	private String right;
	private String roleCode;
	private String userId;
	private String userName;
	private String password;
	private boolean push_reg;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 保存用户数据
	 * 
	 * @param context
	 * @param data
	 * @param username
	 *            用户名可为NUll
	 * @param password
	 *            密码可为NULL
	 */
	public static void SaveInfo(Context context, LoginBean data) {
		Editor sharedata = context.getSharedPreferences(
				KingTellerConfig.SHARED_PREFERENCES.USER, Context.MODE_APPEND)
				.edit();
		if (!CommonUtils.isEmpty(data.getUsername()))
			sharedata.putString("username", data.getUsername());
		if (!CommonUtils.isEmpty(data.getPassword()))
			sharedata.putString("password", data.getPassword());

		sharedata.putString("name", data.getName());
		sharedata.putString("orgId", data.getOrgId());
		sharedata.putString("orgName", data.getOrgName());
		sharedata.putString("right", data.getRight());
		sharedata.putString("roleCode", data.getRoleCode());
		sharedata.putString("userId", data.getUserId());

		sharedata.commit();
	}

	/**
	 * 保存用户数据
	 * 
	 * @param context
	 * @param data
	 */
	public static void SaveInfo(Context context, User data) {
		Editor sharedata = context.getSharedPreferences(
				KingTellerConfig.SHARED_PREFERENCES.USER, Context.MODE_APPEND)
				.edit();
		if (!CommonUtils.isEmpty(data.getUserName()))
			sharedata.putString("username", data.getUserName());
		if (!CommonUtils.isEmpty(data.getPassword()))
			sharedata.putString("password", data.getPassword());

		sharedata.putString("name", data.getName());
		sharedata.putString("orgId", data.getOrgId());
		sharedata.putString("orgName", data.getOrgName());
		sharedata.putString("right", data.getRight());
		sharedata.putString("roleCode", data.getRoleCode());
		sharedata.putString("userId", data.getUserId());

		sharedata.commit();
	}

	/**
	 * 获取用户信息
	 * 
	 * @param context
	 * @return
	 */
	public static User getInfo(Context context) {
		User data = new User();
		SharedPreferences sp = context.getSharedPreferences(
				KingTellerConfig.SHARED_PREFERENCES.USER, Context.MODE_APPEND);

		data.setUserName(sp.getString("username", ""));
		data.setPassword(sp.getString("password", ""));
		data.setName(sp.getString("name", ""));
		data.setOrgId(sp.getString("orgId", ""));
		data.setOrgName(sp.getString("orgName", ""));
		data.setRight(sp.getString("right", ""));
		data.setRoleCode(sp.getString("roleCode", ""));
		data.setUserId(sp.getString("userId", ""));
		data.setPushReg(sp.getBoolean("push_reg", false));

		return data;
	}

	public static void savePushReg(Context context, boolean pushreg) {
		Editor sharedata = context.getSharedPreferences(
				KingTellerConfig.SHARED_PREFERENCES.USER, Context.MODE_APPEND)
				.edit();

		sharedata.putBoolean("push_reg", pushreg);

		sharedata.commit();
	}

	public boolean isPushReg() {
		return push_reg;
	}

	public void setPushReg(boolean push_reg) {
		this.push_reg = push_reg;
	}

	// 得到极光别名
	public String getAlias() {
		return getUserName() + KingTellerApplication.getDeviceToken();
	}

}
