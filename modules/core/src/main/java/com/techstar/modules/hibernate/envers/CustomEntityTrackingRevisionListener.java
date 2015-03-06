package com.techstar.modules.hibernate.envers;

import java.io.Serializable;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;

public class CustomEntityTrackingRevisionListener implements EntityTrackingRevisionListener {
	@Override
	public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType,
			Object revisionEntity) {
		CustomTrackingRevEntity revEntity = (CustomTrackingRevEntity) revisionEntity;
		
		revEntity.getModifiedEntityNames().add(entityName);
		revEntity.setEntityName(entityName);
	}

	@Override
	public void newRevision(Object revisionEntity) {
		if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null) {
			CustomTrackingRevEntity revEntity = (CustomTrackingRevEntity) revisionEntity;
			revEntity.setLastModifiedDate(new Date());
			revEntity.setLastModifiedBy(SecurityUtils.getSubject().getPrincipal().toString());
		} else {
			throw new UnknownAccountException("user not login in the system");
		}		
	}
}
