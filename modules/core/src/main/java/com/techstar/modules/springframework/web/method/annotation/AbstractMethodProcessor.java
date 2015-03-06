package com.techstar.modules.springframework.web.method.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.techstar.modules.commons.beanutils.PropertyUtils;
import com.techstar.modules.springframework.core.annotation.AnnotationUtils;
import com.techstar.modules.springframework.data.jpa.domain.support.DateConverter;

public abstract class AbstractMethodProcessor implements HandlerMethodArgumentResolver,
		HandlerMethodReturnValueHandler, ApplicationContextAware {

	private static final DateConverter DATECONVERTER = new DateConverter();
	protected static final String[] EMPTYIDS = new String[] { "new_row", "_empty" };

	protected EntityManager em;
	protected final boolean annotationNotRequired;

	private Repositories repositories;

	public AbstractMethodProcessor() {
		this.annotationNotRequired = false;
	}

	public AbstractMethodProcessor(boolean annotationNotRequired) {
		this.annotationNotRequired = annotationNotRequired;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		em = entityManagerFactory.createEntityManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext context) {
		this.repositories = new Repositories(context);
	}

	public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {

		String name = ModelFactory.getNameForParameter(parameter);
		Object target = (mavContainer.containsAttribute(name)) ? mavContainer.getModel().get(name) : createAttribute(
				name, parameter, binderFactory, request);

		target = preLoad(parameter, request, target);

		WebDataBinder binder = binderFactory.createBinder(request, target, name);

		resolveArgument(parameter, binder, request);

		mavContainer.addAllAttributes(binder.getBindingResult().getModel());
		return binder.getTarget();
	}

	/**
	 * Extension point to create the model attribute if not found in the model.
	 * The default implementation uses the default constructor.
	 * 
	 * @param attributeName
	 *            the name of the attribute, never {@code null}
	 * @param parameter
	 *            the method parameter
	 * @param binderFactory
	 *            for creating WebDataBinder instance
	 * @param request
	 *            the current request
	 * @return the created model attribute, never {@code null}
	 */
	protected Object createAttribute(String attributeName, MethodParameter parameter,
			WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {

		return BeanUtils.instantiateClass(parameter.getParameterType());
	}

	/**
	 * Extension point to bind the request to the target object.
	 * 
	 * @param binder
	 *            the data binder instance to use for the binding
	 * @param request
	 *            the current request
	 */
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
		((ServletRequestDataBinder) binder).bind(servletRequest);
	}

	/**
	 * Validate the model attribute if applicable.
	 * <p>
	 * The default implementation checks for {@code @javax.validation.Valid}.
	 * 
	 * @param binder
	 *            the DataBinder to be used
	 * @param parameter
	 *            the method parameter
	 */
	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation annot : annotations) {
			if (annot.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = AnnotationUtils.getValue(annot);
				binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] { hints });
			}
		}
	}

	/**
	 * Whether to raise a {@link BindException} on bind or validation errors.
	 * The default implementation returns {@code true} if the next method
	 * argument is not of type {@link Errors}.
	 * 
	 * @param binder
	 *            the data binder used to perform data binding
	 * @param parameter
	 *            the method argument
	 */
	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
		int i = parameter.getParameterIndex();
		Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));

		return !hasBindingResult;
	}

	/**
	 * Return {@code true} if there is a method-level {@code @ModelAttribute} or
	 * if it is a non-simple type when {@code annotationNotRequired=true}.
	 */
	public boolean supportsReturnType(MethodParameter returnType) {
		if (returnType.getMethodAnnotation(ModelAttribute.class) != null) {
			return true;
		} else if (this.annotationNotRequired) {
			return !BeanUtils.isSimpleProperty(returnType.getParameterType());
		} else {
			return false;
		}
	}

	/**
	 * Add non-null return values to the {@link ModelAndViewContainer}.
	 */
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {

		if (returnValue != null) {
			String name = ModelFactory.getNameForReturnValue(returnValue, returnType);
			mavContainer.addAttribute(name, returnValue);
		}
	}

	/**
	 * 转换字符串类型到clazz的property类型的值.
	 * 
	 * @param value
	 *            待转换的字符串
	 * @param clazz
	 *            提供类型信息的Class
	 */
	@SuppressWarnings("unchecked")
	protected <E extends Serializable> E convertValue(String value, Class<E> toType) {
		ConvertUtils.register(DATECONVERTER, Date.class);
		Object obj = ConvertUtils.convert(value, toType);
		return obj == null ? null : (E) obj;
	}

	/**
	 * 实现 Preparable二次部分绑定的效果,先根据form的id从数据库查出实体对象,再把Form提交的内容绑定到该对象上
	 * 
	 * @param parameter
	 * @param request
	 * @param target
	 */
	protected abstract Object preLoad(final MethodParameter parameter, final NativeWebRequest request,
			final Object target);

	protected void resolveArgument(final MethodParameter parameter, final WebDataBinder binder,
			final NativeWebRequest request) throws Exception {

	}

	protected Object findOne(final String idAttributeName, final Object target, final NativeWebRequest request) {
		if (StringUtils.isNotEmpty(idAttributeName)) {
			ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
			// 取得主建参数值
			String idAttributeValue = servletRequest.getParameter(idAttributeName);
			// 如果主键存在，根据主键查询
			if (StringUtils.isNotEmpty(idAttributeValue) && !ArrayUtils.contains(EMPTYIDS, idAttributeValue)
					&& !StringUtils.startsWith(idAttributeValue, "jqg")) {
				RepositoryInformation info = repositories.getRepositoryInformationFor(target.getClass());
				if(info == null){
					throw new RuntimeException("没有找到"+target.getClass().getName()+"对应的Repository（Dao）实例");
				}
				CrudRepository<?, Serializable> repository = repositories.getRepositoryFor(target.getClass());
				Serializable serializable = convertValue(idAttributeValue, info.getIdType());
				return repository.findOne(serializable);
			}

			if (ArrayUtils.contains(EMPTYIDS, idAttributeValue) || StringUtils.startsWith(idAttributeValue, "jqg")) {
				try {
					PropertyUtils.setProperty(target, idAttributeName, null);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e ) {					
					//e.printStackTrace();
				} 
			}
		}
		return target;
	}

}
