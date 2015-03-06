package com.techstar.modules.shiro.web.util;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * shiro 登录用户工具类
 * 
 * @author sundoctor
 * 
 */
public final class SubjectUtils {

	public static boolean hasRole(final String roleName) {
		return StringUtils.isEmpty(roleName) ? false : getSubject().hasRole(StringUtils.trim(roleName));
	}

	public static boolean hasAnyRoles(final Collection<String> roleNames) {
		boolean hasAnyRole = false;
		if (CollectionUtils.isNotEmpty(roleNames)) {
			for (String roleName : roleNames) {
				if (hasRole(roleName)) {
					hasAnyRole = true;
					break;
				}
			}
		}
		return hasAnyRole;
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 获取登录用户
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPrincipal() {
		return (T) getSubject().getPrincipal();
	}

	/**
	 * 获取登录用户
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getPrincipal(Class<T> clazz) {
		return (T) getSubject().getPrincipal();
	}
}
