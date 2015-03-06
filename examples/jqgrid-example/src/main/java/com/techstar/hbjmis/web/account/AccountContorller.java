package com.techstar.hbjmis.web.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.hbjmis.entity.Account;
import com.techstar.hbjmis.entity.Person;
import com.techstar.hbjmis.service.account.AccountService;
import com.techstar.modules.mybatis.domain.MyRowBounds;
import com.techstar.modules.mybatis.domain.Page;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.QuerySpecification;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.utils.Identities;

@Controller
@RequestMapping(value = "/acc")
public class AccountContorller {

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "{path1}/{path2}")
	public String list(@PathVariable("path1") String path1, @PathVariable("path2") String path2, Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return path1 + "/" + path2;
	}

	@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results search(Specification<Account> spec) {
		// Sort sort = new Sort(Sort.Direction.ASC, "order");
		List<Account> accounts = accountService.findAll(spec, new Sort(Sort.Direction.ASC, "orderno"), true);
		return new Response<Account>(accounts);
	}

	// @RequiresPermissions("account:edit")
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody
	Results edit(@Valid @PreModelAttribute(disallowedFields = { "parent", "owner" }) Account account,
			@RequestParam(value = "oper", required = false) String oper,
			@RequestParam(value = "parent", required = false) String parent,
			@RequestParam(value = "personid", required = false) String personid) {

		if (StringUtils.equals("del", oper)) {
			this.accountService.deletex(account);
		} else {
			if (StringUtils.equals("add", oper)) {
				account.setId(null);
			}
			if (StringUtils.isNotEmpty(parent)) {
				account.setParent(accountService.findOne(parent));
			}
			if (StringUtils.isNotEmpty(personid)) {
				account.setOwner(accountService.findOne(Person.class, personid));
			}

			accountService.savex(account);
		}

		return new Results(true, account);
	}

	/**
	 * mybatis例子
	 * 
	 * @param rowBounds
	 * @return
	 */
	@RequestMapping(value = "/mybatis/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results mybatissearch(@PageableDefaults(pageNumber = 0, value = 10) MyRowBounds rowBounds) {
		Map<String, Object> params = new HashMap<String, Object>();
		// params.put("name", "Cash");
		// params.put("personname", "张三");

		Page<Map<String, Object>> accounts = accountService.get(params, rowBounds);
		return new PageResponse<Map<String, Object>>(accounts);
	}

	@RequestMapping(value = "/mybatis/summary", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results summary(@PageableDefaults(pageNumber = 0, value = 10) MyRowBounds rowBounds) {
		Map<String, Object> params = new HashMap<String, Object>();
		// params.put("name", "Cash");
		// params.put("personname", "张三");

		Page<Map<String, Object>> accounts = accountService.get(params, rowBounds);
		Map<String, Object> summary = null;
		if (accounts.hasContent()) {
			rowBounds.setPagination(false);// false 不分页
			summary = accountService.summary(params, rowBounds);
		}

		return new PageResponse<Map<String, Object>>(accounts, summary);
	}

	/**
	 * JQPL查询例子
	 * 
	 * @param pageable
	 * @param spec
	 * @return
	 */
	@RequestMapping(value = "/jpql/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results jpqlsearch(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, QuerySpecification spec) {

		org.springframework.data.domain.Page<Map<String, Object>> accounts = accountService.findByJPQL(pageable, spec);
		return new PageResponse<Map<String, Object>>(accounts);
	}

	/**
	 * SQL查询例子
	 * 
	 * @param pageable
	 * @param spec
	 * @return
	 */
	@RequestMapping(value = "/sql/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results sqlsearch(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, QuerySpecification spec) {

		org.springframework.data.domain.Page<Map<String, Object>> accounts = accountService.findBySQL(pageable, spec);
		return new PageResponse<Map<String, Object>>(accounts);
	}

}
