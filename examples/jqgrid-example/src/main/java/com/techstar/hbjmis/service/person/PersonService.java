package com.techstar.hbjmis.service.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.Person;
import com.techstar.hbjmis.repository.jpa.PersonDao;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;

@Component
@Transactional(readOnly = true)
public class PersonService extends MyJpaServiceImpl<Person, String> {

	@Autowired
	private PersonDao personDao;

	@Override
	protected MyJpaRepository<Person, String> getMyJpaRepository() {
		return personDao;
	}

}
