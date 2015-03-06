package com.techstar.hbjmis.repository.jpa;

import com.techstar.hbjmis.entity.Invoice;
import com.techstar.modules.springframework.data.jpa.repository.MyJpaRepository;

public interface InvoiceDao extends MyJpaRepository<Invoice, String>{

}
