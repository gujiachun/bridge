package com.rainbow.bridge.targetcore.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 模板工具类
 *@author gujiachun
*/
public class TemplateUtil {

    /**
     * 执行模板引擎
     *@author gujiachun
     *@date 2021/9/14 6:37 下午
     *@param configuration
     *@param templateValue
     *@param data
     *@return java.lang.String
    */
    public static String processTemplate(Configuration configuration, String templateValue, Map<String, Object> data) throws IOException, TemplateException {

        StringWriter stringWriter = new StringWriter();
        Template template = new Template("temp", templateValue, configuration);
        template.process(data, stringWriter);
        String s = stringWriter.toString();
        stringWriter.close();

        return s;
    }

}
