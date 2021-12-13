package com.rainbow.bridge.targetcore.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 模板工具类
 *@author gujiachun
*/
public class TemplateUtil {

    protected static final Logger logger = LoggerFactory.getLogger(TemplateUtil.class);

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

        if (StringUtils.isBlank(templateValue)){
            return null;
        }

        if (templateValue.indexOf("${") < 0){
            return templateValue;
        }

        StringWriter stringWriter = new StringWriter();
        Template template = new Template("temp", templateValue, configuration);
        String s;
        try {
            template.process(data, stringWriter);
            s = stringWriter.toString();
        }catch (Exception e){
            logger.error("freemarker 解析异常,temp:{};有可能不存在此属性值",templateValue);
            s = null;
        }finally {
            stringWriter.close();
        }
        return s;
    }

}
