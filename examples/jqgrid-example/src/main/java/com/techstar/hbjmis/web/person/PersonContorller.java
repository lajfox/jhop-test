package com.techstar.hbjmis.web.person;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techstar.hbjmis.entity.Person;
import com.techstar.hbjmis.service.person.PersonService;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.utils.Identities;

@Controller
@RequestMapping(value = "/person")
public class PersonContorller {

	@Autowired
	private PersonService personService;

	@RequestMapping(value = "{path1}/{path2}")
	public String list(@PathVariable("path1") String path1, @PathVariable("path2") String path2, Model model) {
		model.addAttribute("uuid", Identities.uuid2());
		return path1 + "/" + path2;
	}

	@RequestMapping(value = "/select", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results select() {
		List<Person> Persons = personService.findAll(new Sort(Sort.Direction.ASC, "name"));
		return new Response<Person>(Persons);
	}

	@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Person> spc) {
		Page<Person> Persons = personService.findAll(spc, pageable);
		return new PageResponse<Person>(Persons);
	}

}
