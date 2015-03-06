package com.techstar.modules.shiro.web.tags;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.techstar.modules.shiro.web.util.WebUtils;

public class PrincipalTag extends org.apache.shiro.web.tags.PrincipalTag {

	private static final long serialVersionUID = 3436558273295373853L;
	private static final Logger log = LoggerFactory.getLogger(PrincipalTag.class);

	public int onDoStartTag() throws JspException {
		String strValue = null;

		if (getSubject() != null) {

			// Get the principal to print out
			Object principal;

			if (getType() == null) {
				principal = getSubject().getPrincipal();
			} else {
				principal = getPrincipalFromClassName();
			}

			// Get the string value of the principal
			if (principal != null) {
				if (getProperty() == null) {
					strValue = principal.toString();
				} else {
					strValue = getPrincipalProperty(principal, getProperty());
				}
			}

		}

		if (StringUtils.isEmpty(strValue) || StringUtils.equals(strValue, "null")) {
			HttpServletRequest req = WebUtils.toHttp(pageContext.getRequest());
			HttpServletResponse res = WebUtils.toHttp(pageContext.getResponse());
			try {
				res.sendRedirect(req.getContextPath() + "/logout");
			} catch (IOException e) {
				throw new JspTagException("转向登录页.", e);
			}
		} else {
			try {
				pageContext.getOut().write(strValue);
			} catch (IOException e) {
				throw new JspTagException("Error writing [" + strValue + "] to JSP.", e);
			}
		}

		return SKIP_BODY;
	}

	private Object getPrincipalFromClassName() {
		Object principal = null;

		try {
			Class<?> cls = Class.forName(getType());
			principal = getSubject().getPrincipals().oneByType(cls);
		} catch (ClassNotFoundException e) {
			if (log.isErrorEnabled()) {
				log.error("Unable to find class for name [" + getType() + "]");
			}
		}
		return principal;
	}

	private String getPrincipalProperty(Object principal, String property) throws JspTagException {
		String strValue = null;

		try {
			BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

			// Loop through the properties to get the string value of the
			// specified property
			boolean foundProperty = false;
			for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
				if (pd.getName().equals(property)) {
					Object value = pd.getReadMethod().invoke(principal, (Object[]) null);
					strValue = String.valueOf(value);
					foundProperty = true;
					break;
				}
			}

			if (!foundProperty) {
				final String message = "Property [" + property + "] not found in principal of type ["
						+ principal.getClass().getName() + "]";
				if (log.isErrorEnabled()) {
					log.error(message);
				}
				throw new JspTagException(message);
			}

		} catch (Exception e) {
			final String message = "Error reading property [" + property + "] from principal of type ["
					+ principal.getClass().getName() + "]";
			if (log.isErrorEnabled()) {
				log.error(message, e);
			}
			throw new JspTagException(message, e);
		}

		return strValue;
	}
}
