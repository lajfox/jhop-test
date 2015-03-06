package com.techstar.hbjmis.repository.jpa;

import com.techstar.hbjmis.entity.Person;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface PersonDao extends MyJpaRepository<Person, String>{

}
