package com.techstar.modules.springframework.data.jpa.repository.support;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.query.MyJpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.QueryExtractor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.MySimpleJpaRepository;
import org.springframework.data.jpa.repository.support.PersistenceProvider;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

public class MyJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		JpaRepositoryFactoryBean<T, S, ID> {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new MyRepositoryFactory2<T, ID>(entityManager);
	}

	private static class MyRepositoryFactory2<T, I extends Serializable> extends JpaRepositoryFactory {
		private final EntityManager entityManager;
		private final QueryExtractor extractor;

		public MyRepositoryFactory2(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
			this.extractor = PersistenceProvider.fromEntityManager(entityManager);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new MySimpleJpaRepository<T, I>((Class<T>) metadata.getDomainType(), entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
				return QueryDslJpaRepository.class;
			} else {
				return MySimpleJpaRepository.class;
			}
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.data.repository.support.RepositoryFactorySupport#
		 * getQueryLookupStrategy
		 * (org.springframework.data.repository.query.QueryLookupStrategy.Key)
		 */
		@Override
		protected QueryLookupStrategy getQueryLookupStrategy(Key key) {
			return MyJpaQueryLookupStrategy.create(entityManager, key, extractor);
		}		

		private boolean isQueryDslExecutor(Class<?> repositoryInterface) {

			return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
		}
	}

}
