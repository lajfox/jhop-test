package com.techstar.hbjmis.web.invoice;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.techstar.hbjmis.entity.Invoice;
import com.techstar.hbjmis.entity.Item;
import com.techstar.hbjmis.service.invoice.InvoiceService;
import com.techstar.modules.jackson.mapper.JsonMapper;
import com.techstar.modules.springframework.data.jpa.domain.PageResponse;
import com.techstar.modules.springframework.data.jpa.domain.Response;
import com.techstar.modules.springframework.data.jpa.domain.Results;
import com.techstar.modules.springframework.web.bind.annotation.PreModelAttribute;
import com.techstar.modules.springframework.web.util.WebUtils;
import com.techstar.modules.utils.Identities;

@Controller
@RequestMapping(value = "/invoice")
public class InvoiceContorller {

	private static final JsonMapper BINDER = JsonMapper.nonDefaultMapper();
	private static final Map<String, String> shipviaMap = Maps.<String, String> newHashMap();
	static {
		shipviaMap.put("FE", "FedEx");
		shipviaMap.put("TN", "TNT");
		shipviaMap.put("IN", "Intime");
	}

	@Autowired
	private InvoiceService invoiceService;

	@RequestMapping(value = "{path1}/{path2}")
	public String list(@PathVariable("path1") String path1, @PathVariable("path2") String path2, Model model) {
		String json = BINDER.toJson(shipviaMap);
		model.addAttribute("shipviaJson", json);
		
		String shipviaValue = WebUtils.toValue(shipviaMap);
		model.addAttribute("shipviaValue", shipviaValue);
		model.addAttribute("uuid", Identities.uuid2());
		
		return path1 + "/" + path2;
	}

	@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results search(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Invoice> spc) {
		Page<Invoice> invoices = invoiceService.findAll(spc, pageable);
		return new PageResponse<Invoice>(invoices);
	}

	@RequestMapping(value = "/search/userdata", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results searchuserdata(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Invoice> spc) {
		Page<Invoice> invoices = invoiceService.findAll(spc, pageable);

		Map<String, Object> userdata = invoiceService.sum(spc);

		return new PageResponse<Invoice>(invoices, userdata);
	}

	@RequestMapping(value = "/search.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody
	Results searchxml(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Invoice> spc) {
		Page<Invoice> invoices = invoiceService.findAll(spc, pageable);
		return new PageResponse<Invoice>(invoices);
	}

	@RequestMapping(value = "/item/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results itemsearch(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Item> spc,
			@RequestParam(value = "sDs_invoice", required = false) String invoice) {
		if (StringUtils.isNotEmpty(invoice)) {
			Page<Item> items = invoiceService.findAll(Item.class, spc, pageable);

			return new PageResponse<Item>(items);
		} else {
			return new PageResponse<Item>();
		}
	}

	@RequestMapping(value = "/item/search.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody
	Results itemsearchxml(@PageableDefaults(pageNumber = 0, value = 10) Pageable pageable, Specification<Item> spc,
			@RequestParam(value = "sDs_invoice", required = false) String invoice) {
		if (StringUtils.isNotEmpty(invoice)) {
			Page<Item> items = invoiceService.findAll(Item.class, spc, pageable);
			return new PageResponse<Item>(items);
		} else {
			return new PageResponse<Item>();
		}
	}

	@RequestMapping(value = "/item/findby", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	Results itemfindby(@RequestParam(value = "id", required = false) String invoice) {
		if (StringUtils.isNotEmpty(invoice)) {
			Sort sort = new Sort(Sort.Direction.ASC, "num");

			List<Item> items = invoiceService.findBy(Item.class, "invoice", sort, invoice);
			return new Response<Item>(items);
		} else {
			return new Response<Item>();
		}
	}

	@RequestMapping(value = "/item/findby.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody
	Results itemfindbyxml(@RequestParam(value = "id", required = false) String invoice) {
		if (StringUtils.isNotEmpty(invoice)) {
			Sort sort = new Sort(Sort.Direction.ASC, "num");

			List<Item> items = invoiceService.findBy(Item.class, "invoice", sort, invoice);
			return new Response<Item>(items);
		} else {
			return new Response<Item>();
		}
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	public @ResponseBody
	Results edit(@Valid @PreModelAttribute Invoice invoice,
			@RequestParam(value = "oper", required = false) String oper) {

		if (StringUtils.equals("del", oper)) {
			this.invoiceService.delete(invoice);
		} else {
			if (StringUtils.equals("add", oper)) {
				invoice.setId(null);
			}
			invoiceService.save(invoice);
		}

		return new Results(true, invoice);
	}

	@RequestMapping("shipviaSelect")
	public String shipviaSelect(Model model) {
		model.addAttribute("shipviaSelect", shipviaMap);
		return "version35/shipviaSelect";
	}

}
