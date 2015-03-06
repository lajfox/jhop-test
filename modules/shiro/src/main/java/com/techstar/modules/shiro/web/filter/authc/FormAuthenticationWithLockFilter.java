package com.techstar.modules.shiro.web.filter.authc;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techstar.modules.shiro.authc.VerifyCodeException;
import com.techstar.modules.springframework.data.jpa.domain.Results;

/**
 * 自己实现的formAuthcFilter，加入失败登陆次数限制，锁定用户帐号
 * 
 * @author sundoctor
 * 
 */

public class FormAuthenticationWithLockFilter extends FormAuthenticationWithAjaxFilter {

	public static final ConcurrentMap<String, AtomicLong> accountLockMap = new ConcurrentHashMap<String, AtomicLong>();
	private static final ConcurrentMap<String, Long> lockTimeMap = new ConcurrentHashMap<String, Long>();

	private static final Logger log = LoggerFactory.getLogger(FormAuthenticationWithLockFilter.class);
	private long maxLoginAttempts = 10;
	private long unlockTime = 30; // 自动解锁时间,默认为３０分钟，当期<=0时不自动解锁，需要管理员后台手动解锁

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

		request.setAttribute("maxLoginAttempts", maxLoginAttempts);
		AuthenticationToken token = createToken(request, response);
		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			log.error(msg);
			throw new IllegalStateException(msg);
		}

		if (verifyCode(request, token)) {
			unlockAccount(request);

			if (checkIfAccountLocked(request)) {
				return onLoginFailure(token, new ExcessiveAttemptsException(), request, response);
			} else {
				if (!doLogin(request, response, token)) {
					resetAccountLock(getUsername(request));
					return false;
				}
				return true;
			}
		} else {
			return onLoginFailure(token, new VerifyCodeException("验证码错误"), request, response);
		}
	}

	/**
	 * 根据unlockTime解锁用户,到期自动解锁用户
	 * 
	 * @param request
	 */
	private void unlockAccount(ServletRequest request) {
		String username = getUsername(request);
		if (unlockTime > 0 && lockTimeMap.get(username) != null) {
			Date date = new Date();
			if (date.getTime() - lockTimeMap.get(username) >= unlockTime * 60000) {
				lockTimeMap.remove(username);
				resetAccountLock(username);
			}
		}
	}

	/**
	 * 验证当前用户是否己锁定
	 * 
	 * @param request
	 * @return
	 */
	private boolean checkIfAccountLocked(ServletRequest request) {
		String username = getUsername(request);
		if (accountLockMap.get(username) != null) {
			long remainLoginAttempts = accountLockMap.get(username).get();
			if (remainLoginAttempts <= 0) {
				// 将用户的锁定时时间存入lockTimeMap
				lockTimeMap.put(username, new Date().getTime());

				return true;
			}
		}
		return false;
	}

	private boolean doLogin(ServletRequest request, ServletResponse response, AuthenticationToken token)
			throws Exception {
		try {
			Subject subject = getSubject(request, response);
			subject.login(token);
			return onLoginSuccess(token, subject, request, response);
		} catch (IncorrectCredentialsException e) {
			decreaseAccountLoginAttempts(request);
			checkIfAccountLocked(request);
			return onLoginFailure(token, e, request, response);
		} catch (AuthenticationException e) {
			return onLoginFailure(token, e, request, response);
		}
	}

	private void decreaseAccountLoginAttempts(ServletRequest request) {
		AtomicLong initValue = new AtomicLong(maxLoginAttempts);
		AtomicLong remainLoginAttempts = accountLockMap.putIfAbsent(getUsername(request), new AtomicLong(
				maxLoginAttempts));
		if (remainLoginAttempts == null) {
			remainLoginAttempts = initValue;
		}
		remainLoginAttempts.getAndDecrement();
		accountLockMap.put(getUsername(request), remainLoginAttempts);
	}

	public void resetAccountLock(String username) {
		accountLockMap.put(username, new AtomicLong(maxLoginAttempts));
	}

	public void setMaxLoginAttempts(long maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}

	public void setUnlockTime(long unlockTime) {
		this.unlockTime = unlockTime;
	}

	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		request.setAttribute(getFailureKeyAttribute(), getMessage(ae));
	}

	@Override
	protected void setFailureAttribute(final Results results, AuthenticationException ae) {
		results.setMessage(getMessage(ae));
	}

	private String getMessage(AuthenticationException ae) {
		String className = ae.getClass().getName();
		String message = proterties.getProperty(this.getClass().getSimpleName() + "." + className);
		if (StringUtils.isEmpty(message)) {
			message = proterties
					.getProperty("FormAuthenticationWithLockFilter.org.apache.shiro.authc.AccountException");
		}
		if (ae instanceof ExcessiveAttemptsException) {
			if (unlockTime <= 0) {
				message += ",请联系系统管理员";
			} else {
				message += ",请" + unlockTime + "分钟后再试";
			}
		}

		return StringUtils.isEmpty(message) ? "登录失败，帐号或者密码错误" : String.format(message, maxLoginAttempts);
	}
}
