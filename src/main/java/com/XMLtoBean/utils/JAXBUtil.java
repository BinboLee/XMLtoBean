package com.XMLtoBean.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;

/**
 * JAXB XML转Java对象工具类
 * 原生JDK，无第三方依赖，内存友好，中大报文友好
 */
public class JAXBUtil {

    public static <T> T toBean(String xml, Class<T> clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            return (T) context.createUnmarshaller().unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new RuntimeException("XML转Java对象失败", e);
        }
    }
}