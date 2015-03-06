package com.techstar.hbjmis.service.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.techstar.hbjmis.entity.Book;
import com.techstar.hbjmis.repository.jpa.BookDao;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;
import com.techstar.modules.springframework.data.jpa.service.MyJpaServiceImpl;

@Component
@Transactional(readOnly = true)
public class BookService extends MyJpaServiceImpl<Book, String> {

	@Autowired
	private BookDao bookDao;

	@Override
	protected MyJpaRepository<Book, String> getMyJpaRepository() {
		return bookDao;
	}

}
