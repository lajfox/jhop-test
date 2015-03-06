package com.techstar.example.repository.jpa.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.example.entity.Account;
import com.techstar.example.entity.Account_;
import com.techstar.example.entity.Invoice;
import com.techstar.example.entity.Invoice_;
import com.techstar.example.entity.Person;
import com.techstar.example.entity.Person_;
import com.techstar.modules.springframework.test.SpringTransactionalTestCase;

@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class Jpa2Test extends SpringTransactionalTestCase {

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void in() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Account> c = cb.createQuery(Account.class);
		Root<Account> account = c.from(Account.class);
		Path<Person> owner = account.get(Account_.owner);
		Path<String> name = owner.get(Person_.name);
		c.where(cb.in(name).value("X").value("Y").value("Z"));

		TypedQuery<Account> typedQuery = entityManager.createQuery(c);
		List<Account> accounts = typedQuery.getResultList();
	}

	@Test
	public void setJoin() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Person> c = cb.createQuery(Person.class);
		Root<Person> root = c.from(Person.class);

		SetJoin<Person, Account> account = root.join(Person_.accounts);
		c.where(cb.equal(account.get(Account_.type), "A"));

		TypedQuery<Person> typedQuery = entityManager.createQuery(c);
		List<Person> persons = typedQuery.getResultList();
	}

	@Test
	public void avg() {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Double> c = cb.createQuery(Double.class);
		Root<Invoice> a = c.from(Invoice.class);
		c.select(cb.avg(a.get(Invoice_.amount)).alias("amount"));

		TypedQuery<Double> typedQuery = entityManager.createQuery(c);

		Double amount = typedQuery.getSingleResult();
	}

	@Test
	public void where() {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Invoice> c = cb.createQuery(Invoice.class);
		Root<Invoice> root = c.from(Invoice.class);
		Path<Double> amount = root.get(Invoice_.amount);
		c.where(cb.and(cb.ge(amount, 100), cb.le(amount, 200)));

		// c.select(root);

		TypedQuery<Invoice> typedQuery = entityManager.createQuery(c);
		List<Invoice> list = typedQuery.getResultList();

	}

	@Test
	public void parameter() {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Invoice> c = cb.createQuery(Invoice.class);
		Root<Invoice> root = c.from(Invoice.class);

		ParameterExpression<Integer> invno = cb.parameter(Integer.class);
		Predicate condition = cb.gt(root.get(Invoice_.invno), invno);
		c.where(condition);

		TypedQuery<Invoice> q = entityManager.createQuery(c);
		List<Invoice> result = q.setParameter(invno, 3).getResultList();
	}

	@Test
	public void construct() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<InvoiceDetail> c = cb.createQuery(InvoiceDetail.class);
		Root<Invoice> root = c.from(Invoice.class);
		c.select(cb.construct(InvoiceDetail.class, root.get(Invoice_.name), root.get(Invoice_.amount)));

		TypedQuery<InvoiceDetail> q = entityManager.createQuery(c);

		List<InvoiceDetail> list = q.getResultList();

	}

	@Test
	public void array() {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
		Root<Invoice> c = q.from(Invoice.class);
		q.select(cb.array(c.get(Invoice_.name), c.get(Invoice_.total), c.get(Invoice_.tax)));
		List<Object[]> result = entityManager.createQuery(q).getResultList();
	}

	@Test
	public void tuple() {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> q = cb.createTupleQuery();
		Root<Invoice> c = q.from(Invoice.class);

		TupleElement<String> tname = c.get(Invoice_.name).alias("name");
		TupleElement<Double> ttotal = c.get(Invoice_.total).alias("total");

		q.select(cb.tuple(c.get(Invoice_.name).alias("name"), c.get(Invoice_.total).alias("total")));

		List<Tuple> result = entityManager.createQuery(q).getResultList();

		List<TupleElement<?>> elements = result.get(0).getElements();
		for (TupleElement<?> element : elements) {
			String alias = element.getAlias();
			Object value = result.get(0).get(element);
		}

		String name = result.get(0).get(tname);
		Double total = result.get(0).get(ttotal);

	}

	@Test
	public void multiselect1() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<InvoiceDetail> c = cb.createQuery(InvoiceDetail.class);

		Root<Invoice> root = c.from(Invoice.class);
		c.multiselect(root.get(Invoice_.name), root.get(Invoice_.amount));

		TypedQuery<InvoiceDetail> q = entityManager.createQuery(c);

		List<InvoiceDetail> list = q.getResultList();
	}

	@Test
	public void multiselect2() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> c = cb.createTupleQuery();
		Root<Invoice> r = c.from(Invoice.class);
		c.multiselect(r.get(Invoice_.name).alias("name"), r.get(Invoice_.tax).alias("tax"), r.get(Invoice_.total)
				.alias("total"));

		List<Tuple> result = entityManager.createQuery(c).getResultList();
		List<TupleElement<?>> elements = result.get(0).getElements();
		for (TupleElement<?> element : elements) {
			String alias = element.getAlias();

			Object value = result.get(0).get(element);
			System.out.println(alias);
		}
	}

	@Test
	public void multiselect3() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> c = cb.createTupleQuery();
		Root<Invoice> r = c.from(Invoice.class);
		c.multiselect(cb.sum(r.get(Invoice_.amount)).alias("tamount"), cb.sum(r.get(Invoice_.tax)).alias("ttax"), cb
				.sum(r.get(Invoice_.total)).alias("ttotal"));

		List<Tuple> result = entityManager.createQuery(c).getResultList();
		List<TupleElement<?>> elements = result.get(0).getElements();
		for (TupleElement<?> element : elements) {
			String alias = element.getAlias();

			Object value = result.get(0).get(element);
			System.out.println(alias);
		}
	}

	@Test
	public void test1() throws ClassNotFoundException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		Class<Account> cls = (Class<Account>) Class.forName("com.techstar.example.entity.Account");
		Metamodel model = entityManager.getMetamodel();
		EntityType<Account> entity = model.entity(cls);

		CriteriaQuery<Account> c = cb.createQuery(cls);
		Root<Account> account = c.from(entity);
		Path<Integer> balance = account.<Integer> get("balance");
		c.where(cb.and(cb.greaterThan(balance, 100), cb.lessThan(balance, 200)));

		List<Account> result = entityManager.createQuery(c).getResultList();
	}

	// @Test
	public void function() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> q = cb.createTupleQuery();
		Root<Account> c = q.from(Account.class);
		Expression<Date> currentUser = cb.function("CURDATE", Date.class, (Expression<?>[]) null);
		q.multiselect(currentUser, c.get(Account_.createtime));

		List<Tuple> result = entityManager.createQuery(q).getResultList();
	}

	@Test
	public void editing() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Person> c = cb.createQuery(Person.class);
		Root<Person> p = c.from(Person.class);
		c.orderBy(cb.asc(p.get(Person_.name)));
		List<Person> result = entityManager.createQuery(c).getResultList();

		// start editing
		List<Order> orders = c.getOrderList();
		List<Order> newOrders = new ArrayList<Order>(orders);
		newOrders.add(cb.desc(p.get(Person_.phone)));
		c.orderBy(newOrders);
		List<Person> result2 = entityManager.createQuery(c).getResultList();
	}

	static class InvoiceDetail {
		private String name;
		private Double amount;

		public InvoiceDetail(String name, Double amount) {
			this.name = name;
			this.amount = amount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

	}
}
