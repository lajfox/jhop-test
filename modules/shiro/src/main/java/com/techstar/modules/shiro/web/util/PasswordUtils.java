package com.techstar.modules.shiro.web.util;

import com.techstar.modules.security.utils.Digests;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.utils.Encodes;

/**
 * 用户密码工具类
 * 
 * @author sundoctor
 * 
 */
public final class PasswordUtils {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	/**
	 * 密码加密，使用salt经过1024次 sha-1 hash
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String entryptPassword(final String password, final byte[] salt) {
		byte[] hashPassword = Digests.sha1(password.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(hashPassword);
	}

	/**
	 * 密码加密，使用salt经过1024次 sha-1 hash
	 * 
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String entryptPassword(final String password, final String salt) {
		byte[] salts = Encodes.decodeHex(salt);
		return entryptPassword(password, salts);
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 * 
	 * @param user
	 */
	public static void entryptPassword(final ShiroUser user) {
		entryptPassword(user, user.getPlainPassword());
	}

	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 * 
	 * @param user
	 */
	public static void entryptPassword(final ShiroUser user, final String password) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(password.getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}
}
