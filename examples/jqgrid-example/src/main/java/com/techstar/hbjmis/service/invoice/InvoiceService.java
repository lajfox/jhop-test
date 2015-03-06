package com.techstar.hbjmis.service.invoice;

import java.util.Map;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.Invoice;
import com.techstar.hbjmis.entity.Invoice_;
import com.techstar.hbjmis.repository.jpa.InvoiceDao;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;
import com.techstar.modules.springframework.data.jpa.util.TupleUtils;

@Component
@Transactional(readOnly = true)
public class InvoiceService extends MyJpaServiceImpl<Invoice, String> {

	@Autowired
	private InvoiceDao invoiceDao;

	@Override
	protected MyJpaRepository<Invoice, String> getMyJpaRepository() {
		return invoiceDao;
	}

	public Map<String, Object> sum(final Specification<Invoice> spec) {

		CriteriaBuilder cb = getMyJpaRepository().getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();
		Root<Invoice> root = query.from(Invoice.class);
		query.select(cb.tuple(cb.sum(root.get(Invoice_.amount)).alias("amount"),
				cb.sum(root.get(Invoice_.tax)).alias("tax"), cb.sum(root.get(Invoice_.total)).alias("total")));

		return TupleUtils.build(invoiceDao.findOne(root, query, spec));
	}

}
