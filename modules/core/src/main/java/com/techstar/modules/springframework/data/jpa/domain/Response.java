package com.techstar.modules.springframework.data.jpa.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
public class Response<T> extends Results {

	private List<T> content;

	public Response() {

	}

	public Response(List<T> content) {
		this.content = content;
	}

	public Response(List<T> content, Object userdata) {
		super(userdata);
		this.content = content;
	}

	@XmlElement
	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
