package com.techstar.modules.activiti.engine.impl.form;

import java.util.LinkedHashMap;
import java.util.Map;

import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.FormValue;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.form.FormTypes;

import com.techstar.modules.activiti.form.CheckboxFormType;
import com.techstar.modules.activiti.form.MultipleSelectFormType;
import com.techstar.modules.activiti.form.RadioFormType;
import com.techstar.modules.activiti.form.SelectFormType;

public class CustomFormTypes extends FormTypes {

	@Override
	public AbstractFormType parseFormPropertyType(FormProperty formProperty) {
		AbstractFormType formType = null;

		if (RadioFormType.TYPE_NAME.equals(formProperty.getType())) {
			formType = new RadioFormType(getValues(formProperty));
		} else if (MultipleSelectFormType.TYPE_NAME.equals(formProperty.getType())) {
			formType = new MultipleSelectFormType(getValues(formProperty));
		} else if (CheckboxFormType.TYPE_NAME.equals(formProperty.getType())) {
			formType = new CheckboxFormType(getValues(formProperty));
		} else if (SelectFormType.TYPE_NAME.equals(formProperty.getType())) {
			formType = new SelectFormType(getValues(formProperty));
		} else {
			formType = super.parseFormPropertyType(formProperty);
		}

		return formType;
	}

	private Map<String, String> getValues(final FormProperty formProperty) {
		Map<String, String> values = new LinkedHashMap<String, String>();
		for (FormValue formValue : formProperty.getFormValues()) {
			values.put(formValue.getId(), formValue.getName());
		}

		return values;
	}
}
