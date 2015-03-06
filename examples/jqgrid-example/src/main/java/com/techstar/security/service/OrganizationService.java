package com.techstar.security.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.JsonTreeJpaServiceImpl;
import com.techstar.security.entity.Organization;
import com.techstar.security.entity.Permission;
import com.techstar.security.entity.User;
import com.techstar.security.repository.jpa.OrganizationDao;

/**
 * 电揽台帐业务类
 * 
 * @date 2012-10-30 上午10:11:33
 * @author lrm
 */
@Component
@Transactional(readOnly = true)
public class OrganizationService extends JsonTreeJpaServiceImpl<Organization, String> {

    private OrganizationDao organizationDao;

    @Autowired
    public void setOrganizationDao(OrganizationDao organizationDao) {
	this.organizationDao = organizationDao;
    }

    @Override
    protected MyJpaRepository<Organization, String> getMyJpaRepository() {
	return organizationDao;
    }

    /***
     * 构造ZTree数据-全部组织机构
     * 
     * @Date 2013-5-8 下午2:31:22
     * @author lrm
     */
    public List<Map<String, Object>> findAllOrganizations() {
	List<Map<String, Object>> organizations = organizationDao.getAll();
	if (CollectionUtils.isNotEmpty(organizations)) {
	    for (Map<String, Object> map : organizations) {
		map.put("checked", false);
//		map.put("open", MapUtils.getIntValue(map, "level_") == 0 ? true : false);
		map.put("open",false);
	    }
	}
	return organizations;
    } 

    /***
     * 删除组织机构前了清理用户的数据
     * 
     * @Date 2013-5-28 下午2:32:53
     * @author lrm
     */
    @Transactional(readOnly = false)
    public void delete(Organization organization) {
	this.deletex(organization);
    }

    /***
     * 删除组织机构前了清理用户的数据
     * 
     * @Date 2013-5-28 下午2:32:53
     * @author lrm
     */
    @Transactional(readOnly = false)
    public void delete(List<String> ids) {
	for (String id : ids) {
	    Organization organization = this.findOne(id);  
	    this.deletex(organization);
	}
    }
    
    /***
     * 查询除当前节点及子结点以外的数据
     * 
     * @Date 2013-5-30 下午2:25:21
     * @author lrm
     */
    public List<Map<String, Object>> findByIdAndSonNot(final String id){
	List<Map<String, Object>> organizations = organizationDao.findByIdAndSonNot(id,id);
	if (CollectionUtils.isNotEmpty(organizations)) {
	    for (Map<String, Object> map : organizations) {
		map.put("checked", MapUtils.getIntValue(map, "cnt") == 0 ? false : true);
//		map.put("open", MapUtils.getIntValue(map, "level_") == 0 ? true : false);
		map.put("open",false);
	    }
	}
	return organizations;
    }
    
    /***
     * 组织机构信息维护
     * 
     * @Date 2013-5-30 上午11:47:19
     * @author lrm
     */
    @Transactional(readOnly = false)
    public Organization saveOrganization(Organization organization, String oper, String parentid) {
	if (StringUtils.equals("add", oper)) {
	    organization.setId(null);
	}
	// 在迁移过程中，前父结点的level需要修改
	Organization parent = organization.getParent();

	if (StringUtils.isNotEmpty(parentid)) {
	    organization.setParent(this.findOne(parentid));
	} else {
	    organization.setParent(null);
	}
	this.savex(organization);

	// 在迁移过程中，前父结点的level需要修改
	if (parent != null && (StringUtils.isEmpty(parentid) || !parent.getId().equals(parentid))) {
	    parent.getChildrens().remove(organization);
	    if (!parent.hasChildren()) {
		parent.setLeaf(true);
		this.save(parent);
	    }
	}
	return organization;
    }

}
