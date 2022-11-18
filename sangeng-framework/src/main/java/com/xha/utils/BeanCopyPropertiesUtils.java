package com.xha.utils;

import com.xha.domain.entity.Menu;
import com.xha.domain.vo.RoutersVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyPropertiesUtils {

    private BeanCopyPropertiesUtils(){
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
//        创建目标对象
        V result = null;
        try {
//            通过反射创建新的类
            result = clazz.newInstance();
            //        实现属性拷贝
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz) {
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

}
