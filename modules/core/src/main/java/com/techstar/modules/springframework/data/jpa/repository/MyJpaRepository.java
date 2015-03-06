package com.techstar.modules.springframework.data.jpa.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MyJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	EntityManager getEntityManager();
	
	Class<T> getDomainClass();

	/**
	 * 删除记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param id
	 *            　主键
	 */
	<E> void delete(final Class<E> enityClass, final Serializable id);

	/**
	 * 删除记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param entity
	 *            实体
	 */
	<E> void delete(final Class<E> enityClass, final E entity);

	/**
	 * 删除记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param entities
	 *            实体集合
	 */
	<E> void delete(final Class<E> enityClass, final Iterable<? extends E> entities);

	/**
	 * 删除记录
	 * 
	 * @param ids
	 *            　主键集合
	 */
	void delete(final Collection<ID> ids);

	/**
	 * 删除记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param ids
	 *            主键集合
	 */
	<E> void delete(final Class<E> enityClass, final Collection<? extends Serializable> ids);

	/**
	 * 批量删除记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param entities
	 *            实体集合
	 */
	<E> void deleteInBatch(final Class<E> enityClass, Iterable<E> entities);

	/**
	 * 批量删除记录
	 * 
	 * @param ids
	 *            主键集合
	 */
	int deleteInBatch(final Collection<ID> ids);

	/**
	 * 批量删除记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param ids
	 *            主键集合
	 */
	<E> int deleteInBatch(final Class<E> enityClass, final Collection<? extends Serializable> ids);

	/**
	 * 删除所有记录
	 * 
	 * @param enityClass
	 *            实体类型
	 */
	<E> void deleteAll(final Class<E> enityClass);

	/**
	 * 批处理删除所有记录
	 * 
	 * @param enityClass
	 */
	<E> int deleteAllInBatch(final Class<E> enityClass);

	/**
	 * 查询记录总数
	 * 
	 * @param enityClass
	 *            实体类型
	 * @return
	 */
	<E> long count(final Class<E> enityClass);

	/**
	 * 查询记录总数
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param spec
	 *            查询参数
	 * @return
	 */
	<E> long count(final Class<E> enityClass, final Specification<E> spec);

	/**
	 * 保存记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param entity
	 *            实体
	 * @return
	 */
	<E> E save(final Class<E> enityClass, final E entity);

	/**
	 * 保存记录并刷新
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param entity
	 *            实体
	 * @return
	 */
	<E> E saveAndFlush(final Class<E> enityClass, final E entity);

	/**
	 * 保存记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param entities
	 *            实体集合
	 * @return
	 */
	<E> List<E> save(final Class<E> enityClass, final Iterable<E> entities);

	/**
	 * 根据主键查询
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param id
	 *            实体主键
	 * @return
	 */
	<E> E findOne(final Class<E> enityClass, Serializable id);

	/**
	 * 条件查询
	 * 
	 * @see findAll(Root<E>, CriteriaQuery<F>)
	 * @param root
	 * @param query
	 * @return
	 */
	<E, F> F findOne(final Root<E> root, final CriteriaQuery<F> query);

	/**
	 * 条件查询
	 * 
	 * @see findAll(Root<E>, CriteriaQuery<F>, Specification<E>)
	 * @param root
	 * @param query
	 * @param spec
	 * @return
	 */
	<E, F> F findOne(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec);

	/**
	 * 根据主键确定记录是否存在
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param id
	 *            实体主键
	 * @return
	 */
	<E> boolean exists(final Class<E> enityClass, Serializable id);

	/**
	 * 查询记录是否存在
	 * 
	 * @param spec
	 *            查询参数
	 * @return true 存在，false 不存在
	 */
	boolean exists(final Specification<T> spec);

	/**
	 * 查询记录是否存在
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param spec
	 *            查询参数
	 * @return true 存在，false 不存在
	 */
	<E> boolean exists(final Class<E> enityClass, final Specification<E> spec);

	/**
	 * 根据条件查询记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param spec
	 *            查询条件
	 * @return
	 */
	<E> E findOne(final Class<E> enityClass, Specification<E> spec);

	/**
	 * 查找所有记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @return
	 */
	<E> List<E> findAll(final Class<E> enityClass);

	/**
	 * 根据主键集合查询所有记录
	 * 
	 * @param ids
	 *            主键集合
	 * @return
	 */
	List<T> findAll(final Iterable<ID> ids);

	/**
	 * 根据主键集合查询所有记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param ids
	 *            主键集合
	 * @return
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Iterable<? extends Serializable> ids);

	/**
	 * 查找所有记录并排序
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param sort
	 *            排序参数
	 * @return
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Sort sort);

	/**
	 * 查询所有记录并分页
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param pageable
	 *            分页参数
	 * @return
	 */
	<E> Page<E> findAll(final Class<E> enityClass, final Pageable pageable);

	/**
	 * 根据条件查询所有记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param spec
	 *            查询条件参数
	 * @return
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec);

	/**
	 * 根据条件查询所有记录并排序
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param spec
	 *            查询条件参数
	 * @param sort
	 *            排序参数
	 * @return
	 */
	<E> List<E> findAll(final Class<E> enityClass, final Specification<E> spec, final Sort sort);

	/**
	 * 根据条件查询所有记录并分页排序
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param spec
	 *            查询条件参数
	 * @param pageable
	 *            分页排序参数
	 * @return
	 */
	<E> Page<E> findAll(final Class<E> enityClass, final Specification<E> spec, final Pageable pageable);

	/**
	 * 查询数据
	 * 
	 * @param root
	 * @param query
	 * @return
	 */
	<E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query);

	/**
	 * 查询数据
	 * 
	 * @param root
	 * @param query
	 * @param sort
	 *            排序参数
	 * @return
	 */
	<E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Sort sort);

	/**
	 * 查询数据
	 * 
	 * @param root
	 * @param query
	 * @param spec
	 *            查询条件参数
	 * @return
	 */
	<E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec);

	/**
	 * 查询数据
	 * 
	 * @param root
	 * @param query
	 * @param spec
	 *            查询条件参数
	 * @param sort
	 *            排序参数
	 * @return
	 */
	<E, F> List<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec,
			final Sort sort);

	/**
	 * 查询数据
	 * 
	 * @param root
	 * @param query
	 * @param spec
	 *            查询条件参数
	 * @param pageable
	 *            分页排序参数
	 * @return
	 */
	<E, F> Page<F> findAll(final Root<E> root, final CriteriaQuery<F> query, final Specification<E> spec,
			final Pageable pageable);

	/**
	 * 根据spring data jpa 规范查询记录总数
	 * 
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	Long count(final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录总数
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	<E> Long count(final Class<E> enityClass, final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录是否存在
	 * 
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	boolean exists(final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录是否存在
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	<E> boolean exists(final Class<E> enityClass, final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录
	 * 
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	T findOne(final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	<E> E findOne(final Class<E> enityClass, final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录
	 * 
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	List<T> findBy(final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录并排序
	 * 
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param sort
	 *            排序参数
	 * @param values
	 *            查询参数
	 * @return
	 */
	List<T> findBy(final String queryName, final Sort sort, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录并分页排序
	 * 
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param pageable
	 *            分页排序参数
	 * @param values
	 *            查询参数
	 * @return
	 */
	Page<T> findBy(final String queryName, final Pageable pageable, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param values
	 *            查询参数
	 * @return
	 */
	<E> List<E> findBy(final Class<E> enityClass, final String queryName, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录并排序
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param sort
	 *            排序参数
	 * @param values
	 *            查询参数
	 * @return
	 */
	<E> List<E> findBy(final Class<E> enityClass, final String queryName, final Sort sort, final Object... values);

	/**
	 * 根据spring data jpa 规范查询记录并分页排序
	 * 
	 * @param enityClass
	 *            实体类型
	 * @param queryName
	 *            按spring data jpa 规范编写，findByName=>name、findByIdAndName=>idAndName
	 * @param pageable
	 *            分页排序参数
	 * @param values
	 *            查询参数
	 * @return
	 */
	<E> Page<E> findBy(final Class<E> enityClass, final String queryName, final Pageable pageable,
			final Object... values);

}
