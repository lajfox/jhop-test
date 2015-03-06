package com.techstar.modules.hibernate.envers;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.hibernate.envers.RevisionListener;

public class CustomRevisionListener implements RevisionListener {
	public void newRevision(Object revisionEntity) {
		if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null) {
			CustomRevEntity revEntity = (CustomRevEntity) revisionEntity;
			revEntity.setLastModifiedDate(new Date());
			revEntity.setLastModifiedBy(SecurityUtils.getSubject().getPrincipal().toString());
		} else {
			throw new UnknownAccountException("user not login in the system");
		}
	}

}
