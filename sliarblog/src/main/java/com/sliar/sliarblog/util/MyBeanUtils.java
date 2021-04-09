package com.sliar.sliarblog.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class MyBeanUtils {
    /**
     * 获取所有的属性值为空属性名数组
     * @param source
     * @return
     */
    public static String[] getNullProperyNames(Object source){
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        List<String> nullProperyNames = new ArrayList<>();
        for (PropertyDescriptor pd : pds){
            String propertyName = pd.getName();
            if (beanWrapper.getPropertyValue(propertyName) == null){
                nullProperyNames.add(propertyName);
            }
        }
        return nullProperyNames.toArray(new String[nullProperyNames.size()]);
    }
}
