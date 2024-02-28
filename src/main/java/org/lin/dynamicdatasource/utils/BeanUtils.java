package org.lin.dynamicdatasource.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class BeanUtils {
    private BeanUtils() {
    }

    /**
     * 获取到对象中值为null的属性名称
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 获取到对象中值不为null的属性名称
     * @param source
     * @return
     */
    public static String[] getNotNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> notEmptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue != null) {
                notEmptyNames.add(pd.getName());
            }
        }
        String[] result = new String[notEmptyNames.size()];
        return notEmptyNames.toArray(result);
    }

    /**
     * Bean属性复制工具方法（忽略src空属性）
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * Bean属性复制工具方法（只copy target空属性）
     * @param src
     * @param target
     */
    public static void copyPropertiesOnlyNull(Object src, Object target) {
        copyProperties(src, target, getNotNullPropertyNames(target));
    }

    /**
     * Bean属性复制工具方法
     * @param src
     * @param target
     */
    public static void copyProperties(Object src, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(src, target);
    }

    /**
     * Bean属性复制工具方法
     * @param src
     * @param target
     * @param ignoreProperties 忽略字段
     */
    public static void copyProperties(Object src, Object target, String... ignoreProperties) {
        org.springframework.beans.BeanUtils.copyProperties(src, target, ignoreProperties);
    }
}

