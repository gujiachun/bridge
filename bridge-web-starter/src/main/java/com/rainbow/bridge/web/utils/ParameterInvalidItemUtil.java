package com.rainbow.bridge.web.utils;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

/**
 * 无效参数获取
 * @author gujiachun
 */
public class ParameterInvalidItemUtil {
	
	public static <T> List<ParameterInvalidItem> convertCVSetObjectToParameterInvalidItemList(Set<ConstraintViolation<T>> cvset) {
        if (CollectionUtils.isEmpty(cvset)) {
            return null;
        }

        List<ParameterInvalidItem> parameterInvalidItemList = Lists.newArrayList();
        for (ConstraintViolation<?> cv : cvset) {
            ParameterInvalidItem parameterInvalidItem = new ParameterInvalidItem();
            String propertyPath = cv.getPropertyPath().toString();
            if (propertyPath.indexOf(".") != -1) {
                String[] propertyPathArr = propertyPath.split("\\.");
                parameterInvalidItem.setFieldName(propertyPathArr[propertyPathArr.length - 1]);
            } else {
                parameterInvalidItem.setFieldName(propertyPath);
            }
            parameterInvalidItem.setMessage(cv.getMessage());
            parameterInvalidItemList.add(parameterInvalidItem);
        }

        return parameterInvalidItemList;
    }

    public static List<ParameterInvalidItem> convertCVSetToParameterInvalidItemList(Set<ConstraintViolation<?>> cvset) {
        if (CollectionUtils.isEmpty(cvset)) {
            return null;
        }

        List<ParameterInvalidItem> parameterInvalidItemList = Lists.newArrayList();
        for (ConstraintViolation<?> cv : cvset) {
            ParameterInvalidItem parameterInvalidItem = new ParameterInvalidItem();
            String propertyPath = cv.getPropertyPath().toString();
            if (propertyPath.indexOf(".") != -1) {
                String[] propertyPathArr = propertyPath.split("\\.");
                parameterInvalidItem.setFieldName(propertyPathArr[propertyPathArr.length - 1]);
            } else {
                parameterInvalidItem.setFieldName(propertyPath);
            }
            parameterInvalidItem.setMessage(cv.getMessage());
            parameterInvalidItemList.add(parameterInvalidItem);
        }

        return parameterInvalidItemList;
    }

    public static List<ParameterInvalidItem> convertBindingResultToMapParameterInvalidItemList(BindingResult bindingResult) {
        if (bindingResult == null) {
            return null;
        }

        List<ParameterInvalidItem> parameterInvalidItemList = Lists.newArrayList();

        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            ParameterInvalidItem parameterInvalidItem = new ParameterInvalidItem();
            parameterInvalidItem.setFieldName(fieldError.getField());
            parameterInvalidItem.setMessage(fieldError.getDefaultMessage());
            parameterInvalidItemList.add(parameterInvalidItem);
        }

        return parameterInvalidItemList;
    }

}
