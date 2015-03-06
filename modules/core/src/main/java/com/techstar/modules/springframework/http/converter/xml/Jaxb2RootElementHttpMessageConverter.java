package com.techstar.modules.springframework.http.converter.xml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.techstar.modules.springframework.util.ReflectionUtils;

public class Jaxb2RootElementHttpMessageConverter extends
		org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter {

	private final ConcurrentMap<String, JAXBContext> jaxbContexts = new ConcurrentHashMap<String, JAXBContext>();

	protected final Marshaller createMarshaller(Class<?>... clazz) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createMarshaller();
		} catch (JAXBException ex) {
			throw new HttpMessageConversionException("Could not create Marshaller for class [" + clazz + "]: "
					+ ex.getMessage(), ex);
		}
	}

	/**
	 * Creates a new {@link Unmarshaller} for the given class.
	 * 
	 * @param clazz
	 *            the class to create the unmarshaller for
	 * @return the {@code Unmarshaller}
	 * @throws HttpMessageConversionException
	 *             in case of JAXB errors
	 */
	protected final Unmarshaller createUnmarshaller(Class<?>... clazz) throws JAXBException {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException ex) {
			throw new HttpMessageConversionException("Could not create Unmarshaller for class [" + clazz + "]: "
					+ ex.getMessage(), ex);
		}
	}

	protected final JAXBContext getJaxbContext(Class<?>... clazzs) {
		//Assert.notNull(clazzs, "'clazzs' must not be null");
		Assert.notEmpty(clazzs,"'clazzs' must not be empty");
		
		String key = Arrays.asList(clazzs).toString();
		JAXBContext jaxbContext = jaxbContexts.get(key);
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(clazzs);				
				jaxbContexts.putIfAbsent(key, jaxbContext);				
			} catch (JAXBException ex) {
				throw new HttpMessageConversionException("Could not instantiate JAXBContext for class [" + clazzs
						+ "]: " + ex.getMessage(), ex);
			}
		}
		return jaxbContext;
	}

	@Override
	protected void writeToResult(Object o, HttpHeaders headers, Result result) {

		Set<Class<?>> clazzs = new LinkedHashSet<Class<?>>();
		Class<?> clazz = ClassUtils.getUserClass(o);
		clazzs.add(clazz);

		try {
			Marshaller marshaller = createMarshaller(getClazzs(o, clazzs, clazz));
			setCharset(headers.getContentType(), marshaller);
			marshaller.marshal(o, result);
		} catch (MarshalException ex) {
			throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
		} catch (JAXBException ex) {
			throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}
	}

	private Class<?>[] getClazzs(Object o, Set<Class<?>> clazzs, Class<?> clazz) {

		try {
			if (o != null) {
				Field[] fields = ReflectionUtils.getAllDeclaredFields(clazz);
				for (Field field : fields) {
					boolean b = isPrimitiveOrWrapper(field.getType());
					if (!b) {
						if (Collection.class.isAssignableFrom(field.getType())) {
							Object collection = PropertyUtils.getProperty(o, field.getName());
							if (collection != null && CollectionUtils.isNotEmpty((Collection<?>) collection)) {
								Object value = ((Collection<?>) collection).iterator().next();
								if (value != null && !ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
									clazzs.add(value.getClass());
									// 递归
									getClazzs(value, clazzs, field.getType());
								}
							}
						} else if (field.getType().isArray()) {
							Object array = PropertyUtils.getProperty(o, field.getName());
							if (array != null) {
								Object[] values = ((Object[]) array);
								if (ArrayUtils.isNotEmpty(values)
										&& !ClassUtils.isPrimitiveOrWrapper(values[0].getClass())) {
									clazzs.add(values[0].getClass());
									// 递归
									getClazzs(values[0], clazzs, field.getType());
								}
							}
						} else {
							Object object = PropertyUtils.getProperty(o, field.getName());
							if (object != null) {
								clazzs.add(field.getType());
								// 递归
								getClazzs(object, clazzs, field.getType());
							}
						}
					}
				}
			}

		} catch (IllegalAccessException ex) {
			throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		} catch (InvocationTargetException ex) {
			throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		} catch (NoSuchMethodException ex) {
			throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}

		return clazzs.toArray(new Class[clazzs.size()]);
	}

	private boolean isPrimitiveOrWrapper(Class<?> clazz) {
		return ClassUtils.isPrimitiveOrWrapper(clazz) && String.class.isAssignableFrom(clazz)
				&& Date.class.isAssignableFrom(clazz) && ClassUtils.isCglibProxyClass(clazz);
	}

	private void setCharset(MediaType contentType, Marshaller marshaller) throws PropertyException {
		if (contentType != null && contentType.getCharSet() != null) {
			marshaller.setProperty(Marshaller.JAXB_ENCODING, contentType.getCharSet().name());
		}
	}
}
