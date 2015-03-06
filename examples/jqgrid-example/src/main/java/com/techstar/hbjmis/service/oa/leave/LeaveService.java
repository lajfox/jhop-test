package com.techstar.hbjmis.service.oa.leave;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.oa.Leave;
import com.techstar.hbjmis.repository.jpa.LeaveDao;
import com.techstar.modules.activiti.service.EntityWorkflowServiceImpl;
import com.techstar.modules.shiro.domain.ShiroUser;
import com.techstar.modules.shiro.web.util.SubjectUtils;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.util.SpecificationUtils;

/**
 * 请假实体管理
 * 
 * @author HenryYan
 */
@Component
@Transactional(readOnly = true)
public class LeaveService extends EntityWorkflowServiceImpl<Leave, String> {

	@Autowired
	private LeaveDao leaveDao;

	@Override
	protected MyJpaRepository<Leave, String> getMyJpaRepository() {
		return leaveDao;
	}

	public Page<Leave> findTodoTasks(Pageable pageable, Specification<Leave> spec) {

		CriteriaBuilder cb = getMyJpaRepository().getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Leave> query = cb.createQuery(Leave.class);
		Root<Leave> root = query.from(Leave.class);
		query.select(root);

		ShiroUser user = SubjectUtils.getPrincipal();
		List<String> businessKeies = getWorkflowService().findTodoBusinessKeys(user.getLoginName(), "leave");
		return CollectionUtils.isEmpty(businessKeies) ? null : this.leaveDao.findAll(root, query,
				SpecificationUtils.and(spec, root, query, cb, "idIn", businessKeies), pageable);
	}

}
