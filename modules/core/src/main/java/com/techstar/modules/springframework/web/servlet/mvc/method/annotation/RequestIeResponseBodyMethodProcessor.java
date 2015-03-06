package com.techstar.modules.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Source;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.ClassUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.techstar.modules.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import com.techstar.modules.springframework.web.bind.annotation.IeResponseBody;

/**
 * <p>
 * json的mime为：application/json
 * </p>
 * <p>
 * ie6、７、８不支持application/json,一解析认为是文件，提示下载
 * </p>
 * <p>
 * 将mime改为text/plain、text/html、text/json
 * </p>
 * 
 * @author sundoctor
 * 
 */
public class RequestIeResponseBodyMethodProcessor extends RequestResponseBodyMethodProcessor {

	private static final Logger logger = LoggerFactory.getLogger(RequestIeResponseBodyMethodProcessor.class);

	private static final MediaType DEFAULTMEDIATYPE = new MediaType("text", "plain",
			MappingJackson2HttpMessageConverter.DEFAULT_CHARSET);

	private MediaType selectedMediaType = DEFAULTMEDIATYPE;

	private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder",
			RequestIeResponseBodyMethodProcessor.class.getClassLoader());

	private static final boolean jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper",
			RequestIeResponseBodyMethodProcessor.class.getClassLoader())
			&& ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
					RequestIeResponseBodyMethodProcessor.class.getClassLoader());

	private static final boolean jacksonPresent = ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper",
			RequestIeResponseBodyMethodProcessor.class.getClassLoader())
			&& ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator",
					RequestIeResponseBodyMethodProcessor.class.getClassLoader());

	private static boolean romePresent = ClassUtils.isPresent("com.sun.syndication.feed.WireFeed",
			RequestIeResponseBodyMethodProcessor.class.getClassLoader());

	public RequestIeResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
		addDefaultHttpMessageConverters(messageConverters);

	}

	public RequestIeResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters,
			ContentNegotiationManager contentNegotiationManager) {
		super(messageConverters, contentNegotiationManager);
		addDefaultHttpMessageConverters(messageConverters);
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getMethodAnnotation(IeResponseBody.class) != null;
	}

	public void setSelectedMediaType(MediaType selectedMediaType) {
		this.selectedMediaType = selectedMediaType;
	}

	protected final void addDefaultHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {

		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(
				MappingJackson2HttpMessageConverter.DEFAULT_CHARSET);
		stringHttpMessageConverter.setWriteAcceptCharset(false);
		messageConverters.add(stringHttpMessageConverter);

		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new AllEncompassingFormHttpMessageConverter());
		messageConverters.add(new SourceHttpMessageConverter<Source>());

		if (romePresent) {
			messageConverters.add(new AtomFeedHttpMessageConverter());
			messageConverters.add(new RssChannelHttpMessageConverter());
		}
		if (jaxb2Present) {
			messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		}
		if (jackson2Present) {

			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

			List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
			supportedMediaTypes.addAll(converter.getSupportedMediaTypes());

			supportedMediaTypes.add(MediaType.TEXT_HTML);
			supportedMediaTypes.add(MediaType.TEXT_PLAIN);
			supportedMediaTypes.add(new MediaType("text", "json"));
			supportedMediaTypes.add(new MediaType("text", "*+json"));
			supportedMediaTypes.add(new MediaType("text", "html", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET));
			supportedMediaTypes
					.add(new MediaType("text", "plain", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET));
			supportedMediaTypes.add(new MediaType("text", "json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET));
			supportedMediaTypes
					.add(new MediaType("text", "*+json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET));
			converter.setSupportedMediaTypes(supportedMediaTypes);

			messageConverters.add(converter);
		} else if (jacksonPresent) {
			messageConverters.add(new MappingJacksonHttpMessageConverter());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType,
			ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) throws IOException,
			HttpMediaTypeNotAcceptableException {

		HttpServletRequest servletRequest = inputMessage.getServletRequest();
		String userAgent = servletRequest.getHeader("User-Agent");
		double ieversion = 9;
		boolean isie = StringUtils.contains(userAgent, "MSIE");
		if (isie) {
			ieversion = NumberUtils.toDouble(StringUtils.trim(StringUtils.substringBetween(userAgent,"MSIE", ";")));
			logger.info("是否IE:{},IE版本:{}", isie, ieversion);
		}

		if (isie && ieversion < 9) {
			// ie6、ie7、ie8
			Class<?> returnValueClass = returnValue.getClass();

			if (selectedMediaType != null) {
				selectedMediaType = selectedMediaType.removeQualityValue();
				for (HttpMessageConverter<?> messageConverter : messageConverters) {
					if (messageConverter.canWrite(returnValueClass, selectedMediaType)) {
						((HttpMessageConverter<T>) messageConverter).write(returnValue, selectedMediaType,
								outputMessage);
						if (logger.isDebugEnabled()) {
							logger.debug("Written [" + returnValue + "] as \"" + selectedMediaType + "\" using ["
									+ messageConverter + "]");
						}
						return;
					}
				}
			}
			throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
		} else {
			super.writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
		}
	}

}
