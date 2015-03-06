package com.techstar.hbjmis.repository.jpa;

import com.techstar.hbjmis.entity.Book;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface BookDao extends MyJpaRepository<Book, String>{

}
