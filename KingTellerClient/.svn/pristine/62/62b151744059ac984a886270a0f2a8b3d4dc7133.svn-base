package com.kingteller.framework.utils;

import java.security.MessageDigest;


public class EncroptionUtils {
	// 加密
	public static String EncryptSHA(String strPwd) {
		String strEncrypted = "";
		if (CommonUtils.isEmpty(strPwd)) {
			return strEncrypted;
		} else {
			try {
				byte shaKeyBytes[] = { -93, 53, 72, 42, 33, 36, -125, -26, -65,
						-77, 114, -8, 65, -33, 12, -53 };
				MessageDigest alga = MessageDigest.getInstance("SHA-1");
				alga.update(shaKeyBytes);
				alga.update(strPwd.getBytes());
				alga.update(shaKeyBytes);
				byte digesta[] = alga.digest();
				strEncrypted = CommonUtils.byte2hex(digesta);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return strEncrypted;

		}
	}

}
